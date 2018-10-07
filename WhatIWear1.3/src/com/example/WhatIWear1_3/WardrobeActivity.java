package com.example.WhatIWear1_3;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.IOException;
import java.util.Vector;

/**
 * Created by ANNA on 07/09/2018.
 */
public class WardrobeActivity extends Activity
{
    Vector<Dress> dresses = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wardrobe);

        //views' handlers
        TableLayout layout = (TableLayout) findViewById(R.id.layout_content);

        //I charge the vector of dresses
        try
        {
            dresses = DressListFile.readAllFile(getApplicationContext());

            //I show a number of dresses for each row that is defined in the file string.xml
            int nCols = Integer.parseInt(getString(R.string.dresses_each_row_wardrobe));
            int nRow = dresses.size()/nCols+1;

            int Ndress = 0; //this is the counter of dresses that have been added

            for(int r= 0; r<nRow; r++)
            {
                TableRow newTableRow = new TableRow(getApplicationContext());

                for(int c= 0; c<nCols && Ndress<dresses.size(); c++)
                {
                    //I create a new image which have small dimension to be the image of the new image button
                    ImageButton img_btt = new ImageButton(getApplicationContext());
                    Bitmap image = BitmapFactory.decodeFile(dresses.get(c).getImage().getPath());
                    img_btt = new ImageButton(getApplicationContext());
                    img_btt.setImageBitmap(Bitmap.createScaledBitmap(image, 250, 250, false));
                    img_btt.setOnClickListener(new ImageButtonListener(Ndress));

                    //I add the imageButton to the tableRow
                    newTableRow.addView(img_btt);
                    Ndress++;
                }

                layout.addView(newTableRow);
            }



        }catch (DressNotFoundExeption dnfe)
        {
            TextView txt_message = new TextView(getApplicationContext());
            txt_message.setText("No dresses found");
            layout.addView(txt_message);
        }catch (IOException ioe)
        {
            Toast.makeText(getApplicationContext(), "There was an error in reading internal files", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    /**
     * This is the listener of all imageButtons, it define that when I click on an ImageButton,
     * the modifyDressActivity is started
     */
    private class ImageButtonListener implements View.OnClickListener
    {
        private int number = 0;

        ImageButtonListener(int number)
        {
            this.number = number;
        }

        @Override
        public void onClick(View v)
        {
            Intent intent_modify_dress = new Intent(getApplicationContext(), modifyDressActivity.class);

            Object dress = (Object) dresses.get(number);

            intent_modify_dress.putExtra(getString(R.string.key_selectedDress), dress.toString());
            startActivity(intent_modify_dress);
        }
    }

}