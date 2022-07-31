package cn.mianshiyi.braumadmin.netty.handle;

import cn.mianshiyi.braumadmin.entity.LimiterDataEntity;
import cn.mianshiyi.braumadmin.entity.LimiterRegisterInfoEntity;
import cn.mianshiyi.braumadmin.entity.RemoteCode;
import cn.mianshiyi.braumadmin.service.LimiterDataService;
import cn.mianshiyi.braumadmin.service.LimiterRegisterInfoService;
import cn.mianshiyi.braumadmin.utils.SpringUtil;
import cn.mianshiyi.braumclient.monitor.LimiterMonitorEntity;
import cn.mianshiyi.braumclient.monitor.LimiterMonitorRegisterEntity;
import cn.mianshiyi.braumclient.monitor.RemoteMessage;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author shangqing.liu
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    // 处理线程
    private static final ThreadPoolExecutor HANDLE_THREAD_POOL = new ThreadPoolExecutor(50, 50, 100L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1500),
            (new ThreadFactoryBuilder()).setNameFormat("netty-handle-pool-%d").build());

    public static BlockingQueue<LimiterDataEntity> LIMITER_DATA_QUEUE = new LinkedBlockingQueue<>();

    public static void initConsumerData() {
        new Thread(() -> {
            List<LimiterDataEntity> temp = new ArrayList<>(51);
            while (true) {
                try {
                    LimiterDataEntity poll = LIMITER_DATA_QUEUE.poll(1, TimeUnit.SECONDS);
                    Long firstPollTime = null;

                    if (poll != null) {
                        temp.add(poll);
                    } else {
                        if (temp.size() > 0) {
                            LimiterDataEntity first = temp.get(0);
                            if (first != null) {
                                firstPollTime = System.currentTimeMillis();
                            }
                        }
                    }

                    if (temp.size() >= 50 || (firstPollTime != null && System.currentTimeMillis() - firstPollTime > 5000)) {
                        LimiterDataService bean = SpringUtil.getBean(LimiterDataService.class);
                        bean.insertBatch(temp);
                        temp.clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * 读取客户端发送的数据
     *
     * @param ctx 上下文对象，含有管道 pipeline,通道 channel, 地址
     * @param msg 客户端发送的数据，默认为Object
     * @throws Exception 异常
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HANDLE_THREAD_POOL.submit(() -> {
            String buf = (String) msg;
            System.out.println("收到" + buf);

            JSONObject jsonObject = JSON.parseObject(buf);
            int code = Integer.parseInt(jsonObject.get("code").toString());
            switch (code) {
                case RemoteCode.MONITOR_CODE:
                    RemoteMessage<Collection<LimiterMonitorEntity>> list = JSON.parseObject(buf, new TypeReference<RemoteMessage<Collection<LimiterMonitorEntity>>>() {
                    });
                    for (LimiterMonitorEntity limiterMonitorEntity : list.getData()) {
                        LimiterDataEntity entity = new LimiterDataEntity();
                        entity.setName(limiterMonitorEntity.getName());
                        entity.setExecCount(limiterMonitorEntity.getExecCount());
                        entity.setBlockCount(limiterMonitorEntity.getBlockCount());
                        entity.setOccSecond(limiterMonitorEntity.getOccSecond());
                        entity.setCreateTime(new Date());
                        entity.setClientIp(limiterMonitorEntity.getClientIp());
                        try {
                            LIMITER_DATA_QUEUE.offer(entity, 500, TimeUnit.MILLISECONDS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
                case RemoteCode.NAME_REGISTER_CODE:
                    RemoteMessage<LimiterMonitorRegisterEntity> remoteMessage = JSON.parseObject(buf, new TypeReference<RemoteMessage<LimiterMonitorRegisterEntity>>() {
                    });
                    LimiterRegisterInfoService bean = SpringUtil.getBean(LimiterRegisterInfoService.class);
                    LimiterRegisterInfoEntity byName = bean.findByName(remoteMessage.getData().getName());
                    if (byName != null) {
                        break;
                    }
                    LimiterRegisterInfoEntity entity = new LimiterRegisterInfoEntity();
                    entity.setName(remoteMessage.getData().getName());
                    bean.insert(entity);
                    break;
            }
        });
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓存并刷新
        //一般要对数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("success\n", CharsetUtil.UTF_8));
    }

    //处理异常，一般需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
