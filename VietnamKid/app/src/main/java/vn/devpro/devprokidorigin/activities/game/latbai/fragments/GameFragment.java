package vn.devpro.devprokidorigin.activities.game.latbai.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.activities.game.latbai.commom.Shared;
import vn.devpro.devprokidorigin.activities.game.latbai.even.BackGame;
import vn.devpro.devprokidorigin.activities.game.latbai.even.StarNewGame;
import vn.devpro.devprokidorigin.activities.game.latbai.even.engine.FlipDownCardEven;
import vn.devpro.devprokidorigin.activities.game.latbai.even.engine.GameWonPairEven;
import vn.devpro.devprokidorigin.activities.game.latbai.even.engine.HidePairCardEven;
import vn.devpro.devprokidorigin.activities.game.latbai.ui.BoardView;
import vn.devpro.devprokidorigin.activities.game.latbai.ui.PopupManager;
import vn.devpro.devprokidorigin.activities.game.latbai.uitls.Clock;
import vn.devpro.devprokidorigin.activities.game.latbai.uitls.FontLoader;
import vn.devpro.devprokidorigin.activities.game.latbai.uitls.FontLoader.Font;
import vn.devpro.devprokidorigin.activities.game.latbai.uitls.Utils_PairGame;
import vn.devpro.devprokidorigin.models.entity.game.latbai.Game;
import vn.devpro.devprokidorigin.utils.Global;

/**
 * Created by admin on 3/31/2018.
 */

public class GameFragment extends BaseFragment implements View.OnClickListener {
    private BoardView mBoardView;
    private TextView mTime;
    private ImageView mTimeImage;
    private Button select_theme, btnBacktoMenu;
    private Dialog builder;
    private RoundedImageView img_alpha, img_number, img_color, img_fruid, img_animal, img_rau, img_shape, img_app, img_transp,
            img_garden, img_clothing, img_school, img_kitchen, img_tool, img_sport, img_music, img_body, img_job;
    private TextView tv_alphabet, tv_number, tv_color, tv_fruid, tv_animal, tv_rau, tv_shape, tv_app, tv_transp, tv_school,
            tv_garden, tv_clothing, tv_kitchen, tv_tool, tv_sport, tv_music, tv_body, tv_job;
    private Button btnClose;
    private ConstraintLayout constraintLayout, con1;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.manhinhgame_fragment, container, false);
        view.setClipChildren(false);
        ((ViewGroup) view.findViewById(R.id.game_board)).setClipChildren(false);
        mTime = (TextView) view.findViewById(R.id.time_bar_text);
        select_theme = (Button) view.findViewById(R.id.select_theme);
        btnBacktoMenu = view.findViewById(R.id.btnBackToMenuPairGame);
        btnBacktoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shared.eventBus.notify(new BackGame());
            }
        });

        select_theme.setOnClickListener(this);
        select_theme.setClickable(true);

        mTimeImage = (ImageView) view.findViewById(R.id.time_bar_image);
        FontLoader.setTypeface(Shared.context, new TextView[]{mTime}, Font.QUICKSAND_BOLD);

        mBoardView = BoardView.fromXml(getActivity().getApplicationContext(), view);
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.game_container);
        frameLayout.addView(mBoardView);
        frameLayout.setClipChildren(false);

        buildBoard();
        Shared.eventBus.listen(FlipDownCardEven.TYPE, this);
        Shared.eventBus.listen(HidePairCardEven.TYPE, this);
        Shared.eventBus.listen(GameWonPairEven.TYPE, this);

        return view;
    }

    @NonNull
    public void CreateCustomDialog() {
        builder = new Dialog(this.getContext(), R.style.DialogTheme);
        View viewDialog = getLayoutInflater().inflate(R.layout.brother_master_dat, null);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int PX_SCR_W = displayMetrics.widthPixels;
        int PX_SCR_H = displayMetrics.heightPixels;
        builder.getWindow().setLayout(PX_SCR_W - 150, PX_SCR_W / 2);
        builder.setContentView(viewDialog);

        con1 = viewDialog.findViewById(R.id.con1);
//         tìm hình ảnh
        img_alpha = viewDialog.findViewById(R.id.img_alphabet);
        img_color = viewDialog.findViewById(R.id.imageView_color);
        img_number = viewDialog.findViewById(R.id.imageView_number);
        img_fruid = viewDialog.findViewById(R.id.img_fruid);
        img_animal = viewDialog.findViewById(R.id.img_animal);
        img_rau = viewDialog.findViewById(R.id.img_vegertable);
        img_shape = viewDialog.findViewById(R.id.img_shape);
        img_app = viewDialog.findViewById(R.id.img_app);
        img_transp = viewDialog.findViewById(R.id.img_transp);
        img_garden = viewDialog.findViewById(R.id.img_garden);
        img_clothing = viewDialog.findViewById(R.id.img_clothing);
        img_school = viewDialog.findViewById(R.id.img_school);
        img_kitchen = viewDialog.findViewById(R.id.img_kitchen);
        img_sport = viewDialog.findViewById(R.id.img_sport);
        img_tool = viewDialog.findViewById(R.id.img_tool);
        img_music = viewDialog.findViewById(R.id.img_music);
        img_body = viewDialog.findViewById(R.id.img_body);
        img_job = viewDialog.findViewById(R.id.img_job);

        constraintLayout = viewDialog.findViewById(R.id.constraintLayout);
        // tìm text view
        tv_alphabet = viewDialog.findViewById(R.id.tv_alphabet);
        tv_number = viewDialog.findViewById(R.id.tvnumber);
        tv_color = viewDialog.findViewById(R.id.tv_color);
        tv_fruid = viewDialog.findViewById(R.id.tv_fruid);
        tv_rau = viewDialog.findViewById(R.id.tv_vegertable);
        tv_shape = viewDialog.findViewById(R.id.tv_shape);
        tv_animal = viewDialog.findViewById(R.id.tv_animal);
        tv_app = viewDialog.findViewById(R.id.tv_app);
        tv_transp = viewDialog.findViewById(R.id.tv_transp);
        tv_garden = viewDialog.findViewById(R.id.tv_garden);
        tv_clothing = viewDialog.findViewById(R.id.tv_clothing);
        tv_school = viewDialog.findViewById(R.id.tv_school);
        tv_kitchen = viewDialog.findViewById(R.id.tv_kitchen);
        tv_sport = viewDialog.findViewById(R.id.tv_sport);
        tv_tool = viewDialog.findViewById(R.id.tv_tool);
        tv_music = viewDialog.findViewById(R.id.tv_music);
        tv_body = viewDialog.findViewById(R.id.tv_body);
        tv_job = viewDialog.findViewById(R.id.tv_job);

        img_animal.setOnClickListener(topicOnclick);
        img_number.setOnClickListener(topicOnclick);
        img_alpha.setOnClickListener(topicOnclick);
        img_color.setOnClickListener(topicOnclick);
        img_fruid.setOnClickListener(topicOnclick);
        img_tool.setOnClickListener(topicOnclick);
        img_rau.setOnClickListener(topicOnclick);
        img_shape.setOnClickListener(topicOnclick);
        img_app.setOnClickListener(topicOnclick);
        img_transp.setOnClickListener(topicOnclick);
        img_garden.setOnClickListener(topicOnclick);
        img_clothing.setOnClickListener(topicOnclick);
        img_school.setOnClickListener(topicOnclick);
        img_kitchen.setOnClickListener(topicOnclick);
        img_sport.setOnClickListener(topicOnclick);
        img_music.setOnClickListener(topicOnclick);
        img_body.setOnClickListener(topicOnclick);
        img_job.setOnClickListener(topicOnclick);


        btnClose = viewDialog.findViewById(R.id.button);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });


        builder.setCancelable(false);
        setTopicTitile();
    }

    private void setTopicTitile(){
        //TODO set Title for topic indialog by language
        if (Global.getLanguage() == Global.VN) {
            tv_alphabet.setText( Shared.listTopicName.get( 0 ).getTitle_vn() );
            tv_number.setText( Shared.listTopicName.get( 1 ).getTitle_vn() );
            tv_color.setText( Shared.listTopicName.get( 2 ).getTitle_vn() );
            tv_fruid.setText( Shared.listTopicName.get( 3 ).getTitle_vn() );
            tv_animal.setText( Shared.listTopicName.get( 4 ).getTitle_vn() );
            tv_rau.setText( Shared.listTopicName.get( 5 ).getTitle_vn() );
            tv_shape.setText( Shared.listTopicName.get( 6 ).getTitle_vn() );
            tv_app.setText( Shared.listTopicName.get( 7 ).getTitle_vn() );
            tv_transp.setText( Shared.listTopicName.get( 8 ).getTitle_vn() );
            tv_garden.setText( Shared.listTopicName.get( 9 ).getTitle_vn() );
            tv_clothing.setText( Shared.listTopicName.get( 10 ).getTitle_vn() );
            tv_school.setText( Shared.listTopicName.get( 11 ).getTitle_vn() );
            tv_kitchen.setText( Shared.listTopicName.get( 12 ).getTitle_vn() );
            tv_sport.setText( Shared.listTopicName.get( 13 ).getTitle_vn() );
            tv_tool.setText( Shared.listTopicName.get( 14 ).getTitle_vn() );
            tv_music.setText( Shared.listTopicName.get( 15 ).getTitle_vn() );
            tv_body.setText( Shared.listTopicName.get( 16 ).getTitle_vn() );
            tv_job.setText( Shared.listTopicName.get( 17 ).getTitle_vn() );
        }else {
            tv_alphabet.setText( Shared.listTopicName.get( 0 ).getTitle_en() );
            tv_number.setText( Shared.listTopicName.get( 1 ).getTitle_en() );
            tv_color.setText( Shared.listTopicName.get( 2 ).getTitle_en() );
            tv_fruid.setText( Shared.listTopicName.get( 3 ).getTitle_en() );
            tv_animal.setText( Shared.listTopicName.get( 4 ).getTitle_en() );
            tv_rau.setText( Shared.listTopicName.get( 5 ).getTitle_en() );
            tv_shape.setText( Shared.listTopicName.get( 6 ).getTitle_en() );
            tv_app.setText( Shared.listTopicName.get( 7 ).getTitle_en() );
            tv_transp.setText( Shared.listTopicName.get( 8 ).getTitle_en() );
            tv_garden.setText( Shared.listTopicName.get( 9 ).getTitle_en() );
            tv_clothing.setText( Shared.listTopicName.get( 10 ).getTitle_en() );
            tv_school.setText( Shared.listTopicName.get( 11 ).getTitle_en() );
            tv_kitchen.setText( Shared.listTopicName.get( 12 ).getTitle_en() );
            tv_sport.setText( Shared.listTopicName.get( 13 ).getTitle_en() );
            tv_tool.setText( Shared.listTopicName.get( 14 ).getTitle_en() );
            tv_music.setText( Shared.listTopicName.get( 15 ).getTitle_en() );
            tv_body.setText( Shared.listTopicName.get( 16 ).getTitle_en() );
            tv_job.setText( Shared.listTopicName.get( 17 ).getTitle_en() );
        }
    }

    private void changeTopic(int i) {
        Shared.engine.setCurrentopicId(i);
        Utils_PairGame.saveDefaultTopicId(i);
        Shared.eventBus.notify(new StarNewGame(1, i));

        builder.dismiss();
    }

    View.OnClickListener topicOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_alphabet:
                    changeTopic(1);
                    break;
                case R.id.imageView_number:
                    changeTopic(2);
                    break;
                case R.id.imageView_color:
                    changeTopic(3);
                    break;
                case R.id.img_animal:
                    changeTopic(5);
                    break;
                case R.id.img_fruid:
                    changeTopic(4);
                    break;
                case R.id.img_vegertable:
                    changeTopic(6);
                    break;
                case R.id.img_shape:
                    changeTopic(7);
                    break;
                case R.id.img_app:
                    changeTopic(8);
                    break;
                case R.id.img_transp:
                    changeTopic(9);
                    break;
                case R.id.img_garden:
                    changeTopic(10);
                    break;
                case R.id.img_clothing:
                    changeTopic(11);
                    break;
                case R.id.img_school:
                    changeTopic(12);
                    break;
                case R.id.img_kitchen:
                    changeTopic(13);
                    break;
                case R.id.img_sport:
                    changeTopic(14);
                    break;
                case R.id.img_tool:
                    changeTopic(15);
                    break;
                case R.id.img_music:
                    changeTopic(16);
                    break;
                case R.id.img_body:
                    changeTopic(17);
                    break;
                case R.id.img_job:
                    changeTopic(18);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void onDestroy() {
        Shared.eventBus.unlisten(FlipDownCardEven.TYPE, this);
        Shared.eventBus.unlisten(HidePairCardEven.TYPE, this);
        Shared.eventBus.unlisten(GameWonPairEven.TYPE, this);
        super.onDestroy();
    }

    private void buildBoard() {
        Game game = Shared.engine.getActiveGame();
        if (game == null)return;
        int time = game.boardCauhinh.time;
        setTime(time);
        mBoardView.setBoard(game);

        startClock(time);
    }

    private void setTime(int time) {
        int min = time / 60;
        int sec = time - min * 60;
        mTime.setText(" " + String.format("%02d", min) + ":" + String.format("%02d", sec));
    }

    private void startClock(int sec) {
        Clock clock = Clock.getInstance();
        clock.startTimer(sec * 1000, 1000, new Clock.OnTimerCount() {

            @Override
            public void onTick(long millisUntilFinished) {
                setTime((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                setTime(0);
            }
        });
    }

    @Override
    public void onEvent(GameWonPairEven event) {
        mTime.setVisibility(View.GONE);
        mTimeImage.setVisibility(View.GONE);
        PopupManager.showPopupWon(event.gameState);
        select_theme.setClickable(false);
    }

    @Override
    public void onEvent(FlipDownCardEven event) {
        mBoardView.flipDownAll();
    }

    @Override
    public void onEvent(HidePairCardEven event) {
        mBoardView.hideCards(event.id1, event.id2);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_theme:
                CreateCustomDialog();
                builder.show();
        }
    }
}

