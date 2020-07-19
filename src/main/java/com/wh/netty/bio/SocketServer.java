package com.wh.netty.bio;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author wh
 * @date 2020/7/15
 * Description: socket服务端（先起服务端，后起客户端）
 */
public class SocketServer {
    public static void main(String[] args) throws Exception {
        // 创建服务端
        ServerSocket server = new ServerSocket(8081);

        // 服务端阻塞式监听客户端连接请求
        // 接收到连接请求，则创建一个socket实例，与客户端通信
        System.out.println("server将一直等待连接的到来");
        Socket client = server.accept();

        // 获取InputStream读取数据,并建立缓冲区就行读取
        InputStream in = client.getInputStream();
        byte[] bytes = new byte[1024];
        int len;
        StringBuilder sb = new StringBuilder();
        // 客户端关闭输出流后服务端会读取到-1标志
        while ((len = in.read(bytes)) != -1) {
            System.out.println("len:"+len);
            System.out.println(new String(bytes));
            sb.append(new String(bytes, 0, len, StandardCharsets.UTF_8));
        }
        System.out.println("get message from client: " + sb.toString());

        // 获取OutputStream输出数据
        OutputStream out = client.getOutputStream();
        out.write("hello, client".getBytes());
        // 输出结束，关闭输出流
        client.shutdownOutput();

        System.out.println("Server close. " + System.currentTimeMillis());
        server.close();
    }
}
