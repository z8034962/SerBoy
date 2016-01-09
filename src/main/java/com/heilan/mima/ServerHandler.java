package com.heilan.mima;

import java.util.Date;
import java.util.Timer;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.heilan.vo.FileBean;

public class ServerHandler extends IoHandlerAdapter
{
    public static Logger logger = Logger.getRootLogger();
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception
    {
        if (message instanceof FileBean)
        {
            final FileBean bean = (FileBean) message;
            new Thread(new Runnable()
            {

                @Override
                public void run()
                {
                    MDC.put("CODE", bean.getServerID());
                    Date exeTime = bean.getExeTime();
                    ServerTask task = new ServerTask();
                    task.setFileBean(bean);
                    if (!bean.isTiming() )
                    {   
                        logger.info("已接收，正在处理...");
                        new Timer().schedule(task, 1000);
                    }
                    else
                    {
                        new Timer().schedule(task, exeTime);
                        logger.info("已接收，将在 "+exeTime.toString()+" 进行处理");
                    }
                }
            }).start();
            String msg = bean.getServerID() + ":收到，已进行处理";
            session.write(msg);
            session.close();
        }
    }
}
