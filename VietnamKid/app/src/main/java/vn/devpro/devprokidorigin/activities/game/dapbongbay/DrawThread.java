package vn.devpro.devprokidorigin.activities.game.dapbongbay;

public class DrawThread extends Thread {

    private final GameEngine mGameEngine;
    private boolean mGameIsRunning = true;
    private boolean mPauseGame = false;

    private Object mLock = new Object();

    public DrawThread(GameEngine gameEngine) {
        mGameEngine = gameEngine;
    }

    @Override
    public void start() {
        mGameIsRunning = true;
        mPauseGame = false;
        super.start();
    }

    public void stopGame() {
        mGameIsRunning = false;
        resumeGame();
    }

    @Override
    public void run() {
        long elapsedMillis;
        long currentTimeMillis;
        long previousTimeMillis = System.currentTimeMillis();

        while (mGameIsRunning) {
            currentTimeMillis = System.currentTimeMillis();
            elapsedMillis = currentTimeMillis - previousTimeMillis;
            if (mPauseGame) {
                while (mPauseGame) {
                    try {
                        synchronized (mLock) {
                            mLock.wait();
                        }
                    } catch (InterruptedException e) {
                    }
                }
                currentTimeMillis = System.currentTimeMillis();
            }
            if (elapsedMillis < 15) {
                try {
                    Thread.sleep(15-elapsedMillis);
                } catch (InterruptedException e) {
                }
            }
            mGameEngine.onDraw();
            previousTimeMillis = currentTimeMillis;
        }
    }

    public void pauseGame() {
        mPauseGame = true;
    }

    public void resumeGame() {
        if (mPauseGame == true) {
            mPauseGame = false;
            synchronized (mLock) {
                mLock.notify();
            }
        }
    }

    public boolean isGameRunning() {
        return mGameIsRunning;
    }

    public boolean isGamePaused() {
        return mPauseGame;
    }
}
