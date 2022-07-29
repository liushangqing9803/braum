package cn.mianshiyi.braumclient.monitor;

import cn.mianshiyi.braumclient.utils.LocalHost;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author shangqing.liu
 */
@Service
public class EasyLimiterMonitorUtil {

    public static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);

    public static Map<Long, List<LimiterMonitorValue>> monitorMap = new ConcurrentHashMap<>();

    static {
        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis() / 1000 - 5;
            for (Long key : monitorMap.keySet()) {
                if (key < now) {
                    monitorMap.remove(key);
                }
            }
        }, 1000, 5000, TimeUnit.MILLISECONDS);
    }

    public static void handle(String name, boolean acquire) {
        long now = System.currentTimeMillis() / 1000;
        boolean contains = monitorMap.containsKey(now);
        if (contains) {
            monitorMap.get(now).add(new LimiterMonitorValue(name, acquire));
        } else {
            List<LimiterMonitorValue> valueList = Collections.synchronizedList(new ArrayList<>());
            valueList.add(new LimiterMonitorValue(name, acquire));
            monitorMap.put(now, valueList);
        }
    }


    public static Collection<LimiterMonitorEntity> calc() {
        System.out.println("计算");
        Long now = System.currentTimeMillis() / 1000 - 1;
        boolean contains = monitorMap.containsKey(now);
        if (!contains) {
            return null;
        }
        Map<String, LimiterMonitorEntity> map = new HashMap<>();

        List<LimiterMonitorValue> limiterMonitorValues = monitorMap.get(now);
        monitorMap.remove(now);
        for (LimiterMonitorValue limiterMonitorValue : limiterMonitorValues) {
            if (map.containsKey(limiterMonitorValue.getName())) {
                LimiterMonitorEntity limiterMonitorEntity = map.get(limiterMonitorValue.getName());
                limiterMonitorEntity.setExecCount(limiterMonitorEntity.getExecCount() + 1);
                if (!limiterMonitorValue.isAcquire()) {
                    limiterMonitorEntity.setBlockCount(limiterMonitorEntity.getBlockCount() + 1);
                }
            } else {
                LimiterMonitorEntity limiterMonitorEntity = new LimiterMonitorEntity();
                limiterMonitorEntity.setName(limiterMonitorValue.getName());
                limiterMonitorEntity.setOccSecond(now);
                limiterMonitorEntity.setClientIp(LocalHost.getLocalHost());
                limiterMonitorEntity.setExecCount(limiterMonitorEntity.getExecCount() + 1);
                if (!limiterMonitorValue.isAcquire()) {
                    limiterMonitorEntity.setBlockCount(limiterMonitorEntity.getBlockCount() + 1);
                }
                map.put(limiterMonitorValue.getName(), limiterMonitorEntity);
            }
        }
        return map.values();
    }

}
