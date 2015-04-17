package com.grupp3.projekt_it;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by Daniel on 2015-04-17.
 */
public class OnBootReceiver extends BroadcastReceiver{
    String TAG = "com.grupp3.projekt_it";
    private static final int PERIOD = 1000 * 60 * 60;
    @Override
    public void onReceive(Context context, Intent intent) {
        setAlarms(context);
    }
    static void setAlarms(Context context){
        AlarmManager alarmManager= (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i=new Intent(context, GardenService.class);
        PendingIntent pendingIntent=PendingIntent.getService(context, 0, i, 0);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
        SystemClock.elapsedRealtime() + PERIOD, PERIOD, pendingIntent);
    }
}
