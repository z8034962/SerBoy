package com.heilan;

import java.awt.EventQueue;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.stereotype.Component;

import com.heilan.context.ConfigData;
import com.heilan.context.ContextHolder;
import com.heilan.mima.Server;
import com.heilan.tool.HLTool;
import com.heilan.view.MainFrame;
public class SBController
{
    public static Logger logger = Logger.getRootLogger();
    public static void main(String[] args)
    {
        
        ContextHolder.instance();
 
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                   
                    String mode = ConfigData.getPropert("mode");
                    if("server".equals(mode))
                    {
                        new Server();
                    }else{
                        MDC.put("CODE",HLTool.getLocalHost());
                        MainFrame main =  ContextHolder.getContext().getBean(MainFrame.class);
                        main.showUI();
                        
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
}
