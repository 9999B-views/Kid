package com.example.nandhu.alphabets;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
private MediaPlayer mediaPlayer;

    private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
      releaseAudio();
        }
    };
    private AudioManager audioManager;
    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ){
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            }else if(focusChange == AudioManager.AUDIOFOCUS_LOSS){
                mediaPlayer.release();
            }else if(focusChange == AudioManager.AUDIOFOCUS_GAIN){
                mediaPlayer.start();
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_action_logomakr_0flcbp);
        final ArrayList<Alphabets> alphabets = new ArrayList<>();
        alphabets.add(new Alphabets(R.drawable.a,"Apple",R.raw.a));
        alphabets.add(new Alphabets(R.drawable.b,"Baseball",R.raw.b));
        alphabets.add(new Alphabets(R.drawable.c,"Clock",R.raw.c));
        alphabets.add(new Alphabets(R.drawable.d,"Donkey",R.raw.d));
        alphabets.add(new Alphabets(R.drawable.e,"Elephant",R.raw.e));
        alphabets.add(new Alphabets(R.drawable.f,"Father",R.raw.f));
        alphabets.add(new Alphabets(R.drawable.g,"Grandmother",R.raw.g));
        alphabets.add(new Alphabets(R.drawable.h,"Hungry",R.raw.h));
        alphabets.add(new Alphabets(R.drawable.i,"Internet",R.raw.i));
        alphabets.add(new Alphabets(R.drawable.j,"Justice",R.raw.j));
        alphabets.add(new Alphabets(R.drawable.k,"Kangoroo",R.raw.k));
        alphabets.add(new Alphabets(R.drawable.l,"London",R.raw.l));
        alphabets.add(new Alphabets(R.drawable.m,"Monkey",R.raw.m));
        alphabets.add(new Alphabets(R.drawable.n,"Norway",R.raw.n));
        alphabets.add(new Alphabets(R.drawable.o,"Overtime",R.raw.o));
        alphabets.add(new Alphabets(R.drawable.p,"Pillo",R.raw.p));
        alphabets.add(new Alphabets(R.drawable.q,"question",R.raw.q));
        alphabets.add(new Alphabets(R.drawable.r,"Rabbit",R.raw.r));
        alphabets.add(new Alphabets(R.drawable.s,"Superman",R.raw.s));
        alphabets.add(new Alphabets(R.drawable.t,"Telephone",R.raw.t));
        alphabets.add(new Alphabets(R.drawable.u,"Underware",R.raw.u));
        alphabets.add(new Alphabets(R.drawable.v,"vaccinate",R.raw.v));
        alphabets.add(new Alphabets(R.drawable.w,"Worldwideweb",R.raw.w));
        alphabets.add(new Alphabets(R.drawable.x,"Xylophone",R.raw.x));
        alphabets.add(new Alphabets(R.drawable.y,"Yogurt",R.raw.y));
        alphabets.add(new Alphabets(R.drawable.z,"Zebra",R.raw.z));



        AlphabetsAdapter adapter = new AlphabetsAdapter(this,alphabets);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Alphabets audio = alphabets.get(position);
        int recievedAudio = audioManager.requestAudioFocus(onAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        if(recievedAudio == audioManager.AUDIOFOCUS_REQUEST_GRANTED){
            releaseAudio();
        }
        mediaPlayer = MediaPlayer.create(MainActivity.this,audio.getAudio());
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(completionListener);
    }
});

    }

    @Override
    protected void onStop() {
        releaseAudio();
        super.onStop();
    }

    private void releaseAudio(){
        if(mediaPlayer!=null){
            mediaPlayer.release();
        }
        mediaPlayer= null;
        audioManager.abandonAudioFocus(onAudioFocusChangeListener);
    }
}
