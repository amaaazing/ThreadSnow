package xf.mvc.servlet;

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
public class MyFilter {

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
	
	
}
