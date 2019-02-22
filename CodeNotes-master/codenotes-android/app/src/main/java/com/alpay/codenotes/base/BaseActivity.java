package com.alpay.codenotes.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.alpay.codenotes.R;
import com.alpay.codenotes.utils.NavigationManager;

public class BaseActivity extends AppCompatActivity {

    ActionBar actionBar;
    String chapterName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        actionBar = getSupportActionBar();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            chapterName = bundle.getString(NavigationManager.BUNDLE_KEY);
            NavigationManager.openFragmentFromBase(this, chapterName);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_feedback:
                NavigationManager.openFragment(this, NavigationManager.SEND_FEEDBACK);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    protected void setSupportActionBarName(int resID) {
        actionBar.setTitle(resID);
    }
}
