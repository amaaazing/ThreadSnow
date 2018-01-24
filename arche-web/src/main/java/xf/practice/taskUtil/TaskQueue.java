package xf.practice.taskUtil;

public interface TaskQueue {

     void push(TaskJob job);

     JobStatus getStatus(String taskId);

     void tryConsume();

}
