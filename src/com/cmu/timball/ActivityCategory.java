/**
    * File        : ActivityCategory.java
    * App name    : Perkutut
    * Version     : 1.2.0
    * Created     : 01/19/14
    
    * Created by Taufan Erfiyanto & Cahaya Pangripta Alam on 11/24/13.
    * Copyright (c) 2013 pongodev. All rights reserved.
    */

package com.cmu.timball;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;

import com.cmu.timball.ads.Ads;
import com.cmu.timball.fragments.FragmentCategoryList;
import com.cmu.timball.utils.Utils;
import com.google.android.gms.ads.AdView;
import com.cmu.timball.R;

public class ActivityCategory extends ActionBarActivity implements 
	FragmentCategoryList.OnCategoryListSelectedListener{

	// Create an instance of ActionBar, adView and Utils
	private ActionBar actionbar;
	private AdView adView;
	private Utils utils;
	
	protected Fragment mFrag;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		// Declare object of Utils class
		utils = new Utils(this);
		
		// connect view objects and xml ids
		adView = (AdView)this.findViewById(R.id.adView);
								
		// Get ActionBar and set back button on actionbar
		actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
						
		// Connect with fragment
		FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
		mFrag = new FragmentCategoryList();
		t.replace(R.id.frame_content, mFrag);
		t.commit();
		
 		/* CHECK_PLAY_SERV = 1 means Google Play services version on the device 
	    supports the version of the client library you are using */
 		if(utils.loadPreferences(utils.CHECK_PLAY_SERV)==1){
 			
 			// Check the connection
 			if(utils.isNetworkAvailable()){
	            // Condition for admob (0=gone, 1=visible)
	            if(utils.paramAdmob==1){
	            	
	            	adView.setVisibility(View.VISIBLE);
	            	// load ads
		            Ads.loadAds(adView);
	            }
 			}
 		}
	
	}

	@Override
	public void onCategoryListSelected(String mCategoryId, String mCategoryName) {
		// TODO Auto-generated method stub
		
		// Call ActivityPlaceList
		Intent i = new Intent(this, ActivityPlaceList.class);
		i.putExtra(utils.EXTRA_CATEGORY_ID, mCategoryId);
		i.putExtra(utils.EXTRA_CATEGORY_NAME, mCategoryName);
		startActivity(i);	
		
		// Show transition when list selected
		overridePendingTransition (R.anim.open_next, R.anim.close_main);
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

	// Listener when back button pressed
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		// Show transition when back button pressed
		overridePendingTransition (R.anim.open_main, R.anim.close_next);
		
	}
	
}
