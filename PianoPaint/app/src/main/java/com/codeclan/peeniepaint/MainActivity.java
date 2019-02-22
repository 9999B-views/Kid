package com.codeclan.peeniepaint;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button drawButton;
    Button tuneButton;

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawButton = (Button)findViewById(R.id.draw);
        tuneButton = (Button)findViewById(R.id.tune);

        mp = MediaPlayer.create(getApplicationContext(), R.raw.heartandsoul);

        mp.setLooping(true);
        mp.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp != null && mp.isPlaying()) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    protected void onPause() {
        super.onPause();

        if (mp != null && mp.isPlaying()) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }



    public void onDrawButtonClicked(View view) {
        Intent intent = new Intent(this, DrawActivity.class);
        startActivity(intent);
    }

    public void onTuneButtonClicked(View view) {
        Intent intent = new Intent(this, TuneActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(getBaseContext(), "Tap BACK button to exit", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }





}