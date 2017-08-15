package xf.mvc.spring;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.AutowireCandidateQualifier;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.MethodOverrides;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.core.io.Resource;
import org.w3c.dom.Element;
/**
 * 
 * spring 源码的一点探索
 *
 */
public class AbstractBeanDefinition {

	private volatile Object beanClass;

	// bean的作用范围，对应bean属性scope
	//private String scope = SCOPE_DEFAULT;

	// 是否单例，来自bean属性scope
	private boolean singleton = true;

	// 是否原型，来自bean属性scope
	private boolean prototype = false;

	// 是否是抽象，对应bean属性abstract
	private boolean abstractFlag = false;

	// 是否延迟加载，对应bean属性lazy-init
	private boolean lazyInit = false;

	// 自动注入模式，对应bean属性autowire
	//private int autowireMode = AUTOWIRE_NO;

	// 依赖检查
	//private int dependencyCheck = DEPENDENCY_CHECK_NONE;

	// 用来表示一个bean的实例化依赖另一个bean先实例化，对应bean属性depend-on
	private String[] dependsOn;

	/**
	 * 对应bean属性autowire-candidate
	 * 如果设置为false，容器在查找自动装配对象时，将不考虑该bean
	 * 但是此bean本身会使用自动装配来注入其他bean的
	 */
	private boolean autowireCandidate = true;

	// 自动装配时当出现多个bean的候选者时，将作为首选择，对应bean属性primary
	private boolean primary = false;

	// 用于记录Qualifier，对应子元素qualifier
	private final Map<String, AutowireCandidateQualifier> qualifiers =
			new LinkedHashMap<String, AutowireCandidateQualifier>(0);

	// 允许访问非公开的构造器和方法，程序设置
	private boolean nonPublicAccessAllowed = true;

	// 是否以一种宽松的模式解析构造函数，默认为true；
	private boolean lenientConstructorResolution = true;

	// 记录构造函数注入属性，对应bean属性constructor-arg
	private ConstructorArgumentValues constructorArgumentValues;

	// 普通属性集合
	private MutablePropertyValues propertyValues;

	// 方法重写的持有者，记录lookup-method、replaced-method元素
	private MethodOverrides methodOverrides = new MethodOverrides();

	// 对应bean属性factory-bean
	private String factoryBeanName;

	// 对应bean的factory-method
	private String factoryMethodName;

	// 初始化方法，对应bean属性init-method
	private String initMethodName;

	// 销毁方法，对应bean的destroy-method
	private String destroyMethodName;

	// 是否执行init-method，程序设置
	private boolean enforceInitMethod = true;

	// 是否执行destroy-method，程序设置
	private boolean enforceDestroyMethod = true;

	// 是否是用户定义的而不是应用程序本身定义的，创建AOP时候为true，程序设置
	private boolean synthetic = false;

	// 定义这个bean的应用，APPLICATION：用户，INFRASTRUCTURE：完全内部使用，与用户无关；SUPPORT：某些复杂配置的一部分
	// 程序设置
	private int role = BeanDefinition.ROLE_APPLICATION;

	// bean的描述信息
	private String description;
	
	// 这个bean定义的资源
	private Resource resource;
	
	// 省略setter\getter
	
	
	/**
	 * Process the given bean element, parsing the bean definition
	 * and registering it with the registry.
	 */
	protected void processBeanDefinition(Element ele, BeanDefinitionParserDelegate delegate) {
		// 默认标签的解析与提取过程
		BeanDefinitionHolder bdHolder = delegate.parseBeanDefinitionElement(ele);
		if (bdHolder != null) {
			// 从语义上分析：如果需要的话就对beanDefinition进行装饰。这段代码什么功用呢？
			/**
			 * 应用场景：
			 * <bean id="teacher" class="xf.mvc.spring.lookup.Teacher">
			 * 		<mybean:user username="test"/>
			 * </bean>
			 * 
			 * 当spring中的bean使用的是默认的标签配置，但是子元素使用了自定义的配置时，这段代码就起作用了。
			 * 
			 * 注意：这个自定义并不是以bean的形式出现的。
			 */
			bdHolder = delegate.decorateBeanDefinitionIfRequired(ele, bdHolder);
			try {
				// Register the final decorated instance.
				BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry());
			}
			catch (BeanDefinitionStoreException ex) {
				getReaderContext().error("Failed to register bean definition with name '" +
						bdHolder.getBeanName() + "'", ele, ex);
			}
			// Send registration event.
			getReaderContext().fireComponentRegistered(new BeanComponentDefinition(bdHolder));
		}
	}
	
}
