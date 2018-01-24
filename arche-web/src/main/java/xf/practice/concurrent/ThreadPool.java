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

/**
 *  一个线程池包括以下四个基本组成部分：
 1、线程池管理器（ThreadPool）：用于创建并管理线程池，包括 创建线程池，销毁线程池，添加新任务；
 2、工作线程（PoolWorker）：线程池中线程，在没有任务时处于等待状态，可以循环的执行任务；
 3、任务接口（Task）：每个任务必须实现的接口，以供工作线程调度任务的执行，它主要规定了任务的入口，任务执行完后的收尾工作，任务的执行状态等；
 4、任务队列（taskQueue）：用于存放没有处理的任务。提供一种缓冲机制。
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

	//  内部类，工作线程
	class ThreadPoolThread extends Thread{
		ThreadPool parent;		
		volatile boolean shouldRun =  true;// 该工作线程是否有效，用于结束该工作线程
		
		ThreadPoolThread(ThreadPool parent, int i){
			super("ThreadPoolThread" + i);
			this.parent = parent;
		}

		// 关键所在，如果任务队列不空，则取出任务执行，若任务队列空，则等待
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

	// 创建线程池,n为线程池中工作线程的个数
	public ThreadPool(int n){
		cvFlag = new BusyFlag();
		// 一个与cvFlag相关联的条件变量
		cvAvailable = new ConditionVar(cvFlag); // cvAvailable变量用于通知有工作在等待处理
		cvEmpty = new ConditionVar(cvFlag); // 该条件变量表示所有的任务都已经完成
		objects = new Vector<Object>();
		poolThreads = new ThreadPoolThread[n];
		for(int i = 0;i < n;i++){
			poolThreads[i] = new ThreadPoolThread(this,i);
			poolThreads[i].start(); // 开启线程池中的线程
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
			cvFlag.freeBusyFlag();//  finally子句保证了线程异常退出也能是否锁
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
			cvFlag.freeBusyFlag(); //  finally子句保证了线程异常退出也能是否锁
		}
	}
	
	public void waitForAll() throws InterruptedException{
		waitForAll(false);
	}
}
