package vn.devpro.devprokidorigin.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.controllers.hoctap.QuestionLoadingTask;
import vn.devpro.devprokidorigin.databases.DBHelper;
import vn.devpro.devprokidorigin.models.Sound;
import vn.devpro.devprokidorigin.models.entity.hocchucai.LetterModelSmall;
import vn.devpro.devprokidorigin.models.entity.hocchucai.Question;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.utils.Utils;

/**
 * Created by HOANG on 3/9/2018.
 */

@SuppressLint("ValidFragment")
public class NhanBietChuDialog extends DialogFragment implements View.OnClickListener,
        QuestionLoadingTask.IQuestionLoadingListener {
    private final String XVN = "_xvn";
    private final String XEN = "_xen";
    private final String UP = "_up";
    private final String LOW = "_low";
    public static final String TAG = NhanBietChuDialog.class.getSimpleName();
    private final int OPEN_GIFT_TIME = 600;
    private Context context;
    private List<LetterModelSmall> list;
    private int idName;
    private int count1 = 0, count2 = 0;
    private boolean isOpenFinished = true;
    private ImageButton btnClose, btnBackDialog, btnNextDialog;
    private ImageView img_Pic1, img_Pic2;
    private DBHelper dbHelper;
    private List<Question> questionList;
    private QuestionLoadingTask questionLoadingTask;
    private int numberQuestion, showQuestion;
    private boolean isShow = true;
    private int idSound;
    private Animation flipAnim;

    public NhanBietChuDialog(List<LetterModelSmall> list, int idName) {
        this.list = list;
        this.idName = idName;
    }

    public NhanBietChuDialog(Context context, List<LetterModelSmall> list, int idName) {
        this.context = context;
        this.list = list;
        this.idName = idName;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = DBHelper.getInstance(this.getContext());
        questionList = new ArrayList<>();
        initQuestionList();
    }

    private void hideSystemUI() {
        getDialog().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate view cho Dialog nay
        View view = inflater.inflate(R.layout.nhanbietchu_dialog, null);
        btnClose = view.findViewById(R.id.btn_Close);
        btnClose.setOnClickListener(this);
        img_Pic1 = view.findViewById(R.id.btn_Hinh1);
        img_Pic1.setOnClickListener(this);
        img_Pic2 = view.findViewById(R.id.btn_Hinh2);
        img_Pic2.setOnClickListener(this);
        btnBackDialog = view.findViewById(R.id.btn_back_dialog);
        btnBackDialog.setOnClickListener(this);
        btnNextDialog = view.findViewById(R.id.btn_next_dialog);
        btnNextDialog.setOnClickListener(this);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getDialog().getWindow().getDecorView().setSystemUiVisibility(container.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setLayout(width, height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        hideSystemUI();
        if (dbHelper.checkColumnShowRatio(idName) == 0) {
            initGifts();
        } else {
            setChuCai();
            count1 = 1;
            count2 = 1;
        }
    }

    private long startTimeClick = 0;

    @Override
    public void onClick(View v) {
        long time = System.currentTimeMillis();
        if (time - startTimeClick < 600) {
            return;
        }
        startTimeClick = time;

        switch (v.getId()) {
            case R.id.btn_Close:
                this.dismiss();
                break;

            case R.id.btn_Hinh1:
                if (!isOpenFinished) {
                    return;
                }
                count1 = onClickChuCai(img_Pic1, count1, UP);
                break;

            case R.id.btn_Hinh2:
                if (!isOpenFinished) {
                    return;
                }
                count2 = onClickChuCai(img_Pic2, count2, LOW);
                break;

            case R.id.btn_back_dialog:
                onClickBack();
                initQuestionList();
                break;

            case R.id.btn_next_dialog:
                onClickNext();
                initQuestionList();
                break;

            default:
                break;
        }
    }

    private int onClickChuCai(ImageView imageView, int count, String tail) {
        flipAnim = AnimationUtils.loadAnimation(this.getContext(), R.anim.flip_hoc_chu_cai);
        if ((count < 0)) {
            return 0;
        }
        String name = getNameFromList();
        if (count == 0) {
            /*Chay animation hop qua*/
            name = name + tail;
            startAnimationDrawable(imageView, name);
            count++;
        } else if (count == 1) {
            int check = -1;
            /*Set anh chu*/
            imageView.startAnimation(flipAnim);
            if (Global.getLanguage() == Global.VN) {
                name = name + tail + XVN;
            } else {
                name = name + tail + XEN;
            }
            byte[] data = Utils.decodeFileImage(Utils.getByteArrayFromSD(Global.pathImages, name, ".png"));
            if (data == null) {
                imageView.setImageResource(R.drawable.no_img_available);
            } else {
                Sound.getInstance(Global.mContext).playEncrytedFile(name);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                imageView.setImageBitmap(bitmap);
                count++;
            }

            check = DBHelper.getInstance(Global.mContext).checkColumnShowRatio(idName);
            if (check < 3) {
                DBHelper.getInstance(Global.mContext).updateColumnShowRatio(idName, check + 1);
            }
        } else {
            /*Set anh vi du*/
            imageView.startAnimation(flipAnim);
            name = name + tail;
            byte[] data = Utils.decodeFileImage(Utils.getByteArrayFromSD(Global.pathImages, name, ".png"));
            if (data != null) {
                String sound = DBHelper.getInstance(getContext()).getSoundById(idSound);
                Sound.getInstance(Global.mContext).playEncrytedFile(sound);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                imageView.setImageBitmap(bitmap);
                count--;
            }
        }
        return count;
    }

    private void onClickBack() {
        if (idName <= 1) {
            return;
        }
        idName--;
        if (questionList.size() == 4 && showQuestion == 1 && isShow == true) {
            showQuestionDialog();
            isShow = false;
        } else if (isOpenFinished && dbHelper.checkColumnShowRatio(idName) == 0) {
            initGifts();
            isShow = true;
        } else {
            setChuCai();
            count1 = 1;
            count2 = 1;
        }
    }

    private void onClickNext() {
        if (idName >= list.size()) {
            return;
        }
        idName++;
        if (questionList.size() == 4 && showQuestion == 1 && isShow == true) {
            showQuestionDialog();
            isShow = false;
        } else if (isOpenFinished && dbHelper.checkColumnShowRatio(idName) == 0) {
            initGifts();
            isShow = true;
        } else {
            setChuCai();
            count1 = 1;
            count2 = 1;
        }
    }

    private void setChuCai() {
        int check = -1;
        String name = getNameFromList();
        String sound = DBHelper.getInstance(getContext()).getSoundById(idSound);
        byte[] data = Utils.decodeFileImage(Utils.getByteArrayFromSD(Global.pathImages, name + UP, ".png"));
        Bitmap bitmap;
        if (data != null) {
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            img_Pic1.setImageBitmap(bitmap);
            Sound.getInstance(Global.mContext).playEncrytedFile(sound);
        } else {
            img_Pic1.setImageResource(R.drawable.no_img_available);
        }
        data = Utils.decodeFileImage(Utils.getByteArrayFromSD(Global.pathImages, name + LOW, ".png"));
        if (data != null) {
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            img_Pic2.setImageBitmap(bitmap);
        } else {
            img_Pic2.setImageResource(R.drawable.no_img_available);
        }
        check = DBHelper.getInstance(Global.mContext).checkColumnShowRatio(idName);
        if (check < 3) {
            DBHelper.getInstance(Global.mContext).updateColumnShowRatio(idName, check + 1);
        }
        dbHelper.updateColumnShowRatio(idName, check);
    }

    private String getNameFromList() {
        String name = "";
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == (idName)) {
                name = list.get(i).getImage_name();
                idSound = i + 1;
                break;
            }
        }
        return name;
    }

    private void startAnimationDrawable(final ImageView imageView, final String name) {
        Drawable giftDrawable = imageView.getDrawable();
        if (giftDrawable != null && giftDrawable instanceof AnimationDrawable) {
            AnimationDrawable anim = (AnimationDrawable) giftDrawable;
            if (anim.isRunning()) {
            } else {
                anim.setOneShot(true);
                anim.start();
                isOpenFinished = false;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        byte[] data = Utils.decodeFileImage(Utils.getByteArrayFromSD(Global.pathImages, name, ".png"));
                        if (data != null) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            imageView.setImageBitmap(bitmap);
                            String sound = DBHelper.getInstance(getContext()).getSoundById(idSound);
                            Sound.getInstance(Global.mContext).playEncrytedFile(sound);
                            isOpenFinished = true;
                        } else {
                            imageView.setImageResource(R.drawable.no_img_available);
                            isOpenFinished = true;
                        }
                    }
                }, OPEN_GIFT_TIME);
                dbHelper.updateColumnShowRatio(idName, 1);
            }
        } else {
        }
    }

    /**
     * Chuyện nhảm nhí số 1 (*)
     * GUMIHO
     */
    private void initGifts() {
        count1 = 0;
        count2 = 0;
        img_Pic1.setImageResource(R.drawable.animation_list);
        img_Pic2.setImageResource(R.drawable.animation_list);
    }

    private void initQuestionList() {
        questionLoadingTask = new QuestionLoadingTask(this, dbHelper);
        questionLoadingTask.execute();
    }

    private void showQuestionDialog() {
        QuestionDialog dialog = new QuestionDialog(questionList, list);
        dialog.show(getActivity().getSupportFragmentManager(), QuestionDialog.TAG);
    }

    @Override
    public void onResultQuestion(List<Question> questions) {
        if (questions == null || questions.size() <= 0) {
            Log.e("Danh sach cau hoi, ", "NULL");
        } else {
            questionList = questions;
        }
    }

    @Override
    public void getNumberQuestion(int number) {
        numberQuestion = number;
    }

    @Override
    public void isShowQuestion(int isShow) {
        showQuestion = isShow;
    }
}
