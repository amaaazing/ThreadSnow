package xf.utility;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * 配置
 *	<bean id="executorServiceFactory"
 class="com.....ExecutorServiceFactory">
 <property name="corePoolSizeGroupon"
 value="${corePoolSize-GrouponFacadeHandleImpl-groupon}" />
 <property name="maxPoolSizeGroupon"
 value="${maxPoolSize-GrouponFacadeHandleImpl-groupon}" />
 <property name="corePoolSizeQiang"
 value="${corePoolSize-GrouponFacadeHandleImpl-qianggou}" />
 <property name="maxPoolSizeQiang"
 value="${maxPoolSize-GrouponFacadeHandleImpl-qianggou}" />
 </bean>
 *
 * 在Sevrive层使用
 * @Resource
private ExecutorServiceFactory executorServiceFactory;
 *
 *
try{
ExecutorService executor = gexecutorServiceFactory;
List<Future<List<GrouponFacadeOut>>> futureList = new ArrayList<Future<List<GrouponFacadeOut>>>(
channelId2PmInfoIdList.size());
for (Map.Entry<Long, List<Long>> entry : channelId2PmInfoIdList.entrySet()) {
busyCallable = new BusyGrouponCallable(entry.getKey(), provinceId, entry.getValue(), pmId2Groupon);
Future<List<GrouponFacadeOut>> futureTask = executor.submit(busyCallable);
futureList.add(futureTask);
}
for (Future<List<GrouponFacadeOut>> futureTask : futureList) {
grouponFacadeOutList.addAll(futureTask.get());
}
} catch (Exception e) {
log.error("buildBSGrouponVo is exception...", e);
}
 *
 *
 */
public class ExecutorServiceFactory implements InitializingBean, DisposableBean {

    private ExecutorService grouponExecutor;
    private ExecutorService qianggouExecutor;

    private int corePoolSizeGroupon;
    private int maxPoolSizeGroupon;

    private int corePoolSizeQiang;
    private int maxPoolSizeQiang;

    private final static Log log = LogFactory.getLog(ExecutorServiceFactory.class);

    @Override
    public void destroy() throws Exception {
        try {
            if (!grouponExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                grouponExecutor.shutdownNow();
            }

            if (!qianggouExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                qianggouExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("threadpool : " + "close." + " message: " + e.getMessage());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        /**
         * 团购
         */
        String grouponThFactoryName = "GrouponFacadeHandleImpl-groupon-%d";
        ThreadFactory groupopnThFactory = new ThreadFactoryBuilder().setNameFormat(grouponThFactoryName).build();
        // corePoolSizeGroupon=25,maxPoolSizeGroupon=30
        grouponExecutor = new ThreadPoolExecutor(corePoolSizeGroupon, maxPoolSizeGroupon, 1L, TimeUnit.HOURS,
                new ArrayBlockingQueue<Runnable>(20), groupopnThFactory, new ThreadPoolExecutor.CallerRunsPolicy());
        ThreadPoolMonitor.getInstance().register(grouponThFactoryName, (ThreadPoolExecutor) grouponExecutor);

        /**
         * 抢购
         */
        // corePoolSizeQiang=25,maxPoolSizeQiang=30
        String qianggouThFactoryName = "GrouponFacadeHandleImpl-qianggou-%d";
        ThreadFactory qianggouThFactory = new ThreadFactoryBuilder().setNameFormat(qianggouThFactoryName).build();
        qianggouExecutor = new ThreadPoolExecutor(corePoolSizeQiang, maxPoolSizeQiang, 1L, TimeUnit.HOURS,
                new ArrayBlockingQueue<Runnable>(20), qianggouThFactory, new ThreadPoolExecutor.CallerRunsPolicy());
        ThreadPoolMonitor.getInstance().register(qianggouThFactoryName, (ThreadPoolExecutor) qianggouExecutor);
    }

    public ExecutorService getGrouponExecutor() {
        return grouponExecutor;
    }

    public ExecutorService getQianggouExecutor() {
        return qianggouExecutor;
    }

    public void setCorePoolSizeGroupon(int corePoolSizeGroupon) {
        this.corePoolSizeGroupon = corePoolSizeGroupon;
    }

    public void setMaxPoolSizeGroupon(int maxPoolSizeGroupon) {
        this.maxPoolSizeGroupon = maxPoolSizeGroupon;
    }

    public void setCorePoolSizeQiang(int corePoolSizeQiang) {
        this.corePoolSizeQiang = corePoolSizeQiang;
    }

    public void setMaxPoolSizeQiang(int maxPoolSizeQiang) {
        this.maxPoolSizeQiang = maxPoolSizeQiang;
    }


}

