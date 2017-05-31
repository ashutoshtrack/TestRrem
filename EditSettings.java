package com.example.ashutosh.testrreminderui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Ashutosh on 5/31/2017.
 */

public class EditSettings extends Fragment {

    AlarmManager myAlarmManagers;
    EditText talk;
    TextView watch;

    public EditSettings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewsa = inflater.inflate(R.layout.fragment_editsettings, container, false);

        talk =(EditText)viewsa.findViewById(R.id.t1);
        watch = (TextView)viewsa.findViewById(R.id.textView2);

        Button button2 = (Button)viewsa.findViewById(R.id.button2);
        Button button3 = (Button)viewsa.findViewById(R.id.button3);

        myAlarmManagers = (AlarmManager)getActivity().getSystemService(ALARM_SERVICE);


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startThis(v);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopThis(v);
            }
        });

        // Inflate the layout for this fragment
        return viewsa;

    }
    public void startThis(View view) {
        String crux = talk.getText().toString();
        watch.setText(" Alarm set for  "+crux);

        SharedPreferences sh =  view.getContext().getSharedPreferences("Mojas", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sh.edit();
        myEdit.putString("usera",crux);
        myEdit.apply();


        Intent i1 = new Intent();
        i1.setAction("com.ashu.cosmo.reciever.Message");
        i1.addCategory("android.intent.category.DEFAULT");

        PendingIntent pid = PendingIntent.getBroadcast(view.getContext(),1,i1,0);

        myAlarmManagers.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),1000 * 20,pid);


    }

    public void stopThis(View view) {
        Intent i1 = new Intent();
        i1.setAction("com.ashu.gazar.reciever.Message");
        i1.addCategory("android.intent.category.DEFAULT");

//         String delte =  talk.getText().toString();
        //   mydbhandler.deletetask(delte);

        PendingIntent pid = PendingIntent.getBroadcast(view.getContext(),1,i1,0);



        myAlarmManagers.cancel(pid);

        Toast.makeText(view.getContext(), "Stopped the Alarm", Toast.LENGTH_SHORT).show();

    }

}
