package vn.devpro.devprokidorigin.activities.game.latbai.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.activities.game.latbai.MainPairGame;
import vn.devpro.devprokidorigin.activities.game.latbai.commom.Shared;
import vn.devpro.devprokidorigin.activities.game.latbai.even.StarNewGame;
import vn.devpro.devprokidorigin.activities.game.latbai.uitls.Utils_PairGame;
import vn.devpro.devprokidorigin.models.ButtonClick;
import vn.devpro.devprokidorigin.utils.AdMob.AdsObserver;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.utils.NetworkReceiver;



public class MenuFragment extends Fragment {


    private ImageView mStartGameButton;
    private ImageView mStartButtonLights;
    private ImageView mTooltip;
    private ImageButton btnBackPairGame , btnSoundPairGame;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.menu_fragment_pairgame, container, false);
        mStartGameButton =  view.findViewById(R.id.start_game_button);
        btnBackPairGame = view.findViewById( R.id.btnBackPairGame );
        btnBackPairGame.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(">.<","backk click");
                if(NetworkReceiver.isConnected()&& AdsObserver.shouldShow( System.currentTimeMillis())){
                    MainPairGame mainPairGame = (MainPairGame) Shared.activity;
                    if(mainPairGame.mInterstitialAds != null){
                        mainPairGame.mInterstitialAds.show();
                    }
                }else {
                    getActivity().finish();
                }
            }
        } );
        btnSoundPairGame = view.findViewById( R.id.btnSoundPairGame );
        Global.updateButtonSoundPairGame(btnSoundPairGame);
        ButtonClick.Sound soundListener = new ButtonClick.Sound();
        btnSoundPairGame.setOnClickListener(soundListener);



        mStartButtonLights =  view.findViewById(R.id.start_game_button_lights);
        mTooltip =  view.findViewById(R.id.tooltip);
        mStartGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // animate title from place and navigation buttons from place
                animateAllAssetsOff(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Shared.eventBus.notify(new StarNewGame(1,Shared.engine.getCurrentopicId()));

                    }
                });
            }
        });

        startLightsAnimation();
        startTootipAnimation();

        //Music.playBackgroundMusic();
        return view;
    }

    protected void animateAllAssetsOff(AnimatorListenerAdapter adapter) {
        // title
        // 120dp + 50dp + buffer(30dp)
//        ObjectAnimator titleAnimator = ObjectAnimator.ofFloat(mTitle, "translationY", Utils_PairGame.px(-200));
//        titleAnimator.setInterpolator(new AccelerateInterpolator(2));
//        titleAnimator.setDuration(300);

        // lights
        ObjectAnimator lightsAnimatorX = ObjectAnimator.ofFloat(mStartButtonLights, "scaleX", 0f);
        ObjectAnimator lightsAnimatorY = ObjectAnimator.ofFloat(mStartButtonLights, "scaleY", 0f);

        // tooltip
        ObjectAnimator tooltipAnimator = ObjectAnimator.ofFloat(mTooltip, "alpha", 0f);
        tooltipAnimator.setDuration(100);

        // settings button
        ObjectAnimator settingsAnimator = ObjectAnimator.ofFloat(btnSoundPairGame, "translationY",
                Utils_PairGame.px(500));
        settingsAnimator.setInterpolator(new AccelerateInterpolator(2));
        settingsAnimator.setDuration(300);

        // google play button


        ObjectAnimator btnBackPair = ObjectAnimator.ofFloat( btnBackPairGame,"translationY",
                Utils_PairGame.px( 500 ) );
        btnBackPair.setInterpolator( new AccelerateInterpolator( 2 ) );
        btnBackPair.setDuration( 300 );
                // start button
        ObjectAnimator startButtonAnimator = ObjectAnimator.ofFloat(mStartGameButton, "translationY",
                Utils_PairGame.px(130));
        startButtonAnimator.setInterpolator(new AccelerateInterpolator(2));
        startButtonAnimator.setDuration(300);

        // khả năng phải chỉnh sửa
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(  lightsAnimatorX, lightsAnimatorY, tooltipAnimator,btnBackPair, settingsAnimator, startButtonAnimator);
        animatorSet.addListener(adapter);
        animatorSet.start();
    }

    private void startTootipAnimation() {
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mTooltip, "scaleY", 0.8f);
        scaleY.setDuration(200);
        ObjectAnimator scaleYBack = ObjectAnimator.ofFloat(mTooltip, "scaleY", 1f);
        scaleYBack.setDuration(500);
        scaleYBack.setInterpolator(new BounceInterpolator());
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setStartDelay(1000);
        animatorSet.playSequentially(scaleY, scaleYBack);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet.setStartDelay(2000);
                animatorSet.start();
            }
        });
        mTooltip.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        animatorSet.start();
    }

    private void startLightsAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mStartButtonLights, "rotation", 0f, 360f);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(6000);
        animator.setRepeatCount( ValueAnimator.INFINITE);
        mStartButtonLights.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        animator.start();
    }


}
