package cn.mianshiyi.braumclient.ratelimit;

/**
 * @author shangqing.liu
 */
public abstract class EasyRateLimiter {

    public abstract EasyRateLimiter create(double permitsPerSecond);

    public abstract boolean acquire();

    public abstract boolean tryAcquire(long timeout);

}
