package xf.utility;

import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListUtils {

	// chops a list into non-view sublists of length L
	/**
	 * 等分list
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
	 * @param arr
	 * @param n 数组的前n项和
	 * @return
	 */
	public static Integer sum(Integer[] arr, int n){
		  
	     return n == 0 ? 0 : sum(arr, n -1) + arr[n -1];
	}
	
	/**
	 * list递归求和
	 * 
	 * @param list
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

	/**
	 * 要深拷贝的类（T）需要继承 Serializable 接口
	 * list 深拷贝 改变listA中的Object不会影响listB中的Object，因为存储的对象已经不一样
	 * 相互不受影响
	 * 使用System.arraycopy()方法 是属性浅拷贝，包括使用list.addAll()等
	 * @param src
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> deepCopyList(List<T> src) throws Exception
	{
		List<T> dest = null;
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(src);
		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		ObjectInputStream in = new ObjectInputStream(byteIn);
		dest = (List<T>) in.readObject();
		return dest;
	}

	/**
	 * 将list根据pageSize转为2维list
	 *
	 * @param <T>
	 * @param ori 原始list,如果为空或者空list,返回size为1的list,get(0)为emptyList
	 * @param pageSize 分页大小
	 * @return
	 */
	public static <T> List<List<T>> pageList(List<T> ori, int pageSize) {
		//赋空值
		if (ori == null){
			ori= Collections.emptyList();
		}
		//如果每页大小大于总大小,直接返回本页
		if (pageSize >= ori.size()) {
			return Collections.singletonList(ori);
		}
		int maxPage = ori.size() / pageSize
				+ Math.min(ori.size() % pageSize, 1);
		List<List<T>> result = new ArrayList<List<T>>(maxPage);
		for (int currPage = 1; currPage <= maxPage; currPage++) {
			result.add(new ArrayList<T>(ori.subList((currPage - 1) * pageSize,
					Math.min((currPage) * pageSize, ori.size()))));
		}
		return result;
	}

	/**
	 *
	 * @Description: 将多个List转化为一个二维List
	 * eg:[1,"2",3],[2,"3",5]--->[[1,2],["2","3"],[3,5]]
	 * @param list
	 * @return
	 */
	public static <T> List<List<Object>> convertToTwoDimenList(List<T>... list) {
		Integer lengthOfPerList = null;
		for (List<T> obj : list) {
			if (lengthOfPerList == null) {
				lengthOfPerList = obj.size();
			}
			Assert.isTrue(lengthOfPerList == obj.size(), "每个List的长度必须相等");
		}

		List<List<Object>> twoDimenList = new ArrayList<List<Object>>(lengthOfPerList);
		for (int i = 0; i < lengthOfPerList; i++) {
			List<Object> objList = new ArrayList<Object>();
			for (List<T> obj : list) {
				objList.add(obj.get(i));
			}
			twoDimenList.add(objList);
		}

		return twoDimenList;
	}
}
