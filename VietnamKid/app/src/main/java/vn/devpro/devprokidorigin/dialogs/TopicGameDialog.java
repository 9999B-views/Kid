package vn.devpro.devprokidorigin.dialogs;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.makeramen.roundedimageview.RoundedImageView;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.interfaces.ITopicClick;

/**
 * Created by hoang-ubuntu on 22/05/2018.
 */

@SuppressLint("ValidFragment")
public class TopicGameDialog extends DialogFragment implements View.OnClickListener {
    public static final String TAG = TopicGameDialog.class.getSimpleName();
    private RoundedImageView rivMatch1;
    private RoundedImageView rivMatch2;
    private RoundedImageView rivMatch3;
    private RoundedImageView rivMatch4;
    private RoundedImageView rivMatch5;
    private RoundedImageView rivMatch6;
    private RoundedImageView rivMatch7;
    private ImageButton imgClose;
    private Animation animation;
    private ITopicClick listener;
    private int topic = 0;
    private ConstraintLayout constraintLayout;

    public TopicGameDialog(ITopicClick listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.bounce);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
                if (listener != null) {
                    listener.onTopicClick(topic, 0, false);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_matching, container, false);
        rivMatch1 = view.findViewById(R.id.rivMatch1);
        rivMatch2 = view.findViewById(R.id.rivMatch2);
        rivMatch3 = view.findViewById(R.id.rivMatch3);
        rivMatch4 = view.findViewById(R.id.rivMatch4);
        rivMatch5 = view.findViewById(R.id.rivMatch5);
        rivMatch6 = view.findViewById(R.id.rivMatch6);
        rivMatch7 = view.findViewById(R.id.rivMatch7);
        imgClose = view.findViewById(R.id.closeDialogGame);
        constraintLayout = view.findViewById(R.id.constraintLayout);

        rivMatch1.setOnClickListener(this);
        rivMatch2.setOnClickListener(this);
        rivMatch3.setOnClickListener(this);
        rivMatch4.setOnClickListener(this);
        rivMatch5.setOnClickListener(this);
        rivMatch5.setOnClickListener(this);
        rivMatch6.setOnClickListener(this);
        rivMatch7.setOnClickListener(this);
        imgClose.setOnClickListener(this);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setLayout(width, height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rivMatch1:
                topic = 5;
                rivMatch1.startAnimation(animation);
                break;

            case R.id.rivMatch2:
                topic = 7;
                rivMatch2.startAnimation(animation);
                break;

            case R.id.rivMatch3:
                topic = 3;
                rivMatch3.startAnimation(animation);
                break;

            case R.id.rivMatch4:
                topic = 4;
                rivMatch4.startAnimation(animation);
                break;

            case R.id.rivMatch5:
                topic = 6;
                rivMatch5.startAnimation(animation);
                break;

            case R.id.rivMatch6:
                topic = 1;
                rivMatch6.startAnimation(animation);
                break;

            case R.id.rivMatch7:
                rivMatch7.startAnimation(animation);
                topic = 2;
                break;

            case R.id.closeDialogGame:
                this.dismiss();
                break;

            default:
                break;
        }
    }
}
