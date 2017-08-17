package xf.mvc.spring.start;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

public class TestAction {

	/**
	 * 这段代码的功能：
	 * （1）读取配置文件spring/spring-start.xml，验证配置文件，放入内存中
	 * （2）根据spring-start.xml中的配置找到对应类的配置，并根据反射实例化
	 * （3）使用
	 */
	public static void main(String[] args) {
		
		
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-start.xml");
		
		// bean的加载
		Action action = (Action) context.getBean("TheAction");
		
		System.out.println(action.execute("A QQLikeThis"));
		
		//DefaultListableBeanFactory;
		
		XmlBeanFactory bg = new XmlBeanFactory(new ClassPathResource("spring/spring-start.xml"));
		// 不同来源的资源文件都有相应的Resource实现。
		/**
		 * 1.ClassPathResource --> classpath:spring/spring-start.xml
		 * 2.FileSystemResource --> xml来自文件系统
		 * 3.UrlResource --> 来自url资源
		 * 4.InputStreamResource --> 来自一个InputStream的xml
		 * 5.BtyeArrayResource --> 来自Btye数组
		 * 
		 * 有了Resource接口便可对所有资源文件进行统一管理 
		 */
		
		// 当通过Resource相关类完成了对配置文件进行封装后，配置文件的读取工作由XmlBeanDefinitionReader负责
		
		/**
		 * 进度spring 源码解析：46页
		 */
		

	}

	/**
	 * 说明：
	 * （1）根据"spring-start.xml"创建了一个ApplicationContext实例，并从此实例中获取我们所需的Action实现类
	 * （2）我们将spring-start.xml中的配置稍加修改<bean id="TheAction" class="net.xiaxin.spring.qs.LowerAction"/>
	 * 程序的实现也跟着改变
	 * （3）UpperAction和LowerAction的Message属性均由Spring通过读取配置文件（bean.xml）动态设置。
	 * （4）客户代码（这里就是我们的测试代码）仅仅面向接口编程，而无需知道实现类的具体名称。同时，
	 * 我们可以很简单的通过修改配置文件来切换具体的底层实现类。
	 * （5）上面的例子中，我们通过Spring，在运行期动态将字符串“A QQLikeThis” 注入到Action实现类的Message属性中。
	 */
}
