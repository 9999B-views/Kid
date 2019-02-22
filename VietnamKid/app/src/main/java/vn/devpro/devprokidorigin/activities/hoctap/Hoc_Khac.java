package vn.devpro.devprokidorigin.activities.hoctap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.activities.MainActivity;
import vn.devpro.devprokidorigin.adapters.CardListAdapter;
import vn.devpro.devprokidorigin.adapters.ItemAdapter;
import vn.devpro.devprokidorigin.databases.DBHelper;
import vn.devpro.devprokidorigin.dialogs.LoadingDialog;
import vn.devpro.devprokidorigin.interfaces.BackListener;
import vn.devpro.devprokidorigin.interfaces.MyCompleteSound;
import vn.devpro.devprokidorigin.models.ButtonClick;
import vn.devpro.devprokidorigin.models.FixedSpeedScroller;
import vn.devpro.devprokidorigin.models.Sound;
import vn.devpro.devprokidorigin.models.entity.TopicItem;
import vn.devpro.devprokidorigin.models.interfaces.ChangeLanguageCallback;
import vn.devpro.devprokidorigin.models.interfaces.IonImgClick;
import vn.devpro.devprokidorigin.utils.AdMob.AdsObserver;
import vn.devpro.devprokidorigin.utils.AdMob.InterstitialAds;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.utils.IKey;
import vn.devpro.devprokidorigin.utils.NetworkReceiver;
import vn.devpro.devprokidorigin.utils.Utils;

public class Hoc_Khac extends AppCompatActivity implements ChangeLanguageCallback, BackListener, MyCompleteSound {
    private Button langBtn;
    private ImageView imgAnswer1, imgAnswer2, imgAnswer3, imgAnswer4;
    private TextView tvItem, textView;
    private ViewPager viewPager;
    private RecyclerView recyclerView;
    private CardListAdapter cardListAdapter;
    private ArrayList<TopicItem> list;
    private ArrayList<TopicItem> listItemForDialog = new ArrayList<>();
    private DBHelper controller;
    private Dialog dialog;
    private int itemW;
    private int topic_id;
    private InterstitialAds interstitialAds;
    private LinearLayout lnParent;
    private View view;
    private Animation animation;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoc_khac);
        Button backBtn = findViewById(R.id.btnBack_hoctap_khac);
        Button homeBtn = findViewById(R.id.btnBackHome);
        langBtn = findViewById(R.id.btnLang);
        Button soundBtn = findViewById(R.id.btnSound);
        Button learBtn = findViewById(R.id.btLuyenTap);
        tvItem = findViewById(R.id.tvItem);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/boosternextfy_bold.ttf");
        tvItem.setTypeface(face);
        recyclerView = findViewById(R.id.recylerView);
        backBtn.setOnClickListener(mOnClickListener);
        Intent triggerIntent = getIntent();
        topic_id = triggerIntent.getIntExtra(IKey.TOPIC_ID, 0);
        homeBtn.setOnClickListener(mOnClickListener);
        langBtn.setOnClickListener(mOnClickListener);
        soundBtn.setOnClickListener(mOnClickListener);
        learBtn.setOnClickListener(mOnClickListener);
        Global.updateButtonLanguage(langBtn);
        Global.updateButtonSound(soundBtn);
        ButtonClick.Sound soundListener = new ButtonClick.Sound();
        ButtonClick.Language langListener = new ButtonClick.Language(this);
        langBtn.setOnClickListener(langListener);
        soundBtn.setOnClickListener(soundListener);
        initDynamicSize();
        new setDataForLearn(this).execute(topic_id);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                interstitialAds = InterstitialAds.loadInterstitialAds(Hoc_Khac.this, Hoc_Khac.this);
            }
        }, 500);
        Sound.getInstance(Global.mContext).initHocKhacCorrect();
    }


    private void initDynamicSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int PX_SCR_H = displayMetrics.heightPixels;
        int itemH = (int) (PX_SCR_H - (0.78 * PX_SCR_H + Utils.convertDpToPixel(1f, getBaseContext())));
        itemW = (int) (itemH * 1.618);
    }


    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack_hoctap_khac:
                    updateShowRatio();
                    if (NetworkReceiver.isConnected() && AdsObserver.shouldShow(System.currentTimeMillis())) {
                        interstitialAds.show();
                    } else {
                        finish();
                    }
                    break;
                case R.id.btnBackHome:
                    Intent intent = new Intent(Hoc_Khac.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                case R.id.img_htk_1:
                    checkMatch(view);
                    break;
                case R.id.img_htk_2:
                    checkMatch(view);
                    break;
                case R.id.img_htk_3:
                    checkMatch(view);
                    break;
                case R.id.img_htk_4:
                    checkMatch(view);
                    break;
                case R.id.btLuyenTap:
                    Utils.btn_click_animate(view);
                    updateUIDialog(dialog);
                    dialog.show();
                    playSoundDialogIn();
                    break;
            }
        }
    };


    int dem = 0;

    private void checkMatch(View v) {

        if (v.getTag() == textView.getTag()) {
            initUIDialog();
            animDialog();
            dem++;
            playSoundDialog(dem);
            Sound.getInstance(Global.mContext).playPuzzleSound(Sound.PIECE_CORRECT);
        } else {
            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake_hoc_chu));
        }
    }

    @SuppressLint("InflateParams")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void intitDialog() {
        dialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        view = LayoutInflater.from(getBaseContext()).inflate(R.layout.dialog_htk, null);
        dialog.setContentView(view);

        Button btnClose = dialog.findViewById(R.id.btnCloseDialog);
        final Button btnLang = dialog.findViewById(R.id.btnLangDialog);
        ImageView replaySound = dialog.findViewById(R.id.replaySound);
        textView = dialog.findViewById(R.id.mgsTV);
        lnParent = dialog.findViewById(R.id.lnParent);
        imgAnswer1 = dialog.findViewById(R.id.img_htk_1);
        imgAnswer2 = dialog.findViewById(R.id.img_htk_2);
        imgAnswer3 = dialog.findViewById(R.id.img_htk_3);
        imgAnswer4 = dialog.findViewById(R.id.img_htk_4);

        replaySound.setBackgroundResource(R.drawable.soundon);
        initUIDialog();
        animDialog();
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Global.updateButtonLanguage(langBtn);
                updateUIForLanguage();
            }
        });
        btnLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.changeLanguage();
                Global.updateButtonLanguage(btnLang);
                if (Global.getLanguage() == Global.VN) {
                    textView.setText(listItemForDialog.get(1).getTitle_vn());
                } else {
                    textView.setText(listItemForDialog.get(1).getTitle_en());
                }
                view.invalidate();
            }
        });
        replaySound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sound.getInstance(Global.mContext).setMyCompleteSound(Hoc_Khac.this);
                Sound.getInstance(Global.mContext).playEncrytedFile(DBHelper.getInstance(Global.mContext).soundForDiaLogHTK(topic_id));
            }
        });
    }


    private void animDialog() {
        animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.fadein);
        lnParent.startAnimation(animation);
        view.invalidate();
    }

    private void playSoundDialog(int count) {
        if (count % 3 == 0 || count == 0) {
            if (!animation.hasEnded()) {
                Sound.getInstance(Global.mContext).setMyCompleteSound(this);
                Sound.getInstance(Global.mContext).playEncrytedFile(DBHelper.getInstance(Global.mContext).soundForDiaLogHTK(topic_id));
            }
        }
        else {
            if(!animation.hasEnded()){
                if (Global.getLanguage() == Global.VN) {
                    Sound.getInstance(Global.mContext).playEncrytedFile(listItemForDialog.get(1).getSound_vn());
                } else {
                    Sound.getInstance(Global.mContext).playEncrytedFile(listItemForDialog.get(1).getSound_en());
                }
            }
        }

    }

    private void playSoundDialogIn() {
        if (!animation.hasEnded()) {
            Sound.getInstance(Global.mContext).setMyCompleteSound(this);
            Sound.getInstance(Global.mContext).playEncrytedFile(DBHelper.getInstance(Global.mContext).soundForDiaLogHTK(topic_id));
        }
    }

    @Override
    public void updateUIForLanguage() {
        if (Global.getLanguage() == Global.EN) {
            tvItem.setText(list.get(currentPosition).getTitle_en());
        } else {
            tvItem.setText(list.get(currentPosition).getTitle_vn());
        }
    }

    public int currentPosition;

    @Override
    public void toDoBackListener() {
        finish();
    }

    public void onComplete(MediaPlayer mediaPlayer) {
        if (Global.getLanguage() == Global.VN) {
            Sound.getInstance(Global.mContext).playEncrytedFile(listItemForDialog.get(1).getSound_vn());
        } else {
            Sound.getInstance(Global.mContext).playEncrytedFile(listItemForDialog.get(1).getSound_en());
        }
    }

    @SuppressLint("StaticFieldLeak")
    class setDataForLearn extends AsyncTask<Integer, Void, ArrayList<TopicItem>> {
        setDataForLearn(Activity activity) {
            this.activity = activity;
        }

        LoadingDialog dialog = new LoadingDialog();
        Activity activity;

        @Override
        protected void onPreExecute() {
            controller = DBHelper.getInstance(activity);
            dialog.setThongBao("Loading...");
            dialog.show(getSupportFragmentManager(), null);
        }

        @Override
        protected ArrayList<TopicItem> doInBackground(Integer... integers) {
            int data = integers[0];
            list = controller.getTopicByid(data);
            for (int i = 0; i < list.size(); i++) {
                byte[] bytes = Utils.decodeFileImage(Utils.getByteArrayFromSD(Global.pathImages, list.get(i).getImg_name(), null));
                list.get(i).setBitImg(bytes);
            }

            return list;
        }

        @Override
        protected void onPostExecute(final ArrayList<TopicItem> topicItems) {
            list = topicItems;
            if (dialog != null) {
                dialog.dismiss();
            }
            cardListAdapter = new CardListAdapter(Hoc_Khac.this, topicItems, itemW, new IonImgClick() {

                @Override
                public void onClick(int pos) {
                    viewPager.setCurrentItem(pos, true);
                }
            });
            if (Global.getLanguage() == Global.VN && topicItems.get(0).getBitImg() != null) {
                tvItem.setText(topicItems.get(0).getTitle_vn());
            } else {
                tvItem.setText(topicItems.get(0).getTitle_en());
            }
            final LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
            final LinearSmoothScroller smoothScroller = new LinearSmoothScroller(activity) {
                private static final float SPEED = 300f;

                @Override
                protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                    return SPEED / displayMetrics.densityDpi;
                }
            };
            layoutManager.setSmoothScrollbarEnabled(true);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(cardListAdapter);
            cardListAdapter.notifyDataSetChanged();
            viewPager = findViewById(R.id.viewPager_item);
            viewPager.setOffscreenPageLimit(topicItems.size());
            for (int i = 0; i < topicItems.size(); i++) {
                viewPager.setAdapter(new ItemAdapter(topicItems, viewPager.getWidth(), viewPager.getHeight(), topic_id, Hoc_Khac.this));
                try {
                    Field mScroller;
                    mScroller = ViewPager.class.getDeclaredField("mScroller");
                    mScroller.setAccessible(true);
                    FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext());
                    mScroller.set(viewPager, scroller);
                } catch (NoSuchFieldException ignored) {
                } catch (IllegalArgumentException ignored) {
                } catch (IllegalAccessException ignored) {
                }
            }

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                public void onPageSelected(int position) {
                    currentPosition = position;
                    int i = layoutManager.findFirstVisibleItemPosition();
                    int k = layoutManager.findLastCompletelyVisibleItemPosition();
                    int x = layoutManager.findLastVisibleItemPosition();
                    if (currentPosition <= x + 2 && currentPosition <= i + 6 && currentPosition >= k - 5) {
                        smoothScroller.setTargetPosition(position);
                        layoutManager.startSmoothScroll(smoothScroller);
                    } else {
                        recyclerView.smoothScrollToPosition(position);
                        cardListAdapter.setSelectedItem(position);
                    }

                    if (Global.getLanguage() == Global.VN) {
                        String ten = topicItems.get(position).getSound_vn();
                        tvItem.setText(topicItems.get(position).getTitle_vn());
                        Sound.getInstance(getApplicationContext()).playEncrytedFile(ten);
                    } else {
                        String ten = topicItems.get(position).getSound_en();
                        tvItem.setText(topicItems.get(position).getTitle_en());
                        Sound.getInstance(getApplicationContext()).playEncrytedFile(ten);
                    }
                    // FIXME: 6/9/2018
                    int showRatio = topicItems.get(position).getShow_ratio();
                    showRatio++;
                    if (showRatio<4) {
                        topicItems.get(position).setShow_ratio(showRatio);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intitDialog();
            }
        }

    }

    private void updateShowRatio() {
        DBHelper.getInstance(this).updateShowratio(list);
    }

    private void randomImg() {
        int so1 = 0, so2 = 0, so3 = 0, so4;
        Random r = new Random();
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                so1 = r.nextInt(list.size());
                listItemForDialog.add(i, list.get(so1));
            } else if (i == 1) {
                so2 = r.nextInt(list.size());
                while (so2 == so1) {
                    so2 = r.nextInt(list.size());
                }
                listItemForDialog.add(i, list.get(so2));
            } else if (i == 2) {
                so3 = r.nextInt(list.size());
                while (so3 == so1 || so3 == so2) {
                    so3 = r.nextInt(list.size());
                }
                listItemForDialog.add(i, list.get(so3));
            } else {
                so4 = r.nextInt(list.size());
                while (so4 == so1 || so4 == so2 || so4 == so3) {
                    so4 = r.nextInt(list.size());
                }
                listItemForDialog.add(i, list.get(so4));
            }
        }
    }

    private void initUIDialog() {
        randomImg();
        Random r = new Random();
        int listInt[] = new int[4];
        textView.setTag(1);
        if (Global.getLanguage() == Global.VN) {
            textView.setText(listItemForDialog.get(1).getTitle_vn());
        } else {
            textView.setText(listItemForDialog.get(1).getTitle_en());
        }
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                listInt[i] = r.nextInt(4);
            } else if (i == 1) {
                while (listInt[1] == listInt[0]) {
                    listInt[1] = r.nextInt(4);
                }
            } else if (i == 2) {
                while (listInt[2] == listInt[1] || listInt[2] == listInt[0]) {
                    listInt[2] = r.nextInt(4);
                }
            } else {
                while (listInt[3] == listInt[2] || listInt[3] == listInt[1] || listInt[3] == listInt[0]) {
                    listInt[3] = r.nextInt(4);
                }
            }
        }
        imgAnswer1.setImageBitmap(Utils.displayImageForItem(listItemForDialog.get(listInt[0]).getBitImg()));
        imgAnswer2.setImageBitmap(Utils.displayImageForItem(listItemForDialog.get(listInt[1]).getBitImg()));
        imgAnswer3.setImageBitmap(Utils.displayImageForItem(listItemForDialog.get(listInt[2]).getBitImg()));
        imgAnswer4.setImageBitmap(Utils.displayImageForItem(listItemForDialog.get(listInt[3]).getBitImg()));
        imgAnswer1.setTag(listInt[0]);
        imgAnswer2.setTag(listInt[1]);
        imgAnswer3.setTag(listInt[2]);
        imgAnswer4.setTag(listInt[3]);
        imgAnswer1.setOnClickListener(mOnClickListener);
        imgAnswer2.setOnClickListener(mOnClickListener);
        imgAnswer3.setOnClickListener(mOnClickListener);
        imgAnswer4.setOnClickListener(mOnClickListener);
    }

    private void updateUIDialog(Dialog dialog) {
        Button btnLang = dialog.findViewById(R.id.btnLangDialog);
        Global.updateButtonLanguage(btnLang);
        if (Global.getLanguage() == Global.VN) {
            textView.setText(listItemForDialog.get(1).getTitle_vn());
        } else {
            textView.setText(listItemForDialog.get(1).getTitle_en());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateShowRatio();
        if (NetworkReceiver.isConnected() && AdsObserver.shouldShow(System.currentTimeMillis())) {
            interstitialAds.show();
        } else {
            finish();
        }
    }

    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Sound.getInstance(getBaseContext()).releaseHocKhacCorrect();
    }


}
