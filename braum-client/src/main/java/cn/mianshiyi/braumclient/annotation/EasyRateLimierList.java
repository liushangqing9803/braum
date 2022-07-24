package cn.mianshiyi.braumclient.annotation;

import java.lang.annotation.*;

/**
 * @author shangqing.liu
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EasyRateLimierList {
    EasyRateLimier[] value();
}
