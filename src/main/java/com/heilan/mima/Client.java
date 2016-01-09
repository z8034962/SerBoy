package com.heilan.mima;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.heilan.view.setting.LoggerPanal;
import com.heilan.vo.FileBean;

public class Client
{
    
     
    
    public void send(FileBean fb,String ip,LoggerPanal loggerPanal){
        NioSocketConnector connector = new NioSocketConnector();
        connector.getSessionConfig().setReadBufferSize(4096 );
        connector.getSessionConfig().setReceiveBufferSize(4096 );
        connector.getFilterChain().addLast(
                "codec",
                new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        
        // 设置连接超时检查时间
        connector.setConnectTimeoutCheckInterval(30);
        //代理类
        ClientHandler clientHandler = new ClientHandler();
        clientHandler.setLoggerPanal(loggerPanal);
        connector.setHandler(clientHandler);
        
        // 建立连接
        ConnectFuture cf = connector.connect(new InetSocketAddress(ip, 9527));
        // 等待连接创建完成
        cf.awaitUninterruptibly();
        
         
        cf.getSession().write(fb);
        
        // 等待连接断开
        cf.getSession().getCloseFuture().awaitUninterruptibly();
        // 释放连接
        connector.dispose();
    }
    
}
