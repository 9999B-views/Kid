package com.example.usuario.analisis2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail);
        Tales tales = (Tales) getIntent().getSerializableExtra("team_detail");

        ImageView mImageView = findViewById(R.id.tale_image);

        Glide.with(this).load(tales.getLogo()).into(mImageView);
        TextView mTextView = findViewById(R.id.tale_synopsis);

        mTextView.setText(tales.getSynopsis());

    }
}