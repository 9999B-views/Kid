package com.example.ramadan.learnenglishforchildren.presenters;

import android.content.Context;

import com.example.ramadan.learnenglishforchildren.R;
import com.example.ramadan.learnenglishforchildren.base.BaseView;
import com.example.ramadan.learnenglishforchildren.views.Interfaces.LearnEnglishView;

public class LearnEnglishPresenterImpl implements LearnEnglishPresenter {
    private LearnEnglishView learnEnglishView;
    private Context context;

    public LearnEnglishPresenterImpl(Context mContext,BaseView view)
    {
        setView(view);
        context=mContext;
    }
    @Override
    public void getCharactersOfPhotos() {

     String [] charactersVoice=  context.getResources().getStringArray(R.array.abc);
     learnEnglishView.onCharactersReceived(charactersVoice);
    }

    @Override
    public void getWordsOfPhotos() {
      String wordsVoice []=  context.getResources().getStringArray(R.array.wordsvoice);
      learnEnglishView.onWordsReceived(wordsVoice);
    }

    @Override
    public void getPhotosOfCharacters() {
        int arrayPhotosOfCharacters[]={R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,R.drawable.f,R.drawable.j,R.drawable.h,
                R.drawable.i,R.drawable.g,R.drawable.k,R.drawable.l,R.drawable.m,R.drawable.n,R.drawable.o,R.drawable.p,R.drawable.q,R.drawable.r
                ,R.drawable.s,R.drawable.t,R.drawable.u,R.drawable.v,R.drawable.w,R.drawable.x,R.drawable.y,R.drawable.z};
        learnEnglishView.onPhotosOfCharactersReceived(arrayPhotosOfCharacters);
    }

    @Override
    public void getPhotosOfWords() {
        int arrayPhotosOfWords[]={R.drawable.ant,R.drawable.butterfly,R.drawable.cat,R.drawable.dog,R.drawable.elephont,R.drawable.frog,R.drawable.jellyfish
                ,R.drawable.hippoptamous,R.drawable.iguana,R.drawable.giraffe,R.drawable.kangaroo,R.drawable.lion,R.drawable.monkey,R.drawable.narwhal
                ,R.drawable.owl,R.drawable.panda,R.drawable.quetzal,R.drawable.rat,R.drawable.sheep,R.drawable.turtle,R.drawable.unicorn,R.drawable.viper
                ,R.drawable.worm,R.drawable.xrayfish,R.drawable.yak,R.drawable.zebra};
        learnEnglishView.onPhotosOfWordsReceived(arrayPhotosOfWords);
    }

    @Override
    public void setView(BaseView view) {
        learnEnglishView= (LearnEnglishView) view;
    }
}
