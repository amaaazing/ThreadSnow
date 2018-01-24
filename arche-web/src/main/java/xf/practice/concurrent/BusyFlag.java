package xf.practice.concurrent;

/**
 * (锁的作用域跨多个方法)
 * 会话作用域内可使用此类：锁，java的同步原语是不直接提供这种作用域锁的
 * 
 * 等待和通知机制 VS 同步机制
 * 		同步机制：通过关键字synchronized来实现，解决race condition问题
 * 		等待和通知机制：也是一种同步机制；通过方法调用来实现，解决等待的时间问题；不仅仅是一个通信机制，它允许一个线程通知另外一个线程某种特定的条件发生了
 * 			等待和通知机制并不指定该条件到底是什么；不能解决race condition问题
 * 
 * 将等待和通知机制 && 同步机制 结合使用的方法实际上是一种标准用法
 * 
 * 等待和通知机制要配合同步机制使用，那么在等待和通知中存在的竞态条件是什么，为什么要配合同步机制使用消除竞态条件？
 * 以下是一种竞态条件：
 * 1.第一个线程检查条件，确定需要等待
 * 2.第二个线程设定条件
 * 3.第二个线程调用notify()方法，这个调用因为没有等待的线程而立即返回
 * 4.第一个线程调用wait()方法。
 * 简而言之，一个调用wait()的线程是在确定了它所要求的条件不能满足后（典型的方法是检查变量的值）才调用wait()方法的，。而当
 * 其他线程设定了条件后（通常是设置同一个变量的值），它调用notify（）方法。
 * 
 * 所以，等待和通知机制需要synchronized保证检查条件和设置条件值的操作都是原子性操作。
 * 
 * 注意：如果等待和通知机制使用同步块的话，要求必须使用同一个对象锁
 * 
 * @author xinzhenqiu
 *
 */
public class BusyFlag {

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
	public synchronized void getBusyFlag(){
		// wait()需要放在循环中，
		// 当wait()方法返回时，仅仅表示在过去的某个时候条件被满足了，并且另一个线程调用了notify()方法
		// 但是我们不能假设现在条件仍然是满足的，而不再次去检查条件
		while(tryGetBusyFlag() == false){
			try{
				wait(); // wait()方法在进入等待前会释放锁，但从wait()方法返回时会再次获得锁，
				// wait()方法在进入等待前会释放锁，因此不必担心getBusyFlag（）加上synchronized后锁的作用域过大，导致死锁。
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
		if(busyFlag == Thread.currentThread()){
			busyCount++;
			return true;
		}
		return false;
	}
	
	/**
	 * 1.通过将busyFlag设置为null来释放此标志
	 * 2.嵌套锁。getBusyFlagOwner()方法也是synchronized
	 */
	public synchronized void freeBusyFlag(){
		if(getBusyFlagOwner() == Thread.currentThread()){
			busyCount--;
			if(busyCount == 0){
				busyFlag = null; // 当busyCount为0时，才真正释放
				notify(); // ----当标志释放时，通知等待该条件的线程
				/**
				 * 如果调用notify()方法时，没有线程在等待该条件，会有什么结果呢？这不会有什么错误。
				 * 
				 * 没有任何线程在等待该条件，notify()方法就简单地返回，不用发送通知了
				 * 
				 */
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
