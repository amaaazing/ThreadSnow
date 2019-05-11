package xf.utility;

import java.util.Iterator;

/**
 * 为什么需要NullIterator？
 * 选择一：可以让createIterator()方法返回null,如果这么做了，我们的客户代码就需要条件语句来
 * 判断返回值是否为null
 *
 * 选择二：返回一个迭代器，而这个迭代器的hasNext()永远返回false.等于是创建了一个迭代器，其作用是“没作用”
 * 这样，客户代码不用再担心返回值是否为null
 */
public class NullIterator implements Iterator<Object> {
    public boolean hasNext() {
        return false;
    }

    public Object next() {
        return null;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
