package xf.practice.concurrent;

/**
 * BusyFlagV1类存在的问题，可能的执行顺序：
 * 
 * 1.线程A检测busyFlag为闲
 * 2.线程B检测busyFlag为闲
 * 3.线程B设置busyFlag
 * 4.线程B睡眠100ms
 * 5.线程B醒来，确认其拥有busyFlag，退出循环
 * 6.线程A设置busyFlag，睡眠，醒来，确认其拥有busyFlag，退出循环
 * 
 * 这种情况极为罕见，但还是有可能出现。
 * 
 * 思考：
 * 如果有多个线程都来同时设置busyFlag标志，将会有什么结果呢？
 * 对于busyFlag变量的设置是不是原子操作？
 * java规范已经保证了，除了double和long类型以外，对于其他类型的变量进行赋值的操作都是原子性的。
 * 
 * 
 * 我们可不可以通过使用同步原语来解决BusyFlagV1类面临的问题呢？详见：BusyFlagV2
 * 
 * @author xinzhenqiu
 *
 */
public class BusyFlagV1 {

	protected Thread busyFlag = null;
	
	/**
	 * 在当前线程中循环直到可以将busyFlag设置为当前线程
	 * 如果busyFlag被其他线程设置了，线程会等待100ms再试
	 * 
	 * 注意：getBusyFlag()方法就只有一个循环体，如果拿不到busyFlag就一直循环
	 */
	public void getBusyFlag(){
		while(busyFlag != Thread.currentThread()){
			if(busyFlag == null){
				busyFlag = Thread.currentThread();
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
			
		}
	}
	
	/**
	 * 通过将busyFlag设置为null，释放标志
	 */
	public void freeBusyFlag(){
		if(busyFlag == Thread.currentThread()){
			busyFlag = null;
		}
	}
	
	/**
	 * BusyFlagV1的几个问题
	 * 
	 * 1.为什么线程需要睡眠100ms？
	 * 因为除了循环轮询以外，似乎没有其他方法来检测标志位的改变。
	 * 如果不使用sleep，就会浪费其他线程可以使用的CPU周期
	 * 
	 * 2.为什么线程在标志没有被设置时，还是要睡眠100ms呢？
	 * 在检查标志是否为null和设置标志之间存在一个race condition（竞态条件），如果两个线程都发现标志为null
	 * 那么它们都可以设置标志并退出循环。但是通过调用sleep()方法，我们就允许两个线程在通过while循环再次检查该标志之前设置busyFlag，
	 * 这样只有第二个线程可以设置标志并退出循环，从而退出getBusyFlag()方法。
	 * 
	 */
}
