package vn.devpro.devprokidorigin.interfaces;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;


import vn.devpro.devprokidorigin.models.Sound;
import vn.devpro.devprokidorigin.models.xephinh.GameModel;
import vn.devpro.devprokidorigin.models.xephinh.PuzzlePiece;
import vn.devpro.devprokidorigin.utils.AnimatorUtils;
import vn.devpro.devprokidorigin.utils.Global;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class TouchListener implements View.OnTouchListener {
    private float xDelta;
    private float yDelta;
    private int correctCount = 0;
    float scaleDownFactor = 1f;
    PuzzlePiece currPiece;

    public TouchListener(PuzzleGameListener pGameCallBack) {
        gameCallBack = pGameCallBack;
    }

    public PuzzleGameListener getGameCallBack() {
        return gameCallBack;
    }

    public void setGameCallBack(PuzzleGameListener pGameCallBack) {
        gameCallBack = pGameCallBack;
    }

    private PuzzleGameListener gameCallBack;
    private GameModel mGameModel;

    @Override
    public boolean onTouch(View pView, MotionEvent pMotionEvent) {
        float x = pMotionEvent.getRawX();
        float y = pMotionEvent.getRawY();
        PuzzlePiece piece = (PuzzlePiece) pView;
        final double tolerance = sqrt(pow(piece.pieceWidth, 2) + pow(piece.pieceHeight, 2)) / 5;
        if (!piece.canMove) {
            return true;
        }
        scaleDownFactor = piece.startWidth / (float) piece.pieceWidth;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) pView.getLayoutParams();
        switch (pMotionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                //playSound
                Sound.getInstance(Global.mContext).playPuzzleSound(Sound.PIECE_PICK_UP);

                layoutParams.width = piece.pieceWidth;
                layoutParams.height = piece.pieceHeight;
                // new position when size increase
                float scXFac = piece.startWidth / (float) piece.pieceWidth;
                float scYFac = piece.startHeight / (float) piece.pieceHeight;
                layoutParams.leftMargin = (int) (layoutParams.leftMargin - (1 - scXFac) / 2f * piece.pieceWidth);
                layoutParams.topMargin = (int) (layoutParams.topMargin - (1 - scYFac) / 2f * piece.pieceHeight);
                xDelta = x - layoutParams.leftMargin;
                yDelta = y - layoutParams.topMargin;
                piece.bringToFront();
                //Log.d("AADA","------>layoutParams.width:"+layoutParams.width+", layoutParams.height:"+layoutParams.height);
                Log.d("AADA", "------>piece.index:" + piece.index);
                if (piece.getAnimation() != null) {
                    piece.getAnimation().cancel();
                }
                piece.clearAnimation();
                piece.setScaleX(1f);
                piece.setScaleY(1f);
                piece.setLayoutParams(layoutParams);
//                Animator animatorU = createScaleUpAnimator(piece);
//                animatorU.start();
                break;
            case MotionEvent.ACTION_MOVE:
                layoutParams.leftMargin = (int) (x - xDelta);
                layoutParams.topMargin = (int) (y - yDelta);
                pView.setLayoutParams(layoutParams);
                //System.out.println("ACTION_MOVE:-->xDel:"+xDelta+", yDel:"+yDelta+", x:"+x+", y:"+y+", tolerance:"+tolerance);
                break;
            case MotionEvent.ACTION_UP:
                int xDiff = abs(piece.xCoord - layoutParams.leftMargin);
                int yDiff = abs(piece.yCoord - layoutParams.topMargin);
                System.out.println("ACTION_UP:-->xCo:" + piece.xCoord + ", yCo:" + piece.yCoord + ", xDif:" + xDiff + ", yDif:" + yDiff + ", x:" + x + ", y:" + y + ", tolerance:" + tolerance);
                if (xDiff <= tolerance && yDiff <= tolerance) {
                    layoutParams.leftMargin = piece.xCoord;
                    layoutParams.topMargin = piece.yCoord;
                    layoutParams.width = piece.pieceWidth;
                    layoutParams.height = piece.pieceHeight;
                    piece.setLayoutParams(layoutParams);
                    piece.canMove = false;
                    sendViewToBack(piece);
                    correctCount++;
                    checkDone();
                    gameCallBack.onCorrectMove();
                } else {
                    //TODO play sound when release a piece
                    Sound.getInstance(Global.mContext).playPuzzleSound(Sound.PIECE_LAY_DOWN);
                    layoutParams.leftMargin = piece.xStartCoord;
                    layoutParams.topMargin = piece.yStartCoord;
                    layoutParams.width = piece.startWidth;
                    layoutParams.height = piece.startHeight;
                    piece.setLayoutParams(layoutParams);
                    //piece.setLayoutParams(layoutParams);
                    //piece.animate();
                  /*  ObjectAnimator scX = ObjectAnimator.ofFloat(piece, "scaleX", scaleDownFactor);
                    ObjectAnimator scY = ObjectAnimator.ofFloat(piece, "scaleY", scaleDownFactor);
                    currPiece = piece;
                    scX.setDuration(500);
                    scY.setDuration(500);
                    scX.addListener(anmListener);
                    scX.start();
                    scY.start();*/
                    Log.d("AADA", "------>scaleDown:" + scaleDownFactor);
                }

                break;
        }
        return true;
    }

    Animator.AnimatorListener anmListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator pAnimator) {

        }

        @Override
        public void onAnimationEnd(Animator pAnimator) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) currPiece.getLayoutParams();
            currPiece.clearAnimation();
            currPiece.setScaleX(1f);
            currPiece.setScaleY(1f);
            layoutParams.width = currPiece.startWidth;
            layoutParams.height = currPiece.startHeight;
            float scYFac = currPiece.startHeight / (float) currPiece.pieceHeight;
            layoutParams.leftMargin = (int) (layoutParams.leftMargin + (1 - scaleDownFactor) / 2f * currPiece.pieceWidth);
            layoutParams.topMargin = (int) (layoutParams.topMargin + (1 - scYFac) / 2f * currPiece.pieceHeight);
            currPiece.setLayoutParams(layoutParams);
        }

        @Override
        public void onAnimationCancel(Animator pAnimator) {

        }

        @Override
        public void onAnimationRepeat(Animator pAnimator) {

        }
    };


    private Animator createShowItemAnimator(View item) {
        float dx = item.getWidth() / 2;
        float dy = item.getHeight() / 2;
        item.setScaleX(0f);
        item.setScaleY(0f);
        item.setTranslationX(dx);
        item.setTranslationY(dy);
        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.scaleX(0f, 1f),
                AnimatorUtils.scaleY(0f, 1f),
                AnimatorUtils.translationX(dx, 0f),
                AnimatorUtils.translationY(dy, 0f)
        );
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(50);
        return anim;
    }

    private Animator createScaleDownAnimator(View pView) {
        pView.setScaleX(1f);
        pView.setScaleY(1f);
        Animator anim = ObjectAnimator.ofPropertyValuesHolder(pView,
                AnimatorUtils.scaleX(1f, scaleDownFactor),
                AnimatorUtils.scaleY(1f, scaleDownFactor));
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(3000);
        return anim;
    }

    private Animator createScaleUpAnimator(View pView) {
        pView.setScaleX(1f);
        pView.setScaleY(1f);
        Animator anim = ObjectAnimator.ofPropertyValuesHolder(pView,
                AnimatorUtils.scaleX(1f, 1 / scaleDownFactor),
                AnimatorUtils.scaleY(1f, 1 / scaleDownFactor));
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(10);
        return anim;
    }

    public void sendViewToBack(final View child) {
        final ViewGroup parent = (ViewGroup) child.getParent();
        if (parent != null) {
            parent.removeView(child);
            parent.addView(child, 0);
        }
    }

    private void checkDone() {
        if (correctCount >= mGameModel.getPieceNumbers()) {
            gameCallBack.onGameFinish();
        }
    }

    public void setGameModel(GameModel pGameModel) {
        this.mGameModel = pGameModel;
        this.correctCount = 0;
    }
}
