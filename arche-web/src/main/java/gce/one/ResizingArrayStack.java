package gce.one;

import java.util.Iterator;
/**
 * 下压栈（LIFO）
 * 能够动态调整数组大小的实现
 * 
 * 算法特性：
 * 1.每项操作的用时都与几何大小无关
 * 2.空间需求总是不超过集合大小乘以一个常数
 * 
 * 缺点：
 * 某些push和pop操作会调整数组大小，这项操作的耗时和栈的大小成正比
 * @author terry
 *
 * @param <Item>
 */
public class ResizingArrayStack<Item> implements Iterator<Item> {
	 
	// 栈元素
	// Item[] a = new Item[1];创建泛型数组在java中是不允许的 
	private Item[] a = (Item[]) new Object[1];
	// 元素数量
	private int N = 0;
	
	public boolean isEmpty(){
		return N == 0 ;
	}
	
	public int size(){
		return N;
	}
	
	public void push(Item item){
		// 检测栈是否已满
		if(N == a.length){
			resize(2 * a.length);
		}
		a[N++]=item;		
	}
	
	public Item pop(){
		// 如果栈非空，栈的顶部位于a[N-1]
		Item item = a[--N];
		// 避免对象游离，内存得不到释放
		a[N] = null;
		// 删除栈顶元素，如果数组太大，长度减半，处于半满状态
		if(N > 0 && N == a.length/4){
			resize(a.length/2);
		}					
		return item;
	}		
	
	private void resize(int capacity) {
		Item[] temp = (Item[]) new Object[capacity];
		for(int i=0;i<N;i++){
			temp[i] = a[i];
		}
		a = temp;
	}

	public boolean hasNext() {
		return N > 0;
	}

	// 支持后进先出的迭代
	public Item next() {
		return a[--N];
	}

	public void remove() {
		
	}

}
