package xf.practice.concurrent.example;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import xf.practice.concurrent.BusyFlag;
import xf.practice.concurrent.ConditionVar;



/**
 * 
 * AsyncInputStream类的设计原则：以异步方式工作
 * 
 * AsyncInputStream类的实例类似于任何一个InputStream对象。它们可以毫无疑问地在使用InputStream类的地方使用
 * 
 * 虽然AsyncInputStream类也是Runnable类型的，但这只是实现的细节
 * 使用AsyncInputStream类的用户甚至不需要了解当一个AsyncInputStream对象被实例化时会创建一个新的线程。
 * 
 * 
 * 
 * 
 * 正确报告在不阻塞的情况下可以实际读取的字节数，同时自己对数据进行缓存
 * 
 * 使用另外一个线程来读取数据是实现的细节
 * 
 * AsyncInputStream实现了异步读取，但它同时是一个FilterInputStream类。
 * 因此该类的行为必须和InputStream类一样
 * 
 * EOF = -1,表示不可能从数据源中获取更多数据的意思，而不关心数据源是什么
 * 
 * 
 * 尽管InputStream类支持标记和重置功能，但AsyncInputStream类并不支持。
 * 因为没有什么实际的理由要求一个异步流来支持这个功能
 * 如有必要，可通过实例化BufferedInputStream对象来包含AsyncInputStream，从而获得这个功能
 * 
 * 
 * 
 * 
 * @author xinzhenqiu
 *
 */
public class AsyncInputStream extends FilterInputStream implements Runnable{

	// 同步的读取线程
	private Thread runner; // 存放由该类启动的I/O线程的引用
	// 缓冲区
	private volatile byte result[]; // 存放从I/O线程取回的数据
	// 缓存区的大小
	private volatile int reslen; // 存放从I/O线程取回的数据长度
	// 文件尾指示符
	private volatile boolean EOF ; // 在I/O线程中保存EOF事件并捕获I/O异常，然后在调用线程中指示EOF发生或抛出I/O异常
	// IO异常
	private volatile IOException IOError; // EOF 和IOError变量在I/O线程的生命周期中只会使用一次
	// *********************************************************************
	// 数据锁
	// 为什么使用一个锁来保护4个不同的实例变量？1.result和reslen两个变量是相关的，并且不可能只改变其中一个 2.EOF 和IOError变量在I/O线程的生命周期中只会使用一次
	BusyFlag lock;
	// 缓存区result中的数据由实例变量lock保护，还有两个条件变量与锁相关联
	// 我们可以让线程在一个与锁相关联的两个条件上等待
	// 信号变量 
	ConditionVar empty, full;
	// 为什么要使用两个条件变量而不是等待和通知机制？为了效率
	/**
	 * 在该例中，我们有一个数据源（缓冲区result），它可以有两个条件：
	 * 		空：如果线程试图读取数据，就会进入等待数据到来的状态
	 * 		满：当线程试图存储数据到缓冲区，也会进入等待数据被消耗的状态
	 * 如果使用等待和通知机制，则无论哪种情况发生，都必须要调用notifyAll方法，这会导致唤醒太多的线程
	 * 当然，这也是可以工作的。但是唤醒所有的线程来重新检查条件不如条件变量更有效率
	 * 
	 * @param in
	 * @param bufsize
	 */
	// *********************************************************************
	
	protected AsyncInputStream(InputStream in, int bufsize) {
		super(in);
		
		// 分配同步变量
		lock = new BusyFlag();
		empty = new ConditionVar(lock);
		full = new ConditionVar(lock);
		
		// 分配存储区域，对那些用来和I/O线程进行通信的变量初始化
		result = new byte[bufsize];
		reslen = 0;
		EOF = false;
		IOError = null;
		
		// 实例化并启动I/O线程
		// 新的线程只是在输入流上进行阻塞方式的读取，并提供了使得主线程以非阻塞方式获取数据的方式
		runner = new Thread(this);
		runner.start();
	}

	protected AsyncInputStream(InputStream in) {
		this(in, 1024);
	}
	
	/**
	 * 如果调用read()方法的时候，没有数据会发生什么情况呢？read方法在这种情况下会阻塞****************************
	 * 换句话说，read方法所做的违背了它的设计初衷。很显然，read方法不应该阻塞在从InputStream中读取数据上
	 * InputStream处于I/O线程的控制之下，不能直接被read()方法访问
	 * 
	 * 为了模拟这种阻塞，我们使用了条件变量empty。read()方法只是等待更多数据的到来。
	 * 
	 * 如果I/O线程读到了数据，那么将在数据放入缓冲区中时会产生一个信号，这是通过putChar()方法中的cvSignal()来完成的。
	 * *******************************************************************************************************
	 * 当EOF或者IOException发生时，所有被阻塞的线程将会有什么结果呢？
	 * 
	 * 通过使用条件变量可以使read()方法让阻塞方式进行。但是当EOF或者IOException发生时，因为没有数据到来，所有不会有通知发生
	 * 为了解决这个问题，我们必须在这些条件发生时使用cvBroadcast()方法。
	 * 那些等待中的线程将会依次被唤醒，然后处理缓存区中存在的数据。
	 * 
	 * 
	 * 
	 */
	public int read() throws IOException{
		try {
			lock.getBusyFlag(); //*************************************
			while(reslen == 0){ // 当缓存区没有数据时，就检查EOF 和 I/O异常
				try {
					if(EOF) return (-1);
					if(IOError != null) throw IOError;
					empty.cvWait(); // ********************************
				} catch (InterruptedException e) {}
			}
			return (int)getChar();
		} finally  {
			lock.freeBusyFlag(); // ***********************************
		}
	}

	/**
	 * 
	 * 缓存区存在可用的数据，但是不足以填充该字节数组，read(byte[] b)方法仅仅读取这部分数据
	 * 返回的是实际读取的字节数
	 */
	public int read(byte[] b) throws IOException{
		return read(b, 0, b.length);
	}
	
	public int read(byte[] b, int off, int len) throws IOException{
		try{
			lock.getBusyFlag();
			while(reslen == 0){
				if(EOF) return (-1);
				if(IOError != null) throw IOError;
				try {
					empty.cvWait();
				} catch (InterruptedException e) {}
			}
			
			int sizeRead = Math.min(reslen, len);
			byte[] c = getChar(sizeRead);
			System.arraycopy(c, 0, b, off, sizeRead);
			return sizeRead;
		} finally {
			lock.freeBusyFlag();
		}		
	}
	
	// skip()方法以非阻塞的方式跳过指定的字节数
	// 如果要跳过的字节数多于当前可用字节数，则仅仅跳过当前可用的数据，并且返回实际跳过的字节数
	public long skip(long n) throws IOException{
		try{
			lock.getBusyFlag();
			
			int sizeSkip = Math.min(reslen, (int)n);
			if(sizeSkip > 0){
				byte[] c = getChar(sizeSkip);
			}
			return (long)sizeSkip;
		} finally {
			lock.freeBusyFlag();
		}
	}
	
	
	/**
	 * 返回缓存区中的字节个数
	 */
	public int available() throws IOException{
		return reslen;
	}
	
	public void close() throws IOException {
		try{
			lock.getBusyFlag();
			reslen = 0; // 清空缓冲区
			EOF = true; // 标记文件尾
			empty.cvBroadcast(); // 向所有线程发出信号
			full.cvBroadcast();
			
		} finally {
			lock.freeBusyFlag();
		}
	}
	
	public void mark(int readLimit){
		
	}
	
	public void reset() throws IOException {
		
	}
	
	public boolean markSupported(){
		return false;
	}
	
	/**
	 *  在run()方法中，I/O线程调用putChar()将数据放入缓冲区
	 */
	public void run(){
		try {
			while(true){
				int c = in.read();
				try{
					lock.getBusyFlag();
					if(c == -1 || EOF){
						EOF = true; // 标记文件尾
						in.close(); // 关闭输入流 
						return;
					} else { 
						putChar((byte)c); // 存放读取的字节
					}
					if(EOF){
						in.close();
						return;
					}								
				} finally {
					lock.freeBusyFlag();
				}
			}
		} catch (IOException e) {
			IOError = e; // 保存发生的异常
			return;
		} finally {
			try{
				lock.getBusyFlag();
				empty.cvBroadcast(); // 向所有线程发出信号
			} finally {
				lock.freeBusyFlag();
			}
		}
	}
	
	private void putChar(byte c) {
		try{
			lock.getBusyFlag();
			while(reslen == result.length && !EOF){
				try {
					full.cvWait();
				} catch (InterruptedException e) {}
				
			}
			if(!EOF) {
				result[reslen++] = c;
				empty.cvSignal();
			}			
		} finally {
			lock.freeBusyFlag();
		}		
	}

	private byte getChar(){
		try{
			lock.getBusyFlag();
			byte c = result[0];
			System.arraycopy(result, 1, c, 0, --reslen);
			full.cvSignal();
			return c;
		} finally {
			lock.freeBusyFlag();
		}
	}
	
	private byte[] getChar(int chars) {
		
		try{
			lock.getBusyFlag();
			byte[] c = new byte[chars];
			System.arraycopy(result, 0, c, 0, chars);
			reslen -= chars;
			System.arraycopy(result, chars, result, 0, reslen);
			full.cvSignal();
			return c;			
		} finally {
			lock.freeBusyFlag();
		}
	}
}
