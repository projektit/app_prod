package com.grupp3.projekt_it;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by Daniel on 2015-04-17.
 */

//Extends broadcast receiver allows another context to execute application code with same permissions
    // onReceive receives broadcast messages from OS, see manifest

public class OnBootReceiver extends BroadcastReceiver{
    static String TAG = "com.grupp3.projekt_it";
    private static final int PERIOD = 1000 * 60 * 60;

    //on receive restarts alarm after device reboot
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Alarm reset on boot");
        setAlarms(context);
    }

    //set alarms, so GardenService runs in background periodically
    static void setAlarms(Context context){
        Log.i(TAG, "Alarm set");
        AlarmManager alarmManager= (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, GardenService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), PERIOD, pendingIntent);
    }
}
