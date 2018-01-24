package xf.practice.concurrent;

import java.util.Vector;

// 一个和某个队列相关联的锁，来保证将锁公平地分配给每一个等待获取该锁的线程
// 233
public class QueuedBusyFlag extends BusyFlag{

    protected Vector waiters;

    public QueuedBusyFlag(){
        waiters = new Vector();
    }

    public synchronized void getBusyFlag(){
        Thread me = Thread.currentThread();
        if(me == busyFlag){
            busyCount++;
            return;
        }

        waiters.addElement(me);

        while(waiters.elementAt(0) != me){
            try {
                wait();
            } catch (InterruptedException e) {

            }
        } // while end

        busyFlag = me;
        busyCount = 0;
    }

    public synchronized void freeBusyFlag(){
        if (Thread.currentThread() != busyFlag){
            throw new IllegalArgumentException("QueuedBusyFlag not held.");
        }

        if(busyCount == 0){
            waiters.removeElementAt(0);
            busyFlag = null;
            notifyAll();
        }else {
            busyCount--;
        }
    }

    public synchronized boolean tryGetBusyFlag(){
        if(waiters.size() != 0 && busyFlag != Thread.currentThread()){
            return false;
        }
        getBusyFlag();
        return true;
    }
}
