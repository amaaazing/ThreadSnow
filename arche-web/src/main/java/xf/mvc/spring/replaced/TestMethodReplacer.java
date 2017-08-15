package xf.mvc.spring.replaced;

import java.lang.reflect.Method;

import org.springframework.beans.factory.support.MethodReplacer;

public class TestMethodReplacer implements MethodReplacer{

	@Override
	public Object reimplement(Object obj, Method method, Object[] args)
			throws Throwable {
		
		System.out.println(obj instanceof TestChangeMethod); // true
		System.out.println(method.getName()); // changeMe
		System.out.println("我替换了原有方法");
		return null;
	}

}
