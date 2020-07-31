package com.wh.netty.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author wh
 * @date 2020/7/28
 * Description: netty服务端
 */
public class NettyServer {
    public static void main(String[] args) {
        //创建BossGroup和WorkGroup
        //说明
        //1.创建两个线程组bossGroup和workGroup
        //2.bossGroup只处理连接请求，真正和客户端业务处理，会交给workGroup完成
        //3.两个都是无限循环
        //4.bossGroup和workGroup含有的子线程(NioEventLoop)的个数
        // 默认实际CPU核数*2
        EventLoopGroup boosGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            //创建服务器的启动对象，配置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            //使用链式编程配置
            serverBootstrap.group(boosGroup, workGroup)//设置两个线程组
                    .channel(NioServerSocketChannel.class)//使用NioSocketChannel作为服务器通道实现
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道测试对象（匿名对象）
                        //给pipeLine设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyServerHandle());
                        }
                    });//给我们的workGroup的EventLoop对应的管道设置处理器
            System.out.println("......服务器is ready.....");
            //绑定一个端口并同步，生成一个ChannelFeature对象
            //启动服务器（并绑定端口）
            ChannelFuture channelFuture = serverBootstrap.bind(6668).sync();

            //给channelFuture 注册监听器，监控我们关心的事件
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("监听端口 6668 成功");
                    } else {
                        System.out.println("监听端口 6668 失败");
                    }
                }
            });


            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }


    }
}
