package com.alpay.codenotes.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alpay.codenotes.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;

public class Utils {

    public static String SP_START_KEY_IS_PRESSED = "startKeyIsPressed";

    public static void printToastShort(AppCompatActivity appCompatActivity, int resID){
        Toast.makeText(appCompatActivity, appCompatActivity.getResources().getString(resID), Toast.LENGTH_SHORT).show();
    }

    public static void printToastLong(AppCompatActivity appCompatActivity, int resID){
        Toast.makeText(appCompatActivity, appCompatActivity.getResources().getString(resID), Toast.LENGTH_LONG).show();
    }

    public static void printToastShort(AppCompatActivity appCompatActivity, String string){
        Toast.makeText(appCompatActivity, string, Toast.LENGTH_SHORT).show();
    }

    public static void printToastLong(AppCompatActivity appCompatActivity, String string){
        Toast.makeText(appCompatActivity, string, Toast.LENGTH_LONG).show();
    }

    public static boolean isConnected() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return (Runtime.getRuntime().exec(command).waitFor() == 0);
    }

    public static boolean isOnline(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static void showOKDialog(Activity activity, int stringID) {
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

    public static void showOKDialog(final Activity activity, int stringID, final Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(stringID)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(intent != null)
                        {
                            activity.startActivity(intent);
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static boolean isCameraAvailable(AppCompatActivity appCompatActivity) {
        PackageManager pm = appCompatActivity.getPackageManager();
        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        }
        return false;
    }

    public static void addStringToSharedPreferences(AppCompatActivity appCompatActivity, String key, String value) {
        SharedPreferences settings = appCompatActivity.getSharedPreferences("cndata", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void addIntegerToSharedPreferences(AppCompatActivity appCompatActivity, String key, int value) {
        SharedPreferences settings = appCompatActivity.getSharedPreferences("cndata", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void addBooleanToSharedPreferences(AppCompatActivity appCompatActivity, String key, boolean value) {
        SharedPreferences settings = appCompatActivity.getSharedPreferences("cndata", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static String getStringFromSharedPreferences(AppCompatActivity appCompatActivity, String key) {
        SharedPreferences settings = appCompatActivity.getSharedPreferences("cndata", 0);
        return settings.getString(key, "").toString();
    }

    public static int getIntegerFromSharedPreferences(AppCompatActivity appCompatActivity, String key) {
        SharedPreferences settings = appCompatActivity.getSharedPreferences("cndata", 0);
        return settings.getInt(key, 0);
    }

    public static boolean getBooleanFromSharedPreferences(AppCompatActivity appCompatActivity, String key) {
        SharedPreferences settings = appCompatActivity.getSharedPreferences("cndata", 0);
        return settings.getBoolean(key, false);
    }

    public static int convertToDip(Context ctx, float px) {
        return (int) (px * (ctx.getResources().getDisplayMetrics().density + 0.5f));
    }

    public static int convertToPx(Context ctx, float dp) {
        Resources r = ctx.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static int convertPxFromSp(Context ctx, float sp) {
        Resources r = ctx.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, r.getDisplayMetrics());
    }

    public static String getStringFromResource(Context context, int resourceId) {
        return context.getResources().getString(resourceId);
    }

    public static boolean isInternetAvailable(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public static String readInputStreamAsString(InputStream in)
            throws IOException {

        BufferedInputStream bis = new BufferedInputStream(in);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while (result != -1) {
            byte b = (byte) result;
            buf.write(b);
            result = bis.read();
        }
        return buf.toString();
    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
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

    public Drawable encodeImageDrawableFromBase64(Context context, String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
        return drawable;
    }
}
