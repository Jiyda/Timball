package com.cmu.timball;

import com.koushikdutta.ion.Ion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ActivityWelcome extends Activity{

	private Button btnSignup,btnSignin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_welcome);
		ImageView gif = (ImageView) findViewById(R.id.gif);
		String link = "android.resource://"+getPackageName() + "/" + R.drawable.welcome;
		Ion.with(gif).centerCrop().load("http://i.giphy.com/j2IYbuDXpnfY4.gif");
		btnSignin= (Button) findViewById(R.id.aw_btnSignin);
		btnSignup= (Button) findViewById(R.id.aw_btnSignup);
		
		btnSignin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent i = new Intent(ActivityWelcome.this, ActivityLogin2.class);
				startActivity(i);
				finish();
				
			}
		});
		
		
        btnSignup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent i = new Intent(ActivityWelcome.this, ActivitySignUp2.class);
				startActivity(i);
				finish();
			}
		});
        
        
        
		
	}
}
