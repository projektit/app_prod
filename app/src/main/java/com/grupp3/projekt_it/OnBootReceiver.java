package com.grupp3.projekt_it;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import com.google.gson.Gson;
import java.lang.*;
import java.lang.System;
import java.util.ArrayList;
import java.util.Calendar;

//Extends broadcast receiver allows another context to execute application code with same permissions
    // onReceive receives broadcast messages from OS, see manifest
/*
 *
 * @author Marcus Elwin
 * @author Daniel Freberg
 * @author Esra Kahraman
 * @author Oscar Melin
 * @author Mikael Mölder
 * @author Erik Nordell
 * @author Felicia Schnell
 *
*/
public class OnBootReceiver extends BroadcastReceiver{
    static String TAG = "com.grupp3.projekt_it";
    private static final int PERIOD = 1000 * 60 * 60;

    //on receive restarts alarm after device reboot
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Alarm reset on boot");
        setForecastAlarms(context);
        Calendar calendar = Calendar.getInstance();
        setMonthlyAlarms(context, calendar.get(Calendar.MONTH));
        setZoneAlarms(context);
        setAllUserAlarms(context);
    }

    //set alarms, so GardenService runs in background periodically
    static void setForecastAlarms(Context context){
        Log.i(TAG, "Forecast alarm set");
        AlarmManager alarmManager= (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, GardenService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), PERIOD, pendingIntent);
    }

    static void setMonthlyAlarms(Context context, int month){
        Intent intent = new Intent(context, MonthlyUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, month, intent, PendingIntent.FLAG_NO_CREATE);
        if(pendingIntent == null){
            pendingIntent = PendingIntent.getService(context, month, intent, 0);
        }else{
            return;
        }
        Log.i(TAG, "Monthly alarm set");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 30);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }
    static void setZoneAlarms(Context context){
        Log.i(TAG, "Daily alarm set");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 30);

        AlarmManager alarmManager= (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if(pendingIntent != null){
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    static void setAllUserAlarms(Context context) {
        Log.i(TAG, "User alarm set all");
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
        PendingIntent pendingIntent = PendingIntent.getService(context, userNotification.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if(pendingIntent != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}
