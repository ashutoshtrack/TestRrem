package com.example.ashutosh.testrreminderui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity {
    static MainActivity ma;

    TabLayout myTab;
    ViewPager myPager;
    DatabaseHelper myDB;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myDB = new DatabaseHelper(this);


        myTab = (TabLayout) findViewById(R.id.myTab);
        myPager = (ViewPager) findViewById(R.id.mypager);

        myPager.setAdapter(new MyOwnAdapterPager(getSupportFragmentManager()));
        myTab.setupWithViewPager(myPager);

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


        ma = this;
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
        String data[] = {"Camera","Reminder","EditSettings"};

        public MyOwnAdapterPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0){
                return new BlankFragment();
            }
            if (position == 1){
                return new MainReminder();
            }
            if (position == 2){
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

        getMenuInflater().inflate(R.menu.options_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


}
