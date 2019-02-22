package vn.devpro.devprokidorigin.activities.game.latbai;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.GridView;
import android.widget.ImageView;


import java.util.ArrayList;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.activities.game.latbai.commom.Shared;
import vn.devpro.devprokidorigin.activities.game.latbai.ui.PopupManager;
import vn.devpro.devprokidorigin.activities.game.latbai.uitls.Utils_PairGame;
import vn.devpro.devprokidorigin.databases.DBHelper;
import vn.devpro.devprokidorigin.databases.SQLiteDataController;
import vn.devpro.devprokidorigin.activities.game.latbai.engine_moitruong.Engine;
import vn.devpro.devprokidorigin.activities.game.latbai.engine_moitruong.ScreenController;
import vn.devpro.devprokidorigin.activities.game.latbai.even.BackGame;
import vn.devpro.devprokidorigin.activities.game.latbai.even.ui.EventBus;
import vn.devpro.devprokidorigin.interfaces.BackListener;
import vn.devpro.devprokidorigin.models.entity.TopicName;

import vn.devpro.devprokidorigin.utils.AdMob.AdsObserver;
import vn.devpro.devprokidorigin.utils.AdMob.InterstitialAds;
import vn.devpro.devprokidorigin.utils.NetworkReceiver;
import vn.devpro.devprokidorigin.utils.Global;

public class MainPairGame extends FragmentActivity implements BackListener {
    SQLiteDataController sqLiteDataController;
    private ImageView mBackgroundImage;
    private static final int GRIDVIEW_MARGIN_LEFT_RIGHT = 40;
    private static final int GRIDVIEW_PADDING_ITEM = 40;
    private final int NUM_COLUMNS = 5;
    private ArrayList<TopicName> listData;
    private GridView gridView;
    private int topicId = 3;
    public  InterstitialAds mInterstitialAds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.mainpairgame );

        Global.initGlobal( getApplicationContext() );
        Shared.context = getApplicationContext();
        Shared.engine = Engine.getmInstance();
        Shared.eventBus = EventBus.getInstance();

        Shared.listTopicName = DBHelper.getInstance( Global.mContext ).getTopicName();
        mBackgroundImage = findViewById( R.id.background_image );
        Shared.activity = this;
        Shared.engine.start();
        Shared.engine.setBackgroundImageView( mBackgroundImage );

        // set background tạo hình nền
        setBackgroundImage();
        // set menu
        ScreenController.getInstance().openScreen( ScreenController.Screen.MENU );
        topicId = Utils_PairGame.readDefaultTopicId();
        Shared.engine.setCurrentopicId( topicId );
        Handler loadAdsHandler = new Handler();
        loadAdsHandler.postDelayed(loadAdsRunner, 500);
    }

    Runnable loadAdsRunner = new Runnable() {
        @Override
        public void run() {
            mInterstitialAds = InterstitialAds.loadInterstitialAds(MainPairGame.this, MainPairGame.this);
        }
    };


    @Override
    protected void onDestroy() {
        Shared.engine.stop();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Log.d(">.<", "omBack pressed");
        if (PopupManager.isShown()) {
            Log.d(">.<", "omBack pressed - PopupManager.isShown()");
            PopupManager.closePopup();
            if (ScreenController.getLastScreen() == ScreenController.Screen.GAME) {
                Log.d(">.<", "omBack pressed --->BackGame run");
                Shared.eventBus.notify( new BackGame() );
            }
        } else /*if (ScreenController.getInstance().onBack())*/ {
            Log.d(">.<", "omBack pressed - ads");
            if(NetworkReceiver.isConnected() && mInterstitialAds != null && mInterstitialAds.getLoadingDialog() == null&& AdsObserver.shouldShow( System.currentTimeMillis())){
                mInterstitialAds.show();
            }else{
                //super.onBackPressed();
                finish();
            }
        }
    }

    private void setBackgroundImage() {
        Bitmap bitmap = Utils_PairGame.scaleDown( R.drawable.screens_09, Utils_PairGame.screenWidth(), Utils_PairGame.screenHeight() );
        bitmap = Utils_PairGame.crop( bitmap, Utils_PairGame.screenHeight(), Utils_PairGame.screenWidth() );

        bitmap = Utils_PairGame.downscaleBitmap( bitmap, 2 );
        mBackgroundImage.setImageBitmap( bitmap );
    }


    @Override
    public void toDoBackListener() {
        finish();
    }
}


