package com.heilan.view.file;

import java.awt.ComponentOrientation;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXButton;
import org.springframework.stereotype.Component;

import com.heilan.tool.HLTool;
import com.heilan.view.LocalClassPanel;

@Component
public class FilesPanel extends JPanel
{

    private static final long serialVersionUID = -7268450359351899989L;
    @Resource
    LocalClassPanel localClassPanel;
    @Resource
    FilesTableModel filesTableModel;
    FilesDropTarget filesDropTarget;
    private JLabel countLabel;
    private JTable table;

    public JLabel getCountLabel()
    {
        if (countLabel == null)
        {
            countLabel = new JLabel("当前文件数:0");
        }
        return countLabel;
    }

    public void initialize()
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEtchedBorder());
        setFont(HLTool.getFont());
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(getJtable());
        scrollPane.setPreferredSize(new Dimension(600, 500));
        add(getTopPanal());
        add(scrollPane);
       
      
        
    }
    
    private JPanel getTopPanal(){
        JPanel panal = new JPanel();
        panal.setLayout(new FlowLayout(FlowLayout.LEFT));
        panal.add(getJLabel() );
        panal.add(getDelButton());
        panal.add(getClearButton());
        panal.add(getSubmitDiaButton());
        return panal;
    }
    private JButton getDelButton(){
        JButton button = new JButton("删除");
        button.setFont(HLTool.getBtnFont());
        button.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                delRows();
                resetRowNo();
            }
        });
        return button;
    }
    
    private JButton getClearButton(){
        JButton button = new JButton("清空");
        button.setFont(HLTool.getBtnFont());
        button.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                for (int index = filesTableModel.getRowCount() - 1; index >= 0; index--) {
                    filesTableModel.removeRow(index);
                }
            }
        });
        return button;
    }
    
    
    private JButton getSubmitDiaButton(){
        final JButton button = new JButton("手工输入");
        button.setFont(HLTool.getBtnFont());
        button.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                new FilesDialog((JFrame) button.getRootPane().getParent(),filesTableModel,localClassPanel).show();
            }
        });
        return button;
    }
    private JLabel getJLabel()
    {
        JLabel label = new JLabel("请将需要更新的文件拖进来：");
        label.setFont(getFont());
        return label;
    }

    private JTable getJtable()
    {
        if(table == null)
        {
            table = new JTable(filesTableModel);
            table.setAutoscrolls(true);
            filesDropTarget = new FilesDropTarget();
            filesDropTarget.setFilesTableModel(filesTableModel);
            filesDropTarget.setLocalClassPanel(localClassPanel);
            this.setDropTarget(filesDropTarget);
            
            //序号
            getJtable().getColumnModel().getColumn(0).setCellEditor(getJtable().getDefaultEditor(String.class));
            getJtable().getColumnModel().getColumn(0).setCellRenderer(getJtable().getDefaultRenderer(String.class));
            getJtable().getColumnModel().getColumn(0).setPreferredWidth(30);
            getJtable().getColumnModel().getColumn(0).setMaxWidth(40);
            getJtable().getColumnModel().getColumn(0).setMinWidth(20);
            //路径
            getJtable().getColumnModel().getColumn(1).setCellEditor(getJtable().getDefaultEditor(String.class));
            getJtable().getColumnModel().getColumn(1).setCellRenderer(getJtable().getDefaultRenderer(String.class));
            getJtable().getColumnModel().getColumn(1).setPreferredWidth(300);
        }
        return table;
    }
    
    private void delRows(){
        int[] rows = table.getSelectedRows();
        System.out.println(rows);
        for(int i = rows.length-1;i>=0;i--)
        {
            filesTableModel.removeRow(rows[i]);
            
        }
         
    }
    private void resetRowNo(){
        for(int i = 0;i<filesTableModel.getRowCount();i++)
        {
            filesTableModel.setValueAt(i, i, 0);
        }

    }
    
}
