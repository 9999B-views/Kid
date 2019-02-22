package com.example.ramadan.learnenglishforchildren.views.Interfaces;

import com.example.ramadan.learnenglishforchildren.base.BaseView;

public interface LearnEnglishView extends BaseView {
    void initialLearnEnglishData();
    void initialSpeech();
    void onPhotosOfCharactersReceived(int arrayPhotosOfCharacters[]);
    void onPhotosOfWordsReceived(int arrayPhotosOfWords[]);

    void onCharactersReceived(String [] charactersVoice);
    void onWordsReceived(String wordsVoice []);
    void setDataOfThisIndex(int index);
}
