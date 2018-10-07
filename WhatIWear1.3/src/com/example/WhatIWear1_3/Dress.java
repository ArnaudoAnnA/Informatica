package com.example.WhatIWear1_3;

import android.app.LauncherActivity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.util.SparseBooleanArray;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.*;

/**
 * Created by Anna on 29/06/2018.
 */
public class Dress
{
    private File image = null;
    private String style = null; 
    private String category1 = null;
    private String category2 = null;
    private int liking = 0;
    private String climate = null;
    private Time dateAdded = null;
    private Time dateUsed = null;

    public Dress(File image, String style, String category1, String category2, int liking, String climate, Time dateAdded, Time dateUsed)
    {
        this(image, style, category1, category2, liking, climate);
        this.dateAdded = dateAdded;
        this.dateUsed = dateUsed;
    }

    public Dress(File image, String style, String category1, String category2, int liking, String climate)
    {
        this.image = image;
        this.style = style;
        this.category1 = category1;
        if(category2==null)
        {
            this.category2 = "";
        }else
        {
            this.category2 = category2;
        }
        this.liking = liking;
        this.climate = climate;

        this.dateAdded = new Time();
        dateAdded.setToNow();

        this.dateUsed = new Time();
        this.dateUsed.set(100000);
    }

    public Dress(Bitmap imageBitmap, ListView listv_style, Spinner spin_category1, Spinner spin_category2, Spinner spin_liking, ListView listv_climate) throws  IOException
    {
        this(DressListFile.copyImageToInternal(imageBitmap), listv_style, spin_category1, spin_category2, spin_liking, listv_climate);
    }

    public Dress(File image, ListView listv_style, Spinner spin_category1, Spinner spin_category2, Spinner spin_liking, ListView listv_climate)
    {
        this.image = image;
        this.style = listViewToString(listv_style);
        this.category1 = spin_category1.getSelectedItem().toString();
        if(spin_category2.isEnabled())
        {
            this.category2 = spin_category2.getSelectedItem().toString();
        }else
        {
            this.category2 = "";
        }
        this.liking = spin_liking.getSelectedItemPosition();
        this.climate = listViewToString(listv_climate);

        this.dateAdded = new Time();
        dateAdded.setToNow();

        this.dateUsed = new Time();
        this.dateUsed.set(100000);
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getcategory1() {
        return category1;
    }

    public void setcategory1(String category1) {
        this.category1 = category1;
    }

    public String getCategory2() {
        return category2;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public int getLiking() {
        return liking;
    }

    public void setLiking(int liking) {
        this.liking = liking;
    }

    public String getClimate() {
        return climate;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public Time getDateAdded() {
        return dateAdded;
    }

    public void setDate(Time dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getCategory1() {
        return category1;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public Time getDateUsed()
    {
        return this.dateUsed;
    }

    public void setDateUsedToNow()
    {
        this.dateUsed.setToNow();
    }

    public String toString()
    {
        return this.image.getPath()+";"+
                this.style+";"+
                this.category1+";"+
                this.category2+";"+
                this.liking+";"+
                this.climate+";"+
                String.format("%d/%d/%d", dateAdded.monthDay, dateAdded.month, dateAdded.year)+";"+
                String.format("%d/%d/%d", dateUsed.monthDay, dateUsed.month, dateUsed.year)+";";
    }

    public void addToFile(File dressListFile) throws IOException
    {
        FileWriter fw = new FileWriter(dressListFile, true);    //I create a FileWriter in append mode
        BufferedWriter bw = new BufferedWriter(fw);

        String dressLine = this.toString()+"\n";
        bw.write(dressLine);
        bw.close();
    }

    public static Dress stringToDress(String string)
    {
        String[] splitString = string.split(";");
        File image = new File(splitString[0]);
        int liking = Integer.parseInt(splitString[4]);

        //parsing the string to the date in which the dress was added
            Time dateAdded = new Time();
            String[] dateAddedString = splitString[6].split("/");
            dateAdded.set(Integer.parseInt(dateAddedString[0]), Integer.parseInt(dateAddedString[1]), Integer.parseInt(dateAddedString[2]));

        //parsing the string into the date in which the dress was used
            Time dateUsed = new Time();
            String[] dateUsedString = splitString[6].split("/");
            dateUsed.set(Integer.parseInt(dateUsedString[0]), Integer.parseInt(dateUsedString[1]), Integer.parseInt(dateUsedString[2]));

        Dress ret = new Dress(image, splitString[1], splitString[2], splitString[3], liking, splitString[5], dateAdded, dateUsed);

        return ret;
    }

    public void showInImageView(ImageView imageView)
    {
        imageView.setImageURI(Uri.fromFile(this.getImage()));
    }

    /**
     * This method is called by the constructor that uses views to make a dress. It allows to make a string<br>
     *     using the checked items in the passed listView
     * @param listView
     * @return
     */
    private String listViewToString(ListView listView)
    {
        /*I get all the selected items in listView and concat them to made one string*/
        String ret = "";
        SparseBooleanArray array = listView.getCheckedItemPositions();

        for(int i = 0; i<array.size(); i++)
        {
            if(array.valueAt(i))
            {
                ret+=listView.getItemAtPosition(i).toString();
            }
        }

        return ret;
    }

}
