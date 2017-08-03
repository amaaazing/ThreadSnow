package xf.practice.concurrent.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *  run()中的if else
 * 	在run()方法中的while循环中包含了所有对ServerSocket的处理，run()的其他部分用来处理客户数据套接字
 * 
 * @author xinzhenqiu
 *
 */
public class TCPServer implements Runnable,Cloneable{

	Thread runner = null;
	ServerSocket server = null; // 处理服务器套接字
	Socket data = null; // 处理数据套接字
	volatile boolean shouldStop = false;
	
	public synchronized void startServer(int port) throws IOException{
		if(runner == null){
			// 创建一个ServerSocket对象，一个处理该对象的独立线程
			server = new ServerSocket(port);
			runner = new Thread(this);
			runner.start();
			// 通过一个独立线程处理ServerSocket对象，startServer()方法就可以立刻返回			
			// 这样同一个程序就可以作为多个服务器运行。(多new几个TCPServer)
		}
	}
	
	/**
	 * 为什么停止服务器前也要检查server是否为null？
	 * 因为ServerSocket被克隆以处理数据套接字，而对于这些克隆出来的TCPServer，我们将server设置为null
	 * 为了防止我们对一个处理数据套接字的TCPServer调用stopServer(),就需要检查server变量的值
	 * 
	 */
	public synchronized void stopServer(){
		if(server != null){
			shouldStop = true; // 停止已启动的线程（服务器）
			runner.interrupt();// 为了防止运行的线程阻塞在accept()中，需要中断该线程的运行
			runner = null;
			try {
				server.close();
			} catch (IOException e) {}			
			server = null; // 可以再次调用startServer(),使得ServerSocket可以在同一个或其他的port上启动
		}
	}
	
	/**
	 * 对每一个连接，都可以创建一个该类的新拷贝，
	 * 因为这个拷贝也是可运行的，因此每一个客户连接都可以创建另一个线程
	 * 
	 * 原始的TCPServer对象必须处理服务器套接字
	 * 拷贝处理的新线程需要处理数据套接字
	 * 
	 * 
	 */
	public void run() {
		if(server != null){// 处理服务器套接字
			while(!shouldStop){
				try {
					// 接受用户的请求
					// 调用accept()方法开始监听
					Socket dataSocket = server.accept(); 
					// 一旦建立连接，服务器就对自己进行克隆操作
					// 在新的线程中处理新的连接
					TCPServer newSocket = (TCPServer) clone();
					// 克隆处理的newSocket
					newSocket.server = null; // 以处理数据套接字
					newSocket.data = dataSocket;
					newSocket.runner =  new Thread(newSocket);					
					newSocket.runner.start();
				} catch (Exception e) {}
			}
		}else{
			// 处理客户数据套接字
			run(data);
		}
	}

	private void run(Socket data) {
				
	}

}
