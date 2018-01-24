package xf.practice.taskUtil;
//JobTask执行状态
public class JobStatus extends JobIdentity{

    public static final int JOB_STATUS_WAITING      = 0;
    public static final int JOB_STATUS_DOING        = 1;
    public static final int JOB_STATUS_DONE_SUCCESS = 2;
    public static final int JOB_STATUS_EXCEPTION    = 4;

    /*
     * 状态码
     */
    private int             status                  = JOB_STATUS_WAITING;

    /*
     *发生异常时的异常信息
     */
    private String          errmsg;

    public JobStatus(JobIdentity job) {
        this.taskId = job.getTaskId();
    }

    public JobStatus() {

    }

    public JobStatus(JobIdentity job, int status) {
        this.status = status;
        this.taskId = job.getTaskId();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.status = JOB_STATUS_EXCEPTION;
        this.errmsg = errmsg;
    }

    public static JobStatus createException(String errmsg) {
        JobStatus js = new JobStatus();
        js.setStatus(JOB_STATUS_EXCEPTION);
        js.setErrmsg(errmsg);
        js.setTaskId("1");
        return js;
    }
}
