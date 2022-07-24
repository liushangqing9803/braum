package cn.mianshiyi.braumclient.annotation;

import cn.mianshiyi.braumclient.common.Constant;
import cn.mianshiyi.braumclient.enums.LimiterHandleType;
import cn.mianshiyi.braumclient.enums.LimiterKeyType;
import cn.mianshiyi.braumclient.enums.LimiterType;

import java.lang.annotation.*;

/**
 * @author shangqing.liu
 */
@Repeatable(EasyRateLimierList.class)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EasyRateLimier {

    /**
     * 资源命名，唯一标识
     */
    String value() default "";

    /**
     * 限流表达式 可为空
     * keyType为PARAM 解析
     * 如不为空，则完整限流名：资源命名+限流标识
     *
     * @return keys
     */
    String[] keys() default {};

    /**
     * 限流key定义类型
     *
     * @return 定义类型
     */
    LimiterKeyType keyType() default LimiterKeyType.PARAM;

    /**
     * 每秒内允许通过的数量
     */
    double permitsPerSecond() default 0.0d;

    /**
     * 限流类型 本地、分布式
     * 默认本地
     */
    LimiterType limiterType() default LimiterType.LOCAL;

    /**
     * 限流处理类型 默认 抛出异常
     * RateLimitBlockException
     */
    LimiterHandleType limiterHandleType() default LimiterHandleType.EXCEPTION;

    /**
     * 限流异常提示消息
     */
    String blockMessage() default Constant.BLOCK_EXCEPTION_MSG;

    /**
     * 限流超时异常提示消息
     */
    String blockTimeoutMessage() default Constant.BLOCK_TIMEOUT_EXCEPTION_MSG;

    /**
     * 如果限流处理类型为 WAIT ,则默认有效
     * 超时 默认抛出异常:RateLimitTimeoutBlockException
     * 单位：毫秒
     */
    long timeout() default 10L;
}
