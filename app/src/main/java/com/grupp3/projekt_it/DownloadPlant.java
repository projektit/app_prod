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

    public DownloadPlant(Context context, ListView listView){
        this.context = context;
        this.listView = listView;
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
        if(result.equals("")){

            return;
        }
        Log.i(TAG, "results: " + result);
        Gson gson = new Gson();
        Plant [] plants = gson.fromJson(result, Plant[].class);

        Log.i(TAG, "so far");
        ArrayList<String> plantNames = new ArrayList<>();
        Log.i(TAG, "so far2");
        for (int i = 0; i < plants.length; i++) {
            plantNames.add(plants[i].getName());
        }
        Log.i(TAG, "so far3");
        String[] items = plantNames.toArray(new String[plantNames.size()]);
        Log.i(TAG, "so far4");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.list_item, items);
        Log.i(TAG, "so far5 " + items[0]);
        listView.setAdapter(adapter);
        Log.i(TAG, "so far6");
        /*
        //Listen for normal click on items in list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                String gardenName = textView.getText().toString();
                Intent intent = new Intent(context, MyGardenActivity.class);
                intent.putExtra("gardenName", gardenName);
                startActivity(intent);
            }
        });

        //Listener for long click on items in list
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                String gardenName = textView.getText().toString();

                FragmentManager fragmentManager = getFragmentManager();
                GardenListFragment quickOptions = new GardenListFragment();

                Bundle bundle = new Bundle();
                bundle.putString("gardenName", gardenName);

                quickOptions.setArguments(bundle);
                quickOptions.show(fragmentManager, "disIsTag");
                return true;
            }
        });
        */
    }
}
