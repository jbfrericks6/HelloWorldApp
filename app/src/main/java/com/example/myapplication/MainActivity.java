package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// TextView textView1 = new TextView("hello");
        final Button button2;
        button2 = findViewById(R.id.button1);
        final Button capsButton = findViewById(R.id.capsButton);
        final TextView tv;
        tv = findViewById(R.id.textView);
        tv.setVisibility(View.INVISIBLE);
        capsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                tv.setAllCaps(true);
            }

        });

        button2.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                if(tv.getVisibility() == View.INVISIBLE) {
                    tv.setVisibility(View.VISIBLE);
                }else {
                    tv.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

}
