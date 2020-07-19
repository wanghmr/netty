package com.wh.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


/**
 * @author wh
 * @date 2020/7/16
 * Description: nio同步非阻塞服务端
 */
public class NioSocketServer {
    public static void main(String[] args) throws IOException {
        //创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //绑定一个端口
        serverSocketChannel.socket().bind(new InetSocketAddress(8082));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //创建一个seclector
        Selector selector = Selector.open();
        //把创建ServerSocketChannel注册到selecor,关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端连接
        while (true) {
            //没有事件发生
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待了1秒，无连接 ");
                continue;
            }

            //如果返回>0,就获取到相关的SelectionKey
            //1.如果返回>0,表示已经获取到关注的事件
            //2.selector.selectedKeys()返回关注事件的集合
            //  通过selectionKeys反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //遍历Set<SelectionKey>，迭代器
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                //获取selectionKey
                SelectionKey selectionKey = keyIterator.next();
                //根据selectionKey对应的通道处理发生的事件
                if (selectionKey.isAcceptable()) {
                    //该客户端生成了一个socketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功" + socketChannel.hashCode());
                    //将socketChannel 设置为非阻塞
                    socketChannel.configureBlocking(false);
                    //将socketChannel 注册到selector,关注事件OP_READ，同时关联一个buff
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                if (selectionKey.isReadable()) {
                    //通过selectionKey获取对应的通道
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    //获取该渠道关联的buff
                    ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                    channel.read(byteBuffer);
                    System.out.println("客户端：" + new String(byteBuffer.array()));
                }
                //手动从集合中删除key,防止重复操作
                keyIterator.remove();
            }

        }




    }

}
