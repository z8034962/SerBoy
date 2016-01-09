package com.heilan.view.setting;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Enumeration;

import javax.annotation.Resource;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;

import org.springframework.stereotype.Component;

import com.heilan.action.ExecuteAction;
import com.heilan.tool.HLTool;
@Component
public class SettingPanal extends JPanel
{
    private static final long serialVersionUID = 3708919723288160193L;
    final DataSpinner sp = new DataSpinner();
    private  ButtonGroup isReset ;
    private  ButtonGroup isTiming;
    public SettingPanal()
    {
        
    }
    
    public void init()
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEtchedBorder());
        setFont(HLTool.getFont());
        add(getTimePanel());
        add(getRstPanel());
        add(getTimingPanel());

    }
    private JPanel getTimePanel()
    {
        JPanel panal = new JPanel();
        panal.setLayout(new BoxLayout(panal, BoxLayout.X_AXIS));
        panal.setBorder(BorderFactory.createEtchedBorder());
        panal.setMaximumSize(new Dimension(300, 50));
        JSpinner  jxDatePicker = sp.getSpinner();
        panal.add(jxDatePicker);
        panal.add(Box.createVerticalStrut(10));
        JButton night = new JButton("凌晨一点");
        night.setFont(getFont());
        night.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                sp.setNight();
            }
        });
        panal.add(night);
        
        return panal;
    }
    
    /** 
     * 方法描述: 返回设定的时间
     * 创建人：GongPing  
     * 创建时间：2015年3月13日 下午4:34:20
     */ 
    public Date getSetDate()
    {
        return sp.getSetDate();
    }
    
    private JPanel getRstPanel()
    {
        JPanel panal = new JPanel();
        panal.setLayout(new BoxLayout(panal,BoxLayout.X_AXIS));
        panal.setBorder(BorderFactory.createEtchedBorder());
        panal.setMaximumSize(new Dimension(300, 30));

        JRadioButton btn1 = new JRadioButton("更新并重启",true);
        JRadioButton btn2 = new JRadioButton("只更新不重启");
        isReset = new ButtonGroup();
        isReset.add(btn1);
        isReset.add(btn2);
        panal.add(btn1);
        panal.add(btn2);
        
       
      
        return panal;
    }
    @Resource
    ExecuteAction executeAction;
    private JPanel getTimingPanel()
    {
        JPanel panal = new JPanel();
        panal.setLayout(new BoxLayout(panal,BoxLayout.X_AXIS));
        panal.setBorder(BorderFactory.createEtchedBorder());
        panal.setMaximumSize(new Dimension(300, 30));
        
        JRadioButton btn3 = new JRadioButton("定时",true);
        JRadioButton btn4 = new JRadioButton("立即");
        isTiming = new ButtonGroup();
        isTiming.add(btn3);
        isTiming.add(btn4);
        panal.add(btn3);
        panal.add(btn4);
        
        JButton exeBtn = new JButton("执行");
        exeBtn.addActionListener(executeAction);
        exeBtn.setFont(getFont());
        panal.add(Box.createVerticalStrut(10));
        panal.add(exeBtn);
        return panal;
    }
    
    /** 
     * 方法描述: 返回是否需要重启，否则只更新不重启
     * 创建人：GongPing  
     * 创建时间：2015年3月13日 下午4:57:36
     */ 
    public Boolean isReset()
    {
        String rs = null;
        Enumeration<AbstractButton> enu = isReset.getElements();
        while (enu.hasMoreElements())
        {
            AbstractButton radioButton = enu.nextElement();
            if (radioButton.isSelected())
            {
                rs = radioButton.getText();
                break;
            }
        }
        if ("更新并重启".equals(rs))
        {
            return true;
        }
        return false;
    }
    
    /** 
     * 方法描述: 是否定时更新，否则立即更新
     * 创建人：GongPing  
     * 创建时间：2015年3月13日 下午5:01:08
     */ 
    public Boolean isTiming()
    {
        String rs = null;
        Enumeration<AbstractButton> enu = isTiming.getElements();
        while (enu.hasMoreElements())
        {
            AbstractButton radioButton = enu.nextElement();
            if (radioButton.isSelected())
            {
                rs = radioButton.getText();
                break;
            }
        }
        if ("定时".equals(rs))
        {
            return true;
        }
        return false;
    }
    
}
