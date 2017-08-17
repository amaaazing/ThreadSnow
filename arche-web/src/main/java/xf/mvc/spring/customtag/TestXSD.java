package xf.mvc.spring.customtag;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * spring.handlers和spirng.schemas默认位置在工程的META-INF/下面,参看：类DefaultNamespaceHandlerResolver
 * 
 */
public class TestXSD {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-test.xml");
		
		User user = (User) context.getBean("testBean");
		
		System.out.println(user.getUserName() +":"+user.getEmail());
		
		// TODO 146
	}
}
