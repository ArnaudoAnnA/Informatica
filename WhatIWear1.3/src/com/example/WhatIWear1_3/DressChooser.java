package com.example.WhatIWear1_3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

/**
 * Created by Anna on 03/07/2018.
 *
 * This Activity manage a layout that allow to the user to choose what type of outfit he wants.
 * This step is very important because of I can make a subset of all of the dresses where dress can be searched.
 */
public class DressChooser extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dresschooser);

        //VIEWS' HANDLERS
        Spinner spin_style = (Spinner) findViewById(R.id.spin_style);       /*both the spinner are used also in the AggiungiAbito activity*/
        Spinner spin_climate = (Spinner) findViewById(R.id.spin_climate);
        CheckBox chk_borsa = (CheckBox) findViewById(R.id.chk_borsa);
        Spinner spin_tipoBorsa = (Spinner) findViewById(R.id.spin_tipoBorsa);
        CheckBox chk_giacca = (CheckBox) findViewById(R.id.chk_giacca);
        CheckBox chk_cappello = (CheckBox) findViewById(R.id.chk_cappello);
        Button btt_cerca = (Button) findViewById(R.id.btt_cerca);
        CheckBox chk_onlyNewClothes = (CheckBox) findViewById(R.id.chk_newClothes);
        CheckBox chk_onlyRecentlyUsed = (CheckBox) findViewById(R.id.chk_recentlyUsed);

        //setting the array adapter of spinners
        spin_climate.setAdapter(ArrayAdapter.createFromResource(getApplicationContext(), R.array.arr_clima, android.R.layout.simple_spinner_dropdown_item));
        spin_tipoBorsa.setAdapter( ArrayAdapter.createFromResource(getApplicationContext(), R.array.arr_bags, android.R.layout.simple_spinner_dropdown_item));
        spin_style.setAdapter(ArrayAdapter.createFromResource(getApplicationContext(), R.array.arr_stile, android.R.layout.simple_spinner_dropdown_item));

        //LISTENERS
        chk_borsa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(chk_borsa.isChecked())
                {
                    spin_tipoBorsa.setEnabled(true);
                }else
                {
                    spin_tipoBorsa.setEnabled(false);
                }
            }
        });

        btt_cerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* I pass to the activity all the feature that will be used to make dress subset in which I will search the outfit.
                    the bundle's structure is:
                        key_searchedStyle       item selected from spinner
                        key_searchedClimate     item selected from spinner
                        key_thereIsBag          if the chekbox is checked it contains the item selected in spinner spin_tipoBorsa
                                                if the chekbox isn't checked it contains a false boolean value
                        key_thereIsJacket       a boolean value
                        key_thereIsBag          a boolean value
                 */
                Intent intent_showDress = new Intent(getApplicationContext(), ShowDress.class);
                intent_showDress.putExtra(getString(R.string.key_searchedStyle), spin_style.getSelectedItem().toString());
                intent_showDress.putExtra(getString(R.string.key_searchedClimate), spin_climate.getSelectedItem().toString());
                if(chk_borsa.isChecked())
                {
                    intent_showDress.putExtra(getString(R.string.key_thereIsBag), spin_tipoBorsa.getSelectedItem().toString());
                }else
                {
                    intent_showDress.putExtra(getString(R.string.key_thereIsBag), chk_borsa.isChecked());
                }
                intent_showDress.putExtra(getString(R.string.key_thereIsJacket), chk_giacca.isChecked());
                intent_showDress.putExtra(getString(R.string.key_thereIsCap), chk_cappello.isChecked());
                intent_showDress.putExtra(getString(R.string.key_onlyNewClothes), chk_onlyNewClothes.isChecked());
                intent_showDress.putExtra(getString(R.string.key_onlyRecentlyUsed), chk_onlyRecentlyUsed.isChecked());

                startActivity(intent_showDress);
            }
        });

    }
}