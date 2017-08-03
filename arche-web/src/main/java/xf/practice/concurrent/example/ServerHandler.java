package xf.practice.concurrent.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 实现了一个独立的提供服务的应用程序
 * @author xinzhenqiu
 *
 */
public class ServerHandler extends TCPServer {

//	// 在子类中覆盖父类的run(data)方法，而不考虑ServerSocket或其他的数据套接字
//	public void run(Socket data){
//		try {
//			InputStream in = data.getInputStream();
//			OutputStream out = data.getOutputStream();
//			// 处理数据套接字，省略
//		} catch (IOException e) {} 
//		
//	}
	
	
	/**
	 * 使用异步方式从客户端读取数据
	 * 
	 * 
	 * @param data
	 */
	public void run(Socket data){
		try {
			InputStream in = new AsyncInputStream(data.getInputStream());
			OutputStream out = data.getOutputStream();
		} catch (IOException e) {}
	}
	
	/**
	 * ServerHandler类只用修改一行代码，就变成以异步方式从客户端读取数据
	 * 
	 * 同时我们也将提供服务的线程个数翻了一番。
	 * 
	 * 虽然从代码上看不出有什么线程被创建了，但是实际上对于每一个客户端都有2个线程在为其服务
	 * 
	 */
}
