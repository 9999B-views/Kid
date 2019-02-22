package vn.devpro.devprokidorigin.activities.game.dapbongbay;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.activities.game.GameFindShadow;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.game.BalloonGameActivity;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.game.GameFragment;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.game.PauseDialog;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.sound.SoundManager;
import vn.devpro.devprokidorigin.interfaces.BackListener;
import vn.devpro.devprokidorigin.utils.AdMob.InterstitialAds;
import vn.devpro.devprokidorigin.utils.Global;

public abstract class GameBaseActivity extends FragmentActivity implements BackListener {

    protected static final String TAG_FRAGMENT = "content";
    private BaseCustomDialog mCurrentDialog;
    private SoundManager mSoundManager;
    public InterstitialAds mInterstitialAds;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.frame_main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mSoundManager = createSoundManager();
        Handler handler = new Handler();
        handler.postDelayed(loadAdsRunner, 500);
    }

    Runnable loadAdsRunner= new Runnable() {
        @Override
        public void run() {
            mInterstitialAds = InterstitialAds.loadInterstitialAds(GameBaseActivity.this, context);
        }
    };

    protected abstract SoundManager createSoundManager();

    public void showDialog (BaseCustomDialog newDialog, boolean dismissOtherDialog) {
        if (mCurrentDialog != null && mCurrentDialog.isShowing()) {
            if (dismissOtherDialog) {
                mCurrentDialog.dismiss();
            }
            else {
                return;
            }
        }
        mCurrentDialog = newDialog;
        mCurrentDialog.show();
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        if (mCurrentDialog != null && mCurrentDialog.isShowing()) {
            if (mCurrentDialog.dispatchGenericMotionEvent(event)) {
                return true;
            }
        }
        return super.dispatchGenericMotionEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent (KeyEvent event) {
        if (mCurrentDialog != null && mCurrentDialog.isShowing()) {
            if (mCurrentDialog.dispatchKeyEvent(event)) {
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onBackPressed() {
        if (mCurrentDialog != null && mCurrentDialog.isShowing()) {
            mCurrentDialog.dismiss();
            return;
        }
        final GameBaseFragment fragment = (GameBaseFragment) getFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        if (fragment == null || !fragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public SoundManager getSoundManager() {
        return mSoundManager;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSoundManager.pauseBgMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSoundManager.resumeBgMusic();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE);
            }
            else {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }

    protected void navigateToFragment(GameBaseFragment dst) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.framecontainer, dst, TAG_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    public void navigateBack() {
        getFragmentManager().popBackStack();
    }
}
