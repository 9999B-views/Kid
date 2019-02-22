package vn.devpro.devprokidorigin.activities.game.dapbongbay.game;

import android.app.Fragment;
import android.os.Bundle;


import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.GameBaseActivity;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.sound.SoundManager;
import vn.devpro.devprokidorigin.interfaces.BackListener;
import vn.devpro.devprokidorigin.utils.AdMob.InterstitialAds;

public class BalloonGameActivity extends GameBaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.framecontainer, createMenuFragment(), TAG_FRAGMENT)
                    .commit();
        }
    }

    @Override
    protected SoundManager createSoundManager() {
        return new BalloonsSoundManager(getApplicationContext());
    }

    protected Fragment createMenuFragment() {
        return new MainMenuFragment();
    }

    public void startGame() {
        navigateToFragment(new GameFragment());
    }

    @Override
    public void toDoBackListener() {
        finish();
    }
}
