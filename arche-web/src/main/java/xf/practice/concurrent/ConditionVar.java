package xf.practice.concurrent;
/**
 * 条件变量 VS 等待和通知机制
 * 
 * 条件变量的四个操作wait、timed_wait、signal、broadcast与wait()、wait(long)、notify()、 notifyAll()一一对应
 * 
 * 条件变量与等待和通知机制一样，需要拥有一个互斥锁才能工作
 * 
 * 使用条件变量的地方：希望建立由一个锁控制的两个信号通道（如：两个变量）
 * 					    对同一个变量使用不同的锁
 * 
 * 经常使用ConditionVar的地方是，缓冲区管理。当缓存区满时，如果线程试图将数据写入，线程就会阻塞
 * 其他使用缓冲区的线程也必须等到缓冲区有数据才能读操作。这样，我们就有一个锁（和缓冲区相关的锁）和2个条件：空或满
 * 在这种条件下，使用条件变量比使用纯粹的等待和通知机制更清晰
 * 
 * 
 * 条件变量：实际上并不是锁，它是和锁相关联的变量
 * 
 * 锁：是和一个特定的对象实例或类相关联的。指一个特定的线程进入一个同步方法
 * 
 * 互斥：锁的另一种称呼。
 * 
 * @author xinzhenqiu
 *
 */
public class ConditionVar {

	// 这里使用syncVar作为同步锁，而不是绑定对象的对象锁
	private BusyFlag syncVar;
	
	public ConditionVar(){
		this(new BusyFlag());
	}
	
	public ConditionVar(BusyFlag sv){
		this.syncVar = sv;
	}
	
	public void cvWait() throws InterruptedException{
		cvTimedWait(syncVar,0);
	}
	
	public void cvWait(BusyFlag sv) throws InterruptedException{
		cvTimedWait(sv,0);
	}
	
	public void cvTimedWait(int millis) throws InterruptedException{
		cvTimedWait(syncVar,millis);
	}
	
	/**
	 * 条件变量的wait操作要求拥有一个互斥锁。在其等待时，会释放锁；在wait方法返回前会重新获得锁
	 * @param sv
	 * @param millis
	 * @throws InterruptedException
	 */
	public void cvTimedWait(BusyFlag sv,int millis) throws InterruptedException{
		int i = 0;
		InterruptedException errex = null;
		
		synchronized(this){
			// 使用此方法必须拥有锁
			if(sv.getBusyFlagOwner() != Thread.currentThread()){
				throw new IllegalMonitorStateException("current thread not owner!");
			}
			
			// 1.（完全地）释放锁。因为BusyFlag类是可以嵌套的，所以必须释放BusyFlag上的所有锁
			while(sv.getBusyFlagOwner() == Thread.currentThread()){
				i++;
				sv.freeBusyFlag();
			}
			
			// 2.使用wait()方法
			try {
				if(millis == 0){
					wait();
				}else{
					wait(millis);
				}
			} catch (InterruptedException iex) {
				errex = iex;				
			}			
		}// synchronized block end
		
		// 3.获得锁（返回到最初状态）；重新获取先前步骤中释放的锁
		for(;i > 0; i--){
			sv.getBusyFlag();
		}
		
		if(errex != null) throw errex;
		
		return;
	}
	
	public void cvSignal(){
		cvSignal(syncVar);
	}
	
	// 唤醒一个线程
	public synchronized void cvSignal(BusyFlag sv){
		// 使用此方法必须拥有锁
		if(sv.getBusyFlagOwner() != Thread.currentThread()){
			throw new IllegalMonitorStateException("current thread not owner!");
		}
		notify();
	}
	
	public void cvBroadcast(){
		cvBroadcast(syncVar);
	}
	
	// 唤醒所有线程
	public synchronized void cvBroadcast(BusyFlag sv){
		// 使用此方法必须拥有锁
		if(sv.getBusyFlagOwner() != Thread.currentThread()){
			throw new IllegalMonitorStateException("current thread not owner!");
		}
		notifyAll();
	}
}
