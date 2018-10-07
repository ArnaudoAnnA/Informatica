package com.example.WhatIWear1_3;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.io.IOException;
import java.util.ArrayList;

public class modifyDressActivity extends AggiungiAbito { //NOTE THAT I'M INHERITING FROM THE CLASS AggiungiAbito
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_aggiungi_abito);

        //views' handlers
        ListView listv_style = (ListView) findViewById(R.id.listv_style);
        Spinner spin_categoria1 = (Spinner) findViewById(R.id.spin_categoria1);
        Spinner spin_categoria2 = (Spinner) findViewById(R.id.spin_categoria2);
        ListView listv_climate = (ListView) findViewById(R.id.listv_climate);
        Spinner spin_gradimento = (Spinner) findViewById(R.id.spin_gradimento);
        ImageButton imgbtt_fotoAbito = (ImageButton) findViewById(R.id.imgbtt_fotoAbito);
        ImageButton imgbtt_explore = (ImageButton) findViewById(R.id.imgbtt_explore);
        Button btt_add = (Button) findViewById(R.id.btt_aggiungi);
        Button btt_back = (Button) findViewById(R.id.btt_back);

        //getting the selectedDresses
        String dressString = getIntent().getStringExtra(getString(R.string.key_selectedDress));
        Dress selectedDress = Dress.stringToDress(dressString);

        initializeDressInputViews(imgbtt_fotoAbito, imgbtt_explore, listv_style, spin_categoria1, spin_categoria2, listv_climate, spin_gradimento);

        //charging the dress values on the views
        checkListItemsFromString(listv_style, selectedDress.getStyle());
        checkSpinnerItemFromString(spin_categoria1, selectedDress.getcategory1());
        checkSpinnerItemFromString(spin_categoria2, selectedDress.getCategory2());
        checkListItemsFromString(listv_climate, selectedDress.getClimate());
        spin_gradimento.setSelection(selectedDress.getLiking());
        imgbtt_fotoAbito.setImageURI(Uri.fromFile(selectedDress.getImage()));

        //listeners
        btt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!emptyFields(selectedImageBitmap, listv_style, listv_climate))
                {
                    try
                    {
                        Dress modifiedDress = new Dress(selectedImageBitmap, listv_style, spin_categoria1, spin_categoria2, spin_gradimento, listv_climate);

                        /*------------------------------------------------------------------------------------------
                            IN THIS SECTION OF THE CODE I WILL UPDATE THE ROW OF THE DB IN WHICH THE DRESS IS SAVED
                            ---------------------------------------------------------------------------------------*/

                    }catch (IOException ioe)
                    {
                        Toast.makeText(getApplicationContext(), "Impossibile salvare le modifiche", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void checkListItemsFromString(ListView listView, String string)
    {
        ListAdapter listAdapter = listView.getAdapter();

        for(int i = 0; i<listAdapter.getCount(); i++)   //I cycle all the items of the listview
        {
            String item = listAdapter.getItem(i).toString(); //getting the string of the item
            if(string.contains(item))  //if the passed string contains this item, I chek them
            {
                listView.setItemChecked(i, true);
            }
        }
    }


    private void checkSpinnerItemFromString(Spinner spinner, String string)
    {
        //fisrt, I manage the case of spin_catgory2 isn't enabled
        if(spinner.isEnabled())
        {
            SpinnerAdapter spinnerAdapter = spinner.getAdapter();

            String item = spinnerAdapter.getItem(0).toString();
            int i;

            for(i = 1; i<spinnerAdapter.getCount() && !string.contains(item); i++)
            {
                item = spinnerAdapter.getItem(i).toString();
            }

            spinner.setSelection(i);
        }

    }


}