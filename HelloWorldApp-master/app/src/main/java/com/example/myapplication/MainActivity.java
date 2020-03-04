package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button startRecord, stopRecord;
    String save = "";
    MediaRecorder audioRecorder;

    final int REQUEST_PERMISSION_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!checkPermissionFromDevice()) {
            requestPermission();
        }
// TextView textView1 = new TextView("hello");
        final Button button2;
        button2 = findViewById(R.id.button1);
        final Button capsButton = findViewById(R.id.capsButton);
        final TextView tv;
        tv = findViewById(R.id.textView);
        tv.setVisibility(View.INVISIBLE);
        capsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tv.setAllCaps(true);
                if (tv.getText() == "123456789") {
                    tv.setText("Hello World");
                } else {
                    tv.setText("123456789");
                }
            }

        });

        button2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (tv.getVisibility() == View.INVISIBLE) {
                    tv.setVisibility(View.VISIBLE);
                } else {
                    tv.setVisibility(View.INVISIBLE);
                }
            }
        });

        startRecord = (Button)findViewById(R.id.startRecord);
        stopRecord = (Button)findViewById(R.id.stopRecord);




        startRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermissionFromDevice())

                {

                    save = Environment.getExternalStorageDirectory()
                            .getAbsolutePath()+"/" + UUID.randomUUID().toString()+"_app_Recording.3gp";
                    setupAudioRecorder();
                    try {
                        audioRecorder.prepare();
                        audioRecorder.start();
                    }
                    catch(IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(MainActivity.this, "Recording", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    requestPermission();
                }
            }
        });

        stopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioRecorder.stop();
                stopRecord.setEnabled(false);
                startRecord.setEnabled(true);
                setupAudioRecorder();

            }
        });



    }




    private void setupAudioRecorder() {
        audioRecorder = new MediaRecorder();
        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        audioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        audioRecorder.setOutputFile(save);
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(this,new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO},REQUEST_PERMISSION_CODE);
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case REQUEST_PERMISSION_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkPermissionFromDevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;

    }

}
