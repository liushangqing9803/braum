package cn.mianshiyi.braumclient.annotation;

import cn.mianshiyi.braumclient.enums.LimiterHandleType;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author shangqing.liu
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EasyRateLimier {

    /**
     * 资源命名
     */
    String value() default "";

    /**
     * 每秒内允许通过的数量
     */
    double permitsPerSecond() default 0.0d;

    /**
     * 限流处理类型 默认 抛出异常
     * RateLimitBlockException
     */
    LimiterHandleType limiterHandleType() default LimiterHandleType.EXCEPTION;

    /**
     * 如果限流处理类型为 WAIT ,则默认有效
     * 超时 默认抛出异常:RateLimitTimeoutBlockException
     */
    long timeout() default 0L;

    /**
     * 如果限流处理类型为 WAIT ,则默认有效
     */
    TimeUnit timeoutUnit() default TimeUnit.MILLISECONDS;
}
