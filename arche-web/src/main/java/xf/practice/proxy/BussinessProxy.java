package xf.practice.proxy;



public class BussinessProxy implements BussinessProcessor{

	/**
	 * 静态代理：由程序员创建或特定工具自动生成源代码，再对其编译。
	 * 在程序运行前，代理类的.class文件就已经存在了。 
	 */
	BussinessProcessorImpl bussinessProcessorImpl;// 真正要执行业务的对象
	
	public BussinessProxy(BussinessProcessorImpl bussinessProcessorImpl){
		this.bussinessProcessorImpl = bussinessProcessorImpl;
	}

	public void process() {
		System.out.println("代理类增强了bussinessProcessorImpl的功能,在原有逻辑不变的前提下，");
		System.out.println("业务代码之前，处理。。。");
		bussinessProcessorImpl.process();
		System.out.println("业务代码之后，处理。。。");
	}
	
}
