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

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<EarthQuake>>{


    public static final String TAG = EarthquakeActivity.class.getName();
    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    ListView earthquakeListView;
    EarthQuakeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Create a fake list of earthquake locations.
        ArrayList<EarthQuake> earthquakes = new ArrayList<EarthQuake>();

        // Find a reference to the {@link ListView} in the layout
//        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        earthquakeListView = (ListView) findViewById(R.id.list);

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
        getSupportLoaderManager().initLoader(0,null,this).forceLoad();

    }

    @Override
    public Loader<ArrayList<EarthQuake>> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "onCreateLoader: ");
        return new EarthquakeLoader(EarthquakeActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<EarthQuake>> loader, ArrayList<EarthQuake> data) {
        Log.i(TAG, "onLoadFinished: ");
        adapter.addAll(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<EarthQuake>> loader) {
        adapter.addAll(new ArrayList<EarthQuake>());
    }


    @Deprecated
    private class EarthquakeAsyncTask extends AsyncTask<String, Void, ArrayList<EarthQuake>> {

        @Override
        protected ArrayList<EarthQuake> doInBackground(String... params) {
            return QueryUtils.extractEarthquakes();
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
