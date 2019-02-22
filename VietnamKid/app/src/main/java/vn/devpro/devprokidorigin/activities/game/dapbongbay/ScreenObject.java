package vn.devpro.devprokidorigin.activities.game.dapbongbay;

import android.graphics.Rect;

public abstract class ScreenObject extends GameObject {

    protected double mX;
    protected double mY;
    protected int mHeight;
    protected int mWidth;
    public double mRadius;
    public BodyType mBodyType;
    public void onCollision(GameEngine gameEngine, ScreenObject otherObject) {
    }
    public Rect mBoundingRect = new Rect(-1, -1, -1, -1);
    public void onPostUpdate(GameEngine gameEngine) {
        mBoundingRect.set(
                (int) mX,
                (int) mY,
                (int) mX + mWidth,
                (int) mY + mHeight);
    }

    public boolean checkCollision(ScreenObject otherObject) {
            return checkMixedCollision(otherObject);
        }

    private boolean checkMixedCollision(ScreenObject other) {
        ScreenObject circularSprite;
        ScreenObject rectangularSprite;
        if (mBodyType == BodyType.Rectangular) {
            circularSprite = this;
            rectangularSprite = other;
        } else {
            circularSprite = other;
            rectangularSprite = this;
        }

        double circleCenterX = circularSprite.mX + circularSprite.mWidth / 2;
        double positionXToCheck = circleCenterX;
        if (circleCenterX < rectangularSprite.mX) {
            positionXToCheck = rectangularSprite.mX;
        } else if (circleCenterX > rectangularSprite.mX + rectangularSprite.mWidth) {
            positionXToCheck = rectangularSprite.mX + rectangularSprite.mWidth;
        }
        double distanceX = circleCenterX - positionXToCheck;

        double circleCenterY = circularSprite.mY + circularSprite.mHeight / 2;
        double positionYToCheck = circleCenterY;
        if (circleCenterY < rectangularSprite.mY) {
            positionYToCheck = rectangularSprite.mY;
        } else if (circleCenterY > rectangularSprite.mY + rectangularSprite.mHeight) {
            positionYToCheck = rectangularSprite.mY + rectangularSprite.mHeight;
        }
        double distanceY = circleCenterY - positionYToCheck;

        double squareDistance = distanceX * distanceX + distanceY * distanceY;
        if (squareDistance <= circularSprite.mRadius * circularSprite.mRadius) {
            return true;
        }
        return false;

    }
}
