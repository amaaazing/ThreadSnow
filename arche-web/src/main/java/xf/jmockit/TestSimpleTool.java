package xf.jmockit;

import static org.junit.Assert.*;

import org.junit.Test;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;

public class TestSimpleTool {


	@Test
	public void testSimpleTool(){
		SimpleTool mockInstance = new MockUp<SimpleTool>(){
			@Mock // 公有方法
			public String fun1(String str) {
				return "mock fun1";
			}
			
			@Mock // 私有方法
			private String privateMethod(String str) {
				return "mock privateMethod";
			}
			
			@Mock // 私有静态方法
			private String privateStaticMthod(String str){
				return "mock privateStaticMthod";
			}
		}.getMockInstance();
		
		// Mock 私有属性
		Deencapsulation.setField(mockInstance, "name", "mockValue");
		
		assertEquals("mockValue", mockInstance.fetchData("1"));		
		assertEquals("mock fun1", mockInstance.fun1("1"));
		assertEquals("mock privateStaticMthod", mockInstance.fun4("1"));
		// 调用没有mock的方法，发生的是真实的调用
		System.out.println(mockInstance.fun3("1"));
		
	}
	
	
	@Test
	public void testUpper() throws Exception {
		// mock 接口
	    SimpleInterface mockInstance =  new MockUp<SimpleInterface>() {
	        @Mock
	        public String getCityName() {
	            return "BEIJING(MOCK)";
	        }

	        @Mock
	        public String getAreaName() {
	            return "HUABEI(MOCK)";
	        }
	        
	    }.getMockInstance(); 
	    
	    System.out.println(mockInstance.getCityName());
	    System.out.println(mockInstance.getAreaName());

	    SimpleInterfaceImpl simpleInterfaceImpl = new SimpleInterfaceImpl();
	    System.out.println(simpleInterfaceImpl.getCityName());
	    System.out.println(simpleInterfaceImpl.getAreaName());
	}
	
	@Test
	public void testField() {
		final SimpleTool o = new SimpleTool();
		// Mock 私有属性
		Deencapsulation.setField(o, "name", "mockValue");
		
		String actual = o.fetchData("any string");
		assertEquals("mockValue", actual);
	}
}
