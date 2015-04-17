package com.grupp3.projekt_it;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Daniel on 2015-04-17.
 */
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class GardenService extends IntentService {
    String TAG = "com.grupp3.testbackground";
    public GardenService() {
        super("GardenService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Context context = getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        String[] fileNames = context.fileList();
        for(int i = 0; i < fileNames.length; i++) {
            String json = "";
            FileInputStream fileInputStream;
            try{
                fileInputStream = context.openFileInput(fileNames[i]);
                byte[] input = new byte[fileInputStream.available()];
                while(fileInputStream.read(input) != -1){
                    json += new String(input);
                }
                fileInputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //convert json to java object
            Gson gson = new Gson();
            Garden garden = gson.fromJson(json, Garden.class);


            if (networkInfo != null && networkInfo.isConnected()) {

                Forecast forecast = null;
                String urlLocation = garden.location.toLowerCase();
                urlLocation = urlLocation.replaceAll("å", "a");
                urlLocation = urlLocation.replaceAll("ä", "a");
                urlLocation = urlLocation.replaceAll("ö", "o");
                try {
                    new DownloadData(fileNames[i], context)
                            .execute("http://api.openweathermap.org/data/2.5/weather?q=" + urlLocation);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(TAG, "Connected but failed anyway");
                }

            } else {
                Log.i(TAG, "No connection");
            }
        }
    }
}
