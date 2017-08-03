package xf.practice.test;

import java.util.Arrays;
import java.util.List;

import xf.utility.ListUtils;

public class Test {

	public static void main(String[] args) {		
	    
//			ArrayList<String> list = new ArrayList<String>();
//			list.add("数学");
//			list.add("英语");
//			StudentUnmodifiableList s = new StudentUnmodifiableList("Tom", list);
//	        
//	        List<String> anotherList = s.getCourses();
//	        
//	        /**
//	         * throws java.lang.UnsupportedOperationException
//	         * should replace with s.addCourse(String course)
//	         */
////	        anotherList.add("计算机"); 
//	        
//	        s.addCourse("计算机");
//	        
//	        
//	        // never reached
//	        System.out.println("Tom's course.length = " + s.getCourses().size());
//		TestThreadInterrupter t= new TestThreadInterrupter();
//		Thread tt = new Thread(t);
//		tt.start();
//		Thread.yield();	
//		System.out.println("main");
//		tt.interrupt();
		
		Integer[] array = new Integer[]{1,2,3,4,5,6};
		List<Integer> list = Arrays.asList(array);
		
		System.out.println(ListUtils.sum(list, 6));
			
	}

}
