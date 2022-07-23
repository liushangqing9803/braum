## braum:布隆
### **布隆**：英雄联盟中坦克英雄，拥有巨大的盾牌，可以拦截住对方的各种技能。

>使用令牌桶机制限流的小工具,使用注解方式方便使用。
**braum**帮你实现注解方式，更加简单快捷。
### 支持功能
1. 单机限流
2. 分布式限流
3. 限流key 参数解析
4. 限流key 自定义
5. 限流立即返回异常
6. 等待获取token

### 使用方式：
#### 单机限流
springboot：单机只需三步即可使用，
1. 第一步：
拉取代码或引入jar包。
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
    //本地单机限流
    @EasyRateLimier(value = "test", permitsPerSecond = 0.1, limiterType = LimiterType.LOCAL, limiterHandleType = LimiterHandleType.WAIT, timeout = 2000L)
    public String global() {
        return "111";
    }
    //分布式限流
    @EasyRateLimier(value = "test", permitsPerSecond = 0.1, limiterType = LimiterType.DIST, limiterHandleType = LimiterHandleType.WAIT, timeout = 2000L)
    public String global() {
        return "111";
    }
```

#### 分布式限流
需要实现接口：cn.mianshiyi.braumclient.redis.RedisCalc
```aidl
 /**
     * 因考虑redis客户端较多，用户需自行实现该接口，并交由spring管理
     *
     * @param luaValue lua脚本
     * @param key1     限流key
     * @param value1   生产时间
     * @param key2     限流key
     * @param value2   最大token数量
     * @return 执行结果
     */
    Object eval(String luaValue, String key1, String value1, String key2, String value2);
```

### 关键类解释
  本地单机限流
1. cn.mianshiyi.braumclient.ratelimit.LocalEasyRateLimiter
  分布式限流，依赖redis
2. cn.mianshiyi.braumclient.ratelimit.EasyRedisCalcRateLimiter
   注解
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
     * 如果限流处理类型为 WAIT ,则默认有效
     * 超时 默认抛出异常:RateLimitTimeoutBlockException
     * 单位 毫秒
     */
    long timeout() default 0L;
```
### 后续支持
1. 可视化监控
2. 限流规则运行时修改
