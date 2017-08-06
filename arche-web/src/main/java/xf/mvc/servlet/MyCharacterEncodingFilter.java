package xf.mvc.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter也称之为过滤器，它是Servlet技术中最激动人心的技术，WEB开发人员通过Filter技术，
 * 对web服务器管理的所有web资源：例如Jsp, Servlet, 静态图片文件或静态 html 文件等进行拦截，
 * 从而实现一些特殊的功能。例如实现URL级别的权限访问控制、过滤敏感词汇、压缩响应信息等一些高级功能。
 * 
 * web服务器在调用doFilter方法时，会传递一个filterChain对象进来，filterChain对象是filter接口中最重要的一个对 象，它也提供了一个doFilter方法，
 * 
 * Filter开发步骤
 * 		编写java类实现Filter接口，并实现其doFilter方法。
 *      在 web.xml 文件中使用<filter>和<filter-mapping>元素对编写的filter类进行注册，并设置它所能拦截的资源。
 * 
 */ 
public class MyCharacterEncodingFilter implements Filter{ // 使用Decorator模式包装request对象解决get和post请求方式下的中文乱码问题


	// Filter链
	/**
	 * 在一个web应用中，可以开发编写多个Filter，这些Filter组合起来称之为一个Filter链。
　　		web服务器根据Filter在web.xml文件中的注册顺序，决定先调用哪个Filter，当第一个Filter的doFilter方法被调用时，
		web服务器会创建一个代表Filter链的FilterChain对象传递给该方法。在doFilter方法中，
		开发人员如果调用了FilterChain对象的doFilter方法，则web服务器会检查FilterChain对象中是否还有filter，
		如果有，则调用第2个filter，如果没有，则调用目标资源。
	 * 
	 */
	
	// Filter的生命周期
	/**
	 * Filter的创建和销毁由WEB服务器负责。 web 应用程序启动时，web 服务器将创建Filter 的实例对象，并调用其init方法，
	 * 完成对象的初始化功能，从而为后续的用户请求作好拦截的准备工作，filter对象只会创建一次，init方法也只会执行一次。
	 * 通过init方法的参数，可获得代表当前filter配置信息的FilterConfig对象。
	 * 
	 * FilterConfig接口
	 * 用户在配置filter时，可以使用<init-param>为filter配置一些初始化参数，当web容器实例化Filter对象，调用其init方法时，
	 * 会把封装了filter初始化参数的filterConfig对象传递进来。因此开发人员在编写filter时，通过filterConfig对象的方法，就可获得：
　　		String getFilterName()：得到filter的名称。
　　		String getInitParameter(String name)： 返回在部署描述中指定名称的初始化参数的值。如果不存在返回null.
　　		Enumeration getInitParameterNames()：返回过滤器的所有初始化参数的名字的枚举集合。
　　		public ServletContext getServletContext()：返回Servlet上下文对象的引用。
	 * 
	 */
	
	// Filter高级开发
	/**
	 * 在filter中可以得到代表用户请求和响应的request、response对象，因此在编程中可以使用Decorator(装饰器)模式对request、response对象进行包装，
	 * 再把包装对象传给目标资源，从而实现一些特殊需求。
	 * 
	 * 当某个对象的方法不适应业务需求时，通常有2种方式可以对方法进行增强：
	 * 		1.编写子类，覆盖需增强的方法。
	 * 		2.使用Decorator设计模式对方法进行增强。
	 * 
	 * (1).实现用户自动登陆
	 * 思路是这样的：
　　		1、在用户登陆成功后，发送一个名称为user的cookie给客户端，cookie的值为用户名和md5加密后的密码。
　　		2、编写一个AutoLoginFilter，这个filter检查用户是否带有名称为user的cookie来，如果有，则调用dao查询cookie的用户名和密码是否和数据库匹配，
		匹配则向session中存入user对象（即用户登陆标记），以实现程序完成自动登陆。
		如果想取消自动登录，那么可以在用户注销时删除自动登录cookie
	 * (2)控制浏览器缓存页面中的静态资源
	 * (3)禁止浏览器缓存所有动态页面
	 * (4)统一全站字符编码
	 */
	
	// Decorator设计模式的实现
	/**
	 *  1.首先看需要被增强对象继承了什么接口或父类，编写一个类也去继承这些接口或父类。
　　		2.在类中定义一个变量，变量类型即需增强对象的类型。
　　		3.在类中定义一个构造函数，接收需增强的对象。
　　		4.覆盖需增强的方法，编写增强的代码。
	 * 
	 */
	
	// 使用Decorator设计模式增强request对象
	/**
	 * Servlet API 中提供了一个request对象的Decorator设计模式的默认实现类HttpServletRequestWrapper，
	 * HttpServletRequestWrapper 类实现了request 接口中的所有方法，但这些方法的内部实现都是仅仅调用了一下所包装的的 request 对象的对应方法，
	 * 以避免用户在对request对象进行增强时需要实现request接口中的所有方法。
	 * 
	 */

	private FilterConfig filterConfig;
	//设置默认的字符编码
	private String defaultCharset = "UTF-8";	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//得到过滤器的初始化配置信息
		this.filterConfig = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
        HttpServletRequest _request = (HttpServletRequest) request;
        HttpServletResponse _response = (HttpServletResponse) response;
        //得到在web.xml中配置的字符编码
        String charset = filterConfig.getInitParameter("charset");
        if(charset==null){
            charset = defaultCharset;
        }
        _request.setCharacterEncoding(charset);
        _response.setCharacterEncoding(charset);
        _response.setContentType("text/html;charset="+charset);
        
        MyCharacterEncodingRequest requestWrapper = new MyCharacterEncodingRequest(_request);
        // 注意这里：使用的是增强后的requestWrapper
        chain.doFilter(requestWrapper, _response);
	}

	@Override
	public void destroy() {
		
	}
	
	class MyCharacterEncodingRequest extends HttpServletRequestWrapper{
	    //定义一个变量记住被增强对象(request对象是需要被增强的对象)
	    private HttpServletRequest request;
	    //定义一个构造函数，接收被增强对象
	    public MyCharacterEncodingRequest(HttpServletRequest request) {
	        super(request);
	        this.request = request;
	    }
	    /* 覆盖需要增强的getParameter方法
	     * @see javax.servlet.ServletRequestWrapper#getParameter(java.lang.String)
	     * 
	     * 覆盖后的getParameter会自动被调用
	     */
	    @Override
	    public String getParameter(String name) {
	        try{
	            //获取参数的值
	            String value= this.request.getParameter(name);
	            if(value==null){
	                return null;
	            }
	            //如果不是以get方式提交数据的，就直接返回获取到的值
	            if(!this.request.getMethod().equalsIgnoreCase("get")) {
	                return value;
	            }else{
	                //如果是以get方式提交数据的，就对获取到的值进行转码处理
	                value = new String(value.getBytes("ISO8859-1"),this.request.getCharacterEncoding());
	                return value;
	            }
	        }catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }
	}
}
