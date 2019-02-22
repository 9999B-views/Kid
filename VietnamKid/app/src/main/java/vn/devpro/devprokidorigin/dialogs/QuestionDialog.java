package vn.devpro.devprokidorigin.dialogs;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.List;
import java.util.Random;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.databases.DBHelper;
import vn.devpro.devprokidorigin.interfaces.MyCompleteSound;
import vn.devpro.devprokidorigin.models.entity.hocchucai.LetterModelSmall;
import vn.devpro.devprokidorigin.models.entity.hocchucai.Question;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.models.Sound;
import vn.devpro.devprokidorigin.utils.Utils;

/**
 * Created by hoang-ubuntu on 21/03/2018.
 */

@SuppressLint("ValidFragment")
public class QuestionDialog extends DialogFragment implements View.OnClickListener, MyCompleteSound {
    public static final String TAG = QuestionDialog.class.getSimpleName();
    private final long TIME_ANIM = 600;
    private final String UP = "_up";
    private final String DAP_AN = "DAPAN";



    private ImageView img_question1, img_question2, img_question3, img_question4;
    private ImageButton btn_Close_Question;
    private ImageButton btn_soundAG;
    private int resultID;
    private String soundName;
    private List<Question> listQuestion;
    private List<LetterModelSmall> listChuCai;
    private int numberCorrect;
    private String soundQs = "";
    private Animation anim;

    public QuestionDialog(List<Question> listQuestion, List<LetterModelSmall> list) {
        this.listQuestion = listQuestion;
        this.listChuCai = list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resultID = listQuestion.get(0).getId();
        if (Global.getLanguage() == Global.VN) {
            soundName = listQuestion.get(0).getSound_vn();
        } else {
            soundName = listQuestion.get(0).getSound_en();
        }
        Log.d(TAG, "Ten dap an: " + soundName);
        numberCorrect = listQuestion.get(0).getCorrect_count();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.question_dialog, container, false);
        img_question1 = view.findViewById(R.id.img_question1);
        img_question1.setOnClickListener(this);
        img_question2 = view.findViewById(R.id.img_question2);
        img_question2.setOnClickListener(this);
        img_question3 = view.findViewById(R.id.img_question3);
        img_question3.setOnClickListener(this);
        img_question4 = view.findViewById(R.id.img_question4);
        img_question4.setOnClickListener(this);
        btn_Close_Question = view.findViewById(R.id.btn_Close_Question);
        btn_Close_Question.setOnClickListener(this);
        btn_soundAG = view.findViewById(R.id.btn_soundAG);
        btn_soundAG.setOnClickListener(this);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        setQuestion(img_question1, 3);
        setQuestion(img_question2, 2);
        setQuestion(img_question3, 1);
        setQuestion(img_question4, 0);
        /*Doc cau hoi*/
        if (soundName != null) {
            Log.d(TAG, "Am cau hoi: " + soundQs);
            Sound.getInstance(Global.mContext).setMyCompleteSound(this);
            if (Global.getLanguage() == Global.VN) {
                Sound.getInstance(Global.mContext).playEncrytedFile("q_vn_1");
            } else {
                Sound.getInstance(Global.mContext).playEncrytedFile("q_en_1");
            }
        } else {
            /*Do something*/
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setLayout(width, height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_Close_Question:
                this.dismiss();
                break;

            case R.id.btn_soundAG:
                Sound.getInstance(Global.mContext).setMyCompleteSound(this);
                if (Global.getLanguage() == Global.VN) {
                    Sound.getInstance(Global.mContext).playEncrytedFile("q_vn_1");
                } else {
                    Sound.getInstance(Global.mContext).playEncrytedFile("q_en_1");
                }

                break;

            case R.id.img_question1:
                checkResult(img_question1);
                break;

            case R.id.img_question2:
                checkResult(img_question2);
                break;

            case R.id.img_question3:
                checkResult(img_question3);
                break;

            case R.id.img_question4:
                checkResult(img_question4);
                break;

            default:
                break;
        }
    }

    private void setQuestion(ImageView img_question, int a) {
        int random;
        String name = "";
        random = randomQuestion(a);
        if (listQuestion.get(random).getId() == resultID) {
            img_question.setTag(DAP_AN);
        }
        name = getNameFromList(listQuestion.get(random).getId());
        byte[] data = Utils.decodeFileImage(Utils.getByteArrayFromSD(Global.pathImages,name + UP, ".png"));

        if (data != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            img_question.setImageBitmap(bitmap);
            listQuestion.remove(random);
        } else {
            img_question.setImageResource(R.drawable.no_img_available);
            listQuestion.remove(random);
        }
    }

    private void checkResult(final ImageView imageView) {
        if (imageView.getTag() == DAP_AN) {
            DBHelper.getInstance(getContext()).updateColumnCorrectCount(resultID, numberCorrect + 1);
            /*Neu tra loi dung (am thanh + anim)*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                ObjectAnimator.ofFloat(imageView, "rotation", 0, 20, -20, 15, -15, 10, -10, 5, -5, 0)
                        .setDuration(500)
                        .start();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    QuestionDialog.this.dismiss();
                }
            },1500);

        } else {
            //TODO: Tra loi sai
            anim = AnimationUtils.loadAnimation(getContext(), R.anim.shake_hoc_chu);
            imageView.startAnimation(anim);
        }
    }

    private int randomQuestion(int a) {
        if (a <= 0) {
            return 0;
        }
        Random random = new Random();
        int rd = random.nextInt(a);
        return rd;
    }

    private String getNameFromList(int id) {
        String name = "";
        for (int i = 0; i < listChuCai.size(); i++) {
            if (listChuCai.get(i).getId() == (id)) {
                name = listChuCai.get(i).getImage_name();
                break;
            }
        }
        return name;
    }


    @Override
    public void onComplete(MediaPlayer mediaPlayer) {
        Sound.getInstance(Global.mContext).playEncrytedFile(soundName);
    }
}
