package xf.practice.callback;




/**
 * 将老师进行抽象，
 * @author xinzhenqiu1
 *
 */
public class Teacher implements CallBack {

	private Student student;
	
	public Teacher(Student student){
		this.student = student;
	}
	
	public void askQuestion(){
		// 回调的核心就是回调方将本身即this传递给调用方
		student.resolveQuestion(this);
	}
	
	public void tellAnswer(int answer) {
		
		System.out.println("the answer is : " + answer);

	}

}
