package xf.practice.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import xf.practice.proxy.BussinessProcessorImpl;


public class ClazzTest {

	public static void main(String[] args) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		try {
			Class<?> bpClazz = cl.loadClass("com.zing.BussinessProcessorImpl");

			Constructor<?> constructor = bpClazz.getDeclaredConstructor((Class<?>[])null);

			BussinessProcessorImpl bpl = (BussinessProcessorImpl) constructor.newInstance();
			bpl.process();
			bpl.setName("clazz");
			System.out.println(Math.ceil(550/100));
					
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
