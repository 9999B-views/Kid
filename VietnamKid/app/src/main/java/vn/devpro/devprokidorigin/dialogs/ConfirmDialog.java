package vn.devpro.devprokidorigin.dialogs;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.interfaces.DialogConfirmListener;
import vn.devpro.devprokidorigin.models.RandomQuestion;

public class ConfirmDialog extends DialogFragment {
    // khởi tạo biến
    private Context mContext;

    private DialogConfirmListener dialogConfirmListener;
    private String act = "DEFAULT";

    private RandomQuestion randomQuestion;

    private TextView txtCauHoi, txtDapAn1, txtDapAn2, txtDapAn3;
    private ImageView imgDong;


    public static ConfirmDialog newInstance(int dialog_selected) {
        ConfirmDialog dialog = new ConfirmDialog();
        Bundle args = new Bundle();
        args.putInt("dialog_selected", dialog_selected);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_confirm_dialog, null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
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

        // khai báo control
        txtCauHoi = view.findViewById(R.id.txtCauHoi);
        txtDapAn1 = view.findViewById(R.id.txtDapAn1);
        txtDapAn2 = view.findViewById(R.id.txtDapAn2);
        txtDapAn3 = view.findViewById(R.id.txtDapAn3);
        imgDong = view.findViewById(R.id.imgDong);

        // chỉnh font textview
        Typeface type = Typeface.createFromAsset(mContext.getAssets(), "fonts/boosternextfy_bold.ttf");
        txtCauHoi.setTypeface(type);
        txtDapAn1.setTypeface(type);
        txtDapAn2.setTypeface(type);
        txtDapAn3.setTypeface(type);

        // khởi tạo câu hỏi
        randomQuestion = new RandomQuestion(20, 1);

        String cauHoi =
                randomQuestion.getSoA() + " " + randomQuestion.getPhepTinh() + " " + randomQuestion.getSoB() + " = ?";
        txtCauHoi.setText(cauHoi);

        Random random = new Random();
        int dapAn = randomQuestion.getDapAn();
        int them1 = random.nextInt(10 - 1 + 1) + 1;
        int them2 = random.nextInt(10 - 1 + 1) + 1;
        int viTriDapAn = random.nextInt(3 - 1 + 1) + 1;

        switch (viTriDapAn) {
            case 1:
                txtDapAn1.setText(dapAn + "");
                txtDapAn2.setText(dapAn + them1 + "");
                txtDapAn3.setText(dapAn + them1 + them2 + "");
                break;
            case 2:
                txtDapAn2.setText(dapAn + "");
                txtDapAn3.setText(dapAn + them1 + "");
                txtDapAn1.setText(dapAn + them1 + them2 + "");
                break;
            case 3:
                txtDapAn3.setText(dapAn + "");
                txtDapAn1.setText(dapAn + them1 + "");
                txtDapAn2.setText(dapAn + them1 + them2 + "");
                break;
        }

        // bắt sự kiện click
        txtDapAn1.setOnClickListener(dialog_confirm_click);
        txtDapAn2.setOnClickListener(dialog_confirm_click);
        txtDapAn3.setOnClickListener(dialog_confirm_click);
        imgDong.setOnClickListener(dialog_confirm_click);

    }

    View.OnClickListener dialog_confirm_click = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.txtDapAn1:
                    if (Integer.parseInt(txtDapAn1.getText().toString()) == randomQuestion.getDapAn()) {
                        getDialog().dismiss();
                        dialogConfirmListener.confirmTrue(act);
                    } else {
                        dialogConfirmListener.confirmFalse();
                    }
                    break;
                case R.id.txtDapAn2:
                    if (Integer.parseInt(txtDapAn2.getText().toString()) == randomQuestion.getDapAn()) {
                        getDialog().dismiss();
                        dialogConfirmListener.confirmTrue(act);
                    } else {
                        dialogConfirmListener.confirmFalse();
                    }
                    break;
                case R.id.txtDapAn3:
                    if (Integer.parseInt(txtDapAn3.getText().toString()) == randomQuestion.getDapAn()) {
                        getDialog().dismiss();
                        dialogConfirmListener.confirmTrue(act);
                    } else {
                        dialogConfirmListener.confirmFalse();
                    }
                    break;
                case R.id.imgDong:
                    getDialog().dismiss();
                    break;
            }
        }
    };

    public void setListener(DialogConfirmListener listener) {
        dialogConfirmListener = listener;
    }

    public void setAct(String act) {
        this.act = act;
    }
}
