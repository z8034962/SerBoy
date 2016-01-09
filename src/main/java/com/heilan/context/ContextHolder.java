package com.heilan.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

public class ContextHolder
{
  

    private static ApplicationContext context;

    public static ApplicationContext getContext()
    {
        if(context == null)
        {
            context = new ClassPathXmlApplicationContext("spring.xml");  
        }
        return context;
    }

    public static void instance(){
        context = new ClassPathXmlApplicationContext("spring.xml");  
    }
 
}
