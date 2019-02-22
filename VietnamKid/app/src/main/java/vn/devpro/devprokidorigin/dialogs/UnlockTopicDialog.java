package vn.devpro.devprokidorigin.dialogs;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.activities.hoctap.HocTapActivity;
import vn.devpro.devprokidorigin.interfaces.UnlockTopicDialogListener;
import vn.devpro.devprokidorigin.utils.Utils;


public class UnlockTopicDialog extends DialogFragment {

    private Context mContext;
    private HocTapActivity mActivity;

    public void setmActivity(HocTapActivity mActivity) {
        this.mActivity = mActivity;
    }

    private TextView txtTieuDe;
    private EditText edtGiftCode;
    private Button btnThanhToan, btnXemQC, btnNhapCode;
    private ImageView imgDong;
    private UnlockTopicDialogListener unlockTopicDialogListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_unlock_topic, container);
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ẩn tiêu đề dialog
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        // tắt chức năng dismiss outside
        getDialog().setCanceledOnTouchOutside(false);

        txtTieuDe = view.findViewById(R.id.txtTieuDe);
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/boosternextfy_bold.ttf");
        txtTieuDe.setTypeface(font);

        // khai báo control
        btnThanhToan = view.findViewById(R.id.btnThanhToan);
        btnXemQC = view.findViewById(R.id.btnXemQC);
        btnNhapCode = view.findViewById(R.id.btnNhapCode);
        edtGiftCode = view.findViewById(R.id.edtGiftCode);
        imgDong = view.findViewById(R.id.imgDong);

        // bắt sự kiện btnThanhToan, btnXemQC
        btnThanhToan.setOnClickListener(unlock_topic_click);
        btnXemQC.setOnClickListener(unlock_topic_click);
        btnNhapCode.setOnClickListener(unlock_topic_click);
        imgDong.setOnClickListener(unlock_topic_click);
    }

    View.OnClickListener unlock_topic_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnThanhToan:
                    // xử lý mua
                    getDialog().dismiss();
                    unlockTopicDialogListener.toDoPay();
                    break;
                case R.id.btnXemQC:
                    // xử lý xem quảng cáo
                    getDialog().dismiss();
                    unlockTopicDialogListener.toDoViewAds();
                    break;
                case R.id.btnNhapCode:
                    // xử lý check code
                    String code = edtGiftCode.getText().toString();
                    if (code.length() > 0) {
                        getDialog().dismiss();
                        unlockTopicDialogListener.toDoGiftCode(code);
                    } else {
                        Utils.showToast(mContext, "Không được để trống code!");
                    }
                    break;
                case R.id.imgDong:
                    getDialog().dismiss();
                    break;
            }
        }
    };

    public void setUnlockTopicDialogListener(UnlockTopicDialogListener unlockTopicDialogListener) {
        this.unlockTopicDialogListener = unlockTopicDialogListener;
    }
}
