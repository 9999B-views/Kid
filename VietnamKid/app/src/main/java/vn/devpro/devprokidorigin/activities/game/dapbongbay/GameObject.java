package vn.devpro.devprokidorigin.activities.game.dapbongbay;

import android.graphics.Canvas;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.sound.GameEvent;

public abstract class GameObject {

    public int mLayer;

    public abstract void startGame(GameEngine gameEngine);

    public abstract void onUpdate(long elapsedMillis, GameEngine gameEngine);

    public abstract void onDraw(Canvas canvas);

    public final Runnable mOnAddedRunnable = new Runnable() {
        @Override
        public void run() {
            onAddedToGameUiThread();
        }
    };

    public final Runnable mOnRemovedRunnable = new Runnable() {
        @Override
        public void run() {
            onRemovedFromGameUiThread();
        }
    };

    public void onRemovedFromGameUiThread(){
    }

    public void onAddedToGameUiThread(){
    }

    public void onPostUpdate(GameEngine gameEngine) {
    }

    public void addToGameEngine (GameEngine gameEngine, int layer) {
        gameEngine.addGameObject(this, layer);
    }

    public void removeFromGameEngine (GameEngine gameEngine) {
        gameEngine.removeGameObject(this);
    }

    public void onAddedToGameEngine() {
    }

    public void onRemovedFromGameEngine() {
    }

    public void onGameEvent(GameEvent gameEvent) {

    }
}
