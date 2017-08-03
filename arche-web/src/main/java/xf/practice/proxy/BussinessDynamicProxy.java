package xf.practice.proxy;

import java.io.FileOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import sun.misc.ProxyGenerator;


public class BussinessDynamicProxy implements InvocationHandler{
	
	public Object target;
	
	public BussinessDynamicProxy(Object target){
		this.target = target;
	}

	/**
	 * 参数proxy到底是什么？又有什么作用？
	 * 先看参数注释：the proxy instance that the method was invoked on
	 * proxy 是代理类的一个实例
	 * 那参数proxy是谁传的？即这里的invoke方法是谁调用的咧
	 * 调用父类Proxy中的h的invoke()方法.即InvocationHandler.invoke()，并把自个（$Proxy0 == this）做首参传了进去。
	 * 
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		System.out.println(proxy.getClass().getName());
		// 动态代理的实现：合成你要的代理类,用什么合成,就是你传入的InvocationHandler接口的实现类和要代理的接口.
		// 把生成的字节码保存起来
		// 拿到$Proxy0.class,再反编译
		byte[]  b= ProxyGenerator.generateProxyClass(proxy.getClass().getSimpleName(),proxy.getClass().getInterfaces());
        FileOutputStream out = new FileOutputStream("./"+proxy.getClass().getSimpleName()+".class");
        out.write(b);
        out.flush();
        out.close();

		System.out.println("dynamic-before");
		Object result = method.invoke(target, args);
		System.out.println("dynamic-after");
		
		return result;
	}

}
