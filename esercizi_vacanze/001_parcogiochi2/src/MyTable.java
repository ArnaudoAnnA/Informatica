import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by Anna on 20/06/2018.
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

    public void clear()
    {
        DefaultTableModel model = (DefaultTableModel) this.getModel();
        model.setRowCount(0);
        this.setModel(model);
    }

    public void loadFromVector(Vector<Parco> vector)
    {
        this.clear();
        String[] tableLine;

        for(Parco parco: vector)
        {
            //I convert the parco object into an array of string
            tableLine = parco.toStringArray();

            this.addRow(tableLine);     //I add the array of string to the table
        }
    }
}
