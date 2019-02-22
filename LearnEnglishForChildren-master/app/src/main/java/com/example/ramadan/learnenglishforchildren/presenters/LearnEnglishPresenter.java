package com.example.ramadan.learnenglishforchildren.presenters;

import com.example.ramadan.learnenglishforchildren.base.BasePresenter;

public interface LearnEnglishPresenter extends BasePresenter {
    void getCharactersOfPhotos();
    void getWordsOfPhotos();

    void getPhotosOfCharacters();
    void getPhotosOfWords();
}
