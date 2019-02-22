package vn.devpro.devprokidorigin.activities.game.dapbongbay.game;

import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.BaseCustomDialog;
import vn.devpro.devprokidorigin.utils.AdMob.AdsObserver;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.utils.NetworkReceiver;

public class PauseDialog extends BaseCustomDialog implements View.OnClickListener {

    private PauseDialogListener mListener;
    private int mSelectedId;
    private Button btnYes;
    private Button btnNo;
    TextView tvTtitle;
    BalloonGameActivity mActivity;

    public PauseDialog(BalloonGameActivity pActivity) {
        super(pActivity);
        this.mActivity = pActivity;
        setContentView(R.layout.dialog_pause);
        Typeface face = Typeface.createFromAsset(Global.mContext.getAssets(), "fonts/boosternextfy_bold.ttf");
        btnYes = (Button) findViewById(R.id.btnYes);
        btnNo = (Button) findViewById(R.id.btnNo);
        tvTtitle = (TextView) findViewById(R.id.mgsTV);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);
        btnYes.setTypeface(face);
        btnNo.setTypeface(face);
        tvTtitle.setTypeface(face);
        if (Global.getLanguage() == Global.EN) {
            btnYes.setText("Yes");
            btnNo.setText("No");
            tvTtitle.setText("Exit Game");
        } else {
            tvTtitle.setText("Thoát game");
            btnYes.setText("Có");
            btnNo.setText("Không");
        }
    }

    public void setListener(PauseDialogListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnYes) {
            mSelectedId = v.getId();
            super.dismiss();
        } else if (v.getId() == R.id.btnNo) {
            mSelectedId = v.getId();
            super.dismiss();
        }
    }

    @Override
    protected void onDismissed() {
        if (mSelectedId == R.id.btnYes) {
            //mListener.exitGame();
            if (NetworkReceiver.isConnected()&& AdsObserver.shouldShow( System.currentTimeMillis())) {
                mActivity.mInterstitialAds.show();
            } else {
                mActivity.finish();
            }
        } else if (mSelectedId == R.id.btnNo) {
            mListener.resumeGame();
        }
    }

    @Override
    protected int getEnterAnimatorResId() {
        return R.animator.enter_from_top;
    }

    @Override
    protected int getExitAnimatorResId() {
        return R.animator.exit_trough_top;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mSelectedId = R.id.btn_resume;
    }

    public interface PauseDialogListener {

        void exitGame();

        void resumeGame();
    }
}
