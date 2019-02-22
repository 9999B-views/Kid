package vn.devpro.devprokidorigin.activities.game.latbai.commom;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;

import vn.devpro.devprokidorigin.activities.game.latbai.engine_moitruong.Engine;
import vn.devpro.devprokidorigin.activities.game.latbai.even.ui.EventBus;
import vn.devpro.devprokidorigin.models.entity.TopicName;

/**
 * Created by admin on 3/30/2018.
 */

public class    Shared {
//     giống như 1 interfae  , lưu dữ liệu tạm thời
    public static Context context;
    public static FragmentActivity activity;
    public static Engine engine ;
    public static EventBus eventBus;
    public static ArrayList<TopicName> listTopicName;

}
