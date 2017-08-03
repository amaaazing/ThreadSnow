package xf.practice.concurrent.example;


public class MyServer {

	public static void main(String[] args) throws Exception {
		TCPServer server = new ServerHandler();
		server.startServer(300);
	}
}
