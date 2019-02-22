package com.example.ramadan.learnenglishforchildren.base;


public interface BasePresenter<V extends BaseView> {
    void setView(V view);
}
