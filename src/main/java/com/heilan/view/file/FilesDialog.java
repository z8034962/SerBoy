package com.heilan.view.file;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import com.heilan.tool.HLTool;
import com.heilan.view.LocalClassPanel;
public class FilesDialog  
{
    private JDialog dialog ;
    private JTextArea jTextArea;
    public FilesTableModel getFilesTableModel()
    {
        return filesTableModel;
    }

    public void setFilesTableModel(FilesTableModel filesTableModel)
    {
        this.filesTableModel = filesTableModel;
    }

    LocalClassPanel localClassPanel;
    FilesTableModel filesTableModel;
    public LocalClassPanel getLocalClassPanel()
    {
        return localClassPanel;
    }

    public void setLocalClassPanel(LocalClassPanel localClassPanel)
    {
        this.localClassPanel = localClassPanel;
    }

    public FilesDialog(JFrame jf,FilesTableModel model,LocalClassPanel localclass){
        filesTableModel = model;
        localClassPanel = localclass;
        dialog = new JDialog( jf);
        dialog.setTitle("请将路径粘贴进来：");
        dialog.setSize(600,500);
        dialog.setLocationRelativeTo(null);
        dialog.add(init());
    }
    
    public void show()
    {
        dialog.show();
    }
    
    private JTextArea getTextField()
    {
        jTextArea = new JTextArea( ){
            
            @Override public void paste(){
                super.paste();
               String content = jTextArea.getText();
              String[] paths = content.split("\n");
              StringBuffer sb = new StringBuffer();
              for(String pathString :paths)
              {
                  pathString = pathString.replace(
                          pathString.substring(0,pathString.indexOf("src")), "")   ;
                  sb.append(pathString).append("\n");
              }
            //  jTextArea.setText(sb.toString());
               
            }
        };
         
        jTextArea.setLineWrap (true);
        
        return jTextArea;
    }
    
    private JPanel init()
    {
        JPanel panal = new JPanel();
        panal.setFont(HLTool.getFont());
        panal.setLayout(new BoxLayout(panal, BoxLayout.PAGE_AXIS));
        panal.add(getTextField());
        panal.add(getCommitBtn());   
        return panal;
    }
    
    private JButton getCommitBtn()
    {
        JButton btn = new JButton("确定");
        btn.setFont(HLTool.getFont());
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // 对接收的字符做拼接处理
              String value = jTextArea.getText();
              List<String> paths = new ArrayList<String>();
              if(value != null && !"".equals(value))
              {
                  String[] ss = value.split("\n");
                  for(String s:ss)
                  {
                      if(!"".equals(s))
                      {
                          s = s.trim();
                      }
                      String path = appand2Path(s);
                      if(!"".equals(path))
                      {
                          //解决1 ExecuteProcManage$1.class
                          //     ExecuteProcManage$2.class 这种问题
                          if(path.endsWith(".class"))
                          {
                              int i=0;
                              File f = new File(path);
                              if(f.exists())
                              {
                                  paths.add(path);
                              }else{
                                  
                              }  
                              i++;
                              while(i>0)
                              {
                                 String path1 = path.replace(".class", "$"+i+".class");
                                  File f1 = new File(path1);
                                  if(f1.exists())
                                  {
                                      paths.add(path1);
                                      i++;
                                  }else{
                                   break;   
                                  } 
                              }
                          }
                          else {
                            
                              File f = new File(path);
                              if(f.exists())
                              {
                                  paths.add(path);
                              }else{
                                  
                              }   
                        }
                      }
                  }
              }
              for(String path:paths)
              {
                  filesTableModel.addRow(new Object[]{filesTableModel.getRowCount(),path });
              }
                dialog.setVisible(false);
            }
        });
        return btn;
    }
    
    /** 
     * 方法描述: 拼接成绝对路径
     * 创建人：GongPing  
     * 创建时间：2015年3月14日 上午10:24:14
     */ 
    private String appand2Path(String orgPath)
    {
        
        orgPath = orgPath.replaceAll("/","\\\\");
        if(orgPath.startsWith("\\src"))
        {
            orgPath =  orgPath.replace("\\src", "");
        }else if(orgPath.startsWith("src"))
        {
            orgPath =  orgPath.replace("src", "");
        }else return "";
        
        if(orgPath.endsWith("java"))
        {
            orgPath=  orgPath.substring(0, orgPath.length()-4);
            orgPath+="class";
        }
        
        String headPath = localClassPanel.getLocalPath();
        if(headPath.endsWith("\\"))
        {
            headPath = headPath.substring(0, headPath.length()-1);
        }
        return headPath+orgPath;
    }
    
    public static void main(String[] args)
    {
        String orgPath = "/src/com/heilan/pm/view/feather/base/YrfMlSupplierRecord.view.xml";
        orgPath = orgPath.replaceAll("/","\\\\");
        System.out.println(orgPath);
    }
}
