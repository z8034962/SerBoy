package com.heilan.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.annotation.Resource;
import javax.swing.JButton;
import javax.swing.JFrame;

import org.springframework.stereotype.Component;

import com.heilan.context.ContextHolder;
import com.heilan.view.file.FilesPanel;
import com.heilan.view.server.RemoteServersPanel;
@Component
public class MainFrame extends JFrame
{
    @Resource
    LocalClassPanel localClassPanal;
    @Resource
    RemoteServersPanel remoteServersPanal;
    @Resource
    FilesPanel filesPanel;
    private static final long serialVersionUID = -8716206645739648379L;
 
    public void showUI()
    {
        initialize();   
        setVisible(true);
    }
    private void initialize(){
     
        this.setTitle("综合平台文件同步工具 ");
        this.setBounds(100, 100, 1000, 630);
        setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          try
        {
             addComponentsToPane(this.getContentPane());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }  
    }
    
    public void addComponentsToPane(Container pane) throws IOException
    {
        
        JButton button = new JButton("Button 1 (PAGE_START)");
        button.setPreferredSize(new Dimension(200, 100));
        pane.add( localClassPanal, BorderLayout.PAGE_START);     
        button = new JButton("Button 2 (CENTER)");
        pane.add(filesPanel, BorderLayout.CENTER);   
        filesPanel.initialize();
        button = new JButton("Button 3 (LINE_START)");
        remoteServersPanal.initialize();
        pane.add(remoteServersPanal, BorderLayout.LINE_START);     
        button = new JButton("Long-Named Button 4 (PAGE_END)");
       // pane.add(button, BorderLayout.PAGE_END);      
        button = new JButton("5 (LINE_END)");
        button.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                remoteServersPanal.getSelectHostCodes();
                
            }
        });
       // pane.add(button, BorderLayout.LINE_END);
    }
}
