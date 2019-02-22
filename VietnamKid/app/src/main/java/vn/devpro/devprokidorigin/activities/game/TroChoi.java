package vn.devpro.devprokidorigin.activities.game;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.activities.LearnWrite;
import vn.devpro.devprokidorigin.activities.MainActivity;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.game.BalloonGameActivity;
import vn.devpro.devprokidorigin.activities.game.latbai.MainPairGame;
import vn.devpro.devprokidorigin.activities.game.latbai.fragments.MenuFragment;
import vn.devpro.devprokidorigin.adapters.TroChoiAdapter;
import vn.devpro.devprokidorigin.interfaces.ChooseGameClickListener;
import vn.devpro.devprokidorigin.models.ButtonClick;
import vn.devpro.devprokidorigin.models.ItemProperty;
import vn.devpro.devprokidorigin.models.TroChoiModel;
import vn.devpro.devprokidorigin.models.interfaces.ChangeLanguageCallback;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.utils.Utils;

public class TroChoi extends AppCompatActivity implements ChangeLanguageCallback, ChooseGameClickListener {
    Context context;
    private static final int RELATIVE_MARGIN_LEFT_RIGHT = 40;
    private static int ITEM_MARGIN_TOP_BOTTOM = 0;
    private static int ITEM_SPACING = 0;
    private static int RELATIVE_PADDING_ITEM = 0;
    private final int NUM_COLUMNS = 2;
    private RelativeLayout rltCotTren;
    private LinearLayout lnItem;
    private LinearLayout llRecyclerView;
    private Button btnBack;
    private Button btnSound;
    private RecyclerView rcItemTroChoi;
    private ArrayList<TroChoiModel> mTroChoiModelList;
    private TroChoiAdapter adapter;
    private Button btnLanguage;
    public RelativeLayout rltSelect;
    private ProgressBar loadingGameProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tro_choi);
        this.rltCotTren = findViewById(R.id.rltCotTren);
        this.lnItem = findViewById(R.id.lnItem);
        this.llRecyclerView = findViewById(R.id.llRecyclerView);
        this.rcItemTroChoi = findViewById(R.id.rcItemTroChoi);
        this.btnLanguage = findViewById(R.id.btnLanguage);
        loadingGameProgress = findViewById(R.id.loadingGameProgress);
        Global.updateButtonLanguage(this.btnLanguage);
        this.btnBack = findViewById(R.id.btnBack);
        this.btnSound = findViewById(R.id.btnVolume);
        Global.updateButtonSound(this.btnSound);
        mTroChoiModelList = new ArrayList<>();
        creatList();

        initLinearLayout(llRecyclerView);
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //===========================================================
        //@TODO ****** Xu ly su kien cho 2 nut sound va language ****
        //===========================================================
        ButtonClick.Sound soundListener = new ButtonClick.Sound();
        ButtonClick.Language langListener = new ButtonClick.Language(this);
        btnLanguage.setOnClickListener(langListener);
        btnSound.setOnClickListener(soundListener);
        //==========================================================

    }

    private void creatList() {
        mTroChoiModelList.add(new TroChoiModel(2, Global.GAME_POP_BALLOON_VN, Global.GAME_POP_BALLOON_EN));
        mTroChoiModelList.add(new TroChoiModel(4, Global.GAME_PUZZLE_VN, Global.GAME_PUZZLE_EN));
        mTroChoiModelList.add(new TroChoiModel(0, Global.GAME_MATCHING_OBJ_VN, Global.GAME_MATCHING_OBJ_EN));
        mTroChoiModelList.add(new TroChoiModel(1, Global.GAME_FIND_SHADOW_VN, Global.GAME_FIND_SHADOW_EN));
        mTroChoiModelList.add(new TroChoiModel(3, Global.GAME_MAKE_PAIR_VN, Global.GAME_MAKE_PAIR_EN));
        mTroChoiModelList.add(new TroChoiModel(5, Global.GAME_WRITE_LETTER_VN, Global.GAME_WRITE_LETTER_EN));
    }


    public static float convertDptoPx(float dp, DisplayMetrics displayMetrics) {
        float px = dp * displayMetrics.density;
        return px;
    }

    private void initLinearLayout(LinearLayout linearLayout) {
        llRecyclerView.setGravity(Gravity.CENTER_VERTICAL);
        //int totalNumColums = mTroChoiModelList.size();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        ITEM_MARGIN_TOP_BOTTOM = (int) (height / 12.8f);
        int heighTop = (int) convertDptoPx(66, displayMetrics);
        int llheigh = height - heighTop;
        int width = displayMetrics.widthPixels;
        ITEM_SPACING = (int) (width * (61 / 809.0f));
        width = width - ITEM_SPACING * 4;
        int itemwidth = width / NUM_COLUMNS;
        int itemheigh = (int) (itemwidth * 1.196f);
        int totalItemheigh = itemheigh + ITEM_MARGIN_TOP_BOTTOM * 2;
        if (totalItemheigh > llheigh) {
            float scaleDownFactor = ((float) llheigh / totalItemheigh);
            itemwidth = (int) (itemwidth * scaleDownFactor);
            itemheigh = (int) (itemheigh * scaleDownFactor);
        }
        //int totalwidth = itemwidth * totalNumColums + ITEM_SPACING * (totalNumColums - 1);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(TroChoi.this,
                LinearLayoutManager.HORIZONTAL, false);
        rcItemTroChoi.setLayoutManager(horizontalLayoutManagaer);
        ItemProperty itp = new ItemProperty(itemwidth, itemheigh, ITEM_SPACING, ITEM_MARGIN_TOP_BOTTOM);
        adapter = new TroChoiAdapter(TroChoi.this, mTroChoiModelList, itp);
        adapter.setItemClickListener(this);
        rcItemTroChoi.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Global.updateButtonLanguage(btnLanguage);
        Global.updateButtonSound(btnSound);
        updateUIForLanguage();
        loadingGameProgress.setVisibility(View.GONE);
    }

    @Override
    public void updateUIForLanguage() {
        adapter.notifyDataSetChanged();
    }

    /**
     * ChooseGameClickListener
     *
     * @param id of the game saved in TroChoiModel
     */
    @Override
    public void onGameMenuItemClick(int id) {
        Log.d("DDD", "you've just click in game:" + mTroChoiModelList.get(id).getGameNameVN());
        switch (id) {
            case 0:
                if (!Global.isDemoApp()) {
                    loadingGameProgress.setVisibility(View.VISIBLE);
                    Intent gotoMatching = new Intent(TroChoi.this, GameNoiHinhActivity.class);
                    startActivity(gotoMatching);
                } else {
                    Utils.showToast(this, getString(R.string.tip_taitainguyen));
                }
                break;
            case 1:
                if (!Global.isDemoApp()) {
                    loadingGameProgress.setVisibility(View.VISIBLE);
                    Intent gotoFindShadow = new Intent(TroChoi.this, GameFindShadow.class);
                    startActivity(gotoFindShadow);
                } else {
                    Utils.showToast(this, getString(R.string.tip_taitainguyen));
                }
                break;
            case 2:
                loadingGameProgress.setVisibility(View.VISIBLE);
                Intent gotoPopBalloon = new Intent(TroChoi.this, BalloonGameActivity.class);
                startActivity(gotoPopBalloon);
                break;
            case 3:
                if (!Global.isDemoApp()) {
                    loadingGameProgress.setVisibility(View.VISIBLE);
                    Intent gotoPairGame = new Intent(TroChoi.this, MainPairGame.class);
                    startActivity(gotoPairGame);
                } else {
                    Utils.showToast(this, getString(R.string.tip_taitainguyen));
                }
                break;
            case 4:
                loadingGameProgress.setVisibility(View.VISIBLE);
                Intent gotoPuzzle = new Intent(TroChoi.this, PuzzleActivity.class);
                startActivity(gotoPuzzle);
                break;
            case 5:
                if (!Global.isDemoApp()) {
                    loadingGameProgress.setVisibility(View.VISIBLE);
                    Intent gotoLearnWrite = new Intent(TroChoi.this, LearnWrite.class);
                    startActivity(gotoLearnWrite);
                } else {
                    Utils.showToast(this, getString(R.string.tip_taitainguyen));
                }
                break;
            default:
                break;
        }
    }
}

