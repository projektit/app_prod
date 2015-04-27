package com.grupp3.projekt_it;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Daniel on 2015-04-21.
 */
public class DownloadPlant extends AsyncTask<String, Void, String> {
    String TAG = "com.grupp3.projekt_it";
    Context context;
    ListView listView;
    FragmentManager fragmentManager;

    public DownloadPlant(Context context, ListView listView, FragmentManager fragmentManager){
        this.context = context;
        this.listView = listView;
        this.fragmentManager = fragmentManager;
    }
    @Override
    protected String doInBackground(String... urls){
        String rawData = "";
        try {
            // get url
            URL url = new URL(urls[0]);
            // setup http connection
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            // get input
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            // build string from input stream
            StringBuffer json = new StringBuffer(1024);
            String result = reader.readLine();
            reader.close();
            return result;
        }catch(Exception e){
            Log.i(TAG, "Exception in  plant client");
            return null;
        }
    }
    protected void onPostExecute(String result) {
        final Context context1 = context;
        if(result.equals("")){
            Log.i(TAG, "No search match");
            return;
        }
        if(result == null){
            return;
        }
        Gson gson = new Gson();
        final Plant [] plants = gson.fromJson(result, Plant[].class);

        final ArrayList<String> plantNames = new ArrayList<>();
        for (int i = 0; i < plants.length; i++) {
            plantNames.add(plants[i].getName());
        }
        String[] items = plantNames.toArray(new String[plantNames.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.list_item, items);
        listView.setAdapter(adapter);

        //Listen for normal click on items in list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                String plantName = plantNames.get(position);
                /*
                TextView textView = (TextView) view;
                String gardenName = textView.getText().toString();
                Intent intent = new Intent(context, MyGardenActivity.class);
                intent.putExtra("gardenName", gardenName);
                startActivity(intent);
                */
            }
        });

        //Listener for long click on items in list
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                Plant plant = plants[position];
                Gson gson = new Gson();
                String json = gson.toJson(plant);
                Bundle bundle = new Bundle();
                bundle.putString("jsonPlant", json);

                PlantSearchFragment quickOptions = new PlantSearchFragment();
                quickOptions.setArguments(bundle);
                quickOptions.show(fragmentManager, "disIsTag3");
                return true;
            }
        });
    }
}
