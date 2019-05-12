package xf.utility;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.Assert;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import xf.log.ThreadPoolLog;


public class ThreadPoolMonitor implements ApplicationListener {

    @Resource
    private ThreadPoolLogDao threadPoolLogDao;

    @Resource
    private GrouponCodeConfigService grouponCodeConfigService;

    private ScheduledExecutorService executor;
    private volatile Map<String, ThreadPoolExecutor> threadPools = new ConcurrentHashMap<String, ThreadPoolExecutor>();

    // 是否已启动
    private volatile AtomicBoolean started = new AtomicBoolean(false);

    // 是否已停止,查看代码表，随时停用启用
    private volatile AtomicBoolean stoped = new AtomicBoolean(false);

    // Spring初始化是否已经完成
    private boolean inited = false;

    private Logger log = LoggerFactory.getLogger(ThreadPoolMonitor.class);

    private static final long DELAY = 120;// 120s监控一次

    private static final boolean isMonitorEnable = true;

    // 最多间隔5分钟写一次
    private final int MAX_WRITE_INTERVAL_MS = 1000 * 60 * 5;

    // 最多间隔50条数据写一次
    private final int MAX_WRITE_INTERVAL_CNT = 50;

    private Long lastWriteTimeMs = null;

    // 监控数据写表批量写
    private List<ThreadPoolLog> logList = new ArrayList<ThreadPoolLog>();

    public static class Holder {
        private static ThreadPoolMonitor monintor = new ThreadPoolMonitor();
    }

    private ThreadPoolMonitor() {

    }

    public static ThreadPoolMonitor getInstance() {
        return Holder.monintor;
    }

    public void close() {
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("threadpool : " + "close." + " message: " + e.getMessage());
        }
    }

    public void register(final String threadPoolName, final ThreadPoolExecutor threadPool) {
        // 若关闭监控，则直接退出
        if (!isMonitorEnable) {
            return;
        }

        Assert.isTrue(StringUtils.isNotBlank(threadPoolName));
        Assert.isTrue(threadPool != null);
        Assert.isTrue(!threadPools.containsKey(threadPoolName), "该线程池名已存在于监控列表");
        Assert.isTrue(!threadPools.containsValue(threadPool), "该线程池已存在于监控列表");

        threadPools.put(threadPoolName, threadPool);

        if (!started.getAndSet(true)) {
            startAndSchedule();
        }
    }

    private void startAndSchedule() {
        ThreadFactory thFactory = new ThreadFactoryBuilder().setNameFormat("ThreadPoolMonitor-%d").build();
        executor = Executors.newScheduledThreadPool(1, thFactory);
        executor.scheduleWithFixedDelay(newTask(), 300, DELAY, TimeUnit.SECONDS);
    }

    private Runnable newTask() {
        return new Runnable() {
            @Override
            public void run() {
                if (!inited) {
                    return;
                }

                if (Long.valueOf(1).equals(
                        grouponCodeConfigService
                                .<Long> getValue(GrouponCodeConfigService.GROUPON_THREAD_POOL_MONITOR_CLOSE))) {
                    stop();
                } else {
                    restart();
                }

                if (stoped.get()) {
                    return;
                }

                writeLogIfNeed();

                Set<Entry<String, ThreadPoolExecutor>> entrySet = threadPools.entrySet();
                if (entrySet.size() > 0) {
                    String ip = IPUtil.getFirstNoLoopbackIP4Address();
                    if (ip == null) {
                        ip = "127.0.0.1";
                    }
                    for (Entry<String, ThreadPoolExecutor> entry : entrySet) {
                        String threadPoolName = entry.getKey();
                        ThreadPoolExecutor executor = entry.getValue();

                        if (executor.isShutdown() || executor.isTerminated() || executor.isTerminating()) {
                            threadPools.remove(threadPoolName);
                            continue;
                        }

                        ThreadPoolLog log = new ThreadPoolLog();
                        log.setAppPoolName(YccGlobalPropertyConfigurer.getMainPoolId());
                        log.setThreadPoolName(threadPoolName);
                        log.setIp(ip);
                        log.setPoolSize(executor.getPoolSize());
                        log.setMaxPoolSize(executor.getMaximumPoolSize());
                        log.setLargestPoolSize(executor.getLargestPoolSize());
                        log.setActiveThreadCnt(executor.getActiveCount());
                        log.setQueueSize(executor.getQueue().size());
                        log.setCompletedTaskCnt(executor.getCompletedTaskCount());

                        logList.add(log);
                    }

                    writeLogIfNeed();
                }
            }
        };
    }

    /**
     *
     * @Description: Log数量超过了最大上限或者时间间隔到了上限，就写Log
     * @return true:写了Log,false:未写Log
     */
    private boolean writeLogIfNeed() {
        if (logList.size() >= MAX_WRITE_INTERVAL_CNT
                || (lastWriteTimeMs != null && (lastWriteTimeMs + MAX_WRITE_INTERVAL_MS) < System.currentTimeMillis())) {
            try {
                threadPoolLogDao.batchInsert(logList);
                logList.clear();
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
            lastWriteTimeMs = System.currentTimeMillis();
            return true;
        }

        return false;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        lastWriteTimeMs = System.currentTimeMillis();
        inited = true;
    }

    private void stop() {
        if (!stoped.getAndSet(true)) {
            logList.clear();
        }
    }

    private void restart() {
        if (stoped.get()) {
            stoped.getAndSet(false);
        }
    }
}

