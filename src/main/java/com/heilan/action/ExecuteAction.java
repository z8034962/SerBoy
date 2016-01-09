package com.heilan.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.annotation.Resource;
import javax.swing.JOptionPane;

import net.lingala.zip4j.exception.ZipException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.heilan.db.DBUtils;
import com.heilan.mima.Client;
import com.heilan.tool.HLTool;
import com.heilan.view.file.FilesTableModel;
import com.heilan.view.server.RemoteServersPanel;
import com.heilan.view.setting.LoggerPanal;
import com.heilan.view.setting.SettingPanal;
import com.heilan.vo.FileBean;

@Component
public class ExecuteAction implements ActionListener
{
    public static Logger logger = Logger.getRootLogger();

    @Resource
    RemoteServersPanel remoteServersPanel;

    @Resource
    FilesTableModel filesTableModel;

    @Resource
    LoggerPanal loggerPanal;

    @Resource
    SettingPanal settingPanal;

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object[] codes = (Object[]) remoteServersPanel.getSelectHostCodes();
        if (codes == null || codes.length == 0)
        {
            JOptionPane.showMessageDialog(null, "请选择需要更新的服务器");
            return;
        }
        if (filesTableModel.getRowCount() == 0)
        {
            JOptionPane.showMessageDialog(null, "请选择需要更新的文件");
            return;
        }
        try
        {
            String sys_doc_id = HLTool.files2zip(filesTableModel.getFilePaths());
            for (Object code : codes)
            {
                FileBean fileBean = new FileBean();
                fileBean.setExeTime(settingPanal.getSetDate());
                fileBean.setReset(settingPanal.isReset());
                fileBean.setTiming(settingPanal.isTiming());
                fileBean.setSys_doc_id(sys_doc_id);
                fileBean.setServerID((String) code);
                try
                {
                    new Client().send(fileBean, DBUtils.getIpByCode((String) code),loggerPanal);
                    loggerPanal.setLog("更新请求发送成功：" + code);
                    logger.info(HLTool.getLocalHost() + " --> " + fileBean.getServerID() + "| 主键:"
                            + fileBean.getSys_doc_id());

                }
                catch (Exception e1)
                {
                    loggerPanal.setLog("向"+fileBean.getServerID()+"发送请求失败："+e1.getMessage());
                    e1.printStackTrace();
                }
            }
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
            logger.info(e1.getMessage());
        }
        catch (ZipException e1)
        {
            logger.info(e1.getMessage());
        }

    }

}
