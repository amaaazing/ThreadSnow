package xf.mvc.spring.customtag;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * 
 * 实现BeanDefinitionParser接口，用来解析XSD文件中的定义和组件定义
 *
 */
public class UserBeanDefinitionParser extends AbstractSingleBeanDefinitionParser{

	// Element 对应的类User
	protected Class<?> getBeanClass(Element element) {
		return User.class;
	}
	
	// 从Element中解析并提取对应的元素
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		
		String userName = element.getAttribute("userName");
		String email = element.getAttribute("email");
		
		// 将提取的数据放到BeanDefinitionBuilder中，待完成所有bean的解析后统一注册到beanFactory中
		if(StringUtils.hasText(userName)){
			builder.addPropertyValue("userName", userName);
		}
		
		if(StringUtils.hasText(email)){
			builder.addPropertyValue("email", email);
		}
	}
	
}