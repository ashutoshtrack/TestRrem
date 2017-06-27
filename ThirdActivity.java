package com.example.ashutosh.testrreminderui;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

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
    Spinner dynamicSpinner;
    ArrayAdapter<CharSequence> adapter;
    ImageButton btn,but;
    public static final int REQUEST_CAPTURE= 1;
    ImageView img1;
    Button btnAddwa,cancu;
    //AlarmManager myAlarmManager;

    int year_x,month_x,day_x;
    static final int DILOG_ID = 0;
   // DatabaseHelper myDB;

    private static  final  int PICK_IMAGES = 100;
    Uri imageuri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

       // myDB = new DatabaseHelper(this);
        tambura = this;

        final Calendar tarik = Calendar.getInstance();
        year_x =  tarik.get(Calendar.YEAR);
        month_x = tarik.get(Calendar.MONTH);
        day_x = tarik.get(Calendar.DAY_OF_MONTH);

        dd = (EditText) findViewById(R.id.dateset);
        nts = (EditText) findViewById(R.id.notes);
        doctitle = (EditText) findViewById(R.id.doctitle);
        amount = (EditText) findViewById(R.id.amount);

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

       /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);

        dynamicSpinner.setAdapter(adapter); */

        adapter = ArrayAdapter.createFromResource(this, R.array.due_dates, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dynamicSpinner.setAdapter(adapter);



        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String pawar = parent.getItemAtPosition(position).toString();

                    bund = new Bundle();
                bund.putString("key1",pawar);

                Toast.makeText(ThirdActivity.this, "You have selected"+parent.getItemAtPosition(position) , Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //AlaramManager
       // myAlarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);




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
                try {

                    String sharad = bund.getString("key1");

                    boolean isInserted = MainActivity.ma.myDB.insertData(doctitle.getText().toString(), amount.getText().toString(), ImagetoByte(img1), dd.getText().toString(), sharad, nts.getText().toString(), url1.getText().toString());
                    if (isInserted == true) {


                    //  String encodedImage = Base64.encodeToString(this.ImagetoByte(img1), Base64.DEFAULT);



                        String dockyname = doctitle.getText().toString();
                        String dockyamt = amount.getText().toString();

                        SharedPreferences sh = getSharedPreferences("Mojar", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sh.edit();

                        myEdit.putString("user",dockyname);
                        myEdit.putString("amount",dockyamt);
                        myEdit.putString("endcoder",encodeTobase64(img1));

                        myEdit.apply();


                        Intent intent = new Intent(ThirdActivity.this, AlaramRec.class);

                        intent.setAction("com.developer.Caller.reciever.Message");
                        intent.addCategory("android.intent.category.DEFAULT");

                        PendingIntent pid = PendingIntent.getBroadcast(ThirdActivity.this, 0, intent, 0);



                        MainActivity.ma.myAlarmManager.set(AlarmManager.RTC_WAKEUP,
                                System.currentTimeMillis()+
                                        10 * 1000, pid);


                            doctitle.setText("");
                        amount.setText("");
                            dd.setText("");
                        nts.setText("");
                        url1.setText("");




                        Toast.makeText(ThirdActivity.this, "Data inserted!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ThirdActivity.this, "Data not Inserted", Toast.LENGTH_SHORT).show();
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

            public  String encodeTobase64(ImageView img1) {
                Bitmap imagere = ((BitmapDrawable)img1.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imagere.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] b = baos.toByteArray();
                String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

                Log.d("Image Log:", imageEncoded);
                return imageEncoded;
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

        final CharSequence[] items = {"Camera","Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ThirdActivity.this);

        builder.setTitle("Add document image");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(items[which].equals("Camera")){
                    Toast.makeText(ThirdActivity.this, "Camera", Toast.LENGTH_SHORT).show();
                    Intent e = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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




             if(requestCode==REQUEST_CAPTURE){
                Bundle extras = data.getExtras();
                Bitmap photo = (Bitmap)extras.get("data");
                img1.setImageBitmap(photo);
                Log.i("sat1","completed1");
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




    //--------Camera API ka CODE khatam------------//

    //--------Date Picker ka CODE------------//


    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == DILOG_ID)
            return new DatePickerDialog(this,dpickerListner,year_x,month_x,day_x);

        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x=year;
            month_x= month + 1;
            day_x=dayOfMonth;

            Toast.makeText(ThirdActivity.this,"Ye Date tune dala "+day_x +"/"+ month_x +"/" + year_x,Toast.LENGTH_LONG).show();

            dd.setText(day_x +"/"+ month_x +"/" + year_x);
        }
    };
    //--------Date Picker ka CODE khatam------------//


    //--------Save Button ka CODE------------//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.savey,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void savekar(MenuItem item) {

        try {

            String sharad = bund.getString("key1");

            boolean isInserted = MainActivity.ma.myDB.insertData(doctitle.getText().toString(), amount.getText().toString(), ImagetoByte(img1), dd.getText().toString(), sharad, nts.getText().toString(), url1.getText().toString());




            if (isInserted == true) {
                Bitmap bitmape = ((BitmapDrawable)img1.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmape.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                //  String encodedImage = Base64.encodeToString(this.ImagetoByte(img1), Base64.DEFAULT);



                String dockyname = doctitle.getText().toString();
                String dockyamt = amount.getText().toString();

                SharedPreferences sh = getSharedPreferences("Mojar", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sh.edit();

                myEdit.putString("user",dockyname);
                myEdit.putString("amount",dockyamt);
                // myEdit.putString("endcoder",encodedImage);


                myEdit.apply();


                Intent intent = new Intent(ThirdActivity.this, AlaramRec.class);

                intent.setAction("com.developer.Caller.reciever.Message");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.putExtra("BitmapImage", bitmape);
                intent.putExtra("namer",dockyname);

                PendingIntent pid = PendingIntent.getBroadcast(ThirdActivity.this, 0, intent, 0);



                MainActivity.ma.myAlarmManager.set(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis()+
                                10 * 1000, pid);


                doctitle.setText("");
                amount.setText("");
                dd.setText("");
                nts.setText("");
                url1.setText("");




                Toast.makeText(ThirdActivity.this, "Data inserted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ThirdActivity.this, "Data not Inserted", Toast.LENGTH_SHORT).show();
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



}
