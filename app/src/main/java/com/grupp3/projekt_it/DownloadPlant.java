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
 * Created by Daniel on 2015-04-21.
 */
public class DownloadPlant extends AsyncTask<String, Void, String> {
    String TAG = "com.grupp3.projekt_it";
    Context context;
    TextView textView;

    public DownloadPlant(Context context, TextView textView){
        this.context = context;
        this.textView = textView;
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
            String tmp="";
            while((tmp=reader.readLine())!=null) {
                Log.i(TAG, reader.readLine());
                json.append(tmp).append("\n");
            }
            reader.close();
            return rawData;
        }catch(Exception e){
            Log.i(TAG, "Exception in  plant client");
            return null;
        }
    }
    protected void onPostExecute(String result) {
        Log.i(TAG, "printing result in DownloadPlant");
        textView.setText(result);
    }
}
