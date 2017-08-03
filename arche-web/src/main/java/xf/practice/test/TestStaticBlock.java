package xf.practice.test;

public class TestStaticBlock {

	public static String str = "繁华落尽谁我怜";
	
	public static void main(String[] args) {
		System.out.println(str);
	}
	
	static{
		str = "豪情诉云飞秋风";
	}
}
