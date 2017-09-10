package com.example.ashutosh.testrreminderui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Ashutosh on 5/31/2017.
 */

public class EditSettings extends Fragment  {

    GoogleAccountCredential credential;
    DriveId mDriveId;

    public static GoogleApiClient mGoogleApiClient;

    public static final String PACKAGE_NAME = "com.example.ashutosh.testrreminderui";
    public static final String DATABASE_NAME = "Taskyermania.db";


    private static final String TAG = "drive_Humanity";
    private static final int REQUEST_CODE_RESOLUTION = 3;
    public static DriveFile mfileID;

    private static final String DATABASE_PATH = "/data/data/" + PACKAGE_NAME + "/databases/" + DATABASE_NAME;
    private static File DATA_DIRECTORY_DATABASE =
            new File(Environment.getDataDirectory() + "/data/" + PACKAGE_NAME + "/databases/" + DATABASE_NAME);
    private static final String MIME_TYPE = "application/x-sqlite-3";





    TextView watch;

    public EditSettings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewsa = inflater.inflate(R.layout.fragment_editsettings, container, false);


        watch = (TextView)viewsa.findViewById(R.id.textView4);

        Button button2 = (Button)viewsa.findViewById(R.id.button2);
        Button button3 = (Button)viewsa.findViewById(R.id.button3);



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
        Log.i("cream1", "cream2");
        doDriveBackup();
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("BackupStarted");
        pd.setMessage("Please Wait Few Seconds");
        pd.setMax(5);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pd.cancel();
                Toast.makeText(getContext(), "Backup Completed!", Toast.LENGTH_SHORT).show();
                Log.v("Backup","Completed");
            }
        }, 5000);


    }

    public void stopThis(View view) {

      try {
          restoreDriveBackup();
      }catch(Exception e)
        {

            Toast.makeText(getContext(), "Database not Found on Drive!", Toast.LENGTH_SHORT).show();
        }


    }
    public  void doDriveBackup () {
        Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(backupContentsCallback);
    }




     final private ResultCallback<DriveApi.DriveContentsResult> backupContentsCallback = new ResultCallback<DriveApi.DriveContentsResult>() {

        @Override
        public void onResult(DriveApi.DriveContentsResult result) {
            if (!result.getStatus().isSuccess()) {
                return;
            }


            String mimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType("db");
            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                    .setTitle(DATABASE_NAME) // Google Drive File name
                    .setMimeType(mimeType)
                    .setStarred(true).build();



            // create a file on root folder
            Drive.DriveApi.getRootFolder(mGoogleApiClient)
                    .createFile(mGoogleApiClient, changeSet, result.getDriveContents())
                    .setResultCallback(backupFileCallback);


        }
    };
    //--****  new custom implementation
    final private ResultCallback<DriveFolder.DriveFileResult> checks = new ResultCallback<DriveFolder.DriveFileResult>() {
        @Override
        public void onResult(@NonNull DriveFolder.DriveFileResult driveFileResult) {
            Query query = new Query.Builder()
                    .addFilter(Filters.eq(SearchableField.TITLE, DATABASE_NAME))
                    .build();
            Drive.DriveApi.query(mGoogleApiClient,query);
            Log.v("",""+query.getFilter().toString());
            mfileID = driveFileResult.getDriveFile();
        }
    };
//--***********
     final private ResultCallback<DriveFolder.DriveFileResult> backupFileCallback = new ResultCallback<DriveFolder.DriveFileResult>() {
        @Override
        public void onResult(DriveFolder.DriveFileResult result) {
            if (!result.getStatus().isSuccess()) {
                return;
            }
            final DriveId gd = null;
            Query query = new Query.Builder()
                    .addFilter(Filters.eq(SearchableField.TITLE, DATABASE_NAME))
                    .build();
            Drive.DriveApi.query(mGoogleApiClient,query);

            Log.v("",""+query.getFilter().toString());
            mfileID = result.getDriveFile();
            mfileID.open(mGoogleApiClient, DriveFile.MODE_WRITE_ONLY, new DriveFile.DownloadProgressListener() {
                @Override
                public void onProgress(long bytesDownloaded, long bytesExpected) {
                        Log.v("LalaLajpatRai","Downloading... (" + bytesDownloaded + "/" + bytesExpected + ")");
                    Toast.makeText(getContext().getApplicationContext(), "dd", Toast.LENGTH_SHORT).show();
                }
            }).setResultCallback(backupContentsOpenedCallback);
        }
    };

    static final private ResultCallback<DriveApi.DriveContentsResult> backupContentsOpenedCallback = new ResultCallback<DriveApi.DriveContentsResult>() {
        @Override
        public void onResult(DriveApi.DriveContentsResult result) {
            if (!result.getStatus().isSuccess()) {
                return;
            }


            DriveContents contents = result.getDriveContents();
            BufferedOutputStream bos = new BufferedOutputStream(contents.getOutputStream());
            byte[] buffer = new byte[1024];
            int n;


            try {
                FileInputStream is = new FileInputStream(DATA_DIRECTORY_DATABASE);
                BufferedInputStream bis = new BufferedInputStream(is);

                while( ( n = bis.read(buffer) ) > 0 ) {
                    bos.write(buffer, 0, n);
                }
                bos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            contents.commit(mGoogleApiClient, null).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {

                }
            });
        }
    };




    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addApi(Drive.API).addScope(Drive.SCOPE_FILE).addConnectionCallbacks(MainActivity.ma).addOnConnectionFailedListener(MainActivity.ma).build();
        }
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }





    public  void restoreDriveBackup() {
        final DriveId[] md = new DriveId[1];
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, DATABASE_NAME))
                .build();

        Drive.DriveApi.query(mGoogleApiClient, query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
            @Override
            public void onResult(DriveApi.MetadataBufferResult metadataBufferResult) {

            /*for(int i = 0 ;i < metadataBufferResult.getMetadataBuffer().getCount() ;i++) {
                debug("got index "+i);
                debug("filesize in cloud "+ metadataBufferResult.getMetadataBuffer().get(i).getFileSize());
                debug("driveId(1): "+ metadataBufferResult.getMetadataBuffer().get(i).getDriveId());
            }*/


                int count = metadataBufferResult.getMetadataBuffer().getCount() - 1;
                //debug("Count: " + count);
                md[0] = metadataBufferResult.getMetadataBuffer().get(0).getDriveId();
                //debug("driveId: " + driveId);
                metadataBufferResult.getMetadataBuffer().release();

                mfileID = md[0].asDriveFile();

//mfileID.getDriveId().asDriveFile();
                mfileID.open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, new DriveFile.DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDown, long bytesExpected) {


                        ProgressDialog pd = new ProgressDialog(getContext());
                       pd.setTitle("Restoring");
                        pd.setMessage("Please Wait Few Seconds");
                        pd.setMax(5);
                        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        pd.setCancelable(false);
                        pd.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i1 = new Intent(getContext(),MainActivity.class);
                                i1.putExtra("restorereferal",1);

                                startActivity(i1);

                            }
                        }, 5000);


                    }
                })
                        .setResultCallback(restoreContentsCallback);
            }
        });
    }



    static final private ResultCallback<DriveApi.DriveContentsResult> restoreContentsCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.v("hey","Unable to retreive");
                        return;
                    }



                    if ( !DATA_DIRECTORY_DATABASE.exists())
                        DATA_DIRECTORY_DATABASE.delete();

                    DATA_DIRECTORY_DATABASE = new File(String.valueOf(DATA_DIRECTORY_DATABASE));

                    DriveContents contents = result.getDriveContents();
                    //debug("driveId:(2)" + contents.getDriveId());

                    try {
                        FileOutputStream fos = new FileOutputStream(DATA_DIRECTORY_DATABASE);
                        BufferedOutputStream bos = new BufferedOutputStream(fos);
                        BufferedInputStream in = new BufferedInputStream(contents.getInputStream());

                        byte[] buffer = new byte[1024];
                        int n, cnt = 0;


                        //debug("before read " + in.available());

                        while( ( n = in.read(buffer) ) > 0) {
                            bos.write(buffer, 0, n);
                            cnt += n;

                            bos.flush();
                        }

                        //debug(" read done: " + cnt);

                        bos.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    contents.discard(mGoogleApiClient);

                }
            };

}

