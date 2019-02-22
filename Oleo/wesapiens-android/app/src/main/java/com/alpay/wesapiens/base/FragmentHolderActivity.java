package com.alpay.wesapiens.base;

import android.os.Bundle;
import android.view.View;

import com.alpay.wesapiens.R;
import com.alpay.wesapiens.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentHolderActivity extends AppCompatActivity{

    @BindView(R.id.back_button)
    FloatingActionButton backButton;

    @OnClick(R.id.back_button)
    public void backButtonAction(){
        FragmentManager.openFragment(this, FragmentManager.HOME);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Utils.playSoundInLoop(this, R.raw.app);
        FragmentManager.openFragment(this, FragmentManager.HOME);
        backButton.bringToFront();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onDestroy() {
        Utils.stopMediaPlayer();
        super.onDestroy();
    }
}
