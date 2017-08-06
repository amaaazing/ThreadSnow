package xf.mvc.servlet;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;


/**
 * JavaWeb中的监听器是Servlet规范中定义的一种特殊类，它用于监听web应用程序中的ServletContext, 
 * HttpSession和 ServletRequest等域对象的创建与销毁事件，以及监听这些域对象中的属性发生修改的事件。
 * 
 * Servlet监听器的分类:用于监听的事件源分别为ServletContext，HttpSession和ServletRequest这三个域对象
 *Servlet规范针对这三个对象上的操作，又把多种类型的监听器划分为三种类型：
    监听域对象自身的创建和销毁的事件监听器。
    监听域对象中的属性的增加和删除的事件监听器。
    监听绑定到HttpSession域中的某个对象的状态的事件监听器。

   ************在web.xml文件中注册监听器
 */
public class MyListener {

	// 监听ServletContext域对象的创建和销毁
	/**
	 * ServletContextListener接口用于监听ServletContext对象的创建和销毁事件。实现了ServletContextListener接口的类都可以对ServletContext对象的创建和销毁进行监听。

　　当ServletContext对象被创建时，激发contextInitialized (ServletContextEvent sce)方法。

　　当ServletContext对象被销毁时，激发contextDestroyed(ServletContextEvent sce)方法。

　　ServletContext域对象创建和销毁时机：
　　　　创建：服务器启动针对每一个Web应用创建ServletContext
　　　　销毁：服务器关闭前先关闭代表每一个web应用的ServletContext
	 * 
	 */
	
	// 监听HttpSession域对象的创建和销毁
	/**
	 * HttpSessionListener 接口用于监听HttpSession对象的创建和销毁
　　创建一个Session时，激发sessionCreated (HttpSessionEvent se) 方法
　　销毁一个Session时，激发sessionDestroyed (HttpSessionEvent se) 方法。 

<!--注册针对HttpSession对象进行监听的监听器-->
   <listener>
      <description>HttpSessionListener监听器</description>
      <listener-class>me.gacl.web.listener.MyHttpSessionListener</listener-class>
  </listener>
  <!-- 配置HttpSession对象的销毁时机 -->
  <session-config>
      <!--配置HttpSession对象的1分钟之后销毁 -->
      <session-timeout>1</session-timeout>
  </session-config>
	 * 
	 */
	
	// 监听ServletRequest域对象的创建和销毁
	/**
	 * ServletRequestListener接口用于监听ServletRequest 对象的创建和销毁
　　Request对象被创建时，监听器的requestInitialized(ServletRequestEvent sre)方法将会被调用
　　Request对象被销毁时，监听器的requestDestroyed(ServletRequestEvent sre)方法将会被调用

　　ServletRequest域对象创建和销毁时机：
　　　　创建：用户每一次访问都会创建request对象
　　　　销毁：当前访问结束，request对象就会销毁
	 * 
	 * 用户每一次访问都会创建request对象，当访问结束后，request对象就会销毁。
	 */
	
	// 监听域对象中属性的变更的监听器
	/**
	 * 域对象中属性的变更的事件监听器就是用来监听 ServletContext, HttpSession, HttpServletRequest 这三个对象中的属性变更信息事件的监听器。
　　这三个监听器接口分别是ServletContextAttributeListener, HttpSessionAttributeListener 和ServletRequestAttributeListener，
	这三个接口中都定义了三个方法来处理被监听对象中的属性的增加，删除和替换的事件，同一个事件在这三个接口中对应的方法名称完全相同，只是接受的参数类型不同。
	 * 
	 */
	
	// attributeAdded 方法
	/**
	 * 当向被监听对象中增加一个属性时，web容器就调用事件监听器的attributeAdded方法进行响应，这个方法接收一个事件类型的参数，
	 * 监听器可以通过这个参数来获得正在增加属性的域对象和被保存到域中的属性对象
	 * 
	 *  1 public void attributeAdded(ServletContextAttributeEvent scae)
		2 public void attributeReplaced(HttpSessionBindingEvent  hsbe)
		3 public void attributeRmoved(ServletRequestAttributeEvent srae)
	 * 
	 */
	
	// 感知Session绑定的事件监听器	
	/**
	 * 保存在Session域中的对象可以有多种状态：
	 * 		绑定(session.setAttribute("bean",Object))到Session中；
	 * 		从 Session域中解除(session.removeAttribute("bean"))绑定；
	 * 		随Session对象持久化到一个存储设备中；
	 * 		随Session对象从一个存储设备中恢复
	 * 
　　      Servlet 规范中定义了两个特殊的监听器接口"HttpSessionBindingListener和HttpSessionActivationListener"来帮助JavaBean 
		对象了解自己在Session域中的这些状态： ，实现这两个接口的类不需要 web.xml 文件中进行注册。
	 * 
	 */
	
	// HttpSessionBindingListener接口
	/**
	 * 实现了HttpSessionBindingListener接口的JavaBean对象可以感知自己被绑定到Session中和 Session中删除的事件
　　当对象被绑定到HttpSession对象中时，web服务器调用该对象的void valueBound(HttpSessionBindingEvent event)方法
　　当对象从HttpSession对象中解除绑定时，web服务器调用该对象的void valueUnbound(HttpSessionBindingEvent event)方法
	 */
	 class JavaBeanDemo1 implements HttpSessionBindingListener {

	    private String name;
	    
	    @Override
	    public void valueBound(HttpSessionBindingEvent event) {
	        System.out.println(name+"被加到session中了");
	    }

	    @Override
	    public void valueUnbound(HttpSessionBindingEvent event) {
	        System.out.println(name+"被session踢出来了");
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public JavaBeanDemo1(String name) {
	        this.name = name;
	    }
	}
	 
	/** 上述的JavaBeanDemo1这个javabean实现了HttpSessionBindingListener接口，那么这个JavaBean对象可以感知自己被绑定到Session中和
	// 从Session中删除的这两个操作，测试代码如下：
	          //将javabean对象绑定到Session中
	//          session.setAttribute("bean",new JavaBeanDemo1("测试代码"));
	          //从Session中删除javabean对象
	          session.removeAttribute("bean"); 
	 */
	 
	//HttpSessionActivationListener接口
/**	 实现了HttpSessionActivationListener接口的JavaBean对象可以感知自己被活化(反序列化)和钝化(序列化)的事件
	　　当绑定到HttpSession对象中的javabean对象将要随HttpSession对象被钝化(序列化)之前，web服务器调用该javabean对象的
	  void sessionWillPassivate(HttpSessionEvent event) 方法。这样javabean对象就可以知道自己将要和HttpSession对象一起被序列化(钝化)到硬盘中.
	　　当绑定到HttpSession对象中的javabean对象将要随HttpSession对象被活化(反序列化)之后，web服务器调用该javabean对象的
	  void sessionDidActive(HttpSessionEvent event)方法。这样javabean对象就可以知道自己将要和 HttpSession对象一起被反序列化(活化)回到内存中 
*/	
	 
	 
	 // 监听器(Listener)在开发中的应用
	 // 1.统计当前在线人数
	 // 2.自定义Session扫描器
}
