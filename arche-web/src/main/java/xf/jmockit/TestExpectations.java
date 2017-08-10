package xf.jmockit;

import org.junit.Test;

import mockit.Mocked;

public class TestExpectations {

	// 用@Mocked标注的对象，不需要赋值，jmockit自动mock
	// 当前和未来的实例都会被mock
	@Mocked
	MyObject mockInstance;
	
	/**
	 * @Injectable 意味着只有被分配mock字段的实例将具有mock行为
	 * 
	 * 并自动关联到被测试类，而不需要通过其他文件类似spring的配置文件等来进行关联;
	 */
	
	// Mock的函数是不计算到单元测试覆盖率里边的
	
	
	@Test
	public void testExpectations(){
		
	}
	
	// 通常，JUnit / TestNG测试方法不允许有参数。 然而，当使用JMockit时，允许这样的模拟参数。
	@Test
	public void testExpectations(@Mocked MyObject mockInstance){
		
	}
}
