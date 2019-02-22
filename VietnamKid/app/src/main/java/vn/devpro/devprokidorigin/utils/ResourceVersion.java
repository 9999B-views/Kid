package vn.devpro.devprokidorigin.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class ResourceVersion {
    private SharedPreferences mPreferences;

    public ResourceVersion(Context context) {
        mPreferences = context.getSharedPreferences("resourceVersion", Context.MODE_PRIVATE);
    }

    public String getResourceVersion() {
        String resourceVersion = mPreferences.getString("resourceVersion", Global.NO_DATA_KEY);
        Global.resourceVersion = resourceVersion;
        Log.d("resourceVersion", resourceVersion);
        return resourceVersion;
    }

    public boolean setResourceVersion(String resourceVersion) {
        // lưu xuống bộ nhớ
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString("resourceVersion", resourceVersion);
        if (editor.commit()) {
            // cập nhật vào global
            Global.resourceVersion = resourceVersion;
            return true;
        }
        return false;
    }
}
