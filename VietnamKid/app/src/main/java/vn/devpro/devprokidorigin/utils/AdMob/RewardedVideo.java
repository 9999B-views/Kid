package vn.devpro.devprokidorigin.utils.AdMob;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.dialogs.LoadingDialog;
import vn.devpro.devprokidorigin.interfaces.UnlockTopicNameListener;
import vn.devpro.devprokidorigin.utils.Utils;

public class RewardedVideo implements RewardedVideoAdListener {
    private Context context;
    public RewardedVideoAd rewardedVideoAd;
    private LoadingDialog loadingDialog;
    private UnlockTopicNameListener unlockTopicNameListener;
    private Boolean clickShowed = false;
    private InterstitialAd mInter_video;
    private boolean loadFailed = false;
    private int topicID;
    private String admob_id_unit;

    public RewardedVideo(Context context, String admob_id_app, String admob_id_unit) {
        this.context = context;
        this.admob_id_unit = admob_id_unit;

        MobileAds.initialize(context, admob_id_app);
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
        rewardedVideoAd.setRewardedVideoAdListener(this);
        loadFailed = false;
        // tao inter du phong
        mInter_video = new InterstitialAd(context);
        String unitID = context.getString(R.string.admob_id_unit_interstitial_video);
        mInter_video.setAdUnitId(unitID);
        mInter_video.setAdListener(videoInterListener);

        loadAds();
    }

    private void loadAds() {
        rewardedVideoAd.loadAd(admob_id_unit, new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(context.getString(R.string.idTestDevice))
                .addTestDevice(context.getString(R.string.idTestDevice1))
                .build());
        mInter_video.loadAd(new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(context.getString(R.string.idTestDevice))
                .addTestDevice(context.getString(R.string.idTestDevice1))
                .build());
    }

    public void show() {
        clickShowed = true;
        if (rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.show();
        } else if (!loadFailed) {
            showLoading();
        } else if (mInter_video != null && mInter_video.isLoaded()) {
            mInter_video.show();
        } else if (mInter_video.isLoading()) {
            showLoading();
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

    @Override
    public void onRewardedVideoAdLoaded() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            if (clickShowed) {
                rewardedVideoAd.show();
            }
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadAds();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        unlockTopicNameListener.unlockTopicWithID(topicID);
        Utils.showToast(context, "Đã mở khoá chủ đề thành công!");
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        loadFailed = true;
        if (loadingDialog != null && !loadingDialog.isHidden()) {
            loadingDialog.dismiss();
        }
        if (mInter_video != null && mInter_video.isLoaded() && clickShowed) {
            mInter_video.show();
        }

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    public void setListener(UnlockTopicNameListener listener) {
        unlockTopicNameListener = listener;
    }


    // ========= inter du phong ===================
    AdListener videoInterListener = new AdListener() {
        @Override
        public void onAdClosed() {
            unlockTopicNameListener.unlockTopicWithID(topicID);
            Utils.showToast(context, "Đã mở khoá chủ đề thành công!");
        }

        @Override
        public void onAdFailedToLoad(int pI) {
            if (loadingDialog != null && !loadingDialog.isHidden()) {
                loadingDialog.dismiss();
            }
            Log.e("Ads", "load quang cao inter video that bai:" + pI);
        }

        @Override
        public void onAdOpened() {
            Log.d("Ads", "inter video da mo");
        }

        @Override
        public void onAdLoaded() {
            if (loadingDialog != null && !loadingDialog.isHidden()) {
                loadingDialog.dismiss();
            }
            if (clickShowed && loadFailed) { // neu da click show va reward load fail
                mInter_video.show();
            }
        }
    };

    public void setTopicID(int topicID) {
        this.topicID = topicID;
    }

    public static RewardedVideo loadRewardedVideoAds(Context context) {
        String appID = context.getString(R.string.admob_id_app); // lấy admob id app
        String unitID = context.getString(R.string.admob_id_unit_rewarded_video); // lấy admob id unit
        RewardedVideo rewardedVideo = new RewardedVideo(context, appID, unitID);
//        rewardedVideo.setListener(listener);

        return rewardedVideo;
    }

}
