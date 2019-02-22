package com.alpay.wesapiens.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alpay.wesapiens.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;

public class Utils {

    private static MediaPlayer mp;
    private static boolean mp_active = true;
    public static final int PICK_PHOTO = 1;

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }


    public static void playSoundInLoop(AppCompatActivity appCompatActivity, int soundID){
        if(mp_active){
            mp = MediaPlayer.create(appCompatActivity, soundID);
            mp.setLooping(true);
            mp.start();
        }
    }

    public static void playSoundOnce(AppCompatActivity appCompatActivity, int soundID){
        if(mp_active){
            MediaPlayer mediaPlayer = MediaPlayer.create(appCompatActivity, soundID);
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        }
    }

    public static void stopMediaPlayer(){
        mp.stop();
        mp.release();
    }

    public static void muteMedia(){
        mp_active = false;
        mp.setVolume(0.0f,0.0f);
    }

    public static void openSoundMedia(){
        mp_active = true;
        mp.setVolume(1.0f,1.0f);
    }

    public static String[] splitParagraphToSentences(String text){
        String[] words = text.split("\\.");
        return words;
    }

    public static void showOKDialog(AppCompatActivity activity, int stringID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(stringID)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showErrorToast(AppCompatActivity activityCompat, int stringID, int duration) {
        LayoutInflater inflater = activityCompat.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_error, (ViewGroup) activityCompat.findViewById(R.id.error_toast_container));
        TextView text = (TextView) layout.findViewById(R.id.error_toast_text);
        text.setText(activityCompat.getResources().getString(stringID));
        Toast toast = new Toast(activityCompat.getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }

    public static void showWarningToast(AppCompatActivity activityCompat, int stringID, int duration) {
        LayoutInflater inflater = activityCompat.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_warning, (ViewGroup) activityCompat.findViewById(R.id.warning_toast_container));
        TextView text = (TextView) layout.findViewById(R.id.warning_toast_text);
        text.setText(activityCompat.getResources().getString(stringID));
        Toast toast = new Toast(activityCompat.getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }

    public static void showWarningToast(AppCompatActivity activityCompat, String text, int duration) {
        LayoutInflater inflater = activityCompat.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_warning, (ViewGroup) activityCompat.findViewById(R.id.warning_toast_container));
        TextView textView = (TextView) layout.findViewById(R.id.warning_toast_text);
        textView.setText(text);
        Toast toast = new Toast(activityCompat.getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }

    public static Drawable getDrawableWithName(Context context, String fileName) {
        Drawable drawable;
        try {
            String[] assetFileNames = context.getAssets().list("frame");
            if (Arrays.asList(assetFileNames).contains(fileName)) {
                InputStream inputStream = context.getAssets().open("frame/" + fileName);
                drawable = Drawable.createFromStream(inputStream, null);
                inputStream.close();
            } else {
                String directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                if (!directoryPath.isEmpty()) {
                    directoryPath += "/wesapiens/drawable";
                } else {
                    directoryPath = "storage/self/primary/wesapiens/drawable";
                }
                File file = new File(directoryPath + "/" + fileName);
                drawable = Drawable.createFromPath(file.getAbsolutePath());
            }
            return drawable;
        } catch (IOException ex) {
            return null;
        }
    }

    public static String pickImage(AppCompatActivity appCompatActivity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        appCompatActivity.startActivityForResult(intent, PICK_PHOTO);
        return "";
    }

    public static void saveImageDrawable(Drawable drawable){

    }

    public static Drawable drawableFromInputStream(InputStream inputStream){
        return Drawable.createFromStream(inputStream, null);
    }

}
