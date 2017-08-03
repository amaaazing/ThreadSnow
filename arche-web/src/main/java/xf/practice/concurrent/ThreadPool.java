package xf.practice.concurrent;

import java.util.Vector;

/**
 * 一个线程池的实现类
 * 
 * 在线程池中运行的对象应该是实现了Runnable接口的
 * 
 * 
 * 每一个线程都在等待工作，当线程得到通知后，就从存放对象的Vector中取出第一个对象来执行
 * 该对象执行完成后，线程必须通知与该对象关联的锁（如果存在的话），使得addRequestAndWait()方法知道何时可以返回
 * 同时线程也要通知线程池本身，使得waitForAll()方法会检查是否应该返回
 * 
 * 
 * 关闭线程池：通过向waitForAll()方法传递一个参数true.当线程池运行完全部作业后
 * waitForAll()方法安排线程池的线程结束并标记线程池不会再接受新的任务
 * 
 * 然后线程池就可以被垃圾收集程序回收
 * 
 * 
 * @author xinzhenqiu
 *
 */
public class ThreadPool {

	class ThreadPoolRequest{
		Runnable target;
		Object lock;
		
		ThreadPoolRequest(Runnable t, Object l){
			target = t;
			lock = l;
		}				
	} // class ThreadPoolRequest end
	
	class ThreadPoolThread extends Thread{
		ThreadPool parent;		
		volatile boolean shouldRun =  true;
		
		ThreadPoolThread(ThreadPool parent, int i){
			super("ThreadPoolThread" + i);
			this.parent = parent;
		}
		
		public void run(){
			ThreadPoolRequest obj = null;
			while(shouldRun){
				try {
					parent.cvFlag.getBusyFlag();
					while(obj == null && shouldRun){
						try {
							obj = (ThreadPoolRequest) parent.objects.elementAt(0);
							parent.objects.removeElementAt(0);
						} catch (ArrayIndexOutOfBoundsException e) {
							obj = null;
						} catch (ClassCastException e) {
							System.err.println("Unexpected data");
							obj = null;
						}
						
						if(obj == null){
							try {
								// 等待点2.当Vector中没有等待处理的对象时，ThreadPoolThread会在该条件变量上等待
								parent.cvAvailable.cvWait();
							} catch (InterruptedException e) {
								return;
							}
						}
					}					
				} finally {
					parent.cvFlag.freeBusyFlag();
				}
				
				if(!shouldRun) return;
				
				obj.target.run();
				
				try{
					parent.cvFlag.getBusyFlag();
					nObjects--;
					if(nObjects == 0) parent.cvEmpty.cvSignal();
				} finally {
					parent.cvFlag.freeBusyFlag();
				}
				
				if(obj.lock != null){
					synchronized(obj.lock){
						obj.lock.notify(); // 通知到addRequestAndWait()方法中的lock.wait()
					}
				}
				
				obj = null;
			}
		}
	} // class ThreadPoolThread end
	
	
	Vector<Object> objects;
	int nObjects = 0;
	ConditionVar cvAvailable,cvEmpty;
	BusyFlag cvFlag;
	ThreadPoolThread[] poolThreads;
	boolean terminated = false;
	
	public ThreadPool(int n){
		cvFlag = new BusyFlag();
		// 一个与cvFlag相关联的条件变量
		cvAvailable = new ConditionVar(cvFlag); // cvAvailable变量用于通知有工作在等待处理
		cvEmpty = new ConditionVar(cvFlag); // 该条件变量表示所有的任务都已经完成
		objects = new Vector<Object>();
		poolThreads = new ThreadPoolThread[n];
		for(int i = 0;i < n;i++){
			poolThreads[i] = new ThreadPoolThread(this,i);
			poolThreads[i].start();
		}
	}
	
	private void add(Runnable target,Object lock ){
		try{
			cvFlag.getBusyFlag();
			if(terminated){
				throw new IllegalStateException("Thread Pool has shut down");
			}
			objects.addElement(new ThreadPoolRequest(target, lock));
			// 只要nObjects变量增加，就是有等待处理的工作
			nObjects++;
			// 向一个线程发出表示有新工作存在的信号
			cvAvailable.cvSignal();
		} finally {
			cvFlag.freeBusyFlag();
		}
	}
	
	public void addRequest(Runnable target){
		add(target,null);
	}
	
	public void addRequestAndWait(Runnable target) throws InterruptedException{
		// 请求对象有一个相关联的锁对象
		Object lock = new Object();		
		synchronized(lock){
			add(target, lock);
			// 等待点1
			lock.wait(); // 当某个ThreadPoolThread对象执行完run()方法后，它会接受到通知信号
		}
	}
	
	// 等待池中所有对象执行完毕
	public void waitForAll(boolean terminate) throws InterruptedException{
		try{
			cvFlag.getBusyFlag();
			while(nObjects != 0){
				// 条件变量cvEmpty 等待nObjects变为0
				cvEmpty.cvWait();
				if(terminate){
					for(int i = 0;i < poolThreads.length; i++){
						poolThreads[i].shouldRun = false;
					}
					cvAvailable.cvBroadcast();
					terminated = true;
				}
			}
		} finally {
			cvFlag.freeBusyFlag();
		}
	}
	
	public void waitForAll() throws InterruptedException{
		waitForAll(false);
	}
}
