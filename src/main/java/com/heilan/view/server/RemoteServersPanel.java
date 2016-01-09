package com.heilan.view.server;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.heilan.tool.HLTool;
import com.heilan.view.setting.LoggerPanal;
import com.heilan.view.setting.SettingPanal;
@Component
public class RemoteServersPanel extends JPanel
{
    private static final long serialVersionUID = 6735786944784222095L;
    private JTable table;
    private DefaultTableModel tableModel;
    private JPopupMenu jPopupMenu;
    @Resource
    SettingPanal settingPanal;
    @Resource
    LoggerPanal loggerPanal;
    public RemoteServersPanel(){
    }
    public void initialize(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEtchedBorder());
        setPreferredSize(new Dimension(250, 500));
        setMinimumSize(new Dimension(100, 200));
        setMaximumSize(new Dimension(300, 200));
        add(getJLabel());
        add(getJScrollPane());
        settingPanal.init();
        add(settingPanal);
        add(loggerPanal);
       
    }
    
    private JLabel getJLabel(){
        JLabel label = new JLabel("请选择需要更新的服务器：");
        label.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        return label;
    }
    private JScrollPane getJScrollPane(){
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setMaximumSize(new Dimension(500, 200));
        scrollPane.setPreferredSize(new Dimension(500, 100));
        scrollPane.setViewportView(getJtable());
        return scrollPane;
    }
    
    
    private JTable getJtable()
    {
        if(table == null)
        {
            table = new JTable(getTableModel());
            table.setAutoscrolls(true);
            getJtable().getColumnModel().getColumn(0).setCellEditor(getJtable().getDefaultEditor(String.class));
            getJtable().getColumnModel().getColumn(0).setCellRenderer(getJtable().getDefaultRenderer(String.class));
            getJtable().getColumnModel().getColumn(0).setPreferredWidth(20);
            getJtable().getColumnModel().getColumn(1).setCellEditor(getJtable().getDefaultEditor(String.class));
            getJtable().getColumnModel().getColumn(1).setCellRenderer(getJtable().getDefaultRenderer(String.class));
            getJtable().getColumnModel().getColumn(2).setCellEditor(getJtable().getDefaultEditor(Boolean.class));
            getJtable().getColumnModel().getColumn(2).setCellRenderer(getJtable().getDefaultRenderer(Boolean.class));
            getJtable().getColumnModel().getColumn(2).setPreferredWidth(20);
            setTableValue();
            table.addMouseListener(new MouseListener()
            {
                
                @Override
                public void mouseReleased(MouseEvent e)
                {
                    // TODO 自动生成的方法存根
                    
                }
                
                @Override
                public void mousePressed(MouseEvent e)
                {
                    if (e.getButton() == MouseEvent.BUTTON3) {  
                        getMenu().show(e.getComponent(), e.getX(), e.getY());
                    }
                    
                }
                
                @Override
                public void mouseExited(MouseEvent e)
                {
                    // TODO 自动生成的方法存根
                    
                }
                
                @Override
                public void mouseEntered(MouseEvent e)
                {
                    // TODO 自动生成的方法存根
                    
                }
                
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    // TODO 自动生成的方法存根
                    
                }
            });
            
        }
        return table;
    }
    
    private JPopupMenu getMenu()
    {
        if (jPopupMenu == null)
        {
            jPopupMenu =  new JPopupMenu();
            jPopupMenu.add( getMenuItem("正式107", new String[]{"0002","0003","0004"}));
            jPopupMenu.add( getMenuItem("服装109", new String[]{"0007","0008"}));
            jPopupMenu.add(getMenuItem("服装109new", new String[]{"0009","0010"}));
            jPopupMenu.add(getMenuItem("全选", new String[]{"0001","0002","0003","0004","0005","0006","0007","0008","0009","0010"}));
        }
        return jPopupMenu;
    }
    private JMenuItem getMenuItem(String name ,final String[] servers)
    {
        JMenuItem select  = new JMenuItem(name);
        select.setFont(HLTool.getFont());
        select.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setGroupValue(servers);
            }
        });
        return select;
    }
    public void setGroupValue(String[] value)
    {
        int rowCount =  tableModel.getRowCount();
        for(int i = 0;i<rowCount;i++)
        {
            String id= (String) getJtable().getModel().getValueAt(i, 0);
            for(String v:value)
            {
                if(id.equals(v))
                {
                    getJtable().getModel().setValueAt(true,i, 2);
                    break;
                }else{
                    getJtable().getModel().setValueAt(false,i, 2);
                    
                }
            }
        }
    }
    
    private void setTableValue(){
        List<Map<String, Object>> hosts = getRemoteHosts();
        for(Map<String, Object> host:hosts)
        {
            getTableModel().addRow(host.values().toArray());
            
        }
    }
    private DefaultTableModel getTableModel()
    {
        if (tableModel == null)
        {
            tableModel = new DefaultTableModel()
            {
                private static final long serialVersionUID = -7111467629599357528L;
                public boolean isCellEditable(int rowindex, int colindex)
                {
                    if (colindex != 2)
                        return false; // 设置第二列只读
                    return true; // 其他列可以修改
                }
            };
            tableModel.addColumn("ID");
            tableModel.addColumn("名称");
            tableModel.addColumn("选择");
        }

        return tableModel;
    }
    
    @Resource
    JdbcTemplate jdbcTemplate;
    private List<Map<String,Object>> getRemoteHosts(){
        List<Map<String,Object>> hostsMaps = jdbcTemplate.queryForList(getServerSql());
         for(Map<String,Object> e:hostsMaps)
         {
             e.put("SELECT", new Boolean(true));
         }
        return hostsMaps;
    }
    
    private String getServerSql()
    {
        String sql = "SELECT S.CODE,S.NAME FROM KF_SERVER_RIGHT R LEFT JOIN KF_SERVER S ON S.CODE=R.SERVER_ID WHERE s.dr=1 and R.dr=1 ";
        List<String> macs = HLTool.getMac();
        StringBuffer sb = new StringBuffer();
        if (macs != null && macs.size() > 0)
        {

            for (String mac : macs)
            {
                sb.append("'").append(mac).append("',");
            }
            sql = sql + " AND R.MAC_ID IN ( " + sb.substring(0, sb.length() - 1) + " )";
        }else{
            sql = sql + " AND 1 = 2";
        }
        return sql;
    }
    /** 
     * 方法描述: 返回选中的服务器代码
     * 创建人：GongPing  
     * 创建时间：2015年3月2日 上午10:39:23
     */ 
    public Object[] getSelectHostCodes(){
        List<Integer> rows = new ArrayList<Integer>();
        List<Object> selectCodes = new ArrayList<Object>();
       int rowCount =  tableModel.getRowCount();
       for(int i = 0;i<rowCount;i++)
       {
         Boolean isSelect = (Boolean) getJtable().getModel().getValueAt(i, 2);
         if(isSelect)
         {
             rows.add(i);
         }
       }
        for(int row:rows)
        {
            selectCodes.add(getJtable().getModel().getValueAt(row, 0));
        }
       return selectCodes.toArray();
    }

}
