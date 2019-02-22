package vn.devpro.devprokidorigin.activities.game.latbai.commom;

import android.media.MediaPlayer;

import vn.devpro.devprokidorigin.R;


/**
 * Created by admin on 4/2/2018.
 */

public class Music {
    public static boolean OFF = false;

    public static void playCorrent() {

        if (!OFF) {
            MediaPlayer mp = MediaPlayer.create(Shared.context, R.raw.level_up);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    mp.release();
                    mp = null;
                }

            });
            mp.start();
        }
    }

    public static void playBackgroundMusic() {
//          làm gì voiws background music bây giờ ? liệu có cần nhạc nền không ? đó là 1 câu hỏi hay

    }

    public static void showStar() {
        if (!OFF) {
            MediaPlayer mp = MediaPlayer.create(Shared.context, R.raw.ringring);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    mp.release();
                    mp = null;
                }

            });
            mp.start();
        }
    }
}
