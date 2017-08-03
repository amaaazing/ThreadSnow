package xf.practice.callback;




public class Foo implements Student {
	
	public void resolveQuestion(CallBack callBack) {
		
		try {
			System.out.println("the student is thinking,please wait.");
			Thread.sleep(5000);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
		
		callBack.tellAnswer(3);
	}

}
