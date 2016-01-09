package com.heilan.mima;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;


public class Server
{
    private static final int PORT = 9527;   
    public Server() throws IOException{
        // 创建服务端监控线程
        IoAcceptor acceptor = new NioSocketAcceptor();
        acceptor.getSessionConfig().setReadBufferSize(4096 );
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        // 设置日志记录器
        //acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        // 设置编码过滤器
        ObjectSerializationCodecFactory aCodecFactory = new ObjectSerializationCodecFactory();
        aCodecFactory.setDecoderMaxObjectSize(40960000);
        acceptor.getFilterChain().addLast(
            "codec",
            new ProtocolCodecFilter(aCodecFactory));
        // 指定业务逻辑处理器
        ServerHandler handler = new ServerHandler();
       // handler.textArea = textArea;
        acceptor.setHandler(handler);
        // 设置端口号
        acceptor.bind(new InetSocketAddress(PORT));
        // 启动监听线程
        acceptor.bind();
    }
}
