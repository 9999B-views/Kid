package com.barak.ball;

import android.content.Context;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Ball> balls = null;
    private int START_BALLS = 10;
    private int countBall = START_BALLS;

    private TextView textTimer, textScore, textLevel;
    private int screenH, screenW;
    private MyTimer timer = null;
    private SoundPool soundPool = null;
    private int streamSoundTuck = 0;
    private int speed = 10;
    private int streamSoundLose = 0;
    private int mLevel = 0;
    private int mScroe;
    private int caliber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(uiOptions);
        }

        setContentView(R.layout.activity_main);

        textTimer = findViewById(R.id.text_timer);
        textScore = findViewById(R.id.text_score);
        textLevel = findViewById(R.id.text_level);
        textLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timer == null) {
                    timer = new MyTimer(30000, 100);
                    timer.start();
                } else {
                    timer.cancel();
                    timer = null;
                }
            }
        });
        textLevel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                for (int i = balls.size() - 1; i > -1; i--) {
//                    balls.get(i).image.setImageAlpha(0);
                    ((ViewGroup) balls.get(i).image.getParent()).removeView(balls.get(i).image);
                    balls.remove(i);
                }
                if (timer != null) timer.cancel();
                timer = new MyTimer(60000, 10);
                createBalls();
                timer.start();

                return false;
            }
        });
        textScore.setText("Score:" + mScroe);
        textLevel.setText("Level:" + ++mLevel);
        WindowManager windowManager =
                (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        final Display display = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        if (Build.VERSION.SDK_INT >= 19) {
            display.getRealSize(outPoint);
        } else {
            display.getSize(outPoint);
        }
        if (outPoint.y > outPoint.x) {
            screenH = outPoint.y;
            screenW = outPoint.x;
        } else {
            screenH = outPoint.x;
            screenW = outPoint.y;
        }

        createSoundPool();

        balls = new ArrayList<>();
        createBalls();
        timer = new MyTimer(60000, 50);
        timer.start();
    }

    private void createSoundPool() {
        soundPool = new SoundPool(1, AudioAttributes.CONTENT_TYPE_MUSIC, 1);
        streamSoundTuck = soundPool.load(this, R.raw.tuck, 1);
        streamSoundLose = soundPool.load(this, R.raw.lose, 3);
    }

    private void createBalls() {

        for (int i = 0; i < countBall; i++) {
            int rad_x = (int) (Math.random() * screenW * .85)+100;
            int rad_y = (int) (Math.random() * screenH * .85)+100;
//            boolean checks = true;
//            while (checks) {
//                checks = false;
//                for (int j = 0; j < balls.size(); j++) {
//                    if (distance(rad_x,rad_y,balls.get(j).xx,balls.get(j).yy)<caliber){
//                        checks=true;
//                    }
//                }
//            }

            balls.add(new Ball(MainActivity.this, screenH, screenW, 200,
                    10, 30,rad_x,rad_y));
        }

//        balls.add(new Ball(MainActivity.this, screenH, screenW, 200,
//                10, 20, 100, 100));
//        balls.add(new Ball(MainActivity.this, screenH, screenW, 200,
//                10, -10, 150, 150));
//        balls.add(new Ball(MainActivity.this, screenH, screenW, 200,
//                5, speed,1100,1000));

        caliber = balls.get(0).size;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x, y;
        x = event.getX();
        y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                boolean flag = Ball.touch(balls, x, y, soundPool, streamSoundTuck);
                if (flag) {
                    --countBall;
                    textScore.setText("Score:" + ++mScroe);
                    if (countBall == 0) {
                        START_BALLS++;
                        countBall = START_BALLS;
                        mScroe += countBall;
                        mLevel++;
                        textLevel.setText("Level:" + mLevel);
                        textScore.setText("Score:" + mScroe);
                        if (timer != null) timer.cancel();
                        timer = new MyTimer(30000, 100);
                        timer.start();
                        createBalls();
                    }
                }
                break;
        }
        return true;
    }

    public class MyTimer extends CountDownTimer {

        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            textTimer.setText(l / 1000 + "");
            moveBalls();
        }

        @Override
        public void onFinish() {
            timer.cancel();
            timer = null;
        }
    }

    private void moveBalls() {
        for (Ball ball : balls) {
            ball.move();
        }
        handleCollisions();
    }

    public void handleCollisions() {
        double xDist, yDist;
        for (int i = 0; i < balls.size(); i++) {
            Ball A = balls.get(i);
            for (int j = i + 1; j < balls.size(); j++) {
                Ball B = balls.get(j);
                xDist = A.xx - B.xx;
                yDist = A.yy - B.yy;
                double distSquared = xDist * xDist + yDist * yDist;
                //Check the squared distances instead of the the distances, same result, but avoids a square root.
                if (distSquared <= (caliber) * (caliber)) {
                    double xVelocity = B.speedX - A.speedX;
                    double yVelocity = B.speedY - A.speedY;
                    double dotProduct = xDist * xVelocity + yDist * yVelocity;
                    //Neat vector maths, used for checking if the objects moves towards one another.
                    if (dotProduct > 0) {
                        double collisionScale = dotProduct / distSquared;
                        double xCollision = xDist * collisionScale;
                        double yCollision = yDist * collisionScale;
                        //The Collision vector is the speed difference projected on the Dist vector,
                        //thus it is the component of the speed difference needed for the collision.
                        A.speedX += xCollision;
                        A.speedY += yCollision;
                        B.speedX -= xCollision;
                        B.speedY -= yCollision;
                    }
                }
            }
        }
    }

    public static double distance(float x1, float y1,
                                  float x2, float y2) {
        return Math.hypot(x1 - x2, y1 - y2);
    }
}
