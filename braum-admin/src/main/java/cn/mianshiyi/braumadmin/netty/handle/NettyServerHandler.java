package cn.mianshiyi.braumadmin.netty.handle;

import cn.mianshiyi.braumadmin.entity.LimiterDataEntity;
import cn.mianshiyi.braumadmin.entity.RemoteCode;
import cn.mianshiyi.braumadmin.service.LimiterDataService;
import cn.mianshiyi.braumadmin.utils.SpringUtil;
import cn.mianshiyi.braumclient.monitor.LimiterMonitorEntity;
import cn.mianshiyi.braumclient.monitor.RemoteMessage;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author shangqing.liu
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    // 处理线程
    private static final ThreadPoolExecutor HANDLE_THREAD_POOL = new ThreadPoolExecutor(50, 50, 100L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1500),
            (new ThreadFactoryBuilder()).setNameFormat("netty-handle-pool-%d").build());

    /**
     * 读取客户端发送的数据
     *
     * @param ctx 上下文对象，含有管道 pipeline,通道 channel, 地址
     * @param msg 客户端发送的数据，默认为Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HANDLE_THREAD_POOL.submit(() -> {
            String buf = (String) msg;
            System.out.println("收到" + buf);

            RemoteMessage<Collection<LimiterMonitorEntity>> list = JSON.parseObject(buf, new TypeReference<RemoteMessage<Collection<LimiterMonitorEntity>>>() {
            });
            int code = list.getCode();
            switch (code) {
                case RemoteCode.MONITOR_CODE:
                    for (LimiterMonitorEntity limiterMonitorEntity : list.getData()) {
                        LimiterDataEntity entity = new LimiterDataEntity();
                        entity.setName(limiterMonitorEntity.getName());
                        entity.setExecCount(limiterMonitorEntity.getExecCount());
                        entity.setBlockCount(limiterMonitorEntity.getBlockCount());
                        entity.setOccSecond(limiterMonitorEntity.getOccSecond());
                        entity.setCreateTime(new Date());
                        entity.setClientIp(limiterMonitorEntity.getClientIp());
                        LimiterDataService bean = SpringUtil.getBean(LimiterDataService.class);
                        bean.insert(entity);
                    }
                    break;
                case RemoteCode.NAME_REGISTER_CODE:
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
