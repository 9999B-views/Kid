package vn.devpro.devprokidorigin.activities.hoctap;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.TransactionDetails;

import java.util.ArrayList;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.activities.StartActivity;
import vn.devpro.devprokidorigin.adapters.HocTapadapter;
import vn.devpro.devprokidorigin.databases.DBHelper;
import vn.devpro.devprokidorigin.dialogs.UnlockTopicDialog;
import vn.devpro.devprokidorigin.interfaces.BackListener;
import vn.devpro.devprokidorigin.interfaces.HocTapView;
import vn.devpro.devprokidorigin.interfaces.UnlockTopicNameListener;
import vn.devpro.devprokidorigin.models.ButtonClick;
import vn.devpro.devprokidorigin.models.entity.TopicName;
import vn.devpro.devprokidorigin.models.interfaces.ChangeLanguageCallback;
import vn.devpro.devprokidorigin.presenters.HocTapPresenter;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.utils.Utils;


public class HocTapActivity extends AppCompatActivity implements HocTapView, ChangeLanguageCallback, BillingProcessor.IBillingHandler, BackListener, UnlockTopicNameListener {
    private static final int GRIDVIEW_MARGIN_LEFT_RIGHT = 40;
    private static int GRIDVIEW_PADDING_ITEM = 40;
    private final int NUM_COLUMNS = 5;
    private DBHelper dbHelper;
    private ImageButton btnBack, btnLanguege, btnAmthanh;
    private LinearLayout parentGridview;
    private ProgressBar loadingProgressBar;

    public float[] percents = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int total_topic = 18;
    private Context mContext;

    private GridView gridView;
    private HocTapadapter adapter;
    private ArrayList<TopicName> listData;
    private HocTapPresenter hocTapPresenter;
    public UnlockTopicDialog unlockTopicDialog;
    private HocTapActivity activity;
    private boolean database_misssing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoc_tap);
        total_topic = getIntent().getIntExtra("topic_count", 18);

        parentGridview = findViewById(R.id.parentGridview);
        gridView = findViewById(R.id.GridView);
        btnBack = findViewById(R.id.btnBack);
        loadingProgressBar = findViewById(R.id.progressBig);
        btnBack.setOnClickListener(mOnClickListener);
        btnAmthanh = findViewById(R.id.btnAm);
        btnLanguege = findViewById(R.id.btnLang);

        mContext = this;
        activity = this;

        new CreatUnlockDialog().execute();
        new ProcessButtonTask().execute();
        new LoadDataTask().execute();

        loadingProgressBar.setVisibility(View.VISIBLE);

        StartActivity.rewardedVideo.setListener(HocTapActivity.this);
    }

    @Override
    public void unlockTopicWithID(int topicID) {
        DBHelper.getInstance(Global.mContext).updateColumnLock(topicID);
        updateUI();
    }

    @Override
    public void unlockTopicAll() {
        DBHelper.getInstance(Global.mContext).updateColumnLock();
        updateUI();
    }

    class CreatUnlockDialog extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            unlockTopicDialog = new UnlockTopicDialog();
            unlockTopicDialog.setmActivity(activity);
            unlockTopicDialog.onAttach(mContext);
            unlockTopicDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
            return null;
        }
    }

    class LoadDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... pVoids) {
            initPresenter();
            hocTapPresenter.loadData();
            dbHelper = DBHelper.getInstance(HocTapActivity.this);
            return null;
        }

        @Override
        protected void onPostExecute(Void pVoid) {
            initGridView(gridView);
            new LoadTopicIconTask(HocTapActivity.this).execute();
        }
    }

    private class LoadpercentTopic extends AsyncTask<Void, Void, Void> {
        float[] percents;
        int size;

        public LoadpercentTopic(float[] percents, int size) {
            this.percents = percents;
            this.size = size;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < size; i++) {
                percents[i] = dbHelper.updatePercent(i + 1);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.setListpercent(percents);
            adapter.notifyDataSetInvalidated();

        }
    }

    class ProcessButtonTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... pVoids) {
            //first init Btn by lang and sound
            Global.updateButtonLanguage(btnLanguege);
            Global.updateButtonSound(btnAmthanh);
            //===========================================================
            //@TODO ****** Xu ly su kien cho 2 nut sound va language ****
            //===========================================================
            ButtonClick.Sound soundListener = new ButtonClick.Sound();
            ButtonClick.Language langListener = new ButtonClick.Language(HocTapActivity.this);
            btnLanguege.setOnClickListener(langListener);
            btnAmthanh.setOnClickListener(soundListener);
            return null;
        }
    }


    private void initPresenter() {
        hocTapPresenter = new HocTapPresenter(this, this);
        hocTapPresenter.connectActivity(this);
//        hocTapPresenter.setRewardedVideo(mainActivity.rewardedVideo);
    }

    /**
     * @param gridview - gridView to be configured
     * @TODO Initialize gridView to display data on
     */
    private void initGridView(GridView gridview) {
        if (listData == null) {
            getDummyData();
            Toast.makeText(this, "Lỗi, không có dữ liệu", Toast.LENGTH_SHORT).show();
        }
        Log.d("@TIME", "initGridView:-->");
        parentGridview.setPadding(GRIDVIEW_MARGIN_LEFT_RIGHT, 0,
                GRIDVIEW_MARGIN_LEFT_RIGHT, 0);
        parentGridview.setGravity(Gravity.CENTER_VERTICAL);
        int totalNumColumns = listData.size() / 2;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels;
        int scrVHeight = height - Utils.convertDpToPixel(88, displayMetrics);
        //Log.d("Chieu cao man hinh", "" + height + "px = " + Utils.convertPxToDp(height, displayMetrics) + "dp");
        //Log.d("Chieu cao scrollView", "" + scrVHeight);
        int width = displayMetrics.widthPixels;
        GRIDVIEW_PADDING_ITEM = (int) (width / 20.72f);
        // Sau khi tru margin/padding
        width = width - GRIDVIEW_MARGIN_LEFT_RIGHT * 2 - GRIDVIEW_PADDING_ITEM * 4;
        //Log.d("Chieu rong scrollView", "" + width);
        int itemWidth = width / NUM_COLUMNS;
        int itemHeight = itemWidth + (int) (itemWidth / 3f);
        //Log.d("Chieu cao item ", "" + itemHeight);
        int totalItemHeight = itemHeight * 2 + GRIDVIEW_PADDING_ITEM;
        if (totalItemHeight > scrVHeight) {
            float scaleDownFactor = (float) scrVHeight / totalItemHeight;
            //Log.d("SCALEDOWN", "scaleDownFactor:" + scaleDownFactor);
            GRIDVIEW_PADDING_ITEM = (int) (GRIDVIEW_PADDING_ITEM * scaleDownFactor);
            itemHeight = (int) (itemHeight * scaleDownFactor);
            itemWidth = (int) (itemWidth * scaleDownFactor);
        } else {
            Log.d("SCALEDOWN", "everything is okey");
            Log.d("SCALEDOWN", "totalItemHeight:" + totalItemHeight);
            Log.d("SCALEDOWN", "scrVHeight:" + scrVHeight);

        }

        int totalWidth = itemWidth * totalNumColumns + GRIDVIEW_PADDING_ITEM * (listData.size() / 2 - 1)
                + GRIDVIEW_MARGIN_LEFT_RIGHT;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                totalWidth, LinearLayout.LayoutParams.WRAP_CONTENT);

        gridview.setLayoutParams(params);
        gridview.setColumnWidth(itemWidth);
        gridview.setStretchMode(GridView.NO_STRETCH);
        gridview.setNumColumns(totalNumColumns);
        gridview.setVerticalSpacing(GRIDVIEW_PADDING_ITEM);
        gridview.setHorizontalSpacing(GRIDVIEW_PADDING_ITEM);

        adapter = new HocTapadapter(listData, getLayoutInflater(), hocTapPresenter, itemWidth, itemHeight, getApplicationContext());
        adapter.setListpercent(percents);
        if (database_misssing) adapter.database_missing = true;
        this.gridView.setAdapter(adapter);
        Log.d("@TIME", "initGridView done:-->");
    }

    private void getDummyData() {
        listData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            listData.add(new TopicName(i, "...", "...", "_no_img", 0, 1));
        }
        database_misssing = true;
    }


    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btnBack) {
                //fix Dat: 28/5 2:27
                finish();
            }
        }
    };

    @Override
    protected void onResume() {
        Log.d("@TIME", "onResume-->");
        super.onResume();
        Global.updateButtonLanguage(btnLanguege);
        Global.updateButtonSound(btnAmthanh);
        updateUIForLanguage();
        new LoadpercentTopic(percents, total_topic).execute();
        Log.d("@TIME", "onResume done-->");
    }

    @Override
    public void updateUI() {
        new LoadDataTask().execute();
    }

    /**
     * @param list - listData to be displayed
     * @TODO display data onto screen
     */
    @Override
    public void displayData(ArrayList<TopicName> list) {
        listData = list;
        new LoadpercentTopic(percents, listData.size()).execute();
    }

    @Override
    public void updateUIForLanguage() {
        if (adapter == null) return;
        adapter.notifyDataSetChanged();
    }


    class LoadTopicIconTask extends AsyncTask<Void, Void, ArrayList<TopicName>> {
        HocTapActivity mActivity;

        public LoadTopicIconTask(Activity pActivity) {
            this.mActivity = (HocTapActivity) pActivity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<TopicName> doInBackground(Void... pVoids) {
            for (TopicName tpName : mActivity.listData) {
                tpName.setImageData(Utils.decodeFileImage(Utils.getByteArrayFromSD(Global.pathImages, tpName.getImg(), ".png")));
            }
            return mActivity.listData;
        }

        @Override
        protected void onPostExecute(ArrayList<TopicName> pTopicNames) {
            Log.d("@TIME", "onPostExecute:-->");
            super.onPostExecute(pTopicNames);
            mActivity.adapter.setListData(pTopicNames);
            mActivity.adapter.notifyDataSetChanged();
            mActivity.loadingProgressBar.setVisibility(View.GONE);
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    @Override
    public void displayError(String message) {
        //Utils.showToast(this, message);
        Log.i("TAG", "Err " + message);
    }

    @Override
    public Context getViewContext() {
        return this.getApplicationContext();
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Utils.showToast(this, "Bạn đã nâng cấp tài khoản VIP");
    }

    @Override
    public void onPurchaseHistoryRestored() {
        //Called when purchase history was restored and the list of all owned PRODUCT ID's was loaded from Google Play
    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        if (errorCode == Constants.BILLING_RESPONSE_RESULT_USER_CANCELED) {
            Utils.showToast(this, "Hủy mua!!!");
        } else {
            Utils.showToast(this, "Err!!!");
        }
    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (HocTapPresenter.getBuy().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void toDoBackListener() {
        finish();
    }

}
