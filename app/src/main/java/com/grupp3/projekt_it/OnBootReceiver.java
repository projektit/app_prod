package com.grupp3.projekt_it;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;

import java.lang.*;
import java.lang.System;
import java.util.ArrayList;
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
        setAllUserAlarms(context);
    }

    //set alarms, so GardenService runs in background periodically
    static void setForecastAlarms(Context context){
        Log.i(TAG, "Forecast alarm set");
        AlarmManager alarmManager= (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, GardenService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        //alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), PERIOD, pendingIntent);
    }
    static void setMonthlyAlarms(Context context){
        Log.i(TAG, "Monthly alarm set");
        Calendar calendar = Calendar.getInstance();
        //calendar.set(Calendar.DAY_OF_MONTH, 1);
        //calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 60);

        AlarmManager alarmManager= (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MonthlyUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
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
        //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    static void setAllUserAlarms(Context context) {
        Log.i(TAG, "User alarm set all");
    }
    static void setUserAlarms(Context context){
        Log.i(TAG, "User alarm set");
        GardenUtil gardenUtil = new GardenUtil();
        ArrayList <UserNotification> allUserNotifications = gardenUtil.loadAllUserNotifications(context);
        for(UserNotification userNotification : allUserNotifications){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            calendar.set(Calendar.YEAR, userNotification.getYear());
            calendar.set(Calendar.MONTH, userNotification.getMonth());
            calendar.set(Calendar.DAY_OF_MONTH, userNotification.getDay());
            calendar.set(Calendar.HOUR_OF_DAY, userNotification.getHour());
            calendar.set(Calendar.MINUTE, userNotification.getMinute());

            Gson gson = new Gson();
            String json = gson.toJson(userNotification);

            AlarmManager alarmManager= (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, UserNotificationService.class);
            intent.putExtra("jsonUserNotification", json);
            //intent.putExtra("Notification file id", userNotification.getId());
            PendingIntent pendingIntent = PendingIntent.getService(context, userNotification.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
            if(pendingIntent != null) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }
    static void setUserAlarm(Context context, UserNotification userNotification){
        Log.i(TAG, "User alarm set");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.YEAR, userNotification.getYear());
        calendar.set(Calendar.MONTH, userNotification.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, userNotification.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, userNotification.getHour());
        calendar.set(Calendar.MINUTE, userNotification.getMinute());

        Gson gson = new Gson();
        String json = gson.toJson(userNotification);

        AlarmManager alarmManager= (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, UserNotificationService.class);
        intent.putExtra("jsonUserNotification", json);
        //intent.putExtra("Notification file id", userNotification.getId());
        PendingIntent pendingIntent = PendingIntent.getService(context, userNotification.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if(pendingIntent != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}
