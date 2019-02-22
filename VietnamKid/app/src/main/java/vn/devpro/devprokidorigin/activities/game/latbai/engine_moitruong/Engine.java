package vn.devpro.devprokidorigin.activities.game.latbai.engine_moitruong;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.activities.game.latbai.commom.Memory;
import vn.devpro.devprokidorigin.activities.game.latbai.commom.Shared;
import vn.devpro.devprokidorigin.activities.game.latbai.even.BackGame;
import vn.devpro.devprokidorigin.activities.game.latbai.even.ChangeBackground;
import vn.devpro.devprokidorigin.activities.game.latbai.even.DataMissing;
import vn.devpro.devprokidorigin.activities.game.latbai.even.FlipCardEven;
import vn.devpro.devprokidorigin.activities.game.latbai.even.NextGame;
import vn.devpro.devprokidorigin.activities.game.latbai.even.StarNewGame;
import vn.devpro.devprokidorigin.activities.game.latbai.even.Sukien_chon_chude;
import vn.devpro.devprokidorigin.activities.game.latbai.even.Sukien_chondokho;
import vn.devpro.devprokidorigin.activities.game.latbai.even.engine.FlipDownCardEven;
import vn.devpro.devprokidorigin.activities.game.latbai.even.engine.GameWonPairEven;
import vn.devpro.devprokidorigin.activities.game.latbai.even.engine.HidePairCardEven;
import vn.devpro.devprokidorigin.activities.game.latbai.even.ui.EvenObseverAdapter;
import vn.devpro.devprokidorigin.activities.game.latbai.themes.Theme;
import vn.devpro.devprokidorigin.activities.game.latbai.themes.Themes;
import vn.devpro.devprokidorigin.activities.game.latbai.ui.PopupManager;
import vn.devpro.devprokidorigin.activities.game.latbai.uitls.Clock;
import vn.devpro.devprokidorigin.activities.game.latbai.uitls.Utils_PairGame;
import vn.devpro.devprokidorigin.models.Sound;
import vn.devpro.devprokidorigin.models.entity.TopicItem;
import vn.devpro.devprokidorigin.models.entity.game.latbai.BoardCauhinh;
import vn.devpro.devprokidorigin.models.entity.game.latbai.BoardXep;
import vn.devpro.devprokidorigin.models.entity.game.latbai.Game;
import vn.devpro.devprokidorigin.models.entity.game.latbai.GameState;
import vn.devpro.devprokidorigin.models.entity.game.latbai.Image_n_Sound_Name;
import vn.devpro.devprokidorigin.utils.Global;

/**
 * Created by admin on 3/30/2018.
 */

public class Engine extends EvenObseverAdapter {

    ArrayList<TopicItem> listImageForGame = new ArrayList<>();
    private static Engine mInstance = null;
    private Game mPlayingGame = null;
    private int mFlippedId = -1;
    private int mToFlip = -1;

    public int getCurrentopicId() {
        return currentopicId;
    }

    public void setCurrentopicId(int currentopicId) {
        this.currentopicId = currentopicId;
    }

    private int currentopicId;
    private ScreenController mScreenController;
    private Theme mSelectedTheme;
    private ImageView mBackgroundImage;
    private Handler mHandler;
    private android.view.ViewGroup container;

    private Engine() {
        mScreenController = ScreenController.getInstance();
        mHandler = new Handler();
    }

    public static Engine getmInstance() {
        if (mInstance == null) {
            mInstance = new Engine();
        }
        return mInstance;
    }

//

    public void start() {
        Shared.eventBus.listen(Sukien_chondokho.TYPE, this);
        Shared.eventBus.listen(FlipCardEven.TYPE, this);
        Shared.eventBus.listen(StarNewGame.TYPE, this);
        Shared.eventBus.listen(Sukien_chon_chude.TYPE, this);
        Shared.eventBus.listen(BackGame.TYPE, this);
        Shared.eventBus.listen(NextGame.TYPE, this);
        Shared.eventBus.listen(ChangeBackground.TYPE, this);
        Shared.eventBus.listen(DataMissing.TYPE, this);

    }

    public void stop() {
        mPlayingGame = null;
        mBackgroundImage.setImageDrawable(null);
        mBackgroundImage = null;
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;

        Shared.eventBus.unlisten(Sukien_chondokho.TYPE, this);
        Shared.eventBus.unlisten(FlipCardEven.TYPE, this);
        Shared.eventBus.unlisten(StarNewGame.TYPE, this);

        Shared.eventBus.unlisten(Sukien_chon_chude.TYPE, this);
        Shared.eventBus.unlisten(BackGame.TYPE, this);
        Shared.eventBus.unlisten(NextGame.TYPE, this);
        Shared.eventBus.unlisten(ChangeBackground.TYPE, this);
        Shared.eventBus.unlisten(DataMissing.TYPE, this);

        mInstance = null;
    }

    @Override
    public void onEvent(ChangeBackground event) {
        Drawable drawable = mBackgroundImage.getDrawable();
        if (drawable != null) {
            ((TransitionDrawable) drawable).reverseTransition(2000);
        } else {
            new AsyncTask<Void, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Void... params) {
                    Bitmap bitmap = Utils_PairGame.scaleDown(R.drawable.screens_09, Utils_PairGame.screenWidth(),
                            Utils_PairGame.screenHeight());
                    return bitmap;
                }

                protected void onPostExecute(Bitmap bitmap) {
                    mBackgroundImage.setImageBitmap(bitmap);
                }
            }.execute();
        }
    }

    @Override
    public void onEvent(StarNewGame event) {
//         SỰ KIỆN bắt đầu sẽ được nhảy vào chọn chủ đề  , hãy thử sang diffical
        // mScreenController.openScreen( ScreenController.Screen.DIFFICULTY );
//chỉ cần  ném đủ chủ đề vào đây
        final Theme theme = Themes.createThemeById(event.topicId);
        if (theme.listImageForGame.size() == 0) {
            Shared.eventBus.notify(new DataMissing());
            return;
        }
        Shared.eventBus.notify(new Sukien_chon_chude(theme));
        mFlippedId = -1;
        mPlayingGame = new Game();
        mPlayingGame.boardCauhinh = new BoardCauhinh(event.difficulty);
        mPlayingGame.theme = mSelectedTheme;
        mToFlip = mPlayingGame.boardCauhinh.numTiles;

            // arrange board
            //xếp ảnh
            arrangeBoard();

    }

    @Override
    public void onEvent(NextGame event) {
        PopupManager.closePopup();
        int id;

        int difficulty = mPlayingGame.boardCauhinh.difficulty;
        if (mPlayingGame.gameState.achievedStars == 3 && difficulty < 6) {
            difficulty++;
        }

        Shared.eventBus.notify(new StarNewGame(difficulty, Shared.engine.getCurrentopicId()));

    }

    @Override
    public void onEvent(BackGame event) {
        PopupManager.closePopup();
        mScreenController.openScreen(ScreenController.Screen.MENU);
    }

    @Override
    //TODO : Sử lý sự kiện chọn chủ đề
    public void onEvent(Sukien_chon_chude event) {

        mSelectedTheme = event.theme;
        mScreenController.openScreen(ScreenController.Screen.GAME);
        AsyncTask<Void, Void, TransitionDrawable> task = new AsyncTask<Void, Void, TransitionDrawable>() {
            @Override
            protected TransitionDrawable doInBackground(Void... params) {
                Bitmap bitmap = Utils_PairGame.scaleDown(R.drawable.screens_09, Utils_PairGame.screenWidth(), Utils_PairGame.screenHeight());
                Bitmap backgroundImage = Themes.getBackgroundImage(mSelectedTheme);
                backgroundImage = Utils_PairGame.crop(backgroundImage, Utils_PairGame.screenHeight(), Utils_PairGame.screenWidth());
                Drawable backgrounds[] = new Drawable[2];
                backgrounds[0] = new BitmapDrawable(Shared.context.getResources(), bitmap);
                backgrounds[1] = new BitmapDrawable(Shared.context.getResources(), backgroundImage);
                TransitionDrawable crossfader = new TransitionDrawable(backgrounds);
                return crossfader;
            }

            @Override
            protected void onPostExecute(TransitionDrawable result) {
                super.onPostExecute(result);
                mBackgroundImage.setImageDrawable(result);
//              mBackgroundImage.setImageBitmap(  );
                result.startTransition(2000);
            }
        };
        task.execute();
    }

    //TODO - show restart app dialog when db is missing
    @Override
    public void onEvent(DataMissing event) {
        mScreenController.showAlertDialog();
    }


    private void arrangeBoard() {
        BoardCauhinh boardConfiguration = mPlayingGame.boardCauhinh;
        BoardXep boardArrangment = new BoardXep();

        // build pairs
//         xây dựng cặp ảnh
        // result {0,1,2,...n} // n-number of tiles
        List<Integer> ids = new ArrayList<Integer>();
        for (int i = 0; i < boardConfiguration.numTiles; i++) {
            ids.add(i);
        }
        // shuffle
//         xáo trộn để không trùng lặp
        // result {4,10,2,39,...}
        Collections.shuffle(ids);

        // place the board
//         đặt vào bảng
//         mawht sau
        List<String> tileImageUrls = mPlayingGame.theme.tileImageUrls;
        ArrayList<TopicItem> listImageForGame = mPlayingGame.theme.listImageForGame;
        Collections.shuffle(listImageForGame);
        boardArrangment.pairs = new HashMap<Integer, Integer>();
        boardArrangment.tileUrls = new HashMap<Integer, String>();
        boardArrangment.tileImageNames = new HashMap<Integer, Image_n_Sound_Name>();
        int j = 0;
        for (int i = 0; i < ids.size(); i++) {
            if (i + 1 < ids.size()) {
                // {4,10}, {2,39}, ...
                boardArrangment.pairs.put(ids.get(i), ids.get(i + 1));
                // {10,4}, {39,2}, ...
                boardArrangment.pairs.put(ids.get(i + 1), ids.get(i));
                // {4,
                if (Global.getLanguage() == Global.VN) {
                    boardArrangment.tileImageNames.put(ids.get(i), new Image_n_Sound_Name(listImageForGame.get(j).getImg_name(), listImageForGame.get(j).getSound_vn()));
                    boardArrangment.tileImageNames.put(ids.get(i + 1), new Image_n_Sound_Name(listImageForGame.get(j).getImg_name(), listImageForGame.get(j).getSound_vn()));
                } else {
                    boardArrangment.tileImageNames.put(ids.get(i), new Image_n_Sound_Name(listImageForGame.get(j).getImg_name(), listImageForGame.get(j).getSound_en()));
                    boardArrangment.tileImageNames.put(ids.get(i + 1), new Image_n_Sound_Name(listImageForGame.get(j).getImg_name(), listImageForGame.get(j).getSound_en()));
                }

                i++;
                j++;
            }
        }
        mPlayingGame.boardXep = boardArrangment;
    }

    @Override
    public void onEvent(FlipCardEven event) {
        // Log.i("my_tag", "Flip: " + event.id);
        int id = event.id;
        if (mFlippedId == -1) {
            mFlippedId = id;
            // Log.i("my_tag", "Flip: mFlippedId: " + event.id);
        } else {
            if (mPlayingGame.boardXep.isPair(mFlippedId, id)) {
                // Log.i("my_tag", "Flip: is pair: " + mFlippedId + ", " + id);
                // send event - hide id1, id2
                Shared.eventBus.notifyy(new HidePairCardEven(mFlippedId, id), 1000);
                // play music
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Sound.playCorrent();
                    }
                }, 1000);
                mToFlip -= 2;
                if (mToFlip == 0) {
                    int passedSeconds = (int) (Clock.getInstance().getPassedTime() / 1000);
                    Clock.getInstance().pause();
                    int totalTime = mPlayingGame.boardCauhinh.time;
                    GameState gameState = new GameState();
                    mPlayingGame.gameState = gameState;
                    // remained seconds
                    gameState.remainedSeconds = totalTime - passedSeconds;
                    gameState.passedSeconds = passedSeconds;

                    // calc stars
                    if (passedSeconds <= totalTime / 2) {
                        gameState.achievedStars = 3;
                    } else if (passedSeconds <= totalTime - totalTime / 5) {
                        gameState.achievedStars = 2;
                    } else if (passedSeconds < totalTime) {
                        gameState.achievedStars = 1;
                    } else {
                        gameState.achievedStars = 0;
                    }

                    // calc score
                    gameState.achievedScore = mPlayingGame.boardCauhinh.difficulty * gameState.remainedSeconds *
                            mPlayingGame.theme.id;

                    // save to memory
                    Memory.save(mPlayingGame.theme.id, mPlayingGame.boardCauhinh.difficulty, gameState.achievedStars);
                    Memory.saveTime(mPlayingGame.theme.id, mPlayingGame.boardCauhinh.difficulty, gameState.passedSeconds);


                    Shared.eventBus.notifyy(new GameWonPairEven(gameState), 1000);
                }
            } else {
                // Log.i("my_tag", "Flip: all down");
                // send event - flip all down
                Shared.eventBus.notifyy(new FlipDownCardEven(), 1000);
            }
            mFlippedId = -1;
            // Log.i("my_tag", "Flip: mFlippedId: " + mFlippedId);
        }
    }

    public Game getActiveGame() {
        return mPlayingGame;
    }

    public Theme getSelectedTheme() {
        return mSelectedTheme;
    }

    public void setBackgroundImageView(ImageView backgroundImage) {
        mBackgroundImage = backgroundImage;
    }


    private void animateShow(View view) {
        ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1f);
        ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(300);
        animatorSet.playTogether(animatorScaleX, animatorScaleY);
        animatorSet.setInterpolator(new DecelerateInterpolator(2));
        view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        animatorSet.start();
    }

    private void setStars(ImageView imageView, Theme theme, String type) {
        int sum = 0;
        for (int difficulty = 1; difficulty <= 6; difficulty++) {
            sum += Memory.getHighStars(theme.id, difficulty);
        }
        int num = sum / 6;
        if (num != 0) {
            String drawableResourceName = String.format(Locale.US, type + "_theme_star_%d", num);
            int drawableResourceId = Shared.context.getResources().getIdentifier(drawableResourceName,
                    "drawable", Shared.context.getPackageName());
            imageView.setImageResource(drawableResourceId);

        }
    }
}