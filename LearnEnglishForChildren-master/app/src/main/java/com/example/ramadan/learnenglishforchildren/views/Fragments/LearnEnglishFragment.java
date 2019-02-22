package com.example.ramadan.learnenglishforchildren.views.Fragments;


import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramadan.learnenglishforchildren.R;
import com.example.ramadan.learnenglishforchildren.presenters.LearnEnglishPresenterImpl;
import com.example.ramadan.learnenglishforchildren.views.Activities.LearnEnglishActivity;
import com.example.ramadan.learnenglishforchildren.views.Interfaces.LearnEnglishView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LearnEnglishFragment extends Fragment implements LearnEnglishView, View.OnClickListener {
    private int[] arrayPhotosOfCharacters;
    private int[] arrayPhotosOfWords;
    private String[] charactersVoice;
    private String[] wordsVoice;
    private int indexNow = 0;
    private TextView wordTitleTv;
    private ImageView imageCharIv;
    private ImageView imageWordIv;
    private TextToSpeech textMakeVoice;
    private Button backBtn;
    private Button nextBtn;
    private Button voiceCharBtn;
    private Button voiceWordBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.learn_english_fragment, container, false);
        wordTitleTv = (TextView) view.findViewById(R.id.word_txt_id);
        imageCharIv = (ImageView) view.findViewById(R.id.image_char_id);
        imageWordIv = (ImageView) view.findViewById(R.id.image_word_id);
        backBtn = (Button) view.findViewById(R.id.go_back_id);
        nextBtn = (Button) view.findViewById(R.id.go_front_id);
        voiceCharBtn = (Button) view.findViewById(R.id.speak_char_id);
        voiceWordBtn = (Button) view.findViewById(R.id.speak_Word_id);
        backBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        voiceCharBtn.setOnClickListener(this);
        voiceWordBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initialLearnEnglishData();
    }

    @Override
    public void initialLearnEnglishData() {
        LearnEnglishPresenterImpl learnEnglishPresenter = new LearnEnglishPresenterImpl(getActivity(), this);
        learnEnglishPresenter.getPhotosOfWords();
        learnEnglishPresenter.getWordsOfPhotos();
        learnEnglishPresenter.getCharactersOfPhotos();
        learnEnglishPresenter.getPhotosOfCharacters();
        imageCharIv.setImageResource(arrayPhotosOfCharacters[indexNow]);
        imageWordIv.setImageResource(arrayPhotosOfWords[indexNow]);
        wordTitleTv.setText(wordsVoice[indexNow]);
        initialSpeech();
    }

    @Override
    public void initialSpeech() {
        textMakeVoice = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textMakeVoice.setLanguage(Locale.ENGLISH);
                } else {
                    showErrMsg(getString(R.string.language_not_sported));
                }
            }
        });
    }

    @Override
    public void onPhotosOfCharactersReceived(int[] arrayPhotosOfCharacters) {
        this.arrayPhotosOfCharacters = arrayPhotosOfCharacters;
    }

    @Override
    public void onPhotosOfWordsReceived(int[] arrayPhotosOfWords) {
        this.arrayPhotosOfWords = arrayPhotosOfWords;
    }

    @Override
    public void onCharactersReceived(String[] charactersVoice) {
        this.charactersVoice = charactersVoice;
    }

    @Override
    public void onWordsReceived(String[] wordsVoice) {
        this.wordsVoice = wordsVoice;
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showErrMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

//    @OnClick(R.id.speak_char_id)
//    void onVoiceCharClicked() {
//        String charVoiceNow = charactersVoice[indexNow];
//        textMakeVoice.speak(charVoiceNow, TextToSpeech.QUEUE_FLUSH, null);
//    }
//
//    @OnClick(R.id.speak_Word_id)
//    void onVoiceWordClicked() {
//        String wordVoiceNow = wordsVoice[indexNow];
//        textMakeVoice.speak(wordVoiceNow, TextToSpeech.QUEUE_FLUSH, null);
//    }
//
//    @OnClick(R.id.go_back_id)
//    void onGoBackClicked() {
//        indexNow--;
//        if (indexNow == -1) {
//            indexNow = wordsVoice.length - 1;
//            setDataOfThisIndex(indexNow);
//        } else
//            setDataOfThisIndex(indexNow);
//    }
//
//    @OnClick(R.id.go_front_id)
//    void onGoFrontClicked() {
//        indexNow++;
//        if (indexNow == charactersVoice.length && indexNow == wordsVoice.length) {
//            indexNow = 0;
//            setDataOfThisIndex(indexNow);
//        } else
//            setDataOfThisIndex(indexNow);
//
//    }

    @Override
    public void setDataOfThisIndex(int index) {
        imageCharIv.setImageResource(arrayPhotosOfCharacters[index]);
        imageWordIv.setImageResource(arrayPhotosOfWords[index]);
        wordTitleTv.setText(wordsVoice[index]);
    }

    @Override
    public void onDestroy() {
        if (textMakeVoice != null) {
            textMakeVoice.stop();
            textMakeVoice.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.speak_char_id:
                String charVoiceNow = charactersVoice[indexNow];
                textMakeVoice.speak(charVoiceNow, TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.speak_Word_id:
                String wordVoiceNow = wordsVoice[indexNow];
                textMakeVoice.speak(wordVoiceNow, TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.go_back_id:
                indexNow--;
                if (indexNow == -1) {
                    indexNow = wordsVoice.length - 1;
                    setDataOfThisIndex(indexNow);
                } else
                    setDataOfThisIndex(indexNow);
                break;
            case R.id.go_front_id:
                indexNow++;
                if (indexNow == charactersVoice.length && indexNow == wordsVoice.length) {
                    indexNow = 0;
                    setDataOfThisIndex(indexNow);
                } else
                    setDataOfThisIndex(indexNow);

                break;
        }
    }
}
