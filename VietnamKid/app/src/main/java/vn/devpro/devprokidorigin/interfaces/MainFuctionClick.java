package vn.devpro.devprokidorigin.interfaces;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import java.util.ArrayList;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.activities.MainActivity;
import vn.devpro.devprokidorigin.adapters.QuangCaoAdapter;
import vn.devpro.devprokidorigin.dialogs.DialogMoreApp;
import vn.devpro.devprokidorigin.dialogs.DialogSetting;
import vn.devpro.devprokidorigin.models.QuangCaoModel;
import vn.devpro.devprokidorigin.utils.Global;

/**
 * Created by NamQuoc on 4/9/2018.
 */

public class MainFuctionClick implements View.OnClickListener {
    private Context context;


    public MainFuctionClick(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        // sự kiện click phím chức năng
        switch (view.getId()) {
            case R.id.ibtnQuangCao:
                excute_dialog_quangcao();
                break;
            case R.id.ibtnNgonNgu:
                Global.changeLanguage();
                Global.updateButtonLanguage((ImageButton) view);
                break;
            case R.id.ibtnCaiDat:
                excute_dialog_caidat();
                break;
        }
    }

    // khởi tạo và thực thi dialog quảng cáo
    private void excute_dialog_quangcao() {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        DialogMoreApp dialogMoreApp = new DialogMoreApp();
        dialogMoreApp.show(fragmentManager, null);
    }

    // khởi tạo và thực thi dialog cài đặt
    private void excute_dialog_caidat() {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        DialogSetting dialogSetting = new DialogSetting();
        dialogSetting.onAttach(context);
        dialogSetting.show(fragmentManager, null);
    }
}
