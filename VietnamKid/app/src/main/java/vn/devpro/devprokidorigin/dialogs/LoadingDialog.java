package vn.devpro.devprokidorigin.dialogs;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import vn.devpro.devprokidorigin.R;

public class LoadingDialog extends DialogFragment {
    private TextView txtThongBao;
    private String thongBao;

    /*public static LoadingDialog newInstance(String thongBao) {
        LoadingDialog dialog = new LoadingDialog();
        Bundle args = new Bundle();
        args.putString("thong_bao", thongBao);
        dialog.setArguments(args);
        return dialog;
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_loading_dialog, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        // cài đặt kịch thước dialog
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.7);
        int height = (int) (width * 0.618);
        getDialog().getWindow().setLayout(width, height);
        getDialog().getWindow().setGravity(Gravity.CENTER | Gravity.CENTER);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ẩn tiêu đề dialog
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ẩn background dialog
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // tắt chức năng dismiss outside
        getDialog().setCanceledOnTouchOutside(false);

//        String thongBao = getArguments().getString("thong_bao");
        if (thongBao != null) {
            txtThongBao = view.findViewById(R.id.txtThongBao);
            txtThongBao.setText(thongBao);
        }

    }

    public void setThongBao(String thongBao) {
        this.thongBao = thongBao;
    }
}
