package xf.practice.concurrent;

/**
 * 屏障（barrier）：一个简单的等待点；
 * 即所有线程在这一点，它们要么是合并各自的结果，要么是运行到整个任务的下一个阶段
 * 
 * 一个简单的实现：利用Thread类的join()方法，我们可以等待全部线程结束从而可以处理结果或者为下一个任务启动新的线程
 * 
 * barrier:多个线程的集合点：在所有线程到达这一点之前，任何线程都不能越过这一点。java中没有屏障类
 * 
 * 
 * 
 * @author xinzhenqiu
 *
 */
public class Barrier {

	private int threads2Wait4;
	private InterruptedException iex;
	
	public Barrier(int nThreads){
		threads2Wait4 = nThreads;
	}
	
	public synchronized int waitForRest() throws InterruptedException{
		// threadNum线程号，最后一个到达屏障的线程将被分配为0
		int threadNum = --threads2Wait4;
		
		if(iex != null) throw iex;
		// 在屏障已经释放后，如果还有线程到来，就会被赋予一个负值，这表示出现了错误：
		// 如果对一个设计为同步4个线程的屏障发送了5个线程，第5个将收到负值。
		// 出现错误，屏障将不会阻塞任何线程
		if(threads2Wait4 <= 0){
			System.out.println("thread number("+threadNum+") is finally arrived at "+ this.toString() +".");
			notifyAll();// 当最后一个线程到达时，就通知所有等待的线程
			return threadNum;
		}
		
		while(threads2Wait4 > 0){
			if(iex != null) throw iex; // 3.如果其中任何一个等待线程收到中断信号，则所有的线程都应当收到同样的中断信号
			try {
				System.out.println("thread number("+threadNum+") is arrived at "+ this.toString() +".");
				wait(); // 每一个到达屏障的线程都调用wait()
			} catch (InterruptedException e) {
				iex = e;// 2.如果其中任何一个等待线程收到中断信号，则所有的线程都应当收到同样的中断信号 				
				notifyAll();
			}
		}
		System.out.println("thread number = "+threadNum);
		return threadNum;
	}
	
	/**
	 * 用来对所有的线程产生一个中断信号
	 */
	public synchronized void freeAll(){
		// 1.如果其中任何一个等待线程收到中断信号，则所有的线程都应当收到同样的中断信号
		iex = new InterruptedException("Barrier released by freeAll");
		notifyAll();
	}
		
}
