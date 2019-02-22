package vn.devpro.devprokidorigin.activities.game.latbai.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.activities.game.latbai.commom.Shared;
import vn.devpro.devprokidorigin.activities.game.latbai.even.FlipCardEven;
import vn.devpro.devprokidorigin.activities.game.latbai.uitls.Utils_PairGame;
import vn.devpro.devprokidorigin.models.Sound;
import vn.devpro.devprokidorigin.models.entity.game.latbai.BoardCauhinh;
import vn.devpro.devprokidorigin.models.entity.game.latbai.BoardXep;
import vn.devpro.devprokidorigin.models.entity.game.latbai.Game;
import vn.devpro.devprokidorigin.utils.Global;

/**
 * Created by admin on 3/31/2018.
 */

public class BoardView extends LinearLayout {

    private LinearLayout.LayoutParams mRowLayoutParams = new LinearLayout.LayoutParams
            (LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    private LinearLayout.LayoutParams mTileLayoutParams;
    private int mScreenWidth;
    private int mScreenHeight;
    private BoardCauhinh mboardCauhinh;
    private BoardXep mboardXep;
    private Map<Integer, Tile> mViewReference;
    private List<Integer> flippedUp = new ArrayList<Integer>();
    //     úp tất cả khi bắt đầu
    private boolean mLocked = false;
    private int mSize;

    public BoardView(Context context) {
        super( context, null );
    }

    public BoardView(Context context, AttributeSet attributeSet) {
        super( context, attributeSet );
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);
        int margin = getResources().getDimensionPixelSize( R.dimen.margine_top);
        int padding = getResources().getDimensionPixelSize(R.dimen.board_padding);
        mScreenHeight = getResources().getDisplayMetrics().heightPixels - margin - padding*2;
        mScreenWidth = getResources().getDisplayMetrics().widthPixels - padding*2 - Utils_PairGame.px(20);
        mViewReference = new HashMap<Integer, Tile>();
        setClipToPadding(false);
    }


    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public static BoardView fromXml (Context context, ViewGroup parent) {
        return (BoardView) LayoutInflater.from(context).inflate(R.layout.banghienthi, parent, false);
    }

    public void setBoard(Game game) {
        mboardCauhinh = game.boardCauhinh;
        mboardXep = game.boardXep;
        // xét chhieeuf rộng và chiều cao
        int singleMargin = getResources().getDimensionPixelSize(R.dimen.card_margin);
        float density = getResources().getDisplayMetrics().density;
        singleMargin = Math.max((int) (1 * density), (int) (singleMargin - mboardCauhinh.difficulty * 2 * density));
        int sumMargin = 0;
        for (int row = 0; row < mboardCauhinh.numRows; row++) {
            sumMargin += singleMargin * 2;
        }
        int tilesHeight = (mScreenHeight - sumMargin) / mboardCauhinh.numRows;
        int tilesWidth = (mScreenWidth - sumMargin) / mboardCauhinh.numTilesInRow;
        mSize = Math.min(tilesHeight, tilesWidth);
// chỉnh mặt sau kéo sang hình chữ nhật
        mTileLayoutParams = new LinearLayout.LayoutParams( (int) (mSize ), (int) (mSize/(1.3f)) );
        mTileLayoutParams.setMargins(singleMargin, singleMargin, singleMargin, singleMargin);

        // xây dựng hình ảnh hiển thị
        buildBoard();
    }

    /**
     * Build the board
     */
    private void buildBoard() {
        for (int row = 0; row < mboardCauhinh.numRows; row++) {
            // add row
            addBoardRow(row);
        }

        setClipChildren(false);
    }


    private void addBoardRow(int rowNum) {

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);

        for (int tile = 0; tile < mboardCauhinh.numTilesInRow; tile++) {
            addTile(rowNum * mboardCauhinh.numTilesInRow + tile, linearLayout);
        }

        // add to this view
        addView(linearLayout, mRowLayoutParams);
        linearLayout.setClipChildren(false);
    }

    @SuppressLint("StaticFieldLeak")
    private void addTile(final int id, ViewGroup parent) {
        final Tile tileView = Tile.fromXml(getContext(), parent);
        tileView.setLayoutParams(mTileLayoutParams);
        parent.addView(tileView);
        parent.setClipChildren(false);
        mViewReference.put(id, tileView);

        new AsyncTask<Void, Void, Bitmap>() {
            private String soundName;

            @Override
            protected Bitmap doInBackground(Void... params) {
                soundName = mboardXep.getTileSoundName( id );
                return mboardXep.getTitleBitmap(id, mSize);
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                if (result!=null) {
                    tileView.setTileImage(result);
                    tileView.setSoundName(soundName);
                }
            }
        }.execute();

        tileView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!mLocked && tileView.isFlippedDown()) {
                    tileView.flipUp();
                    flippedUp.add(id);
                    //TODO play am khi mo bai
                    Sound.getInstance( Global.mContext ).playEncrytedFile( tileView.getSoundName() );
                    if (flippedUp.size() == 2) {
                        mLocked = true;
                    }
                    Shared.eventBus.notify(new FlipCardEven(id));
                }
            }
        });

        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(tileView, "scaleX", 0.8f, 1f);
        scaleXAnimator.setInterpolator(new BounceInterpolator());
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(tileView, "scaleY", 0.8f, 1f);
        scaleYAnimator.setInterpolator(new BounceInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator);
        animatorSet.setDuration(500);
        tileView.setLayerType( View.LAYER_TYPE_HARDWARE, null);
        animatorSet.start();
    }

    public void flipDownAll() {
        for (Integer id : flippedUp) {
            mViewReference.get(id).flipDown();
        }
        flippedUp.clear();
        mLocked = false;
    }

    public void hideCards(int id1, int id2) {
        animateHide(mViewReference.get(id1));
        animateHide(mViewReference.get(id2));
        flippedUp.clear();
        mLocked = false;
    }

    protected void animateHide(final Tile v) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "alpha", 0f);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                v.setLayerType(View.LAYER_TYPE_NONE, null);
                v.setVisibility(View.INVISIBLE);
            }
        });
        animator.setDuration(100);
        v.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        animator.start();
    }

}
