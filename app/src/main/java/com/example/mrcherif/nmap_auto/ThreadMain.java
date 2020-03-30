package com.example.mrcherif.nmap_auto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ThreadMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_main);
        Thread background = new Thread() {
            public void run() {
                try {

                    sleep(5*1000);


                    Intent i=new Intent(getBaseContext(),MainActivity.class);
                    startActivity(i);


                    finish();
                } catch (Exception e) {
                }
            }
        };

        background.start();
    }
}
