package vn.devpro.devprokidorigin.interfaces;

import android.content.Context;

import java.util.ArrayList;

import vn.devpro.devprokidorigin.models.entity.TopicName;

/**
 * Created by admin on 3/26/2018.
 */

public interface HocTapView {
    void updateUI();
    void displayData(ArrayList<TopicName> list);
    void displayError(String message);
    Context getViewContext();
}

