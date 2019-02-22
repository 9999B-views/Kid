package vn.devpro.devprokidorigin.activities.game.latbai.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.activities.game.latbai.commom.Music;
import vn.devpro.devprokidorigin.activities.game.latbai.commom.Shared;

/**
 * Created by admin on 4/2/2018.
 */


public class setting extends LinearLayout {
    private ImageView mSoundImage;
    private TextView mSoundText;

    public setting(Context context) {
        this( context, null );
    }

    public setting(Context context, @Nullable AttributeSet attrs) {
        super( context, attrs );
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundResource( R.drawable.settings_popup);
        LayoutInflater.from(getContext()).inflate(R.layout.setting, this, true);
        mSoundText = (TextView) findViewById(R.id.sound_off_text);
        TextView rateView = (TextView) findViewById(R.id.rate_text);
        mSoundImage = (ImageView) findViewById(R.id.sound_image);
        View soundOff = findViewById(R.id.sound_off);
        soundOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Music.OFF = !Music.OFF;
                setMusicButton();
            }
        });
        View rate = findViewById(R.id.rate);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = Shared.context.getPackageName();
                try {
                    Shared.activity.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    Shared.activity.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
        setMusicButton();
    }

    private void setMusicButton() {
        if (Music.OFF) {
            mSoundText.setText("Sound OFF");
            mSoundImage.setImageResource(R.drawable.soundoff);
        } else {
            mSoundText.setText("Sound ON");
            mSoundImage.setImageResource(R.drawable.soundon);
        }
    }
}
