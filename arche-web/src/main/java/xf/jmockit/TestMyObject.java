package xf.jmockit;

import static org.junit.Assert.*;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Verifications;

import org.junit.Test;

// Expectations：期望，指定的方法必须被调用
// StrictExpectations：严格的期望，指定方法必须按照顺序调用
// 未mock方法返回null
public class TestMyObject {

	// 被修饰的对象将会被Mock，对应的类和实例都会受影响（同一个测试用例中）
	//用@Mocked标注的对象，不需要赋值，jmockit自动mock；当前和未来的实例都会被mock
	
	@Test  
	public void testHello() {
		
		new MockUp<MyObject>() {
		    @Mock
		    public double getDouble(double d) {
		        return 1.0;
		    }
		};
		
		MyObject mockInstance = new MyObject();
		
		assertEquals(1.0, mockInstance.getDouble(10.0),0.0000001);
	}
	
	
//	// MockUp调用原始方法会java.lang.StackOverflowError
//	@Test  
//	public void testGetDouble() {		
//		
//		new MockUp<MyObject>() {
//			
//		    @Mock
//		    public double getDouble(double d) {
//		        if(d == 1.0){
//		        	return 1.0;
//		        }else{
//		        	return new MyObject().getDouble(d);
//		        }
//		    }
//		};
//		
//		MyObject mockInstance = new MyObject();
//		
//		assertEquals(1.0, mockInstance.getDouble(10.0),0.0000001);
//	}
	
	

}
