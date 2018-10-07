package com.example.parchi;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewFragment;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Vector;

public class Main extends Activity {
    Spinner spin_select = null;
    Button btt_add = null;
    GoogleMapsFragment frag_googleMaps = null;
    /**
     * Called when the activity is first created.
     */

    Vector<Parco> parks = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        parks = new Vector<Parco>();

        //views' handlers
        spin_select = (Spinner) findViewById(R.id.spin_select);
        btt_add = (Button) findViewById(R.id.btt_add);
        frag_googleMaps = new GoogleMapsFragment((WebView) findViewById(R.id.frag_googleMaps));

        ///setting the array adapter of the spinner
        ArrayAdapter arrAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item);
        spin_select.setAdapter(arrAdapter);

        //listeners
        btt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent_addPark = new Intent(getApplicationContext(), AddParkActivity.class);
                startActivityForResult(intent_addPark, 0);
            }
        });

        spin_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Parco selectedPark = parks.get(spin_select.getSelectedItemPosition());
                frag_googleMaps.onParkChanged(selectedPark);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==0)
        {
            if(resultCode == RESULT_OK)
            {
                Parco p =Parco.parseParco(getApplicationContext(), data.getBundleExtra(getString(R.string.key_newPark)));
                parks.add(p);

                //I add the new park to the spinner
                ArrayAdapter arrayAdapter = (ArrayAdapter) spin_select.getAdapter();
                arrayAdapter.add(p.getDescriprion());
                spin_select.setAdapter(arrayAdapter);
            }
        }
    }

}

