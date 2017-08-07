package xf.mvc.struts;

import com.opensymphony.xwork2.ActionSupport;

public class MyAction extends ActionSupport{

	private static final long serialVersionUID = 8521802907326608686L;

	private EndService endService;
	

	public String execute() throws Exception {
    	System.out.println(endService.getInfo());
        return SUCCESS;
    }
    
    public EndService getEndService() {
		return endService;
	}

	public void setEndService(EndService endService) {
		this.endService = endService;
	}
}
