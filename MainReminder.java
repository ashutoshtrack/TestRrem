package com.example.ashutosh.testrreminderui;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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


    public MainReminder() {
        // Required empty public constructor
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.task_list_activity, container, false);

           lw =(ListView)rootView.findViewById(R.id.list_view);
        task = new ArrayList<>();
        adapter = new TaskListAdapter(getContext(), R.layout.taski_items,task);

        lw.setAdapter(adapter);
        Log.i("hello","adaptive");

        Cursor res = MainActivity.ma.myDB.getALLData();
        task.clear();
        while (res.moveToNext()){
            int id =  res.getInt(0);
            String name = res.getString(1);
            String amount = res.getString(2);
            byte[] photos = res.getBlob(3);
            Log.i("hello","while");

            task.add(new TaskWall(id, name, amount, photos));
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
                game(v);
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

        Intent i1 = new Intent();
        i1.setAction("com.ashu.Alaramcall.reciever.Message");
        i1.addCategory("android.intent.category.DEFAULT");


        PendingIntent pid = PendingIntent.getBroadcast(getContext(),1,i1,0);



        MainActivity.ma.myAlarmManager.cancel(pid);

        Toast.makeText(getContext(), "Stopped the Alarm", Toast.LENGTH_SHORT).show();





        final int postion = lw.getPositionForView(v);

        String namer = task.get(postion).getName();
        MainActivity.ma.myDB.deleteEntry(namer);
        Cursor res = MainActivity.ma.myDB.getALLData();
        task.clear();
        while (res.moveToNext()) {
            int id = res.getInt(0);
            String name = res.getString(1);
            String amount = res.getString(2);
            byte[] photos = res.getBlob(3);
            Log.i("hello", "pop");

            task.add(new TaskWall(id, name, amount, photos));
        }
        adapter.notifyDataSetChanged();
    }



}
