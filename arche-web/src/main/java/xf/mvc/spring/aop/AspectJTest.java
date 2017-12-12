package xf.mvc.spring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

// 创建advisor step 1 : 定义增强
@Aspect
public class AspectJTest {

	// step 2 ：定义切点,对所有类的test方法进行增强
	@Pointcut("execution(* *test(..))")
	public void test(){
		
	}
	
	// 增强的方式
	@Before("test()")
	public void beforeTest(){
		System.out.println("beforeTest");
	}
	
	// 增强的方式
	@After("test()")
	public void afterTest(){
		System.out.println("afterTest");
	}
	
	// 增强的方式
	@Around("test()")
	public Object aroundTest(ProceedingJoinPoint p){
		System.out.println("before1");
		
		Object o = null;
		
		try {
			o = p.proceed();
		} catch (Throwable e) {}
		
		System.out.println("after1");
		
		return o;
	}
	
	// step 3：配置文件
}
