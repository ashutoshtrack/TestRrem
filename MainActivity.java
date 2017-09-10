package com.example.ashutosh.testrreminderui;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    static MainActivity ma;
    private static final int REQUEST_CODE_RESOLUTION = 3;

    TabLayout myTab;
    ViewPager myPager;
    DatabaseHelper myDB;
    AlarmManager myAlarmManager;
    public static GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError =false;

    private static final String STATE_RESOLVING_ERROR ="resolving_error";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResolvingError = savedInstanceState!= null
        && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR,false);

        myDB = new DatabaseHelper(this);


        myTab = (TabLayout) findViewById(R.id.myTab);
        myPager = (ViewPager) findViewById(R.id.mypager);

        myPager.setAdapter(new MyOwnAdapterPager(getSupportFragmentManager()));
        myTab.setupWithViewPager(myPager);

        myPager.setCurrentItem(1);

        myTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                myPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        myAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        ma = this;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR,mResolvingError);
    }

    public void doExitApp(MenuItem item) {
        AlertDialog.Builder exiting = new AlertDialog.Builder(this);
        exiting.setTitle("EXIT!!");
        exiting.setMessage("Are you sure you want to exit?");
        exiting.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
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

    public void doAddTask(MenuItem item) {

        Toast.makeText(this, "Opening Setter!!", Toast.LENGTH_SHORT).show();

        Intent intenti = new Intent(MainActivity.this, ThirdActivity.class);
        startActivity(intenti);
    }


    class MyOwnAdapterPager extends FragmentPagerAdapter {
        String data[] = {"Camera", "Reminder", "EditSettings"};

        public MyOwnAdapterPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {


            if (position == 0) {
                return new BlankFragment();
            }
            if (position == 1) {
                return new MainReminder();
            }
            if (position == 2) {
                return new EditSettings();
            }

            return null;
        }

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return data[position];
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("TAG", "API client connected.");
        Toast.makeText(this, "Account Connected!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("TAG", "GoogleApiClient connection suspended");

        try{

            Toast.makeText(this, "Connection suspended", Toast.LENGTH_SHORT).show();


        }catch(Exception e){}

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("TAG", "GoogleApiClient connection failed: " + connectionResult.toString());
        try {
        Toast.makeText(this, "Connection Failed Check Your Network!", Toast.LENGTH_SHORT).show();
if(mResolvingError){

    mGoogleApiClient.connect();
}
        if (!connectionResult.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, connectionResult.getErrorCode(), 0).show();
            return;
        }
           mResolvingError = true;
            connectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            Log.e(" Tag", "Exception while starting resolution activity", e);

            mGoogleApiClient.connect();
        }catch(Exception e){
            mGoogleApiClient.connect();

        }
    }


}
