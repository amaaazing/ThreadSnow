package xf.mvc.spring.start;

// LowerAction将其message属性与输入字符串相连接，并返回其小写形式
public class LowerAction implements Action {

	private String message;
	
	@Override
	public String execute(String str) {
		
		return (getMessage() + str).toLowerCase();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
