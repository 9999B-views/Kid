package vn.devpro.devprokidorigin.activities.game.dapbongbay.game;

import android.graphics.Bitmap;
import java.util.ArrayList;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.BodyType;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.GameEngine;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.Sprite;

public class AnimGame extends Sprite {
    private final AnimExplisionSystem animExplisionSystem;
    private ArrayList<AnimGameModifiers> mModifiers;
    private long mTotalMillis;
    private long mTimeToLive;
    public double mSpeedY;


    protected AnimGame(AnimExplisionSystem animGameSystem, GameEngine gameEngine, Bitmap bmp) {
        super(gameEngine, bmp, BodyType.None);
        animExplisionSystem = animGameSystem;
    }

    @Override
    public void startGame(GameEngine gameEngine) {

    }

    @Override
    public void removeFromGameEngine(GameEngine gameEngine) {
        super.removeFromGameEngine(gameEngine);
        animExplisionSystem.returnToPool(this);
    }

    public void activate(GameEngine gameEngine, long timeToLive, double x, double y, ArrayList<AnimGameModifiers> modifiers, int layer) {
        mTimeToLive = timeToLive;
        mX = x - mWidth / 2;
        mY = y - mHeight / 2;
        addToGameEngine(gameEngine, layer);
        mModifiers = modifiers;
        mTotalMillis = 0;
    }


    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mTotalMillis += elapsedMillis;
        if (mTotalMillis > mTimeToLive) {
            // Return it to the pool
            removeFromGameEngine(gameEngine);
        } else {
            mY += mSpeedY * elapsedMillis;
            for (int i = 0; i < mModifiers.size(); i++) {
                mModifiers.get(i).apply(this, mTotalMillis);
            }
        }
    }
}
