package vn.devpro.devprokidorigin.activities.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.interfaces.BackListener;
import vn.devpro.devprokidorigin.interfaces.PuzzleGameListener;
import vn.devpro.devprokidorigin.interfaces.TouchListener;
import vn.devpro.devprokidorigin.models.ButtonClick;
import vn.devpro.devprokidorigin.models.Sound;
import vn.devpro.devprokidorigin.models.xephinh.GameModel;
import vn.devpro.devprokidorigin.models.xephinh.PuzzleData;
import vn.devpro.devprokidorigin.models.xephinh.PuzzlePiece;
import vn.devpro.devprokidorigin.utils.AdMob.AdsObserver;
import vn.devpro.devprokidorigin.utils.AdMob.InterstitialAds;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.utils.NetworkReceiver;
import vn.devpro.devprokidorigin.utils.Utils;

import static java.lang.Math.abs;

public class PuzzleActivity extends AppCompatActivity implements PuzzleGameListener, BackListener {

    ArrayList<PuzzlePiece> pieces;
    ImageButton btnBack, btnSound, btnRand, btnShow;
    ImageView mainImageView;
    RelativeLayout layout;
    Handler fadeoutHandler;
    private InterstitialAds interstitialAds;
    Sound mSound;
    GameModel mGameModel;
    int pieceNumbes = 9;
    int currentImage = 0;

    ProgressBar mProgressBar;
    TextView counterText;
    //View view1,view2,view3,view4,view5,view6,view7,view8;
    View[] views = new View[9];
    int[] viewID = {R.id.view1, R.id.view2, R.id.view3, R.id.view4, R.id.view5, R.id.view6, R.id.view7, R.id.view8, R.id.view8};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        //TODO: Dua load anh vao AsyncTask.
        new LoadImageAsyncTask().execute();
/*        initView();
        loadImage(mainImageView);
        mainImageView.post(createPieces);*/
        fadeoutHandler = new Handler();
        new PrepareSoundTask().execute();

        //===================================
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                interstitialAds = InterstitialAds.loadInterstitialAds(PuzzleActivity.this, PuzzleActivity.this);
            }
        }, 500);
    }

    private ImageView initView() {
        layout = findViewById(R.id.rlt_layout);
        for (int i = 0; i < views.length; i++) {
            views[i] = findViewById(viewID[i]);
        }
        mainImageView = findViewById(R.id.imageView);
        mainImageView.setAlpha(0f);
        btnBack = findViewById(R.id.btnBack);
        btnSound = findViewById(R.id.btnSound);
        btnRand = findViewById(R.id.btnRand);
        btnShow = findViewById(R.id.btnShow);
        mProgressBar = findViewById(R.id.progressBar);
        counterText = findViewById(R.id.textCounter);
        mProgressBar.setVisibility(View.GONE);
        counterText.setVisibility(View.GONE);
        btnRand.setOnClickListener(btnListener);
        btnBack.setOnClickListener(btnListener);
        btnShow.setOnClickListener(btnListener);
        //first update for sound
        Global.updateButtonSound(btnSound);
        //===================================
        //*****TODO process for btnSound ****
        //===================================
        ButtonClick.Sound soundListener = new ButtonClick.Sound();
        btnSound.setOnClickListener(soundListener);
        mSound = Sound.getInstance(Global.mContext);
        return mainImageView;
    }

    @Override
    public void toDoBackListener() {
        finish();
    }

    private class PrepareSoundTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... pVoids) {
            Boolean res = Sound.getInstance(Global.mContext).initPuzzleSound(1);
            return res;
        }

        @Override
        protected void onPostExecute(Boolean pBoolean) {
            super.onPostExecute(pBoolean);
            //Toast.makeText(getBaseContext(),"done prepare sound:"+pBoolean,Toast.LENGTH_SHORT).show();
        }
    }


    Runnable createPieces = new Runnable() {
        @Override
        public void run() {
            pieces = splitImage();
            TouchListener touchListener = new TouchListener(PuzzleActivity.this);
            touchListener.setGameModel(mGameModel);
            Collections.shuffle(pieces);
            int[] location = new int[2];
            int w = views[0].getWidth();
            int h = views[0].getHeight();
            int size = pieces.size();
            for (int i = 0; i < size; i++) {
                PuzzlePiece piece = pieces.get(i);
                views[i].getLocationOnScreen(location);
                piece.xStartCoord = location[0];
                piece.yStartCoord = location[1];
                piece.startWidth = w;
                piece.startHeight = h;
            }
            for (PuzzlePiece piece : pieces) {
                piece.setOnTouchListener(touchListener);
                //add piece into layout
                layout.addView(piece);
                //place piece at appropriate position
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) piece.getLayoutParams();
                if (piece.index == 1) { // special adjust for first piece
                    piece.startWidth = piece.startWidth - piece.startWidth / 5;
                    piece.startHeight = piece.startHeight - piece.startHeight / 5;
                    piece.xStartCoord = piece.xStartCoord + piece.startWidth / 8;
                    piece.yStartCoord = piece.yStartCoord + piece.startHeight / 8;
                }
                if (piece.index == 2 || piece.index == 3 || piece.index == 5 || piece.index == 6 || piece.index == 8 || piece.index == 9) {
                    piece.xStartCoord = piece.xStartCoord - piece.startWidth / 8;
                    //piece.yStartCoord = piece.yStartCoord + piece.startHeight/8;
                }
                lParams.topMargin = piece.yStartCoord;
                lParams.leftMargin = piece.xStartCoord;
                lParams.width = piece.startWidth;
                lParams.height = piece.startHeight;
                piece.setLayoutParams(lParams);
                piece.setVisibility(View.INVISIBLE);
            }
            animateCreatePieces();
        }
    };

    private void animateCreatePieces() {
        btnRand.setClickable(false);
        int DURATION = 250;
        ScaleAnimation[] animations = new ScaleAnimation[pieces.size()];
        for (int i = 0; i < pieces.size(); i++) {
            animations[i] = new ScaleAnimation(0f, 1f, 0f, 1f);
            animations[i].setDuration(DURATION);
            if (i < 4) {
                animations[i].setStartOffset(i * DURATION);
            } else {
                animations[i].setStartOffset((i - 4) * DURATION);
            }
            if (i == pieces.size() - 1) {
                animations[i].setAnimationListener(anmListener);
            }
            pieces.get(i).setVisibility(View.VISIBLE);
            pieces.get(i).startAnimation(animations[i]);
        }
    }

    Animation.AnimationListener anmListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation pAnimation) {

        }

        @Override
        public void onAnimationEnd(Animation pAnimation) {
            btnRand.setClickable(true);
            btnShow.setClickable(true);
            fadeoutHandler.postDelayed(fadeoutRunable, 100);
        }

        @Override
        public void onAnimationRepeat(Animation pAnimation) {

        }
    };

    Runnable fadeoutRunable = new Runnable() {
        @Override
        public void run() {
            Log.d("AADA", "fadeout running..");
            AlphaAnimation animation = new AlphaAnimation(0f, 1f);
            animation.setDuration(1000);
            AlphaAnimation animation1 = new AlphaAnimation(1f, 0f);
            animation1.setDuration(10000);
            animation1.setStartOffset(1000);
            AnimationSet set = new AnimationSet(false);
            set.addAnimation(animation);
            set.addAnimation(animation1);
            set.setFillAfter(true);
            mainImageView.setAlpha(1f);
            mainImageView.startAnimation(set);
        }
    };


    private int img_key = 1;

    //TODO: LoadImage AsyncTask.
    private class LoadImageAsyncTask extends AsyncTask<Void, Void, ImageView> {

        @Override
        protected ImageView doInBackground(Void... voids) {
            ImageView v = initView();
            return v;
        }

        @Override
        protected void onPostExecute(ImageView imageView) {
            loadImage(imageView);
            loadImage(mainImageView);
            imageView.post(createPieces);
        }
    }

    private void loadImage(ImageView pImageView) {
        Random r = new Random();

        do {
            img_key = r.nextInt(4);
            img_key++;
        } while (img_key == currentImage);

        String imgName = PuzzleData.imgnames.get(img_key);
        Log.d("IMG_KEY", "--->" + img_key);
        mGameModel = new GameModel(this.pieceNumbes, img_key);
        currentImage = img_key;
        //read bitmap from file
        Bitmap orgBitmap = Utils.getBitmapForGame(Global.pathImgGameXepHinh, imgName, ".png", false);
        if (orgBitmap != null) {
            //fill bgr color for bitmap
            orgBitmap = Utils.fillColorBitmap(orgBitmap, PuzzleData.bgrColors.get(imgName));
            pImageView.setImageBitmap(orgBitmap);
        }
    }

    View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View pView) {
            Utils.btn_click_animate(pView);
            if (pView.getId() == R.id.btnRand) {
                onGameRestart();
                if (myCountDownTimer != null) {
                    myCountDownTimer.cancel();
                    mProgressBar.setVisibility(View.GONE);
                    counterText.setVisibility(View.GONE);
                }
            } else if (pView.getId() == R.id.btnBack) {
                Sound.getInstance(Global.mContext).releasePuzzleSound();
                if (Global.isDemoApp()){finish();}
                if (NetworkReceiver.isConnected() && AdsObserver.shouldShow(System.currentTimeMillis())) {
                    interstitialAds.show();
                } else {
                    finish();
                }
            } else if (pView.getId() == R.id.btnShow) {
                Log.d("AADA", "---->show up");
                AlphaAnimation alp_UP = new AlphaAnimation(0, 0.5f);
                alp_UP.setDuration(1000);
                AlphaAnimation alp_Down = new AlphaAnimation(0.5f, 0);
                alp_Down.setDuration(10000);
                alp_Down.setStartOffset(1000);
                //mainImageView.setAlpha(1f);
                AnimationSet s = new AnimationSet(false);//false means don't share interpolators
                s.addAnimation(alp_UP);
                s.addAnimation(alp_Down);
                s.setFillAfter(true);
                mainImageView.startAnimation(s);
            }
        }
    };


    private ArrayList<PuzzlePiece> splitImage() {
        int piecesNumber = this.pieceNumbes;
        int rows = 3;
        int cols = 3;

        ImageView imageView = findViewById(R.id.imageView);
        ArrayList<PuzzlePiece> pieces = new ArrayList<>(piecesNumber);

        //get the bitmap of the source image
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        int[] dimentions = getBitmapPositionInsideImageView(imageView);
        int scaledBitmapLeft = dimentions[0];
        int scaledBitmapTop = dimentions[1];
        int scaledBitmapWidth = dimentions[2];
        int scaledBitmapHeight = dimentions[3];

        int croppedImgWidth = scaledBitmapWidth - 2 * abs(scaledBitmapLeft);
        int croppedImgHeight = scaledBitmapHeight - 2 * abs(scaledBitmapTop);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledBitmapWidth, scaledBitmapHeight, true);
        Bitmap croppedBitmap = Bitmap.createBitmap(scaledBitmap, abs(scaledBitmapLeft), abs(scaledBitmapTop), croppedImgWidth, croppedImgHeight);

        //calculate the width and height of the pieces
        int pieceWidth = croppedImgWidth / cols;
        int pieceHeight = croppedImgHeight / rows;

        //create bimap piece and add to the resulting array
        int yCoord = 0;
        int index = 1; // index of each piece
        for (int row = 0; row < rows; row++) {
            int xCoord = 0;
            for (int col = 0; col < cols; col++) {
                // calculate offset for each piece
                int offsetX = 0;
                int offsetY = 0;
                if (col > 0) {
                    offsetX = pieceWidth / 3;
                }
                if (row > 0) {
                    offsetY = pieceHeight / 3;
                }
                //apply the offset to each piece
                Bitmap pieceBitmap = Bitmap.createBitmap(croppedBitmap, xCoord - offsetX, yCoord - offsetY, pieceWidth + offsetX, pieceHeight + offsetY);
                PuzzlePiece piece = new PuzzlePiece(getApplicationContext());
                piece.setImageBitmap(pieceBitmap);
                piece.xCoord = xCoord - offsetX + imageView.getLeft();
                piece.yCoord = yCoord - offsetY + imageView.getTop();
                piece.pieceWidth = pieceWidth + offsetX;
                piece.pieceHeight = pieceHeight + offsetY;
                // bitmap to hold final puzzle piece image
                Bitmap puzzlePiece = Bitmap.createBitmap(pieceWidth + offsetX, pieceHeight + offsetY, Bitmap.Config.ARGB_8888);

                //draw path
                int bumpSize = pieceHeight / 4;
                Canvas canvas = new Canvas(puzzlePiece);
                Path path = new Path();
                path.moveTo(offsetX, offsetY);
                if (row == 0) {
                    //top side piece
                    path.lineTo(pieceBitmap.getWidth(), offsetY);
                } else {
                    //top bump
                    path.lineTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 3, offsetY);
                    path.cubicTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 6, offsetY - bumpSize,
                            offsetX + (pieceBitmap.getWidth() - offsetX) / 6 * 5, offsetY - bumpSize,
                            offsetX + (pieceBitmap.getWidth() - offsetX) / 3 * 2, offsetY);
                    path.lineTo(pieceBitmap.getWidth(), offsetY);
                }
                if (col == cols - 1) {
                    // right side piece
                    path.lineTo(pieceBitmap.getWidth(), pieceBitmap.getHeight());
                } else {
                    // right bump
                    path.lineTo(pieceBitmap.getWidth(), offsetY + (pieceBitmap.getHeight() - offsetY) / 3);
                    path.cubicTo(pieceBitmap.getWidth() - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6,
                            pieceBitmap.getWidth() - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6 * 5,
                            pieceBitmap.getWidth(), offsetY + (pieceBitmap.getHeight() - offsetY) / 3 * 2);
                    path.lineTo(pieceBitmap.getWidth(), pieceBitmap.getHeight());
                }
                if (row == rows - 1) {
                    //bottom side piece
                    path.lineTo(offsetX, pieceBitmap.getHeight());
                } else {
                    // bottom bump
                    path.lineTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 3 * 2, pieceBitmap.getHeight());
                    path.cubicTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 6 * 5, pieceBitmap.getHeight() - bumpSize,
                            offsetX + (pieceBitmap.getWidth() - offsetX) / 6, pieceBitmap.getHeight() - bumpSize,
                            offsetX + (pieceBitmap.getWidth() - offsetX) / 3, pieceBitmap.getHeight());
                    path.lineTo(offsetX, pieceBitmap.getHeight());
                }
                if (col == 0) {
                    //left side piece
                    path.close();
                } else {
                    //left bump
                    path.lineTo(offsetX, offsetY + (pieceBitmap.getHeight() - offsetY) / 3 * 2);
                    path.cubicTo(offsetX - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6 * 5,
                            offsetX - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6,
                            offsetX, offsetY + (pieceBitmap.getHeight() - offsetY) / 3);
                    path.close();
                }
                //TODO mask the path
                Paint paint = new Paint();
                paint.setColor(0XFF000000);
                paint.setStyle(Paint.Style.FILL);

                canvas.drawPath(path, paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(pieceBitmap, 0, 0, paint);

                //TODO draw border around puzzle piece
                //draw white border
                Paint border = new Paint();
                border.setColor(0X80FFFFFF);
                border.setStyle(Paint.Style.STROKE);
                border.setStrokeWidth(8.0f);
                canvas.drawPath(path, border);

                //draw black border
                border = new Paint();
                border.setColor(Color.BLACK);
                border.setStyle(Paint.Style.STROKE);
                border.setStrokeWidth(3.0f);
                canvas.drawPath(path, border);

                // set the resulting bitmap to the piece
                piece.setImageBitmap(puzzlePiece);

                //set index
                piece.index = index;

                pieces.add(piece);
                xCoord += pieceWidth;
                index++; // increase index for next turn
            }
            yCoord += pieceHeight;
        } // end for
        return pieces;
    }

    private int[] getBitmapPositionInsideImageView(ImageView pImageView) {
        int[] ret = new int[4];

        if (pImageView == null || pImageView.getDrawable() == null) return ret;

        //get image dimentions
        //get image matrix values and place them in an array
        float[] f = new float[9];
        pImageView.getImageMatrix().getValues(f);

        // Extract the scalee values using the constans (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        float scaleY = f[Matrix.MSCALE_Y];

        // get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = pImageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        //calculate the actual dimentions


        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        ret[2] = actW;
        ret[3] = actH;

        //get image position
        // assume the image is centered into ImageView
        int imgViewW = pImageView.getWidth();
        int imgViewH = pImageView.getHeight();

        int top = (int) ((imgViewH - actH) / 2f);
        int left = (int) ((imgViewW - actW) / 2f);
        ret[0] = left;
        ret[1] = top;
        return ret;
    }


    MyCountDownTimer myCountDownTimer = new MyCountDownTimer(10000, 10);

    @Override
    public void onGameFinish() {
        if (img_key % 2 == 0) {
            mSound.playPuzzleSound(Sound.PUZZLE_DONE);
        } else {
            mSound.playPuzzleSound(Sound.PUZZLE_DONE2);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(getBaseContext(), "You are finish the game!!", Toast.LENGTH_SHORT).show();
                layout.removeAllViews();
                AlphaAnimation animation1 = new AlphaAnimation(0.5f, 1.0f);
                animation1.setDuration(1000);
                animation1.setStartOffset(1000);
                animation1.setFillAfter(true);
                mainImageView.setAlpha(1f);
                mainImageView.startAnimation(animation1);
            }
        }, 500);
        btnShow.setClickable(false);
        //===================================
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.VISIBLE);
                counterText.setText("10");
                counterText.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(100);
                myCountDownTimer.start();
            }
        }, 3000);
    }

    @Override
    public void onCorrectMove() {
        mSound.playPuzzleSound(Sound.PIECE_CORRECT);
    }

    @Override
    public void onGameRestart() {
        //TODO: LoadImage
        loadImage(mainImageView);
        /*new LoadImageAsyncTask().execute();*/
        layout.removeAllViews();
        mainImageView.setAlpha(0f);
        mainImageView.post(createPieces);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Sound.getInstance(Global.mContext).releasePuzzleSound();
        if (Global.isDemoApp()){finish();}
        if (NetworkReceiver.isConnected() && AdsObserver.shouldShow(System.currentTimeMillis())) {
            interstitialAds.show();
        } else {
            finish();
        }
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (millisUntilFinished % 1000 < 100) {
                counterText.setText(String.valueOf(millisUntilFinished / 1000));
            }
            int progress = (int) (millisUntilFinished / 100);
            mProgressBar.setProgress(progress);
        }

        @Override
        public void onFinish() {
            counterText.setText("0");
            mProgressBar.setProgress(0);
            mProgressBar.setVisibility(View.GONE);
            counterText.setVisibility(View.GONE);
            Utils.btn_click_animate(btnRand);
            onGameRestart();
        }

    }

}
