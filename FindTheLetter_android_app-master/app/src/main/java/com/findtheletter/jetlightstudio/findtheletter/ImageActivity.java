package com.findtheletter.jetlightstudio.findtheletter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ImageActivity extends AppCompatActivity {
    ImageView image;
    TextView scoreText;
    TextView hintText;
    EditText textField;
    ProgressBar progressBar;
    Button helpButton,friends;
    String words[] = {"fox", "castle", "car", "elephant", "house", "giraffe", "planet", "boat", "eagle", "africa", "radio", "alarm", "camera", "pizza", "money", "sheep", "rain", "penguin", "television", "tree", "fire"};
    int imageIndex = 0;
    int score = 0;
    int wordIndex = 0;
    int soloIndex = 0;
    String a="Can you help me to solve this puzzle? Take a look at the picture. The game located here - linkps";
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_image);


        image = (ImageView) findViewById(R.id.myImage);
        scoreText = (TextView) findViewById(R.id.scoreText);

// Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, "ca-app-pub-3644088453072578~1347065445");
        mAdView = findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);


        textField = (EditText) findViewById(R.id.textField);
        textField.setImeOptions(EditorInfo.IME_ACTION_DONE);
        textField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    checkImage(findViewById(android.R.id.content));
                    return true;
                }
                return false;
            }
        });

        friends=(Button)findViewById(R.id.friends);
        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bm = ((android.graphics.drawable.BitmapDrawable)image.getDrawable()).getBitmap();
                try {
                    java.io.File file = new java.io.File(getExternalCacheDir() + "/image.jpg");
                    java.io.OutputStream out = new java.io.FileOutputStream(file);
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                  //  showMessage(e.toString());
                }

                Intent iten = new Intent(android.content.Intent.ACTION_SEND);
                iten.setType("*/*");
                iten.putExtra(android.content.Intent.EXTRA_SUBJECT, a);
                iten.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new java.io.File(getExternalCacheDir() + "/image.jpg")));
                startActivity(Intent.createChooser(iten, "Send image"));
            }
        });


        helpButton = (Button) findViewById(R.id.help);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // AlertDialog.Builder b = new AlertDialog.Builder(ImageActivity.this);
                final android.app.AlertDialog b = new android.app.AlertDialog.Builder(ImageActivity.this).create();
                View v = getLayoutInflater().inflate(R.layout.activity_help, null);
                b.setView(v);
                //assigning functions of buttons and textFields
                ImageButton exitButton = v.findViewById(R.id.exitButton);
                exitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    b.dismiss();
                    }
                });
                Button wordButton = v.findViewById(R.id.getWordButton);
                wordButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (score > 25) {
                            score -= 25;
                            hintText.setText("try using the word: " + words[imageIndex]);
                            scoreText.setText("Score: " + String.format("%02d", score));
                        } else {
                            Toast.makeText(getApplicationContext(), "Not enought points!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Button letterButton = v.findViewById(R.id.getLetterButton);
                letterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (score > 10) {
                            score -= 10;
                            hintText.setText("this word starts with: " + words[imageIndex].charAt(0));
                            scoreText.setText("Score: " + String.format("%02d", score));
                        } else {
                            Toast.makeText(getApplicationContext(), "Not enought points!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                hintText = v.findViewById(R.id.hintText);
                TextView startsText = v.findViewById(R.id.starsText);
                startsText.setText("You have " + score + " points");
                //end of assigning
                b.setView(v);
              //  AlertDialog a = b.create();
                b.show();
            }
        });

        textField.setImeOptions(EditorInfo.IME_ACTION_DONE);
        textField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    checkImage(findViewById(android.R.id.content));
                    return true;
                }
                return false;
            }
        });


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        setImage();
    }

    public void setImage() {
        Random rand = new Random();


        int i = this.getResources().getIdentifier(words[imageIndex], "mipmap", this.getPackageName());
        Toast.makeText(this, " "+i, Toast.LENGTH_SHORT).show();
        image.setImageResource(i);
    }

    public void checkImage(View v) {


        final android.app.AlertDialog b = new android.app.AlertDialog.Builder(ImageActivity.this).create();
        View v1 = getLayoutInflater().inflate(R.layout.dialogcorrect, null);
        b.setView(v1);
        //assigning functions of buttons and textFields

        TextView starttext = v1.findViewById(R.id.starsText);
        TextView score1 = v1.findViewById(R.id.score);
        Button wordButton = v1.findViewById(R.id.getWordButton);
        ImageView imageicon = v1.findViewById(R.id.imageicon);

        //making the keyboard disappear
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        String msg;
        String msg1;
        if (textField.getText().toString().toLowerCase().equals(words[imageIndex])) {
            starttext.setText("Your answer is correct!");
            msg1 = "Continue!!";
            wordButton.setText(msg1);
            score += 15;
            float progress = (float) imageIndex / words.length;
            progressBar.setProgress((int) (progress * 100));
            scoreText.setText("Score: " + String.format("%02d", score));
            imageIndex++;
            setImage();
        } else  {
            imageicon.setImageResource(R.mipmap.falseicon);
            score1.setVisibility(View.INVISIBLE);
            starttext.setText("Your answer is wrong!");
            msg = "try again...";
            wordButton.setText(msg);

        }


        wordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               b.dismiss();
            }
        });



        //end of assigning
        b.setView(v1);
        //  AlertDialog a = b.create();
        b.show();

        textField.setText("");
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class).putExtra("score", score);
        i = pushItems(i, score, wordIndex, imageIndex, soloIndex);
        startActivity(i);
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getExtras() != null) {
            loadItems();
        } else System.out.println("no");

        scoreText.setText("Score: " + String.format("%02d", score));
    }

    protected Intent pushItems(Intent i, int score, int wordIndex, int imageIndex, int soloIndex) {
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
        System.out.println(score + " " + wordIndex + " " + imageIndex);
        setImage();
    }


}
