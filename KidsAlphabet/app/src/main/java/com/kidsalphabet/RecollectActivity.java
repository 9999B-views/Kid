package com.kidsalphabet;

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class RecollectActivity extends Activity implements TextToSpeech.OnInitListener
{
	private char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	private int currentAlphabetIndex = 0;
	private final String ACTIVITY_NAME = "Recollect";
	// Navigation Buttons
	private ImageButton ibNext = null; 
	private ImageButton ibPrev = null;
	// Image Views
	private ImageView ballonIcon = null; 
	private ImageView questionIcon = null; 
	private TextView tvBottomCaption = null;
	private TextToSpeech tts = null;
	private boolean isTtsActive = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recollect);
   
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
    	tvBottomCaption = (TextView) findViewById(R.id.bottomCaption);
    	tvBottomCaption.setClickable(true);
    	tvBottomCaption.setVisibility(View.INVISIBLE);
    	tvBottomCaption.setOnClickListener(new OnClickListener() {

      	     @Override
      	     public void onClick(View v) 
      	     {
      	    	speakWord(String.valueOf(tvBottomCaption.getText()));
        	 }
      	    });     	    	
    	
    	ballonIcon = (ImageView) findViewById(R.id.balloon_icon); 
    	ballonIcon.setClickable(true);
    	ballonIcon.setOnClickListener(new OnClickListener() {

	   	     @Override
	   	     public void onClick(View v) 
	   	     {
	   	    	speakAlphabet(currentAlphabetIndex);
	     	 }
	   	    });    	
   	
    	questionIcon = (ImageView) findViewById(R.id.question_icon);
    	questionIcon.setClickable(true);
    	questionIcon.setOnClickListener(new OnClickListener() {

      	     @Override
      	     public void onClick(View v) 
      	     {
      	        // Convert question into apple, speak apple and make bottom caption visible.
      	    	showImageForQuestion(currentAlphabetIndex);
      	    	speakWord(String.valueOf(tvBottomCaption.getText()));
      	    	tvBottomCaption.setVisibility(View.VISIBLE);
        	 }
      	    });     	

    }
    
    private void showImageForQuestion(int index)
    {
    	// This is the current alphabet
    	char currentAlphabet = alphabet[index];

    	switch (currentAlphabet)
    	{
    			case 'a': 
    				       questionIcon.setImageResource(R.drawable.letter_a);
			    			break;
    			case 'b': 
							 questionIcon.setImageResource(R.drawable.letter_b);
							 break;
    			case 'c': 
							 questionIcon.setImageResource(R.drawable.letter_c);
							 break;
    			case 'd': 
							 questionIcon.setImageResource(R.drawable.letter_d);
							 break;    			
    			case 'e': 
							 questionIcon.setImageResource(R.drawable.letter_e);
							 break;    			
    			case 'f':
							 questionIcon.setImageResource(R.drawable.letter_f);
							 break;    			
    			case 'g': 
							 questionIcon.setImageResource(R.drawable.letter_g);
							 break;    			
    			case 'h':
							 questionIcon.setImageResource(R.drawable.letter_h);
							 break;     			
    			case 'i':
							 questionIcon.setImageResource(R.drawable.letter_i);
							 break;     			
    			case 'j': 
							 questionIcon.setImageResource(R.drawable.letter_j);
							 break;     			
    			case 'k':
							 questionIcon.setImageResource(R.drawable.letter_k);
							 break;     			
    			case 'l': 
							 questionIcon.setImageResource(R.drawable.letter_l);
							 break;     			
    			case 'm':
							 questionIcon.setImageResource(R.drawable.letter_m);
							 break; 
    			case 'n':
							 questionIcon.setImageResource(R.drawable.letter_n);
							 break; 
    			case 'o': 
							 questionIcon.setImageResource(R.drawable.letter_o);
							 break;     			
    			case 'p':
							 questionIcon.setImageResource(R.drawable.letter_p);
							 break;     			
    			case 'q': 
							 questionIcon.setImageResource(R.drawable.letter_q);
							 break;     			
    			case 'r': 
							 questionIcon.setImageResource(R.drawable.letter_r);
							 break;     			
    			case 's':
							 questionIcon.setImageResource(R.drawable.letter_s);
							 break;     			
    			case 't':
							 questionIcon.setImageResource(R.drawable.letter_t);
							 break;     		
    			case 'u':
							 questionIcon.setImageResource(R.drawable.letter_u);
							 break;     			
    			case 'v':
							 questionIcon.setImageResource(R.drawable.letter_v);
							 break;     			
    			case 'w':
							 questionIcon.setImageResource(R.drawable.letter_w);
							 break;     			
    			case 'x': 
							 questionIcon.setImageResource(R.drawable.letter_x);
							 break;     			
    			case 'y':
							 questionIcon.setImageResource(R.drawable.letter_y);
							 break;     			
    			case 'z':
							 questionIcon.setImageResource(R.drawable.letter_z);
							 break;     			
    	}    	
    	
    }
    
    private void updateAlphabet(int index)
    {
    	// This is the current alphabet
    	char currentAlphabet = alphabet[index];
    	tvBottomCaption.setVisibility(View.INVISIBLE);
    	questionIcon.setImageResource(R.drawable.question);
    	
    	switch (currentAlphabet)
    	{
    			case 'a': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.a)));  
			    			ballonIcon.setImageResource(R.drawable.balloon_a);
			    			break;
    			case 'b': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.b)));  
							 ballonIcon.setImageResource(R.drawable.balloon_b);
							 break;
    			case 'c': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.c)));
							 ballonIcon.setImageResource(R.drawable.balloon_c);
							 break;
    			case 'd': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.d)));
							 ballonIcon.setImageResource(R.drawable.balloon_d);
							 break;    			
    			case 'e': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.e)));
							 ballonIcon.setImageResource(R.drawable.balloon_e);
							 break;    			
    			case 'f': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.f)));
							 ballonIcon.setImageResource(R.drawable.balloon_f);
							 break;    			
    			case 'g': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.g)));
							 ballonIcon.setImageResource(R.drawable.balloon_g);
							 break;    			
    			case 'h': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.h)));
							 ballonIcon.setImageResource(R.drawable.balloon_h);
							 break;     			
    			case 'i': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.i)));
							 ballonIcon.setImageResource(R.drawable.balloon_i);
							 break;     			
    			case 'j': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.j)));
							 ballonIcon.setImageResource(R.drawable.balloon_j);
							 break;     			
    			case 'k': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.k)));
							 ballonIcon.setImageResource(R.drawable.balloon_k);
							 break;     			
    			case 'l': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.l)));
							 ballonIcon.setImageResource(R.drawable.balloon_l);
							 break;     			
    			case 'm': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.m)));
							 ballonIcon.setImageResource(R.drawable.balloon_m);
							 break; 
    			case 'n': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.n)));
							 ballonIcon.setImageResource(R.drawable.balloon_n);
							 break; 
    			case 'o': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.o)));
							 ballonIcon.setImageResource(R.drawable.balloon_o);
							 break;     			
    			case 'p': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.p)));
							 ballonIcon.setImageResource(R.drawable.balloon_p);
							 break;     			
    			case 'q': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.q)));
							 ballonIcon.setImageResource(R.drawable.balloon_q);
							 break;     			
    			case 'r': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.r)));
							 ballonIcon.setImageResource(R.drawable.balloon_r);
							 break;     			
    			case 's': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.s)));
							 ballonIcon.setImageResource(R.drawable.balloon_s);
							 break;     			
    			case 't': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.t)));
							 ballonIcon.setImageResource(R.drawable.balloon_t);
							 break;     		
    			case 'u': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.u)));
							 ballonIcon.setImageResource(R.drawable.balloon_u);
							 break;     			
    			case 'v': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.v)));
							 ballonIcon.setImageResource(R.drawable.balloon_v);
							 break;     			
    			case 'w': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.w)));
							 ballonIcon.setImageResource(R.drawable.balloon_w);
							 break;     			
    			case 'x': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.x)));
							 ballonIcon.setImageResource(R.drawable.balloon_x);
							 break;     			
    			case 'y': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.y)));
							 ballonIcon.setImageResource(R.drawable.balloon_y);
							 break;     			
    			case 'z': tvBottomCaption.setText(String.valueOf(getResources().getString(R.string.z)));
							 ballonIcon.setImageResource(R.drawable.balloon_z);
							 break;     			
    	}
    }    
}
