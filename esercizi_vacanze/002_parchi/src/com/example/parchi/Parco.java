package com.example.parchi;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;

/**
 * Created by Anna on 20/07/2018.
 */
public class Parco
{
    private float longitude = 0;
    private float latitude = 0;
    private String descriprion = null;

    public Parco(float latitude, float longitude, String descriprion)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.descriprion = descriprion;
    }

    public Parco(EditText txte_longitude, EditText txte_latitude, EditText txte_description)
    {
        this.longitude = Float.parseFloat(txte_longitude.getText().toString());
        this.latitude = Float.parseFloat(txte_latitude.getText().toString());
        this.descriprion = txte_description.getText().toString();
    }

    public static Parco parseParco(Context context, Bundle bundle)
    {
        float latitude = bundle.getFloat(context.getString(R.string.key_parkLatitude));
        float longitude = bundle.getFloat(context.getString(R.string.key_parkLongitude));
        String descriprion = bundle.getString(context.getString(R.string.key_parkDescription));

        return new Parco(latitude, longitude, descriprion);
    }

    public Bundle toBundle(String key_longitude, String key_latitude, String key_description)
    {
        Bundle ret = new Bundle();
        ret.putFloat(key_longitude, this.longitude);
        ret.putFloat(key_latitude, this.latitude);
        ret.putString(key_description, this.descriprion);

        return ret;
    }

    public float getLongitude()
    {
        return this.longitude;
    }

    public float getLatitude()
    {
        return this.latitude;
    }

    public String getDescriprion() {
        return descriprion;
    }
}
