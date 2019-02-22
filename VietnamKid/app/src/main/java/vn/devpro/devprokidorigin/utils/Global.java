package vn.devpro.devprokidorigin.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import java.io.File;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import vn.devpro.devprokidorigin.R;


/**
 * Created by NamQuoc on 3/20/2018.
 */

public class Global extends Object {

    // biến hệ thống
    public static Context mContext = null;
    // FIXME: 4/27/2018 che dau key
    public static int KEY;
    public static long limit_ads = 60000; // gioi han ko show quang cao sau 1 phut

    public static String GP_LICENSE_KEY;
    public static String productID;
    private static boolean isMute = true;
    private static int language = 0; // 0 == VN  |  1 == EN | default == VN
    public static final int VN = 0;
    public static final int EN = 1;
    private static int maxShow_ratio;
    private static String UrlApp = "";
    //public static String resourceVersion;
    public static String resourceVersion = "covadaci"; // gán mặc định để chống null pointer exception trong trg hợp resourceVersion ko đc cập nhật thành công
    public final static String NO_DATA_KEY = "covadaci"; //  nodata -- covadaci - -
    public final static String DEMO_DATA_KEY = "noncovadaci";// demodata noncovadaci
    public static String databaseFileName = "devkid.db";
    public static String versionSeverFileName = "_futureHyperVector.namquoc"; //  _versionServer -->  _futureHyperVector
    public static String dataDemoFileName = "accessible_ability.zip"; // devpro_demo --> accessible_ability
    //    public static String urlServer = "http://devkid.byethost7.com/"; // server download cua minh
    public static String urlServer = "http://laptrinhandroid.vn/download/"; // server download cua minh

    // biến thư mục và đường dẫn
    public static File pathSysDatabase; // đường dẫn file database của app
    public static File pathFiles;
    public static String pathDataDemoFile;
    private static String pathDatabase; // đường dẫn file database trong bộ nhớ
    public static File pathCache;
    public static File pathSounds;
    public static File pathImages;
    private static File pathGames;

    //biến đường dẫn game
    public static File pathGameFindShadow;
    public static File pathGamePopballoon;
    public static File pathImgGameXepHinh;
    public static File pathGameMatch;
    public static File pathGameLearnWrite;

    // biến tên game
    public static String GAME_MATCHING_OBJ_VN = "Nối Đồ Vật";
    public static String GAME_MATCHING_OBJ_EN = "Matching Object";
    public static String GAME_FIND_SHADOW_VN = "Tìm Bóng";
    public static String GAME_FIND_SHADOW_EN = "Find Shadow";
    public static String GAME_POP_BALLOON_VN = "Đập Bóng Bay";
    public static String GAME_POP_BALLOON_EN = "Pop Balloon";
    public static String GAME_MAKE_PAIR_VN = "Ghép cặp";
    public static String GAME_MAKE_PAIR_EN = "Make Pair";
    public static String GAME_PUZZLE_VN = "Xếp Hình";
    public static String GAME_PUZZLE_EN = "Jigsaw Puzzle";
    public static String GAME_WRITE_LETTER_VN = "Tập Viết Chữ";
    public static String GAME_WRITE_LETTER_EN = "Write Letter";
    public static String RATE_APP_LINK = "https://play.google.com/store/apps/details";
    public static boolean isPremium = false;
    private static String FB_TAG = "DB Firebase";
    private static String TAG = "Global";


    public static void initGlobal(Context context) {
        if (context == null) {
            return;
        }
        mContext = context;
        productID = "premium";
        maxShow_ratio = 3;
        KEY = Utils.A + UpdateTask.B + IKey.B;

        // set dường dẫn hệ thống files
        pathSysDatabase = mContext.getDatabasePath(databaseFileName);
        pathCache = mContext.getCacheDir();
        pathFiles = mContext.getExternalFilesDir("/");
        pathDataDemoFile = pathFiles + "/" + dataDemoFileName;
        pathDatabase = pathFiles + "/" + dataDemoFileName;
        pathSounds = mContext.getExternalFilesDir("Sounds");
        pathImages = mContext.getExternalFilesDir("Images");
        pathGames = mContext.getExternalFilesDir("Games");

        // set đường dẫn games
        pathGameFindShadow = mContext.getExternalFilesDir("Games/FindShadow");
        pathGamePopballoon = mContext.getExternalFilesDir("Games/PopBalloon");
        pathImgGameXepHinh = mContext.getExternalFilesDir("Games/Puzzle");
        pathGameMatch = mContext.getExternalFilesDir("Games/Matching");
        pathGameLearnWrite = mContext.getExternalFilesDir("Games/LearnWrite");

        Font.initTypeFace(mContext);

        if (NetworkReceiver.isConnected()) {
            getAppKey();
        }
    }

    public static int getLanguage() {
        return language;
    }

    public static void setLanguage(int language) {
        Global.language = language;
    }

    public static boolean getIsMute() {
        return isMute;
    }


    private static void setMute(boolean isMute) {
        Global.isMute = isMute;
    }

    public static void changeLanguage() {
        if (language == Global.VN) {
            Global.setLanguage(Global.EN);
        } else {
            Global.setLanguage(Global.VN);
        }
    }

    public static void changeSoundState() {
        if (Global.isMute) {
            Global.setMute(false);
        } else {
            Global.setMute(true);
        }
    }

    public static void updateButtonSound(Button btnSound) {
        if (isMute) {
            btnSound.setBackgroundResource(R.drawable.soundon);
        } else {
            btnSound.setBackgroundResource(R.drawable.soundoff);
        }
    }


    public static void updateButtonSound(ImageButton btnSound) {
        if (isMute) {
            btnSound.setBackgroundResource(R.drawable.soundon);
        } else {
            btnSound.setBackgroundResource(R.drawable.soundoff);
        }
    }

    public static void updateButtonSoundPairGame(ImageButton btnSoundPairGame) {
        if (isMute) {
            btnSoundPairGame.setBackgroundResource(R.drawable.soundon);
        } else {
            btnSoundPairGame.setBackgroundResource(R.drawable.soundoff);
        }
    }


    public static void updateButtonLanguage(Button langBtn) {
        if (language == Global.VN) {
            langBtn.setBackgroundResource(R.drawable.language_vi);
        } else {
            langBtn.setBackgroundResource(R.drawable.language_en);
        }
    }

    public static void updateButtonLanguage(ImageButton langBtn) {
        if (language == Global.VN) {
            langBtn.setBackgroundResource(R.drawable.language_vi);
        } else {
            langBtn.setBackgroundResource(R.drawable.language_en);
        }
    }


    public static void setDatabaseFileName(String databaseFileName) {
        Global.databaseFileName = databaseFileName;
    }


    public static class Font {
        public static Typeface Quicksand_Bold;
        public static Typeface Boosternextfy_Bold;

        static void initTypeFace(Context pContext) {
            Quicksand_Bold = Typeface.createFromAsset(pContext.getAssets(), "fonts/quicksand_bold.ttf");
            Boosternextfy_Bold = Typeface.createFromAsset(pContext.getAssets(), "fonts/boosternextfy_bold.ttf");
        }
    }

    private static void getAppKey() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        // app-key --> usi_uii_id
        myRef.child("usi_uii_id").child("nam-nbc").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                if (value != null) {
                    GP_LICENSE_KEY = value;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.e(FB_TAG, "Failed to read value.", error.toException());
            }
        });
        // app-key --> usi_uii_id
        //  url-app-key --> hud_pop_gpk
        myRef.child("usi_uii_id").child("hud_pop_gpk").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                if (value != null) {
                    RATE_APP_LINK = value;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(FB_TAG, "Failed to read value.", error.toException());
            }
        });
        // app-key --> usi_uii_id
        myRef.child("usi_uii_id").child("hud_fa_tab").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Integer value = dataSnapshot.getValue(Integer.class);
                if (value != null) {
                    KEY = value;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(FB_TAG, "Failed to read value.", error.toException());
            }
        });
    }


    public static void doRestart(Context c) {
        try {
            //check if the context is given
            if (c != null) {
                //fetch the packagemanager so we can get the default launch activity
                // (you can replace this intent with any other activity if you want
                PackageManager pm = c.getPackageManager();
                //check if we got the PackageManager
                if (pm != null) {
                    //create the intent with the default start activity for your application
                    Intent mStartActivity = pm.getLaunchIntentForPackage(
                            c.getPackageName()
                    );
                    if (mStartActivity != null) {
                        mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        //create a pending intent so the application is restarted after System.exit(0) was called.
                        // We use an AlarmManager to call this intent in 100ms
//                        int mPendingIntentId = 223344;
//                        PendingIntent mPendingIntent = PendingIntent
//                                .getActivity(c, mPendingIntentId, mStartActivity,
//                                        PendingIntent.FLAG_CANCEL_CURRENT);
//                        AlarmManager mgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
//                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                        //kill the application
                        //System.exit(0);
                        c.startActivity(mStartActivity);
                    } else {
                        Log.e(TAG, "Was not able to restart application, mStartActivity null");
                    }
                } else {
                    Log.e(TAG, "Was not able to restart application, PM null");
                }
            } else {
                Log.e(TAG, "Was not able to restart application, Context null");
            }
        } catch (Exception ex) {
            Log.e(TAG, "Was not able to restart application");
        }
    }


    public static void setIsPremium(boolean isPremium) {
        Global.isPremium = isPremium;
    }

    public static boolean isDemoApp() {
        if (Global.resourceVersion == null) return true;
        return Global.resourceVersion.equals(Global.DEMO_DATA_KEY);
    }
}


