package com.heilan.view.file;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import org.springframework.stereotype.Component;

@Component
public class FilesTableModel extends DefaultTableModel
{
    private static final long serialVersionUID = 2988625519374212101L;

    public FilesTableModel()
    {
        addColumn("序号");
        addColumn("路径");
    }

    @Override
    public void addRow(Object[] rowData)
    {
        boolean flag = true;
        Vector data = this.getDataVector();
        for (int i = 0; i < data.size(); i++)
        {
            String path = (String) ((Vector) data.elementAt(i)).elementAt(1);
            if (path.equals(rowData[1]))
                flag = false;
        }

        if (flag)
            super.addRow(rowData);
    }
    @Override
    public boolean isCellEditable(int row, int column)
    {
       return false;
    }
    
    public List<String> getFilePaths()
    {
        List<String> filePaths = new ArrayList<String>();
        for(int i = 0;i<this.getRowCount();i++)
        {
            String path =(String) this.getValueAt(i,1);
            filePaths.add(path);
        }
        return filePaths;
    }
}
