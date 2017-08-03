package xf.practice.concurrent.example;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import xf.practice.concurrent.ThreadPool;


/**
 * 作业调度程序实现：
 * 遍历全部的作业请求（存放在Vector中）
 * 并且要么将那些要运行的作业放入一个线程池中去处理
 * 要么启动一个新的线程来执行这个作业
 * ，此外我们还要算出该作业下次要运行的时间，并且等待那个时间的到达
 * 。然后这个处理过程循环往复。
 * 
 * 作业调度程序是否需要作为一个守护线程来运行呢？
 * 情况：Vector中有作业在等待，而没有线程与他们相关联，此时所有用户线程已退出
 * 如果此时作业调度程序是作为一个守护线程来配置的，它就会在还有作业等待执行的情况下退出
 * 
 * 因此，我们使作业调度程序作为一个守护线程运行。还使用了DaemonLock，保证只有在没有作业需要调度并且没有用户线程存在的情况下，作业调度程序才会退出
 * 
 * 每当作业加到调度程序中时，要获得一个守护锁；每当作业从调度程序中删除时，才释放该锁
 * 
 * 但是，因为线程池中的线程不是守护线程，所以这个方法只有在作业调度程序没有使用线程池才有用。
 * 也就是，每一个作业都是使用一个新的线程来运行的
 * 
 * 
 * 
 * 
 * @author xinzhenqiu
 *
 */
public class JobScheduler implements Runnable{

	public final static int ONCE = 1;
	public final static int FOREVER = -1;
	public final static long HOURLY = (long)60*60*1000;
	public final static long DAILY = 24*HOURLY;
	public final static long WEEKLY = 7*DAILY;
	public final static long MONTHLY = -1;
	public final static long YEARLY = -2;
	
	
	private class JobNode{
		public Runnable job;
		public Date executeAt;
		public long interval; // 执行作业的时间间隔
		public int count; // 作业要运行的次数
		/**
		 * 运行完一个作业后，还要计算是否有必要进行另一次计算及何时要执行下一次计算
		 */
	}
	
	private ThreadPool tp;
	
	private DaemonLock dlock = new DaemonLock();
	
	private Vector<JobNode> jobs = new Vector<JobNode>(100);
	
	public JobScheduler(int poolSize){
		tp = (poolSize > 0) ? new ThreadPool(poolSize) : null;
		Thread js = new Thread(this);
		js.setDaemon(true);
		js.start();		
	}
	
	private synchronized void addJob(JobNode job){
		// 当作业加到调度程序中时，要获得一个守护锁
		dlock.acquire();
		jobs.addElement(job);
		notify();		
	}
	
	private synchronized void deleteJob(Runnable job){
		for(int i = 0;i < jobs.size();i++){
			if(jobs.elementAt(i).job == job){
				// 当作业从调度程序中删除时，才释放该锁
				jobs.removeElementAt(i);
				dlock.release();
				break;
			}
		}
	}
	
	/**
	 * 作业是否需要重复执行
	 * @param jn
	 * @return
	 */
	private JobNode updateJobNode(JobNode jn){
		Calendar cal = Calendar.getInstance();
		cal.setTime(jn.executeAt);
		if(jn.interval == MONTHLY){
			cal.add(Calendar.MONTH, 1);
			jn.executeAt = cal.getTime();
		} else if(jn.interval == YEARLY){
			cal.add(Calendar.YEAR, 1);
			jn.executeAt = cal.getTime();
		} else {
			jn.executeAt = new Date(jn.executeAt.getTime() + jn.interval);
		}
		
		jn.count = (jn.count == FOREVER) ? FOREVER : jn.count - 1;
		
		return (jn.count != 0)? jn : null;		
	}
	
	private synchronized long runJobs(){
		long minDiff = Long.MAX_VALUE;
		long now = System.currentTimeMillis();
		
		for(int i = 0;i < jobs.size();){
			JobNode jn = jobs.elementAt(i);
			if(jn.executeAt.getTime() <= now){
				if(tp != null){
					tp.addRequest(jn.job);
				} else {
					Thread jt = new Thread(jn.job);
					jt.setDaemon(false);
					jt.start();
				}
				
				if(updateJobNode(jn) == null){
					jobs.removeElementAt(i);
					dlock.release();					
				}	
			} else {
				long diff = jn.executeAt.getTime() - now;
				minDiff = Math.min(diff, minDiff);
				i++;
			}
		}
		return minDiff;
	}
	
	public synchronized void run(){
		while(true){
			long waitTime = runJobs();			
			try {
				wait(waitTime);
			} catch (InterruptedException e) {}
		}
	}
	
	// 用于只运行一次的作业
	public void execute(Runnable job){
		executeIn(job ,(long)0);
	}

	// 用于只运行一次的作业。在经过指定的时间后运行
	public void executeIn(Runnable job, long millis) {		
		executeInAndRepeat(job, millis, 1000, ONCE);
	}

	// 用于多次执行一个作业
	public void executeInAndRepeat(Runnable job, long millis, long repeat) {
		executeInAndRepeat(job, millis, repeat, FOREVER);
	}

	// 用于只运行一次的作业。在经过指定的时间后运行
	public void executeAt(Runnable job, Date when){
		executeInAndRepeat(job, when, 1000, ONCE);
	}
	
	public void executeInAndRepeat(Runnable job, Date when, long repeat) {
		executeInAndRepeat(job, when, repeat, FOREVER);
	}
	
	public void executeInAndRepeat(Runnable job, long millis, long repeat, int count) {
		Date when = new Date(System.currentTimeMillis() + millis);
		executeInAndRepeat(job, when, repeat, count);
	}

	public void executeInAndRepeat(Runnable job, Date when, long repeat,
			int count) {
		JobNode jn = new JobNode();
		jn.job = job;
		jn.executeAt = when;
		jn.interval = repeat;
		jn.count = count;
		addJob(jn);		
	}
	
	public void cancel(Runnable job){
		deleteJob(job);
	}
	
}
