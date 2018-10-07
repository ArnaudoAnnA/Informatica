package com.example.WhatIWear1_3;

import android.media.Image;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.Random;
import java.util.Vector;

/**
 * Created by Anna on 10/08/2018.
 */
public class DressImageView
{
    private ImageView imageView = null;
    private ImageButton imgbtt_left = null;
    private ImageButton imgbtt_right = null;
    private String category = null;
    private int nShownDress = -1;
    private Vector<Dress> subsetDresses = null;

    public DressImageView()
    {

    }

    public DressImageView(ImageView imageView, ImageButton imgbtt_left, ImageButton imgbtt_right, String category, int nShownDress, Vector<Dress> subsetDresses)
    {
        this.imageView = imageView;
        this.imgbtt_left = imgbtt_left;
        this.imgbtt_right = imgbtt_right;
        this.category = category;
        this.nShownDress = nShownDress;
        this.subsetDresses = subsetDresses;

        final Vector<Dress> subsetDressesFinal = subsetDresses;

        //listeners
        imgbtt_left.setOnClickListener(new LeftButtonListener());
        imgbtt_right.setOnClickListener(new RightButtonListener());

        //I set the dimension of the imageView due to the category of the dress that has to sow
        this.setImageViewDimension(category);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public ImageButton getImgbtt_left() {
        return imgbtt_left;
    }

    public void setImgbtt_left(ImageButton imgbtt_left) {
        this.imgbtt_left = imgbtt_left;
        imgbtt_left.setOnClickListener(new LeftButtonListener());
    }

    public ImageButton getImgbtt_right() {
        return imgbtt_right;
    }

    public void setImgbtt_right(ImageButton imgbtt_right) {
        this.imgbtt_right = imgbtt_right;
        imgbtt_right.setOnClickListener(new RightButtonListener());
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
        //I set the dimension of the imageView due to the category of the dress that has to sow
        this.setImageViewDimension(category);
    }

    public int getnShownDress() {
        return nShownDress;
    }

    public void setnShownDress(int nShownDress) {
        this.nShownDress = nShownDress;
    }

    public Vector<Dress> getSubsetDresses() {
        return subsetDresses;
    }

    public void setSubsetDresses(Vector<Dress> subsetDresses) {
        this.subsetDresses = subsetDresses;
    }

    public Dress getShownDress()
    {
        return subsetDresses.get(nShownDress);
    }

    public static int[] getViewDimensionDueCategory(String category)       /*!!!!NOTE!!!!: if the category's names has been modified, this method have to
                                                                        been modified!*/
    {
        int[] dimension = new int[2];

        switch (category)
        {
            case "cap":
                dimension[0] = 350;
                dimension[1] = 350;
            break;

            case "bag":
                dimension[0] = 350;
                dimension[1] = 350;
                break;

            case "shirt":
                dimension[0] = 260;
                dimension[1] = 350;
                break;

            case "sweatshirt":
                dimension[0] = 350;
                dimension[1] = 350;
                break;

            case "pants":
                dimension[0] = 350;
                dimension[1] = 530;
                break;

            case "jacket":
                dimension[0] = 350;
                dimension[1] = 530;
                break;
        }

        return dimension;
    }

    private void setImageViewDimension(String category)
    {
        int[] dimension = getViewDimensionDueCategory(category);
        this.getImageView().setMaxWidth(dimension[0]);
        this.getImageView().setMaxHeight(dimension[1]);
        this.getImageView().setMinimumWidth(dimension[0]);
        this.getImageView().setMinimumHeight(dimension[1]);
    }

    /**
     * This method get a random dress from the passed subset of dresses and set the passed imageView to show the image of the<br>
     *     dress got.
     * @return an int object that contains the number of the selected dress into the subset of dresses
     * @throws DressNotFoundExeption if the size of the passed subset is 0 (no dress has been found)
     */
    public int getRandomDress() throws DressNotFoundExeption
    {
        int randomN = -1;

        if(subsetDresses.size()!=0)
        {
            //getting a casual number
            Random random = new Random();
            randomN = random.nextInt(subsetDresses.size());

            //getting the dress corresppondent to the random number
            Dress randomDress = subsetDresses.get(randomN);
            imageView.setImageURI(Uri.fromFile(randomDress.getImage()));
            nShownDress = randomN;
        }else
        {
            throw new DressNotFoundExeption();
        }

        return randomN;
    }

    //INNER CLASSES
    private class LeftButtonListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            //I decrement the number of the dress shown
            int newN = getnShownDress()-1;
            if(newN<0)
            {
                newN = getSubsetDresses().size()-1;
            }
            setnShownDress(newN);

            //I update the image shown by the imageView
            getSubsetDresses().get(newN).showInImageView(imageView);
        }
    }

    private class RightButtonListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            //I increment the number of the dress shown
            int newN = getnShownDress()+1 % getSubsetDresses().size();
            setnShownDress(newN);

            //I update the image shown by the imageView
            getSubsetDresses().get(newN).showInImageView(imageView);
        }
    }
}
