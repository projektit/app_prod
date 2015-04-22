package com.grupp3.projekt_it;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
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

/**
 * Created by Daniel on 2015-04-15.
 */
public class DownloadForecast extends AsyncTask<String, Void, String> {
    String TAG = "com.grupp3.projekt_it";
    String fileName;
    Context context;
    Garden garden;

    public DownloadForecast(String fileName, Context context, Garden garden){
        this.fileName = fileName;
        this.context = context;
        this.garden = garden;
    }
    @Override
    protected String doInBackground(String... urls){
        String rawData = "";
        try {
            // get url
            URL url = new URL(urls[0]);
            // setup http connection
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            //add api key
            connection.addRequestProperty("x-api-key","92a437c886b5e874102c9d28c6c10359");
            // get input
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            // build string from input stream
            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();
            //check input satus code
            JSONObject data = new JSONObject(json.toString());
            rawData = json.toString();
            if(data.getInt("cod") != 200){
                Log.i(TAG, "cod not 200");
                return null;
            }
            // return json string
            return rawData;
        }catch(Exception e){
            Log.i(TAG, "Exception in  weather client");
            return null;
        }
    }
    protected void onPostExecute(String result){
        //convert from json to java object for both weather and garden
        Gson gson = new Gson();
        //convert forecast json to java object
        Forecast forecast = gson.fromJson(result, Forecast.class);
        //add forecast to garden object
        Log.i(TAG, " saving new forecast, name, time: " + Long.toString(forecast.getDt()) + ", " + forecast.name);
        garden.setForecast(forecast);
        //convert garden back to json
        GardenUtil gardenUtil = new GardenUtil();
        gardenUtil.saveGarden(garden, context);
    }
}
