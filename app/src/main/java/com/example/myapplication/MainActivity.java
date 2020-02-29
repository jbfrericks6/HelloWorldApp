package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static android.Manifest.permission.RECORD_AUDIO;

public class MainActivity extends AppCompatActivity {

   Button btnRecord, btnStopRecord, btnPlay, btnStop;
   MediaRecorder mediaRecorder;
   MediaPlayer mediaPlayer;
   File audioDirTemp = new File(Environment.getExternalStorageDirectory(), "Audiotape");


   final int REQUEST_PERMISSION_CODE = 1000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!checkPermissionFromDevice()) {
            requestPermission();
        }

        btnPlay = findViewById(R.id.btnPlay);
        btnRecord = findViewById(R.id.btnStartRecord);
        btnStop = findViewById(R.id.btnStop);
        btnStopRecord = findViewById(R.id.btnStopRecord);

        btnRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkPermissionFromDevice()) {
                        setupMediaRecorder();
                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    btnPlay.setEnabled(false);
                    btnStop.setEnabled(false);

                    Toast.makeText(MainActivity.this, "Recording...", Toast.LENGTH_SHORT).show();
                    } else {
                        requestPermission();
                    }
                }
            });

        btnStopRecord.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               mediaRecorder.stop();
               btnStopRecord.setEnabled(false);
               btnPlay.setEnabled(true);
               btnRecord.setEnabled(true);
               btnStop.setEnabled(false);
            }

       });

        btnPlay.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              btnStop.setEnabled(true);
              btnStopRecord.setEnabled(false);
              btnRecord.setEnabled(false);
              mediaPlayer = new MediaPlayer();
              try {
                  mediaPlayer.setDataSource(audioDirTemp + "/audio_file"
                          + ".mp3");
                  mediaPlayer.prepare();
              } catch (IOException e) {
                  e.printStackTrace();
              }
              mediaPlayer.start();
              Toast.makeText(MainActivity.this, "Playing...", Toast.LENGTH_SHORT).show();
          }
        });

      btnStop.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              btnStopRecord.setEnabled(false);
              btnRecord.setEnabled(true);
              btnStop.setEnabled(false);
              btnPlay.setEnabled(true);

              if(mediaPlayer != null) {
                  mediaPlayer.stop();
                  mediaPlayer.release();
                  setupMediaRecorder();
              }
          }
      });
    }

    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(audioDirTemp + "/audio_file"
                + ".mp3");
    }

    private boolean checkPermissionFromDevice() {
        int write_external_storage_result  =
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestPermission() {
           ActivityCompat.requestPermissions(this, new String[]{
                   Manifest.permission.WRITE_EXTERNAL_STORAGE,
           RECORD_AUDIO}, REQUEST_PERMISSION_CODE);
    }
}

