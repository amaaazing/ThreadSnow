package xf.practice.concurrent.example;

/**
 * 异步I/O类:该类特定于网络套接字
 * 
 * 对其他数据源（如文件、管道等），该类没有扩展能力
 * 不同于TCPServer类，AsyncReadSocket没有很好地隐藏其线程的实现细节
 * 
 * @author xinzhenqiu
 *
 */
public class AsyncReadSocket extends Thread{
	StringBuffer result;
	
	public AsyncReadSocket(String host, int port){
		// 打开到给定主机的套接字
	}

	public void run(){
		// 从套接字中读取数据，放入字符串缓冲区
	}
	
	// 获取已经从套接字读入的字符串
	// 只允许进行读操作的线程执行这个方法。我们假设进行读操作的线程名称以“Reader”开头
	// 可以通过setName或构造方法给线程赋值名称
	// getResult() 没有指定要读取的长度
	public String getResult(){
		// 获取线程对象的引用只是为了获取线程的名称，我们实际想要的是调用getResult()方法的线程名称，
		// 而不一定是AsyncReadSocket线程的名称
		String reader = Thread.currentThread().getName();
		if(reader.startsWith("Reader")){
			String retVal = result.toString();
			result  = new StringBuffer();
			return retVal;
		}else{
			return "";
		}

	}
}
