package xf.log;

import java.util.UUID;

public class ThreadLocalUtils {

    // ThreadLocal 是一个泛型容器
    private static ThreadLocal<String> uuidThread = new ThreadLocal<String>();

    // 将对象放入 ThreadLocal
    public static void setUuid(String uuid) {
        uuidThread.set(uuid);
    }

    // 从 ThreadLocal 中读取内容
    public static String getUuid() {
        return uuidThread.get();
    }

    /**
     * 获得一个UUID
     * 
     * @return String UUID
     */
    public static String generateUUID() {

        String s = UUID.randomUUID().toString();
        // 去掉“-”符号
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }
    
    public static void removeUuid(){
    	uuidThread.remove();
    }
}

