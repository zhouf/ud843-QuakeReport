package com.example.android.quakereport;

import android.content.Context;
import android.util.Log;

import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2017-6-24.
 */

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<EarthQuake>> {

    public static final String TAG = EarthquakeLoader.class.getName();

    private String url;

    public EarthquakeLoader(Context context, String url) {
        super(context);
        this.url = url;
        Log.i(TAG, "EarthquakeLoader: ");
    }

    @Override
    public ArrayList<EarthQuake> loadInBackground() {
        Log.i(TAG, "loadInBackground: ");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return QueryUtils.extractEarthquakes(url);
//        return null;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
