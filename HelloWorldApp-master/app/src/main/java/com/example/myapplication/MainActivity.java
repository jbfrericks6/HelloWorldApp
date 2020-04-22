package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.SilenceDetector;

public class MainActivity extends AppCompatActivity {




    /*Button startRecord, stopRecord;       //Old recording button
    String save = "";
    MediaRecorder audioRecorder;*/

    final int REQUEST_PERMISSION_CODE = 1000;

    //TextView pitchText;
    TextView noteText;                      //Hold the textview for frequency and loudness
    TextView loudText;



    AudioDispatcher dispatcher;
    int On = 0;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);



            dispatcher =
                    AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);           //Using Tarsos dispatcher
                                                                                                      //Pitchhandler class
            PitchDetectionHandler pdh = new PitchDetectionHandler() {
                @Override
                public void handlePitch(PitchDetectionResult res, AudioEvent e) {
                    final float pitchInHz = res.getPitch();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            processPitch(pitchInHz);
                        }
                    });
                }
            };
            AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
            dispatcher.addAudioProcessor(pitchProcessor);


            Thread audioThread = new Thread(dispatcher, "Audio Thread");                        //Thread will be used for loudness and pitch
            audioThread.start();                                                                      //Replaces Mediarecorder

            //super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);






            //pitchText = (TextView) findViewById(R.id.pitchText);
            noteText = (TextView) findViewById(R.id.noteText);                                       //Find their xml ids
            loudText = (TextView) findViewById(R.id.loudText);

        Button Start  = findViewById(R.id.start);                                                   //Find their xml ids
        Button Stop  = findViewById(R.id.stopID);

        //pitchText.setVisibility(View.GONE);
        noteText.setVisibility(View.GONE);                                                          //Start/stop usage by hiding
        loudText.setVisibility(View.GONE);

        Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                                             //button click actions
                //pitchText.setVisibility(View.GONE);
                noteText.setVisibility(View.GONE);
                loudText.setVisibility(View.GONE);
            }
        });

        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pitchText.setVisibility(View.VISIBLE);
                noteText.setVisibility(View.VISIBLE);
                loudText.setVisibility(View.VISIBLE);
            }
        });


    }



    SilenceDetector silenceDetector;                                                               //From library handles decibel conversions


    public void processPitch(float pitchInHz) {                                                 //Text displaying for frequency and loudness
                                                                                                //As well as respective conditions
        //pitchText.setText("" + pitchInHz);                //Old allowed continous pitch display

        if(pitchInHz >= 375 && pitchInHz < 425) {
            //A
            noteText.setText("Frequency 400 Found");
        }
        else if(pitchInHz >= 575 && pitchInHz < 625) {
            //B
            noteText.setText("Frequency 600 Found");
        }
        else if(pitchInHz >= 775 && pitchInHz < 825) {
            //C
            noteText.setText("Frequency 800 Found");
        }
        else{
            noteText.setText("Searching for frequency...");
        }

        if(silenceDetector == null){
             silenceDetector= new SilenceDetector();
            dispatcher.addAudioProcessor(silenceDetector);
//            silenceDetector.process(new AudioEvent(){})
        }
        Log.d("DECI::::", "" + silenceDetector.currentSPL());
        double decibel = silenceDetector.currentSPL();
        if(decibel >= -70)
        {
            loudText.setText("3");
        }
        else if(decibel >= -80 && decibel < -70)
        {
            loudText.setText("2");
        }
        else if(decibel < -80)
        {
            loudText.setText("1");
        }

        //loudText.setText(""+silenceDetector.currentSPL());                    //Old allowed continuous DB display




    }

    //Permissions of storage and mic

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
