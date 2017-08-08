package xf.practice.test;

public class Demo {


	public static void main(String[] args) {
		A a2 = new B();

		System.out.println(a2.show(new A()));
		System.out.println(a2.show(new B()));
		System.out.println(new B().show(new A()));
		System.out.println(new B().show(new B()));
		
	}
}

class A {
	public String show(A obj){
		return "A and A";
	}
}

class B extends A {
	
	public String show(B obj){
		return "B and B";
	}
	
	@Override
	public String show(A obj){
		return "B and A";
	}
}