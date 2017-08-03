package xf.practice.test;

public class TestThreadInterrupter implements Runnable{

	public void run() {
		System.out.println("tt线程将cpu让给main线程");
		
		try {
			wait();
		} catch (Exception e) {
			System.out.println("收到了来自main线程的中断");
		}

//		if(Thread.currentThread().isInterrupted()){
//			System.out.println("收到了来自main线程的中断-- business logic");
//		}
	}

}
