package gce.one;

import java.util.Iterator;

public class Queue<Item> implements Iterator<Item> {

	private Node first;
	private Node last;
	private int N = 0;
	
	private class Node{
		 Item item;
		 Node next;
	}
	
	public boolean isEmpty(){
		return N == 0;
	}
	
	public int size(){
		return N;
	}
	
	public void enqueue(Item item){
		// 链表的尾部添加
		Node oldLast = last;
		last = new Node();
		last.item = item;
		last.next = null;
		if(N == 0){
			first = last;
		}else{
			oldLast.next = last;
		}		
		N++;		
	}
	
	public Item dequeue(){
		// 出队列在表头
		Item item = first.item;
		first = first.next;
		
		if(N == 0){
			last = null;
		}
		N--;
		return item;
	}
	
	
	public boolean hasNext() {
		return false;
	}

	public Item next() {
		return null;
	}

	@Override
	public void remove() {
		
	}

}
