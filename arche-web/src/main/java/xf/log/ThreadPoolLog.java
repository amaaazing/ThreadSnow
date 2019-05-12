package xf.log;

import java.io.Serializable;
import java.util.Date;


public class ThreadPoolLog implements Serializable {

    private static final long serialVersionUID = 912078197053968440L;

    private Long id;
    private String appPoolName;
    private String threadPoolName;
    private String ip;
    private int maxPoolSize;
    private int poolSize;
    private int largestPoolSize;
    private int activeThreadCnt;
    private int queueSize;
    private long completedTaskCnt;
    private Date createTime;

    public ThreadPoolLog() {
        createTime = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppPoolName() {
        return appPoolName;
    }

    public void setAppPoolName(String appPoolName) {
        this.appPoolName = appPoolName;
    }

    public String getThreadPoolName() {
        return threadPoolName;
    }

    public void setThreadPoolName(String threadPoolName) {
        this.threadPoolName = threadPoolName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getLargestPoolSize() {
        return largestPoolSize;
    }

    public void setLargestPoolSize(int largestPoolSize) {
        this.largestPoolSize = largestPoolSize;
    }

    public int getActiveThreadCnt() {
        return activeThreadCnt;
    }

    public void setActiveThreadCnt(int activeThreadCnt) {
        this.activeThreadCnt = activeThreadCnt;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public long getCompletedTaskCnt() {
        return completedTaskCnt;
    }

    public void setCompletedTaskCnt(long completedTaskCnt) {
        this.completedTaskCnt = completedTaskCnt;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}

