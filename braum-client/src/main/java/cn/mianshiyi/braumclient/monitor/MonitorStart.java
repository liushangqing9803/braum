package cn.mianshiyi.braumclient.monitor;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author shangqing.liu
 */
public class MonitorStart {

    @Value("${spring.easy.limiter.monitor.url:#{null}}")
    private String url;

    @PostConstruct
    void startNetty() {
        if (url == null) {
            return;
        }
        new Thread(() -> {
            //初始化配置文件
            try {
                start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);

    public static Channel CHANNEL = null;

    public void start() throws Exception {

        //客户端需要一个事件循环组就可以
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            //创建客户端的启动对象 bootstrap ，不是 serverBootStrap
            Bootstrap bootstrap = new Bootstrap();
            //设置相关参数
            bootstrap.group(group) //设置线程组
                    .channel(NioSocketChannel.class) //设置客户端通道的实现数 （反射）
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new NettyClientHandler()); //加入自己的处理器
                        }
                    });

            System.out.println("客户端 ready is ok..");
            //连接服务器
            String[] split = url.split(":");
            final ChannelFuture channelFuture = bootstrap.connect(split[0], Integer.parseInt(split[1])).sync();

            Channel channel = channelFuture.channel();
            CHANNEL = channel;
            scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        Collection<LimiterMonitorEntity> calc = EasyLimiterMonitorUtil.calc();
                        if (calc == null) {
                            return;
                        }
                        RemoteMessage<Collection<LimiterMonitorEntity>> remoteMessage = new RemoteMessage(10, calc);
                        channel.writeAndFlush(Unpooled.copiedBuffer(JSON.toJSONString(remoteMessage) + "\n", CharsetUtil.UTF_8));
                    } catch (Exception e) {
                        System.out.println(e);
                    }

                }
            }, 1000, 1000, TimeUnit.MILLISECONDS);
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
    //TODO
    public static void registerName(String name) {

    }
}
