package vn.devpro.devprokidorigin.activities.game.dapbongbay;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
public class QuadTree {

    private static final int MAX_QUADTREES = 0;
    private List<ScreenObject> mGameObjects = new ArrayList<ScreenObject>();
    private Rect mArea = new Rect();
    private static List<QuadTree> sQuadTreePool = new ArrayList<QuadTree>();

    public static void init() {
        sQuadTreePool.clear();
        for (int i = 0; i < MAX_QUADTREES; i++) {
            sQuadTreePool.add(new QuadTree());
        }
    }

    public void setArea(Rect area) {
        mArea.set(area);
    }


    public void checkCollisions(GameEngine gameEngine, List<Collision> detectedCollisions) {
        int numObjects = mGameObjects.size();
            for (int i = 0; i < numObjects; i++) {
                ScreenObject obA = mGameObjects.get(i);
                for (int j = i + 1; j < numObjects; j++) {
                    ScreenObject obB = mGameObjects.get(j);
                    if (obA.checkCollision(obB)) {
                        Collision c = Collision.init(obA, obB);
                        if (!hasBeenDetected(detectedCollisions, c)) {
                            detectedCollisions.add(c);
                            obA.onCollision(gameEngine, obB);
                            obB.onCollision(gameEngine, obA);
                        }
                    }
                }
            }
//        }
    }

    private boolean hasBeenDetected(List<Collision> detectedCollisions, Collision c) {
        int numCollisions = detectedCollisions.size();
        for (int i=0; i<numCollisions; i++) {
            if (detectedCollisions.get(i).equals(c)) {
                return true;
            }
        }
        return false;
    }


    public void addGameObject(ScreenObject sgo) {
        mGameObjects.add(sgo);
    }

    public void removeGameObject(ScreenObject objectToRemove) {
        mGameObjects.remove(objectToRemove);
    }
}
