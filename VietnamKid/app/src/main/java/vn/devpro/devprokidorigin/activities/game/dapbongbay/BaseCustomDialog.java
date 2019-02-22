package vn.devpro.devprokidorigin.activities.game.dapbongbay;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import vn.devpro.devprokidorigin.R;

import static android.view.View.*;

public abstract class BaseCustomDialog implements OnTouchListener, Animation.AnimationListener {

    private boolean mIsShowing;

    protected final GameBaseActivity mParent;
    private ViewGroup mRootLayout;
    private View mRootView;
    private boolean mIsHiding;

    public BaseCustomDialog(GameBaseActivity activity) {
        mParent = activity;
    }


    protected void setContentView(int dialogResId) {
        ViewGroup activityRoot =  mParent.findViewById(android.R.id.content);
        mRootView = LayoutInflater.from(mParent).inflate(dialogResId, activityRoot, false);
    }

    public void show() {
        if (mIsShowing) {
            return;
        }
        mIsShowing = true;
        mIsHiding = false;

        ViewGroup activityRoot =  mParent.findViewById(android.R.id.content);
        mRootLayout = (ViewGroup) LayoutInflater.from(mParent).inflate(R.layout.my_overlay_dialog, activityRoot, false);
        activityRoot.addView(mRootLayout);
        mRootLayout.setOnTouchListener(this);
        mRootLayout.addView(mRootView);
        startShowAnimation();
    }

    private void startShowAnimation() {
        Animation dialogIn = AnimationUtils.loadAnimation(mParent, getEnterAnimatorResId());
        mRootView.startAnimation(dialogIn);
    }

    protected abstract int getEnterAnimatorResId();

    protected abstract int getExitAnimatorResId();

    public void dismiss() {
        if (!mIsShowing) {
            return;
        }
        if (mIsHiding) {
            return;
        }
        mIsHiding = true;
        startHideAnimation();
    }

    protected void onDismissed() {
    }

    private void startHideAnimation() {
        Animation dialogOut = AnimationUtils.loadAnimation(mParent, getExitAnimatorResId());
        dialogOut.setAnimationListener(this);
        mRootView.startAnimation(dialogOut);
    }

    private void hideViews() {
        mRootLayout.removeView(mRootView);
        ViewGroup activityRoot = mParent.findViewById(android.R.id.content);
        activityRoot.removeView(mRootLayout);
    }

    protected View findViewById(int id) {
        return mRootView.findViewById(id);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    public boolean isShowing() {
        return mIsShowing;
    }

    @Override
    public void onAnimationStart(Animation paramAnimation) {
    }

    @Override
    public void onAnimationEnd(Animation paramAnimation) {
        hideViews();
        mIsShowing = false;
        onDismissed();
    }

    @Override
    public void onAnimationRepeat(Animation paramAnimation) {
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return false;
    }

    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        return false;
    }
}
