package xf.mvc.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyServletContext extends HttpServlet{

	/**
	 * ServletContext对象：实际上是一个map
	 * 
	 * WEB容器在启动时，它会为每个WEB应用程序都创建一个对应的ServletContext对象，它代表当前web应用。
	 * 
	 * 在编写servlet时，可以通过ServletConfig.getServletContext方法获得ServletContext对象。
	 * 
	 * 由于一个WEB应用中的所有Servlet共享同一个ServletContext对象，因此Servlet对象之间可以通过ServletContext对象来实现通讯。
	 * 
	 * ServletContext对象通常也被称之为context域对象。
	 * 
	 * ServletContext的应用
	 * 		1.多个Servlet通过ServletContext对象实现数据共享
	 * 		2.获取WEB应用的初始化参数。方法同ServletConfig
	 * 		3.用servletContext实现请求转发
	 * 		4.利用ServletContext对象读取资源文件(类装载器也可读)
	 * 
	 */
	
	// 3.用servletContext实现请求转发
	// 假设此ServletContextDemo4，访问的是ServletContextDemo4，浏览器显示的却是ServletContextDemo5的内容，这就是使用ServletContext实现了请求转发
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	             throws ServletException, IOException {
		ServletContext context = getServletContext();// 从父类GenericServlet中获取ServletContext对象
		RequestDispatcher rd = context.getRequestDispatcher("/servlet/ServletContextDemo");//获取请求转发对象(RequestDispatcher)
		rd.forward(request, response);//调用forward方法实现请求转发
	}
	
	
	/**
	 * 通过ServletContext对象读取src目录下的properties配置文件
	 * 
	 * @param response
	 * @throws IOException
	 */
	private void readSrcDirPropCfgFile(HttpServletResponse response) throws IOException {
		
		InputStream in = getServletContext().getResourceAsStream("/WEB-INF/classes/db1.properties");
		Properties prop = new Properties();
		prop.load(in);
		String driver = prop.getProperty("driver");		
		// 剩下代码片段，略		
	}

	/**
	 * 通过ServletContext对象读取webapp目录下的properties配置文件
	 * 
	 * @param response
	 * @throws IOException
	 */
	private void readWebappDirPropCfgFile(HttpServletResponse response) throws IOException {
		
		InputStream in = getServletContext().getResourceAsStream("/db2.properties");
		Properties prop = new Properties();
		prop.load(in);
		String driver = prop.getProperty("driver");		
		// 剩下代码片段，略		
	}

	/**
	 * 通过ServletContext对象读取xf.mvc包中的properties配置文件
	 * 
	 * @param response
	 * @throws IOException
	 */
	private void readPropCfgFile(HttpServletResponse response) throws IOException {
		
		InputStream in = getServletContext().getResourceAsStream("/WEB-INF/classes/xf/mvc/db3.properties");
		Properties prop = new Properties();
		prop.load(in);
		String driver = prop.getProperty("driver");		
		// 剩下代码片段，略		
	}
	
	
	/**
	 * 使用类装载器读取资源文件
	 * 
	 * 通过类装载器读取资源文件的注意事项:不适合装载大文件，否则会导致jvm内存溢出
	 * 
	 * ClassLoader loader = MyServletContext.class.getClassLoader();
	 * InputStream in = loader.getResourceAsStream("db1.properties");
	 * 
	 */
}
