package cn.mianshiyi.demo;

import cn.mianshiyi.braumclient.ratelimit.EasyLocalRateLimiter;
import cn.mianshiyi.braumclient.ratelimit.EasyRateLimiter;

/**
 * @author shangqing.liu
 */
public class LocalLimiter {

    public static void main(String[] args) throws InterruptedException {
        EasyRateLimiter easyRateLimiter = new EasyLocalRateLimiter().create(2000, "test");
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
                        System.out.println(easyRateLimiter.tryAcquire());
                    }
                }
            }).start();
        }
    }
}
