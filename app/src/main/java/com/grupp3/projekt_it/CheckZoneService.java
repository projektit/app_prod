package com.grupp3.projekt_it;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class CheckZoneService extends IntentService {
    String TAG = "com.grupp3.projekt_it";

    public CheckZoneService() {
        super("CheckZoneService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "CheckZoneService started");
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
        ArrayList <String> messages = new ArrayList <String>();
        int counter = 0;
        for(int i = 0; i < items.length; i++) {
            GardenUtil gardenUtil = new GardenUtil();
            Garden garden = gardenUtil.loadGarden(items[i], context);
            if(garden.getForecast() == null){
                return;
            }
            double gardenMinTemp = garden.getForecast().getMain().getTemp_min();
            String tableName = garden.getTableName();
            SQLPlantHelper sqlPlantHelper = new SQLPlantHelper(getApplicationContext());
            ArrayList <Plant_DB> allPlants = sqlPlantHelper.getAllPlants(garden.getTableName());

            for(Plant_DB plant_db: allPlants) {
                if (!"None".equals(plant_db.get_zone_min())) {
                    int plantZone = Integer.parseInt(plant_db.get_zone_min());
                    double plantMinTemp = 0;
                    if (plantZone == 1) {
                        plantMinTemp = 9;
                    }
                    if (plantZone == 2) {
                        plantMinTemp = 8;
                    }
                    if (plantZone == 3) {
                        plantMinTemp = 7;
                    }
                    if (plantZone == 4) {
                        plantMinTemp = 6;
                    }
                    if (plantZone == 5) {
                        plantMinTemp = 5;
                    }
                    if (plantZone == 6) {
                        plantMinTemp = 4;
                    }
                    if (plantZone == 7) {
                        plantMinTemp = 3;
                    }
                    if (plantZone == 8) {
                        plantMinTemp = 2;
                    }
                    if (plantZone == 9) {
                        plantMinTemp = 1;
                    }

                    if (plantMinTemp < gardenMinTemp) {
                        messages.add("Din " + plant_db.get_swe_name() + " i " + garden.getName() + " fryser");
                        counter += 1;
                    }
                }

            }
        }
        if(counter > 0) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            if (items.length > 1) {
                if (counter > 1) {
                    builder.setContentText("Några blommor i dina trädgårdar fryser");
                } else {
                    builder.setContentText("En blommor i någon av dina trädgårdar fryser");
                }
            } else {
                if(counter > 1) {
                    builder.setContentText("Några blommor i din trädgård fryser");
                }else{
                    builder.setContentText("En blomma i din trädgård fryser");
                }
            }
            Intent intent1 = new Intent(this, MyGardenListActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setAutoCancel(true);
            builder.setContentTitle("Nya skötselråd!");
            builder.setSmallIcon(R.drawable.app_icon);

            builder.setLights(Color.WHITE, 1000, 5000);

            Notification notification = builder.build();
            SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            boolean vibrate = getPrefs.getBoolean("notification_vibration", true);
            if(vibrate == true) {
                notification.defaults |= Notification.DEFAULT_VIBRATE;
            }
            notification.defaults |= Notification.DEFAULT_LIGHTS;

            builder.setContentIntent(pendingIntent);

            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle("Händelser: ");
            for (String message : messages) {
                inboxStyle.addLine(message);
            }
            builder.setStyle(inboxStyle);

            NotificationManager manager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
            manager.notify(2, notification);
        }

        OnBootReceiver.setZoneAlarms(getApplicationContext());
    }
}