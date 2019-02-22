package vn.devpro.devprokidorigin.activities.game.latbai.uitls;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.util.SparseArray;
import android.widget.TextView;


public class    Clock {
    private static PauseTimer mPauseTimer = null;
    private static Clock mInstance = null;

    private Clock() {
        Log.i("my_tag", "NEW INSTANCE OF CLOCK");
    }

    public static class PauseTimer extends CountDown {
        private OnTimerCount mOnTimerCount = null;

        public PauseTimer(long millisOnTimer, long countDownInterval, boolean runAtStart, OnTimerCount onTimerCount) {
            super(millisOnTimer, countDownInterval, runAtStart);
            mOnTimerCount = onTimerCount;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (mOnTimerCount != null) {
                mOnTimerCount.onTick(millisUntilFinished);
            }
        }

        @Override
        public void onFinish() {
            if (mOnTimerCount != null) {
                mOnTimerCount.onFinish();
            }
        }

    }

    public static Clock getInstance() {
        if (mInstance == null) {
            mInstance = new Clock();
        }
        return mInstance;
    }

    /**
     * Start timer
     *
     * @param millisOnTimer
     * @param countDownInterval
     */
    public void startTimer(long millisOnTimer, long countDownInterval, OnTimerCount onTimerCount) {
        if (mPauseTimer != null) {
            mPauseTimer.cancel();
        }
        mPauseTimer = new PauseTimer(millisOnTimer, countDownInterval, true, onTimerCount);
        mPauseTimer.create();
    }

    /**
     * Pause
     */
    public void pause() {
        if (mPauseTimer != null) {
            mPauseTimer.pause();
        }
    }

    /**
     * Resume
     */
    public void resume() {
        if (mPauseTimer != null) {
            mPauseTimer.resume();
        }
    }

    /**
     * Stop and cancel the timer
     */
    public void cancel() {
        if (mPauseTimer != null) {
            mPauseTimer.mOnTimerCount = null;
            mPauseTimer.cancel();
        }
    }

    public long getPassedTime() {
        return mPauseTimer.timePassed();
    }

    public interface OnTimerCount {
        public void onTick(long millisUntilFinished);

        public void onFinish();
    }

    /**
     * Created by admin on 4/14/2018.
     */

    static class FontLoader {
        public static final int GROBOLD = 0;

        private static SparseArray<Typeface> fonts = new SparseArray<Typeface>();
        private static boolean fontsLoaded = false;

        public static enum Font {
            GROBOLD( FontLoader.GROBOLD, "fonts/grobold.ttf");

            private int val;
            private String path;

            private Font(int val, String path) {
                this.val = val;
                this.path = path;
            }

            public static String getByVal(int val) {
                for (Font font : Font.values()) {
                    if (font.val == val) {
                        return font.path;
                    }
                }
                return null;
            }
        }

        public static void loadFonts(Context context) {
            for (int i = 0; i < Font.values().length; i++) {
                fonts.put(i, Typeface.createFromAsset(context.getAssets(), Font.getByVal(i)));
            }
            fontsLoaded = true;
        }

        /**
         * Returns a loaded custom font based on it's identifier.
         *
         * @param context
         *            - the current context
         * @param fontIdentifier
         *            = the identifier of the requested font
         *
         * @return Typeface object of the requested font.
         */
        public static Typeface getTypeface(Context context, Font font) {
            if (!fontsLoaded) {
                loadFonts(context);
            }
            return fonts.get(font.val);
        }

        /**
         * Set the given font into the array of text views
         *
         * @param context
         *            - the current context
         * @param textViews
         *            - array of text views to set
         * @param fontIdentifier
         *            = the identifier of the requested font
         */
        public static void setTypeface(Context context, TextView[] textViews, Font font) {
            setTypeFaceToTextViews(context, textViews, font, Typeface.NORMAL);
        }

        /**
         * Set the given bold font into the array of text views
         *
         * @param context
         *            - the current context
         * @param textViews
         *            - array of text views to set
         * @param fontIdentifier
         *            = the identifier of the requested font
         */
        public static void setBoldTypeface(Context context, TextView[] textViews, Font font) {
            setTypeFaceToTextViews(context, textViews, font, Typeface.BOLD);
        }

        private static void setTypeFaceToTextViews(Context context, TextView[] textViews, Font font, int fontStyle) {
            if (!fontsLoaded) {
                loadFonts(context);
            }
            Typeface currentFont = fonts.get(font.val);

            for (int i = 0; i < textViews.length; i++) {
                if (textViews[i] != null)
                    textViews[i].setTypeface(currentFont, fontStyle);
            }
        }

    }
}
