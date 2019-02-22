package vn.devpro.devprokidorigin.utils.AdMob;


import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.dialogs.LoadingDialog;
import vn.devpro.devprokidorigin.interfaces.BackListener;

public class InterstitialAds {

    private Context context;
    private InterstitialAd mInterstitialAd;
    private InterstitialAd mInter_Video;

    public LoadingDialog getLoadingDialog() {
        return loadingDialog;
    }

    private LoadingDialog loadingDialog;
    private BackListener backListener;
    private Boolean clickShowed = false;
    private String admob_id_unit;

    public InterstitialAds(Context context, String admob_id_app, String admob_id_unit) {
        this.context = context;
        this.admob_id_unit = admob_id_unit;

        MobileAds.initialize(context, admob_id_app);

        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(admob_id_unit);

        mInterstitialAd.setAdListener(imterstitialAdListener);
        mInter_Video = new InterstitialAd(context);
        mInter_Video.setAdUnitId(context.getString(R.string.admob_id_unit_interstitial_video));

        mInter_Video.setAdListener(imterVideoAdListener);

        loadAds();
    }

    private void loadAds() {
        mInterstitialAd.loadAd(new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(context.getString(R.string.idTestDevice))
                .addTestDevice(context.getString(R.string.idTestDevice1))
                .build());
        mInter_Video.loadAd(new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(context.getString(R.string.idTestDevice))
                .addTestDevice(context.getString(R.string.idTestDevice1))
                .build());
    }

    private boolean interLoadFailed = false;
    AdListener imterstitialAdListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            interLoadFailed = false;
            // Code to be executed when an ad finishes loading.
            if (loadingDialog != null) {
                loadingDialog.dismiss();
                if (clickShowed) {
                    mInterstitialAd.show();
                }
            }
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            // Code to be executed when an ad request fails.
            interLoadFailed = true;
            if (loadingDialog != null && !loadingDialog.isHidden()) {
                loadingDialog.dismiss();
            }
            Log.e("Ads", "Load interstitial failed-->code:" + errorCode);
        }

        @Override
        public void onAdOpened() {
            // Code to be executed when the ad is displayed.
        }

        @Override
        public void onAdLeftApplication() {
            // Code to be executed when the user has left the app.
        }

        @Override
        public void onAdClosed() {
            // Code to be executed when when the interstitial ad is closed.
            loadAds();
            backListener.toDoBackListener();
        }
    };


    AdListener imterVideoAdListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            // Code to be executed when an ad finishes loading.
            if (loadingDialog != null && !loadingDialog.isHidden()) {
                loadingDialog.dismiss();
                if (clickShowed && interLoadFailed) {
                    mInter_Video.show();
                }
            }
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            // Code to be executed when an ad request fails.
            if (loadingDialog != null) {
                if(!loadingDialog.isHidden())loadingDialog.dismiss();
            }
            Log.e("Ads", "Load inter video -->code:" + errorCode);
        }

        @Override
        public void onAdOpened() {
            // Code to be executed when the ad is displayed.
        }


        @Override
        public void onAdLeftApplication() {
            // Code to be executed when the user has left the app.
        }

        @Override
        public void onAdClosed() {
            // Code to be executed when when the interstitial ad is closed.
            backListener.toDoBackListener();
        }
    };

    public void show() {
        clickShowed = true;
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else if (mInterstitialAd.isLoading()) {
            showLoading();
        } else if (mInter_Video != null && mInter_Video.isLoaded()) {
            mInter_Video.show();
        } else if (mInter_Video.isLoading()) {
            showLoading();
        } else {
            Log.d("Ads", "No need to show loading dialog");
            if (backListener != null) backListener.toDoBackListener();
        }
        AdsObserver.lastShow = System.currentTimeMillis();
    }

    private void showLoading() {
        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
        String thongBaoDialog = context.getString(R.string.loading_quangcao);
        loadingDialog = new LoadingDialog();
        loadingDialog.setThongBao(thongBaoDialog);
        loadingDialog.show(fm, null);
    }

    public void setBackListener(BackListener backListener) {
        this.backListener = backListener;
    }

    public static InterstitialAds loadInterstitialAds(BackListener pBackListener, Context pContext) {
        String appID = pContext.getString(R.string.admob_id_app); // lấy admob id app
        String unitID = pContext.getString(R.string.admob_id_unit_interstitial); // lấy admob id unit
        InterstitialAds interstitialAds = new InterstitialAds(pContext, appID, unitID);
        interstitialAds.setBackListener(pBackListener);
        return interstitialAds;
    }
}
