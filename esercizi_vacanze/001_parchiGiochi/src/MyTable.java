import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * Created by Anna on 19/06/2018.
 */
public class MyTable extends JTable
{
    public void setColumns(String[] names)
    {
        for(int i = 0; i< names.length; i++)
        {
            this.addColumn(names[i]);
        }
    }

    public void addColumn(String name)
    {
        DefaultTableModel model = (DefaultTableModel) this.getModel();
        model.addColumn(name);
        this.setModel(model);
    }

    public void addRow(String[] fields)
    {
        DefaultTableModel model = (DefaultTableModel) this.getModel();
        model.addRow(fields);
        this.setModel(model);
    }
}
