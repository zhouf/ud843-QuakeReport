/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<EarthQuake>>{

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    public static final String TAG = EarthquakeActivity.class.getName();
    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final int EARTHQUAKE_LOADER_ID = 1;

    ListView earthquakeListView;
    EarthQuakeAdapter adapter;
    TextView emptyView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Create a fake list of earthquake locations.
        ArrayList<EarthQuake> earthquakes = new ArrayList<EarthQuake>();

        // Find a reference to the {@link ListView} in the layout
//        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        earthquakeListView = findViewById(R.id.list);
        progressBar = findViewById(R.id.progressBar);

        emptyView = findViewById(R.id.empty_view);
        emptyView.setVisibility(View.GONE);
        earthquakeListView.setEmptyView(emptyView);


        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        Log.i(TAG, "onCreate: isConnected=>" + isConnected);
        if(isConnected){

            // Create a new {@link ArrayAdapter} of earthquakes
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, earthquakes);

//        EarthQuakeAdapter adapter = new EarthQuakeAdapter(this,earthquakes);
            adapter = new EarthQuakeAdapter(this,earthquakes);
            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            earthquakeListView.setAdapter(adapter);
            earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    EarthQuake earthQuake = (EarthQuake) parent.getItemAtPosition(position);

                    Intent webIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(earthQuake.getUrl()));
                    startActivity(webIntent);
                }
            });
            //EarthquakeAsyncTask task = new EarthquakeAsyncTask();
            //task.execute();
            getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID,null,this);
        }else{
            progressBar.setVisibility(View.GONE);
            emptyView.setText(R.string.not_connected);
            emptyView.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<ArrayList<EarthQuake>> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "onCreateLoader: " + id);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(EarthquakeActivity.this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<EarthQuake>> loader, ArrayList<EarthQuake> data) {
        Log.i(TAG, "onLoadFinished: ");
        progressBar.setVisibility(View.GONE);
        if(data!=null && !data.isEmpty()){
            adapter.clear();
            adapter.addAll(data);
        }else{
            emptyView.setText(R.string.empty_tip);
            emptyView.setVisibility(View.VISIBLE);
        }
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<EarthQuake>> loader) {
        adapter.addAll(new ArrayList<EarthQuake>());
    }


    @Deprecated
    private class EarthquakeAsyncTask extends AsyncTask<String, Void, ArrayList<EarthQuake>> {

        @Override
        protected ArrayList<EarthQuake> doInBackground(String... params) {
            return QueryUtils.extractEarthquakes("");
        }

        @Override
        protected void onPostExecute(ArrayList<EarthQuake> earthQuakes) {
            adapter.addAll(earthQuakes);
//            adapter = new EarthQuakeAdapter(EarthquakeActivity.this,earthQuakes);
            adapter.notifyDataSetChanged();

//            earthquakeListView.setAdapter(adapter);
        }
    }

}
