package com.example.ashutosh.testrreminderui;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

/**
 * Created by Ashutosh on 6/1/2017.
 */

public class AlaramRec extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        SharedPreferences sh1 = context.getSharedPreferences("Mojar",Context.MODE_APPEND);
        String  p = sh1.getString("user","");
        String q = sh1.getString("amount","");
        NotificationManagerCompat myManager = NotificationManagerCompat.from(context);
        NotificationCompat.Builder myNoti = new NotificationCompat.Builder(context);
        myNoti.setContentText(q);
        myNoti.setContentTitle(p);
        myNoti.setSmallIcon(R.drawable.ic_stat_namerec);
        myNoti.setDefaults(Notification.DEFAULT_SOUND);
        myNoti.setDefaults(Notification.DEFAULT_VIBRATE);

        myManager.notify(1,myNoti.build());
    }
}
