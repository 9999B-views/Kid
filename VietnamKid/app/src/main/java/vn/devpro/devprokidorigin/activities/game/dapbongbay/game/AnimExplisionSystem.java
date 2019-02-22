package vn.devpro.devprokidorigin.activities.game.dapbongbay.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import vn.devpro.devprokidorigin.activities.game.dapbongbay.GameEngine;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.ScreenObject;

public class AnimExplisionSystem extends ScreenObject {
    private final ArrayList<AnimGameModifiers> mModifiers;
    private final ArrayList<AnimGameInitializers> mInitializers;
    private final long mTimeToLive;
    private final double mPixelFactor;
    private List<AnimGame> mAnimGamePool = new ArrayList<>();
    private long mTotalMillis;
    private int mActivatedAnim;
    private boolean mIsEmiting;

    public AnimExplisionSystem(GameEngine gameEngine, int maxAnimGame, Bitmap bmpAnh, long timeToLive) {
        mModifiers = new ArrayList<>();
        mInitializers = new ArrayList<>();
        mTimeToLive = timeToLive;
        mPixelFactor = gameEngine.mPixelFactor;
        for (int i = 0; i < maxAnimGame; i++) {
            mAnimGamePool.add(new AnimGame(this, gameEngine, bmpAnh));
        }
    }

    public AnimExplisionSystem setSpeedRange(double speedMin, double speedMax) {
        mInitializers.add(new TranferAnimGame(speedMin * mPixelFactor, speedMax * mPixelFactor));
        return this;
    }


    @Override
    public void startGame(GameEngine gameEngine) {
    }


    public void oneShot(GameEngine gameEngine, double x, double y, int numAnimGame) {
        mX = x;
        mY = y;
        mIsEmiting = false;
        for (int i = 0; !mAnimGamePool.isEmpty() && i < numAnimGame; i++) {
            activateAnimGame(gameEngine);
        }
    }

    public AnimExplisionSystem setFadeOut(final long milisecondsBeforeEnd) {
        new AnimGameModifiers() {
            @Override
            public void apply(AnimGame mAnimGame, long miliseconds) {
                miliseconds = milisecondsBeforeEnd;
                long mStartTime = mTimeToLive - miliseconds;
                if (miliseconds < mStartTime) {
                    mAnimGame.mAlpha = 255;
                } else if (miliseconds > mTimeToLive) {
                    mAnimGame.mAlpha = 0;
                } else {
                    double percentageValue = (miliseconds - mStartTime) * 1d / miliseconds;
                    int newAlphaValue = (int) (255 + (0 - 255) * percentageValue);
                    mAnimGame.mAlpha = newAlphaValue;
                }
            }
        };
        return this;
    }

    private void activateAnimGame(GameEngine gameEngine) {
        AnimGame p = mAnimGamePool.remove(0);
        for (int i = 0; i < mInitializers.size(); i++) {
            mInitializers.get(i).initAnimGame(p);
        }
        p.activate(gameEngine, mTimeToLive, mX, mY, mModifiers, mLayer);
        mActivatedAnim++;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        if (!mIsEmiting) {
            return;
        }
        mTotalMillis += elapsedMillis;
        while (!mAnimGamePool.isEmpty() &&
                mActivatedAnim < mTotalMillis) {
            activateAnimGame(gameEngine);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
    }

    public void returnToPool(AnimGame animGame) {
        mAnimGamePool.add(animGame);
    }
}
