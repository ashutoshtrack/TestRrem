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
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import static android.R.attr.data;

/**
 * Created by Ashutosh on 6/1/2017.
 */

public class AlaramRec extends BroadcastReceiver {
    DatabaseHelper dabaseReciever;






    @Override
    public void onReceive(Context context, Intent intent) {

        dabaseReciever = new DatabaseHelper(context);





        byte [] photo = dabaseReciever.getPhoto(dabaseReciever.getIdkarantest());

        Bitmap bitmapwall = BitmapFactory.decodeByteArray(photo, 0 , photo.length);

        SharedPreferences sh1 = context.getSharedPreferences("Mojar", Context.MODE_APPEND);
        String p = sh1.getString("user", "");
        String q = sh1.getString("amount", "");
        String r = sh1.getString("encoder", "");
      Object object=  intent.getExtras().get("BitmapImage");
        if(object!=null){
            Log.v("object ",""+object);
        }
        else
        {
            Log.v("object","is empty");

        }




   /*
        if (!r.equalsIgnoreCase("")) {
            byte[] b = Base64.decode(r, Base64.DEFAULT);
           Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);*/

Intent intent1 = new Intent(context,ThirdActivity.class);

       /* if(intent1.hasExtra("byteArray")) {

            Bitmap b = BitmapFactory.decodeByteArray(
                    intent1.getByteArrayExtra("byteArray"),0,intent1
                            .getByteArrayExtra("byteArray").length); */


        PendingIntent pid = PendingIntent.getActivity(context, 1, intent1, 0);
            NotificationManagerCompat myManager = NotificationManagerCompat.from(context);
            NotificationCompat.Builder myNoti = new NotificationCompat.Builder(context);
            //BitmapFactory.decodeResource(context.getResources(),
            myNoti.setContentTitle(dabaseReciever.getNaamkarantest(dabaseReciever.getIdkarantest()));
            myNoti.setContentText(dabaseReciever.getAmountkarantest());
        myNoti.setSmallIcon(R.drawable.ic_stat_namerec);
            myNoti.setLargeIcon(bitmapwall);
     //   myNoti.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.balbasor));
     //   myNoti.setLargeIcon(decodeBase64(r));
      //  myNoti.setLargeIcon(bitmape);
        myNoti.setDefaults(Notification.DEFAULT_ALL);
        myNoti.setAutoCancel(true);
        myNoti.setContentIntent(pid);
         //  myNoti.setLargeIcon(bitmap);
        //    myNoti.setDefaults(Notification.DEFAULT_SOUND+Notification.DEFAULT_VIBRATE);
          //  myNoti.setDefaults(Notification.DEFAULT_VIBRATE);


            myManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), myNoti.build());
      //  }
    }
}
