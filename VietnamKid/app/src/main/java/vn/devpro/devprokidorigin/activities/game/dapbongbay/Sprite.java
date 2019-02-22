package vn.devpro.devprokidorigin.activities.game.dapbongbay;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

public abstract class Sprite extends ScreenObject {

    private static final boolean VISUAL_COLLISION_DEBUG = false;
    public double mRotation;
    protected final double mPixelFactor;
    protected Bitmap mBitmap;
    private final Matrix mMatrix = new Matrix();
    private final Paint mPaint = new Paint();
    public int mAlpha = 255;
    public double mScale = 1;

    protected Sprite (GameEngine gameEngine, Bitmap bmp, BodyType bodyType) {
        mPixelFactor = gameEngine.mPixelFactor;
        mHeight = (int) (bmp.getHeight() * mPixelFactor);
        mWidth = (int) (bmp.getWidth() * mPixelFactor);
        mBitmap = bmp;
        mRadius = Math.max(mHeight, mWidth)/2;
        mBodyType = bodyType;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mX > canvas.getWidth()
                || mY > canvas.getHeight()
                || mX < -mWidth
                || mY < -mHeight) {
            return;
        }
        if (VISUAL_COLLISION_DEBUG) {
            mPaint.setColor(Color.YELLOW);
            mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
            if (mBodyType == BodyType.Circular) {
                canvas.drawCircle((int) (mX + mWidth / 2), (int) (mY + mHeight / 2), (int) mRadius, mPaint);
            } else if (mBodyType == BodyType.Rectangular) {
                canvas.drawRect(mBoundingRect, mPaint);
            }
        }
        float scaleFactor = (float) (mPixelFactor*mScale);
        mMatrix.reset();
        mMatrix.postScale(scaleFactor, scaleFactor);
        mMatrix.postTranslate((float) mX, (float) mY);
        mMatrix.postRotate((float) mRotation, (float) (mX + mWidth*mScale / 2), (float) (mY + mHeight*mScale / 2));
        mPaint.setAlpha(mAlpha);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
    }
}
