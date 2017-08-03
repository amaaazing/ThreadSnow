package xf.practice.concurrent;

/**
 * 循环链表:双向链表
 * 
 * 一个实现细节：通过保存前一节点和后一节点的引用来提高搜素时的效率
 * 
 * @author xinzhenqiu
 *
 */
public class CircularList {

	private CircularListNode current;
	
	// 定义节点
	class CircularListNode{
		Object data;
		CircularListNode next;
		CircularListNode prev;
	}
	
	public synchronized void insert(Object o){
		CircularListNode node = new CircularListNode();
		node.data = o;
		if(current == null){
			node.next = node.prev = node;
			current = node;
		}else{
			// 在当前节点前增加
			// 双链表的插入
			node.next = current;
			node.prev = current.prev;
			current.prev.next = node;
			current.prev = node;			
		}
	}
	
	public synchronized void delete(Object o){
		CircularListNode p = find(o);
		CircularListNode next = p.next;
		CircularListNode prev = p.prev;
		
		if(p == p.next){ // 链表中的最后一个对象
			current = null;
			return;
		}
		
		prev.next = next;
		next.prev = prev;
		
		// 如果删除的是当前对象
		if(current == p) current = next;
	}
	
	private CircularListNode find(Object o){
		CircularListNode p = current;
		if(p == null){
			throw new IllegalArgumentException();
		}
		
		do{
			if(p.data == o) return p;
			p = p.next;
		} while (p != current);
		
		throw new IllegalArgumentException();
	}
	
	public synchronized Object locate(Object o){
		CircularListNode p = current;
		
		do{
			if(p.data == o) return p.data;
			p = p.next;
		} while (p != current);
		
		throw new IllegalArgumentException();
	}
	
	public synchronized Object getNext(){
		if(current == null){
			return null;
		}		
		current = current.next;
		return current.data;
	}
}
