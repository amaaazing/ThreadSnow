package xf.practice.concurrent.example;

/**
 * 通过保证存在一个用户线程来保护守护线程
 * 
 * 只要系统中存在一个用户线程，jvm就不会退出
 * 这也就允许守护线程来完成其关键任务，当任务完成时，守护线程就释放该Daemon锁
 * 从而导致用户线程的终止
 * 
 * 
 * @author xinzhenqiu
 *
 */
public class DaemonLock implements Runnable{

	private int lockCount = 0;
	
	public synchronized void acquire(){
		if(lockCount++ == 0){
			Thread t = new Thread(this);
			t.setDaemon(false);
			t.start();
		}
	}
	
	public synchronized void release(){
		if(--lockCount == 0){
			notify();
		}
	}
	
	// synchronized 的run方法
	public synchronized void run(){
		while(lockCount != 0){
			try {
				wait();
			} catch (InterruptedException e) {}
		}
	}
}
