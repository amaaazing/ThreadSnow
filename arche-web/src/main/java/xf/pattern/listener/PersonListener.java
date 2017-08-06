package xf.pattern.listener;

/**
* @ClassName: PersonListener(事件监听器)
* @Description: 设计Person类(事件源)的监听器接口
*
*/ 
interface PersonListener {

    /**
    * @Method: doeat
    * @Description: 这个方法是用来监听Person对象eat(吃)这个行为动作，
    *                 当实现类实现doeat方法时就可以监听到Person类对象eat(吃)这个动作
    *
    * @param e
    */ 
    void doeat(Event e);

    /**
    * @Method: dorun
    * @Description: 这个方法是用来监听Person对象run(跑)这个行为动作，
    *                 当实现类实现dorun方法时就可以监听到Person类对象run(跑)这个动作
    *
    * @param e
    */ 
    void dorun(Event e);

}
