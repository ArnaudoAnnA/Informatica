/**
 * Created by Anna on 19/06/2018.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;


public class Main extends JFrame
{
    private Vector<String[]> parchi = null;

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main();
            }
        });
    }

    private Main()
    {
        initUI();
        parchi = new Vector<String[]>();
    }

    private void initUI()
    {
        this.setTitle("Parco Giochi");
        this.setSize(300, 400);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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

            JPanel pnl_details = new JPanel(new GridLayout(3, 2));

                //components
                JLabel lbl_descrizione = new JLabel("Descrizione");
                JLabel lbl_latitudine = new JLabel("Latitudine");
                JLabel lbl_longitudine = new JLabel("Longitudine");
                JTextField txt_descrizione = new JTextField();
                JTextField txt_latitudine = new JTextField();
                JTextField txt_longitudine = new JTextField();

                pnl_details.add(lbl_descrizione);
                pnl_details.add(txt_descrizione);
                pnl_details.add(lbl_latitudine);
                pnl_details.add(txt_latitudine);
                pnl_details.add(txt_longitudine);
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

            }
        });


        this.setVisible(true);
    }
}
