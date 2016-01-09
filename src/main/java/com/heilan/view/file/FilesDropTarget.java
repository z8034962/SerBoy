package com.heilan.view.file;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.heilan.tool.HLTool;
import com.heilan.view.LocalClassPanel;

public class FilesDropTarget extends DropTarget
{
    private static final long serialVersionUID = 3877362388629123726L;
    @Resource
    FilesTableModel filesTableModel;
    public FilesTableModel getFilesTableModel()
    {
        return filesTableModel;
    }

    public void setFilesTableModel(FilesTableModel filesTableModel)
    {
        this.filesTableModel = filesTableModel;
    }

    public LocalClassPanel getLocalClassPanel()
    {
        return localClassPanel;
    }

    public void setLocalClassPanel(LocalClassPanel localClassPanel)
    {
        this.localClassPanel = localClassPanel;
    }

    @Resource
    LocalClassPanel localClassPanel;
    @SuppressWarnings("unchecked")
    @Override
    public void drop(DropTargetDropEvent evt)
    {
        try
        {
            evt.acceptDrop(DnDConstants.ACTION_COPY);
            List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(
                    DataFlavor.javaFileListFlavor);
            for (File file : droppedFiles)
            {
                if (file.getAbsolutePath().contains(localClassPanel.getLocalPath()))
                {
                    if (file.isDirectory())
                    {
                        getFiles(file.getAbsolutePath());
                    }
                    else
                    {
                        filesTableModel.addRow(new Object[]{filesTableModel.getRowCount(),file.getAbsolutePath() });
                    }
                }
                 
            }
        }
        catch (UnsupportedFlavorException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private void getFiles(String filePath)
    {
        if((!filePath.endsWith("/"))&&(!filePath.endsWith("\\")))
        {
            if(HLTool.isWindows())
            {
                filePath +="\\";
            }else{
                filePath +="/";
            }
        }
        File root = new File(filePath);
        File[] files = root.listFiles();
        for (File file : files)
        {
            if (file.isDirectory())
            {
                //递归调用
                getFiles(file.getAbsolutePath());
            }
            else
            {
              filesTableModel.addRow(new Object[]{filesTableModel.getRowCount() ,file.getAbsolutePath() });
            }
        }
    }
}
