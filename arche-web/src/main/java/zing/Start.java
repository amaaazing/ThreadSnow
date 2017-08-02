package zing;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Start {

	/**
	 * 可以通过WebApplicationContextUtils.getWebApplicationContext方法在web应用中获取applicationcontext的引用
	 * 
	 * @param args
	 * @throws IOException
	 */
    public static void main(String[] args) throws IOException{  
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(  
                new String[] { "classpath:applicationContext.xml" });  
        context.start();  
        System.out.println("服务启动成功!");  
        System.in.read(); // 为保证服务一直开着，利用输入流的阻塞来模拟  
    }
    
    
    // log4j 的使用
    private static Logger log = Logger.getLogger(Start.class);            
    static{
    	/**
    	 * <bean id="start" class="zing.Start" lazy-init = "true"></bean>
    	 * 
    	 * 懒加载---就是在spring容器启动的是先不把所有的bean都加载到spring的容器中去，而是在当需要用的时候，才把这个对象实例化到容器中。 
    	 * 
    	 * 使用lazy-init = "true"配置，静态代码块不会执行
    	 * 
    	 * lazy-init 比default-lazy-init的优先级高。default-lazy-init 配置在beans上
    	 * 
    	 */
    	log.error("log4j run!");
    }
}
