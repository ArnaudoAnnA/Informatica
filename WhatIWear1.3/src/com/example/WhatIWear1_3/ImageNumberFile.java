package com.example.WhatIWear1_3;

import java.io.*;
import java.nio.Buffer;

/**
 * Created by ANNA on 04/09/2018.
 *
 * This class lead a File in which is written only one number that indicates the number that
 * has to be given to the new images of the dresses added
 */
public class ImageNumberFile extends File
{
    public ImageNumberFile(File applicationDir) throws IOException
    {
        super(applicationDir+"/imageNumberFile.csv");

        boolean firstTime = this.createNewFile();

        if(firstTime)
        {
            this.write("0");
        }
    }

    private void write(String string) throws IOException
    {
        FileWriter fw = new FileWriter(this);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write(string);

        bw.close();
        fw.close();
    }

    private String readLine() throws IOException
    {
        FileReader fr = new FileReader(this);
        BufferedReader br = new BufferedReader(fr);

        String ret = br.readLine();

        br.close();
        fr.close();

        return ret;
    }

    public int getNextNumber() throws IOException
    {
        int ret = Integer.parseInt(this.readLine());
        Integer nextNumber = ret+1;
        this.write(nextNumber.toString());

        return ret;
    }

    public void resetNumber() throws IOException
    {
        this.write("0");
    }
}
