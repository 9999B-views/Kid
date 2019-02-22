package vn.devpro.devprokidorigin.activities.game.latbai.engine_moitruong;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.activities.game.latbai.commom.Shared;
import vn.devpro.devprokidorigin.activities.game.latbai.even.ChangeBackground;
import vn.devpro.devprokidorigin.activities.game.latbai.fragments.GameFragment;
import vn.devpro.devprokidorigin.activities.game.latbai.fragments.MenuFragment;
import vn.devpro.devprokidorigin.dialogs.CustomAlertDialog;
import vn.devpro.devprokidorigin.utils.Global;

public class ScreenController {

    private static ScreenController mInstance = null;
    private static List<Screen> openedScreens = new ArrayList<Screen>();
    private FragmentManager mFragmentManager;

    private ScreenController() {
    }

    public static ScreenController getInstance() {
        if (mInstance == null) {
            mInstance = new ScreenController();
        }
        return mInstance;
    }

    public void showAlertDialog() {
        if(mFragmentManager == null) mFragmentManager = Shared.activity.getSupportFragmentManager();
        show_alert(mFragmentManager,Shared.activity);
    }

    private void show_alert(FragmentManager fm, final Context mContext) {
        CustomAlertDialog alertDialog = CustomAlertDialog.newInstance(null, new Runnable() {
            @Override
            public void run() {
                Global.doRestart(mContext);
            }
        });
        alertDialog.onAttach(mContext);
        alertDialog.show(fm,null);
    }




    public static enum Screen {
        MENU,
        GAME,


    }

    public static Screen getLastScreen() {
        return openedScreens.get(openedScreens.size() - 1);
    }

    public void openScreen(Screen screen) {
        mFragmentManager = Shared.activity.getSupportFragmentManager();


        if (screen == Screen.GAME && openedScreens.get(openedScreens.size() - 1) == Screen.GAME) {
            openedScreens.remove(openedScreens.size() - 1);
        } else if (screen == Screen.GAME && openedScreens.get(openedScreens.size() - 1) == Screen.GAME) {
            openedScreens.remove(openedScreens.size() - 1);
            openedScreens.remove(openedScreens.size() - 1);
        }

        Fragment fragment = getFragment(screen);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.fragment_container, fragment);
        fragmentTransaction.commit();
        openedScreens.add(screen);
        Log.d(">.<","openedScreens.size()-->"+openedScreens.size());
    }

    public boolean onBack() {
        if (openedScreens.size() > 0) {
            Screen screenToRemove = openedScreens.get(openedScreens.size() - 1);
            openedScreens.remove(openedScreens.size() - 1);
            if (openedScreens.size() == 0) {
                return true;
            }
            Screen screen = openedScreens.get(openedScreens.size() - 1);
            openedScreens.remove(openedScreens.size() - 1);
            openScreen(screen);
//            chuyển hình nền khi nhảy từ menu sang chọn chủ đề
            if ((screen== Screen.MENU) && (screenToRemove == Screen.GAME)) {
                Shared.eventBus.notify(new ChangeBackground());
            }
            return false;
        }
        return true;
    }

    private Fragment getFragment(Screen screen) {
        switch (screen) {
            case MENU:
                return new MenuFragment() ;
            case GAME:
                return new GameFragment();
            default:
                break;
        }
        return null;
    }
}


