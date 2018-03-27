package xf.practice.aop;

import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;


public class TestAop {

    public static void main(String[] args) {

        AspectJProxyFactory factory = new AspectJProxyFactory();
        Waiter target = new NaiveWaiter();
        factory.setTarget(target);
        factory.addAspect(PreGreetingAspect.class);

        Waiter proxy = factory.getProxy();

        proxy.greetTo("心悦卿兮");
        proxy.serverTo("卿可知");
    }

}
