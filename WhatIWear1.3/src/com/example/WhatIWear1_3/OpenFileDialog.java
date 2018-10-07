package com.example.WhatIWear1_3;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import org.w3c.dom.Text;

import java.io.File;

/**
 * Created by Anna on 01/06/2018.
 */
public class OpenFileDialog extends Activity {

    //PROPERTY
    File currentPath = null;
    ScrollView scrollContainer = null;
    ClickOnDirListener listener = null;

    //METHODS
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_openfiledialog);
        listener = new ClickOnDirListener();

        scrollContainer = (ScrollView) findViewById(R.id.scrl_container);

        //views' handlers
        TextView txt_internal = (TextView) findViewById(R.id.txt_internal_storage);
        TextView txt_external = (TextView) findViewById(R.id.txt_external_storage);

        //LISTENERS
        txt_external.setOnClickListener(listener);
        txt_internal.setOnClickListener(listener);
    }

    //INNER CLASS
    private class ClickOnDirListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            TextView txt_onClick = (TextView) v;
            String dirName = txt_onClick.getText().toString();

            if(this.changeDirectory(dirName)!=null) //i control if the operation of changing directory has returned a pointer to any files or null
            {
                Intent intent_data = new Intent();              /*if the previous method has returned a File pointer, I save it in a intent that I will return
                                                                  to the call activity, then I finish the activity*/
                intent_data.setData(Uri.fromFile(currentPath));
                setResult(RESULT_OK, intent_data);
                finish();
            }

        }


        /**
         * <b>changeDirectory</b><br>
         *     This method add the directory name passed as parameter to the path that points to the current position in the <br>
         *     file system, if the directory name is ".." this method remove the last component to the path that points the<br>
         *     current position in the file system. <br><br>
         *     This method calls <b>showDirectoryContent()</b>
         *
         * @param dirName the name of the selected directory
         * @return null if the directory has been opened, a File pointer if dirName was the name of a file (not a directory)
         */
        private File changeDirectory(String dirName)
        {
            File ret = null;

            if(dirName.equals(getString(R.string.txt_internal_storage)))    /*if the current position in the file manager is the root directory
                                                                                I control if external storage or internal has been clicked
                                                                                in positive case I change the position of currentFile pointer*/
            {
                currentPath = getFilesDir();
            }else if(dirName.equals(getString(R.string.txt_external_storage)))
            {
                currentPath = Environment.getExternalStorageDirectory();
            }else if(dirName.equals(".."))                                  /* in negative case I control if the previous directory has been clicked,
                                                                                if the result of the if clause is true, I get the path of the previous
                                                                                directory and I move the pointer*/
            {
                String path = currentPath.getPath();
                path = path.substring(0, path.lastIndexOf("/"));
                currentPath = new File(path);
            }else                                                           /* in negative case I move the pointer into the directory that has been click*/
            {
                currentPath = new File(currentPath.getPath()+"/"+dirName);
            }


            File[] directoryContent;
            directoryContent = currentPath.listFiles();     //I get the list of files contained in this directory

            if(directoryContent!=null)          //if the list of file is null, it means that this is a file instead of a directory
            {
                this.showDirectoryContent(directoryContent);
            }else /* if the result of the method listFiles() is null, it means that the selected textView is a file*/
            {
                /* in this case I set the result of this activity as the path of the selected file*/
                ret = currentPath;
            }

            return ret;
        }

        /**
         * <b>showDirectoryContent()</b><br>
         *      This method add to a Table layout as rows as the number of elements contained by the directory passed as parameter.<br>
         *      For each component this method adds an icon (a directory icon or a file icon due to the component) and a TextView <br>
         *      that contains the name of the directory. This method sets the onClickListener to each TextView as this onClickListener.
         * @param directoryContent the array that contains all the File pointr of the directory
         */
        private void showDirectoryContent(File[] directoryContent)
        {
            TableLayout currentLayout = (TableLayout) findViewById(R.id.layout_openFileDialog);
            currentLayout.removeAllViews();                                 /*I clear the layout and I get the list of files in the new directory*/

            //I add the icon and the text to give the possibility to the user to go back to the previous folder
            ImageView image = new ImageView(getApplicationContext());
            image.setImageDrawable(getDrawable(R.drawable.icon_directory));
            TextView text = new TextView(getApplicationContext());
            text.setText("..");
            text.setOnClickListener(listener);
            text.setTextSize((float)20);

            this.addElementToLayout(currentLayout, image, text);

            for(File file:directoryContent)     /*I cycle all the files in the directory*/
            {
                image = new ImageView(getApplicationContext());
                text = new TextView(getApplicationContext());
                text.setText(file.getName());
                text.setPadding(0, 40, 10, 10);
                text.setOnClickListener(listener);      //I set this object as onCLickListener (ricorsive)
                //I control if the pointed file is a file or a directory and I set a different image due to the type of file
                if(file.isDirectory())
                {
                    image.setImageDrawable(getDrawable(R.drawable.icon_directory));
                }else
                {
                    image.setImageDrawable(getDrawable(R.drawable.icon_file));
                }
                //then I add it to the layout
                this.addElementToLayout(currentLayout, image, text);
            }
        }


        private void addElementToLayout(TableLayout layout, ImageView image, TextView text)
        {
            //I add the views to a tableRow and I add the tableRow to the layout
            TableRow tableRow = new TableRow(getApplicationContext());
            tableRow.addView(image);
            tableRow.addView(text);
            layout.addView(tableRow);
        }
    }


}