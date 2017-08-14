package xf.mvc.struts;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class MyAction extends ActionSupport implements ServletRequestAware{

	private static final long serialVersionUID = 8521802907326608686L;

	private EndService endService;
	
	private HttpServletRequest request;

	public String execute() throws Exception {
    	System.out.println(endService.getInfo());
    	
    	// 下面2种方法等价
    	ActionContext.getContext().put("xf", "hello world");// 可在jsp页面中通过${requestScope.xf}获取值
    	request.setAttribute("xf", "hello world");
    	
        return SUCCESS;
    }
    
    public EndService getEndService() {
		return endService;
	}

	public void setEndService(EndService endService) {
		this.endService = endService;
	}

	@Override // struts框架自动调用这个方法，通过自动注入的方式给request赋值
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	/*
	 * 在action中获取servlet的API，3种方法：
	 * 1.ActionContext.getContext(),然后调用里面的方法
	 * 2.action要实现这些接口ServletContextAware,ServletRequestAware,ServletResponseAware
	 * 3.ServletActionContext
	 * 
	 * 在Servlet中doGet和doPost方法可以通过ServletContext、HttpServletRequest、HttpServletResponse直接访问Servlet API。
	那Struts2中的Action没有HttpServletRequest、HttpServletResponse，它怎么访问Servlet API的？

	Struts2提供了三种方法访问Servlet API：
	ActionContext
	实现****Aware接口
	ServletActionContext、

	以上的对象都是以Map的形式存储的
	 * 
	 * 
	 */
	
	/**
	 * action的动态方法调用：
	 * 1.在struts.xml中配置method
	 * 2.在页面中：<form action="/actionName!methodName.action">
	 * 
	 */
}
