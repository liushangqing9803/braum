package cn.mianshiyi.braumclient.ratelimit;

import cn.mianshiyi.braumclient.monitor.MonitorContext;
import cn.mianshiyi.braumclient.redis.RedisCalc;
import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

/**
 * 限流name：本地
 * 当前时间:redis时间
 * 下次生成token时间：redis
 * 生成一个token耗时：本地
 * 最大token数量：本地
 * 当前的token数量：redis
 *
 * @author shangqing.liu
 */
public class EasyRedisCalcRateLimiter extends EasyRateLimiter {

    private final Object lock = new Object();

    private final Stopwatch stopwatch = Stopwatch.createStarted();

    private final static String lua = "local keyPoint = KEYS[1];\n" +
            "local stableIntervalMicrosParam = ARGV[1];\n" +
            "local maxPermitsParam = ARGV[2];\n" +
            "local ratelimitInfo = redis.pcall('HMGET', keyPoint, 'nextFreeTicketMicros', 'storedPermits');\n" +
            "local current = redis.call('TIME');\n" +
            "local nowMicros = current[1] * 1000000 + current[2];\n" +
            "local nextFreeTicketMicros = nowMicros\n" +
            "local storedPermits = 0\n" +
            "local maxPermits = tonumber(maxPermitsParam);\n" +
            "local stableIntervalMicros = tonumber(stableIntervalMicrosParam);\n" +
            "if ratelimitInfo[1] then\n" +
            "    nextFreeTicketMicros = tonumber(ratelimitInfo[1]);\n" +
            "else\n" +
            "    redis.pcall('HMSET', keyPoint, 'nextFreeTicketMicros', tostring(nextFreeTicketMicros));\n" +
            "end\n" +
            "if ratelimitInfo[2] then\n" +
            "    storedPermits = tonumber(ratelimitInfo[2]);\n" +
            "else\n" +
            "    redis.pcall('HMSET', keyPoint, 'storedPermits', tostring(storedPermits));\n" +
            "end\n" +
            "local spTime = nowMicros - nextFreeTicketMicros;\n" +
            "if spTime >= 0 then\n" +
            "    local newPermits = spTime / stableIntervalMicros;\n" +
            "    storedPermits = math.min(maxPermits, storedPermits + newPermits);\n" +
            "    local storedPermitsToSpend = math.min(1, storedPermits);\n" +
            "    local freshPermits = 1 - storedPermitsToSpend;\n" +
            "    local waitMicros = freshPermits * stableIntervalMicros;\n" +
            "    nextFreeTicketMicros = nowMicros + waitMicros;\n" +
            "    storedPermits = storedPermits - storedPermitsToSpend;\n" +
            "    redis.pcall('HMSET', keyPoint, 'nextFreeTicketMicros', tostring(nextFreeTicketMicros), 'storedPermits', tostring(storedPermits));\n" +
            "    redis.pcall('EXPIRE', keyPoint, 3600);\n" +
            "    return 1;\n" +
            "else\n" +
            "    if storedPermits - 1 >= 0 then\n" +
            "        return 1;\n" +
            "    else\n" +
            "        return -1;\n" +
            "    end\n" +
            "end\n";

    //毫秒
    private static final long MAX_INTERVAL_WAIT_TIME = 5;
    //最大存储容量
    double maxPermits;
    //多长时间生成一个容量
    double stableIntervalMicros;
    //限流名称
    String pointName;
    //执行接口
    RedisCalc redisCalc;

    public EasyRateLimiter setRedisCalc(RedisCalc redisCalc) {
        this.redisCalc = redisCalc;
        return this;
    }

    @Override
    public EasyRateLimiter create(double permitsPerSecond, String pointName) {
        synchronized (lock) {
            this.doSetRate(permitsPerSecond, pointName);
        }
        MonitorContext.register(pointName);
        return this;
    }

    private void doSetRate(double permitsPerSecond, String pointName) {
        this.stableIntervalMicros = (double) TimeUnit.SECONDS.toMicros(1L) / permitsPerSecond;
        this.maxPermits = permitsPerSecond;
        this.pointName = pointName;
    }


    @Override
    public boolean acquire(long timeout) {
        long currentTime = this.stopwatch.elapsed(TimeUnit.MILLISECONDS);
        if (tryAcquireInner()) {
            MonitorContext.handle(this.pointName, true);
            return true;
        }
        while (this.stopwatch.elapsed(TimeUnit.MILLISECONDS) - currentTime < timeout) {
            if (tryAcquireInner()) {
                MonitorContext.handle(this.pointName, true);
                return true;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(MAX_INTERVAL_WAIT_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        MonitorContext.handle(this.pointName, false);
        return false;
    }

    @Override
    public boolean tryAcquire() {
        boolean acquire = tryAcquireInner();
        MonitorContext.handle(this.pointName, acquire);
        return acquire;
    }

    private boolean tryAcquireInner() {
        Long eval = (Long) redisCalc.eval(lua, this.pointName, String.valueOf(this.stableIntervalMicros), this.pointName, String.valueOf(this.maxPermits));
        //执行redis脚本 -1 代表失败 1代表成功
        return eval != null && eval > 0;
    }
}
