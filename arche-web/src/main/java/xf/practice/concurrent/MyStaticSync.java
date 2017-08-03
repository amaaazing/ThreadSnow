package xf.practice.concurrent;

/**
 * 同步静态方法
 * 
 * 如果一个同步静态方法拥有一个对象引用，它是否可以可以调用该对象的同步方法或
 * 使用该对象来锁住一段同步代码块？
 * 
 * 通过调用一个同步静态方法来获取类锁，然后使用传递进来的对象获取对象锁
 * 
 * 
 * 类锁（称谓）：对应的类对象的对象锁
 * 
 * 
 * @author xinzhenqiu
 *
 */
public class MyStaticSync {

	public synchronized static void staticMethod(MyStaticSync obj){
		// 进入同步方法内，表明该线程获得了类锁
		obj.nonStaticMethod();
		
		synchronized(obj){
			// 获得了类锁和对象锁
		}
		
	}
	
	public synchronized void nonStaticMethod(){
		// 获得了对象锁
	}
	
	// 一个非静态方法不通过调用同步静态方法是否也可以获取静态锁呢？
	
	// 通过使用一个公共的静态对象来使用同步块机制
	public static Object lock = new Object();
	public synchronized void process(){
		synchronized(lock){
			// 访问类的静态变量的代码
		}
		
//		// 等价于
//		synchronized(MyStaticSync.class){
//			// 访问类的静态变量的代码
//		}
		
	}
	
	
	// 在一个静态方法中不能直接调用wait()和notify()
	public static void staticWait(){
		synchronized(lock){
			try {
				lock.wait();
			} catch (InterruptedException e) {}
		}
	}
	
	public static void staticNotify(){
		synchronized(lock){
			lock.notify();
		}
	}
}
