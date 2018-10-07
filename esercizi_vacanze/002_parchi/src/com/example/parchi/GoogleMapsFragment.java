package com.example.parchi;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewFragment;

/**
 * Created by Anna on 23/07/2018.
 */
public class GoogleMapsFragment
{
    public WebView googleMapsView = null;

    public GoogleMapsFragment(WebView webView)
    {
        googleMapsView = webView;
        googleMapsView.loadUrl("https://www.google.com/maps");
        WebSettings webSettings = googleMapsView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    public void onParkChanged(Parco park)
    {
        String url = "https://www.google.com/maps/place/"+ park.getDescriprion() + "/@"+park.getLongitude()+","+park.getLatitude()+"z/data=!4m5!3m4!1s0x48758f8cce12ee43:0x61340efc81d0fab!8m2!3d"+park.getLongitude()+"!4d"+park.getLatitude();
        googleMapsView.loadUrl(url);
    }
}
