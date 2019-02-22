package vn.devpro.devprokidorigin.activities.game.dapbongbay.sound;

import java.io.IOException;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.utils.Global;

public abstract class SoundManager {

    private static final int MAX_STREAMS = 10;
    private static final float DEFAULT_MUSIC_VOLUME = 0.6f;
    private static final String MUSIC_PREF_KEY = "music";
    private HashMap<GameEvent, Integer> mSoundsMap;

    protected Context mContext;
    private SoundPool mSoundPool;
    private boolean mMusicEnabled;

    private MediaPlayer mBgPlayer;

    public SoundManager(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        mMusicEnabled = prefs.getBoolean(MUSIC_PREF_KEY, true);
        mContext = context;
        loadIfNeeded();
    }


    protected void loadEventSound(Context context, GameEvent event, String... filename) {
//		mSoundsMap.put(event,new SoundInfo(context, mSoundPool, filename));
        try {
            AssetFileDescriptor descriptor = context.getAssets().openFd("sfx/" + filename[0]);
            int soundId = mSoundPool.load(descriptor, 1);
            mSoundsMap.put(event, soundId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("TAGG", "loadEventSound :--------->" + filename[0]);
    }

    public void playSoundForGameEvent(GameEvent event) {
        Integer soundId = mSoundsMap.get(event);
        if (soundId != null) {
            mSoundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }


    private void loadIfNeeded() {
        loadSounds();
        if (mMusicEnabled) {
            loadMusic();
        }
    }

    private void loadSounds() {
        createSoundPool();
        mSoundsMap = new HashMap<GameEvent, Integer>();
        loadEventSounds(mSoundsMap);
    }

    protected abstract void loadEventSounds(HashMap<GameEvent, Integer> mSoundsMap);

    private void createSoundPool() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        } else {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            mSoundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(MAX_STREAMS)
                    .build();
        }
    }

    private void loadMusic() {
        try {
            mBgPlayer = new MediaPlayer();
            AssetFileDescriptor afd = mContext.getAssets().openFd(getMusicFileAssetPath());
            mBgPlayer.setDataSource(afd.getFileDescriptor(),
                    afd.getStartOffset(), afd.getLength());
            mBgPlayer.setLooping(true);
            mBgPlayer.setVolume(0.04f, 0.04f);
            mBgPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract String getMusicFileAssetPath();

    public void pauseBgMusic() {
        if (mMusicEnabled) {
            mBgPlayer.pause();
        }
    }

    public void resumeBgMusic() {
        if (mMusicEnabled) {
            mBgPlayer.start();
        }
    }

    private void unloadMusic() {
        mBgPlayer.stop();
        mBgPlayer.release();
    }

    private void unloadSounds() {
        mSoundPool.release();
        mSoundPool = null;
        mSoundsMap.clear();
    }

    public void toggleMusicStatus() {
        mMusicEnabled = !mMusicEnabled;
        if (mMusicEnabled) {
            loadMusic();
            resumeBgMusic();
        } else {
            unloadMusic();
        }
        PreferenceManager.getDefaultSharedPreferences(mContext).edit()
                .putBoolean(MUSIC_PREF_KEY, mMusicEnabled)
                .commit();
    }

    public boolean getMusicStatus() {
        return mMusicEnabled;
    }

}
