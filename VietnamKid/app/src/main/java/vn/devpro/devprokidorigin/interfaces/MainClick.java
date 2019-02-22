package vn.devpro.devprokidorigin.interfaces;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.activities.game.TroChoi;
import vn.devpro.devprokidorigin.activities.hoctap.HocTapActivity;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.utils.Utils;

/**
 * Created by NamQuoc on 4/9/2018.
 */

public class MainClick implements View.OnClickListener, Animation.AnimationListener {
    private Context context;
    private int extra;
    private Intent intent;

    /**
     * @param ex - tong so topic hien co de chuyen sang mh HocTap thong qua intent
     * @return - doi tuong MainClick hien tai
     * @author ThanhDat
     * @TODO - dem truoc tong so topic hien tai de mh hoc tap xu ly load Percent topic
     */
    public MainClick putExtra(int ex) {
        this.extra = ex;
        return this;
    }

    public MainClick(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtnTroChoi:
                gotoTroChoi(view);
                break;
            case R.id.ibtnHocTap:
                gotoHocTap(view);
                break;
            default:
                break;
        }
    }


    private void gotoHocTap(View pView) {
        Log.d("@TIME", "click-->" + System.currentTimeMillis());
        Utils.didTapButtom(pView, R.anim.scale_on_main);
        pView.getAnimation().setAnimationListener(this);
        intent = new Intent(Global.mContext, HocTapActivity.class);
    }


    private void gotoTroChoi(View pView) {
        Utils.didTapButtom(pView, R.anim.scale_on_main);
        pView.getAnimation().setAnimationListener(this);
        intent = new Intent(context, TroChoi.class);
    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intent.putExtra("topic_count", extra);
                intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }, 200);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
