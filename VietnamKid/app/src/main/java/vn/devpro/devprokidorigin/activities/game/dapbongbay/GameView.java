package vn.devpro.devprokidorigin.activities.game.dapbongbay;

import android.content.Context;
import java.util.List;

public interface GameView {

    void draw();

    void setGameObjects(List<List<GameObject>> gameObjects);

    int getWidth();

    int getHeight();

    int getPaddingLeft();

    int getPaddingRight();

    int getPaddingTop();

    int getPaddingBottom();

    Context getContext();

    void postInvalidate();

}
