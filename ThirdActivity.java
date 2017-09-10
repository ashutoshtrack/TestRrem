package com.example.ashutosh.testrreminderui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ashutosh on 5/31/2017.
 */

public class ThirdActivity extends AppCompatActivity {
static ThirdActivity tambura;
    AlaramRec alaramRec;
    Bundle bund;
    Bitmap imagere;
    EditText dd,url1;
    EditText doctitle,amount,nts;
    RadioGroup rg,rp;
    RadioButton defTime,customTime,nonrepeatingradio,repeatingradio;
    Spinner dynamicSpinner;
    ArrayAdapter<CharSequence> adapter;
    ImageButton btn,but;
    public static final int REQUEST_CAPTURE= 1;
    ImageView img1;
    Button btnAddwa,cancu;
    AlarmManager myAlarmManger;
    static int counter = 0;
    static Uri capturedImageUri = null;

    int year_x,month_x,day_x,monthcopy_x;
    static  long calendrum ;
    long calendrumcopy;
    int hour_x,minute_x,second_x;

int scrap;
int rep,repcopy;
    private static final int DILOG_ID = 0 ;
    private static final int DILOGTIME_ID = 1 ;


   DatabaseHelper myDB;
   int priordateposition;
    private static  final  int PICK_IMAGES = 100;
    Uri imageuri;

    String nametaker,nametaker2;
    int i =0;

    int alarmid ;
    long intentid =0;
    long intentid1;


    Bundle bfromrec;
    String FromReciever;
    private File mImageFolder;
    public File destination;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        myAlarmManger = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmid = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);


        myDB = new DatabaseHelper(this);
        tambura = this;

        final Calendar tarik = Calendar.getInstance();
        year_x =  tarik.get(Calendar.YEAR);
        month_x = tarik.get(Calendar.MONTH);
        day_x = tarik.get(Calendar.DAY_OF_MONTH);

        dd = (EditText) findViewById(R.id.dateset);

        dd.setFocusable(false);
        nts = (EditText) findViewById(R.id.notes);
        doctitle = (EditText) findViewById(R.id.doctitle);
        amount = (EditText) findViewById(R.id.amount);

        rg = (RadioGroup) findViewById(R.id.radioGroup);

        defTime = (RadioButton) findViewById(R.id.radioDefault);
        defTime.setChecked(true);
        DefaultTimeClickecopy();
        customTime= (RadioButton) findViewById(R.id.radioCustom);
        defTime.getId();

        nonrepeatingradio = (RadioButton) findViewById(R.id.radioNonR);
        nonrepeatingradio.setChecked(true);
        NonRepeatClickecopy();

        repeatingradio = (RadioButton) findViewById(R.id.radioRepeat);



        btnAddwa = (Button) findViewById(R.id.btnAdder);
        cancu = (Button) findViewById(R.id.cancelbaby);
        but =(ImageButton)findViewById(R.id.daba);
        img1 = (ImageView)findViewById(R.id.imgdikha);

        url1 = (EditText)findViewById(R.id.urldala);
            alaramRec=new AlaramRec();
        showDialogOnImageButtonClickListener();

        if(!hasCamers()){
            but.setEnabled(false);
        }



        //-----Spinner ka Code------------//




        dynamicSpinner = (Spinner)findViewById(R.id.spinner);



        adapter = ArrayAdapter.createFromResource(this, R.array.due_dates, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dynamicSpinner.setAdapter(adapter);



        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                priordateposition = parent.getSelectedItemPosition();




                Toast.makeText(ThirdActivity.this, "You have selected "+priordateposition+" "+parent.getItemAtPosition(position) , Toast.LENGTH_SHORT).show();
                //parent.getItemAtPosition(position)
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                         parent.getItemAtPosition(0);

            }
        });






        createImageFolder();
        AddData();
        showToast();


    }

    private void showToast() {
        cancu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ThirdActivity.this, "cancelled", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ThirdActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //add  Data;
    private void AddData() {
        btnAddwa.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View v) {
                SavingAcitivity();
            }

            private byte[] ImagetoByte(ImageView img1) {
                Bitmap bitmap = ((BitmapDrawable)img1.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                return byteArray;

            }

        });

    }
    //--------Camera API ka CODE------------//

    public boolean hasCamers()
    {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }
    public void showDialogOnImageButtonClickListener()
    {

        btn = (ImageButton)findViewById(R.id.cal);

        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(DILOG_ID);
                    }
                }
        ) ;
    }

    public void dabaya(View view) {

        Log.v("hellow","world");
        final CharSequence[] items = {"Camera","Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ThirdActivity.this);

        builder.setTitle("Add document imagho");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(items[which].equals("Camera")){
                    Toast.makeText(ThirdActivity.this, "Camera", Toast.LENGTH_SHORT).show();
                     destination = new File(mImageFolder,
                            System.currentTimeMillis() + ".jpg");
                    Intent e = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    e.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));


                    startActivityForResult(e,REQUEST_CAPTURE);

                }else  if (items[which].equals("Gallery")){
                    Toast.makeText(ThirdActivity.this, "Gallery", Toast.LENGTH_SHORT).show();
                    Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(gallery,PICK_IMAGES);

                }

            }
        });
        builder.show();
        builder.setCancelable(false);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {

             if(requestCode==REQUEST_CAPTURE && resultCode == Activity.RESULT_OK){
             //onCaptureImageResult(data);
                 try {
                Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse("file:"+destination));
                     ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                     mImageBitmap.compress(Bitmap.CompressFormat.JPEG,100, bytes);

                     img1.setImageBitmap(mImageBitmap);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
            if(requestCode==PICK_IMAGES){
      imageuri = data.getData();
                img1.setImageURI(imageuri);


            }}
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this, "Try Again!", Toast.LENGTH_SHORT).show();
        }
    }

    private void createImageFolder() {
        File imageFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        mImageFolder = new File(imageFile, "Documents");
        if(!mImageFolder.exists()) {
            mImageFolder.mkdirs();
        }}
/*
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();


        thumbnail.compress(Bitmap.CompressFormat.JPEG,100, bytes);
     //   File destination = new File(mImageFolder,
      //         System.currentTimeMillis() + ".jpg");

        //-------trials



        //-------trailsend
     /*   FileOutputStream fo;
        try {

            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        img1.setImageBitmap(thumbnail);
    }
*/
    //--------Camera API ka CODE khatam------------//

    //--------Date Picker ka CODE------------//


    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == DILOG_ID)
            return new DatePickerDialog(this,dpickerListner,year_x,month_x,day_x);
        else if(id == DILOGTIME_ID)
            return new TimePickerDialog(this,tpickerListner,hour_x,minute_x,true);
        return null;


    }

    private DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x=year;
            month_x= month + 1;
            day_x=dayOfMonth;

            monthcopy_x=month;
            dd.setText(day_x +"/"+ month_x +"/" + year_x);
        }
    };
    //--------Date Picker ka CODE khatam------------//

    //--------Time Picker ka CODE chaalu-----------//

    private TimePickerDialog.OnTimeSetListener tpickerListner = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {

            hour_x = i;
            minute_x= i1;
            Toast.makeText(ThirdActivity.this, hour_x +"hrs  "+ minute_x +"minutes" , Toast.LENGTH_SHORT).show();


        }
    };

    //--------Time Picker ka CODE khatam------------//


    //--------Save Button ka CODE------------//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.savey,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void savekar(MenuItem item) {
        SavingAcitivity();
    }


    public  void SavingAcitivity(){

        try {
            nametaker = doctitle.getText().toString();
            nametaker2 = myDB.getCloneAll(nametaker);
            if(nametaker.equals(nametaker2))
            {

                i++;
                doctitle.setText(nametaker+"("+i+")");
                Log.v("Pog0.1"," IT has duplicate value"+nametaker+nametaker2);

            }else
            {
                i=0;
                Log.v("Pog0.2"," ITs fresh "+nametaker+nametaker2);
            }
        }catch (Exception e)
        {
            Log.v("PogExcep","Error"+e.getMessage());
        }

        try {
            if(intentid == 0){
                intentid = intentid1 = System.currentTimeMillis();
                counter++;
                Log.v("Pog1","initial cycle "+(int)intentid+" "+(int)intentid1);
                Log.v("Pog1.1","initial cycle "+counter);
            }
            else
            {
                intentid1 = intentid;
                counter++;
                Log.v("Pog2","second cycle "+intentid+" "+(int)intentid1);
                Log.v("Pog2.1","second cycle"+counter);
            }
            if(counter !=1) {
                intentid = System.currentTimeMillis();
                Log.v("Pog2.2","second cycle"+(int)intentid+" "+(int)intentid1);
                Log.v("Pog2.3","second cycle"+counter);
            }
            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.YEAR,year_x);
            calendar.set(Calendar.MONTH,monthcopy_x);
            calendar.set(Calendar.DATE,day_x);
            //  calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            calendar.set(Calendar.HOUR_OF_DAY, hour_x);
            calendar.set(Calendar.MINUTE,minute_x);  //
            calendar.set(Calendar.SECOND, 0);

            calendrum = calendar.getTimeInMillis();
            calendrumcopy=calendrum;
            boolean isInserted = myDB.insertData(doctitle.getText().toString(), amount.getText().toString(), ImagetoByte(img1), dd.getText().toString(), String.valueOf(priordateposition), nts.getText().toString(), url1.getText().toString(),(int)intentid, calendrum, repcopy );

            if (isInserted ) {




                if(priordateposition==7){

                    calendar.add(Calendar.DAY_OF_YEAR,-7);
                    calendrum = calendar.getTimeInMillis();
                }
                if(priordateposition==6){

                    calendar.add(Calendar.DAY_OF_YEAR,-6);
                    calendrum = calendar.getTimeInMillis();
                }
                if(priordateposition==5){

                    calendar.add(Calendar.DAY_OF_YEAR,-5);
                    calendrum = calendar.getTimeInMillis();
                }
                if(priordateposition==4){

                    calendar.add(Calendar.DAY_OF_YEAR,-4);
                    calendrum = calendar.getTimeInMillis();
                }
                if(priordateposition==3){

                    calendar.add(Calendar.DAY_OF_YEAR,-3);
                    calendrum = calendar.getTimeInMillis();
                }
                if(priordateposition==2){

                    calendar.add(Calendar.DAY_OF_YEAR,-2);
                    calendrum = calendar.getTimeInMillis();
                }

                if(priordateposition==1){

                    calendar.add(Calendar.DAY_OF_YEAR,-1);
                    calendrum = calendar.getTimeInMillis();
                }

                Intent ione = new Intent(ThirdActivity.this, AlaramRec.class);

                ione.setAction("com.developer.Caller.reciever.Message");
                ione.addCategory("android.intent.category.DEFAULT");
                ione.putExtra("id",(int)intentid);
                ione.putExtra("calenderalarmdate",calendrum);
                ione.putExtra("calenderuseretter",calendrumcopy);



                PendingIntent pid = PendingIntent.getBroadcast(ThirdActivity.this, (int) intentid, ione, 0);

                if (rep ==1)
                {

                    myAlarmManger.setRepeating(AlarmManager.RTC_WAKEUP,
                            calendrum, 10 * 1000
                            , pid);

                }
                else {
                    myAlarmManger.set(AlarmManager.RTC_WAKEUP,
                            calendrum
                            , pid);

                }
             /*   doctitle.setText("");
                amount.setText("");
                dd.setText("");
                nts.setText("");
                url1.setText("");
*/

                Toast.makeText(ThirdActivity.this, "Data inserted! " +priordateposition+" "+(int)intentid, Toast.LENGTH_SHORT).show();
                ProgressDialog pd = new ProgressDialog(this);
                pd.setTitle("Setting Your Alarm....");
                pd.setMessage("Please Wait....");
                pd.setMax(5);
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setCancelable(false);
                pd.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                  Intent i1 = new Intent(ThirdActivity.this,MainActivity.class);
                        startActivity(i1);
                finish();
                    }
                }, 2000);
            } else {
                Toast.makeText(ThirdActivity.this, "Data not Inserted "+alarmid, Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(ThirdActivity.this, "insert Full details!", Toast.LENGTH_SHORT).show();
        }





    }

    private byte[] ImagetoByte(ImageView img1) {
            Bitmap bitmap = ((BitmapDrawable)img1.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            return byteArray;
    }
    //--------Save Button ka CODE khatam------------//



    //--------URL Button ka CODE------------//

    public void webkholdia(View view)
    {
        Intent browserIntent  = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+url1.getText()));
        startActivity(browserIntent);
    }


    public void DefaultTimeClicke(View view) {
        Log.v("RadioButton","DefaultTimeClicke Working");

        hour_x =9;
        minute_x=0;
        Toast.makeText(this, "Default Time For Alarm @9.00 a.m", Toast.LENGTH_SHORT).show();
    }
    public void DefaultTimeClickecopy() {

        hour_x =9;
        minute_x=0;
    }

    public void CustomTimeClicke(View view) {

        Log.v("RadioButton","CustomTimeClicke Working");


        showDialog(DILOGTIME_ID);
    }


    public void dateclicker(View view) {

        showDialog(DILOG_ID);
    }

    public void NonRepeatClicke(View view) {


        rep = 0;
        repcopy = 100;
        Toast.makeText(this, "Non-Repeat Clicked"+rep, Toast.LENGTH_SHORT).show();
    }
    public void NonRepeatClickecopy(){
        rep = 0;
        repcopy = 100;
    }

    public void RepeatClicke(View view) {

        rep = 1;
        repcopy = 101;
        Toast.makeText(this, "Repeat Clicked"+rep, Toast.LENGTH_SHORT).show();
    }
}
