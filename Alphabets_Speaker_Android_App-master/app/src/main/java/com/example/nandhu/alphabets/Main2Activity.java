package com.example.nandhu.alphabets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Thread thread = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(1500);
                    Intent intent = new Intent(getApplication(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
