package xf.mvc.spring.customtag;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

// 创建一个Handle文件，扩展自NamespaceHandlerSupport，将组件注册到Spring容器
public class MyNamespaceHandler extends NamespaceHandlerSupport{

	@Override
	public void init() {
		
		registerBeanDefinitionParser("user", new UserBeanDefinitionParser());
		// 可注册多个标签解析器
		
	}
	
	/**
	 * 当spring遇到自定义标签<user:aaa这样类似于以user开头的标签
	 * 就会把这个元素丢给对应的UserBeanDefinitionParser去解析
	 * 
	 */

}
