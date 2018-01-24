package xf.practice.taskUtil;

import java.util.UUID;

public class UUIDGenerator {

    /**
     * 获取UUID
     * @description 一句话说明这个方法是干什么的
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}

