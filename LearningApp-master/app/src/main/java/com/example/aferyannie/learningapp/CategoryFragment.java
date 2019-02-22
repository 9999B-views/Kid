package com.example.aferyannie.learningapp;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Locale;
import java.util.Random;

/**
 * Do not forget to set!
 * COUNTDOWN_IN_MILLIS = 10000
 * (10 seconds).
 */

public class CategoryFragment extends Fragment {
    private static final long COUNTDOWN_IN_MILLIS = 10000;
    private static final String TAG_AUDIO = "AUDIO_LOG";
    private static final String TAG_TIMER = "TIMER_LOG";

    private TextView txtCategory;
//    private TextView txtPronounce;
    public static Button btnSound;
    public Button btnClear;
    private TextView txtTimer;
    private int RandomNumber;
    private TensorFlowInferenceInterface tf;

    private ColorStateList colorDefaultCountdown;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private PaintView paintView;

    MediaPlayer pronounce;
    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, null, false);

        paintView = view.findViewById(R.id.PaintView);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);

        if (MainActivity.main_menu.isPlaying()) {
            MainActivity.main_menu.pause();
            Log.d(TAG_AUDIO, "main_menu:onPause via Category Screen");
        }

        txtCategory = view.findViewById(R.id.txtCategory);
//        txtPronounce = view.findViewById(R.id.txtPronounce);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("Angka")) {
                KategoriAngka();
            } else if (bundle.containsKey("HurufKapital")) {
                KategoriHurufKapital();
            } else {
                KategoriHurufKecil();
            }
        }

        btnClear = view.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintView.clear();
            }
        });

        btnSound = view.findViewById(R.id.btnSound);
        Log.d(TAG_AUDIO, "pronounce:onEnable");
        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pronounce.isPlaying()) {
                    pronounce.stop();
                }
                SoundClick();
            }
        });
        txtTimer = view.findViewById(R.id.txtTimer);
        colorDefaultCountdown = txtTimer.getTextColors();

        timeLeftInMillis = COUNTDOWN_IN_MILLIS;
        startCountdown();
        return view;
    }

    public void SoundClick() {
        try {
            pronounce.start();
            Log.d(TAG_AUDIO, "pronounce:onPlay");
            pronounce.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    Log.d(TAG_AUDIO, "pronounce:onComplete");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG_AUDIO, "pronounce:onError");
        }
    }

    public void PronounceHurufHandler(String string) {
        txtCategory.setText(string); // setText for category text view.
//        txtPronounce.setVisibility(View.GONE); // make pronounce text view gone from layout.
        switch (string) {
            case "a":
            case "A":
                pronounce = MediaPlayer.create(getContext(), R.raw.a);
                break;
            case "b":
            case "B":
                pronounce = MediaPlayer.create(getContext(), R.raw.b);
                break;
            case "c":
            case "C":
                pronounce = MediaPlayer.create(getContext(), R.raw.c);
                break;
            case "d":
            case "D":
                pronounce = MediaPlayer.create(getContext(), R.raw.d);
                break;
            case "e":
            case "E":
                pronounce = MediaPlayer.create(getContext(), R.raw.e);
                break;
            case "f":
            case "F":
                pronounce = MediaPlayer.create(getContext(), R.raw.f);
                break;
            case "g":
            case "G":
                pronounce = MediaPlayer.create(getContext(), R.raw.g);
                break;
            case "h":
            case "H":
                pronounce = MediaPlayer.create(getContext(), R.raw.h);
                break;
            case "i":
            case "I":
                pronounce = MediaPlayer.create(getContext(), R.raw.i);
                break;
            case "j":
            case "J":
                pronounce = MediaPlayer.create(getContext(), R.raw.j);
                break;
            case "k":
            case "K":
                pronounce = MediaPlayer.create(getContext(), R.raw.k);
                break;
            case "l":
            case "L":
                pronounce = MediaPlayer.create(getContext(), R.raw.l);
                break;
            case "m":
            case "M":
                pronounce = MediaPlayer.create(getContext(), R.raw.m);
                break;
            case "n":
            case "N":
                pronounce = MediaPlayer.create(getContext(), R.raw.n);
                break;
            case "o":
            case "O":
                pronounce = MediaPlayer.create(getContext(), R.raw.o);
                break;
            case "p":
            case "P":
                pronounce = MediaPlayer.create(getContext(), R.raw.p);
                break;
            case "q":
            case "Q":
                pronounce = MediaPlayer.create(getContext(), R.raw.q);
                break;
            case "r":
            case "R":
                pronounce = MediaPlayer.create(getContext(), R.raw.r);
                break;
            case "s":
            case "S":
                pronounce = MediaPlayer.create(getContext(), R.raw.s);
                break;
            case "t":
            case "T":
                pronounce = MediaPlayer.create(getContext(), R.raw.t);
                break;
            case "u":
            case "U":
                pronounce = MediaPlayer.create(getContext(), R.raw.u);
                break;
            case "v":
            case "V":
                pronounce = MediaPlayer.create(getContext(), R.raw.v);
                break;
            case "w":
            case "W":
                pronounce = MediaPlayer.create(getContext(), R.raw.w);
                break;
            case "x":
            case "X":
                pronounce = MediaPlayer.create(getContext(), R.raw.x);
                break;
            case "y":
            case "Y":
                pronounce = MediaPlayer.create(getContext(), R.raw.y);
                break;
            case "z":
            case "Z":
                pronounce = MediaPlayer.create(getContext(), R.raw.z);
                break;
        }
    }

    public void KategoriAngka() {
        bundle = getArguments(); // get arguments from bundle in HomeFragment.
//        int num = bundle.getInt("Angka"); // get bundle with key "Angka".
        Random random = new Random();
        int num = random.nextInt(9);
        RandomNumber = num;
        tf = new TensorFlowInferenceInterface(getActivity().getAssets(), "DigitMNIST.pb"); // TODO: ini apa?
        txtCategory.setText(Integer.toString(num)); // setText for category text view.
        switch (num) {
            case 0:
//                txtPronounce.setText("Nol");
                // initialize audio resource.
                pronounce = MediaPlayer.create(getContext(), R.raw.nol);
                break;
            case 1:
//                txtPronounce.setText("Satu");
                pronounce = MediaPlayer.create(getContext(), R.raw.satu);
                break;
            case 2:
//                txtPronounce.setText("Dua");
                pronounce = MediaPlayer.create(getContext(), R.raw.dua);
                break;
            case 3:
//                txtPronounce.setText("Tiga");
                pronounce = MediaPlayer.create(getContext(), R.raw.tiga);
                break;
            case 4:
//                txtPronounce.setText("Empat");
                pronounce = MediaPlayer.create(getContext(), R.raw.empat);
                break;
            case 5:
//                txtPronounce.setText("Lima");
                pronounce = MediaPlayer.create(getContext(), R.raw.lima);
                break;
            case 6:
//                txtPronounce.setText("Enam");
                pronounce = MediaPlayer.create(getContext(), R.raw.enam);
                break;
            case 7:
//                txtPronounce.setText("Tujuh");
                pronounce = MediaPlayer.create(getContext(), R.raw.tujuh);
                break;
            case 8:
//                txtPronounce.setText("Delapan");
                pronounce = MediaPlayer.create(getContext(), R.raw.delapan);
                break;
            case 9:
//                txtPronounce.setText("Sembilan");
                pronounce = MediaPlayer.create(getContext(), R.raw.sembilan);
                break;
        }
    }

    public void KategoriHurufKecil() {
        bundle = getArguments();
        String[] charsLower = bundle.getStringArray("HurufKecil");
//        String charsDisplay = charsLower[(int) (Math.random() * 10)];
        Random random = new Random();
        RandomNumber = random.nextInt(25);
        Log.d("CEK", String.valueOf(RandomNumber));
        tf = new TensorFlowInferenceInterface(getActivity().getAssets(), "LowercaseModel.pb");
        String charsDisplay = charsLower[RandomNumber];
        PronounceHurufHandler(charsDisplay);
    }

    public void KategoriHurufKapital() {
        Bundle bundle = getArguments();
        String[] charsUpper = bundle.getStringArray("HurufKapital");
//        String charsDisplay = charsUpper[(int) (Math.random() * 10)];
        Random random = new Random();
        RandomNumber = random.nextInt(25);
        tf = new TensorFlowInferenceInterface(getActivity().getAssets(), "UpperCaseModel.pb");
        String charsDisplay = charsUpper[RandomNumber];
        PronounceHurufHandler(charsDisplay);
    }

    private void startCountdown() {
        Log.d(TAG_TIMER, "countDownTimer:onStart");
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdown();
            }

            @Override
            public void onFinish() {
                Bundle bundle = getArguments();
                int JumlahSekarang = bundle.getInt("jumlahTest");
                int[] intValues = new int[784];
                float[] floatValues = new float[784];

                timeLeftInMillis = 0;
                updateCountdown();
                countDownTimer.cancel();
                Log.d(TAG_TIMER, "countDownTimer:onFinish");
                if (pronounce != null) {
                    pronounce.stop();
                    Log.d(TAG_AUDIO, "pronounce:onDisable");
                }
//                showFragment(new ResultFragment(),R.id.fragment_container);
                paintView.setDrawingCacheEnabled(true);
                Bitmap result = Bitmap.createBitmap(paintView.getDrawingCache());
                paintView.setDrawingCacheEnabled(false);
                Bitmap resizedbmp = Bitmap.createScaledBitmap(result, 28, 28, true);
                result.recycle();
                result = null;
                Bitmap bm = Bitmap.createBitmap(resizedbmp.getWidth(), resizedbmp.getHeight(), Bitmap.Config.RGB_565);
                Canvas c = new Canvas(bm);
                Paint paint = new Paint();
                ColorMatrix cm = new ColorMatrix();
                cm.setSaturation(0);
                ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
                paint.setColorFilter(f);
                c.drawBitmap(resizedbmp, 0, 0, paint);
                resizedbmp.recycle();
                resizedbmp = null;
                Bitmap inverted = createInvertedBitmap(bm);
                bm.recycle();
                bm = null;
                inverted.getPixels(intValues, 0, inverted.getWidth(), 0, 0, inverted.getWidth(), inverted.getHeight());
                for (int i = 0; i < intValues.length; ++i) {
                    final int val = intValues[i];
                    floatValues[i] = (val & 0xFF) / 255.0f;
                }
                tf.feed("conv2d_1_input", floatValues, 1, 28, 28, 1);
                String[] s = new String[1];
                s[0] = "dense_2/Softmax";
                tf.run(s);
                float[] rslt = new float[62];
                tf.fetch("dense_2/Softmax", rslt);
                double highest = 0.1;
                int penanda = 0;
                for (int i = 0; i < rslt.length; i++) {
                    Log.d("Check Log", String.valueOf(rslt[i]));
                    if (rslt[i] > highest) {
                        highest = rslt[i];
                        penanda = i;
                    }
                }
//
                Log.d("Check Log", "-----------------------");
                Log.d("Check Log", "V : " + String.valueOf(rslt[21] * 100) + "%");
                Log.d("Check Log", String.valueOf(highest));
                Log.d("Check Log", String.valueOf(penanda));
                if (penanda == RandomNumber) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt("jumlahTest", JumlahSekarang);
                    bundle2.putDouble("result", highest);
                    if (bundle.containsKey("Angka")) {
                        bundle2.putInt("Angka", RandomNumber);
                    } else if (bundle.containsKey("HurufKapital")) {
                        String[] charsUpper = {
                                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
                                "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
                        bundle2.putStringArray("HurufKapital", charsUpper);
                    } else {
                        String[] charsLower = {
                                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
                                "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
                        bundle2.putStringArray("HurufKecil", charsLower);
                    }
                    ResultFragment RsltFragment = new ResultFragment();
                    RsltFragment.setArguments(bundle2);
                    showFragment(RsltFragment, R.id.fragment_container);
                } else {
                    bundle = getArguments();
                    if (bundle != null) {
                        if (bundle.containsKey("Angka")) {
                            KategoriAngka();
                        } else if (bundle.containsKey("HurufKapital")) {
                            KategoriHurufKapital();
                        } else {
                            KategoriHurufKecil();
                        }
                        paintView.clear();
                        timeLeftInMillis = COUNTDOWN_IN_MILLIS;
                        startCountdown();
                    }
                }
//
            }
        }.start();
    }

    private void updateCountdown() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        txtTimer.setText(timeFormatted);

        if (timeLeftInMillis < 6000) {
            txtTimer.setTextColor(Color.RED);
            if (timeLeftInMillis < 2000) {
                FancyToast.makeText(getContext(), "Waktu Habis", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
            }
        } else {
            txtTimer.setTextColor(colorDefaultCountdown);
        }
    }

    private Bitmap createInvertedBitmap(Bitmap src) {
        ColorMatrix colorMatrix_Inverted = new ColorMatrix(new float[]{-1, 0, 0, 0, 255, 0, -1, 0, 0, 255, 0, 0, -1, 0, 255, 0, 0, 0, 1, 0});

        ColorFilter ColorFilter_Sepia = new ColorMatrixColorFilter(colorMatrix_Inverted);

        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();

        paint.setColorFilter(ColorFilter_Sepia);
        canvas.drawBitmap(src, 0, 0, paint);

        return bitmap;
    }

    public void showFragment(Fragment fragment, int fragmentResourceID) {
        if (fragment != null) {
            FragmentManager fragmentManager = this.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(fragmentResourceID, fragment);
            fragmentTransaction.detach(fragment);
            fragmentTransaction.attach(fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.main_menu.start();
        Log.d(TAG_AUDIO, "main_menu:onResume via Category Screen");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

}
