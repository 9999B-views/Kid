package com.alpay.wesapiens.base;

import androidx.appcompat.app.AppCompatDelegate;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}