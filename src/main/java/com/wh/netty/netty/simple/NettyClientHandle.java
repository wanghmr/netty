package com.wh.netty.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author wh
 * @date 2020/7/29
 * Description: netty客户端处理类
 */
public class NettyClientHandle extends ChannelInboundHandlerAdapter {
    /**
     * 当通道就绪时，就会触发此方法
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client ctx:" + ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello， server", CharsetUtil.UTF_8));
    }

    /**
     * 当通道有读取数据是触发此方法
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        Channel channel = ctx.channel();
        System.out.println("服务端发送的消息是：" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("服务端地址是：" + channel.remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
