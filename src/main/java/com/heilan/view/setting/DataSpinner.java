package com.heilan.view.setting;

import java.awt.Dimension;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;

import com.heilan.tool.HLTool;

public class DataSpinner{

    JSpinner spinner;
    String format = "yyyy-MM-dd HH:mm:ss";
    DateFormat timeFormat = new SimpleDateFormat(format);
    public JSpinner getSpinner() {
        spinner = new JSpinner();
        spinner.setPreferredSize(new Dimension(150, 30));
        spinner.setMaximumSize(new Dimension(300, 30));
        spinner.setMinimumSize(new Dimension(100, 30));
        spinner.setFont(HLTool.getFont());
        SpinnerModel dateModel=null;
        dateModel = new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE);
        spinner.setModel(dateModel);
        //对spinner的时间格式进行设置
        spinner.setEditor(new JSpinner.DateEditor(spinner,format));
        return spinner;
    }
    
    /** 
     * 方法描述: 设置时间为第二天凌晨1点
     * 创建人：GongPing  
     * 创建时间：2015年3月13日 下午4:33:53
     */ 
    public void setNight()
    {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 1);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        spinner.setValue(c.getTime());
        
    }
    
    /** 
     * 方法描述: 取得设定的时间
     * 创建人：GongPing  
     * 创建时间：2015年3月13日 下午4:34:05
     */ 
    public Date getSetDate()
    {
        return (Date) spinner.getValue();
    }
}