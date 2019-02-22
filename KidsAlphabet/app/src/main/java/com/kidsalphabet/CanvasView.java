package com.kidsalphabet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CanvasView extends View 
{
    private static final float RADIUS = 20;
    private float x = 30;
    private float y = 30;
    private float initialX;
    private float initialY;
    private float offsetX;
    private float offsetY;
    private Paint backgroundPaint;
    private Paint myPaint;
    private Paint letterPaint;
    private String letterText = "B b";

    public CanvasView(Context context, AttributeSet attrs) 
    {
        super(context, attrs);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.GRAY);

        myPaint = new Paint();
        myPaint.setColor(Color.GREEN);
        myPaint.setAntiAlias(true);
        
        letterPaint = new Paint();
        letterPaint.setColor(Color.BLACK);
        letterPaint.setStyle(Style.FILL);
        letterPaint.setTextSize(300);
    }
    
    public void setLetterPaint(int color)
    {
    	myPaint.setColor(color);
    }
    
    public void setLetterSize(int size)
    {
    	letterPaint.setTextSize(size);
    }    

    public void setLetterText(String text)
    {
    	letterText = text;
    }  
    
    @Override
    public boolean onTouchEvent(MotionEvent event) 
    {
        int action = event.getAction();
        
        switch(action) 
        {
	        // Need to remember where the initial starting point
	        // center is of our Dot and where our touch starts from
	        case MotionEvent.ACTION_DOWN:

	            initialX = x;
	            initialY = y;
	            offsetX = event.getX();
	            offsetY = event.getY();
	            break;
	            
	        case MotionEvent.ACTION_MOVE:
	        	
	        case MotionEvent.ACTION_UP:
	
			case MotionEvent.ACTION_CANCEL:
			            x = initialX + event.getX() - offsetX;
			            y = initialY + event.getY() - offsetY;
			     break;
        }
        
        return(true);
    }

    @Override
    public void draw(Canvas canvas) 
    {
    	super.onDraw(canvas);
    	
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        canvas.drawRect(0, 0, width, height, backgroundPaint);
        canvas.drawText(letterText, (width/4), height/2, letterPaint);
        canvas.drawCircle(x, y, RADIUS, myPaint);
        
        invalidate();
    }
}
