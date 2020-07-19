package com.wh.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author wh
 * @date 2020/7/17
 * Description: nio同步非阻塞客户端
 */
public class NioSocketClient {

    public static void main(String[] args) throws IOException {
        //得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //提供服务端IP与端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8082);

        //连接服务器
        if (!socketChannel.connect(inetSocketAddress)){
            while (!socketChannel.finishConnect()){
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他工作。。。");
            }
        }

        //如果连接成功，发送数据
        String str="hello,尚硅谷";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        //发送数据，将buffer写入channel
        socketChannel.write(buffer);
        System.in.read();

    }
}
