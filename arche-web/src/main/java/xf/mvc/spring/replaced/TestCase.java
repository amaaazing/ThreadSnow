package xf.mvc.spring.replaced;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestCase {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/replacedMethod.xml");
		
		TestChangeMethod test = (TestChangeMethod) context.getBean("testChangeMethod");
		
		test.changeMe();
	}
}
