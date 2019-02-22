package vn.devpro.devprokidorigin.controllers.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class DrawView extends View {
    private Paint paint1 = new Paint();
    private Paint paint2 = new Paint();
    private Paint paint3 = new Paint();
    private float x1, x2, y1, y2;
    private float x21, x22, y21, y22;
    private float x31, x32, y31, y32;

    public void Init() {
        setBackgroundColor(Color.TRANSPARENT);
        paint1.setStrokeCap(Paint.Cap.ROUND);
        paint1.setColor(Color.YELLOW);
        paint1.setStrokeWidth(20);
        setWillNotDraw(false);

        paint2.setColor(Color.BLUE);
        paint2.setStrokeCap(Paint.Cap.ROUND);
        paint2.setStrokeWidth(20);
        setWillNotDraw(false);

        paint3.setColor(Color.RED);
        paint3.setStrokeWidth(20);
        paint3.setStrokeCap(Paint.Cap.ROUND);
        setWillNotDraw(false);
    }

    public DrawView(Context context) {
        super(context);
        Init();
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Init();
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init();
    }

    public void setLine1(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public void setLine2(float x1, float y1, float x2, float y2) {
        this.x21 = x1;
        this.y21 = y1;
        this.x22 = x2;
        this.y22 = y2;
    }

    public void setLine3(float x1, float y1, float x2, float y2) {
        this.x31 = x1;
        this.y31 = y1;
        this.x32 = x2;
        this.y32 = y2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(x1, y1, x2, y2, paint1);
        canvas.drawLine(x21, y21, x22, y22, paint2);
        canvas.drawLine(x31, y31, x32, y32, paint3);
    }
}