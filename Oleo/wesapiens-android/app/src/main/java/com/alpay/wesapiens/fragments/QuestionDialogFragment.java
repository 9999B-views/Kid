package com.alpay.wesapiens.fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alpay.wesapiens.R;
import com.alpay.wesapiens.listener.OnSwipeTouchListener;
import com.alpay.wesapiens.utils.Constants;
import com.alpay.wesapiens.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class QuestionDialogFragment extends DialogFragment {

    public View view;
    private Unbinder unbinder;
    private String mAnswer;
    private String mQuestionTitle;
    private List<String> mQuestionBody;
    private int mCurrentBodyPosition = 0;
    private String mResult;

    @BindView(R.id.question_dialog_title)
    TextView questionDialogTitle;

    @BindView(R.id.question_dialog_body)
    TextView questionDialogBody;

    @BindView(R.id.question_edittext)
    EditText questionEditText;

    @BindView(R.id.question_dialog_frame)
    FrameLayout questionDialogFrame;

    @BindView(R.id.question_submit_button)
    Button submitButton;

    @OnClick(R.id.question_submit_button)
    public void submitButtonAction(){
        checkAnswer();
    }

    @OnClick(R.id.question_next_page_button)
    public void nextButtonAction(){
        nextPage();
    }

    @OnClick(R.id.question_prev_page_button)
    public void prevButtonAction(){
        previousPage();
    }

    public QuestionDialogFragment() {

    }

    public interface QuestionDialogListener {
        void onFinishDialog(String inputText);
    }


    public static QuestionDialogFragment newInstance() {
        QuestionDialogFragment questionDialogFragment = new QuestionDialogFragment();
        return questionDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_question_dialog, container);
        unbinder = ButterKnife.bind(this, view);
        questionDialogFrame.setOnTouchListener(new OnSwipeTouchListener(getActivity()){
            @Override
            public void onSwipeLeft() {
                nextPage();
                super.onSwipeLeft();
            }

            @Override
            public void onSwipeRight() {
                previousPage();
                super.onSwipeRight();
            }
        });
        mCurrentBodyPosition = 0;
        questionDialogTitle.setText(mQuestionTitle);
        questionDialogBody.setText(mQuestionBody.get(mCurrentBodyPosition));
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setGravity(Gravity.RIGHT | Gravity.TOP);
        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.x = 100; // right margin
        layoutParams.y = 100; // top margin
        getDialog().getWindow().setAttributes(layoutParams);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUI();
    }

    private void hideSystemUI() {
        view.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        QuestionDialogListener listener = (QuestionDialogListener) getActivity();
        listener.onFinishDialog(mResult);
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public void setQuestionDialogTitle(String title){
        mQuestionTitle = title;
    }

    public void setQuestionDialogBody(String context, String question){
        mQuestionBody = new ArrayList<>();
        String[] contextSentences = Utils.splitParagraphToSentences(context);
        mQuestionBody.addAll(Arrays.asList(contextSentences));
        mQuestionBody.add(question);
    }

    public void setQuestionAnswer(String answer){
        mAnswer = answer;
    }

    public String getAnswer() {
        return mAnswer;
    }

    private void checkAnswer(){
        if(questionEditText.getText().toString().isEmpty()){
            Utils.showWarningToast((AppCompatActivity) getActivity(), R.string.empty_area_error, Toast.LENGTH_SHORT);
        }else{
            int userAnswer= Integer.valueOf(questionEditText.getText().toString());
            if(userAnswer == Integer.valueOf(mAnswer)){
                mResult = Constants.CORRECT_ANSWER;
            }else{
                mResult = Constants.WRONG_ANSWER;
            }
            dismiss();
        }
    }

    private void nextPage(){
        if(mCurrentBodyPosition < mQuestionBody.size() -1){
            mCurrentBodyPosition++;
            questionDialogBody.setText(Utils.fromHtml(mQuestionBody.get(mCurrentBodyPosition)));
        }
        if(mCurrentBodyPosition == mQuestionBody.size() -1){
            questionEditText.setVisibility(View.VISIBLE);
            questionEditText.setText("");
            submitButton.setVisibility(View.VISIBLE);
        }
    }

    private void previousPage(){
        if(mCurrentBodyPosition > 0){
            mCurrentBodyPosition--;
            questionDialogBody.setText(Utils.fromHtml(mQuestionBody.get(mCurrentBodyPosition)));
        }
    }
}
