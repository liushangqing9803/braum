## braum:布隆
### **布隆**：英雄联盟中坦克英雄，拥有巨大的盾牌，可以拦截住对方的各种技能。

>使用令牌桶机制限流的小工具,使用注解方式方便使用。
**braum**帮你实现注解方式，更加简单快捷。


#### 使用方式：
springboot：只需三步即可使用，
1. 第一步：
复制代码或引入jar包。
2. 第二步：
```
/**
* 使用 @EnableEasyRateLimiter 注解，才可以使用rateLimiter 注解类工具
*/
@SpringBootApplication()
@EnableEasyRateLimiter
public class Application extends SpringBootServletInitializer {
   ......
}
```
2. 第三步：
```
    @EasyRateLimier(value = "test", permitsPerSecond = 0.5, limiterHandleType = LimiterHandleType.WAIT, timeout = 1000L)
    public String global() {
        return "111";
    }
```

### 关键类解释
1. cn.mianshiyi.braumclient.ratelimit.LocalEasyRateLimiter
   本地单机限流
2. cn.mianshiyi.braumclient.ratelimit.EasyRedisCalcRateLimiter
   分布式限流，依赖redis
3. cn.mianshiyi.braumclient.annotation.EasyRateLimier
   注解
```
    /**
     * 资源命名，唯一标识
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
     * 如果限流处理类型为 WAIT 
     * 默认：毫秒
     */
    TimeUnit timeoutUnit() default TimeUnit.MILLISECONDS;

```

