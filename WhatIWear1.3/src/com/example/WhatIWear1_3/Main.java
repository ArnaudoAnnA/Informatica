package com.example.WhatIWear1_3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.WhatIWear1_3.R;

public class Main extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //views' handlers
        Button btt_aggiungi = (Button) findViewById(R.id.btt_aggiungi);
        Button btt_scegli = (Button) findViewById(R.id.btt_scegli);
        Button btt_guardaroba = (Button) findViewById(R.id.btt_guardaroba);

        btt_aggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_aggiungi = new Intent(getApplicationContext(), AggiungiAbito.class);
                startActivity(intent_aggiungi);
            }
        });

        btt_scegli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_scegli = new Intent(getApplicationContext(), DressChooser.class);
                startActivity(intent_scegli);
            }
        });

        btt_guardaroba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_guardaroba = new Intent(getApplicationContext(), WardrobeActivity.class);
                startActivity(intent_guardaroba);
            }
        });
    }
}
