package com.grupp3.projekt_it;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
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
public class MonthlyUpdateService extends IntentService {
    String TAG = "com.grupp3.projekt_it";

    public MonthlyUpdateService() {
        super("MonthlyUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean notifi = getPrefs.getBoolean("notification_on", true);

        if (notifi == true ) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setAutoCancel(true);
            builder.setContentTitle("Månadens tips är här");
            builder.setContentText("Klicka för mer att få veta mer");
            builder.setSmallIcon(R.mipmap.app_icon_launch);

            Intent intent1 = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            Notification notification = builder.build();

            boolean vibrate = getPrefs.getBoolean("notification_vibration", true);
            if (vibrate == true) {
                notification.defaults |= Notification.DEFAULT_VIBRATE;
            }

            boolean sound = getPrefs.getBoolean("notification_sound", true);
            if (sound == true) {
                notification.defaults |= Notification.DEFAULT_SOUND;
            }

            notification.defaults |= Notification.DEFAULT_LIGHTS;
            NotificationManager manager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
            manager.notify(1, notification);

            Calendar c = Calendar.getInstance();
            int month = c.get(Calendar.MONTH);
            if(month == 11){
                month = 0;
            }else{
                month += 1;
            }
            OnBootReceiver.setMonthlyAlarms(getApplicationContext(), month);
        }
    }
}
