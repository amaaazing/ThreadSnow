package xf.practice.concurrent.example;

/**
 * 任何一个Thread类的子类，她的方法既可以被线程对象自己调用，也可以被其他线程对象调用
 * 如：AsyncReadSocket的getResult()方法
 * 
 * @author xinzhenqiu
 *
 */
public class TestRead extends Thread{

	AsyncReadSocket arc;
	
	public TestRead(AsyncReadSocket arc){
		super("ReaderThread");
		this.arc = arc;
	}
	
	public void run(){
		// 调用其他线程中的方法
		System.out.println("Data is"+ arc.getResult());
	}
	
	public static void main(String[] args) {
		AsyncReadSocket arc = new AsyncReadSocket("host",6002);
		TestRead tr = new TestRead(arc);
		arc.start();
		tr.start();
	}
}
