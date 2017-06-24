package com.example.android.quakereport;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2017-6-24.
 */

public class EarthquakeLoader extends android.support.v4.content.AsyncTaskLoader<ArrayList<EarthQuake>> {

    public static final String TAG = EarthquakeLoader.class.getName();

    public EarthquakeLoader(Context context) {
        super(context);
        Log.i(TAG, "EarthquakeLoader: ");
    }

    @Override
    public ArrayList<EarthQuake> loadInBackground() {
        Log.i(TAG, "loadInBackground: ");
        return QueryUtils.extractEarthquakes();
    }
}
