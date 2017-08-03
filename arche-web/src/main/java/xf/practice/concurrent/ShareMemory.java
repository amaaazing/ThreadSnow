package xf.practice.concurrent;

/**
 * 一个共享内存的实现
 * @author xinzhenqiu
 *
 */
public class ShareMemory extends BusyFlag {

	byte memory[];
	
	public ShareMemory(int size){
		memory = new byte[size];
	}
	
	public synchronized byte[] attach(){
		getBusyFlag();
		return memory;
	}
	
	public synchronized void detach(){
		freeBusyFlag();
	}
}
