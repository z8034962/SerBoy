package com.heilan.context;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

 
public class ConfigData
{
    private static Properties prop;

    public static Properties getProp() throws IOException
    {
        if(prop == null)
        {
            InputStream in = ClassLoader.getSystemResourceAsStream("config.properties");
            prop = new Properties();// 属性集合对象
            prop.load(in);
            in.close();
        }
        return prop;
    }

    
    public static String getLocalClassPath() throws IOException
    {

        return getProp().getProperty("localpath");
    }
    
    public static String getPropert(String key) throws IOException
    {

        return getProp().getProperty(key);
    }
    public static void saveLocalClassPath(String path) throws IOException
    {
        OutputStream outputStream = new FileOutputStream("config.properties");  
        Properties aProperties=   getProp();
        aProperties.setProperty("localpath", path);
        aProperties.store(outputStream, "author");
        outputStream.close();
    }
}
