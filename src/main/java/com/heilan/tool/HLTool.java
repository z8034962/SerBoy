package com.heilan.tool;

import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.jdbc.support.lob.OracleLobHandler;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import com.heilan.context.ConfigData;
import com.heilan.context.ContextHolder;
import com.heilan.db.DBUtils;
import com.heilan.view.LocalClassPanel;
import com.heilan.view.MainFrame;

public class HLTool
{
    /*
     * 方法描述: 是否是windows 创建人：GongPing 创建时间：2014年12月11日 上午8:28:00
     */
    public static boolean isWindows()
    {
        if ("/".equals(System.getProperties().getProperty("file.separator")))
        {
            return false;
        }
        return true;
    }
    
    public static Font getFont(){
       return new Font("微软雅黑", Font.PLAIN, 12);
    }
    
    public static Font getBtnFont()
    {
        return new Font("微软雅黑", Font.BOLD, 14);
    }
    
    public static String getCurrentTimeName()
    {
        return (new java.text.SimpleDateFormat("yyyyMMddhhmmss")).format(new Date());
    }
    /**
     * 方法描述: 当前主机MAC地址 创建人：GongPing 创建时间：2014年12月11日 下午2:16:41
     */
    public static List<String> getMac()
    {
        List<String> res = new ArrayList<String>();
        try
        {
            Enumeration<NetworkInterface> el = NetworkInterface.getNetworkInterfaces();
            while (el.hasMoreElements())
            {
                byte[] mac = el.nextElement().getHardwareAddress();
                if (mac == null)
                    continue;
                // 下面代码是把mac地址拼装成String
                StringBuffer sb = new StringBuffer();

                for (int i = 0; i < mac.length; i++)
                {
                    if (i != 0)
                    {
                        sb.append("-");
                    }
                    // mac[i] & 0xFF 是为了把byte转化为正整数
                    String s = Integer.toHexString(mac[i] & 0xFF);
                    sb.append(s.length() == 1 ? 0 + s : s);
                }
                if (sb.length() == 17)
                {
                    res.add(sb.toString().toUpperCase());
                }
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        return res;
    }
    
    /** 
     * 方法描述: 将文件列表打包并上传至数据库，返回主键
     * 创建人：GongPing  
     * 创建时间：2015年3月14日 下午4:16:27
     */ 
    public static String files2zip(List<String> filePaths) throws IOException, ZipException
    {
        String zipPath = ConfigData.getPropert("zipPath");
        final String verPath = getCurrentTimeName();
        String realPath = zipPath + verPath  + "\\";
        new File(realPath).mkdirs();
        for (String fileName : filePaths)
        {
           LocalClassPanel p =  ContextHolder.getContext().getBean(LocalClassPanel.class);
          
             String[] dirs = fileName.replace(p.getLocalPath(), "").split("\\\\");
            String tempPath = realPath;
            for (int i = 0; i < dirs.length; i++)
            {
                if (i < dirs.length - 1)
                {
                    tempPath += dirs[i] + "\\";
                    new File(tempPath).mkdirs();
                }
                else
                {
                    copyFile(new File(fileName), new File(tempPath + dirs[i]));
                }

            } 
        }
       
        ZipFile zipFile = new ZipFile(zipPath + verPath + ".zip");
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        zipFile.addFolder(zipPath + verPath, parameters);
        removedir(new File(zipPath + verPath));
        final String sys_doc_id = DBUtils.getSeq();
        final LobHandler lobHandler=new OracleLobHandler();
        final File binaryFile=new File(zipPath + verPath + ".zip");  
        final InputStream is=new FileInputStream(binaryFile);  
        DBUtils.getJdbcTemplate().execute("insert into KF_UPDATE_FILE (PK_KF_UPDATE_FILE,FILE_,FILE_NAME)"
                + " VALUES(?,?,?) ", 
                new AbstractLobCreatingPreparedStatementCallback(lobHandler)
        {
            
            @Override
            protected void setValues(PreparedStatement pstmt, LobCreator lobCreator) throws SQLException, DataAccessException
            {
                pstmt.setString(1,sys_doc_id);
                pstmt.setString(3, verPath + ".zip");
                lobCreator.setBlobAsBinaryStream(pstmt, 2, is, (int) binaryFile.length());
            }
        });
        
        return sys_doc_id;
    }
    public static void removedir(File file)
    {
        File[] files = file.listFiles();
        
        for (File f : files)
        {
            if (f.isDirectory())// 递归调用
            {
                removedir(f);
            }
            else
            {
                f.delete();
            }
        }
        // 一层目录下的内容都删除以后，删除掉这个文件夹
        file.delete();
    }
    // 复制文件
    public static void copyFile(File sourceFile, File targetFile) throws IOException
    {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try
        {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1)
            {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        }
        finally
        {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }
    // 复制文件夹
    public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
                copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }
    
    /**
     * 方法描述: 当前主机IP地址 创建人：GongPing 创建时间：2014年12月11日 下午2:16:30
     */
    public static String getLocalHost()
    {
        try
        {
            return InetAddress.getLocalHost().toString();
        }
        catch (UnknownHostException e)
        {
        }
        return "UnknownHost";
    }
}
