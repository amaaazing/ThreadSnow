package xf.mvc.spring.lookup;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestLookup {
	
	/**
	 * 除了配置文件外，AbstractGetBean中的抽象方法还没有被实现，怎么可能直接调用呢？---> spring的获取器注入
	 * 
	 * 它是把一个方法声明为返回某种类型的bean，但实际要返回的bean是在配置文件里面配置的。
	 * 
	 * 此方法可用在设计有些可插拔的功能上，解除程序依赖
	 */
	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/lookup.xml");
		
		AbstractGetBean test = (AbstractGetBean) context.getBean("getBeanTest");
		
		test.showMe();
	}
	
	

}
