package vn.devpro.devprokidorigin.activities.game.dapbongbay.game;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

import vn.devpro.devprokidorigin.activities.game.dapbongbay.sound.GameEvent;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.sound.SoundManager;


public class BalloonsSoundManager extends SoundManager {

    public BalloonsSoundManager(Context context) {
        super(context);
    }

    protected void loadEventSounds(HashMap<GameEvent, Integer> mSoundsMap) {

        loadEventSound(mContext, BalloonGameEvent.BalloonHit,"sound.wav");
    }

    @Override
    protected String getMusicFileAssetPath() {
        return "sfx/Dean_Caedab_-_Everyday_Success.mp3";
    }
}
