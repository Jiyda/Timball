/**
    * File        : ActivityAbout.java
    * App name    : Perkutut
    * Version     : 1.2.0
    * Created     : 24/11/14

    * Created by Taufan Erfiyanto & Cahaya Pangripta Alam on 11/24/13.
    * Copyright (c) 2013 pongodev. All rights reserved.
    */

package com.cmu.timball;

import com.cmu.timball.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ActivityAbout extends ActionBarActivity implements OnClickListener{
   
	// Create an instance of ActionBar
	private ActionBar actionbar;
	
	// Declare view objects
	private Button btnShare, btnRate;
			
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
            
     	// Get ActionBar and set back button on actionbar
     	actionbar = getSupportActionBar();
     	actionbar.setDisplayHomeAsUpEnabled(true);
     	
     	// Connect view objects and xml ids
		btnShare	= (Button) findViewById(R.id.btnShare);
		btnRate		= (Button) findViewById(R.id.btnRate);
		
		btnShare.setOnClickListener(this);
		btnRate.setOnClickListener(this);
	            
    }
	
	// Listener for option menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    	case android.R.id.home:
	    		// Previous page or exit
	    		finish();
	    		
	    		// Show transition when push back button in actionbar
	    		overridePendingTransition (R.anim.open_main, R.anim.close_next);
	    		return true;
	    		
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btnShare:
			// Share this app to other application
			Intent iShare = new Intent(Intent.ACTION_SEND);
			iShare.setType("text/plain");
			iShare.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
			iShare.putExtra(Intent.EXTRA_TEXT, getString(R.string.gplay_url));
			startActivity(Intent.createChooser(iShare, getString(R.string.share_via)));
			
			// Show transition when button pressed
			overridePendingTransition (R.anim.open_next, R.anim.close_main);
			
			break;
			
		case R.id.btnRate:
			// Rate this app in Play Store
			Intent iRate = new Intent(Intent.ACTION_VIEW);
			iRate.setData(Uri.parse(getString(R.string.gplay_url)));
			startActivity(iRate);
			
			// Show transition when push button
			overridePendingTransition (R.anim.open_next, R.anim.close_main);
			
			break;
			
		default:
			break;
		}
		// TODO Auto-generated method stub		
	}	

	// Listener when back button pressed
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		// Show transition when back button pressed
		overridePendingTransition (R.anim.open_main, R.anim.close_next);
		
	}
	
}