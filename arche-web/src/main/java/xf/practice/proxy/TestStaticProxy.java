package xf.practice.proxy;

import xf.practice.test.SystemCode;

public class TestStaticProxy {
	
	public static void main(String[] args) {
		BussinessProcessorImpl bp = new BussinessProcessorImpl();
		BussinessProxy bussinessProxy = new BussinessProxy(bp);
		bussinessProxy.process();
		
		System.out.println(SystemCode.valueOf(1));// 输出：Groupon
	}

}
