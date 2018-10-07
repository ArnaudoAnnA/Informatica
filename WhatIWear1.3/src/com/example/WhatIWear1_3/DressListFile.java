package com.example.WhatIWear1_3;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.format.Time;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Vector;

/**
 * Created by Anna on 11/07/2018.
 */
public abstract class DressListFile
{
    private static File dressListFile = null;
    private static File imageDir = null;
    private static File applicationDir = null;
    private static ImageNumberFile imageNumberFile = null;
    private static final String imageDirName = "images";


    /**
     * <b>add</b><br>
     *     <p>
     *      This method add a new dress to the file.csv that contains all the dresses.<br>
     *      The dress is saved as a line whith all information (see de returned String by the method dress.toString())<br>
     *      <b>NOTE:</b> if the file doesn't already exist, it creates it
     *     </p>
     *     <p><b>reference to:</b> class Dress, dress.addToFile(file), this.copyImageToInternal()
     *     </p>
     * @param context
     * @param image
     * @param listv_style
     * @param spin_category1
     * @param spin_category2
     * @param spin_liking
     * @param listv_climate
     * @throws IOException
     */
    public static void add(Context context, Bitmap image, ListView listv_style, Spinner spin_category1, Spinner spin_category2, Spinner spin_liking, ListView listv_climate) throws IOException
    {
        if(dressListFile== null)
        {
            initializeFiles(context);
        }

        File dressImage = copyImageToInternal(image);
        Dress dress = new Dress(dressImage, listv_style, spin_category1, spin_category2, spin_liking, listv_climate);
        dress.addToFile(dressListFile);
    }

    /**
     * <b>add</b><br>
     *     <p>
     *      This method add a new dress to the file.csv that contains all the dresses<br>
     *      The dress is saved as a line whith all information (see de returned String by the method dress.toString())<br>
     *      <b>NOTE:</b> if the file doesn't already exist, it creates it
     *     </p>
     *     <p><b>reference to:</b> class Dress, dress.addToFile(file), this.copyImageToInternal()
     *     </p>
     * @param context
     * @param image
     * @param style
     * @param category1
     * @param category2
     * @param liking
     * @param climate
     * @throws IOException
     */
    public static void add(Context context, Bitmap image, String style, String category1, String category2, int liking, String climate) throws IOException
    {
        if(dressListFile== null)
        {
            initializeFiles(context);
        }

        File dressImage = copyImageToInternal(image);
        Dress dress = new Dress(dressImage, style, category1, category2, liking, climate);
        dress.addToFile(dressListFile);
    }

    /**
     * This method initialize all the pointer to the file used in this class
     * @param context the context of the application
     * @throws IOException
     */
    private static void initializeFiles(Context context) throws IOException
    {
        applicationDir = context.getFilesDir();
        dressListFile = new File(applicationDir+"/dressListFile.csv");
        dressListFile.createNewFile();
        imageDir = new File(applicationDir+"/"+imageDirName);
        imageDir.mkdir();
        imageNumberFile = new ImageNumberFile(applicationDir);
    }

    /**
     * This method creates a new file and then copy the last selected image (which is showed in the imgbtt_dressImage) into them.
     * @return a file pointer to the new file created
     * @throws IOException
     */
    public static File copyImageToInternal(Bitmap selectedImageBitmap) throws IOException
    {
        //creating the file where the image will be copied
        File destination = new File(imageDir+"/image"+imageNumberFile.getNextNumber()+".jpeg");
        destination.createNewFile();

        FileOutputStream fos = new FileOutputStream(destination);
        selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

        fos.close();

        return destination;
    }


    /**
     * <b>readAllFile()</b><br>
     *     <p>
     *         This method read all the dressListFile.csv
     *     </p>
     *     <p>
     *         <b>reference to:</b> class Dress, Dress.stringToDress()
     *     </p>
     * @return Vector of Dress
     * @throws IOException if the dressListFile.csv doesn't already exist (the user didn't already saved any dresses)
     */
    public static Vector<Dress> readAllFile(Context context) throws IOException, DressNotFoundExeption
    {
        Vector<Dress> dresses = null;

        if(dressListFile== null)    /*I control if the file doesn't already exist
                                        - in positive case I throw a fileNotFoundException
                                        - in negative case I continue reading the file*/
        {
            initializeFiles(context);
        }
        /*I read all the file and, for each line, I create a dress that will be added to the vector of dresses*/
        dresses = new Vector<Dress>();

        FileReader fr = new FileReader(dressListFile);
        BufferedReader br = new BufferedReader(fr);

        String line;
        while((line= br.readLine())!= null)
        {
            dresses.add(Dress.stringToDress(line));
        }

        if(dresses.size()==0)
        {
            throw new DressNotFoundExeption();
        }

        return dresses;
    }


    /**
     * This method takes a vector of dresses and decide which dresses have the given features.<br>
     *     P.S. if you want to not do a division in base one of the features, you just have to pass "" as parameter<br>
     * @param dresses the vector of dresses that the method have to read
     * @param category
     * @param style
     * @param climate
     * @return a vector of Dress. Each dress is added to the vector tree times if its liking is tree, twice if its liking is two ec..
     */
    public static Vector<Dress> matches(Vector<Dress> dresses, String category, String style, String climate, Time addedFromThisDate, Time usedFromThisDate)
    {
        Vector<Dress> ret = new Vector<Dress>();

        /*I cycle all the vector of dresses and save the dresses that has the searched category, style, climate, they are added after
            the date passed as parameter and they are been used after the date passed as parameter
             NOTE: I add to the vector a number of dresses due to the liking of the dress
             NOTE: I use the method contains() because a dress can belong different subset and the information string
                    contains all the names of these subset*/

        for(Dress d: dresses)
        {
            if(d.getcategory1().contains(category) && d.getStyle().contains(style)&& d.getClimate().contains(climate) && d.getDateAdded().after(addedFromThisDate) && d.getDateUsed().after(usedFromThisDate))
            {
                for(int i = 0; i<=d.getLiking(); i++)
                {
                    ret.add(d);
                }
            }
        }

        return ret;
    }

    public static Vector<Dress> matches(Vector<Dress> dresses, String style, String climate, Time addedFromThisDate, Time usedFromThisDate)
    {
        return matches(dresses, "", style, climate, addedFromThisDate, usedFromThisDate);
    }


}
