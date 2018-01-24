package xf.practice.taskUtil;

/**
 * JOBTask所需基础信息
 */
public class JobIdentity {

    /*
 * 唯一id
 */
    protected String taskId;

    /*
     *当前登录用户id
     */
    protected Long   endUserId;

    /*
     *当前登录用户帐号
     */
    protected String realname;

    /*
     *文件上传至YDFS的下载路径
     */
    protected String downurl;

    /**
     *   由于队列TaskContext队列所需，需要重写hashcode和equals方法<br>
     *   具体用户请查看TaskContext中的jobQueue的操作
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public int hashCode() {
        return this.taskId.hashCode();
    }

    /**
     *   由于队列TaskContext队列所需，需要重写hashcode和equals方法<br>
     *   具体用户请查看TaskContext中的jobQueue的操作
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof JobIdentity) {
            return this.taskId.equals(((JobIdentity) obj).getTaskId());
        }
        return this.taskId.equals(obj);
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Long getEndUserId() {
        return endUserId;
    }

    public void setEndUserId(Long endUserId) {
        this.endUserId = endUserId;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getDownurl() {
        return downurl;
    }
}
