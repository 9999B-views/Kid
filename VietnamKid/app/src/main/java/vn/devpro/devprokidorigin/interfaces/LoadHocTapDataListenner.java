package vn.devpro.devprokidorigin.interfaces;

import java.util.ArrayList;
import vn.devpro.devprokidorigin.models.entity.TopicName;

/**
 * Created by admin on 3/26/2018.
 */

public interface LoadHocTapDataListenner {
    void onLoadTopicNameSuccess(ArrayList<TopicName> listData);
    void onLoadTopicNameFailure(String message);
}
