package vn.devpro.devprokidorigin.dialogs;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.solver.Goal;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.interfaces.DialogConfirmListener;
import vn.devpro.devprokidorigin.models.RandomQuestion;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.utils.Utils;

public class CustomAlertDialog extends DialogFragment {
    // khởi tạo biến
    private Context mContext;
    TextView messageTV;
    Button okBtn;
    String mgs;
    private Runnable runable;


    public static CustomAlertDialog newInstance() {
        CustomAlertDialog dialog = new CustomAlertDialog();
        return dialog;
    }

    public static CustomAlertDialog newInstance(String mgs, Runnable action) {
        CustomAlertDialog dialog = new CustomAlertDialog();
        dialog.mgs = mgs;
        dialog.runable = action;
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_restart, null);
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
        int width = Utils.convertDpToPixel(300,mContext);
        int height = (int) (width * 0.52);
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
        getDialog().setCanceledOnTouchOutside(true);

        // khai báo control
        messageTV = view.findViewById(R.id.mgsTV);
        okBtn = view.findViewById(R.id.btnYes);

        // chỉnh font textview
        Typeface type = Typeface.createFromAsset(mContext.getAssets(), "fonts/boosternextfy_bold.ttf");
        messageTV.setTypeface(type);
        if(mgs!=null)messageTV.setText(mgs);
        // bắt sự kiện click
        okBtn.setOnClickListener(dialog_confirm_click);

    }


    View.OnClickListener dialog_confirm_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getDialog().dismiss();
            if(runable != null)
                runable.run();
        }
    };
}
