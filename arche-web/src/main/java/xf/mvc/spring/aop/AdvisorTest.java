package xf.mvc.spring.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class AdvisorTest {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-aop.xml");
		
		TestBean bean = (TestBean) context.getBean("test");
		
		bean.test();
		
	}
}
