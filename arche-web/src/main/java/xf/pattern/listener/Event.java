package xf.pattern.listener;

/**
* @ClassName: Event(事件对象)
* @Description:设计事件类，用来封装事件源
*
*/ 
class Event {

    /**
    * @Field: source
    *          事件源(Person就是事件源)
    */ 
    private Person source;

    public Event() {
        
    }

    public Event(Person source) {
        this.source = source;
    }

    public Person getSource() {
        return source;
    }

    public void setSource(Person source) {
        this.source = source;
    }
}