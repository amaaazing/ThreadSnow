package xf.practice.callback;

public class CallBackTest {

	public static void main(String[] args) {
		
		Foo foo = new Foo();
		Teacher teacher = new Teacher(foo);
		
		teacher.askQuestion();
	}
}
