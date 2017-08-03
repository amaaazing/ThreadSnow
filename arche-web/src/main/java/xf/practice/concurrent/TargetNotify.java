package xf.practice.concurrent;

/**
 * 实现控制哪一个线程能接受通知
 * @author xinzhenqiu
 *
 */
public class TargetNotify {

	private Object targets[] = null;
	
	public TargetNotify(int numberOfTargets){
		targets = new Object[numberOfTargets];
		for(int i = 0;i < numberOfTargets;i++){
			targets[i] = new Object();
		}
	}
	
	/**
	 * 不是让所有线程等待在this对象上，而是选择一个对象来等待
	 * @param targetNumber
	 */
	public void wait(int targetNumber){
		synchronized(targets[targetNumber]) {
			try {
				targets[targetNumber].wait();
			} catch (InterruptedException e) {}
		}
	}
	
	public void notify(int targetNumber) throws InterruptedException{
		synchronized(targets[targetNumber]) {
			targets[targetNumber].notify();
		}
	}
}
