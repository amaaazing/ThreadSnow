package xf.jmockit;

import static org.junit.Assert.assertEquals;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

public  class TestedClass {
    public String doSomething() throws InterruptedException {
        StreamGobbler sg1 = new StreamGobbler("OUTPUT");
        sg1.start();

        StreamGobbler sg2 = new StreamGobbler("ERROR");
        sg2.start();

        sg1.join(5000);
        sg2.join(5000);

        String output1 = sg1.getOutput();
        String output2 = sg2.getOutput();
        return output1 + '|' + output2;
    }
    
	@Test
	public void useStreamGobbler(@Mocked StreamGobbler sg) throws Exception {
		
		// @Mocked和Expectations 配合使用
		
	    new Expectations() {{
	    	
	        new StreamGobbler("OUTPUT").getOutput(); result = "test output";
	        new StreamGobbler("ERROR").getOutput(); result = "";
	        
	    }};

	    String output = new TestedClass().doSomething();

	    assertEquals("test output|", output);
	}
	
}
