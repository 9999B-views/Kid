package vn.devpro.devprokidorigin.models;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.widget.Button;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.utils.Global;

public class PlaybgSound {
    public static MediaPlayer mediaPlayer;
    private static SoundPool soundPool;
    public static boolean isplayingAudio = false;

    public static void playAudio(Context c, int resID) {
        mediaPlayer = MediaPlayer.create(c, resID);
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        if (!mediaPlayer.isPlaying()) {
            isplayingAudio = true;
            mediaPlayer.isLooping();
            mediaPlayer.start();
        }
    }

    public static void pauseAudio() {
        mediaPlayer.pause();
    }

    public static void stopAudio() {
        isplayingAudio = false;
        mediaPlayer.stop();
    }

    public static void changeStatusBgSound(Button button) {
        if (PlaybgSound.mediaPlayer.isPlaying()) {
            PlaybgSound.stopAudio();
            Global.changeSoundState();
            button.setBackgroundResource(R.drawable.soundoff);
            Log.i("TAG", "Da click sound, stop media dang chay");
            return;
        } else {
            if (!Global.getIsMute()) {
                PlaybgSound.mediaPlayer.stop();
                Global.updateButtonSound(button);
                Global.changeSoundState();
                Log.i("TAG", "Da click sound, khong co media nao chay,stop play ");
            } else {
                PlaybgSound.playAudio(Global.mContext, R.raw.bg_sound);
                Global.updateButtonSound(button);
                Global.changeSoundState();
                Log.i("TAG", "Da click sound, khong co media nao chay, play bg ");
            }
        }
    }

    public static void playBgSoundGame(Context context, int resID) {
        mediaPlayer = MediaPlayer.create(context, resID);
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        if (!mediaPlayer.isPlaying()) {
            isplayingAudio = true;
            mediaPlayer.start();
        }
    }
}
