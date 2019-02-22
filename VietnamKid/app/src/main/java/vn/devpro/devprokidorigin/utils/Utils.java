package vn.devpro.devprokidorigin.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.databases.DBHelper;
import vn.devpro.devprokidorigin.models.Bounce;

/**
 * Created by Laptop88 on 3/27/2018.
 */

public class Utils {
    public static final String TAG = Utils.class.getSimpleName();
    public static final String PNG = ".png";
    public static final int A = AnimatorUtils.C + AnimatorUtils.D;
    private static Animation animate = AnimationUtils.loadAnimation(Global.mContext, R.anim.effect_clicked);
    private static  Bounce interpolator = new Bounce(0.1, 10);

    public static void btn_click_animate(View view) {
        if (view == null || animate == null) {
            return;
        }
        view.startAnimation(animate);
    }


    @Nullable
    public static byte[] getByteArrayFromSD(File path, String fileName, String suffix) {
        // array co the bi null
        byte[] array;
        array = readImageFile(path, fileName, suffix);
        return array;
    }


    public static Bitmap displayImageForHocTapActivity(byte[] arr, int width, int height, boolean isHocTapAct) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        int scale = Utils.calculateInSampleSize(options, width, height);
        if (isHocTapAct) {
            options.inSampleSize = Utils.calculateInSampleSize(options, width, height);
        } else if (scale < 3) {
            options.inSampleSize = 3;
        }
        options.inJustDecodeBounds = false;
        if (arr == null) return null;
        Bitmap data = BitmapFactory.decodeByteArray(arr, 0, arr.length, options);
        return data;
    }


    /**
     * @param fileName
     * @param sufix
     * @param needDecrypt
     * @return data the bitmap
     * @author Thanh Dat
     */
    @Nullable
    public static Bitmap getBitmapForGame(File path, String fileName, String sufix, boolean needDecrypt) {
        byte[] arr;
        if (needDecrypt) {
            arr = decodeFileImage(getByteArrayFromSD(path, fileName, sufix));
        } else {
            arr = getByteArrayFromSD(path, fileName, sufix);
        }
        if (arr == null) return null;
        Bitmap data = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        return data;
    }

    public static Bitmap displayImageForItem(byte[] arr) {
        if (arr == null) return null;
        Bitmap data = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        return data;
    }


    private static boolean checkSound(String name) {
        if (name.isEmpty() || name == null) {
            return false;
        }
        try {
            if (Global.pathSounds == null) return false;
            File file = new File(Global.pathSounds.getCanonicalPath() + File.separator + name + ".mp3");
            if (file == null || !file.exists()) {
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String TAG_E = "ERROR";
    private static String TAG_G = "Global";
    private static String TAG_R = "READ_FILE:";

    @Nullable
    private static byte[] getAudioFileFromSdCard(String fileName) {
        if (!checkSound(fileName)) { // Neu false
            Log.e(TAG_E, "Khong co file audio--->" + fileName);
            return null;
        }
        File file = null;
        try {
            file = new File(Global.pathSounds.getCanonicalPath() + File.separator + fileName + ".mp3");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!file.exists() || file == null) {
            Log.e(TAG_G, "read audio :-->File not found  return null (byte[])");
            return null;
        }
        byte[] inarry = null;
        try {
            File file2 = new File(Global.pathSounds, fileName + ".mp3"); //Creating file object
            FileInputStream fileInputStream = null;
            if (!file2.exists() || file2 == null){
                return null;
            }
            byte[] bFile = new byte[(int) file2.length()];
            fileInputStream = new FileInputStream(file2);
            fileInputStream.read(bFile);
            fileInputStream.close();
            inarry = bFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inarry;
    }

    @Nullable
    private static byte[] decodeFile(byte[] encryptedFile, int i) {
        if (encryptedFile == null) {
            Log.e(TAG_G, "decodeFile:------>encryptedFile null");
            return null;
        }
        byte[] bFile = encryptedFile;
        int j = bFile.length;
        int n = 0;
        if (n < j) {
            for (int a = 0; a < j; a++) {
                bFile[a] = (byte) (bFile[a] ^ i);
            }
        }
        return bFile;
    }

    public static void clearMp3FileCache(Context context) {
        if (context == null) return;
        File dir = context.getCacheDir();
        if (dir != null && dir.isDirectory()) {
            File[] files = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    return s.toLowerCase().endsWith(".mp3");
                }
            });
            if (files == null) {
            } else {
                File temp;
                for (int i = 0; i < files.length; i++) {
                    temp = files[i];
                    temp.delete();
                    Log.i("Show", "Trong folder cache co chua file mp3 la" + files[i]);
                }
            }
        }
    }


    public static byte[] decodeFileImage(byte[] array) {
        if (array == null) {
            Log.e(TAG_R, "input byte: null");
            return null;
        }
        int j = array.length;
        int n = 0;
        if (n < j) {
            for (int a = 0; a < j; a++) {
                array[a] = (byte) (array[a] ^ (Global.KEY));
            }
        }
        return array;
    }

    @Nullable
    private static byte[] readImageFile(File path, String fileName, String suffix) {
        if (suffix == null) {
            suffix = ".jpg";
        }
        byte[] inarry = null;
        File file = null;
        try {
            file = new File(path.getCanonicalFile() + File.separator + fileName + suffix);
            if (!file.exists()) {
                Log.e(TAG, "---->File not found  return null (byte[])");
                return null;
            }

            FileInputStream fileInputStream = null;
            inarry = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(inarry);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return inarry;
    }


    /**
     * TODO convert dp to pixel by device pixel density
     *
     * @param paramFloat
     * @param paramContext
     * @return dimension measured in device's pixels
     * @author Thanh Dat
     */
    public static int convertDpToPixel(float paramFloat, Context paramContext) {
        if (paramContext == null) {
            Log.e("ERR", "convertDpToPixel:context is null, return 0");
            return 0;
        }

        return Math.round(paramFloat * (paramContext.getResources().getDisplayMetrics().density));
    }

    /**
     * TODO convert Dp to Px with given display metric
     *
     * @param paramFloat      - Dp to convert
     * @param pDisplayMetrics - given display metric
     * @return - int pixel converted
     * @author Thanh Dat
     */
    public static int convertDpToPixel(float paramFloat, @NonNull DisplayMetrics pDisplayMetrics) {
        return Math.round(paramFloat * pDisplayMetrics.density);
    }


    /**
     * TODO convert from px to dp unit
     *
     * @param px              - pixel to convert
     * @param pDisplayMetrics - given display metric
     * @return dp converted
     * @author Thanh Dat
     * @author ThanhDat
     */
    public static int convertPxToDp(float px, @NonNull DisplayMetrics pDisplayMetrics) {
        return Math.round(px / pDisplayMetrics.density);
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


    public static void didTapButtom(View v) {
        if (v == null) {
            return;
        }
        //ImageView imageView = (ImageView) v;
        final Animation myAnim = AnimationUtils.loadAnimation(Global.mContext, R.anim.bounce);
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        Bounce interpolator = new Bounce(0.2, 15);
        myAnim.setInterpolator(interpolator);
        v.startAnimation(myAnim);
    }

    public static void didTapButtom(View v, int resIdAnimation) {
        //ImageView imageView = (ImageView) v;
        final Animation myAnim = AnimationUtils.loadAnimation(v.getContext(), resIdAnimation);
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        Bounce interpolator = new Bounce(0.1, 10);
        myAnim.setInterpolator(interpolator);
        v.startAnimation(myAnim);
    }


    public static byte[] decodeFileMp3(String fileMp3) {
        if (fileMp3.isEmpty() || fileMp3 == null){
            return null;
        }
        // decryptMp3File - Co the null.
        byte[] decryptMp3File = decodeFile(getAudioFileFromSdCard(fileMp3), Global.KEY);
        return decryptMp3File;
    }


    /**
     * fill bgr color for transparent bitmap
     *
     * @param pBitmap - the input bitmap need o fill bgr
     * @param pColor  - the bgr color
     * @return the bitmap has filled with bgr color
     * @author Thanh Dat
     */
    public static Bitmap fillColorBitmap(Bitmap pBitmap, int pColor) {
        // Create new bitmap based on the size and config of the old
        Bitmap newBitmap = Bitmap.createBitmap(pBitmap.getWidth(), pBitmap.getHeight(), pBitmap.getConfig());
        // Instantiate a canvas and prepare it to paint to the new bitmap
        Canvas canvas = new Canvas(newBitmap);
        // Paint it white (or whatever color you want)
        canvas.drawColor(pColor);
        // Draw the old bitmap ontop of the new white one
        canvas.drawBitmap(pBitmap, 0, 0, null);
        return newBitmap;
    }

    public static void showToast(Context context, String noiDung) {
        Toast.makeText(context, noiDung, Toast.LENGTH_SHORT).show();
    }

    public static void fixNullDatabase(Context context) {
        boolean checkDb = DBHelper.getInstance(context).checkExistDataBase();
        if (!checkDb) {
            boolean copyDemoFile = FileProcessor.copyFileFromAsset(context, "devkid.db", Global.pathSysDatabase.getPath());
            Log.i("TAG", "copyDemoFile" + copyDemoFile);
            if (!copyDemoFile) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder
                        .setMessage("Xin lỗi App đã xảy ra lỗi trong quá trình cài đặt. Vui lòng cài lại Ứng dụng!")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                System.exit(0);
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
    }
}

