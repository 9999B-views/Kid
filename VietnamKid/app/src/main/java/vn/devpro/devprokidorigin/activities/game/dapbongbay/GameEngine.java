package vn.devpro.devprokidorigin.activities.game.dapbongbay;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import vn.devpro.devprokidorigin.activities.game.dapbongbay.game.Balloon;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.game.GameController;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.input.InputController;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.sound.GameEvent;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.sound.SoundManager;

public class GameEngine {

    private final GameView mGameView;
    private List<List<GameObject>> mLayers = new ArrayList<List<GameObject>>();
    private List<GameObject> mGameObjects = new ArrayList<GameObject>();
    private QuadTree mQuadTreeRoot = new QuadTree();
    private List<GameObject> mObjectsToAdd = new ArrayList<GameObject>();
    private List<GameObject> mObjectsToRemove = new ArrayList<GameObject>();
    private UpdateThread mUpdateThread;
    private DrawThread mDrawThread;
    public Activity mActivity;
    public int count;
    public InputController mInputController = new InputController();
    public Random mRandom = new Random();
    public int mWidth;
    public int mHeight;
    public double mPixelFactor;
    private SoundManager mSoundManager;
    private List<Collision> mDetectedCollisions = new ArrayList<Collision>();

    public GameEngine(Activity a, GameView gameView, int numLayers) {
        mActivity = a;
        mGameView = gameView;
        mGameView.setGameObjects(mLayers);
        QuadTree.init();
        mWidth = gameView.getWidth() - gameView.getPaddingRight() - gameView.getPaddingLeft();
        mHeight = gameView.getHeight() - gameView.getPaddingTop() - gameView.getPaddingBottom();
        mQuadTreeRoot.setArea(new Rect(0, 0, mWidth, mHeight));
        mPixelFactor = mHeight / 400d;
        for (int i = 0; i < numLayers; i++) {
            mLayers.add(new ArrayList<GameObject>());
        }
    }

    public void setSoundManager(SoundManager soundManager) {
        mSoundManager = soundManager;
    }


    public void onGameEvent(GameEvent gameEvent) {
        int numObjects = mGameObjects.size();
        for (int i = 0; i < numObjects; i++) {
           mGameObjects.get(i).onGameEvent(gameEvent);
        }
        mSoundManager.playSoundForGameEvent(gameEvent);
    }

    public void startGame() {
        stopGame();
        int numLayers = mGameObjects.size();
        for (int i = 0; i < numLayers; i++) {
            mGameObjects.get(i).startGame(this);
        }
        if (mInputController != null) {
            mInputController.onStart();
        }
        mUpdateThread = new UpdateThread(this);
        mUpdateThread.start();
        mDrawThread = new DrawThread(this);
        mDrawThread.start();
    }

    public void stopGame() {
        if (mUpdateThread != null) {
            synchronized (mLayers) {
                onGameEvent(new GameEvent.GameFinished());
            }
            mUpdateThread.stopGame();
            mUpdateThread = null;
        }
        if (mDrawThread != null) {
            mDrawThread.stopGame();
        }
        if (mInputController != null) {
            mInputController.onStop();
        }
    }

    public void pauseGame() {
        if (mUpdateThread != null) {
            mUpdateThread.pauseGame();
        }
        if (mDrawThread != null) {
            mDrawThread.pauseGame();
        }
        if (mInputController != null) {
            mInputController.onPause();
        }
    }

    public void resumeGame() {
        if (mUpdateThread != null) {
            mUpdateThread.resumeGame();
        }
        if (mDrawThread != null) {
            mDrawThread.resumeGame();
        }
        if (mInputController != null) {
            mInputController.onResume();
        }
    }

    public void addGameObject(final GameObject gameObject, int layer) {
        gameObject.mLayer = layer;
        if (isRunning()) {
            synchronized (mLayers) {
                mObjectsToAdd.add(gameObject);
            }
        } else {
            addToLayerNow(gameObject);
        }
        mActivity.runOnUiThread(gameObject.mOnAddedRunnable);
    }

    public void removeGameObject(final GameObject gameObject) {
        synchronized (mLayers) {
            mObjectsToRemove.add(gameObject);
        }
        mActivity.runOnUiThread(gameObject.mOnRemovedRunnable);
    }

    public void onUpdate(long elapsedMillis) {
        mInputController.onPreUpdate();
        int numObjects = mGameObjects.size();
        for (int i = 0; i < numObjects; i++) {
            mGameObjects.get(i).onUpdate(elapsedMillis, this);
            mGameObjects.get(i).onPostUpdate(this);
        }
        checkCollisions();
        synchronized (mLayers) {
            while (!mObjectsToRemove.isEmpty()) {
                GameObject objectToRemove = mObjectsToRemove.remove(0);
                if (mGameObjects.remove(objectToRemove)) {
                    mLayers.get(objectToRemove.mLayer).remove(objectToRemove);
                    if (objectToRemove instanceof ScreenObject) {
                        mQuadTreeRoot.removeGameObject((ScreenObject) objectToRemove);
                    }
                    objectToRemove.onRemovedFromGameEngine();
                }
            }
            while (!mObjectsToAdd.isEmpty()) {
                GameObject gameObject = mObjectsToAdd.remove(0);
                addToLayerNow(gameObject);
            }
        }
    }

    public void checkCollisions() {
        while (!mDetectedCollisions.isEmpty()) {
            Collision.release(mDetectedCollisions.remove(0));
        }
        mQuadTreeRoot.checkCollisions(this, mDetectedCollisions);
    }

    private void addToLayerNow(GameObject object) {
        int layer = object.mLayer;
        while (mLayers.size() <= layer) {
            mLayers.add(new ArrayList<GameObject>());
        }
        mLayers.get(layer).add(object);
        mGameObjects.add(object);
        if (object instanceof ScreenObject) {
            ScreenObject sgo = (ScreenObject) object;
            if (sgo.mBodyType != BodyType.None) {
                mQuadTreeRoot.addGameObject(sgo);
            }
        }
        object.onAddedToGameEngine();
    }

    public void onDraw() {
        mGameView.draw();
    }

    public boolean isRunning() {
        return mUpdateThread != null && mUpdateThread.isGameRunning();
    }

    public boolean isPaused() {
        return mUpdateThread != null && mUpdateThread.isGamePaused();
    }

    public Context getContext() {
        return mGameView.getContext();
    }
}
