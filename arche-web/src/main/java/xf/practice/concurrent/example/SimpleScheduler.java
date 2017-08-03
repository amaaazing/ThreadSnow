package xf.practice.concurrent.example;

/**
 * 如何创建一个循环调度程序？
 * 
 * 创建一个基于时间的调度事件
 * 当定时器线程醒来时，它就成为当前运行线程，从而导致原来运行线程所处的链表被重新调整
 * 
 * 简单的调度程序仅仅是具有高优先级的定时器，它被周期性地唤醒，然后再次立刻进入睡眠状态
 * 
 * 这种调度程序要求每当JVM从优先级链表中选取一个线程运行后，都会对该链表重新排序
 * 
 * 
 * @author xinzhenqiu
 *
 */
public class SimpleScheduler extends Thread{

	int timeSlice;
	
	public SimpleScheduler(int t){
		timeSlice = t;
		setPriority(Thread.MAX_PRIORITY);
		setDaemon(true);
	}
	
	public void run(){
		while(true){
			try {
				sleep(timeSlice);
			} catch (InterruptedException e) {}
		}
	}
}
