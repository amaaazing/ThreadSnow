package xf.mvc.servlet;

public class MyRequest {

	/**
	 * Request对象实现请求转发
	 * 
	 * 请求转发：指一个web资源收到客户端请求后，通知服务器去调用另外一个web资源进行处理。
	 * 
	 * 在Servlet中实现请求转发的两种方式：
	 * 1、通过ServletContext的getRequestDispatcher(String path)方法，该方法返回一个RequestDispatcher对象，调用这个对象的forward方法可以实现请求转发。
	 * 2、通过request对象提供的getRequestDispatche(String path)方法，该方法返回一个RequestDispatcher对象，调用这个对象的forward方法可以实现请求转发。
	 * 
	 */
}
