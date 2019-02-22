package vn.devpro.devprokidorigin.activities.game.latbai.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.activities.game.latbai.commom.Music;
import vn.devpro.devprokidorigin.activities.game.latbai.commom.Shared;
import vn.devpro.devprokidorigin.activities.game.latbai.even.BackGame;
import vn.devpro.devprokidorigin.activities.game.latbai.even.NextGame;
import vn.devpro.devprokidorigin.activities.game.latbai.uitls.Clock;
import vn.devpro.devprokidorigin.models.Sound;
import vn.devpro.devprokidorigin.models.entity.game.latbai.GameState;


/**
 * Created by admin on 3/31/2018.
 */

public class View_win extends RelativeLayout {
    private TextView mScore;
    private TextView mTime;
    private ImageView mNextButton;
    private ImageView mBackButton;
    private Handler mHandler;
    private ImageView mStar1;
    private ImageView mStar2;
    private ImageView mStar3;

    public View_win(Context context) {
        this( context, null );
    }

    public View_win(Context context, AttributeSet attrs) {
                super( context, attrs );

        LayoutInflater.from( context ).inflate( R.layout.win_activity, this, true );
        mScore = (TextView) findViewById( R.id.score_bar_text );
        mBackButton = (ImageView) findViewById( R.id.button_back );
        mNextButton = (ImageView) findViewById( R.id.button_next );
        mTime = (TextView) findViewById( R.id.time_bar_text );
        mStar1 = (ImageView) findViewById(R.id.star_1);
        mStar2 = (ImageView) findViewById(R.id.star_2);
        mStar3 = (ImageView) findViewById(R.id.star_3);
// ảnh hoàn thành game kết thúc


        setBackgroundResource( R.drawable.level_complete );

        mHandler = new Handler();

        mBackButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Shared.eventBus.notify( new BackGame() );
            }
        } );
        mNextButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shared.eventBus.notify( new NextGame() );

            }
        } );

    }

    public void setGameState(final GameState gameState) {
        int min = gameState.remainedSeconds / 60;
        int sec = gameState.remainedSeconds - min * 60;
        mTime.setText(" " + String.format("%02d", min) + ":" + String.format("%02d", sec));

              mScore.setText( "" + 0 );

        mHandler.postDelayed( new Runnable() {

            @Override
            public void run() {
                  animateScore( gameState.remainedSeconds, gameState.achievedScore );
                  animateStars(gameState.achievedStars);
            }
        }, 500 );
    }
    private void animateStars(int start) {
        switch (start) {
            case 0:
                mStar1.setVisibility(View.GONE);
                mStar2.setVisibility(View.GONE);
                mStar3.setVisibility(View.GONE);
                break;
            case 1:
                mStar2.setVisibility(View.GONE);
                mStar3.setVisibility(View.GONE);
                mStar1.setAlpha(0f);
                animateStar(mStar1, 0);
                break;
            case 2:
                mStar3.setVisibility(View.GONE);
                mStar1.setVisibility(View.VISIBLE);
                mStar1.setAlpha(0f);
                animateStar(mStar1, 0);
                mStar2.setVisibility(View.VISIBLE);
                mStar2.setAlpha(0f);
                animateStar(mStar2, 600);
                break;
            case 3:
                mStar1.setVisibility(View.VISIBLE);
                mStar1.setAlpha(0f);
                animateStar(mStar1, 0);
                mStar2.setVisibility(View.VISIBLE);
                mStar2.setAlpha(0f);
                animateStar(mStar2, 600);
                mStar3.setVisibility(View.VISIBLE);
                mStar3.setAlpha(0f);
                animateStar(mStar3, 1200);
                break;
            default:
                break;
        }
    }

    private void animateStar(final View view, int delay) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0, 1f);
        alpha.setDuration(100);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(alpha, scaleX, scaleY);
        animatorSet.setInterpolator(new BounceInterpolator());
        animatorSet.setStartDelay(delay);
        animatorSet.setDuration(600);
        view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        animatorSet.start();

        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Sound.showStar();
            }
        }, delay);
    }
    private void animateScore(final int remainedSeconds, final int achievedScore) {
        final int totalAnimation = 1200;

        Clock.getInstance().startTimer( totalAnimation,
                35, new Clock.OnTimerCount() {
            @Override
            public void onTick(long millisUntilFinished) {
                float factor = millisUntilFinished / (totalAnimation * 1f); // 0.1
                int scoreToShow = achievedScore - (int) (achievedScore * factor);
                int timeToShow = (int) (remainedSeconds * factor);
                int min = timeToShow / 60;
                int sec = timeToShow - min * 60;
                mTime.setText( " " + String.format( "%02d", min ) + ":" + String.format( "%02d", sec ) );
                mScore.setText( "" + scoreToShow );
            }

            @Override
            public void onFinish() {
                mTime.setText( " " + String.format( "%02d", 0 ) + ":" + String.format( "%02d", 0 ) );
                mScore.setText( "" + achievedScore );
            }
        } );


    }
}

