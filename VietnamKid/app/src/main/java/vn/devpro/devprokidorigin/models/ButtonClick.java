package vn.devpro.devprokidorigin.models;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import vn.devpro.devprokidorigin.models.interfaces.ChangeLanguageCallback;
import vn.devpro.devprokidorigin.models.interfaces.LanguageListener;
import vn.devpro.devprokidorigin.models.interfaces.SoundListener;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.utils.Utils;

public class ButtonClick {
    public ButtonClick() {
    }

    public static class Sound implements SoundListener {
        public Sound() {
            super();
        }

        @Override
        public void onClick(View v) {
            Utils.btn_click_animate(v);
            Global.changeSoundState();
            try {
                if(v instanceof ImageButton){
                    Global.updateButtonSound((ImageButton) v);
                }else{
                    Global.updateButtonSound((Button) v);
                }
            } catch (ClassCastException e) {
                Log.e("SOUND","ClassCastException:"+e.getLocalizedMessage());
            }
        }
    }

    public static class Language implements LanguageListener{
        ChangeLanguageCallback callback;

        public Language(ChangeLanguageCallback callback) {
            super();
            this.callback = callback;
        }
        @Override
        public void onClick(View v) {
            Utils.btn_click_animate(v);
            Global.changeLanguage();
            try {
                if (v instanceof Button) {
                    Global.updateButtonLanguage((Button) v);
                } else {
                    Global.updateButtonLanguage((ImageButton) v);
                }
                callback.updateUIForLanguage();
            } catch (ClassCastException e) {
                Log.e("LANG","ClassCastException:"+e.getLocalizedMessage());
            }
        }
    }
}
