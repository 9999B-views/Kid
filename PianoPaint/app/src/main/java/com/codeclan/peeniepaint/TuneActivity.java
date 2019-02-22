package com.codeclan.peeniepaint;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class TuneActivity extends AppCompatActivity {

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    MediaPlayer mp;

    Button a, b, c, d, e, f, g, h;


//    soundPool is a service that manages and plays audio files for applications - it uses mediaplayer but is distinct.
    private SoundPool soundPool;

    private int sound_a, sound_b, sound_c, sound_d, sound_e, sound_f, sound_g, sound_h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tune);

        a = (Button) findViewById(R.id.a);
        b = (Button) findViewById(R.id.b);
        c = (Button) findViewById(R.id.c);
        d = (Button) findViewById(R.id.d);
        e = (Button) findViewById(R.id.e);
        f = (Button) findViewById(R.id.f);
        g = (Button) findViewById(R.id.g);
        h = (Button) findViewById(R.id.h);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder().setMaxStreams(5).build();
        } else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        sound_a = soundPool.load(this, R.raw.a2, 1);
        sound_b = soundPool.load(this, R.raw.b2, 1);
        sound_c = soundPool.load(this, R.raw.c1, 1);
        sound_d = soundPool.load(this, R.raw.d1, 1);
        sound_e = soundPool.load(this, R.raw.e1, 1);
        sound_f = soundPool.load(this, R.raw.f1, 1);
        sound_g = soundPool.load(this, R.raw.g1, 1);
        sound_h = soundPool.load(this, R.raw.c2, 1);

        a.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                soundPool.play(sound_a, 1, 1, 0, 0, 1);
            }
        });

        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                soundPool.play(sound_b, 1, 1, 0, 0, 1);
            }
        });

        c.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                soundPool.play(sound_c, 1, 1, 0, 0, 1);
            }
        });

        d.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                soundPool.play(sound_d, 1, 1, 0, 0, 1);
            }
        });

        e.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                soundPool.play(sound_e, 1, 1, 0, 0, 1);
            }
        });

        f.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                soundPool.play(sound_f, 1, 1, 0, 0, 1);
            }
        });

        g.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                soundPool.play(sound_g, 1, 1, 0, 0, 1);
            }
        });

        h.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                soundPool.play(sound_h, 1, 1, 0, 0, 1);
            }
        });

//        ObjectAnimator cloudAnim = ObjectAnimator.ofFloat(findViewById(R.id.imageView), "x", 700);
//
//        cloudAnim.setDuration(37000);
//        cloudAnim.setRepeatCount(ValueAnimator.INFINITE);
//        cloudAnim.setRepeatMode(ValueAnimator.REVERSE);
//        cloudAnim.start();


        ObjectAnimator cloudAnim2 = ObjectAnimator.ofFloat(findViewById(R.id.imageView2), "x", 800);

        cloudAnim2.setDuration(52000);
        cloudAnim2.setRepeatCount(ValueAnimator.INFINITE);
        cloudAnim2.setRepeatMode(ValueAnimator.REVERSE);
        cloudAnim2.start();

        ObjectAnimator rocketAnim = ObjectAnimator.ofFloat(findViewById(R.id.imageView3), "x", 2000);

        rocketAnim.setDuration(24000);
        rocketAnim.setRepeatCount(ValueAnimator.INFINITE);
        rocketAnim.setRepeatMode(ValueAnimator.RESTART);
//        rocketAnim.setRepeatMode(ValueAnimator.REVERSE);
        rocketAnim.start();

//        mp = MediaPlayer.create(getApplicationContext(), R.raw.heartandsoul);
//
//        mp.start();
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


    protected void onPause() {
        super.onPause();

        if (mp != null && mp.isPlaying()) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

}

