package cn.mianshiyi.braumclient.ratelimit;

/**
 * @author shangqing.liu
 */
public abstract class EasyRateLimiter {

    public abstract EasyRateLimiter create(double permitsPerSecond, String pointName);

    public abstract boolean acquire(long timeout);

    public abstract boolean tryAcquire();

}
