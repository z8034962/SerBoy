package com.heilan.view.setting;

import java.awt.Dimension;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.springframework.stereotype.Component;
@Component
public class LoggerPanal extends JScrollPane
{
    private static final long serialVersionUID = 4925981823031989644L;
    private  JTextPane textPane;
    
    public LoggerPanal(){
        setPreferredSize(new Dimension(300, 200));
        setMaximumSize(new Dimension(300, 400));
        textPane = new JTextPane();
        setViewportView(textPane);
    }
    
    public void setLog(String log)
    {
        textPane.setText( textPane.getText()+"\n"+new Date()+":"+log);
    }
}
