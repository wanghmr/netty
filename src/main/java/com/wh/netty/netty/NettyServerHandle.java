package com.wh.netty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

/**
 * @author wh
 * @date 2020/7/28
 * Description: netty服务端处理类
 * 说明：
 * 1.我们自定义的handle需要继续netty规定好的某个HandlerAdapter规范
 * 2.这是我们自定义的handle才是一个handle
 */
public class NettyServerHandle extends ChannelInboundHandlerAdapter {

    /**
     * @param ctx 上下文对象，含有管道pipeline,通道channel，地址
     * @param msg 客户端发送的数据，默认object
     * @throws Exception 异常
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);默认
        System.out.println("服务器读取线程" + Thread.currentThread().getName());
        System.out.println("server ctx:" + ctx);
        System.out.println("看看channel和pipeline关系");
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = ctx.pipeline();
        //把msg转成一个ByteBuf
        //ByteBuf是Netty提供的，怒视Nio的ByteBuffer
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("客户端发送的消息是：" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址是：" + channel.remoteAddress());
    }

    /**
     * 数据读取完毕
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        super.channelReadComplete(ctx);默认
        //将数据写入缓存并刷新
        //一般来讲，我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端", CharsetUtil.UTF_8));
    }

    /**
     * 处理异常，一般是需要关闭通道
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);//默认
        ctx.close();
    }
}
