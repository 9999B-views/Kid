package vn.devpro.devprokidorigin.activities.game.dapbongbay.game;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.BodyType;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.GameEngine;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.ScreenObject;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.Sprite;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.sound.SoundManager;
import vn.devpro.devprokidorigin.models.Sound;
import vn.devpro.devprokidorigin.utils.Global;

import static android.content.Context.AUDIO_SERVICE;


public class Balloon extends Sprite {

    private SoundPool mSoundPool;
    int soundpop = -1;
    private final GameController mController;
    private String objectName;
    private Bitmap imgBitmap;
    private final double mSpeed;
    private final int mInitialY;
    private double mSpeedY;
    private AnimExplisionSystem mAnimExplision;

    public Balloon(GameController gameController, GameEngine gameEngine, Bitmap bmp, String fileName, Bitmap imgBitmap) {
        super(gameEngine, bmp, BodyType.Circular);
        mSpeed = 100d * mPixelFactor / 1200d;
        mController = gameController;
        mInitialY = gameEngine.mHeight;
        if (imgBitmap != null) {
            mAnimExplision = new AnimExplisionSystem(gameEngine, 14, imgBitmap, 1000).setFadeOut(300).setSpeedRange(50, 140);
        }
        this.objectName = fileName;
        this.imgBitmap = imgBitmap;
    }

    public String getObjectName() {
        return objectName;
    }

    @Override
    public void startGame(GameEngine gameEngine) {
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mY += mSpeedY * elapsedMillis;
        if (mY < -mHeight) {
            gameEngine.onGameEvent(BalloonGameEvent.BalloonMissed);
            removeFromGameEngine(gameEngine);
        }
    }

    @Override
    public void onRemovedFromGameEngine() {
        mController.returnToPool(this);
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenObject otherObject) {
        super.onCollision(gameEngine, otherObject);
        if (otherObject instanceof GameFragment.DummyObject) {
            explode(gameEngine);
            removeFromGameEngine(gameEngine);
            if (this.imgBitmap == null) {
                gameEngine.onGameEvent(BalloonGameEvent.BalloonHit);
            } else {
                Sound.getInstance(Global.mContext).playEncrytedFile(this.getObjectName());
            }
        }
    }

    public void explode(GameEngine gameEngine) {
        if (mAnimExplision == null) {
            return;
        }
        mAnimExplision.oneShot(gameEngine, mX + mWidth / 2, mY + mHeight / 2, 15);
        gameEngine.count++;
    }

    public void init(GameEngine gameEngine) {
        mSpeedY = -mSpeed * (gameEngine.mRandom.nextInt(50 + gameEngine.count) + (50 + gameEngine.count)) / 100f;
        mX = gameEngine.mRandom.nextInt(gameEngine.mWidth * 6 / 8) + gameEngine.mWidth / 8;
        mY = mInitialY;
        mRotation = gameEngine.mRandom.nextInt(20) - 10;
    }

}
