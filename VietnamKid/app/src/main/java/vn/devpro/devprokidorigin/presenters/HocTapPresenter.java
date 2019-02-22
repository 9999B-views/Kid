package vn.devpro.devprokidorigin.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.TransactionDetails;

import java.util.ArrayList;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.activities.StartActivity;
import vn.devpro.devprokidorigin.activities.hoctap.HocTapActivity;
import vn.devpro.devprokidorigin.activities.hoctap.Hoc_ChuCai;
import vn.devpro.devprokidorigin.activities.hoctap.Hoc_Khac;
import vn.devpro.devprokidorigin.databases.DBHelper;
import vn.devpro.devprokidorigin.dialogs.ConfirmDialog;
import vn.devpro.devprokidorigin.dialogs.CustomAlertDialog;
import vn.devpro.devprokidorigin.interfaces.DialogConfirmListener;
import vn.devpro.devprokidorigin.interfaces.HocTapView;
import vn.devpro.devprokidorigin.interfaces.ITopicClick;
import vn.devpro.devprokidorigin.interfaces.LoadHocTapDataListenner;
import vn.devpro.devprokidorigin.interfaces.UnlockTopicDialogListener;
import vn.devpro.devprokidorigin.interfaces.UnlockTopicNameListener;
import vn.devpro.devprokidorigin.models.HocTapModel;
import vn.devpro.devprokidorigin.models.entity.TopicName;
import vn.devpro.devprokidorigin.utils.GiftCode;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.utils.IKey;
import vn.devpro.devprokidorigin.utils.NetworkReceiver;
import vn.devpro.devprokidorigin.utils.Utils;

/**
 * Created by admin on 3/26/2018.
 */

public class HocTapPresenter implements LoadHocTapDataListenner, ITopicClick, UnlockTopicDialogListener, DialogConfirmListener, BillingProcessor.IBillingHandler, UnlockTopicNameListener {
    private Context context;
    private HocTapModel hocTapModel;
    private HocTapView hocTapView;
    private HocTapActivity mActivity;
    private String GIFTCODE;
    private static BillingProcessor buy;

    public HocTapPresenter(final Context context, HocTapView hocTapView) {
        this.context = context;
        this.hocTapView = hocTapView;

        hocTapModel = new HocTapModel(this, context);
        buy = new BillingProcessor(context, Global.GP_LICENSE_KEY, this);
    }

    public void loadData() {
        hocTapModel.loadData();
    }

    @Override
    public void onLoadTopicNameSuccess(ArrayList<TopicName> listData) {
        Log.i("Pre:", "Load Data success");
        hocTapView.displayData(listData);
    }

    @Override
    public void onLoadTopicNameFailure(String message) {
        hocTapView.displayError(message);
    }

    @Override
    public void onTopicClick(int id, int lock, boolean pDatabase_missing) {
        StartActivity.rewardedVideo.setTopicID(id);

        FragmentManager fragmentManager = ((FragmentActivity) mActivity).getSupportFragmentManager();
        if (pDatabase_missing) {
            show_alert(fragmentManager,context);
            return;
        }
        mActivity.unlockTopicDialog.setUnlockTopicDialogListener(this);
        if (lock == 0) {
            // chủ đề đã được mở khoá
            goToScreen(id);
        } else {
            // chủ đề đang khoá
            // hiển thị dialog mở khoá (xem quảng cáo hoặc thanh toán)
            if (NetworkReceiver.isConnected()) {
                mActivity.unlockTopicDialog.show(fragmentManager, null);
            } else {
                Utils.showToast(context, "Kết nối mạng để mở khóa!");
            }
        }
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

    public void goToScreen(int pId) {
        if (pId == 1) {
            Intent jumpTo = new Intent(mActivity, Hoc_ChuCai.class);
            mActivity.startActivity(jumpTo);
        } else {
            if (!Global.isDemoApp()) {
                Intent jumpTo = new Intent(mActivity, Hoc_Khac.class);
                jumpTo.putExtra(IKey.TOPIC_ID, pId);
                mActivity.startActivity(jumpTo);
            } else {
                Utils.showToast(context, context.getString(R.string.tip_taitainguyen));
            }
        }
    }

    public void connectActivity(HocTapActivity pActivity) {
        this.mActivity = pActivity;
    }

   /* public void setRewardedVideo(RewardedVideo rewardedVideo) {
        this.rewardedVideo = rewardedVideo;
    }*/

    public Activity getActivyty() {
        return mActivity;
    }

    @Override
    public void toDoViewAds() {
        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.onAttach(context);
        confirmDialog.setListener(this);
        confirmDialog.setAct("VIEW_ADS");
        confirmDialog.show(fm, null);
    }

    @Override
    public void toDoGiftCode(String code) {
        GIFTCODE = code;
        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.onAttach(context);
        confirmDialog.setListener(this);
        confirmDialog.setAct("CONFIRM_CODE");
        confirmDialog.show(fm, null);
    }

    @Override
    public void toDoPay() {
        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.onAttach(context);
        confirmDialog.setListener(this);
        confirmDialog.setAct("GOOGLE_PAY");
        confirmDialog.show(fm, null);
    }

    @Override
    public void confirmTrue(String act) {
        switch (act) {
            case "VIEW_ADS":
                // hành động khi chọn xem quảng cáo
                if (NetworkReceiver.isConnected()) {
                    StartActivity.rewardedVideo.show();
                }
                break;
            case "CONFIRM_CODE":
                // hành động khi chọn nhập code
                GiftCode giftCode = new GiftCode(context, GIFTCODE);
                giftCode.setUnlockTopicNameListener(this);
                giftCode.checkGiftcode();
                break;
            case "GOOGLE_PAY":
                if (NetworkReceiver.isConnected()) {
                    if (buy.isPurchased(Global.productID)) {
                        Global.setIsPremium(true);
                        this.unlockTopicAll();
                        //buy.consumePurchase(Global.productID);
                        Utils.showToast(context, "Đã kích hoạt bản Primeum!");
                    } else {
                        buy.purchase(mActivity, Global.productID);
                        //Utils.showToast(mContext, "================" +buy.getPurchaseListingDetails(Global.productID).priceText.toString() + "---------------");
                    }
                } else {
                    Utils.showToast(context, "Kết nối mạng để mua!");
                }
                break;
        }
    }

    @Override
    public void confirmFalse() {
        Utils.showToast(context, "Bạn đã trả lời sai rồi!");
    }


    @Override
    public void unlockTopicWithID(int topicID) {
        DBHelper.getInstance(Global.mContext).updateColumnLock(topicID);
        mActivity.updateUI();
    }

    @Override
    public void unlockTopicAll() {
        DBHelper.getInstance(Global.mContext).updateColumnLock();
        mActivity.updateUI();
    }

    public static BillingProcessor getBuy() {
        return buy;
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Global.setIsPremium(true);
        unlockTopicAll();
    }

    @Override
    public void onPurchaseHistoryRestored() {
        //Called when purchase history was restored and the list of all owned PRODUCT ID's was loaded from Google Play
    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        if (errorCode == Constants.BILLING_RESPONSE_RESULT_USER_CANCELED) {
            Utils.showToast(context, "Hủy mua!!!");
        } else {
            Log.e("Bill", "mua khong thanh cong");
            Utils.showToast(context, "Có lỗi xảy ra, mua không thành công");
        }
    }

    @Override
    public void onBillingInitialized() {

    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (buy.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            Log.i("Bill", "Err tai" + data.getAction());
        }
    }*/
}