package com.heilan.mima;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import com.heilan.view.setting.LoggerPanal;

public class ClientHandler extends  IoHandlerAdapter
{
    LoggerPanal loggerPanal;
    public LoggerPanal getLoggerPanal()
    {
        return loggerPanal;
    }

    public void setLoggerPanal(LoggerPanal loggerPanal)
    {
        this.loggerPanal = loggerPanal;
    }

    public void messageReceived(IoSession session, Object message) throws Exception {
        String content = message.toString();
        if(loggerPanal != null)
        {
            loggerPanal.setLog(content);
        }
    }

    public void messageSent(IoSession session, Object message) throws Exception {
       // System.out.println("messageSent -> ：" + message);
    }
}
