package xf.practice.proxy;



public class BussinessProcessorImpl implements BussinessProcessor{

	private String name;
	private Integer id;
	
	public void process() {
		System.out.println("业务接口实现类,也是委托类");	
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
