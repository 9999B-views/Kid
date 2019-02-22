package com.barak.ball;

import android.media.SoundPool;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

public class Ball {

    public int x, y, xx , yy, speedX, speedY;
    private int screenH, screenW;
    public ImageView image;

    public static int size;
    public static int countTouch = 0;

    public Ball(MainActivity mainActivity, int screenH, int screenW,
                int size, int minSpeed, int maxSpeed, int rad_x,int rad_y){

        image = new ImageView(mainActivity);
        this.screenH = screenH;
        this.screenW = screenW;
        this.size = size;

        this.xx = rad_x;
        this.yy = rad_y;

        this.x = rad_x-size/2;
        this.y = rad_y-size/2;

        this.speedX = minSpeed + (int) (Math.random() * (maxSpeed - minSpeed) + .5);
        this.speedY = minSpeed + (int) (Math.random() * (maxSpeed - minSpeed) + .5);
        image.setX(x);
        image.setY(y);
        mainActivity.addContentView(image,
                new RelativeLayout.LayoutParams(size, size));
        image.setImageResource(R.drawable.ball_);
    }

    public void move(){
        x += speedX;
        y += speedY;
        image.setX(x);
        image.setY(y);
        this.xx = x + size/2;
        this.yy = y + size/2;
        if (xx <= size/2 || xx >= screenW - size/2){
            speedX = - speedX;
        }
        if (yy <= size/2 || yy >= screenH - size/2)
            speedY = - speedY;




    }

    public static boolean touch(List<Ball> balls, float x, float y,
                             SoundPool soundPool, int streamSound){
        for (int i = 0; i < balls.size(); i++){
            if ((balls.get(i).x <= x) && (balls.get(i).x + size >= x) &&
                    (balls.get(i).y <= y) && (balls.get(i).y + size >= y)){
                balls.get(i).image.setImageAlpha(0);
                countTouch++;
                balls.remove(i);
                return true;
            }
        }
        return false;
    }



}
