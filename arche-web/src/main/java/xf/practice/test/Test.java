package xf.practice.test;

import java.io.*;
import java.util.LinkedList;

public class Test {

	public static String FILE_PATH = "F:\\Amys\\data.txt";
	public void call(){
		System.out.println("sub Test call invoked " );
		//System.out.println(fr);
		// fr 为null,因为实例化Test时，是在父类的构造函数中调用的子类重写的call(),此时子类还没有被实例出来
	}
	public static void main(String[] args) throws IOException {

		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
				new FileOutputStream(FILE_PATH)
		));
		out.writeDouble(3.1415926);
		out.writeUTF("我喜欢芳儿");
		out.writeUTF("I miss fanger");
		out.close();
		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(FILE_PATH)));
		System.out.println(in.readDouble());
		System.out.println(in.readUTF());
		System.out.println(in.readUTF());
		in.close();


//		BufferedReader in = new BufferedReader(new StringReader(TextFile.read("F:\\Amy\\E28.java")));
//		// 输出到文件
//		// PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH)));
//		PrintWriter out = new PrintWriter(FILE_PATH);// java1.5里面添加了一个辅助构造器，直接创建文本文件
//		int lineCount =1;
//		String s;
//		while ((s = in.readLine()) !=null){
//			out.println(lineCount++ + ": " +s);
//			// 要为所有输出文件调用close(),不然缓存区内容不会被刷新清空
//		}
//		out.close(); // 如果不调用close()，文件里面没有数据


//	    DataInputStream in = new DataInputStream(
//	    		new BufferedInputStream(
//	    				new FileInputStream("F:\\Amy\\ClassCasts.java")
//				)
//		);
//	    while (in.available() !=0)
//			// 一个字节一个字节的读取字符
//			System.out.println((char) in.readByte());

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
		
//		Integer[] array = new Integer[]{1,2,3,4,5,6};
//		List<Integer> list = Arrays.asList(array);
//		
//		System.out.println(ListUtils.sum(list, 6));

		{
			LinkedList<String> result = read("F:\\Amy\\ClassCasts.java");
		}

//		while(!result.isEmpty())
//			System.out.println(result.removeLast());
//		ListIterator<String> it = result.listIterator(result.size());
//		while(it.hasPrevious())
//			System.out.println(it.previous());
	}
	public static LinkedList<String> read(String fileName) {
		LinkedList<String> context = new LinkedList<String>();
		try {
			BufferedReader in= new BufferedReader(new FileReader(
					new File(fileName).getAbsoluteFile()));
			try {
				String s;
				while((s = in.readLine()) != null) {
					context.add(s);
				}
			} finally {
				in.close();
			}
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
		return context;
	}

}
