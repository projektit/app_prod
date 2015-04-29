package com.grupp3.projekt_it;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.SystemClock;
import android.util.Log;

import java.util.Calendar;

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
        setForecastAlarms(context);
        setMonthlyAlarms(context);
        setZoneAlarms(context);
    }

    //set alarms, so GardenService runs in background periodically
    static void setForecastAlarms(Context context){
        Log.i(TAG, "Forecast alarm set");
        AlarmManager alarmManager= (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, GardenService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), PERIOD, pendingIntent);
    }
    static void setMonthlyAlarms(Context context){
        Log.i(TAG, "Monthly alarm set");
        Calendar calendar = Calendar.getInstance();
        //calendar.set(Calendar.DAY_OF_MONTH, 1);
        //calendar.set(Calendar.HOUR_OF_DAY, 11);
        //calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 60);

        AlarmManager alarmManager= (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MonthlyUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
    static void setZoneAlarms(Context context){
        Log.i(TAG, "Zone alarm set");
        Calendar calendar = Calendar.getInstance();
        //calendar.set(Calendar.HOUR_OF_DAY, 16);
        //calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 60);

        AlarmManager alarmManager= (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, CheckZoneService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
