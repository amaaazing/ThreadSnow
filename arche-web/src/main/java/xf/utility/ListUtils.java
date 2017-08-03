package xf.utility;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

	// chops a list into non-view sublists of length L
	/**
	 * 切分list
	 * 
	 * @param list
	 * @param L 小list的长度
	 * @return
	 */
	public static <T> List<List<T>> chopped(List<T> list, final int L) {
				
		if (list == null || list.size() == 0 || L < 1) {
			return null;
		}
		
		final int N = list.size();
		
	    List<List<T>> parts = new ArrayList<List<T>>();
	    
	    for (int i = 0; i < N; i += L) {
	        parts.add(new ArrayList<T>(
	            list.subList(i, Math.min(N, i + L)))
	        );
	    }
	    return parts;
	}
	
	/**
	 * 数组递归求和
	 * 
	 * @param a
	 * @param n 数组的前n项和
	 * @return
	 */
	public static Integer sum(Integer[] arr, int n){
		  
	     return n == 0 ? 0 : sum(arr, n -1) + arr[n -1];
	}
	
	/**
	 * list递归求和
	 * 
	 * @param a
	 * @param n list的前n项和
	 * @return
	 */
	public static Integer sum(List<Integer> list, int n){
		 if(list == null || n < 0){
			 throw new IllegalArgumentException("Argument Error!");
		 }
		 
		 Integer[] arr = list.toArray(new Integer[list.size()]);
		 
	     return n == 0 ? 0 : sum(arr, n -1) + arr[n -1];
	}
}
