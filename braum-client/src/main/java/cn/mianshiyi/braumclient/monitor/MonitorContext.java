package cn.mianshiyi.braumclient.monitor;

import cn.mianshiyi.braumclient.utils.LocalHost;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author shangqing.liu
 */
public class MonitorContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorContext.class);

    /**
     * 客户端发消息用的channel
     */
    public static Channel CHANNEL;
    /**
     * 本机ip
     */
    public static String CLIENT_IP;

    static {
        CLIENT_IP = LocalHost.getLocalHost();
    }

    public static ScheduledThreadPoolExecutor sendMessageExecutor = new ScheduledThreadPoolExecutor(1);

    public static ScheduledThreadPoolExecutor clearMessageExecutor = new ScheduledThreadPoolExecutor(1);

    public static Map<Long, List<LimiterMonitorValue>> monitorMap = new ConcurrentHashMap<>();

    static {
        sendMessageExecutor.scheduleAtFixedRate(() -> {
            try {
                Collection<LimiterMonitorEntity> calc = calc();
                if (calc == null) {
                    return;
                }
                List<LimiterMonitorEntity> objects = new ArrayList<>();
                for (LimiterMonitorEntity limiterMonitorEntity : calc) {
                    LimiterMonitorEntity limiterMonitorEntity1 = new LimiterMonitorEntity();
                    BeanUtils.copyProperties(limiterMonitorEntity, limiterMonitorEntity1);
                    limiterMonitorEntity1.setClientIp("127.0.0.1");
                    limiterMonitorEntity1.setExecCount(limiterMonitorEntity1.getExecCount() + 10);
                    limiterMonitorEntity1.setBlockCount(limiterMonitorEntity1.getBlockCount() + 10);
                    objects.add(limiterMonitorEntity1);
                }
                RemoteMessage<Collection<LimiterMonitorEntity>> remoteMessage = new RemoteMessage(10, calc);
                CHANNEL.writeAndFlush(Unpooled.copiedBuffer(JSON.toJSONString(remoteMessage) + "\n", CharsetUtil.UTF_8));
                //TODO 测试使用
                RemoteMessage<Collection<LimiterMonitorEntity>> remoteMessage1 = new RemoteMessage(10, objects);
                CHANNEL.writeAndFlush(Unpooled.copiedBuffer(JSON.toJSONString(remoteMessage1) + "\n", CharsetUtil.UTF_8));

            } catch (Exception e) {
                LOGGER.error("send monitor message exception", e);
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS);

        clearMessageExecutor.scheduleAtFixedRate(() -> {
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

    public static void register(String name) {
        RemoteMessage<LimiterMonitorRegisterEntity> remoteMessage = new RemoteMessage(20, new LimiterMonitorRegisterEntity(name));
        CHANNEL.writeAndFlush(Unpooled.copiedBuffer(JSON.toJSONString(remoteMessage) + "\n", CharsetUtil.UTF_8));
    }


    public static Collection<LimiterMonitorEntity> calc() {
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
                limiterMonitorEntity.setClientIp(CLIENT_IP);
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
