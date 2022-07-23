package cn.mianshiyi.braumclient.ratelimit;

import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

/**
 * @author shangqing.liu
 */
public class EasyLocalRateLimiter extends EasyRateLimiter {

    private final Stopwatch stopwatch = Stopwatch.createStarted();
    //毫秒
    private static final long MAX_INTERVAL_WAIT_TIME = 5;

    private final Object lock = new Object();

    //当前剩余可存储的容量
    double storedPermits;
    //最大存储容量
    double maxPermits;
    //多长时间生成一个容量
    double stableIntervalMicros;
    //下次生成时间
    private long nextFreeTicketMicros;

    private String pointName;

    @Override
    public EasyRateLimiter create(double permitsPerSecond, String pointName) {
        synchronized (lock) {
            this.doSetRate(permitsPerSecond, this.stopwatch.elapsed(TimeUnit.MICROSECONDS));
        }
        this.pointName = pointName;
        return this;
    }

    private void doSetRate(double permitsPerSecond, long nowMicros) {
        this.storedPermits = permitsPerSecond;
        this.nextFreeTicketMicros = nowMicros;
        this.stableIntervalMicros = (double) TimeUnit.SECONDS.toMicros(1L) / permitsPerSecond;
        this.maxPermits = permitsPerSecond;
    }

    @Override
    public boolean acquire(long timeout) {
        long currentTime = this.stopwatch.elapsed(TimeUnit.MILLISECONDS);
        if (tryAcquire()) {
            return true;
        }
        while (this.stopwatch.elapsed(TimeUnit.MILLISECONDS) - currentTime < timeout) {
            if (tryAcquire()) {
                return true;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(MAX_INTERVAL_WAIT_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean tryAcquire() {
        //计算池子中可用令牌
        synchronized (lock) {
            long nowMicros = this.stopwatch.elapsed(TimeUnit.MICROSECONDS);
            long spTime = nowMicros - this.nextFreeTicketMicros;
            if (spTime > 0) {
                double newPermits = (double) (spTime) / this.stableIntervalMicros;
                this.storedPermits = Math.min(this.maxPermits, this.storedPermits + newPermits);
                double storedPermitsToSpend = Math.min(1, this.storedPermits);
                double freshPermits = 1 - storedPermitsToSpend;
                long waitMicros = (long) (freshPermits * stableIntervalMicros);
                this.nextFreeTicketMicros = nowMicros + waitMicros;
                this.storedPermits -= storedPermitsToSpend;
                return true;
            }
            return false;
        }
    }
}
