package xf.practice.concurrent;

/**
 * 解决嵌套锁的问题（死锁）：一个线程试图两次获取同一个锁，ReentrantLock,可重入锁
 * 
 * 使用此类可以锁定任何范围内的代码，而不需要担心已经拥有了该锁
 * 
 * @author xinzhenqiu
 *
 */
public class BusyFlagV3 {
	
	protected Thread busyFlag = null;
	// 检测自己是否拥有锁
	protected int busyCount = 0;
	
	/**
	 * getBusyFlag()在当前线程中循环直到可以将busyFlag设为当前线程
	 * 如果busyFlag被其他线程设置了，线程会等待100ms后再试
	 * 
	 * getBusyFlag()为什么没使用synchronized？因为它并不试图访问busyFlag变量。
	 * 
	 */
	public void getBusyFlag(){
		while(tryGetBusyFlag() == false){
			try{
				// 1.sleep方法的调用：为了避免浪费CPU周期
				// 2.为什么线程需要睡眠100ms？
				// 因为除了轮询之外，没有其他方法来检测标志位的改变；如果不使用sleep方法的话，就会浪费其他线程可以使用的CPU周期
				// 3.矛盾：如果睡眠时间过长，就会造成等待的时间过长，从而引起性能下降；反过来，如果不使用sleep，则会因为不停地轮询而使用
				// 过多的CPU周期，从而同样引起性能下降问题
				// 4.我们期望只要锁被释放，getBusyFlag()就能立刻获取busyFlag，同时也不会在循环中消耗任何的CPU周期
				// 5.由此，引出线程的等待和通知
				// 6.解决方案：使用wait()代替sleep(),等待一个条件发生：busyFlag为null，
				Thread.sleep(100); // sleep的真正用意:等待一个条件的发生（busyFlag为闲）
			}catch(Exception e){}
		}
	}

	/**
	 * 检测标志是否为null和设置标志之间存在一个竞态条件。因此需要synchronized
	 * @return
	 */
	public synchronized boolean tryGetBusyFlag() {
		// 只要busyFlag标志为null，线程就立刻将busyFlag设置为当前线程
		if(busyFlag == null){
			busyFlag = Thread.currentThread();
			busyCount = 1;
			return true;
		}
		// 对象检查自己是不是拥有锁，已经拥有了锁就仅仅对该计数器加1
		// 在对应的freeBusyFlag()方法中它将计数器的值减一，当值为0时，才真正释放该锁
		if(busyFlag == Thread.currentThread()){
			busyCount++;
			return true;
		}
		return false;
	}
	
	/**
	 * 1.通过将busyFlag设置为null来释放此标志
	 * 2.嵌套锁的引入。getBusyFlagOwner()方法也是synchronized
	 */
	public synchronized void freeBusyFlag(){
		// 判断拥有锁的是不是当前线程
		if(getBusyFlagOwner() == Thread.currentThread()){
			busyCount--;
			if(busyCount == 0){
				busyFlag = null; // 当busyCount为0时，才真正释放
			}
		}
	}
	
	/**
	 * 指明哪个线程拥有锁
	 * @return
	 */
	public synchronized Thread getBusyFlagOwner(){
		return busyFlag;
	}
}
