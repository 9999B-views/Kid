package vn.devpro.devprokidorigin.dialogs;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import vn.devpro.devprokidorigin.R;

/**
 * Created by NamQuoc on 4/9/2018.
 */

public class DialogSetting extends DialogFragment {
    private ImageView imgPhanHoi, imgDanhGia, imgChiaSe, imgDong;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main_setting, container);
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

        // khai báo control cho imagebutton trong dialog để bắt sự kiện
        imgPhanHoi = view.findViewById(R.id.imgPhanHoi);
        imgDanhGia = view.findViewById(R.id.imgDanhGia);
        imgChiaSe = view.findViewById(R.id.imgChiaSe);
        imgDong = view.findViewById(R.id.imgDong);

        // bắt sự kiện cho button dialog
        imgPhanHoi.setOnClickListener(dialog_click);
        imgDanhGia.setOnClickListener(dialog_click);
        imgChiaSe.setOnClickListener(dialog_click);
        imgDong.setOnClickListener(dialog_click);
    }

    private View.OnClickListener dialog_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imgChiaSe:
                    getDialog().dismiss();
                    excute_dialog_confirm(R.id.imgChiaSe);
                    break;
                case R.id.imgDanhGia:
                    getDialog().dismiss();
                    excute_dialog_confirm(R.id.imgDanhGia);
                    break;
                case R.id.imgPhanHoi:
                    getDialog().dismiss();
                    excute_dialog_confirm(R.id.imgPhanHoi);
                    break;
                case R.id.imgDong:
                    getDialog().dismiss();
                    break;
            }
        }
    };

    private void excute_dialog_confirm(int dialog_selected) {
        DialogConfirm dialogConfirm = DialogConfirm.newInstance(dialog_selected);
        dialogConfirm.onAttach(mContext);
        dialogConfirm.show(getFragmentManager(), null);
    }


}
