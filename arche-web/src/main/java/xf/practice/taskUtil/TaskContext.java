package xf.practice.taskUtil;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * JobTask队列上下文
 *
 *  //此对象只创建一个
    private TaskContext context = TaskContextFactory.create();
 *
 * TaskJob job = this.context.newJob(filename, this.samClubDataImportTaskService);
 *
 */

public class TaskContext implements TaskQueue {

    /**
     *任务队列
     */
    private Queue<TaskJob> jobQueue;

    /**
     * 状态队列
     */
    private Map<String, JobStatus> jobStatus;

    /*
     * 执行标记  true表示正在执行  false当前未执行
     */
    public AtomicBoolean executeflag;

    public TaskContext() {

        /*
         * ConcurrentLinkedQueue线程安全队列
         */
        jobQueue = new ConcurrentLinkedQueue<TaskJob>();
        jobStatus = new ConcurrentHashMap<String, JobStatus>();

        executeflag = new AtomicBoolean(false);
    }

    /**
     * 向队列新增一个任务
     *
     */
    @Override
    public synchronized void push(TaskJob job) {

        /*
         * 判断是否已经把上传的文件保存了临时文件
         * 并且已经把该临时文件赋值给TaskJob#setTemFile()
         * 执行TaskJob需要使用该文件，所以此文件必须存在
         */
        if (job.getTemFile() == null) {
            try {
                throw new FileNotFoundException("临时文件不存在");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        /*
         * 注意重点：job.getJobStatus()
         * add job的属性jobstatus是后 队列中和taskjob中的jobstatus为同一个引用对象
         * 当在taskjob中对jobstatus进行变化后 队列中会同时改变
         * 队列中的jobstatus是用于把taskjob的当前状态返回给页面
         */
        jobStatus.put(job.getTaskId(), job.getJobStatus());

        /*
         * 把taskjob添加到队列中进行排队
         */
        this.jobQueue.add(job);

        /*
         * 添加到队列中后尝试执行一次消费
         */
        tryConsume();
    }

    /**
     * 尝试执行taskjob队列消费
     * 如果当前没有taskjob在执行 则试图执行一次消息
     *
     * @see
     */
    public void tryConsume() {

        //executeflag.get()为true时说明当前有队列正在消费
        //此时不用做任何处理,因为队列消费完毕后会自动执行下一次消费
        if (!executeflag.get()) {

            //执行消费
            consume();
        }
    }

    /**
     *
     * @description 消费Taskjob队列
     */
    private void consume() {

        //从队列中获取一个Job
        TaskJob job = jobQueue.poll();

        if (job != null) {

            //执行之前先把执行状态改成true
            //意味着当前已经有taskjob正在执行
            executeflag.set(true);

            /*
             * TaskJob实现了Rannable,使用线程的方法调用
             */
            new Thread(job).start();
        }
    }


    /**
     *
     * @description TaskJob执行完毕后会调用此方法，无论taskjob的业务处理是否成功
     *
     */
    public void notifyJob() {

        /*设置当前执行标记为未执行*/
        executeflag.set(false);

        /*
         *再次尝试执行一次消费
         */
        tryConsume();
    }

    /**
     * 获取taskid对应的taskjob的执行状态
     * 并且从jobStatus中自动移除已经结束的状态
     * 状态值2和4为结束状态
     *
     */
    @Override
    public synchronized JobStatus getStatus(String taskId) {

        JobStatus status = this.jobStatus.get(taskId);

        /*
         *当状态已完成状态之后（status=2或者4）
         *自动从jobStatus移除状态
         */
        if (status != null) {
            if (status.getStatus() == JobStatus.JOB_STATUS_DONE_SUCCESS || status.getStatus() == JobStatus.JOB_STATUS_EXCEPTION) {
                jobStatus.remove(taskId);
            }
        }
        return status;
    }

    /**
     *
     * @description 创建一个新的TaskJob
     * @param filename
     * @param hedwigService
     * @return
     */
    public TaskJob newJob(SamDataImportFilename filename, SamClubDataImportTaskService hedwigService) {

        if (filename == SamDataImportFilename.NEW) {
            return new MtrackNewTaskJob(this, hedwigService);
        } else if (filename == SamDataImportFilename.ADV) {
            return new MtrackAdvTaskJob(this, hedwigService);
        } else {
            return new MtrackBmTaskJob(this, hedwigService);
        }
    }

}

