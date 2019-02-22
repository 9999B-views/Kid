package vn.devpro.devprokidorigin.activities.game;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.controllers.game.DrawView;
import vn.devpro.devprokidorigin.dialogs.CustomAlertDialog;
import vn.devpro.devprokidorigin.dialogs.TopicGameDialog;
import vn.devpro.devprokidorigin.interfaces.BackListener;
import vn.devpro.devprokidorigin.interfaces.ITopicClick;
import vn.devpro.devprokidorigin.models.ButtonClick;
import vn.devpro.devprokidorigin.models.Sound;
import vn.devpro.devprokidorigin.models.entity.game.MatchObjEntity;
import vn.devpro.devprokidorigin.models.entity.game.PointDraw;
import vn.devpro.devprokidorigin.models.interfaces.ChangeLanguageCallback;
import vn.devpro.devprokidorigin.presenters.GamePresenter;
import vn.devpro.devprokidorigin.utils.AdMob.AdsObserver;
import vn.devpro.devprokidorigin.utils.AdMob.InterstitialAds;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.utils.NetworkReceiver;
import vn.devpro.devprokidorigin.utils.Utils;

public class GameNoiHinhActivity extends AppCompatActivity implements View.OnClickListener,
        ChangeLanguageCallback, ITopicClick, BackListener {
    private final String TAG = GameNoiHinhActivity.class.getSimpleName();
    InterstitialAds mInterstitialAds;
    private final String UP = "_up";
    private final String LOW = "_low";
    private final String PNG = ".png";
    private final String JPG = ".jpg";
    private DrawView drawView;
    private RelativeLayout root;
    private ImageView img_Hinh1;
    private RoundedImageView img_Hinh2;
    private ImageView img_Hinh3;
    private RoundedImageView img_Hinh4;
    private ImageView img_Hinh5;
    private RoundedImageView img_Hinh6;
    private LinearLayout ln_hinh1;
    private LinearLayout ln_hinh2;
    private LinearLayout ln_hinh3;
    private LinearLayout ln_hinh4;
    private LinearLayout ln_hinh5;
    private LinearLayout ln_hinh6;
    private ImageButton img_back;
    private ImageButton img_sound;
    private ImageButton img_ngon_ngu;
    private ImageButton img_chu_de_noi;
    private TextView txt_notification;
    private Animation animation;
    private Animation animation2;
    private TopicGameDialog topicGameDialog = null;
    private Sound mSound;

    private int statusHeight = 0;
    private float startX = -1, startY = -1;
    private float endX = -1, endY = -1;
    private int ketqua = 0;
    private boolean finishAnim = true;
    private String tag1 = "";
    private String tag2 = "";
    private int[] pos1 = new int[2];
    private int[] pos2 = new int[2];
    private int[] pos3 = new int[2];
    private int[] pos4 = new int[2];
    private int[] pos5 = new int[2];
    private int[] pos6 = new int[2];

    private int topic = 5;
    private GamePresenter gamePresenter;
    private boolean check = false;
    private PointDraw point1, point2;
    private final String[] names = {"a", "aw", "aa", "b", "c", "d", "dd", "e", "ee", "f", "g", "h",
            "i", "j", "k", "l", "m", "n", "o", "oo", "ow", "p", "q", "r",
            "s", "t", "u", "uw", "v", "w", "x", "y", "z"};

    private List<String> listChuCai;
    private List<MatchObjEntity> listMatchObj;
    private List<MatchObjEntity> listAll;
    private ArrayList<String> nameArray = new ArrayList<>();
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("@Time", "-->onCreate");
        new InitSoundTask().execute();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_noi_hinh);
        new InitViewTask().execute();

        getScreenSize();
        //TODO async load ads
        Handler laodAdsHandler = new Handler();
        laodAdsHandler.postDelayed(loadAdsRunner, 500);
        Log.d("@Time", "-->onCreate done");

    }

    class InitSoundTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... pVoids) {
            initSounds();
            return null;
        }
    }

    class InitViewTask extends AsyncTask<Void, Void, RelativeLayout.LayoutParams> {
        @Override
        protected RelativeLayout.LayoutParams doInBackground(Void... pVoids) {
            RelativeLayout.LayoutParams p = initView();
            return p;
        }

        @Override
        protected void onPostExecute(RelativeLayout.LayoutParams params) {
            newGameABC();
            root.addView(drawView, params);
            txt_notification.setTypeface(typeface);
            Global.updateButtonLanguage(img_ngon_ngu);
            //=====================================
            img_Hinh1.getLocationOnScreen(pos1);
            img_Hinh2.getLocationOnScreen(pos2);
            img_Hinh3.getLocationOnScreen(pos3);
            img_Hinh4.getLocationOnScreen(pos4);
            img_Hinh5.getLocationOnScreen(pos5);
            img_Hinh6.getLocationOnScreen(pos6);
        }
    }


    Runnable loadAdsRunner = new Runnable() {
        @Override
        public void run() {
            mInterstitialAds = InterstitialAds.loadInterstitialAds(GameNoiHinhActivity.this, GameNoiHinhActivity.this);
        }
    };

    private void getScreenSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.heightPixels;
        Log.d(TAG, "ScreenWidth = " + screenWidth);
        statusHeight = (int) 0.22 * screenWidth;
    }


    private RelativeLayout.LayoutParams initView() {
        Log.d("@Time", "-->initView");
        long time = 0;
        root = findViewById(R.id.root);
        ln_hinh1 = findViewById(R.id.ln_Hinh1);
        ln_hinh2 = findViewById(R.id.ln_Hinh2);
        ln_hinh3 = findViewById(R.id.ln_Hinh3);
        ln_hinh4 = findViewById(R.id.ln_Hinh4);
        ln_hinh5 = findViewById(R.id.ln_Hinh5);
        ln_hinh6 = findViewById(R.id.ln_Hinh6);
        img_Hinh1 = findViewById(R.id.img_Hinh1);
        img_Hinh2 = findViewById(R.id.img_Hinh2);
        img_Hinh3 = findViewById(R.id.img_Hinh3);
        img_Hinh4 = findViewById(R.id.img_Hinh4);
        img_Hinh5 = findViewById(R.id.img_Hinh5);
        img_Hinh6 = findViewById(R.id.img_Hinh6);
        img_back = findViewById(R.id.img_back_activity);
        img_back.setOnClickListener(this);
        img_sound = findViewById(R.id.img_sound);
        ButtonClick.Sound soundListener = new ButtonClick.Sound();
        img_sound.setOnClickListener(soundListener);
        img_ngon_ngu = findViewById(R.id.img_ngon_ngu);
        ButtonClick.Language langListener = new ButtonClick.Language(this);
        img_ngon_ngu.setOnClickListener(langListener);
        img_chu_de_noi = findViewById(R.id.img_chu_de_noi);
        img_chu_de_noi.setOnClickListener(this);
        txt_notification = findViewById(R.id.txt_notification);
        drawView = new DrawView(this);
        mSound = Sound.getInstance(Global.mContext);
        listAll = new ArrayList<>();
        listMatchObj = new ArrayList<>();
        listChuCai = new ArrayList<>();
        gamePresenter = new GamePresenter(pos1, pos2, pos3, pos4, pos5, pos6);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.zoom_in_game);
        /*Xu ly theo ngon ngu */
        if (Global.getLanguage() == Global.VN) {
            gamePresenter.fillListVN(listChuCai, names);
        } else {
            gamePresenter.fillListEN(listChuCai, names);
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        long timeCR = System.currentTimeMillis();
        Log.d("@Time", "-->initView done");
        typeface = Typeface.createFromAsset(getAssets(), "fonts/boosternextfy_bold.ttf");
        return params;
    }

    private boolean initSounds() {
        Log.d("@Time", "-->initSounds");
        boolean res = Sound.getInstance(Global.mContext).initPuzzleSound(2);
        Log.d("@Time", "-->initSounds done");
        return res;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    private void getLocateImages() {
        img_Hinh1.getLocationOnScreen(pos1);
        img_Hinh2.getLocationOnScreen(pos2);
        img_Hinh3.getLocationOnScreen(pos3);
        img_Hinh4.getLocationOnScreen(pos4);
        img_Hinh5.getLocationOnScreen(pos5);
        img_Hinh6.getLocationOnScreen(pos6);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onResume() {
        Log.d("@Time", "-->onResume");
        super.onResume();
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                getLocateImages();
                gamePresenter.setPos(pos1, pos2, pos3, pos4, pos5, pos6);

                int index1 = gamePresenter.checkLocate(event.getX(), event.getY(),
                        img_Hinh1.getWidth(), img_Hinh1.getHeight());
                point1 = gamePresenter.toaDoVe(index1, img_Hinh1.getWidth(), img_Hinh1.getHeight());
                Log.d(TAG, "Width , Height = " + img_Hinh1.getWidth() + "-" + img_Hinh1.getHeight());
                setTagPoint(index1, point1);
                tag1 = point1.getTagImage();
                startX = point1.getxLocate();
                startY = point1.getyLocate();
                Log.d(TAG, "StartX, StartY: " + startX + "-" + startY);
                break;

            case (MotionEvent.ACTION_MOVE):
                if (startX < 0 || startY < 0) {
                    break;
                }
                float x1 = event.getX();
                float y1 = event.getY();
                drawCurrent(x1, y1);
                drawView.invalidate();
                break;

            case (MotionEvent.ACTION_UP):
                int index2 = gamePresenter.checkLocate(event.getX(), event.getY(),
                        img_Hinh1.getWidth(), img_Hinh1.getHeight());
                if (index2 <= 0) {
                    wrongDraw();
                } else {
                    point2 = gamePresenter.toaDoVe(index2, img_Hinh1.getWidth(), img_Hinh1.getHeight());
                    setTagPoint(index2, point2);
                    tag2 = point2.getTagImage();
                    if (tag1 != null && tag2 != null) {
                        check = !tag1.isEmpty() && !tag2.isEmpty()
                                && !tag1.equals(tag2) && tag1.equalsIgnoreCase(tag2);
                    }
                    if (check == true) {
                        if (finishAnim == false) {
                            return true;
                        }
                        endX = point2.getxLocate();
                        endY = point2.getyLocate();
                        drawFinal();
                        turnOffClickImage(tag1);
                        turnOffClickImage(tag2);
                    } else {
                        wrongDraw();
                        turnOffClickImage(tag1);
                        turnOffClickImage(tag2);
                    }
                    drawView.invalidate();
                }
                if (ketqua == 3) {
                    succesfulMatching();
                    newGame();
                }
                resetToaDo();
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void drawCurrent(float x1, float y1) {
        if (ketqua == 0) {
            drawView.setLine1(startX, startY - statusHeight, x1, y1 - statusHeight);
        } else if (ketqua == 1) {
            drawView.setLine2(startX, startY - statusHeight, x1, y1 - statusHeight);
        } else if (ketqua == 2) {
            drawView.setLine3(startX, startY - statusHeight, x1, y1 - statusHeight);
        }
    }

    private void drawFinal() {
        if (ketqua == 0) {
            drawView.setLine1(startX, startY - statusHeight, endX, endY - statusHeight);
            ketqua++;
        } else if (ketqua == 1) {
            drawView.setLine2(startX, startY - statusHeight, endX, endY - statusHeight);
            ketqua++;
        } else if (ketqua == 2) {
            drawView.setLine3(startX, startY - statusHeight, endX, endY - statusHeight);
            ketqua++;
        }
    }

    private void wrongDraw() {
        if (ketqua == 0) {
            drawView.setLine1(0, 0, 0, 0);
        } else if (ketqua == 1) {
            drawView.setLine2(0, 0, 0, 0);
        } else if (ketqua == 2) {
            drawView.setLine3(0, 0, 0, 0);
        }
        drawView.invalidate();
    }

    private void resetToaDo() {
        startX = -1;
        startY = -1;
        endX = -1;
        endY = -1;
    }

    private void succesfulMatching() {
        /*Am hoan thanh*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSound.playPuzzleSound(Sound.PUZZLE_DONE);
                animtionSucces(img_Hinh1);
                animtionSucces(img_Hinh2);
                animtionSucces(img_Hinh3);
                animtionSucces(img_Hinh4);
                animtionSucces(img_Hinh5);
                animtionSucces(img_Hinh6);
            }
        }, 500);
    }

    private void newGame() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*Am game moi*/
                mSound.playPuzzleSound(Sound.PIECE_PICK_UP);
                if (topic == 5) {
                    startAnimNewGame();
                    newGameABC();
                } else {
                    newGameByTopic();
                }
            }
        }, 3000);
    }

    private void turnOffClickImage(String tag) {
        if (tag == null || tag.isEmpty()) {
            return;
        }
        if (tag.equals(img_Hinh1.getTag())) {
            checkResult(img_Hinh1);
        } else if (tag.equals(img_Hinh2.getTag())) {
            checkResult(img_Hinh2);
        } else if (tag.equals(img_Hinh3.getTag())) {
            checkResult(img_Hinh3);
        } else if (tag.equals(img_Hinh4.getTag())) {
            checkResult(img_Hinh4);
        } else if (tag.equals(img_Hinh5.getTag())) {
            checkResult(img_Hinh5);
        } else if (tag.equals(img_Hinh6.getTag())) {
            checkResult(img_Hinh6);
        }
    }

    private void checkResult(View view) {
        if (check == true) {
            view.setClickable(true);
            mSound.playPuzzleSound(Sound.PUZZLE_DONE2);
            animationMatchTrue(view);
        } else {
            mSound.playPuzzleSound(Sound.PIECE_LAY_DOWN);
            startAnimResultFalse(view);
        }
    }

    private void startAnimResultFalse(View imageView) {
        animation = AnimationUtils.loadAnimation(this, R.anim.shake_hoc_chu);
        imageView.startAnimation(animation);
    }

    private void animtionSucces(final View imageView) {
        ObjectAnimator.ofFloat(imageView, "rotation", 0, 25, -25, 20, -20, 15, -15, 10, -10, 5, -5, 3, -3, 0)
                .setDuration(2000)
                .start();
    }

    private void animationMatchTrue(final View imageView) {
        finishAnim = false;
        //TODO: Game moi.
        mSound.playPuzzleSound(Sound.PIECE_PICK_UP);
        ObjectAnimator.ofFloat(imageView, "rotation", 0, 20, -20, 15, -15, 10, -10, 5, -5, 0)
                .setDuration(700)
                .start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finishAnim = true;
            }
        }, 750);
    }

    private void setTagPoint(int indexHinh, PointDraw point) {
        switch (indexHinh) {
            case 1:
                point.setTagImage((String) img_Hinh1.getTag());
                break;
            case 2:
                point.setTagImage((String) img_Hinh2.getTag());
                break;
            case 3:
                point.setTagImage((String) img_Hinh3.getTag());
                break;
            case 4:
                point.setTagImage((String) img_Hinh4.getTag());
                break;
            case 5:
                point.setTagImage((String) img_Hinh5.getTag());
                break;
            case 6:
                point.setTagImage((String) img_Hinh6.getTag());
                break;
            default:
                break;
        }
    }

    private void newGameABC() {
        Log.d("@Time", "-->newGameABC");
        resetParams();
        nameArray.clear();
        /*startAnimNewGame();*/
        //Set anh vao tung Image
        gamePresenter.getNameImage(nameArray, listChuCai);
        setImageABC(img_Hinh1, nameArray.get(0), UP, "");
        setImageABC(img_Hinh3, nameArray.get(1), UP, "");
        setImageABC(img_Hinh5, nameArray.get(2), UP, "");

        //Set anh chu thuong.
        int index = gamePresenter.imageRandom(9);
        setImageABC(img_Hinh2, nameArray.get(index).toUpperCase(), LOW, "");
        nameArray.remove(index);

        index = gamePresenter.imageRandom(6);
        setImageABC(img_Hinh4, nameArray.get(index).toUpperCase(), LOW, "");
        nameArray.remove(index);
        setImageABC(img_Hinh6, nameArray.get(0).toUpperCase(), LOW, "");
        Log.d("@Time", "-->newGameABC done");
        root.setBackgroundResource(R.drawable.screens_03);
    }

    private void setImageABC(ImageView imageView, String name, String suf, String tag) {
        new LoadBitMapABCTask(imageView, topic).execute(name, tag, suf);
    }

    private void setImageByTopic(ImageView imageView, String img_name, String tag) {
        new LoadBitMapTask(imageView, topic).execute(img_name, tag);
    }

    private class LoadBitMapABCTask extends AsyncTask<String, Void, Bitmap> {
        String tag, suf;
        int topicID;
        String img_name;
        ImageView targetImgV;

        public LoadBitMapABCTask(ImageView pTargetImgV, int pTopicID) {
            topicID = pTopicID;
            this.targetImgV = pTargetImgV;
        }


        @Override
        protected Bitmap doInBackground(String... pStrings) {
            img_name = pStrings[0];
            tag = pStrings[1];
            suf = pStrings[2];
            Bitmap bitmap = Utils.getBitmapForGame(Global.pathImages, img_name.toLowerCase() + suf, PNG, true);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap pBitmap) {
            if (pBitmap != null) {
                targetImgV.setImageBitmap(pBitmap);
                if (topic == 5) {
                    targetImgV.setTag(img_name);
                    /*animationMatchTrue(targetImgV);*/
                } else {
                    targetImgV.setTag(tag);
                }
            } else {
                targetImgV.setImageResource(R.drawable.no_img_available);
            }
        }
    }


    private class LoadBitMapTask extends AsyncTask<String, Void, Bitmap> {
        ImageView targetImageView;
        int topicID;

        LoadBitMapTask(ImageView targetImgV, int topicID) {
            this.targetImageView = targetImgV;
            this.topicID = topicID;
        }

        String tag;

        @Override
        protected Bitmap doInBackground(String... pStrings) {
            String imgName = pStrings[0];
            tag = pStrings[1];
            Bitmap bitmap;
            if (topicID == 6) {
                bitmap = Utils.getBitmapForGame(Global.pathGameFindShadow, imgName, PNG, false);
            } else if (topicID == 7) {
                bitmap = Utils.getBitmapForGame(Global.pathImages, imgName, JPG, true);
            } else {
                bitmap = Utils.getBitmapForGame(Global.pathGameMatch, imgName, PNG, true);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap pBitmap) {
            if (pBitmap != null) {
                targetImageView.setImageBitmap(pBitmap);
                targetImageView.setTag(tag);
            } else {
                targetImageView.setImageResource(R.drawable.no_img_available);
            }
            if (topicID == 7) {
                targetImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        }
    }


    private void startAnimNewGame() {
        img_Hinh1.startAnimation(animation2);
        img_Hinh2.startAnimation(animation2);
        img_Hinh3.startAnimation(animation2);
        img_Hinh4.startAnimation(animation2);
        img_Hinh5.startAnimation(animation2);
        img_Hinh6.startAnimation(animation2);
    }

    private void resetParams() {
        ketqua = 0;
        drawView.setLine1(0, 0, 0, 0);
        drawView.setLine2(0, 0, 0, 0);
        drawView.setLine3(0, 0, 0, 0);
        drawView.invalidate();

        img_Hinh1.setClickable(false);
        img_Hinh2.setClickable(false);
        img_Hinh3.setClickable(false);
        img_Hinh4.setClickable(false);
        img_Hinh5.setClickable(false);
        img_Hinh6.setClickable(false);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_back_activity:
                mSound.releasePuzzleSound();
                if (NetworkReceiver.isConnected() && mInterstitialAds != null && AdsObserver.shouldShow(System.currentTimeMillis())) {
                    mInterstitialAds.show();
                } else {
                    finish();
                }
                break;
            case R.id.img_chu_de_noi:
                showDialogGame();
                break;
            default:
                break;
        }
    }

    private void showDialogGame() {
        if (topicGameDialog != null) {
            topicGameDialog.dismiss();
            topicGameDialog = null;
        }
        topicGameDialog = new TopicGameDialog(this);
        topicGameDialog.show(getSupportFragmentManager(), TopicGameDialog.TAG);
    }

    private void getListAllByTopic() {
        if (topic == 5) {
            startAnimNewGame();
            newGameABC();
        } else {
            listAll = gamePresenter.getAllListObj(topic);
            if (listAll != null) {
                if(listAll.size() == 0){
                    show_alert(this.getSupportFragmentManager(),this);
                    return;
                }
                newGameByTopic();
            } else {
                Toast.makeText(GameNoiHinhActivity.this, "Danh sach anh null", Toast.LENGTH_SHORT).show();
                //TODO: Danh sach anh ma Null -> Do something.
            }
        }
        mSound.playPuzzleSound(Sound.PIECE_PICK_UP);
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

    private void setImageChuDe() {
        resetParams();
        String key1 = "";
        String key2 = "";
        String key3 = "";
        int index = gamePresenter.imageRandom(9);
        int index1 = gamePresenter.imageRandom(6);
        startAnimNewGame();
        switch (topic) {
            case 1:
                key1 = listMatchObj.get(0).getPair() + "";
                key2 = listMatchObj.get(1).getPair() + "";
                key3 = listMatchObj.get(2).getPair() + "";
                setImageByTopic(img_Hinh1, listMatchObj.get(0).getImg_name(), key1 + "A");
                setImageByTopic(img_Hinh3, listMatchObj.get(1).getImg_name(), key2 + "A");
                setImageByTopic(img_Hinh5, listMatchObj.get(2).getImg_name(), key3 + "A");

                setImageByTopic(img_Hinh2, listMatchObj.get(index + 3).getImg_name(), listMatchObj.get(index + 3).getId() + "a");
                listMatchObj.remove(index + 3);
                setImageByTopic(img_Hinh4, listMatchObj.get(index1 + 3).getImg_name(), listMatchObj.get(index1 + 3).getId() + "a");
                listMatchObj.remove(index1 + 3);
                setImageByTopic(img_Hinh6, listMatchObj.get(3).getImg_name(), listMatchObj.get(3).getId() + "a");
                break;

            case 2:
                key1 = listMatchObj.get(0).getPair() + "";
                key2 = listMatchObj.get(1).getPair() + "";
                key3 = listMatchObj.get(2).getPair() + "";
                setImageByTopic(img_Hinh1, listMatchObj.get(0).getImg_name(), key1 + "A");
                setImageByTopic(img_Hinh3, listMatchObj.get(1).getImg_name(), key2 + "A");
                setImageByTopic(img_Hinh5, listMatchObj.get(2).getImg_name(), key3 + "A");

                setImageByTopic(img_Hinh2, listMatchObj.get(index + 3).getImg_name(), listMatchObj.get(index + 3).getId() + "a");
                listMatchObj.remove(index + 3);
                setImageByTopic(img_Hinh4, listMatchObj.get(index1 + 3).getImg_name(), listMatchObj.get(index1 + 3).getId() + "a");
                listMatchObj.remove(index1 + 3);
                setImageByTopic(img_Hinh6, listMatchObj.get(3).getImg_name(), listMatchObj.get(3).getId() + "a");
                break;

            case 3:
                key1 = listMatchObj.get(0).getCount() + "";
                key2 = listMatchObj.get(1).getCount() + "";
                key3 = listMatchObj.get(2).getCount() + "";
                setImageByTopic(img_Hinh1, listMatchObj.get(0).getImg_name(), key1 + "A");
                setImageByTopic(img_Hinh3, listMatchObj.get(1).getImg_name(), key2 + "A");
                setImageByTopic(img_Hinh5, listMatchObj.get(2).getImg_name(), key3 + "A");

                setImageByTopic(img_Hinh2, listMatchObj.get(index + 3).getImg_name(), listMatchObj.get(index + 3).getCount() + "a");
                listMatchObj.remove(index + 3);
                setImageByTopic(img_Hinh4, listMatchObj.get(index1 + 3).getImg_name(), listMatchObj.get(index1 + 3).getCount() + "a");
                listMatchObj.remove(index1 + 3);
                setImageByTopic(img_Hinh6, listMatchObj.get(3).getImg_name(), listMatchObj.get(3).getCount() + "a");
                break;

            case 4:
                key1 = listMatchObj.get(0).getCount() + "";
                key2 = listMatchObj.get(1).getCount() + "";
                key3 = listMatchObj.get(2).getCount() + "";
                setImageByTopic(img_Hinh2, listMatchObj.get(0).getImg_name(), key1 + "A");
                setImageByTopic(img_Hinh4, listMatchObj.get(1).getImg_name(), key2 + "A");
                setImageByTopic(img_Hinh6, listMatchObj.get(2).getImg_name(), key3 + "A");

                setImageByTopic(img_Hinh1, listMatchObj.get(index).getCount() + "", listMatchObj.get(index).getCount() + "a");
                listMatchObj.remove(index);
                setImageByTopic(img_Hinh3, listMatchObj.get(index1).getCount() + "", listMatchObj.get(index1).getCount() + "a");
                listMatchObj.remove(index1);
                setImageByTopic(img_Hinh5, listMatchObj.get(0).getCount() + "", listMatchObj.get(0).getCount() + "a");
                break;

            case 5:
                key1 = listMatchObj.get(0).getPair() + "";
                key2 = listMatchObj.get(1).getPair() + "";
                key3 = listMatchObj.get(2).getPair() + "";
                setImageByTopic(img_Hinh1, listMatchObj.get(0).getImg_name(), key1 + "A");
                setImageByTopic(img_Hinh3, listMatchObj.get(1).getImg_name(), key2 + "A");
                setImageByTopic(img_Hinh5, listMatchObj.get(2).getImg_name(), key3 + "A");

                setImageByTopic(img_Hinh2, listMatchObj.get(index + 3).getImg_name(), listMatchObj.get(index + 3).getId() + "a");
                listMatchObj.remove(index + 3);
                setImageByTopic(img_Hinh4, listMatchObj.get(index1 + 3).getImg_name(), listMatchObj.get(index1 + 3).getId() + "a");
                listMatchObj.remove(index1 + 3);
                setImageByTopic(img_Hinh6, listMatchObj.get(3).getImg_name(), listMatchObj.get(3).getId() + "a");
                break;

            case 6:
                key1 = listMatchObj.get(0).getImg_name();
                key2 = listMatchObj.get(1).getImg_name();
                key3 = listMatchObj.get(2).getImg_name();
                setImageByTopic(img_Hinh1, key1, key1 + "A");
                setImageByTopic(img_Hinh3, key2, key2 + "A");
                setImageByTopic(img_Hinh5, key3, key3 + "A");

                setImageByTopic(img_Hinh2, listMatchObj.get(index).getImg_name() + "s", listMatchObj.get(index).getImg_name() + "a");
                listMatchObj.remove(index);
                setImageByTopic(img_Hinh4, listMatchObj.get(index1).getImg_name() + "s", listMatchObj.get(index1).getImg_name() + "a");
                listMatchObj.remove(index1);
                setImageByTopic(img_Hinh6, listMatchObj.get(0).getImg_name() + "s", listMatchObj.get(0).getImg_name() + "a");
                break;

            case 7:
                if (Global.getLanguage() == Global.VN) {
                    key1 = listMatchObj.get(0).getLetter_vn();
                    key2 = listMatchObj.get(1).getLetter_vn();
                    key3 = listMatchObj.get(2).getLetter_vn();
                    Log.d(TAG, "Key1 = " + key1);
                    Log.d(TAG, "Key2 = " + key2);
                    Log.d(TAG, "Key3 = " + key3);
                    // Set anh ve 1:
                    setImageByTopic(img_Hinh2, listMatchObj.get(0).getImg_name(), key1 + "A");
                    setImageByTopic(img_Hinh4, listMatchObj.get(1).getImg_name(), key2 + "A");
                    setImageByTopic(img_Hinh6, listMatchObj.get(2).getImg_name(), key3 + "A");

                    setImageABC(img_Hinh1, listMatchObj.get(index).getLetter_vn(), LOW, listMatchObj.get(index).getLetter_vn() + "a");
                    listMatchObj.remove(index);
                    setImageABC(img_Hinh3, listMatchObj.get(index1).getLetter_vn(), LOW, listMatchObj.get(index1).getLetter_vn() + "a");
                    listMatchObj.remove(index1);
                    setImageABC(img_Hinh5, listMatchObj.get(0).getLetter_vn(), LOW, listMatchObj.get(0).getLetter_vn() + "a");

                } else {
                    key1 = listMatchObj.get(0).getLetter_en();
                    key2 = listMatchObj.get(1).getLetter_en();
                    key3 = listMatchObj.get(2).getLetter_en();

                    setImageByTopic(img_Hinh2, listMatchObj.get(0).getImg_name(), key1 + "A");
                    setImageByTopic(img_Hinh4, listMatchObj.get(1).getImg_name(), key2 + "A");
                    setImageByTopic(img_Hinh6, listMatchObj.get(2).getImg_name(), key3 + "A");

                    setImageABC(img_Hinh1, listMatchObj.get(index).getLetter_en(), LOW, listMatchObj.get(index).getLetter_en() + "a");
                    listMatchObj.remove(index);
                    setImageABC(img_Hinh3, listMatchObj.get(index1).getLetter_en(), LOW, listMatchObj.get(index1).getLetter_en() + "a");
                    listMatchObj.remove(index1);
                    setImageABC(img_Hinh5, listMatchObj.get(0).getLetter_en(), LOW, listMatchObj.get(0).getLetter_en() + "a");
                }
                break;
        }
    }

    public void newGameByTopic() {
        listMatchObj = gamePresenter.newGameByTopic(listAll, topic);
        if (topic == 6 || topic == 7 || topic == 4 && listMatchObj.size() == 3) {
            setImageChuDe();
        } else if (listMatchObj.size() == 6) {
            setImageChuDe();
        } else {
            Toast.makeText(this, "Danh sach cau hoi chua duoc chuan bi", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTopicClick(int id, int lock, boolean pDatabase_missing) {
        Log.i(GameNoiHinhActivity.class.getSimpleName(), "--->id topic:" + id);
        if (id != 0) {
            topic = id;
            switch (topic) {
                case 1:
                    root.setBackgroundResource(R.drawable.bg6);
                    updateBackGround(1);
                    break;

                case 2:
                    root.setBackgroundResource(R.drawable.bg1);
                    updateBackGround(2);
                    break;

                case 3:
                    root.setBackgroundResource(R.drawable.bgr_jobs);
                    updateBackGround(1);
                    break;

                case 4:
                    root.setBackgroundResource(R.drawable.bgr_kitchen);
                    updateBackGround(1);
                    break;

                case 5:
                    root.setBackgroundResource(R.drawable.screens_03);
                    updateBackGround(2);
                    break;

                case 6:
                    root.setBackgroundResource(R.drawable.bg2);
                    updateBackGround(2);
                    break;

                case 7:
                    root.setBackgroundResource(R.drawable.bg5);
                    updateBackGround(2);
                    break;
                default:
                    break;
            }
            getListAllByTopic();
        }
    }

    private void updateBackGround(int i) {
        if (i < 0) {
            return;
        }
        if (i == 1) {
            ln_hinh1.setBackgroundResource(R.drawable.border_matching_selector);
            ln_hinh2.setBackgroundResource(R.drawable.border_matching_selector);
            ln_hinh3.setBackgroundResource(R.drawable.border_matching_selector);
            ln_hinh4.setBackgroundResource(R.drawable.border_matching_selector);
            ln_hinh5.setBackgroundResource(R.drawable.border_matching_selector);
            ln_hinh6.setBackgroundResource(R.drawable.border_matching_selector);
        } else {
            ln_hinh1.setBackgroundResource(R.drawable.boder_item_selector);
            ln_hinh2.setBackgroundResource(R.drawable.boder_item_selector);
            ln_hinh3.setBackgroundResource(R.drawable.boder_item_selector);
            ln_hinh4.setBackgroundResource(R.drawable.boder_item_selector);
            ln_hinh5.setBackgroundResource(R.drawable.boder_item_selector);
            ln_hinh6.setBackgroundResource(R.drawable.boder_item_selector);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mSound.releasePuzzleSound();
        if (NetworkReceiver.isConnected() && mInterstitialAds != null && AdsObserver.shouldShow(System.currentTimeMillis())) {
            mInterstitialAds.show();
        } else {
            finish();
        }
    }

    @Override
    public void updateUIForLanguage() {
        if (Global.getLanguage() == Global.VN) {
            txt_notification.setText("Nối Hình");
            if (topic == 5) {
                gamePresenter.fillListVN(listChuCai, names);
                newGameABC();
            } else if (topic == 7) {
                newGameByTopic();
            }
        } else {
            txt_notification.setText("Matching Object");
            if (topic == 5) {
                gamePresenter.fillListEN(listChuCai, names);
                newGameABC();
            } else if (topic == 7) {
                newGameByTopic();
            }
        }
    }

    @Override
    public void toDoBackListener() {
        finish();
    }
}
