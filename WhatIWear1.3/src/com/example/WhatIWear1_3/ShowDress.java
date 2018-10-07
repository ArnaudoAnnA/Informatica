package com.example.WhatIWear1_3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.text.format.Time;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.Vector;

/**
 * Created by Anna on 03/07/2018.
 */
public class ShowDress extends Activity {


    private Vector<Dress> dresses = null;
    private DressImageView[] dressImagesViews = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_showdress);

        String[] arr_categories = this.getCategoriesFromResString(getApplicationContext());

        //views' handlers
        Button btt_again = (Button) findViewById(R.id.btt_again);
        Button btt_ok = (Button) findViewById(R.id.btt_ok);

            //SETTING THE VALUES OF THE ARRAYS USED AS PROPRIETY
            dressImagesViews = new DressImageView[6];

            /*CAP*/
            dressImagesViews[0] = new DressImageView();
            dressImagesViews[0].setImageView((ImageView) findViewById(R.id.img_cap));
            dressImagesViews[0].setImgbtt_left((ImageButton) findViewById(R.id.imgbtt_capLeft));
            dressImagesViews[0].setImgbtt_right((ImageButton) findViewById(R.id.imgbtt_capRight));
            dressImagesViews[0].setCategory(arr_categories[0]);


            /*BAG*/
            dressImagesViews[1] = new DressImageView();
            dressImagesViews[1].setImageView((ImageView) findViewById(R.id.img_bag));
            dressImagesViews[1].setImgbtt_left((ImageButton) findViewById(R.id.imgbtt_bagLeft));
            dressImagesViews[1].setImgbtt_right((ImageButton) findViewById(R.id.imgbtt_bagRight));
            dressImagesViews[1].setCategory(arr_categories[1]);


            /*SHIRT*/
            dressImagesViews[2] = new DressImageView();
            dressImagesViews[2].setImageView((ImageView) findViewById(R.id.img_shirt));
            dressImagesViews[2].setImgbtt_left((ImageButton) findViewById(R.id.imgbtt_shirtLeft));
            dressImagesViews[2].setImgbtt_right((ImageButton) findViewById(R.id.imgbtt_shirtRight));
            dressImagesViews[2].setCategory(arr_categories[2]);


            /*SWEATSHIRT*/
            dressImagesViews[3] = new DressImageView();
            dressImagesViews[3].setImageView((ImageView) findViewById(R.id.img_sweatshirt));
            dressImagesViews[3].setImgbtt_left((ImageButton) findViewById(R.id.imgbtt_sweatshirtLeft));
            dressImagesViews[3].setImgbtt_right((ImageButton) findViewById(R.id.imgbtt_sweatshirtRight));
            dressImagesViews[3].setCategory(arr_categories[3]);


            /*PANTS*/
            dressImagesViews[4] = new DressImageView();
            dressImagesViews[4].setImageView((ImageView) findViewById(R.id.img_pants));
            dressImagesViews[4].setImgbtt_left((ImageButton) findViewById(R.id.imgbtt_pantsLeft));
            dressImagesViews[4].setImgbtt_right((ImageButton) findViewById(R.id.imgbtt_pantsRight));
            dressImagesViews[4].setCategory(arr_categories[4]);


            /*JACKET*/
            dressImagesViews[5] = new DressImageView();
            dressImagesViews[5].setImageView((ImageView) findViewById(R.id.img_jacket));
            dressImagesViews[5].setImgbtt_left((ImageButton) findViewById(R.id.imgbtt_jacketLeft));
            dressImagesViews[5].setImgbtt_right((ImageButton) findViewById(R.id.imgbtt_jacketRight));
            dressImagesViews[5].setCategory(arr_categories[5]);


        //listeners
        btt_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRandomOutfit();
            }
        });

        btt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //I set the date on which the dress has been used to each dress showed
                for(int i = 0; i<6; i++)
                {
                    dressImagesViews[i].getShownDress().setDateUsedToNow();
                }

                finish();
            }
        });

        //when all views are ready, I get the first random outfit to show
         getRandomOutfit();
    }


    /**
     * This method get a random outfit due to the data in the intent that started the activity (the intent from DressChooser.java).<br>
     * The dresses chosen are showed in the correspondent image view.
     */
    private void getRandomOutfit()
    {
        //getting the intent
        Intent intent=getIntent();
        String style = intent.getStringExtra(getString(R.string.key_searchedStyle));
        String climate = intent.getStringExtra(getString(R.string.key_searchedClimate));
        String bag;
        try
        {
            bag = intent.getStringExtra(getString(R.string.key_thereIsBag));
        }catch (Exception e)
        {
            bag = null;
        }
        Boolean cap = intent.getBooleanExtra(getString(R.string.key_thereIsCap), false);
        Boolean jacket = intent.getBooleanExtra(getString(R.string.key_thereIsJacket), false);
        Boolean onlyNewClothes = intent.getBooleanExtra(getString(R.string.key_onlyNewClothes), false);
        Boolean onlyLastUsedClothes = intent.getBooleanExtra(getString(R.string.key_onlyRecentlyUsed), false);

        /*if the checkbox onlyNewClothes is checked I set the min date(before this date clothes are not considered) as one week ago,
           if it isn't checked, I set the in date as the earliest (so all clothes will be considered)
         */
        Time addedFromThisDate = setDueToCheckBoxChecked(onlyNewClothes);
        Time usedFromThisDate = setDueToCheckBoxChecked(onlyLastUsedClothes);

        //I open and read all the dressList file
        try
        {
            dresses = DressListFile.readAllFile(getApplicationContext());

        }catch (DressNotFoundExeption dnfe)
        {
            Toast.makeText(getApplicationContext(), "There are no saved dresses yet", Toast.LENGTH_SHORT).show();
        }catch (IOException ioe)
        {
            Toast.makeText(getApplicationContext(), "There was an error in charging the dresses", Toast.LENGTH_SHORT).show();
        }


        /*Now I take care for each image in order of the layout*/
        for(int i = 0; i<6; i++)
        {
                /*in each cycle i take care for the view's group correspondent of the category,
                    I set the subset of dresses that may be shown in the imageView,
                    then I get a random number and I use it to get a random dress from the subset already created
                 */
            String category = dressImagesViews[i].getCategory();
            if(!(category.equals("bag") && bag==null) && !(category.equals("cap")&& !cap) && !(category.equals("jacket")&&!jacket))
            {
                try
                {
                    DressImageView currentImageView = dressImagesViews[i];
                    currentImageView.setSubsetDresses(DressListFile.matches(dresses, currentImageView.getCategory(), style, climate, addedFromThisDate, usedFromThisDate));
                    currentImageView.getRandomDress();
                }catch (DressNotFoundExeption dnfe)
                {
                    Toast.makeText(getApplicationContext(), "no dress found for the category \""+ dressImagesViews[i].getCategory()+"\"", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    /**
     * This method is used by the method "getRandomOutfit()" to set the value of the minimum date that the searched dress<br>
     * have to have (dresses before that date are not accepted).
     * @param checked the input value from the checkBox
     * @return a Time object that indicates the minimum date
     */
    private Time setDueToCheckBoxChecked(Boolean checked)
    {
        Time ret = new Time();

        if(checked)
        {
            ret.setToNow();
            ret.allDay = true;
            ret.set(ret.toMillis(true)-604800000);
        }else
        {
            ret.set(0);
        }

        return ret;
    }

    /**
     * This method is used by the method "getRandomOutfit()" to convert the array written in the file string.xml<br>
     *     into an array java object.
     * @return the vector of string that contains all the items of the resource array
     */
    public static String[] getCategoriesFromResString(Context context)
    {
        ArrayAdapter arr_categories = ArrayAdapter.createFromResource(context, R.array.arr_categoria, android.R.layout.simple_spinner_dropdown_item);
        String[] ret = new String[arr_categories.getCount()-1];

        for(int i = 1; i<arr_categories.getCount(); i++)     //I shift the cycle of one unit because I don't want to include the first empty item of the array
        {
            ret[i-1]=arr_categories.getItem(i).toString();
        }

        return ret;
    }

}