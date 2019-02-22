package com.alpay.codenotes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.alpay.codenotes.R;
import com.alpay.codenotes.models.Feedback;
import com.alpay.codenotes.utils.NavigationManager;
import com.alpay.codenotes.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SendFeedbackFragment extends Fragment {

    @BindView(R.id.feedbackTitleEditText)
    EditText feedbackTitleEditText;

    @BindView(R.id.feedbackDetailEditText)
    EditText feedbackDetailEditText;

    @BindView(R.id.formView)
    ScrollView formView;

    @BindView(R.id.successView)
    LinearLayout successView;

    public View view;
    private Unbinder unbinder;
    public SendFeedbackFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_send_feedback, container, false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }

    @Nullable
    @OnClick(R.id.sendFeedBackButton)
    protected void sendFeedBack() {
        String feedbackTitle = feedbackTitleEditText.getText().toString();
        String feedbackDetail = feedbackDetailEditText.getText().toString();

        if (feedbackTitle.equals("")) {
            Utils.printToastShort((AppCompatActivity) getActivity(), R.string.sendfeedback_formtitle_required);
            return;
        }
        if (feedbackDetail.equals("")) {
            Utils.printToastShort((AppCompatActivity) getActivity(), R.string.sendfeedback_formcontent_required);
            return;
        }
        sendFeedbackToFirebase(feedbackTitle, feedbackDetail);
        changeToSuccessView();
    }

    @Nullable
    @OnClick(R.id.feedbackSentOkButton)
    protected void backToLatestScreen() {
        NavigationManager.openFragment((AppCompatActivity) getActivity(), NavigationManager.HOME);
    }

    protected void sendFeedbackToFirebase(String title, String detail) {
        Feedback feedback = new Feedback(title, detail);
    }

    protected void changeToSuccessView() {
        formView.setVisibility(View.GONE);
        successView.setVisibility(View.VISIBLE);
    }

}
