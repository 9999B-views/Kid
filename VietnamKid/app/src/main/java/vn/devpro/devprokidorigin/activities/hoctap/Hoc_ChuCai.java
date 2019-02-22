package vn.devpro.devprokidorigin.activities.hoctap;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.activities.MainActivity;
import vn.devpro.devprokidorigin.activities.game.PuzzleActivity;
import vn.devpro.devprokidorigin.adapters.BangChuCaiAdapter;
import vn.devpro.devprokidorigin.interfaces.ItemClick;
import vn.devpro.devprokidorigin.databases.DBHelper;
import vn.devpro.devprokidorigin.dialogs.NhanBietChuDialog;
import vn.devpro.devprokidorigin.interfaces.BackListener;
import vn.devpro.devprokidorigin.models.ButtonClick;
import vn.devpro.devprokidorigin.models.entity.hocchucai.LetterModelSmall;
import vn.devpro.devprokidorigin.models.interfaces.ChangeLanguageCallback;
import vn.devpro.devprokidorigin.utils.AdMob.AdsObserver;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.utils.NetworkReceiver;
import vn.devpro.devprokidorigin.utils.Utils;
import vn.devpro.devprokidorigin.utils.AdMob.InterstitialAds;

public class Hoc_ChuCai extends AppCompatActivity implements ItemClick, View.OnClickListener, ChangeLanguageCallback, BackListener {
    private final String TAG = Hoc_ChuCai.class.getSimpleName();
    private final int GRIDVIEW_PADDING = 40;
    private final int GRIDVIEW_PADDING_ITEM = 30;
    private final float ITEM_RATIO = 1.27f;
    private int[] order_index_vn = {1, 2, 3, 16, 17, 18, 19, 4, 5, 20, 21, 6, 7, 8, 22, 23, 9, 10, 11, 24, 25, 26, 12, 13, 27, 28, 14, 15, 29};
    private int[] order_index_en = {1, 2, 14, 15, 3, 4, 16, 17, 5, 6, 18, 19, 7, 8, 20, 21, 9, 10, 22, 23, 11, 12, 24, 25, 13, 26};

    ScreenType screenType;

    private enum ScreenType {
        SCREEN_TYPE_PHONE,
        SCREEN_TYPE_TABLET;
    }

    private boolean isVietNamese = true;

    private int countLang = 0;
    private final String listChuCai[] = {"a", "aw", "aa", "b", "c", "d", "dd", "e", "ee", "f", "g", "h",
            "i", "j", "k", "l", "m", "n", "o", "oo", "ow", "p", "q", "r",
            "s", "t", "u", "uw", "v", "w", "x", "y", "z"};
    private ImageButton btnBack, btnLoa, btnNgonNgu, btnHome;
    private ConstraintLayout rootLayout;
    private HorizontalScrollView scrollView;
    private LinearLayout parentGridView;
    private GridView bangChuCaiGridView;
    private GridView bangChuCaiGridView2;
    private List<LetterModelSmall> chuCaiList;
    private List<LetterModelSmall> tmpList;
    private BangChuCaiAdapter bangChuCaiAdapter;
    private NhanBietChuDialog chucaiDialog;
    private DBHelper controller;
    private String checkScreeen = "";
    private InterstitialAds interstitialAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoc_chu_cai);
        initViews();
        controller = DBHelper.getInstance(this);
        // first init for btn language and sound
        Global.updateButtonLanguage(btnNgonNgu);
        Global.updateButtonSound(btnLoa);
        screenType = checkScreenSize();
        chuCaiList = initBangChuCai();
        initCommonGridView();
        //TODO: Nhac nen.
        Global.updateButtonSound(btnLoa);
        //request permission
        //requestWriteExternalPermission();

        // tải quảng cáo cho sự kiện back
        // fix Nam: chuyển hàm loadInterstitialAds vào class InterstitialAds
        //        loadInterstitialAds();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                interstitialAds = InterstitialAds.loadInterstitialAds(Hoc_ChuCai.this, Hoc_ChuCai.this);
            }
        }, 500);
    }


    private void initViews() {
        rootLayout = findViewById(R.id.root_layout);
//        rootLayout.setPadding(GRIDVIEW_PADDING_ITEM, GRIDVIEW_PADDING_ITEM, GRIDVIEW_PADDING_ITEM, GRIDVIEW_PADDING_ITEM);
        parentGridView = findViewById(R.id.parentGridview);
        parentGridView.setPadding(GRIDVIEW_PADDING, 0, GRIDVIEW_PADDING, 0);

        btnBack = findViewById(R.id.btnBack);
        btnLoa = findViewById(R.id.btnLoa);
        btnHome = findViewById(R.id.btnHome);
        btnNgonNgu = findViewById(R.id.btnNgonNgu);
        scrollView = findViewById(R.id.scrollView);
        bangChuCaiGridView = findViewById(R.id.bccGridView);
        bangChuCaiGridView2 = findViewById(R.id.bccGridView2);

        btnBack.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        //===============================
        ButtonClick.Sound soundListener = new ButtonClick.Sound();
        ButtonClick.Language langListener = new ButtonClick.Language(this);
        btnNgonNgu.setOnClickListener(langListener);
        btnLoa.setOnClickListener(soundListener);
    }

    private void initCommonGridView() {
        List<LetterModelSmall> list1 = new ArrayList<>();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        screenWidth = screenWidth - GRIDVIEW_PADDING * 2;
        screenHeight = screenHeight
                - Utils.convertDpToPixel(getResources().getDimension(R.dimen.size_button_hoc_chu) + 8, displayMetrics);
        int numColums = 0;
        int numRows = 0;
        int totalNumColums = 0;
        int horizontalSpacingItem = 0;
        int verticalSpacingItem = 0;
        float horizontalSpacing = 0;
        float verticalSpacing = 0;
        float tmpItemWidth = 0;
        float tmpItemHeight = 0;
        float itemWidth = 0;
        float itemHeight = 0;
        int totalWidth = 0;
        int totalHeight = 0;

        int tmpTotalWidth = 0;
        int tmpTotalHeight = 0;
        LinearLayout.LayoutParams gridViewparams;

        if (screenType == ScreenType.SCREEN_TYPE_PHONE) {
            bangChuCaiGridView2.setVisibility(View.INVISIBLE);
            bangChuCaiGridView.setPadding(GRIDVIEW_PADDING, 0, GRIDVIEW_PADDING, 0);
            /*Doi voi man hinh 480 x 854 chua ok*/
            /*Ok voi man hinh 1*/
            for (int i = 0; i < chuCaiList.size(); i++) {
                list1.add(chuCaiList.get(i));
            }
            numColums = 4;
            numRows = 2;
            totalNumColums = Global.getLanguage() == Global.VN ? 15 : 13;
            // Uoc tinh width/height cua mot o
            tmpItemWidth = (screenWidth * 0.9f) / numColums;
            tmpItemHeight = (screenHeight * 0.9f) / numRows;
            // Tinh kich thuoc that theo ti le width/height = 1.27
            itemHeight = calculateItemHeight(tmpItemWidth, tmpItemHeight);
            itemWidth = itemHeight * ITEM_RATIO;
            // Tinh khoang cach danh cho spacing
            horizontalSpacing = screenWidth - itemWidth * numColums;
            verticalSpacing = screenWidth - itemHeight * numRows;
            // Tinh khoang cach giua cac cot
            horizontalSpacingItem = (int) (horizontalSpacing / (numColums - 1));
            verticalSpacingItem = 40;

            totalWidth = (int) (itemWidth * totalNumColums + horizontalSpacingItem * (totalNumColums - 1) + 2 * GRIDVIEW_PADDING);
            totalHeight = (int) (itemHeight * numRows + verticalSpacingItem);

            gridViewparams = new LinearLayout.LayoutParams(totalWidth, totalHeight);
        } else {
            bangChuCaiGridView.setPadding(GRIDVIEW_PADDING_ITEM, 0, GRIDVIEW_PADDING_ITEM, 0);
            tmpList = new ArrayList<>();
            list1 = createList2Tablet();
            numColums = 6;
            numRows = 5;
            totalNumColums = numColums;
            // Uoc tinh width/height cua mot o
            /*screenHeight = screenHeight - 2 * GRIDVIEW_PADDING;*/
            tmpItemWidth = screenWidth * 0.9f / numColums;
            tmpItemHeight = screenHeight * 0.9f / numRows;
            // Tinh kich thuoc that theo ti le width/height = 1.27
            itemHeight = (int) calculateItemHeight(tmpItemWidth, tmpItemHeight);
            itemWidth = (int) (itemHeight * ITEM_RATIO);
            // Tinh khoang cach danh cho spacing
            horizontalSpacing = screenWidth - itemWidth * totalNumColums;
            // Tinh khoang cach giua cac cot
            horizontalSpacingItem = (int) (horizontalSpacing / totalNumColums);
            verticalSpacing = screenHeight - itemHeight * numRows;
            // Tinh khoang cach giua cac hang
            verticalSpacingItem = (int) (verticalSpacing / (numRows - 1));

            totalWidth = (int) (itemWidth * totalNumColums + horizontalSpacing);
            /*totalWidth = bangChuCaiGridView.getWidth();*/
            totalHeight = (int) (itemHeight * (numRows - 1) + 3 * verticalSpacingItem);

            /*Dong duoi Tablet*/
            LinearLayout.LayoutParams params;
            if (tmpList.size() == 2) {
                tmpTotalWidth = (int) (2 * itemWidth + horizontalSpacingItem);
                tmpTotalHeight = (int) itemHeight;
                params = new LinearLayout.LayoutParams(tmpTotalWidth, tmpTotalHeight);
                params.topMargin = verticalSpacingItem;

            } else {
                /*params.leftMargin = (int) (itemWidth + horizontalSpacingItem / 2);*/
                bangChuCaiGridView2.setPadding(0, 0, 0, 0);
                /*Tinh kich thuoc cho 1 dong gridview*/
                tmpTotalWidth = (int) (5 * itemWidth + 4 * horizontalSpacingItem);
                tmpTotalHeight = (int) itemHeight;
                params = new LinearLayout.LayoutParams(tmpTotalWidth, tmpTotalHeight);
                params.topMargin = verticalSpacingItem;
            }

            gridViewparams = new LinearLayout.LayoutParams(totalWidth, totalHeight);
            bangChuCaiGridView2.setLayoutParams(params);
            bangChuCaiGridView2.setColumnWidth((int) itemWidth);
            bangChuCaiGridView2.setNumColumns(5);
            bangChuCaiGridView2.setStretchMode(GridView.NO_STRETCH);
            bangChuCaiGridView2.setHorizontalSpacing(horizontalSpacingItem);
            bangChuCaiAdapter = new BangChuCaiAdapter(tmpList, this, (int) itemWidth, (int) itemHeight, this);
            bangChuCaiGridView2.setAdapter(bangChuCaiAdapter);
        }

        bangChuCaiGridView.setLayoutParams(gridViewparams);
        bangChuCaiGridView.setColumnWidth((int) itemWidth);
        bangChuCaiGridView.setNumColumns(totalNumColums);
        bangChuCaiGridView.setStretchMode(GridView.NO_STRETCH);
        bangChuCaiGridView.setVerticalSpacing(verticalSpacingItem);
        bangChuCaiGridView.setHorizontalSpacing(horizontalSpacingItem);

        bangChuCaiAdapter = new BangChuCaiAdapter(list1, this, (int) itemWidth, (int) itemHeight, this);
        bangChuCaiGridView.setAdapter(bangChuCaiAdapter);
    }

    private void getLanguageFromActivity() {
        Intent getIntent = getIntent();
        Bundle bundle = getIntent.getBundleExtra("LANGUAGE");
        isVietNamese = bundle.getBoolean("LANGUAGE");
    }

    private List<LetterModelSmall> createList2Tablet() {
        List<LetterModelSmall> list1Tablet = new ArrayList<>();
        if (Global.getLanguage() == Global.VN) {
            tmpList.add(chuCaiList.get(24));
            tmpList.add(chuCaiList.get(25));
            tmpList.add(chuCaiList.get(26));
            tmpList.add(chuCaiList.get(27));
            tmpList.add(chuCaiList.get(28));

            for (int i = 0; i < chuCaiList.size() - 5; i++) {
                list1Tablet.add(chuCaiList.get(i));
            }
        } else {
            tmpList.add(chuCaiList.get(24));
            tmpList.add(chuCaiList.get(25));

            for (int i = 0; i < chuCaiList.size() - 2; i++) {
                list1Tablet.add(chuCaiList.get(i));
            }
        }
        return list1Tablet;
    }

    private List<LetterModelSmall> initBangChuCai() {
        List<String> bangChuCai = fillterByLanguage();
        List<LetterModelSmall> dsChuCai = new ArrayList<>();

        if (Global.getLanguage() == Global.VN) {
            for (int i = 0; i < 29; i++) {
                dsChuCai.add(new LetterModelSmall(bangChuCai.get(i), i + 1));
            }
        } else {
            for (int i = 0; i < 26; i++) {
                dsChuCai.add(new LetterModelSmall(bangChuCai.get(i), i + 1));
            }
        }

        return dsChuCai;
    }

    @Override // from ChangeLanguageCallback
    public void updateUIForLanguage() {
        chuCaiList = initBangChuCai();
        initCommonGridView();
        int n = DBHelper.getInstance(this).resetShowRatio_Correct();
        if (n < 0) {
            Log.e(TAG, "Reset not Succes");
        } else {
            Log.d(TAG, "Reset Succesful");
        }
    }

    private List<String> fillterByLanguage() {
        List<String> bangChuCai = new ArrayList<>();
        for (int i = 0; i < 33; i++) {
            bangChuCai.add(listChuCai[i]);
        }

        if (Global.getLanguage() == Global.VN) {
            bangChuCai.remove(9);
            bangChuCai.remove(12);
            bangChuCai.remove(27);
            bangChuCai.remove(29);
        } else {
            bangChuCai.remove(1);
            bangChuCai.remove(1);
            bangChuCai.remove(4);
            bangChuCai.remove(5);
            bangChuCai.remove(15);
            bangChuCai.remove(15);
            bangChuCai.remove(21);
        }
        return bangChuCai;
    }

    private ScreenType checkScreenSize() {
        ScreenType type = ScreenType.SCREEN_TYPE_PHONE;
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
            type = ScreenType.SCREEN_TYPE_TABLET;
        }
        return type;
    }

    private void reorderList(List<LetterModelSmall> pList, int[] pIndice) {
        int listS = pList.size();
        int pInS = pIndice.length;
        if (pInS < listS) {
            Log.e("REORDER", "reorder error: list size:" + listS + " bigger index size:" + pInS);
            return;
        }
        for (int i = 0; i < listS; i++) {
            pList.get(i).setIndex(pIndice[i]);
        }
        //sort
        Collections.sort(pList, new LetterComparator());
        Log.i("REORDER", "reorder list done!");
    }

    private class LetterComparator implements Comparator<LetterModelSmall> {
        @Override
        public int compare(LetterModelSmall pT1, LetterModelSmall pT2) {
            if (pT1.getIndex() == pT2.getIndex()) {
                return 0;
            } else if (pT1.getIndex() > pT2.getIndex()) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    /**
     * Calculate width/height of gridview item follow by the ratio 1.27
     *
     * @param width
     * @param height
     * @return
     */
    private float calculateItemHeight(float width, float height) {
        float ratio = width / height;
        if (ratio > ITEM_RATIO) {
            width = height * ITEM_RATIO;
        } else {
            height = width / ITEM_RATIO;
        }
        return height;
    }

    @Override
    public void onClickItemGridView(int position) {
        showNhanBietChuDialog(chuCaiList, position);
    }

    private void showNhanBietChuDialog(List<LetterModelSmall> list, int id) {
        chucaiDialog = new NhanBietChuDialog(this, list, id);
        chucaiDialog.show(getSupportFragmentManager(), NhanBietChuDialog.TAG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                // fix Nam: 23/5 8:58
                if (NetworkReceiver.isConnected() && AdsObserver.shouldShow(System.currentTimeMillis())) {
                    interstitialAds.show();
                } else {
                    finish();
                }
                //end fix
                break;
            case R.id.btnHome:
                Intent mainActivity = new Intent(this, MainActivity.class);
                mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainActivity);
                this.finish();
                break;
        }
    }


//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE) {
//            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                finish();
//            }
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // fix Nam: 23/5 8:58
        if (NetworkReceiver.isConnected() && AdsObserver.shouldShow(System.currentTimeMillis())) {
            interstitialAds.show();
        } else {
            finish();
        }
        //end fix
    }

    @Override
    public void toDoBackListener() {
        finish();
    }
}
