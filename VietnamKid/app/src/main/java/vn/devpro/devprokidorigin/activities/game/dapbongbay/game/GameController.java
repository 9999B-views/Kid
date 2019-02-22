package vn.devpro.devprokidorigin.activities.game.dapbongbay.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import vn.devpro.devprokidorigin.activities.game.dapbongbay.GameEngine;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.GameObject;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.sound.GameEvent;
import vn.devpro.devprokidorigin.models.entity.TopicItem;
import vn.devpro.devprokidorigin.utils.Global;

public class GameController extends GameObject {

    private static final int TIME_BETWEEN_BALLOONS = 1200;

    private long mCurrentMillis;

    public List<Balloon> getmBalloonPool() {
        return mBalloonPool;
    }

    private List<Balloon> mBalloonPool = new ArrayList<Balloon>();

    private int mBalloonsSpawned;

    public GameController(GameEngine gameEngine, GameFragment parent, ArrayList<TopicItem> listItem, ArrayList<Bitmap> listImgBitmap) {
        for (int i = 0; i < listItem.size(); i++) {
            if (listImgBitmap.get(i) == null) {
                if (Global.getLanguage() == Global.VN) {
                    mBalloonPool.add(new Balloon(this, gameEngine, listItem.get(i).getBitmap(), listItem.get(i).getSound_vn(), null));
                } else {
                    mBalloonPool.add(new Balloon(this, gameEngine, listItem.get(i).getBitmap(), listItem.get(i).getSound_en(), null));
                }
            } else {
                if (Global.getLanguage() == Global.VN) {
                    mBalloonPool.add(new Balloon(this, gameEngine, listItem.get(i).getBitmap(), listItem.get(i).getSound_vn(), listImgBitmap.get(i)));
                } else {
                    mBalloonPool.add(new Balloon(this, gameEngine, listItem.get(i).getBitmap(), listItem.get(i).getSound_en(), listImgBitmap.get(i)));
                }
            }
        }
    }

    @Override
    public void startGame(GameEngine gameEngine) {
        mCurrentMillis = 0;
        mBalloonsSpawned = 0;
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mCurrentMillis += elapsedMillis;
        if (TIME_BETWEEN_BALLOONS - gameEngine.count < 500) {
            gameEngine.count = 690;
        } else {
            // TODO: make time between balloons variable
            long waveTimestamp = mBalloonsSpawned * (TIME_BETWEEN_BALLOONS - gameEngine.count);
            if (mCurrentMillis > waveTimestamp) {
                // get a ballon at random from the pool
                Balloon a = mBalloonPool.remove(gameEngine.mRandom.nextInt(mBalloonPool.size()));
                a.init(gameEngine);
                a.addToGameEngine(gameEngine, mLayer);
                mBalloonsSpawned++;
                return;
            }
        }
    }


    @Override
    public void onDraw(Canvas canvas) {
    }

    public void returnToPool(Balloon balloon) {
        mBalloonPool.add(balloon);
    }

}
