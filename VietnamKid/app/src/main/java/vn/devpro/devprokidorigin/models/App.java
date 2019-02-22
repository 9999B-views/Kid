package vn.devpro.devprokidorigin.models;

import android.app.Application;
import android.content.Context;

import vn.devpro.devprokidorigin.utils.NetworkReceiver;

/**
 * Created by Laptop88 on 3/30/2018.
 */

public class App extends Application {

    private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

  /*  public void setConnectionListener(NetworkReceiver.ConnectionReceiverListener listener) {
        NetworkReceiver.connectionReceiverListener = listener;
    }*/
}