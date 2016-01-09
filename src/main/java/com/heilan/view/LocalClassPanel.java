package com.heilan.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.springframework.stereotype.Component;

import com.heilan.context.ConfigData;
 
/**      
 * 类描述：   最上面显示本机类路径的panal
 * 创建人：GongPing
 * 创建时间：2015年3月1日 上午9:04:54   
 */
  
@Component
public class LocalClassPanel extends JPanel
{

    private static final long serialVersionUID = 5028054256706444389L;
    private JTextField textField ;
    public LocalClassPanel() throws IOException
    {
        initUI();
    }

    private void initUI() throws IOException
    {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEtchedBorder());
        setPreferredSize(new Dimension(200, 30));
        JLabel label = new JLabel("本机classes路径：");
        label.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        this.add(label);
        textField = new JTextField(ConfigData.getLocalClassPath());
        this.add(textField);
        JButton btn = new JButton("保存");
        btn.setFont(getFont());
        btn.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    ConfigData.saveLocalClassPath(textField.getText());
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        });
       // this.add(btn);
    }
    
    
    /** 
     * 方法描述:从控件中获取class路径 
     * 创建人：GongPing  
     * 创建时间：2015年3月12日 下午3:38:03
     */ 
    public String getLocalPath()
    {
        return textField.getText();
    }
}
