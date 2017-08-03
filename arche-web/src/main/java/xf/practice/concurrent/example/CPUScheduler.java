package xf.practice.concurrent.example;

import xf.practice.concurrent.CircularList;



/**
 * 
 * 分时间片循环调度
 * 
 * 3.如果有2个线程试图创建CPUScheduler，会有什么结果呢？如此，我们会有2个线程调度线程，他们之间会竞争对其他线程的调度
 * 因此我们要确保只能创建一个CPUScheduler实例
 * 
 * 4.设计一个退出机制
 * // 当所有的客户线程结束后，只有守护线程CPUScheduler存在于程序中，此时程序就会结束
 * 因此我们需要CPUScheduler在没有线程需要调度时不是退出，而是等待更多线程的到来
 * 
 * 
 * 5.如果当前运行的线程阻塞了，会有什么结果？没什么大问题
 * 
 * 
 * 总结：
 * java调度程序允许你对线程调度进行一些控制，但是不能进行绝对的控制
 * 
 * 
 * 
 * @author xinzhenqiu
 *
 */
public class CPUScheduler extends Thread{

	private int timeSlice; // 线程应当运行的毫秒数
	private CircularList threads; // 所有被调度的线程
	
	public volatile boolean shouldRun = false; // 当该标志被设置时退出
	
	private  static boolean initialized = false;
	
	private boolean needThreads; // 
	
	private synchronized static boolean isInitialized(){
		if(initialized) return true;
		initialized =  true;
		return false;
	}	
	
	public CPUScheduler(int t){
		if(isInitialized()) throw new SecurityException("Already initialized");
		timeSlice = t;
		threads = new CircularList();
		setDaemon(true);// CPUScheduler标记为守护线程
		
	}
	
	/**
	 * 当一个线程被加入到循环链表中时，是否需要向CPUScheduler线程发出通知信号
	 * @param t
	 */
	public void addThread(Thread t){
		threads.insert(t);
		t.setPriority(2);
		if(needThreads){
			needThreads = false;
			notify();
		}
	}
	
	public void removeThread(Thread t){
		t.setPriority(5);
		threads.delete(t);
	}
	
	/**
	 * 此处违反了不要将run()方法声明为同步方法的原则
	 * 
	 * why？
	 * 
	 * 实际上在该方法中等待的时间要多于运行时间
	 * 因为处于等待状态时，会释放所拥有的锁
	 * 因此同步该方法是没有问题的
	 * 
	 * 
	 */
	public synchronized void run(){
		Thread current;
		setPriority(6);
		while(shouldRun){
			current = (Thread) threads.getNext();
			//if(current == null) return;
			// 如果链表中没有线程，就会进入等待状态，直到链表中有一个为止
			while(current == null){
				needThreads = true;
				try {
					wait();
				} catch (InterruptedException e) {}				
				current = (Thread) threads.getNext();
			}
			current.setPriority(4);
			try {
				//Thread.sleep(timeSlice);
				wait(timeSlice);
			} catch (InterruptedException e) {}
			current.setPriority(2);
		}
		
	}
}
