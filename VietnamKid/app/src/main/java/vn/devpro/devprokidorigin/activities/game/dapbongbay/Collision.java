package vn.devpro.devprokidorigin.activities.game.dapbongbay;

import java.util.ArrayList;
import java.util.List;

public class Collision {

    private static List<Collision> sCollisionPool = new ArrayList<Collision>();

    public static Collision init(ScreenObject objectA, ScreenObject objectB) {
        if (sCollisionPool.isEmpty()) {
            return new Collision(objectA, objectB);
        }
        Collision c = sCollisionPool.remove(0);
        c.mObjectA = objectA;
        c.mObjectB = objectB;
        return c;
    }

    public static void release(Collision c) {
        c.mObjectA = null;
        c.mObjectB = null;
        sCollisionPool.add(c);
    }

    public ScreenObject mObjectA;
    public ScreenObject mObjectB;

    public Collision(ScreenObject objectA, ScreenObject objectB) {
        mObjectA = objectA;
        mObjectB = objectB;
    }

    public boolean equals (Collision c) {
        return (mObjectA == c.mObjectA && mObjectB == c.mObjectB)
                || (mObjectA == c.mObjectB && mObjectB == c.mObjectA);
    }
}
