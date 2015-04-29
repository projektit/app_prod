package com.grupp3.projekt_it;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MonthlyUpdateService extends IntentService {
    String TAG = "com.grupp3.projekt_it";

    public MonthlyUpdateService() {
        super("GardenService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setAutoCancel(true);
        builder.setContentTitle("Månadens tips är här");
        builder.setContentText("Klicka för mer att få veta mer");
        builder.setSmallIcon(R.drawable.app_icon);

        Intent intent1 = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean vibrate = getPrefs.getBoolean("notification_vibration", true);
        if(vibrate == true) {
            notification.defaults |= Notification.DEFAULT_VIBRATE;
        }
        NotificationManager manager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1, notification);

        OnBootReceiver.setMonthlyAlarms(getApplicationContext());
    }
}
