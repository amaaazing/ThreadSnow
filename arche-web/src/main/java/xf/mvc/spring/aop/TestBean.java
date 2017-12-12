package xf.mvc.spring.aop;

public class TestBean {

	private String str = "testBean";
	
	public void test(){
		System.out.println("test");
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}
	
	
}
