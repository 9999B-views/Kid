package com.kidsalphabet;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener 
{
	private char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	private int currentAlphabetIndex = 0;
	private final String ACTIVITY_NAME = "Main";
	private TextView tvUpperCase = null;
	private TextView tvLowerCase = null;
	private TextView tvBottomCaption = null;
	private ImageButton ibNext = null; 
	private ImageButton ibPrev = null;
	private ImageView letterIcon = null; 
	private TextToSpeech tts = null;
	private boolean isTtsActive = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
   
        Log.i(ACTIVITY_NAME, "Passed setContentView");
        
        //Get the buttons and attach event handlers
        enableImageButtonEvents();
        initializeTTS();
        initializeAlphabet();
        updateAlphabet(currentAlphabetIndex);
        
        
        Log.i(ACTIVITY_NAME, "onCreate() complete");
    }
    
    private void enableImageButtonEvents()
    {
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
    
    private void initializeTTS()
    {
        // Initialize text-to-speech. This is an asynchronous operation.
        // The OnInitListener (second argument) is called after initialization completes.
        tts = new TextToSpeech(this, this);  // TextToSpeech.OnInitListener
    }
    
    // Implements TextToSpeech.OnInitListener.
    public void onInit(int status) 
    {
        // status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
        if (status == TextToSpeech.SUCCESS) 
        {
            // Set preferred language to US English.
            // Note that a language may not be available, and the result will indicate this.
            int result = tts.setLanguage(Locale.US);
            // Try this someday for some interesting results.
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) 
            {
               // Language data is missing or the language is not supported.
                Log.e(ACTIVITY_NAME, "Language is not available.");
            }
            else 
            {
            	isTtsActive = true;
            	setTextToSpeechUS();
            }
        } 
        else 
        {
            // Initialization failed.
            Log.e(ACTIVITY_NAME, "Could not initialize TextToSpeech.");
        }
    }    
    
    private void speakAlphabet(int currentIndex)
    {
    	if (tts != null && isTtsActive)
    	{
    	   char alphabet = this.alphabet[currentIndex];
    	   tts.speak(String.valueOf(alphabet),
    	            TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
    	            null);
    	}
    }
    
    private void speakWord(String word)
    {
    	if (tts != null && isTtsActive)
    	{
    	   tts.speak(String.valueOf(word),
    	            TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
    	            null);
    	}
    }    
    
    private void setTextToSpeechUS()
    {
    	if (tts != null)
    	{
	        if (tts.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_COUNTRY_AVAILABLE)
	        {
	        	tts.setLanguage(Locale.US);
	        }
    	}
    }
    
    @Override
    public void onDestroy() 
    {
        // Don't forget to shutdown!
        if (tts != null) 
        {
        	tts.stop();
        	tts.shutdown();
        }

        super.onDestroy();
    }    
    
    private void initializeAlphabet()
    {
    	tvUpperCase = (TextView) findViewById(R.id.tvUpperCase); 
    	tvUpperCase.setClickable(true);
    	tvUpperCase.setOnClickListener(new OnClickListener() {

	   	     @Override
	   	     public void onClick(View v) 
	   	     {
	   	    	magnify(tvUpperCase);
	   	    	speakAlphabet(currentAlphabetIndex);
	     	 }
	   	    });    	
    	
    	tvLowerCase = (TextView) findViewById(R.id.tvLowerCase);
    	tvLowerCase.setClickable(true);
    	tvLowerCase.setOnClickListener(new OnClickListener() {

      	     @Override
      	     public void onClick(View v) 
      	     {
      	    	magnify(tvLowerCase);
      	    	speakAlphabet(currentAlphabetIndex);
        	 }
      	    });        	
    	
    	
    	tvBottomCaption = (TextView) findViewById(R.id.bottomCaption);
    	tvBottomCaption.setClickable(true);
    	tvBottomCaption.setOnClickListener(new OnClickListener() {

      	     @Override
      	     public void onClick(View v) 
      	     {
      	    	speakWord(String.valueOf(tvBottomCaption.getText()));
        	 }
      	    });     	
    	
    	letterIcon = (ImageView) findViewById(R.id.letter_icon); 
    	letterIcon.setClickable(true);
    	
    	letterIcon.setOnClickListener(new OnClickListener() {

     	     @Override
     	     public void onClick(View v) 
     	     {
     	    	speakWord(String.valueOf(tvBottomCaption.getText()));
       	     }
     	 });      	
    }
    
    private void updateAlphabet(int index)
    {
    	// This is the current alphabet
    	char currentAlphabet = alphabet[index];
    	
    	// Upper case
    	tvUpperCase.setText(String.valueOf(Character.toUpperCase(currentAlphabet)));
    	
    	// Lower case
    	tvLowerCase.setText(String.valueOf(currentAlphabet));    	

    	switch (currentAlphabet)
    	{
    			case 'a': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.a)));  
			    			letterIcon.setImageResource(R.drawable.letter_a);
			    			break;
    			case 'b': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.b)));  
							 letterIcon.setImageResource(R.drawable.letter_b);
							 break;
    			case 'c': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.c)));
							 letterIcon.setImageResource(R.drawable.letter_c);
							 break;
    			case 'd': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.d)));
							 letterIcon.setImageResource(R.drawable.letter_d);
							 break;    			
    			case 'e': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.e)));
							 letterIcon.setImageResource(R.drawable.letter_e);
							 break;    			
    			case 'f': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.f)));
							 letterIcon.setImageResource(R.drawable.letter_f);
							 break;    			
    			case 'g': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.g)));
							 letterIcon.setImageResource(R.drawable.letter_g);
							 break;    			
    			case 'h': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.h)));
							 letterIcon.setImageResource(R.drawable.letter_h);
							 break;     			
    			case 'i': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.i)));
							 letterIcon.setImageResource(R.drawable.letter_i);
							 break;     			
    			case 'j': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.j)));
							 letterIcon.setImageResource(R.drawable.letter_j);
							 break;     			
    			case 'k': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.k)));
							 letterIcon.setImageResource(R.drawable.letter_k);
							 break;     			
    			case 'l': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.l)));
							 letterIcon.setImageResource(R.drawable.letter_l);
							 break;     			
    			case 'm': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.m)));
							 letterIcon.setImageResource(R.drawable.letter_m);
							 break; 
    			case 'n': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.n)));
							 letterIcon.setImageResource(R.drawable.letter_n);
							 break; 
    			case 'o': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.o)));
							 letterIcon.setImageResource(R.drawable.letter_o);
							 break;     			
    			case 'p': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.p)));
							 letterIcon.setImageResource(R.drawable.letter_p);
							 break;     			
    			case 'q': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.q)));
							 letterIcon.setImageResource(R.drawable.letter_q);
							 break;     			
    			case 'r': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.r)));
							 letterIcon.setImageResource(R.drawable.letter_r);
							 break;     			
    			case 's': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.s)));
							 letterIcon.setImageResource(R.drawable.letter_s);
							 break;     			
    			case 't': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.t)));
							 letterIcon.setImageResource(R.drawable.letter_t);
							 break;     		
    			case 'u': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.u)));
							 letterIcon.setImageResource(R.drawable.letter_u);
							 break;     			
    			case 'v': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.v)));
							 letterIcon.setImageResource(R.drawable.letter_v);
							 break;     			
    			case 'w': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.w)));
							 letterIcon.setImageResource(R.drawable.letter_w);
							 break;     			
    			case 'x': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.x)));
							 letterIcon.setImageResource(R.drawable.letter_x);
							 break;     			
    			case 'y': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.y)));
							 letterIcon.setImageResource(R.drawable.letter_y);
							 break;     			
    			case 'z': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.z)));
							 letterIcon.setImageResource(R.drawable.letter_z);
							 break;     			
    	}
    }

    private void magnify(TextView textView)
    {
    	Animation animate = AnimationUtils.loadAnimation(this, R.anim.magnify);
    	animate.reset();
    	textView.clearAnimation();
    	textView.startAnimation(animate);
    }
    
}