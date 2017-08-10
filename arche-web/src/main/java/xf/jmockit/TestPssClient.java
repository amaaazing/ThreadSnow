package xf.jmockit;

import static org.junit.Assert.*;
import mockit.Mock;
import mockit.MockUp;

import org.junit.Test;

public class TestPssClient {

	@Test
	public void testPssClient(){
		
		PssClient mockInstance = new MockUp<PssClient>(){
			
//			@Mock 
//			public PssClient getInstance(long time){
//				return PssClient.getInstance(time);
//			}
			
			@Mock
			public MyObject getMyObject(){
				return new MockUp<MyObject>(){
					@Mock
					public double getDouble(double d){
						return 2.0;
					}
				}.getMockInstance();
			}

			
		}.getMockInstance();
		
		assertEquals(2.0, mockInstance.getMyObject().getDouble(100.0),0.00001);
		
		
	}
}
