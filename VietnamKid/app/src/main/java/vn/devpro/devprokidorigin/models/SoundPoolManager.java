package vn.devpro.devprokidorigin.models;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;



/**
 * Created by Administrator on 3/13/2018.
 */

public class SoundPoolManager {
    private static SoundPoolManager manager;

    public static SoundPoolManager getInstance() {
        if (manager == null) {
            manager = new SoundPoolManager();
        }
        return manager;
    }

    private SoundPool soundPool;
    private int currentSoundId = -1;

    public SoundPoolManager() {
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    }

    public void soundBtClicked(Context context) {
        manager.soundBtClicked(context);
    }

    public void play(Context context, int resId, SoundPool.OnLoadCompleteListener listener) {
        if (currentSoundId != -1) {
            soundPool.stop(currentSoundId);
        }
        soundPool.load(context.getApplicationContext(), resId, 0);
        soundPool.play(currentSoundId, 1, 1, 1, 0, 1);
        soundPool.setOnLoadCompleteListener(listener);
    }
}