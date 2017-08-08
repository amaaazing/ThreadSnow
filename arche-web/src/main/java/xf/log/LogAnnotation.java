package xf.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogAnnotation {

    int operateType(); // 操作类型 1.团购 2.品牌团 3..
    
    int bussType(); // 1.新增 2.修改 3.删除 4.审核

    String comment(); // 操作按钮 如 团购审核管理-编辑-保存

}
