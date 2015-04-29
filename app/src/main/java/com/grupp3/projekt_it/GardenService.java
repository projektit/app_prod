package com.grupp3.projekt_it;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
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
import java.util.ArrayList;

/**
 * Created by Daniel on 2015-04-17.
 */

//service which will run in background according to alarm setup in OnBootReceiver class

public class GardenService extends IntentService {
    String TAG = "com.grupp3.projekt_it";
    public GardenService() {
        super("GardenService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "GardenService started");
        Context context = getApplicationContext();
        //get network info
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //get all files in applications internal memory
        String [] files = getApplicationContext().fileList();
        ArrayList<String> desiredFiles = new ArrayList<>();
        for(int i = 0; i < files.length; i ++){
            if (files[i].endsWith(".grdn")){
                desiredFiles.add(files[i].substring(0, files[i].length()-5));
            }
        }
        String [] items = desiredFiles.toArray(new String[desiredFiles.size()]);
        //do for all gardens
        for(int i = 0; i < items.length; i++) {
            GardenUtil gardenUtil = new GardenUtil();
            Garden garden = gardenUtil.loadGarden(items[i], context);

            //if has network
            if (networkInfo != null && networkInfo.isConnected()) {
                Forecast forecast = null;
                //replace swedish chars
                String urlLocation = garden.location.toLowerCase();
                urlLocation = urlLocation.replaceAll("å", "a");
                urlLocation = urlLocation.replaceAll("ä", "a");
                urlLocation = urlLocation.replaceAll("ö", "o");
                try {
                    new DownloadForecast(items[i], context, garden)
                            .execute("http://api.openweathermap.org/data/2.5/weather?q=" + urlLocation + "&units=metric");
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
