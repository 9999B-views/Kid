package vn.devpro.devprokidorigin.dialogs;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.adapters.QuangCaoAdapter;
import vn.devpro.devprokidorigin.models.ItemProperty;
import vn.devpro.devprokidorigin.models.QuangCaoModel;

/**
 * Created by NamQuoc on 4/9/2018.
 */

public class DialogMoreApp extends DialogFragment {
    private static final int RELATIVE_MARGIN_LEFT_RIGHT = 40;
    private static int ITEM_MARGIN_TOP_BOTTOM = 0;
    private static int ITEM_SPACING = 0;
    private static int RELATIVE_PADDING_ITEM = 0;
    private ConstraintLayout constrainQuangCao;
    private final int NUM_COLUMNS = 2;
    private ImageView imgDong;
    private RecyclerView rcQuangCao;
    private QuangCaoAdapter adapter;
    private ArrayList<QuangCaoModel> quangCaoModelArrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main_quangcao, container);
    }

    @Override
    public void onResume() {
        super.onResume();
        // cài đặt kịch thước dialog
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.85f);
        int height = (int) (width * 0.59f);
        //int height = (int)(getResources().getDisplayMetrics().heightPixels * 0.93f);
        getDialog().getWindow().setLayout(width, height);
        getDialog().getWindow().setGravity(Gravity.CENTER | Gravity.CENTER);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //xoa thanh title bar
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // ẩn tiêu đề dialog
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ẩn background dialog
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // tắt chức năng dismiss outside
        getDialog().setCanceledOnTouchOutside(false);
        // khai báo control cho imagebutton trong dialog để bắt sự kiện
        imgDong = view.findViewById(R.id.imgDong);
        constrainQuangCao = view.findViewById(R.id.constrainQuangCao);
        this.rcQuangCao = view.findViewById(R.id.rcQuangCao);
        quangCaoModelArrayList = new ArrayList<>();
        quangCaoModelArrayList.add(new QuangCaoModel(R.mipmap.ic_launcher, "Game Bé vui học chữ cực hay","Game này chơi rất vui, đặc biệt dành cho trẻ em dưới 10 tuổi, game vừa giải trí vừa phát triển trí tuệ, giúp trẻ học giỏi hơn", "4.5"));
        quangCaoModelArrayList.add(new QuangCaoModel(R.mipmap.ic_launcher, "Game Bé vui học chữ cực hay","Game này chơi rất vui, đặc biệt dành cho trẻ em dưới 10 tuổi, game vừa giải trí vừa phát triển trí tuệ, giúp trẻ học giỏi hơn", "4.5"));
        quangCaoModelArrayList.add(new QuangCaoModel(R.mipmap.ic_launcher, "Game Bé vui học chữ cực hay","Game này chơi rất vui, đặc biệt dành cho trẻ em dưới 10 tuổi, game vừa giải trí vừa phát triển trí tuệ, giúp trẻ học giỏi hơn", "4.5"));
        quangCaoModelArrayList.add(new QuangCaoModel(R.mipmap.ic_launcher, "Game Bé vui học chữ cực hay","Game này chơi rất vui, đặc biệt dành cho trẻ em dưới 10 tuổi, game vừa giải trí vừa phát triển trí tuệ, giúp trẻ học giỏi hơn", "4.5"));
        quangCaoModelArrayList.add(new QuangCaoModel(R.mipmap.ic_launcher, "Game Bé vui học chữ cực hay","Game này chơi rất vui, đặc biệt dành cho trẻ em dưới 10 tuổi, game vừa giải trí vừa phát triển trí tuệ, giúp trẻ học giỏi hơn", "4.5"));
        quangCaoModelArrayList.add(new QuangCaoModel(R.mipmap.ic_launcher, "Game Bé vui học chữ cực hay","Game này chơi rất vui, đặc biệt dành cho trẻ em dưới 10 tuổi, game vừa giải trí vừa phát triển trí tuệ, giúp trẻ học giỏi hơn", "4.5"));

        initLayoutQuangCao(constrainQuangCao);
        // bắt sự kiện cho button dialog
        imgDong.setOnClickListener(dialog_click);
    }
    public static float convertDptoPx(float dp, DisplayMetrics displayMetrics) {
        float px = dp * displayMetrics.density;
        return px;
    }
    private void initLayoutQuangCao(ConstraintLayout constraintLayout){

        int totalNumColums = quangCaoModelArrayList.size();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        ITEM_MARGIN_TOP_BOTTOM = (int) (height / 12.8f);
        int heighTop = (int) convertDptoPx(66, displayMetrics);
        int llheigh = height - heighTop;
        int width = displayMetrics.widthPixels;
        ITEM_SPACING = (int) (width * (61 / 809.0f));
        width = width - ITEM_SPACING * 4;
        int itemwidth = width / NUM_COLUMNS;
        int itemheigh = (int) (itemwidth * 1.196f);
        int totalItemheigh = itemheigh + ITEM_MARGIN_TOP_BOTTOM * 2;
        if (totalItemheigh > llheigh) {
            float scaleDownFactor = ((float) llheigh / totalItemheigh);
            itemwidth = (int) (itemwidth * scaleDownFactor);
            itemheigh = (int) (itemheigh * scaleDownFactor);
        }
        int totalwidth = itemwidth * totalNumColums + ITEM_SPACING * (totalNumColums - 1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        ItemProperty itp = new ItemProperty(itemwidth, itemheigh, ITEM_SPACING, ITEM_MARGIN_TOP_BOTTOM);
        rcQuangCao.setLayoutManager(linearLayoutManager);
        adapter = new QuangCaoAdapter(getContext(), quangCaoModelArrayList, itp);
        rcQuangCao.setAdapter(adapter);
    }
    private View.OnClickListener dialog_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imgDong:
                    getDialog().dismiss();
                    break;
            }
        }
    };
}
