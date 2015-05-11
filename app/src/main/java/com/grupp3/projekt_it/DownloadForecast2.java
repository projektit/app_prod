package com.grupp3.projekt_it;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Daniel on 2015-05-11.
 */
public class DownloadForecast2 extends AsyncTask<String, Void, String> {
    String TAG = "com.grupp3.projekt_it";
    String fileName;
    Context context;
    Garden garden;

    public DownloadForecast2(String fileName, Context context, Garden garden){
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
        if(result == null){
            return;
        }
        //convert from json to java object for both weather and garden
        Gson gson = new Gson();
        //convert forecast json to java object
        Forecast2 forecast2 = gson.fromJson(result, Forecast2.class);
        //add forecast to garden object
        garden.setForecast2(forecast2);
        //convert garden back to json
        GardenUtil gardenUtil = new GardenUtil();
        gardenUtil.saveGarden(garden, context);
    }
}
