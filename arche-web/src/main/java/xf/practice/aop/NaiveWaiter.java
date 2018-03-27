package xf.practice.aop;

public class NaiveWaiter implements Waiter {

    public void greetTo(String name) {
        System.out.println("NaiveWaiter"+":greet to "+name+"....");
    }

    public void serverTo(String name) {
        System.out.println("NaiveWaiter"+":server to "+name+"....");
    }
}
