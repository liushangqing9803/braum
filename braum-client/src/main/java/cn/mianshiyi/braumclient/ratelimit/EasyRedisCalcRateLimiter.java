package cn.mianshiyi.braumclient.ratelimit;

/**
 * @author shangqing.liu
 */
public class EasyRedisCalcRateLimiter extends EasyRateLimiter {

    @Override
    public EasyRateLimiter create(double permitsPerSecond) {
        return null;
    }

    @Override
    public boolean acquire() {
        return false;
    }

    @Override
    public boolean tryAcquire(long timeout) {
        return false;
    }
}
