package com.example.ashutosh.testrreminderui;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import static android.R.attr.data;

/**
 * Created by Ashutosh on 6/1/2017.
 */

public class AlaramRec extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sh1 = context.getSharedPreferences("Mojar", Context.MODE_APPEND);
        String p = sh1.getString("user", "");
        String q = sh1.getString("amount", "");
    /*  String r = sh1.getString("encoder", "");
        if (!r.equalsIgnoreCase("")) {
            byte[] b = Base64.decode(r, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            */
Intent intent1 = new Intent(context,ThirdActivity.class);
        PendingIntent pid = PendingIntent.getActivity(context, 1, intent1, 0);
            NotificationManagerCompat myManager = NotificationManagerCompat.from(context);
            NotificationCompat.Builder myNoti = new NotificationCompat.Builder(context);

            myNoti.setContentTitle(p);
            myNoti.setContentText(q);
        myNoti.setSmallIcon(R.drawable.ic_stat_namerec);
        myNoti.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.balbasor));
        myNoti.setDefaults(Notification.DEFAULT_ALL);
        myNoti.setAutoCancel(true);
        myNoti.setContentIntent(pid);
         //  myNoti.setLargeIcon(bitmap);
        //    myNoti.setDefaults(Notification.DEFAULT_SOUND+Notification.DEFAULT_VIBRATE);
          //  myNoti.setDefaults(Notification.DEFAULT_VIBRATE);


            myManager.notify(1, myNoti.build());
        //}
    }
}
