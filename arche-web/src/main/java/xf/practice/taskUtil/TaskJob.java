package xf.practice.taskUtil;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public abstract class TaskJob extends JobIdentity implements Runnable {

    protected Logger logger = LoggerFactory.getLogger(TaskJob.class);

    /**
     * TaskContext上下文
     */
    private TaskContext                    context;

    /**
     *task的执行状态
     *状态为 排队中、正在处理、处理成功、处理异常几个状态
     */
    protected JobStatus                    jobStatus;

    /**
     * 上传文件后的保存在web容器目录的临时原文件
     * 并且未经过任何加密解密处理
     */
    protected File temFile;

    /**
     * 执行数据库操作的hedwig服务接口
     * 由badge-server提供
     * 当文件解析和上传YDFS成功后会执行数据库操作，届时会使用到该类
     * 该类严格依赖spring容器，可以直接从注入的类里传递
     */
    protected SamClubDataImportTaskService hedwigService;

    /**
     * 当前上传文件的文件名枚举类
     */
    protected SamDataImportFilename        filename;

    /**
     * 构造Job所需基础信息
     * @param context task上下文
     * @param hedwigService hedwig接口服务类
     */
    public TaskJob(TaskContext context, SamClubDataImportTaskService hedwigService) {
        this.context = context;
        this.taskId = UUIDGenerator.getUUID();
        this.jobStatus = new JobStatus(this);
        this.hedwigService = hedwigService;
    }

    /**
     * 每一个TaskJob都是一个线程，此方法会自动由TaskContext触发
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {

            /*
             * 业务处理
             */
            dotask();

            /*
             * 当业务处理成功后，会把本次TaskJob的状态设置为成功状态
             * 此对象jobStatus的和TaskContext的队列statusJob中的对象为同一个对象（引用传递）
             * 所以此时更改status的值后 会直接影响TaskContext#getStatus()被调用时的结果
             */
            jobStatus.setStatus(JobStatus.JOB_STATUS_DONE_SUCCESS);

        } catch (Exception e) {
            /*
             * 当dotask()处理抛出异常后会执行此处
             * 意味着本次taskjob业务处理已经失败
             * 同时需要把失败的信息更新到本次状态属性里
             */
            jobStatus.setErrmsg(e.getMessage());
        } finally {

            /*
             * 不论本次jobtask成功还是失败
             * 都需要通知到taskcontext
             */
            notifyContext();
        }
    }

    /**
     *
     * @description 业务处理
     * @throws Exception
     */
    public abstract void dotask() throws Exception;

    /**
     * @description 通知到taskcontext
     */
    public void notifyContext() {

        /*
         * 通知至taskcontext
         * 具体作用请查看notifyJob()的方法注释
         */
        this.context.notifyJob();

        if (this.temFile.exists() && this.temFile.delete()) {
            logger.info("---------------------------删除文件" + taskId + "成功---------------------------");
        }
        logger.info("---------------------------" + taskId + "处理完毕---------------------------");
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setTemFile(File temFile) {
        this.temFile = temFile;
    }

    public File getTemFile() {
        return temFile;
    }

    public void setFilename(SamDataImportFilename filename) {
        this.filename = filename;
    }

}
