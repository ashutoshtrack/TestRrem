package com.example.ashutosh.testrreminderui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Ashutosh on 5/31/2017.
 */

public class MainReminder extends Fragment {
     static MainReminder mr;
    ListView lw;
    ArrayList<TaskWall> task;
    TaskListAdapter adapter = null;
    String doc_titler;
DatabaseHelper dba;
    AlarmManager amger;

    Bundle bfromrec;
    String FromReciever,FromRecievercopy;

    int restoregetter;

    public MainReminder() {
        // Required empty public constructor
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.task_list_activity, container, false);

        dba = new DatabaseHelper(container.getContext());
           lw =(ListView)rootView.findViewById(R.id.list_view);
        task = new ArrayList<>();
        adapter = new TaskListAdapter(getContext(), R.layout.taski_items,task);

        amger = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        lw.setAdapter(adapter);
        Log.i("hello","adaptive");

        try {
            // Intent ilu = getIntent();
            bfromrec = getActivity().getIntent().getExtras();
            if (bfromrec != null) {
                FromReciever = bfromrec.getString("deletertitlereciever");
                restoregetter = bfromrec.getInt("restorereferal");
            }
            Log.v("IntentExtra", "Got Extra " + FromReciever);

            if (FromReciever != null) {
                     FromRecievercopy =FromReciever;
                Log.v("IntentExtra", "Got Extra " + FromReciever);


                Intent i1 = new Intent(getContext(),AlaramRec.class);
                i1.setAction("com.developer.Caller.reciever.Message");
                i1.addCategory("android.intent.category.DEFAULT");
                i1.putExtra("deletertitlereciever",FromReciever);

                PendingIntent pid = PendingIntent.getBroadcast(getContext(),  MainActivity.ma.myDB.getAlarmIDtest(FromReciever),i1,0);


                amger.cancel(pid);
                MainActivity.ma.myDB.deleteEntry(FromReciever);

                Toast.makeText(getContext(), "Delete Alarm for "+FromRecievercopy, Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e)
        {Log.v("IntentExtra","Actually Crashed "+e.getMessage());
        }
        try {
if(restoregetter ==1)
{
    long intentid = System.currentTimeMillis();
    Cursor rest = MainActivity.ma.myDB.getALLData();
    while(rest.moveToNext()) {
        int  restorealaid  = rest.getInt(8);

        long calendrum = rest.getLong(9);

        int repcopy = rest.getInt(10);

        Intent ione = new Intent(getContext(), AlaramRec.class);

        ione.setAction("com.developer.Caller.reciever.Message");
        ione.addCategory("android.intent.category.DEFAULT");
        ione.putExtra("id", restorealaid);


        PendingIntent pid = PendingIntent.getBroadcast(getContext(), restorealaid, ione, 0);


     if(repcopy == 100) {
         amger.set(AlarmManager.RTC_WAKEUP,
                 calendrum
                 , pid);
     }
     else if(repcopy == 101)
     {
         amger.setRepeating(AlarmManager.RTC_WAKEUP,
                 calendrum,10*1000
                 , pid);
     }

    }
    //Toast.makeText(getContext(), "have values", Toast.LENGTH_SHORT).show();
}


}catch (Exception e){

        }


        Cursor res = MainActivity.ma.myDB.getALLData();
        task.clear();
        while (res.moveToNext()){
            int id =  res.getInt(0);
            String name = res.getString(1);
            String amount = res.getString(2);
            byte[] photos = res.getBlob(3);
            String ddText = res.getString(4);
            String notesText = res.getString(6);
            Log.i("hello","while");

            task.add(new TaskWall(id, name, amount, photos, ddText, notesText));
        }


        adapter.notifyDataSetChanged();

        /*
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long ide) {
                String namer = task.get(position).getName();
                MainActivity.ma.myDB.deleteEntry(namer);
                Cursor res = MainActivity.ma.myDB.getALLData();
                task.clear();
                while (res.moveToNext()){
                    int id =  res.getInt(0);
                    String name = res.getString(1);
                    String amount = res.getString(2);
                    byte[] photos = res.getBlob(3);
                    Log.i("hello","pop");

                    task.add(new TaskWall(id, name, amount, photos));
                }
                adapter.notifyDataSetChanged();

            }
        });
        */

        mr = this;

        // Inflate the layout for this fragment

        return rootView;

    }

    public void cow(final View v ){

        AlertDialog.Builder exiting = new AlertDialog.Builder(getContext());
        exiting.setTitle("Delete!!");
        exiting.setMessage("Are you sure you want to delete this Reminder?");
        exiting.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
             try
             {game(v);}catch (Exception e){

                 Toast.makeText(getContext(), "Restart app!!!", Toast.LENGTH_SHORT).show();

             }
            }
        });

        exiting.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        exiting.show();
        exiting.setCancelable(false);
    }

    private void game(View v) {
        final int postione = lw.getPositionForView(v);
        String namer = task.get(postione).getName();

        Intent i1 = new Intent(getContext(),AlaramRec.class);
        i1.setAction("com.developer.Caller.reciever.Message");
        i1.addCategory("android.intent.category.DEFAULT");
        i1.putExtra("id",MainActivity.ma.myDB.getAlarmIDtest(namer));

        PendingIntent pid = PendingIntent.getBroadcast(getContext(),MainActivity.ma.myDB.getAlarmIDtest(namer),i1,0);

        //MainActivity.ma.myAlarmManager.cancel(pid);
        amger.cancel(pid);
        Toast.makeText(getContext(), "Stopped the Alarm of " +namer +MainActivity.ma.myDB.getAlarmIDtest(namer), Toast.LENGTH_SHORT).show();



        MainActivity.ma.myDB.deleteEntry(namer);



        Cursor res = MainActivity.ma.myDB.getALLData();
        task.clear();
        while (res.moveToNext()) {
            int id = res.getInt(0);
            String name = res.getString(1);
            String amount = res.getString(2);
            byte[] photos = res.getBlob(3);
            String ddText = res.getString(4);
            String notesText = res.getString(6);

            Log.i("hello", "pop");

            task.add(new TaskWall(id, name, amount, photos , ddText, notesText));
        }
        adapter.notifyDataSetChanged();
    }



}
