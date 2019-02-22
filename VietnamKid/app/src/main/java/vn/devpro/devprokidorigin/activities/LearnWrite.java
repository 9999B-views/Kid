package vn.devpro.devprokidorigin.activities;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.lang.reflect.Field;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.adapters.LearnWriteAdapter;
import vn.devpro.devprokidorigin.interfaces.BackListener;
import vn.devpro.devprokidorigin.models.DrawView;
import vn.devpro.devprokidorigin.utils.AdMob.AdsObserver;
import vn.devpro.devprokidorigin.utils.AdMob.InterstitialAds;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.utils.NetworkReceiver;
import vn.devpro.devprokidorigin.utils.Utils;

public class LearnWrite extends FragmentActivity implements View.OnClickListener, BackListener {
    private final String TAG = LearnWrite.class.getSimpleName();
    private final String listLow[] = {"a", "ă", "â", "b", "c", "d", "đ", "e", "ê", "f", "g", "h",
            "i", "j", "k", "l", "m", "n", "o", "ô", "ơ", "p", "q", "r",
            "s", "t", "u", "ư", "v", "w", "x", "y", "z"};
    private final String listUp[] = {"A", "Ă", "Â", "B", "C", "D", "Đ", "E", "Ê", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "Ô", "Ơ", "P", "Q", "R",
            "S", "T", "U", "Ư", "V", "W", "X", "Y", "Z"};
    private Button btn_clear, btn_Back;
    private int position;
    private ImageView display, txView;
    private LearnWriteAdapter adapter;
    private GridView gridViewLeft;
    private DrawView drawView;
    private Bitmap bitmap;
    private GridView gridViewRight;
    private float screenWidth = 0;
    private float screenHeight = 0;
    private int lineWidth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_write);
        initView();
        bitmap = drawView.getBitmap();
        final File pathImg = Global.pathGameLearnWrite;
        final String swap[] = {"aw", "aa", "dd", "ee", "oo", "ow", "uw"};
        getWidthHeightScreen();
        float itemHeight = (float) (0.8 * screenWidth) / 4;
        float spacing = (float) (0.2 * screenWidth);
        float itemSpacing = (float) spacing / 3;
        lineWidth = (int) (0.8 * itemSpacing);
        drawView = findViewById(R.id.drawView);
        btn_Back = findViewById(R.id.btn_Back);
        drawView.setLine(lineWidth);
        btn_Back.setOnClickListener(this);

        adapter = new LearnWriteAdapter(listLow, getLayoutInflater(), itemHeight);
        gridViewLeft.setVerticalSpacing((int) itemSpacing);
        gridViewLeft.setAdapter(adapter);

        adapter = new LearnWriteAdapter(listUp, getLayoutInflater(), itemHeight);
        gridViewRight.setVerticalSpacing((int) itemSpacing);
        gridViewRight.setAdapter(adapter);

        gridViewLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                drawView.clear();
                String name;

                if (position == 1) {
                    name = swap[0];
                } else if (position == 2) {
                    name = swap[1];
                } else if (position == 6) {
                    name = swap[2];
                } else if (position == 8) {
                    name = swap[3];
                } else if (position == 19) {
                    name = swap[4];
                } else if (position == 20) {
                    name = swap[5];
                } else if (position == 27) {
                    name = swap[6];
                } else {
                    name = listLow[position];
                }
                Bitmap bmp = Utils.getBitmapForGame(pathImg, name, ".png", false);
                if (bmp == null) {
                    Utils.showToast(LearnWrite.this, "Data game bị lỗi");
                } else {
                    display.setImageBitmap(Bitmap.createScaledBitmap(bmp, bmp.getWidth(), bmp.getHeight(), false));
                    display.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.scale));
                }
            }
        });

        gridViewRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                drawView.clear();
                String name;
                String extend = "_up";
                if (position == 1) {
                    name = swap[0];
                } else if (position == 2) {
                    name = swap[1];
                } else if (position == 6) {
                    name = swap[2];
                } else if (position == 8) {
                    name = swap[3];
                } else if (position == 19) {
                    name = swap[4];
                } else if (position == 20) {
                    name = swap[5];
                } else if (position == 27) {
                    name = swap[6];
                } else {
                    name = listLow[position];
                }
                Bitmap bmp = Utils.getBitmapForGame(pathImg, name + extend, ".png", false);
                if (bmp == null) {
                    Utils.showToast(LearnWrite.this, "Data game bị lỗi");
                } else {
                    display.setImageBitmap(Bitmap.createScaledBitmap(bmp, bmp.getWidth(), bmp.getHeight(), false));
                    display.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.scale));
                }
            }
        });

        Handler loadAdsHandler = new Handler();

        loadAdsHandler.postDelayed(loadAdsRunner, 500);
    }

    private InterstitialAds mInterstitialAds;
    Runnable loadAdsRunner = new Runnable() {
        @Override
        public void run() {
            mInterstitialAds = InterstitialAds.loadInterstitialAds(LearnWrite.this, LearnWrite.this);
        }
    };

    private void initView() {
        btn_clear = findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(this);
        display = findViewById(R.id.display);
        gridViewLeft = findViewById(R.id.gridViewLeft);
        gridViewRight = findViewById(R.id.grid_right);
        drawView = findViewById(R.id.drawView);
    }

    private void getWidthHeightScreen() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.heightPixels;
        screenWidth = screenWidth - Utils.convertDpToPixel(140, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clear:
                drawView.clear();
                break;
            case R.id.btn_Back:
                if (NetworkReceiver.isConnected() && mInterstitialAds != null && AdsObserver.shouldShow(System.currentTimeMillis())) {
                    mInterstitialAds.show();
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void toDoBackListener() {
        finish();
    }
}
