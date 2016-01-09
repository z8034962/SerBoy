package com.heilan.mima;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.jdbc.support.lob.OracleLobHandler;
import org.springframework.util.FileCopyUtils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import com.heilan.db.DBUtils;
import com.heilan.tool.HLTool;
import com.heilan.vo.FileBean;

public class ServerTask extends TimerTask {
    public static Logger logger = Logger.getRootLogger();
    private FileBean fileBean;

    public FileBean getFileBean() {
        return fileBean;
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run()
    {
        // TODO 备份代码
        // TODO 从数据库获取相应ID服务器的临时路径和src路径
        // TODO 从数据库获取文件
        // TODO 将文件解压到临时路径
        // TODO 将解压到的文件copy到src路径
        String tempPath = DBUtils.getTempPathByCode(fileBean.getServerID());
        String srcPath = DBUtils.getSrcPathByCode(fileBean.getServerID());

        // 备份下代码
        try
        {
            ZipFile zipFile = new ZipFile(tempPath + "backup_" + HLTool.getCurrentTimeName() + ".zip");
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            zipFile.addFolder(srcPath, parameters);
            logger.info("已将 "+srcPath + " 代码备份至 " + tempPath);
        }
        catch (ZipException e)
        {
            logger.error(e.getMessage());
        }
        //从数据库中取得压缩包，存于此路径
        String zipPath = tempPath + fileBean.getSys_doc_id() + ".zip";
        String zipName = DBUtils.getJdbcTemplate().
                queryForObject("select file_name from KF_UPDATE_FILE where pk_kf_update_file=?",
                        String.class,fileBean.getSys_doc_id());
        zipName = zipName.substring(0, zipName.length()-4);
        try
        {
            
            final LobHandler lobHandler = new OracleLobHandler();
            final OutputStream os = new FileOutputStream(new File(zipPath));
            DBUtils.getJdbcTemplate().query("select file_ from KF_UPDATE_FILE where pk_kf_update_file=?",
                    new Object[]{fileBean.getSys_doc_id()},
                    new AbstractLobStreamingResultSetExtractor()
                    {
                        protected void streamData(ResultSet rs) throws SQLException, IOException, DataAccessException
                        {
                            FileCopyUtils.copy(lobHandler.getBlobAsBinaryStream(rs, 1), os);
                            os.close();
                        }
                    });
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
        }
        ZipFile zipFile;
        try
        {
            zipFile = new ZipFile(zipPath);
            zipFile.extractAll(tempPath+fileBean.getSys_doc_id()
                    );
            //删除压缩包
            new File(zipPath).delete();
        }
        catch (ZipException e)
        {
            logger.error(e.getMessage());
        }
        
        try
        {
            HLTool.copyDirectiory(tempPath+fileBean.getSys_doc_id()+System.getProperties().getProperty("file.separator")
                    +zipName, srcPath);
          
        }
        catch (IOException e)
        {
            logger.error(e.getMessage());
        }
        
        if(fileBean.isReset())
        {
            //TODO 重启tomcat
            try
            {
                logger.info("代码更新完毕！");
                logger.info("开始重启...！");
                reStart(fileBean);
                logger.info("重启成功！");
            }
            catch (Exception e)
            {
                logger.error(e.getMessage());
            }
        }else {
            logger.info("代码更新完毕，无需重启！");
        }
    }

    
    private void reStart(FileBean fileBean) throws IOException, InterruptedException{
        String tomcatPort = DBUtils.getJdbcTemplate().queryForObject(
                "select port from KF_SERVER where code=?", String.class,fileBean.getServerID());
        String bootPath = DBUtils.getJdbcTemplate().queryForObject(
                "select bootPath from KF_SERVER where code=?", String.class,fileBean.getServerID());
        boolean isWindows = HLTool.isWindows();
        Process a = Runtime.getRuntime().exec(isWindows?"netstat -aon":"netstat -lnp | grep "+tomcatPort);

        final InputStream ii = a.getInputStream();

        final InputStreamReader ir = new InputStreamReader(ii);

        final BufferedReader br = new BufferedReader(ir);
        String str = null;
        String pid = null;
        while ((str = br.readLine()) != null)
        {
           
            if(str.contains((isWindows?"0.0.0.0:":":::")+tomcatPort))
            {
                String[] rs = str.split("  ");
                System.out.println(str);
                
                if(isWindows)
                {
                    pid=rs[rs.length-1].trim();
                    if(!isWindows)
                    {
                        pid=pid.split("/")[0];
                    }
                }else{
                    for(String s:rs)
                    {
                      if(s.contains("java"))
                      {
                          pid=s.split("/")[0];
                      }
                    }
                }
                
                break;
            }
        }
        if(pid != null){
            String exec = (isWindows?"taskkill -pid ":"kill -9 ")+pid;
            Runtime.getRuntime().exec(exec);
            Thread.sleep(1000);
            exec = bootPath;
            Process bProcess= Runtime.getRuntime().exec(exec);
            BufferedReader bbBufferedReader= new BufferedReader(new InputStreamReader(bProcess.getInputStream())); 
            String str1= null;
            while ((str1 = bbBufferedReader.readLine()) != null)
            {
                System.out.println(str1);
            }
            
        }
    }
    public static void main(String[] args) {
    }

}
