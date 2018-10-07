package com.example.WhatIWear1_3;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Created by Anna on 31/05/2018.
 */
public class AggiungiAbito extends Activity {

    private ImageButton imgbtt_fotoAbito;
    private Spinner spin_categoria1;
    public Bitmap selectedImageBitmap;
    private final int SELECTPHOTO_CODE = 123;
    private final int CROPPHOTO_CODE = 124;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_aggiungi_abito);

        //views' handlers
        ListView listv_style = (ListView) findViewById(R.id.listv_style);
        spin_categoria1 = (Spinner) findViewById(R.id.spin_categoria1);
        Spinner spin_categoria2 = (Spinner) findViewById(R.id.spin_categoria2);
        ListView listv_climate = (ListView) findViewById(R.id.listv_climate);
        Spinner spin_gradimento = (Spinner) findViewById(R.id.spin_gradimento);
        imgbtt_fotoAbito = (ImageButton) findViewById(R.id.imgbtt_fotoAbito);
        ImageButton imgbtt_explore = (ImageButton) findViewById(R.id.imgbtt_explore);
        Button btt_add = (Button) findViewById(R.id.btt_aggiungi);
        Button btt_back = (Button) findViewById(R.id.btt_back);

        initializeDressInputViews(imgbtt_fotoAbito, imgbtt_explore, listv_style, spin_categoria1, spin_categoria2, listv_climate, spin_gradimento);

        btt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                /* I CONTROL THAT ALL VIEWS ARE FILLED IN*/
                if(!emptyFields(selectedImageBitmap, listv_style, listv_climate))
                {
                    //IF ALL IT'S Ok I OPEN THE DATABASE FILE AND SAVE THE DRESS INTO THEM
                    try
                    {
                    /*the next method creates a new dress object adn adds them into the file*/
                        DressListFile.add(getApplicationContext(), selectedImageBitmap, listv_style, spin_categoria1, spin_categoria2, spin_gradimento, listv_climate);
                        Toast.makeText(getApplicationContext(), "you have added a new dress", Toast.LENGTH_SHORT).show();
                    }catch (IOException ioe)
                    {
                        Toast.makeText(getApplicationContext(), "There was an error in saving the dress", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * this method, given the handlers to all the views of the layout_aggiungi_abito, set all the arrayAdapter of spinner and listview<br>
     *     and all the listeners of the views that are use to take in input dress' features
     * @param imgbtt_fotoAbito
     * @param imgbtt_explore
     * @param listv_style
     * @param spin_categoria1
     * @param spin_categoria2
     * @param listv_climate
     * @param spin_gradimento
     */
    public void initializeDressInputViews(ImageButton imgbtt_fotoAbito, ImageButton imgbtt_explore, ListView listv_style, Spinner spin_categoria1, Spinner spin_categoria2, ListView listv_climate, Spinner spin_gradimento)
    {
        listv_style.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listv_style.setItemsCanFocus(false);
        listv_climate.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listv_climate.setItemsCanFocus(false);

        //setting the spinners' items
        ArrayAdapter arr_stile = ArrayAdapter.createFromResource(getApplicationContext(), R.array.arr_stile, android.R.layout.simple_list_item_multiple_choice);
        listv_style.setAdapter(arr_stile);
        ArrayAdapter arr_categoria = ArrayAdapter.createFromResource(getApplicationContext(), R.array.arr_categoria, android.R.layout.simple_spinner_dropdown_item);
        spin_categoria1.setAdapter(arr_categoria);
        ArrayAdapter arr_clima = ArrayAdapter.createFromResource(getApplicationContext(), R.array.arr_clima, android.R.layout.simple_list_item_multiple_choice);
        listv_climate.setAdapter(arr_clima);
        /**mettere delle stelle al posto degli asterischi (creare un array di immagini)**/
        ArrayAdapter arr_gradimento = ArrayAdapter.createFromResource(getApplicationContext(), R.array.arr_gradimento, android.R.layout.simple_spinner_dropdown_item);
        spin_gradimento.setAdapter(arr_gradimento);

        final ArrayAdapter ARR_NULL = ArrayAdapter.createFromResource(getApplicationContext(), R.array.arr_vuoto, android.R.layout.simple_spinner_dropdown_item);

        //spinner_categoria2 is disabled
        spin_categoria2.setEnabled(false);
        spin_categoria2.setAdapter(ARR_NULL);


        //LISTENERS
        imgbtt_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(spin_categoria1.getSelectedItemPosition()==0)
                {
                    Toast.makeText(getApplicationContext(), "select a category before choose an image", Toast.LENGTH_SHORT).show();
                }else
                {
                    //When the  user click on the image button, I show an open file dialog to choose the image of the dress
                    //The path of the image selected by the user will bi saved in the onActivityResult method
                    Intent intent_openFileDialog = new Intent(getApplicationContext(), OpenFileDialog.class);
                    setDimensionToImageViewDueCategory();
                    startActivityForResult(intent_openFileDialog, SELECTPHOTO_CODE);
                }

            }
        });


        imgbtt_fotoAbito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spin_categoria1.getSelectedItemPosition()==0)
                {
                    Toast.makeText(getApplicationContext(), "select a category before choose an image", Toast.LENGTH_SHORT).show();
                }else
                {
                    try
                    {
                        /*When the user click on the image button, I start an Activity that allow to take a picture*/
                        Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        setDimensionToImageViewDueCategory();
                        startActivityForResult(intent_camera, SELECTPHOTO_CODE);
                    }catch (ActivityNotFoundException anfe)
                    {
                        Toast.makeText(getApplicationContext(), "Can't start camera. You have to select the image using the file system (click on the directory icon)", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


        spin_categoria1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                switch (spin_categoria1.getSelectedItem().toString())      /* I control if the user has selected shirt or pants
                                                                                in this case I charge the second spinner and I charge it
                                                                                with different array due to the first spinner's selected item*/
                {
                    case "shirt":
                        ArrayAdapter arr_shirt = ArrayAdapter.createFromResource(getApplicationContext(), R.array.arr_shirt, android.R.layout.simple_spinner_dropdown_item);
                        spin_categoria2.setAdapter(arr_shirt);
                        spin_categoria2.setEnabled(true);
                        break;

                    case "trousers and body":
                        ArrayAdapter arr_pants = ArrayAdapter.createFromResource(getApplicationContext(), R.array.arr_pants, android.R.layout.simple_spinner_dropdown_item);
                        spin_categoria2.setAdapter(arr_pants);
                        spin_categoria2.setEnabled(true);
                        break;

                    case "bag":
                        ArrayAdapter arr_bag = ArrayAdapter.createFromResource(getApplicationContext(), R.array.arr_bags, android.R.layout.simple_spinner_dropdown_item);
                        spin_categoria2.setAdapter(arr_bag);
                        spin_categoria2.setEnabled(true);

                    default:
                        spin_categoria2.setEnabled(false);
                        spin_categoria2.setAdapter(null);
                        break;
                }

                imgbtt_fotoAbito.setImageBitmap(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SELECTPHOTO_CODE && resultCode==RESULT_OK)       /*I control if the request code is correspondent to open_file_dialog's activity
                                                                            or to camera activity*/
        {
            try {
                /*I start the activity to crop the image*/
                Intent intent_cropImage = new Intent("com.android.camera.action.CROP");
                intent_cropImage.setDataAndType(data.getData(), "image/*");
                intent_cropImage.putExtra("crop", "true");

                //getting the dimension of the imageButton on the top of the layout (I have to get an image that has the same dimension)
                int[] dimension = DressImageView.getViewDimensionDueCategory(spin_categoria1.getSelectedItem().toString());

                intent_cropImage.putExtra("aspectX", dimension[0]);
                intent_cropImage.putExtra("aspectY", dimension[1]);

                intent_cropImage.putExtra("outputX", dimension[0]);
                intent_cropImage.putExtra("outputY", dimension[1]);
                intent_cropImage.putExtra("return-data", true);
                startActivityForResult(intent_cropImage, CROPPHOTO_CODE);
            }catch (ActivityNotFoundException anfe)
            {
                Toast.makeText(getApplicationContext(), "Can't start crop operation. Your device doesn't support it", Toast.LENGTH_SHORT).show();

            }catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "The selected file is not valid", Toast.LENGTH_SHORT).show();
            }

        }else if(requestCode == CROPPHOTO_CODE && resultCode == RESULT_OK)
        {
            /*after the image is been cropped, I show it in the imageButton on the rop of the layout*/
            Bundle extras = data.getExtras();
            selectedImageBitmap = extras.getParcelable("data");
            imgbtt_fotoAbito.setImageBitmap(selectedImageBitmap);
        }
    }


    /**
     * This method set the dimension of the image button that will show the selected image. The dimension<br>
     *     given are the same as the dimension of the image view in the layout showDressLayout.xml that show dress from<br>
     *         the same category.
     */
    private void setDimensionToImageViewDueCategory()
    {
        int[] dimension = DressImageView.getViewDimensionDueCategory(spin_categoria1.getSelectedItem().toString());

        imgbtt_fotoAbito.setMaxWidth(dimension[0]);
        imgbtt_fotoAbito.setMaxHeight(dimension[1]);
        imgbtt_fotoAbito.setMinimumWidth(dimension[0]);
        imgbtt_fotoAbito.setMinimumHeight(dimension[1]);
    }

    /**
     * this function controls all the passed fields are not empty
     * @param selectedImageBitmap
     * @param listv_style
     * @param listv_climate
     * @return true if there is an empty field
     */
    public boolean emptyFields(Bitmap selectedImageBitmap, ListView listv_style, ListView listv_climate)
    {
        boolean ret = true;

        if(selectedImageBitmap == null)   //I control if the user hasn't selected any images
        {
            Toast.makeText(getApplicationContext(), "select an image", Toast.LENGTH_SHORT).show();
        }else if(listv_style.getCheckedItemCount()==0)
        {
            Toast.makeText(getApplicationContext(), "select a style for the dress", Toast.LENGTH_SHORT).show();
        }else if(listv_climate.getCheckedItemCount()==0)
        {
            Toast.makeText(getApplicationContext(), "select a climate for the dress", Toast.LENGTH_SHORT).show();
        }else
        {
            ret = false;
        }

        return ret;
    }
}