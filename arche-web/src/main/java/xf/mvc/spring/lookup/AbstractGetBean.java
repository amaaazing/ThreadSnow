package xf.mvc.spring.lookup;

public abstract class AbstractGetBean {

	public abstract User getBean();
	
	public void showMe(){
		this.getBean().showMe();
	}
}
