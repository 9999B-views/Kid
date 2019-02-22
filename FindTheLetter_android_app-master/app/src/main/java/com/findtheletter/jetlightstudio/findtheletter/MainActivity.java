package com.findtheletter.jetlightstudio.findtheletter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    Button wordButton;
    Button imageButton;
    Button soloButton;
    TextView scoreText;
    SQLiteManagerMine help;
    int score;
    int imageIndex;
    int wordIndex;
    int soloIndex;
    SharedPreferences sharedpreferences;
    static String savescore="savescore";
    static String saveword="savescore";
    static String saveimage="savescore";
    static  String savesolo="savescore";
    private String sharedPrefFile =
            "com.example.android.hellosharedprefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        help = new SQLiteManagerMine(this, null);

        if(Build.VERSION.SDK_INT>22){
            requestPermissions(new String[] {READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE}, 1);
        }

        imageButton = (Button) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ImageActivity.class);
                i = pushItems(i, score, wordIndex, imageIndex, soloIndex);
                startActivity(i);
            }
        });

        scoreText = (TextView) findViewById(R.id.scoreText);


//       sharedpreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
//        score= sharedpreferences.getInt(savescore, 0);
//        wordIndex= sharedpreferences.getInt(saveword, 0);
//        imageIndex= sharedpreferences.getInt(saveimage, 0);
//        soloIndex= sharedpreferences.getInt(savesolo, 0);
        scoreText.setText("Score: " + String.format("%02d", score));
    }


//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        SharedPreferences.Editor preferencesEditor = sharedpreferences.edit();
//        preferencesEditor.putInt(savescore,score);
////        preferencesEditor.putInt(saveword,wordIndex);
////        preferencesEditor.putInt(saveimage,imageIndex);
////        preferencesEditor.putInt(savesolo, soloIndex);
//        preferencesEditor.apply();
//
//
//    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getExtras() != null) {
            loadItems();
        }
        scoreText.setText("Score: " + String.format("%02d", score));

    }

    protected Intent pushItems(Intent i, int score, int wordIndex, int imageIndex, int soloIndex) {
        //help.updateData(score, wordIndex, imageIndex, soloIndex);
        i.putExtra("score", score);
        i.putExtra("wordIndex", wordIndex);
        i.putExtra("imageIndex", imageIndex);
        i.putExtra("soloIndex", soloIndex);
        return i;
    }

    protected void loadItems() {
        score = getIntent().getExtras().getInt("score");
        wordIndex = getIntent().getExtras().getInt("wordIndex");
        imageIndex = getIntent().getExtras().getInt("imageIndex");
        soloIndex = getIntent().getExtras().getInt("soloIndex");



    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(MainActivity.this, "Permission denied to access your location.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
