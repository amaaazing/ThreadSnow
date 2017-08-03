package gce.one;

import java.util.Iterator;

/**
 * 下压栈（链表实现）
 * 
 * 算法特性：
 * 1.可以处理任意类型的数据
 * 2.所需的空间总是和集合的大小成正比
 * 3.操作所需的时间总是和集合的大小无关
 * 
 * 栈的顶部即为表头
 * 实例变量first指向栈顶
 * 元素的添加和删除都在表头
 * 
 * @author terry
 *
 * @param <Item>
 */
public class Stack<Item> implements Iterator<Item> {

	// 栈顶，最近添加的元素
	private Node first;
	// 元素的数量
	private int N = 0;
	
	// 定义节点的嵌套类
	private class Node{
		private Item item;
		private Node next;
	}
	
	public boolean isEmpty(){
		return N == 0; // 或者 first == null
	}
	
	public int size(){
		return N;
	}
	
	// 向栈顶添加元素
	public void push(Item item){
		Node oldFirst = first;
		first = new Node();
		first.item = item;
		first.next = oldFirst;
		N++;
	}
	
	// 从栈顶删除元素
	public Item pop(){
		Item item = first.item;;				
		first = first.next;
		N--;		
		return item;
	}
	public boolean hasNext() {		
		return first == null;
	}

	public Item next() {
		Item item = first.item;
		first = first.next;
		return item;
	}

}
