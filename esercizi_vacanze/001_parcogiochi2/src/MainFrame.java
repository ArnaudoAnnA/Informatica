import javafx.stage.FileChooser;

import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.io.*;
import java.util.Vector;
/**
 * Created by Anna on 20/06/2018.
 */
public class MainFrame extends JFrame
{
    private Vector<Parco> parchi = null;
    private boolean saved = true;

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });
    }

    private MainFrame()
    {
        initUI();
        parchi = new Vector<Parco>();
    }

    private void initUI()
    {
        this.setTitle("Parco Giochi");
        this.setSize(300, 400);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        //NORTH
            //components
            JMenuBar mnb_north = new JMenuBar();
            JMenu mn_file = new JMenu("file");

                //menu items
                JMenuItem mni_carica = new JMenuItem("Carica file CSV...");
                JMenuItem mni_salva = new JMenuItem("Salva file CSV...");
                JMenuItem mni_esci = new JMenuItem("Esci");

                mn_file.add(mni_carica);
                mn_file.add(mni_salva);
                mn_file.add(mni_esci);

                mnb_north.add(mn_file);

            this.add(mnb_north, BorderLayout.NORTH);

        //CENTER
            JPanel pnl_center = new JPanel(new GridLayout(2, 1));

                //components
                MyTable tbl_content = new MyTable();
                tbl_content.setColumns(new String[]{"Descrizione", "Longitudine", "Latitudine"});
                JScrollPane scrlpane = new JScrollPane(tbl_content);
                pnl_center.add(scrlpane);

                JPanel pnl_details = new JPanel(new GridLayout(3, 2));

                    //components
                    JLabel lbl_descrizione = new JLabel("   Descrizione:");
                    JLabel lbl_latitudine = new JLabel("    Latitudine:");
                    JLabel lbl_longitudine = new JLabel("   Longitudine:");
                    JTextField txt_descrizione = new JTextField();
                    JTextField txt_latitudine = new JTextField();
                    JTextField txt_longitudine = new JTextField();

                    pnl_details.add(lbl_descrizione);
                    pnl_details.add(txt_descrizione);
                    pnl_details.add(lbl_latitudine);
                    pnl_details.add(txt_latitudine);
                    pnl_details.add(lbl_longitudine);
                    pnl_details.add(txt_longitudine);

                    pnl_center.add(pnl_details);

                this.add(pnl_center, BorderLayout.CENTER);

        //SOUTH
            JPanel pnl_south = new JPanel(new FlowLayout(FlowLayout.TRAILING));
                //components
                JButton btt_aggiungi = new JButton("Aggiungi nuovo parco");
                pnl_south.add(btt_aggiungi);

            this.add(pnl_south, BorderLayout.SOUTH);

        //LISTENERS
        mni_carica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    JFileChooser fc = new JFileChooser();       //I show the fileChooser to allow the user to select the csv file
                    if(fc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
                    {
                        File selectedFile = fc.getSelectedFile();

                        //I control if the selected file is a csv file
                        if(!selectedFile.getName().substring(selectedFile.getName().lastIndexOf(".")).equals(".csv"))
                        {
                            throw new IOException("formato non supportato");
                        }

                        parchi = Parco.loadVectorFromCSV(selectedFile);
                        tbl_content.loadFromVector(parchi);
                        setTitle(selectedFile.getName());
                        setSaved();
                    }
                }catch (IOException ioe)
                {
                    JOptionPane.showMessageDialog(null, "Errore nel caricamento del file");
                }
            }
        });

        Mni_salvaActionListener mni_salvaActionListener = new Mni_salvaActionListener();
        mni_salva.addActionListener(mni_salvaActionListener);

        mni_esci.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!saved)
                {
                    //I answer to the user if he want to save
                    int ret = JOptionPane.showConfirmDialog(null, "salvare prima di uscire?", "salvataggio", JOptionPane.YES_NO_CANCEL_OPTION);

                    switch (ret)
                    {
                        case JOptionPane.YES_OPTION:
                            mni_salvaActionListener.actionPerformed(null);
                            System.exit(0);
                            break;

                        case JOptionPane.NO_OPTION:
                            System.exit(0);
                            break;

                    }
                }else
                {
                    System.exit(0);
                }
            }
        });

        btt_aggiungi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Parco parco = new Parco(txt_descrizione.getText(),
                                        Double.parseDouble(txt_latitudine.getText()),
                                        Double.parseDouble(txt_longitudine.getText()));
                parchi.add(parco);
                tbl_content.addRow(parco.toStringArray());
                setNotSaved();
            }
        });

        this.setVisible(true);
    }

    private void setNotSaved()
    {
        if(saved)   //If it was saved before
        {
            saved= false;
            this.setTitle(this.getTitle()+"*");
        }
    }

    private void setSaved()
    {
        if(!saved)  //if if wasn't saved before
        {
            saved= true;
            this.setTitle(this.getTitle().substring(0, this.getTitle().lastIndexOf("*")));
        }
    }

    //INNER CLASS
    private class Mni_salvaActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            try
            {
                JFileChooser fc = new JFileChooser();
                if(fc.showSaveDialog(null)==JFileChooser.APPROVE_OPTION)
                {
                    //I control if the extension of the selected file is "csv"
                    if(!fc.getSelectedFile().getName().substring(fc.getSelectedFile().getName().indexOf(".")).equals(".csv"))
                    {
                        throw new IOException("formato non supportato");
                    }

                    Parco.saveVectorToCSV(fc.getSelectedFile(), parchi);
                    setSaved();
                }
            }catch (IOException ioe)
            {
                JOptionPane.showMessageDialog(null, "errore nel salvataggio del file");
            }

        }
    }





}
