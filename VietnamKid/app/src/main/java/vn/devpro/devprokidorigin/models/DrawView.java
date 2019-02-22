package vn.devpro.devprokidorigin.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import vn.devpro.devprokidorigin.R;

public class DrawView extends View implements View.OnTouchListener {
    private List<Point> points = new ArrayList<Point>();
    private Paint paint;
    private Bitmap bitmap;
    private Canvas actualCanvas;
    private int lineWidth;
    private final int color = Color.BLUE;

    public DrawView(Context context) {
        super(context);
        init();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public void setLine(int lineWidth){
        this.lineWidth = lineWidth;
        init();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        setWillNotDraw(false);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(lineWidth);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setFilterBitmap(true);
        paint.setDither(true); // Dithering affects how colors that are higher precision than the device are down-sample
    }

    @Override
    public void onDraw(Canvas canvas) {

        //Rect rect = new Rect(0, 0, getWidth(), getHeight());
        if (bitmap == null) {
            Bitmap immutableBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.transparent_bg);
            Bitmap resize = Bitmap.createScaledBitmap(immutableBitmap, getWidth(), getHeight(), true);
            this.bitmap = resize.copy(Bitmap.Config.ARGB_8888, true);
            actualCanvas = new Canvas(bitmap);
        }


        for (Point point : points) {
            if (points.indexOf(point) == 0) {
                actualCanvas.drawCircle(point.x , point.y, (int) (lineWidth / 2), paint);
            } else {
                Point last = points.get(points.indexOf(point) - 1);
                actualCanvas.drawLine(last.x, last.y, point.x, point.y, paint);
            }
        }
        canvas.drawBitmap(bitmap, 0  , 0, paint);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void clear() {
        bitmap = null;
        actualCanvas = null;
        points.clear();
        invalidate();
    }

    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            points.clear();
        } else {
            Point point = new Point();
            point.x = event.getX();
            point.y = event.getY();
            points.add(point);
        }
        Log.i("POINT", "toa do ve la" + event.getX() + "&" + event.getY());
        invalidate();
        return true;
    }

    class Point {
        float x, y;

        @Override
        public String toString() {
            return x + ", " + y;
        }
    }
}
