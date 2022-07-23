package cn.mianshiyi.demo;

import cn.mianshiyi.braumclient.ratelimit.EasyRateLimiter;
import cn.mianshiyi.braumclient.ratelimit.EasyRedisCalcRateLimiter;
import cn.mianshiyi.braumclient.redis.RedisCalc;

/**
 * @author shangqing.liu
 */
public class DistLimiter {


    public static void main(String[] args) throws InterruptedException {
        RedisCalc redisCalc = new RedisCalcImpl();
        EasyRateLimiter easyRateLimiter = new EasyRedisCalcRateLimiter().setRedisCalc(redisCalc).create(2, "test");

        for (int i = 0; i < 20; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 100; j++) {
//                        try {
//                            TimeUnit.MILLISECONDS.sleep(500);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                        System.out.println(easyRateLimiter.acquire(1000));
                    }
                }
            }).start();
        }

    }
}
