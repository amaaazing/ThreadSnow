package xf.practice.test;

public class TestStaticBlock {

	public static String str = "繁华落尽谁我怜";

	static{
		str = str +"\n豪情诉云飞秋风";
	}

	public static void main(String[] args) {


		System.out.println(str);
		System.out.println(System.getProperty("file.encoding"));// 查看系统默认字符集
		System.out.println(foo());// 返回 1
	}

	public static int foo(){
		try {
			return 0;
		}finally {
			return 1;
		}
	}

}
