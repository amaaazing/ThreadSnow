package xf.practice.concurrent;

import java.util.Enumeration;
import java.util.Vector;

/**
 * 读写锁
 * 
 * 读锁：可以被多个线程同时获取
 * 
 * 此读写锁没有“锁升级”的概念：如果你拥有一个读锁，就不能获取一个写锁
 * 因此，必须显示地释放读锁后，再去申请获取写锁，否则就产生一个IllegalArgumentException异常
 * 
 * 
 * 
 * 
 * 
 * @author xinzhenqiu
 *
 */
public class RWLock {

	// 试图获取锁的线程会先存放在waiters向量中，直到成为该向量中的第一个元素
	// 对于读锁而言，“第一”是指waiters队列中在其前面没有其他请求写锁的请求
	// 如果某个请求前面的所有请求都是希望获取读锁的，则这个请求可以获得读锁
	// 否则，只能等待该请求成为队列的第一个元素
	
	// 写锁的请求：只有队列的第一个元素才能获取该锁
	
	
	private Vector<RWNode> waiters;
	
	public RWLock(){
		waiters = new Vector<RWNode>();
	}
	/**
	 * 因为要跟踪每个线程要获取什么类型的锁
	 * 因此需要RWNode封装线程和其希望获取锁的类型
	 * 
	 * @author xinzhenqiu
	 *
	 */
	class RWNode{
		static final int READER = 0;
		static final int WRITER = 1;
		Thread t;
		int state;
		int nAcquires; // nAcquires和每一个线程关联
		
		RWNode(Thread t, int state){
			this.t = t;
			this.state = state;
			nAcquires = 0;
		}
	}
	
	/**
	 * 辅助方法：用来在waiters中搜素第一个试图获取写锁的节点
	 * 
	 * 
	 * 
	 * 
	 * @return
	 */
	private int firstWriter(){
		Enumeration<RWNode> e;
		int index;		
		for(index = 0, e = waiters.elements();e.hasMoreElements();index++){
			RWNode node = e.nextElement();
			if(node.state == RWNode.WRITER) return index;						
		}
		// 如果在向量中找不到写锁，就默认放回一个最大的index
		return Integer.MAX_VALUE;
	}
	
	/**
	 * 用来找出与调用线程相关联的节点对应的索引号
	 * @param t
	 * @return
	 */
	private int getIndex(Thread t){
		Enumeration<RWNode> e;
		int index;		
		for(index = 0, e = waiters.elements();e.hasMoreElements();index++){
			RWNode node = e.nextElement();
			if(node.t == t) return index;						
		}
		return -1;
	}
	
	public synchronized void lockRead(){
		RWNode node;
		Thread me = Thread.currentThread();
		
		int index = getIndex(me);
		if(index == -1){
			node = new RWNode(me, RWNode.READER);
			waiters.addElement(node);			
		} else {
			node = waiters.elementAt(index);
		}
		
		while(getIndex(me) > firstWriter()){
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		
		node.nAcquires++;
	}
	
	public synchronized void lockWrite(){
		RWNode node;
		Thread me = Thread.currentThread();
		
		int index = getIndex(me);
		if(index == -1){
			node = new RWNode(me, RWNode.WRITER);
			waiters.addElement(node);			
		} else {
			node = waiters.elementAt(index);
			if(node.state == RWNode.READER){
				throw new IllegalArgumentException("Upgrade lock");
			}
			node.state = RWNode.WRITER;
		}
		
		while(getIndex(me) != 0){
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		
		node.nAcquires++;
	}
	
	public synchronized void unlock(){
		RWNode node;
		Thread me = Thread.currentThread();
		
		int index = getIndex(me);

		if(index > firstWriter()){
			throw new IllegalArgumentException("Lock not held");
		}
		
		node = waiters.elementAt(index);
		node.nAcquires--;
		if(node.nAcquires == 0){
			waiters.removeElementAt(index);
			notifyAll();			
		}		
	}
	
}
