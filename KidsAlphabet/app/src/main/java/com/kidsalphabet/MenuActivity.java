package com.kidsalphabet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuActivity extends Activity 
{
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        enableButtonEvents();
    }
    
    private void enableButtonEvents()
    {
    	Button btnLearn = (Button) findViewById(R.id.btnLearn);
    	btnLearn.setOnClickListener(new OnClickListener() {
    	    public void onClick(View v) {
    	        Intent learnIntent = new Intent(MenuActivity.this, MainActivity.class);
    	        startActivity(learnIntent);
    	    }
    	});  
    	
    	Button btnRecollect = (Button) findViewById(R.id.btnRecollect);
    	btnRecollect.setOnClickListener(new OnClickListener() {
    	    public void onClick(View v) {
    	        Intent recollectIntent = new Intent(MenuActivity.this, RecollectActivity.class);
    	        startActivity(recollectIntent);
    	    }
    	});  
    	
    	Button btnQuiz = (Button) findViewById(R.id.btnQuiz);
    	btnQuiz.setOnClickListener(new OnClickListener() {
    	    public void onClick(View v) {
    	        Intent quizIntent = new Intent(MenuActivity.this, QuizActivity.class);
    	        startActivity(quizIntent);
    	    }
    	});      	
    	
    	Button btnSlate = (Button) findViewById(R.id.btnSlate);
    	btnSlate.setOnClickListener(new OnClickListener() {
    	    public void onClick(View v) {
    	        Intent canvasIntent = new Intent(MenuActivity.this, CanvasActivity.class);
    	        startActivity(canvasIntent);
    	    }
    	});      	
    }
    
}
