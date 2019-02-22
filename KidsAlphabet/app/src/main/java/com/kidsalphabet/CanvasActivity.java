package com.kidsalphabet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class CanvasActivity extends Activity 
{
	private ImageButton ibNext = null; 
	private ImageButton ibPrev = null;
	private char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	private int currentAlphabetIndex = 0;
	private final String ACTIVITY_NAME = "Canvas";	
	private CanvasView canvasView = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canvas);
        getCanvasView();
        enableImageButtonEvents();
        updateAlphabet(currentAlphabetIndex);
    }
    
    private void getCanvasView()
    {
    	canvasView = (CanvasView) findViewById(R.id.drawingCanvas);
    }
    
    private void changePencilColor(int color)
    {
    	if (canvasView != null)
    	{
    		canvasView.setLetterPaint(color);
    	}
    }
    
    private void changeLetter(String text)
    {
    	if (canvasView != null)
    	{
    		canvasView.setLetterText(text);
    	}
    }    
    
    private void enableImageButtonEvents()
    {
    	ImageButton btnBlue = (ImageButton) findViewById(R.id.btnBlue);
    	btnBlue.setOnClickListener(new OnClickListener() {
    	    public void onClick(View v) {
    	        changePencilColor(Color.rgb(0,178,238));  
    	    }
    	});  
    	
    	ImageButton btnGreen = (ImageButton) findViewById(R.id.btnGreen);
    	btnGreen.setOnClickListener(new OnClickListener() {
    	    public void onClick(View v) {
    	    	changePencilColor(Color.GREEN);
    	    }
    	});  
    	
    	ImageButton btnOrange = (ImageButton) findViewById(R.id.btnOrange);
    	btnOrange.setOnClickListener(new OnClickListener() {
    	    public void onClick(View v) {
    	    	changePencilColor(Color.rgb(255,128,0));      
    	    }
    	});
    	
    	ImageButton btnYellow = (ImageButton) findViewById(R.id.btnYellow);
    	btnYellow.setOnClickListener(new OnClickListener() {
    	    public void onClick(View v) {
    	    	changePencilColor(Color.YELLOW);
    	    }
    	});     
    	
        // Previous Image Button  
        ibPrev = (ImageButton) findViewById(R.id.prevButton);
          
          if (ibPrev != null)
          {
          	ibPrev.setOnClickListener(new OnClickListener() {

          	     @Override
          	     public void onClick(View v) 
          	     {
  					currentAlphabetIndex--;
  					
  					if (currentAlphabetIndex < 0)
  					{
  						currentAlphabetIndex = 25;
  					}
  					
  					updateAlphabet(currentAlphabetIndex);
            	     }
          	    });
          }
          
          // Next Image Button        
          ibNext = (ImageButton) findViewById(R.id.nextButton);
          
          if (ibNext !=null)
          {
  	        ibNext.setOnClickListener(new OnClickListener() {
  	        	
  	        	@Override
  				public void onClick(View view)
  				{
  					currentAlphabetIndex++;
  					
  					if (currentAlphabetIndex > 25)
  					{
  						currentAlphabetIndex = 0;
  					}
  					
  					updateAlphabet(currentAlphabetIndex);
  				}
  			});        
          }    	
    }
    
    private void updateAlphabet(int index)
    {
    	// This is the current alphabet
    	char currentAlphabet = alphabet[index];
    	String text = String.valueOf(Character.toUpperCase(currentAlphabet)) + " " + currentAlphabet;
    	changeLetter(text);
    }
}
