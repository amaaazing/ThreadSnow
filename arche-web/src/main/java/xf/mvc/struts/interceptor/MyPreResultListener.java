package xf.mvc.struts.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.PreResultListener;

/**
 * 
 * 在execute()方法返回之前执行
 *
 */
public class MyPreResultListener implements PreResultListener{

	@Override
	public void beforeResult(ActionInvocation invocation, String resultCode) {
		System.out.println("resultCode :"+ resultCode);
	}

	
	
}
