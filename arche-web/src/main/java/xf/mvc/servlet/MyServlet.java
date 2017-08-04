package xf.mvc.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

// web应用,就是在webapp下面建立我们的网站，classes当然是存servlet用的 
// Servlet中方法都是回调函数   都是会在特定的时候特定的环境下自动调用的
// Servlet的本质是一个Java对象,就像下面这个类的对象。
// Servlet没有main方法，所有行为由Container控制。Container就是一个java程序。
public class MyServlet implements Servlet{

	
	ServletConfig config; // 定义ServletConfig对象来接收配置的初始化参数
	
	/**
	 * Servlet运行于支持Java的应用服务器中。从实现上讲，Servlet可以响应任何类型的请求，
	 * 但绝大多数情况下Servlet只用来扩展基于HTTP协议的Web服务器。
	 * 对这句话再做点解释，比如HttpServlet类继承自Servlet类，可以利用继承HttpServlet来实现Http请求，
	 * 当不是Http请求的时候，也可以定义其他形式的Servlet。
	 * 可使用Servlet来处理自己的应用层协议。
	 * 
	 * 开发servlet三种方法：
	 * 一种是实现servlet接口，一种是继承GernericServlet  还有一种是继承HttpServlet
	 * 
	 * 这3个方法表示servlet技术经过3个发展阶段
	 * 
	 * 引入servlet的生命周期:init service destory
	 * 
	 * 
	 */	
	
	private String path;
	
	
	// 该函数用于初始化该servlet， 类似于我们的类的构造函数
	// 该函数只是会被调用一次， 当用户第一次访问该servlet的时候被调用
	
	public void init(ServletConfig config) throws ServletException {
		this.config = config; // init方法进行初始化MyServlet操作
		path = config.getInitParameter("path");
		
		// 获取所有的初始化参数
		Enumeration<String> e  = config.getInitParameterNames();
		while(e.hasMoreElements()){
			String name = e.nextElement();
			String value = config.getInitParameter(name);
			
			System.out.println(name + "=" + value);
		}
		
		
		
		System.out.println("init it");
	}

	public ServletConfig getServletConfig() {
		
		return null;
	}

	// service 函数用于处理业务逻辑
	// 程序员应当把业务逻辑代码写在这里
	// 该函数在用户每次访问servlet的时候都会被调用
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		
		System.out.println("service it");
		PrintWriter pw = res.getWriter();
		
		pw.println("My Servlet!---\n" + path);
	}

	public String getServletInfo() {
		return null;
	}

	// Servlet实例对象一旦创建，它就会驻留在内存中，为后续的其它请求服务，直至web容器退出，servlet实例对象才会销毁。
	public void destroy() {
		System.out.println("destory it");
	}

	// web工程中URL地址的推荐写法
	
	/**
	 * 在JavaWeb开发中，只要是写URL地址，那么建议最好以"/"开头，也就是使用绝对路径的方式，那么这个"/"到底代表什么呢？
	 * 可以用如下的方式来记忆"/"：如果"/"是给服务器用的，则代表当前的web工程，
	 * 如果"/"是给浏览器用的，则代表webapps目录。
	 * 
	 * ServletContext.getRealPath(String path)获取资源的绝对路径
	 * 
	 * 使用 request.getContextPath()代替"/项目名称"
	 * 
	 * 
	 * 
	 */

}
