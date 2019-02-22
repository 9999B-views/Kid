package vn.devpro.devprokidorigin.activities.game;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.databases.DBHelper;
import vn.devpro.devprokidorigin.dialogs.CustomAlertDialog;
import vn.devpro.devprokidorigin.interfaces.BackListener;
import vn.devpro.devprokidorigin.models.ButtonClick;
import vn.devpro.devprokidorigin.models.Sound;
import vn.devpro.devprokidorigin.models.entity.game.FindShadow;
import vn.devpro.devprokidorigin.models.interfaces.ChangeLanguageCallback;
import vn.devpro.devprokidorigin.utils.AdMob.AdsObserver;
import vn.devpro.devprokidorigin.utils.AdMob.InterstitialAds;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.utils.NetworkReceiver;
import vn.devpro.devprokidorigin.utils.Utils;

public class GameFindShadow extends FragmentActivity implements View.OnDragListener, View.OnTouchListener, ChangeLanguageCallback, BackListener {
    private Button btnBack, btnSound, btnRandom, btnLang;
    private ImageView imgShadow, imgShadow2, imgShadow3, imgOrigin1, imgOrigin2, imgOrigin3;
    private Animation a, a1, a2, a3, animationFinish;
    private int random[];
    private boolean start = false;
    private LinearLayout layoutOriginImg, layoutShadowImg;
    private ArrayList<FindShadow> list = new ArrayList<>();
    private ArrayList<FindShadow> listGame = new ArrayList<>();
    private int count;
    InterstitialAds mInterstitialAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_find_shadow);
        btnBack = findViewById(R.id.btnBackFSGame);
        btnRandom = findViewById(R.id.btnRandom);
        btnLang = findViewById(R.id.btnLangFSGame);
        btnSound = findViewById(R.id.btnSoundFSGame);
        imgOrigin1 = findViewById(R.id.imgOrigin1);
        imgOrigin2 = findViewById(R.id.imgOrigin2);
        imgOrigin3 = findViewById(R.id.imgOrigin3);
        imgShadow = findViewById(R.id.imgShadow1);
        imgShadow2 = findViewById(R.id.imgShadow2);
        imgShadow3 = findViewById(R.id.imgShadow3);
        layoutOriginImg = findViewById(R.id.listOriginImg);
        layoutShadowImg = findViewById(R.id.listImgShadow);
        a = AnimationUtils.loadAnimation(this, R.anim.scale);
        a1 = AnimationUtils.loadAnimation(this, R.anim.scale);
        a2 = AnimationUtils.loadAnimation(this, R.anim.scale);
        a3 = AnimationUtils.loadAnimation(this, R.anim.scale);
        animationFinish = AnimationUtils.loadAnimation(this, R.anim.ani_fsgame);
        if (!animationFinish.hasEnded()){
            btnRandom.setClickable(false);
        }

        layoutShadowImg.setOnDragListener(null);
        ButtonClick.Language langListener = new ButtonClick.Language(this);
        ButtonClick.Sound sounDListener = new ButtonClick.Sound();
        btnSound.startAnimation(AnimationUtils.loadAnimation(this, R.anim.btn_click));
        btnLang.startAnimation(AnimationUtils.loadAnimation(this, R.anim.btn_click));
        btnSound.setOnClickListener(sounDListener);
        btnLang.setOnClickListener(langListener);
        //======= first update for language and sound btn =========
        Global.updateButtonLanguage(btnLang);
        Global.updateButtonSound(btnSound);
        //=========================================================
        creatUI();
        btnRandom.setOnClickListener(myBtnListener);
        btnBack.setOnClickListener(myBtnListener);
        Handler loadAdsHandler = new Handler();

        loadAdsHandler.postDelayed(loadAdsRunner,500);
    }

    Runnable loadAdsRunner= new Runnable() {
        @Override
        public void run() {
            mInterstitialAds = InterstitialAds.loadInterstitialAds(GameFindShadow.this, GameFindShadow.this);
        }
    };


    View.OnClickListener myBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View pView) {
            switch (pView.getId()){
                case R.id.btnRandom:
                    if (a.hasEnded() && a1.hasEnded()&& a2.hasEnded() && a3.hasEnded()) {
                        Utils.btn_click_animate(btnRandom);
                        creatUI();
                    }
                    break;
                case R.id.btnBackFSGame:
                    if(NetworkReceiver.isConnected() && mInterstitialAds != null&& AdsObserver.shouldShow( System.currentTimeMillis())){
                        mInterstitialAds.show();
                    }else {
                        finish();
                    }
                    break;
            }
        }
    };


    private void randomImg() {
        int so1 = 0, so2 = 0, so3;
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                Random r = new Random();
                so1 = r.nextInt(list.size());
                listGame.add(i, list.get(so1));
            } else if (i == 1) {
                Random r = new Random();
                so2 = r.nextInt(list.size());
                while (so2 == so1) {
                    so2 = r.nextInt(list.size());
                }
                listGame.add(i, list.get(so2));
            } else {
                Random r = new Random();
                so3 = r.nextInt(list.size());
                while (so3 == so1 || so3 == so2) {
                    so3 = r.nextInt(list.size());
                }
                listGame.add(i, list.get(so3));
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOriginView() {
        if (Utils.getBitmapForGame(Global.pathGameFindShadow,listGame.get(0).getOrigin_img(),".png",false) == null) {
            Utils.showToast(this, "Chưa có tài nguyên game!");
        }
        imgOrigin1.setImageBitmap(Utils.getBitmapForGame(Global.pathGameFindShadow,listGame.get(0).getOrigin_img(),".png",false));
        imgOrigin2.setImageBitmap(Utils.getBitmapForGame(Global.pathGameFindShadow,listGame.get(1).getOrigin_img(),".png",false));
        imgOrigin3.setImageBitmap(Utils.getBitmapForGame(Global.pathGameFindShadow,listGame.get(2).getOrigin_img(),".png",false));

        imgOrigin1.setTag("A");
        imgOrigin2.setTag("B");
        imgOrigin3.setTag("C");
        listGame.get(0).setTag("A");
        listGame.get(1).setTag("B");
        listGame.get(2).setTag("C");

        imgOrigin1.startAnimation(a);
        imgOrigin2.startAnimation(a);
        imgOrigin3.startAnimation(a);

        imgOrigin1.setVisibility(View.VISIBLE);
        imgOrigin2.setVisibility(View.VISIBLE);
        imgOrigin3.setVisibility(View.VISIBLE);

        imgOrigin1.setOnTouchListener(this);
        imgOrigin3.setOnTouchListener(this);
        imgOrigin2.setOnTouchListener(this);

        imgOrigin1.setOnDragListener(this);
        imgOrigin2.setOnDragListener(this);
        imgOrigin3.setOnDragListener(this);

    }

    private void initShadowView() {
        int so1 = 0, so2 = 0, so3 = 0;
        Random r = new Random();
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                so1 = r.nextInt(3);
            } else if (i == 1) {
                while (so2 == so1) {
                    so2 = r.nextInt(3);
                }
            } else {
                while (so3 == so1 || so3 == so2) {
                    so3 = r.nextInt(3);
                }
            }
        }
        random = getRandom();
        startAnimationImg(a, imgShadow, Utils.getBitmapForGame(Global.pathGameFindShadow,listGame.get(random[0]).getShadow_img(), ".png",false), listGame.get(random[0]).getTag(), a1);
        startAnimationImg(a1, imgShadow2, Utils.getBitmapForGame(Global.pathGameFindShadow,listGame.get(random[1]).getShadow_img(), ".png",false), listGame.get(random[1]).getTag(), a2);
        startAnimationImg(a2, imgShadow3, Utils.getBitmapForGame(Global.pathGameFindShadow,listGame.get(random[2]).getShadow_img(), ".png",false), listGame.get(random[2]).getTag(), a3);
        imgShadow.setOnDragListener(this);
        imgShadow2.setOnDragListener(this);
        imgShadow3.setOnDragListener(this);
    }

    protected void startAnimationImg(final Animation animation, final ImageView imageView,
                                     final Bitmap bmp, final String tag, final Animation animation2) {
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                imageView.setImageBitmap(bmp);
                imageView.setVisibility(View.VISIBLE);
                imageView.startAnimation(animation2);
                imageView.setTag(tag);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    v.invalidate();
                }
                v.getTag();
                return true;

            case DragEvent.ACTION_DRAG_ENTERED:
                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                if (v == layoutOriginImg)
                    return false;

                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                return true;
            case DragEvent.ACTION_DROP:
                View view1 = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) v.getParent();
                if (v == owner) {
                    break;
                }
                if (owner == layoutShadowImg) {
                    if (v.getTag() == view1.getTag()) {
                        count++;
                        ImageView imageView = (ImageView) v;
                        Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);
                        for (int i = 0; i < 3; i++) {
                            if (listGame.get(i).getTag() == view1.getTag()) {
                                imageView.setImageBitmap(Utils.getBitmapForGame(Global.pathGameFindShadow,listGame.get(i).getOrigin_img(), ".png",false));
                                if (Global.getLanguage() == Global.VN) {
                                    Sound.getInstance(Global.mContext).playEncrytedFile(listGame.get(i).getSound_vn());
                                } else {
                                    Sound.getInstance(Global.mContext).playEncrytedFile(listGame.get(i).getSound_en());
                                }
                                for (int j = 0; j < 3; j++) {
                                    if (layoutShadowImg.getChildAt(j).getTag() == v.getTag()) {
                                        layoutShadowImg.getChildAt(j).setVisibility(View.INVISIBLE);
                                        break;
                                    } else {
                                        layoutShadowImg.getChildAt(j).setVisibility(View.VISIBLE);
                                        break;
                                    }
                                }

                            }
                        }
                        imageView.startAnimation(animation);
                        imageView.setVisibility(View.VISIBLE);
                        return true;
                    } else {
                        View view2 = (View) event.getLocalState();
                        ObjectAnimator
                                .ofFloat(v, "translationX", 0, 25, -25, 25, -25, 15, -15, 9, -9, 6, -6, 0)
                                .setDuration(500)
                                .start();
                        view2.setVisibility(View.VISIBLE);
                        v.setVisibility(View.VISIBLE);
                        break;
                    }
                }
                return false;

            case DragEvent.ACTION_DRAG_ENDED:

                if (!event.getResult()) {
                    final View droppedView = (View) event.getLocalState();
                    droppedView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (layoutOriginImg.getChildCount() != 1) {
                                droppedView.setVisibility(View.VISIBLE);
                            }else {
                                droppedView.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (count == 3) {
                            imgShadow.startAnimation(animationFinish);
                            imgShadow2.startAnimation(animationFinish);
                            imgShadow3.startAnimation(animationFinish);
                            animationFinish.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    btnRandom.setClickable(false);
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    try {
                                        Thread.sleep(300);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    btnRandom.setClickable(true);
                                    creatUI();
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });

                        }
                    }
                }, 400);
            default:
                break;

        }
        return true;
    }

    private void creatUI() {
        Random r = new Random();
        int i = r.nextInt(10);
        if (0 <= i && i <= 3) {
            list = DBHelper.getInstance(this).getImageGameFS(5);
        } else if (4 <= i && i <= 7) {
            list = DBHelper.getInstance(this).getImageGameFS(8);
        } else {
            list = DBHelper.getInstance(this).getImageGameFS(6);
        }
        if(list.size() == 0){
            show_alert(this.getSupportFragmentManager(),this);
            return;
        }
        imgShadow.setVisibility(View.INVISIBLE);
        imgShadow2.setVisibility(View.INVISIBLE);
        imgShadow3.setVisibility(View.INVISIBLE);
        randomImg();
        initOriginView();
        initShadowView();
        count = 0;
    }

    private void show_alert(FragmentManager fm, final Context mContext) {
        CustomAlertDialog alertDialog = CustomAlertDialog.newInstance(null, new Runnable() {
            @Override
            public void run() {
                Global.doRestart(mContext);
            }
        });
        alertDialog.onAttach(mContext);
        alertDialog.show(fm,null);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
        v.startDrag(data, shadowBuilder, v, 0);
        v.setVisibility(View.INVISIBLE);
        return true;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    private int[] getRandom() {
        int list[] = new int[3];
        Random r = new Random();
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                list[i] = r.nextInt(3);
            } else if (i == 1) {
                while (list[1] == list[0]) {
                    list[1] = r.nextInt(3);
                }
            } else {
                while (list[2] == list[1] || list[2] == list[0]) {
                    list[2] = r.nextInt(3);
                }
            }
        }
        return list;
    }

    @Override
    public void updateUIForLanguage() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(NetworkReceiver.isConnected() && mInterstitialAds != null&& AdsObserver.shouldShow( System.currentTimeMillis())){
            mInterstitialAds.show();
        }else {
            finish();
        }
    }

    @Override
    public void toDoBackListener() {
        finish();
    }
}
