package xf.practice.concurrent;

/**
 * 屏障技术对多阶段算法很有用
 * 每一个阶段都可以通过多个线程来实现，而在阶段间，每一个线程都要等待其他线程完成它们在该阶段中的工作
 * 
 * 
 * 注意：使用Brrier类时，所有的线程都是活动的
 * @author xinzhenqiu
 *
 */
public class ProcessItBarrier implements Runnable {

	String is[];
	Barrier bpStart,bp1,bp2,bpEnd,bp1b,bp2b;
	
	public ProcessItBarrier(String sources[]){
		is = sources;
		bpStart = new Barrier(sources.length);
		bp1 = new Barrier(sources.length);
		bp2 = new Barrier(sources.length);
		bpEnd = new Barrier(sources.length);
		bp1b = new Barrier(sources.length);
		bp2b = new Barrier(sources.length);
				
		for(int i = 0;i < is.length;i++){
			// 如果is.length = 2，则这里启动了2个线程
			(new Thread(this)).start();
		}
	}
	
	/**
	 * 在同一个run()方法中包含了全部三个阶段的代码
	 * 
	 * 在不同阶段使用同一个屏障
	 */
//	public void run() {			
//		try {
//			// 第一个屏障：等待点
//			// 线程进入下个阶段前，会等待其他线程同步
//			int i = bpStart.waitForRest(); 
//			doPhaseOne(is[i]);
//			
//			// 第二个屏障：等待点
//			// 线程进入下个阶段前，会等待其他线程同步
//			bp1.waitForRest();
//			doPhaseTwo(is[i]);
//			
//			// 第三个屏障：等待点
//			// 线程进入下个阶段前，会等待其他线程同步
//			bp2.waitForRest();
//			doPhaseThree(is[i]);
//			
//			// 第四个屏障：等待点
//			// 线程进入下个阶段前，会等待其他线程同步
//			bpEnd.waitForRest();
//			
//		} catch (InterruptedException e) {}
//	}
	
	public void doPhaseOne(String ps){
		System.out.println("doPhaseOne is done.(" +ps+")");
	}

	public void doPhaseTwo(String ps){
		System.out.println("doPhaseTwo is done.(" +ps+")");
	}
	
	public void doPhaseThree(String ps){
		System.out.println("doPhaseThree is done.(" +ps+")");
	}
	
	public static void main(String[] args) {
		ProcessItBarrier pi = new ProcessItBarrier(new String[]{"snow","rain","foo"});
		System.loadLibrary("");
	}
	
	/**
	 * 
	 * 在不同阶段使用两个屏障
	 * 
	 */
	public void run() {			
		try {
			// 第一个屏障：等待点
			// 线程进入下个阶段前，会等待其他线程同步
			int i = bpStart.waitForRest();
			// 第一个阶段
			doPhaseOne(is[i]);
			// 通过Barrier来保护不同阶段的设置代码
			if(bp1.waitForRest() == 0){         // ************************** 在不同阶段使用两个屏障之第一个
				// 使用屏障返回的线程号来判断是哪个线程要执行这些代码
				// 还可以使用其他方法来选择这个线程：1.开始执行时就指定使用哪个线程 2.使用运行这些设置代码的线程
				doPhaseTwoSetup(); // 此外，屏障的定义和清理都可以包含在设置代码中
			}
			bp1b.waitForRest();                 // ************************** 在不同阶段使用两个屏障之第二个
			
			// 第二阶段
			doPhaseTwo(is[i]);
			
			if(bp2.waitForRest() == 0){
				// 模拟在两阶段代码中的清理和设置代码，只有一个线程会执行这些代码
				doPhaseThreeSetup();
			}
			bp2b.waitForRest();					
			
			// 第三阶段
			doPhaseThree(is[i]);
			
			// 第四个屏障：等待点
			// 线程进入下个阶段前，会等待其他线程同步
			bpEnd.waitForRest();
			
		} catch (InterruptedException e) {}
	}

	private void doPhaseThreeSetup() {		
		System.out.println("**************doPhaseThreeSetup**************");
	}

	private void doPhaseTwoSetup() {		
		System.out.println("**************doPhaseTwoSetup**************");
	}
}
