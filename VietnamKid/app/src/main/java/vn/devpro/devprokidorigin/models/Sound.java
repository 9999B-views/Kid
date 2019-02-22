package vn.devpro.devprokidorigin.models;

/**
 * Created by hoang-ubuntu on 03/04/2018.
 */

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.activities.game.latbai.commom.Shared;
import vn.devpro.devprokidorigin.interfaces.MyCompleteSound;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.utils.Utils;

/**
 * Created by Administrator on 3/13/2018.
 */

public class Sound {
    private static final String TAG = "SOUND_TAG";
    private static final String GUARD = "GUARD";
    private static final String MP3 = ".mp3";
    private static boolean preparing = false;
    public static int PIECE_PICK_UP = 125;
    public static int PIECE_LAY_DOWN = 126;
    public static int PIECE_CORRECT = 127;
    public static int PUZZLE_DONE = 128;
    public static int PUZZLE_DONE2 = 129;
    private String currName = "";
    private String currTempName = "";
    private static Sound manager;
    private boolean same = false;
    private Context mContext;
    private Uri mUri;
    private MyCompleteSound myCompleteSound = null;
    private int count;

    public void setMyCompleteSound(MyCompleteSound myCompleteSound) {
        this.myCompleteSound = myCompleteSound;
    }

    private Sound(Context mContext) {
        this.mContext = mContext;
    }

    public static Sound getInstance(Context context) {
        if (manager == null) {
            manager = new Sound(context);
        }
        return manager;
    }

    private MediaPlayer mediaPlayer = null;
    private MediaPlayer bgMedia = null;

    private MediaPlayer bgSound = null;

    private MediaPlayer pickUp = null;
    private MediaPlayer layDown = null;
    private MediaPlayer pieceCorrect = null;
    private MediaPlayer puzzleDone = null;
    private MediaPlayer puzzleDone2 = null;

    private String currFile2 = "";

    private void makeTempFileAndPlay(byte[] mp3SoundByteArray, String realName) {
        if (mp3SoundByteArray == null || realName.isEmpty() || realName == null) {
            return;
        }
        try {
            realName = realName + "aa";
            File tempMp3 = File.createTempFile(realName, MP3, Global.pathCache.getCanonicalFile());
            if (!tempMp3.exists() || tempMp3 == null) {
                return;
            }
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(mp3SoundByteArray);
            fos.close();
            String fileNameWithOutExt = tempMp3.getName().replaceFirst("[.][^.]+$", "");
            if (!realName.equals(currFile2)) {
                File f = new File(Global.pathCache.getCanonicalFile() + File.separator + currTempName + MP3);
                if (f.exists()) {
                    f.delete();
                } else {
                    Log.e(TAG, "makeTempFileAndPlay---->xoa file tam cu: file ko ton tai ko xoa dc");
                }
                //f.deleteOnExit();
                currFile2 = realName;
            }
            currTempName = fileNameWithOutExt;
            tempMp3.deleteOnExit();
            Log.i(TAG, "makeTempFileAndPlay----->fileNameWithOutExt:" + fileNameWithOutExt);
            playSound(fileNameWithOutExt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private static String currSound = "";

    public void playSound(String name) {
        Log.i(TAG, "playSound---->jump in  play-->" + name + ".mp3");
        if (name.isEmpty()) {
            return;
        }
        if (preparing) {
            Log.i(GUARD, "playSound---->preparing:" + preparing);
            return; // protect when media is preparing
        }

        if (!currSound.equals(name)) { // check if the same sound is queried
            currSound = name;
            same = false;
        } else {
            same = true;
        }

        Log.i(TAG, "playSound---->same:" + same);

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying() && same) {
                Log.i(GUARD, "playSound---->isplaying:" + mediaPlayer.isPlaying() + " -- same:" + same + " --> return");
                return; // protect when media is playing
            }
        }

        try {
            File f = new File(Global.pathCache.getCanonicalPath() + File.separator + name + ".mp3");
            Log.i("PATH", "playSound:-------------->" + f.getPath());
            if (!f.exists() || f == null) {
                Log.e(TAG, "playSound----->File not exist -->" + name + ".mp3");
                return;
            }
            if (!same)
                mUri = Uri.parse(Global.pathCache.getCanonicalPath() + File.separator + name + ".mp3");
            if (mUri == null) {
                Log.e(TAG, "playSound----->mUri null");
                return;
            }
            if (mediaPlayer == null) {
                Log.e(TAG, "playSound------>mm null");
                mediaPlayer = MediaPlayer.create(mContext, mUri);
                mediaPlayer.start();
                //mMediaPlayer.setOnCompletionListener(mCompletionListener);
            } else {
                if (same) {
                    Log.i(TAG, "playSound------->mm not null, play same");
                    mediaPlayer.stop();
                    preparing = true;
                    mediaPlayer.setOnPreparedListener(onPreparedListener);
                    mediaPlayer.setOnCompletionListener(mCompletionListener);
                    mediaPlayer.prepareAsync();
                } else {

                    Log.i(TAG, "playSound-------->mm not null, play other");
                    mediaPlayer.release();
                    mediaPlayer = null;
                    mediaPlayer = MediaPlayer.create(mContext, mUri);
                    mediaPlayer.setOnPreparedListener(onPreparedListener);
                    mediaPlayer.setOnCompletionListener(mCompletionListener);
                    mediaPlayer.setVolume(100, 100);
                    mediaPlayer.start();
                }
            }
            Log.i(TAG, "playSound-------->end----------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.stop();
            if (myCompleteSound != null) {
                myCompleteSound.onComplete(mediaPlayer);
                myCompleteSound = null;
            }
        }
    };

    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            preparing = false;
            mediaPlayer.start();
        }
    };

    public void playEncrytedFile(String fileName) {
        if (Global.getIsMute() == false || fileName.isEmpty() || fileName == null) return;
        count++;
        if (fileName.equals(currName) && count < 10) {
            playSound(currTempName);
            Log.i(TAG, "playEncrytedFile:--->play lai am cu " + currTempName);
        } else {
            currName = fileName;
            makeTempFileAndPlay(Utils.decodeFileMp3(fileName), fileName);
        }
    }

//    public void playBgSound() {
//        bgMedia = null;
//        bgMedia = MediaPlayer.create(mContext, R.raw.bg_sound);
//        bgMedia.start();
//    }
//
//    public void stopBgSound() {
//        if (bgMedia != null) {
//            if (bgMedia.isPlaying()) {
//                bgMedia.stop();
//            }
//        }
//    }

    public boolean initPuzzleSound(int params) {
        puzzleDone = MediaPlayer.create(mContext, R.raw.level_up);
        layDown = MediaPlayer.create(mContext, R.raw.lay_down_piece);
        switch (params) {
            case 1:
                pickUp = MediaPlayer.create(mContext, R.raw.pick_up_piece);
                pieceCorrect = MediaPlayer.create(mContext, R.raw.piece_correct);
                puzzleDone2 = MediaPlayer.create(mContext, R.raw.puzzle_finish_2);
                break;
            case 2:
                pickUp = MediaPlayer.create(mContext, R.raw.ringring);
                puzzleDone2 = MediaPlayer.create(mContext, R.raw.rengreng);
                break;
        }
        return true;
    }

    public boolean initHocKhacCorrect() {
        pieceCorrect = MediaPlayer.create(mContext, R.raw.correct_answer);
        if (pieceCorrect != null)
            return true;
        return false;

    }

    public void releaseHocKhacCorrect() {
        if (pieceCorrect != null) {
            pieceCorrect.release();
            pieceCorrect = null;
        }
    }


    public void releasePuzzleSound() {
        if (pickUp != null) {
            pickUp.release();
            pickUp = null;
        }
        if (layDown != null) {
            layDown.release();
            layDown = null;
        }
        if (pieceCorrect != null) {
            pieceCorrect.release();
            pieceCorrect = null;
        }
        if (puzzleDone != null) {
            puzzleDone.release();
            puzzleDone = null;
        }
        if (puzzleDone2 != null) {
            puzzleDone2.release();
            puzzleDone2 = null;
        }
    }


    public void playPuzzleSound(int soundID) {
        if (!Global.getIsMute()) return; // to not play sound when isMute == true
        if (soundID == PIECE_PICK_UP) {
            if (pickUp != null) {
                Log.d(TAG, "INFO: --> PIECE_PICK_UP");
                pickUp.start();
            } else {
                Log.e(TAG, "ERR: --> pickUp null");
            }
        } else if (soundID == PIECE_LAY_DOWN) {
            if (layDown != null) {
                Log.d(TAG, "INFO: --> PIECE_LAY_DOWN");
                layDown.start();
            } else {
                Log.e(TAG, "ERR: --> layDown null");
            }
        } else if (soundID == PIECE_CORRECT) {
            if (pieceCorrect != null) {
                Log.d(TAG, "INFO: --> PIECE_CORRECT");
                pieceCorrect.start();
            } else {
                Log.e(TAG, "ERR: --> pieceCorrect null");
            }
        } else if (soundID == PUZZLE_DONE) {
            if (puzzleDone != null) {
                Log.d(TAG, "INFO: --> PUZZLE_DONE");
                puzzleDone.start();
            } else {
                Log.e(TAG, "ERR: --> puzzleDone null");
            }
        } else if (soundID == PUZZLE_DONE2) {
            if (puzzleDone2 != null) {
                Log.d(TAG, "INFO: --> PUZZLE_DONE2");
                puzzleDone2.start();
            } else {
                Log.e(TAG, "ERR: --> puzzleDone2 null");
            }

        } else {
            Log.e(TAG, "--->id sound wrong");
        }
    }

    public static void playCorrent() {

        if (Global.getIsMute()) {
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
        // TODO
    }

    public static void showStar() {
        if (Global.getIsMute()) {
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
