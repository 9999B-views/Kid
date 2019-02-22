package vn.devpro.devprokidorigin.activities.game.dapbongbay;

public class UpdateThread extends Thread {

    private final GameEngine mGameEngine;
    private boolean mIsRunning = true;
    private boolean mPause = false;

    private Object mLock = new Object();

    public UpdateThread(GameEngine gameEngine) {
        mGameEngine = gameEngine;
    }

    @Override
    public void start() {
        mIsRunning = true;
        mPause = false;
        super.start();
    }

    public void stopGame() {
        mIsRunning = false;
        resumeGame();
    }

    @Override
    public void run() {
        long previousTime;
        long currentTime;
        long time;
        previousTime = System.currentTimeMillis();

        while (mIsRunning) {
            currentTime = System.currentTimeMillis();
            time = currentTime - previousTime;
            if (mPause) {
                while (mPause) {
                    try {
                        synchronized (mLock) {
                            mLock.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                currentTime = System.currentTimeMillis();
            }

            mGameEngine.onUpdate((long) (time * 1.5));
            previousTime = currentTime;
        }
    }

    public void pauseGame() {
        mPause = true;
    }

    public void resumeGame() {
        if (mPause == true) {
            mPause = false;
            synchronized (mLock) {
                mLock.notify();
            }
        }
    }

    public boolean isGameRunning() {
        return mIsRunning;
    }

    public boolean isGamePaused() {
        return mPause;
    }
}