package vn.devpro.devprokidorigin.activities.game.latbai.uitls;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

/**
 * Created by admin on 4/2/2018.
 */

public abstract class CountDown {
    /**
     * thời gian tính từ khi bắt đầu đến dừng dại
     */
    private long mStopTimeInFuture;

    /**
     * thời gian thực còn lại khi hoàn thành
     */
    private long mMillisInFuture;

    /**
     * tổng thời gian kể từ khi lúc bắt đầu
     */
    private final long mTotalCountdown;

    /**
     * The interval in millis that the user receives callbacks
     */
    private final long mCountdownInterval;

    /**
     * thời gian còn lại khi bị tạm dừng
     */
    private long mPauseTimeRemaining;

    /**
     * đúng nếu bộ hẹn giờ bắt đầu chạy hoạc không
     */
    private boolean mRunAtStart;

    /**
     * @param millisInFuture
     *            The number of millis in the future from the call to
     *            {@link #start} until the countdown is done and
     *            {@link #onFinish()} is called
     * @param countDownInterval
     *            The interval in millis at which to execute
     *            {@link #onTick(millisUntilFinished)} callbacks
     * @param runAtStart
     *            True if timer should start running, false if not
     */
    public CountDown(long millisOnTimer, long countDownInterval, boolean runAtStart) {
        mMillisInFuture = millisOnTimer;
        mTotalCountdown = mMillisInFuture;
        mCountdownInterval = countDownInterval;
        mRunAtStart = runAtStart;
    }

    /**
     * Cancel the countdown and clears all remaining messages
     */
    public final void cancel() {
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * Create the timer object.
     */
    public synchronized final CountDown create() {
        if (mMillisInFuture <= 0) {
            onFinish();
        } else {
            mPauseTimeRemaining = mMillisInFuture;
        }

        if (mRunAtStart) {
            resume();
        }

        return this;
    }

    /**
     * Pauses the counter.
     */
    public void pause() {
        if (isRunning()) {
            mPauseTimeRemaining = timeLeft();
            cancel();
        }
    }

    /**
     * Resumes the counter.
     */
    public void resume() {
        if (isPaused()) {
            mMillisInFuture = mPauseTimeRemaining;
            mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
            mHandler.sendMessage(mHandler.obtainMessage(MSG));
            mPauseTimeRemaining = 0;
        }
    }

    /**
     * Tests whether the timer is paused.
     *
     * @return true if the timer is currently paused, false otherwise.
     */
    public boolean isPaused() {
        return (mPauseTimeRemaining > 0);
    }

    /**
     * Tests whether the timer is running. (Performs logical negation on
     * {@link #isPaused()})
     *
     * @return true if the timer is currently running, false otherwise.
     */
    public boolean isRunning() {
        return (!isPaused());
    }

    /**
     * Returns the number of milliseconds remaining until the timer is finished
     *
     * @return number of milliseconds remaining until the timer is finished
     */
    public long timeLeft() {
        long millisUntilFinished;
        if (isPaused()) {
            millisUntilFinished = mPauseTimeRemaining;
        } else {
            millisUntilFinished = mStopTimeInFuture - SystemClock.elapsedRealtime();
            if (millisUntilFinished < 0)
                millisUntilFinished = 0;
        }
        return millisUntilFinished;
    }

    /**
     * Returns the number of milliseconds in total that the timer was set to run
     *
     * @return number of milliseconds timer was set to run
     */
    public long totalCountdown() {
        return mTotalCountdown;
    }

    /**
     * Returns the number of milliseconds that have elapsed on the timer.
     *
     * @return the number of milliseconds that have elapsed on the timer.
     */
    public long timePassed() {
        return mTotalCountdown - timeLeft();
    }

    /**
     * Returns true if the timer has been started, false otherwise.
     *
     * @return true if the timer has been started, false otherwise.
     */
    public boolean hasBeenStarted() {
        return (mPauseTimeRemaining <= mMillisInFuture);
    }

    /**
     * Callback fired on regular interval
     *
     * @param millisUntilFinished
     *            The amount of time until finished
     */
    public abstract void onTick(long millisUntilFinished);

    /**
     * Callback fired when the time is up.
     */
    public abstract void onFinish();

    private static final int MSG = 1;

    // handles counting down
    private final Handler mHandler = new Handler( Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {

            synchronized (CountDown.this) {
                long millisLeft = timeLeft();

                if (millisLeft <= 0) {
                    cancel();
                    onFinish();
                } else if (millisLeft < mCountdownInterval) {
                    // no tick, just delay until done
                    sendMessageDelayed(obtainMessage(MSG), millisLeft);
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    onTick(millisLeft);

                    long delay = mCountdownInterval - (SystemClock.elapsedRealtime() - lastTickStart);

                    while (delay < 0)
                        delay += mCountdownInterval;

                    sendMessageDelayed(obtainMessage(MSG), delay);
                }
            }
        }
    };
}
