package xf.practice.concurrent;

/**
 * 相比BusyFlagV1，引入了一个新的方法tryGetBusyFlag(),还使用了synchronized
 * 
 * tryGetBusyFlag()和freeBusyFlag()使用的是同一个锁。
 * 这意味着不可能在线程试图释放和获取busyFlag时产生race condition（竞态条件）
 * 
 * @author xinzhenqiu
 *
 */
public class BusyFlagV2 {

	protected Thread busyFlag = null;	

	/**
	 * BusyFlagV2的getBusyFlag()方法并没有被声明为synchronized。
	 * 因为此处并不试图访问busyFlag变量。而BusyFlagV1中的getBusyFlag()直接访问busyFlag变量
	 * 
	 * 如果直接给BusyFlagV1中的getBusyFlag()方法加上synchronized，会造成什么问题呢？
	 * 死锁
	 * 
	 * tryGetBusyFlag()方法减小了synchronized同步的作用域
	 * 
	 * 我们要做的是将锁的作用域限定在我们要改变数据的范围内（即检查和获取busyFlag），并不需要在整个方法中一直持有该锁
	 * 
	 */
	public void getBusyFlag(){
		while(tryGetBusyFlag() == false){			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
			
		}
	}
	
	/**
	 * 此方法与getBusyFlag()等价
	 * 重点：使用了同步块
	 */
	public void getBusyFlagV2(){
		while(true){
			// 使用synchronized 块仅同步了对busyFlag进行检查和设置
			// 此方法锁的作用域减小了
			// 为了和freeBusyFlag()使用同一个对象锁，因此使用this来获取对象锁
			synchronized(this){ 
				if(busyFlag == null){
					busyFlag = Thread.currentThread();
					break;
				}
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
			
		}
	}
	
//	/** 锁的作用域太大
//	 * 如果直接给BusyFlagV1中的getBusyFlag()方法加上synchronized，会造成什么问题呢？
//	 * 这会阻止freeBusyFlag()方法的运行，如果getBusyFlag()被调用，标志为忙状态，getBusyFlag()会循环等待该标志被释放
//	 * 但是因为freeBusyFlag()方法只有等到getBusyFlag()释放了对象锁之后才能够运行，所以没有办法释放标志
//	 * 这种两难的情况称为死锁（dead lock）。这是锁和标志之间的问题；更常见的是，死锁会发生在两个或多个锁上。
//	 */
//	public synchronized void getBusyFlag(){
//		while(busyFlag != Thread.currentThread()){
//			if(busyFlag == null){
//				busyFlag = Thread.currentThread();
//			}
//			
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {}
//			
//		}
//	}
	
	
	/**
	 * 这个方法并不等待标志被释放。如果标志为闲，它就设置标志并返回ture
	 * @return
	 */
	public synchronized boolean tryGetBusyFlag(){
		if(busyFlag == null){
			busyFlag = Thread.currentThread();
			return true;
		}
		return false;
	}
	
	
	public synchronized void freeBusyFlag(){
		if(busyFlag == Thread.currentThread()){
			busyFlag = null;
		}
	}
	

}
