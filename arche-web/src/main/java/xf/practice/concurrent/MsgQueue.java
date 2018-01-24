package xf.practice.concurrent;

import java.util.Vector;

/**
 * 消息队列的一个简单实现
 * @author xinzhenqiu
 *
 */
public class MsgQueue {

	// Vector == 线程安全的ArrayList
	Vector<Object> queue = new Vector<Object>();
	
	// 发送消息 -- 消息加入队列中
	public synchronized void send(Object obj){
		queue.addElement(obj);
	}
	
	// 接受消息 -- 消息从队列中删除
	public synchronized Object receive(){
		if(queue.size() == 0) return null;
		Object o = queue.firstElement();
		queue.removeElementAt(0);
		return o;
	}
}
