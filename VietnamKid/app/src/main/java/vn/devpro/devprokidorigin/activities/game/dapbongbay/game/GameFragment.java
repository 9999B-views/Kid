package vn.devpro.devprokidorigin.activities.game.dapbongbay.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.BodyType;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.GameBaseActivity;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.GameBaseFragment;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.GameEngine;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.GameView;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.Sprite;
import vn.devpro.devprokidorigin.activities.game.dapbongbay.sound.SoundManager;
import vn.devpro.devprokidorigin.databases.DBHelper;
import vn.devpro.devprokidorigin.models.entity.TopicItem;
import vn.devpro.devprokidorigin.utils.Global;

/**
 * A placeholder fragment containing a simple view.
 */
@SuppressLint("NewApi")
public class GameFragment extends GameBaseFragment implements InputManager.InputDeviceListener, PauseDialog.PauseDialogListener, View.OnTouchListener {

    private static final int DUMMY_OBJECT_POOL_SIZE = 10;
    private GameBaseActivity mParent;
    private GameEngine mGameEngine;
    private Bitmap bmp, bmpOverLay;
    private Button btnSoundGame;
    private List<DummyObject> mDummyObjectPool = new ArrayList<DummyObject>();
    private ArrayList<TopicItem> listImg = new ArrayList<>();
    private ArrayList<TopicItem> listAlphabet = new ArrayList<>();
    private ArrayList<TopicItem> listVietAlphabet = new ArrayList<>();
    private ArrayList<TopicItem> listEnAlphabet = new ArrayList<>();


    public GameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        return rootView;
    }

    @Override
    protected void onLayoutCompleted() {
        initListAlphabet();
        prepareAndStartGame();
    }

    private void initListAlphabet() {
        listAlphabet.add(new TopicItem(0, "A", "A", "a_up", "av", "ae", 0, 0));
        listAlphabet.add(new TopicItem(1, "Ă", null, "aw_up", "aw", null, 0, 0));
        listAlphabet.add(new TopicItem(2, "Â", null, "aa_up", "aa", null, 0, 0));
        listAlphabet.add(new TopicItem(3, "B", "B", "b_up", "bv", "be", 0, 0));
        listAlphabet.add(new TopicItem(4, "C", "C", "c_up", "cv", "ce", 0, 0));
        listAlphabet.add(new TopicItem(5, "D", "D", "d_up", "dv", "de", 0, 0));
        listAlphabet.add(new TopicItem(6, "Đ", null, "dd_up", "dd", null, 0, 0));
        listAlphabet.add(new TopicItem(7, "E", "E", "e_up", "ev", "e", 0, 0));
        listAlphabet.add(new TopicItem(8, "Ê", null, "ee_up", "ee", null, 0, 0));
        listAlphabet.add(new TopicItem(9, null, "F", "f_up", null, "f", 0, 0));
        listAlphabet.add(new TopicItem(10, "G", "G", "g_up", "gv", "ge", 0, 0));
        listAlphabet.add(new TopicItem(11, "H", "H", "h_up", "hv", "he", 0, 0));
        listAlphabet.add(new TopicItem(12, "I", "I", "i_up", "iv", "ie", 0, 0));
        listAlphabet.add(new TopicItem(13, null, "J", "j_up", null, "j", 0, 0));
        listAlphabet.add(new TopicItem(14, "K", "K", "k_up", "kv", "ke", 0, 0));
        listAlphabet.add(new TopicItem(15, "L", "L", "l_up", "lv", "le", 0, 0));
        listAlphabet.add(new TopicItem(16, "M", "M", "m_up", "mv", "me", 0, 0));
        listAlphabet.add(new TopicItem(17, "N", "N", "n_up", "nv", "ne", 0, 0));
        listAlphabet.add(new TopicItem(18, "O", "O", "o_up", "ov", "oe", 0, 0));
        listAlphabet.add(new TopicItem(19, "Ô", null, "oo_up", "oo", null, 0, 0));
        listAlphabet.add(new TopicItem(20, "Ơ", null, "ow_up", "ow", null, 0, 0));
        listAlphabet.add(new TopicItem(21, "P", "P", "p_up", "pv", "pe", 0, 0));
        listAlphabet.add(new TopicItem(22, "Q", "Q", "q_up", "qv", "qe", 0, 0));
        listAlphabet.add(new TopicItem(23, "R", "R", "r_up", "rv", "re", 0, 0));
        listAlphabet.add(new TopicItem(24, "S", "S", "s_up", "sv", "se", 0, 0));
        listAlphabet.add(new TopicItem(25, "T", "T", "t_up", "tv", "te", 0, 0));
        listAlphabet.add(new TopicItem(26, "U", "U", "u_up", "uv", "ue", 0, 0));
        listAlphabet.add(new TopicItem(27, "Ư", null, "uw_up", "uw", null, 0, 0));
        listAlphabet.add(new TopicItem(28, "V", "V", "v_up", "vv", "ve", 0, 0));
        listAlphabet.add(new TopicItem(29, null, "W", "w_up", null, "w", 0, 0));
        listAlphabet.add(new TopicItem(30, "X", "X", "x_up", "xv", "xe", 0, 0));
        listAlphabet.add(new TopicItem(31, "Y", "Y", "y_up", "yv", "ye", 0, 0));
        listAlphabet.add(new TopicItem(32, null, "Z", "z_up", "zv", "ze", 0, 0));
        for (int i = 0; i < listAlphabet.size(); i++) {
            if (i == 1 || i == 2) {
                listVietAlphabet.add(listAlphabet.get(i));
            } else if (i == 6 || i == 8) {
                listVietAlphabet.add(listAlphabet.get(i));
            } else if (i == 19 || i == 20) {
                listVietAlphabet.add(listAlphabet.get(i));
            } else if (i == 27) {
                listVietAlphabet.add(listAlphabet.get(i));
            } else if (i == 9 || i == 13) {
                listEnAlphabet.add(listAlphabet.get(i));
            } else if (i == 29 || i == 32) {
                listEnAlphabet.add(listAlphabet.get(i));
            } else {
                listVietAlphabet.add(listAlphabet.get(i));
                listEnAlphabet.add(listAlphabet.get(i));
            }
        }
    }

    private void prepareAndStartGame() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int c = metrics.heightPixels;
        int w = metrics.widthPixels;
        btnSoundGame = getView().findViewById(R.id.btnSoundGame);
        GameView gameView = getView().findViewById(R.id.gameView);
        Button btnBack = getView().findViewById(R.id.btnBack);
        Random r = new Random();
        ArrayList<Bitmap> listBalloon = new ArrayList<>();
        listBalloon.add(BitmapFactory.decodeResource(getResources(), R.drawable.aqua));
        listBalloon.add(BitmapFactory.decodeResource(getResources(), R.drawable.teal_blue));
        listBalloon.add(BitmapFactory.decodeResource(getResources(), R.drawable.yellow));
        listBalloon.add(BitmapFactory.decodeResource(getResources(), R.drawable.purple));
        listBalloon.add(BitmapFactory.decodeResource(getResources(), R.drawable.mageta));
        mGameEngine = new GameEngine(getActivity(), gameView, 4);
        mGameEngine.setSoundManager(getMainActivity().getSoundManager());
        listImg = DBHelper.getInstance(this.getActivity().getApplicationContext()).getImageGame();
        if (Global.getLanguage() == Global.VN) {
            listImg.addAll(listVietAlphabet);
        } else {
            listImg.addAll(listEnAlphabet);
        }
        ArrayList<TopicItem> listRandom = new ArrayList<>();
        ArrayList<Bitmap> listImgBitmap = new ArrayList<>();
        Random random = new Random();

        for (int j = 0; j < 30; j++) {
            int i = random.nextInt(listImg.size());
            listRandom.add(listImg.get(i));
        }


        for (int k = 0; k < listRandom.size(); k++)
            try {
                String fileGameImagesPath = Global.pathGamePopballoon.getCanonicalFile() + "/" + listRandom.get(k).getImg_name() + ".png";
                Bitmap data = BitmapFactory.decodeFile(fileGameImagesPath);
                bmp = listBalloon.get(r.nextInt(listBalloon.size()));
                if (data == null) {
                    listImgBitmap.add(data);
                    listRandom.get(k).setBitmap(bmp);
                } else {
                    listImgBitmap.add(data);
                    int sizeY =  data.getHeight();
                    int sizeX = data.getWidth() * sizeY / data.getHeight();
                    bmp = listBalloon.get(r.nextInt(listBalloon.size()));
                    Bitmap bmpBong = Bitmap.createScaledBitmap(data, sizeX, sizeY, false);
                    //Tao bitmap overlay
                    bmpOverLay = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
                    Paint paint = new Paint();
                    Paint paint2 = new Paint();
                    paint.setFlags(Paint.ANTI_ALIAS_FLAG);
                    paint2.setAlpha(80);
                    Canvas canvas = new Canvas(bmpOverLay);
                    canvas.drawBitmap(bmpBong, (bmp.getWidth() - bmpBong.getWidth()) / 2.0f, (bmp.getHeight() - bmpBong.getHeight()) / 2.0f, paint);
                    canvas.drawBitmap(bmp, new Matrix(), paint2);
                    listRandom.get(k).setBitmap(bmpOverLay);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        btnSoundGame.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                getMainActivity().getSoundManager().toggleMusicStatus();
                updateSoundAndMusicButtons();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                pauseGameAndShowPauseDialog();
            }
        });
        new GameController(mGameEngine, GameFragment.this, listRandom, listImgBitmap).addToGameEngine(mGameEngine, 2);
        mGameEngine.startGame();

        InputManager inputManager = (InputManager) getActivity().getSystemService(Context.INPUT_SERVICE);
        inputManager.registerInputDeviceListener(GameFragment.this, null);
        gameView.postInvalidate();

        for (
                int i = 0;
                i < DUMMY_OBJECT_POOL_SIZE; i++) {
            mDummyObjectPool.add(new DummyObject(mGameEngine));
        }

        getView().findViewById(R.id.gameView).setOnTouchListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGameEngine.isRunning()) {
            pauseGameAndShowPauseDialog();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGameEngine.stopGame();
        InputManager inputManager = (InputManager) getActivity().getSystemService(Context.INPUT_SERVICE);
        inputManager.unregisterInputDeviceListener(this);
    }

    @Override
    public boolean onBackPressed() {
        if (mGameEngine.isRunning() && !mGameEngine.isPaused()) {
            pauseGameAndShowPauseDialog();
            return true;
        }
        return super.onBackPressed();
    }

    private void pauseGameAndShowPauseDialog() {
        if (mGameEngine.isPaused()) {
            return;
        }
        mGameEngine.pauseGame();
        PauseDialog dialog = new PauseDialog(getMainActivity());
        dialog.setListener(this);
        showDialog(dialog);
    }

    public void resumeGame() {
        mGameEngine.resumeGame();
    }

    @Override
    public void exitGame() {
        mGameEngine.stopGame();
        getMainActivity().navigateBack();

    }

    @Override
    public void onInputDeviceAdded(int deviceId) {

    }

    @Override
    public void onInputDeviceRemoved(int deviceId) {
        if (mGameEngine.isRunning()) {
            pauseGameAndShowPauseDialog();
        }
    }

    @Override
    public void onInputDeviceChanged(int deviceId) {

    }

    @Override
    public synchronized boolean onTouch(View v, MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN ||
                event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
            if (!mDummyObjectPool.isEmpty()) {
                DummyObject dummy = mDummyObjectPool.remove(0);
                dummy.init(event);
                dummy.addToGameEngine(mGameEngine, 0);
            }
            return true;
        }
        return false;
    }

    public class DummyObject extends Sprite {

        private long mTotalMilis;

        public DummyObject(GameEngine gameEngine) {
            super(gameEngine, BitmapFactory.decodeResource(getResources(), R.drawable.touch_visual), BodyType.Circular)
            ;
        }


        @Override
        public void startGame(GameEngine gameEngine) {

        }

        @Override
        public void onRemovedFromGameEngine() {
            super.onRemovedFromGameEngine();
            mDummyObjectPool.add(this);
        }

        @Override
        public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
            mTotalMilis += elapsedMillis;
            if (mTotalMilis > 200) {
                gameEngine.removeGameObject(this);
            }
        }

        public void init(MotionEvent event) {
            mX = event.getX(event.getActionIndex());
            mY = event.getY(event.getActionIndex());
            mTotalMilis = 0;
        }
    }

    public void updateSoundAndMusicButtons() {
        SoundManager soundManager = this.getMainActivity().getSoundManager();
        boolean music = soundManager.getMusicStatus();
        if (music) {
            btnSoundGame.setBackgroundResource(R.drawable.music_on_btn);
        } else {
            btnSoundGame.setBackgroundResource(R.drawable.music_off_btn);
        }
    }
}
