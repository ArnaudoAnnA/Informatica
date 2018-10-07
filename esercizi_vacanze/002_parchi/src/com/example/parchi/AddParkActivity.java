package com.example.parchi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Anna on 20/07/2018.
 */
public class AddParkActivity extends Activity {
    private Location currentLocation = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addparklayout);

        //views' handlers
        EditText txte_description = (EditText)findViewById(R.id.txte_description);
        EditText txte_longitude = (EditText) findViewById(R.id.txte_longitude);
        EditText txte_latitude = (EditText) findViewById(R.id.txte_latitude);
        Button btt_posizioneCorrente = (Button) findViewById(R.id.btt_getCurrentPosition);
        Button btt_save = (Button) findViewById(R.id.btt_save);

        btt_posizioneCorrente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                LocationListener listener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        currentLocation = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        String coordinateFormat = getString(R.string.coordinateFormat);
                        txte_latitude.setText(String.format(coordinateFormat,currentLocation.getLatitude()));
                        txte_longitude.setText(String.format(coordinateFormat, currentLocation.getLongitude()));
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {}

                    @Override
                    public void onProviderEnabled(String provider) {}

                    @Override
                    public void onProviderDisabled(String provider) {}
                };

                locMan.requestSingleUpdate(locMan.GPS_PROVIDER, listener, null);
                currentLocation = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                String coordinateFormat = getString(R.string.coordinateFormat);
                txte_latitude.setText(String.format(coordinateFormat,currentLocation.getLatitude()));
                txte_longitude.setText(String.format(coordinateFormat, currentLocation.getLongitude()));
            }
        });

        btt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(txte_longitude.getText().toString()=="" || txte_latitude.getText().toString()=="" || txte_description.getText().toString()=="")
                {
                    Toast.makeText(getApplicationContext(), "completare tutti i campi", Toast.LENGTH_SHORT).show();
                }else
                {
                    Intent intent_park = new Intent();
                    Parco newPark = new Parco(txte_latitude, txte_longitude, txte_description);
                    Bundle bundlePark = newPark.toBundle(getString(R.string.key_parkLongitude), getString(R.string.key_parkLatitude), getString(R.string.key_parkDescription));

                    intent_park.putExtra(getString(R.string.key_newPark), bundlePark);

                    setResult(RESULT_OK, intent_park);
                    finish();
                }

            }
        });

    }
}