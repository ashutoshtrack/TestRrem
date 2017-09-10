package com.example.ashutosh.testrreminderui;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
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
    AlarmManager myAlarmManger;
    DatabaseHelper dbrec;
    int recieverid;
    Bundle recExtra;
    long calendrum,calendrumcopy;


    int Uniquepdid = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);


    @Override
    public void onReceive(Context context, Intent intent) {

        dbrec = new DatabaseHelper(context);

        recExtra = intent.getExtras();
        recieverid = (int) recExtra.get("id");
        calendrum = (long) recExtra.get("calenderalarmdate");

       calendrumcopy = (long) recExtra.get("calenderuseretter");

        myAlarmManger = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if(calendrum>calendrumcopy)
        {

            Log.v("CalenderOnRecieve","System Time is larger");
            Intent i1 = new Intent(context,AlaramRec.class);
          /*  i1.setAction("com.developer.Caller.reciever.Message");
            i1.addCategory("android.intent.category.DEFAULT");

            i1.putExtra("id",recieverid);
          */
            PendingIntent pid = PendingIntent.getBroadcast(context, recieverid, i1, 0);   //(int)intentid1
            myAlarmManger.cancel(pid);


        }


        byte [] photo = dbrec.getPhoto(recieverid);

        Bitmap bitmapwall = BitmapFactory.decodeByteArray(photo, 0 , photo.length);


      Object object=  intent.getExtras().get("BitmapImage");
        if(object!=null){
            Log.v("object ",""+object);
        }
        else
        {
            Log.v("object","is empty");

        }






        Uri alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        Intent intent1 = new Intent(context,MainActivity.class);
        PendingIntent pid = PendingIntent.getActivity(context, 1, intent1, 0);




        Intent rdelete = new Intent(context,MainActivity.class);
        rdelete.putExtra("deletertitlereciever",dbrec.NaamKaAlaram(recieverid));
        PendingIntent Prdelete = PendingIntent.getActivity(context, Uniquepdid, rdelete, 0);

        Intent browserIntentRecievers  = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" +dbrec.URlKaAlaram(recieverid)));
        PendingIntent PbrowserIntentRecievers = PendingIntent.getActivity(context, Uniquepdid, browserIntentRecievers, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationManagerCompat myManager = NotificationManagerCompat.from(context);
            NotificationCompat.Builder myNoti = new NotificationCompat.Builder(context);


            myNoti.setContentTitle(dbrec.NaamKaAlaram(recieverid))
            .setContentText(dbrec.AmountKaAlaram(recieverid))
            .setSmallIcon(R.drawable.ic_stat_namerec)
            .setLargeIcon(bitmapwall)
        .addAction(R.mipmap.ic_launcher,"Delete",Prdelete)
        .addAction(R.mipmap.ic_launcher,"WEBSITE",PbrowserIntentRecievers)
           // myNoti.setDefaults(Notification.DEFAULT_ALL);
             .setAutoCancel(true)
                    .setSound(alarmTone)
             .setContentIntent(pid)
           .setDefaults(Notification.DEFAULT_VIBRATE);

        Notification notification = myNoti.build();
        notification.flags|= Notification.FLAG_INSISTENT;

            myManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), notification);

    }
}
