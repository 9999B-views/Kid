package vn.devpro.devprokidorigin.activities.game.dapbongbay.game;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.GameBaseFragment;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.sound.SoundManager;
import vn.devpro.devprokidorigin.models.ButtonClick;
import vn.devpro.devprokidorigin.models.interfaces.ChangeLanguageCallback;
import vn.devpro.devprokidorigin.utils.Global;

public class MainMenuFragment extends GameBaseFragment implements View.OnClickListener,ChangeLanguageCallback {

    String TAG = MainMenuFragment.class.getSimpleName();
    public MainMenuFragment() {

    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutResId(), container, false);
        return rootView;
    }

    protected int getLayoutResId() {
        return R.layout.fragment_main_menu;
    }


    Button btnLang;
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_start).setOnClickListener(this);
        view.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getActivity().finish();
            }
        });
        ButtonClick.Language listener = new ButtonClick.Language(this);
        btnLang = view.findViewById(R.id.btn_lang_fsgame);
        Global.updateButtonLanguage(btnLang);
        btnLang.setOnClickListener(listener);
    }

    @Override
    protected void onLayoutCompleted() {
        animateTitles();
    }

    private void animateTitles() {
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_start){
           getBalloonsActivity().startGame();
        }
    }

    private BalloonGameActivity getBalloonsActivity() {
        return (BalloonGameActivity) getActivity();
    }

    @Override
    public boolean onBackPressed() {
        boolean consumed = super.onBackPressed();
        if (!consumed){
            getActivity().finish();
        }
        return consumed;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart -->" + TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume -->" + TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause -->" + TAG);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop -->" + TAG);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView -->" + TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy -->" + TAG);
    }

    @Override
    public void updateUIForLanguage() {

    }
}
