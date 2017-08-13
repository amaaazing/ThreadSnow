package xf.mvc.struts.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

// 也可以extends abstractInterceptor
// ActionInvocation可以拿到context，可以拿到session，
public class MyInterceptor implements Interceptor{

	private String hello;
	
	
	@Override
	public void destroy() {
		
	}

	@Override
	public void init() {
		
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		System.out.println("interceptor 一个简单的实现"+ hello);
		
		
		// 注册监听器
		invocation.addPreResultListener(new MyPreResultListener());
		
		
		// 可以参考ActionInvocation类
		String result = invocation.invoke();
		
		System.out.println("interceptor finish");
		
		return result;
	}

	public String getHello() {
		return hello;
	}

	public void setHello(String hello) {
		this.hello = hello;
	}

}
