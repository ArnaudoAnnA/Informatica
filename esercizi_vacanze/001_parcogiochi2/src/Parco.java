import javax.swing.*;
import java.io.*;
import java.util.Vector;

/**
 * Created by Anna on 20/06/2018.
 */
public class Parco
{
    private String descrizione = null;
    private double latitudine = 0;
    private double longitudine = 0;

    public Parco(String descrizione, double latitudine, double longitudine)
    {
        this.descrizione = descrizione;
        this.longitudine = longitudine;
        this.latitudine = latitudine;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public double getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(double latitudine) {
        this.latitudine = latitudine;
    }

    public double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(double longitudine) {
        this.longitudine = longitudine;
    }

    public String[] toStringArray()
    {
        String[] array = new String[3];
        array[0] = this.getDescrizione();
        array[1] = Double.toString(this.getLatitudine());
        array[2] = Double.toString(this.getLongitudine());

        return array;
    }

    /**
     * This method save a vector of Parco into a csv file using ";" as separator
     * @param fileOUT  the pointer to the file where the csv has to be saved
     * @param parchi    the vector of Parco that has to been saved
     * @throws IOException
     */
    public static void saveVectorToCSV(File fileOUT, Vector<Parco> parchi) throws IOException
    {
        try
        {
            FileWriter fw = new FileWriter(fileOUT);
            BufferedWriter bw = new BufferedWriter(fw);

            for(Parco parco: parchi)
            {
                String[] arr_parco = parco.toStringArray();

                bw.write(arr_parco[0]+";"+arr_parco[1]+";"+arr_parco[2]+";\n");
            }


            bw.close();
        }catch (IOException ioe)
        {
            JOptionPane.showMessageDialog(null, "errore nel salvataggio del file");
        }
    }

    /**
     * This method read all the file given and parse every line to a Parco object. Every Parco object is putted into a vector of Parco.
     * @param fileIN    the pointer to the file that has to be read
     * @return  a Vector<Parco> object
     * @throws IOException
     */
    public static Vector<Parco> loadVectorFromCSV(File fileIN) throws IOException
    {
        Vector<Parco> parchi = new Vector<Parco>();

        //pointer to the file
        FileReader fr = new FileReader(fileIN);
        BufferedReader br = new BufferedReader(fr);

        String line;
        String[] splitLine;

        //I read all the file
        while((line=br.readLine())!=null)
        {
            splitLine = line.split(";");            //I split the csv line read

            //I control that the line read is valid
            if(splitLine.length!=3)
            {
                throw new IOException();
            }

            Parco p = new Parco(splitLine[0], Double.parseDouble(splitLine[1]), Double.parseDouble(splitLine[2]));      //I create a new Parco from the line read
            parchi.add(p);  //I add the Parco to the vector
        }

        br.close();

        return parchi;
    }
}
