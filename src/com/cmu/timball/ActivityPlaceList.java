/**
    * File        : ActivityPlaceList.java
    * App name    : Perkutut
    * Version     : 1.2.0
    * Created     : 01/19/14

    * Created by Taufan Erfiyanto & Cahaya Pangripta Alam on 11/24/13.
    * Copyright (c) 2013 pongodev. All rights reserved.
    */

package com.cmu.timball;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;

import com.cmu.timball.ads.Ads;
import com.cmu.timball.fragments.FragmentPlaceList;
import com.cmu.timball.utils.Utils;
import com.google.android.gms.ads.AdView;
import com.cmu.timball.R;

public class ActivityPlaceList extends ActionBarActivity implements FragmentPlaceList.OnListSelectedListener{
	
	// Create an instance of ActionBar, AdView and Utils
	private ActionBar actionbar;
	private AdView adView;
	private Utils utils;
	
	// Declare variable to store data
	private String mCategoryId, mCategoryName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		// Declare object of Utils class
		utils = new Utils(this);
		
		// connect view objects and xml ids
		adView = (AdView)this.findViewById(R.id.adView);
				
		// Get intent Data from ActivityCategory
        Intent i = getIntent();
        mCategoryId   = i.getStringExtra(utils.EXTRA_CATEGORY_ID);
        mCategoryName = i.getStringExtra(utils.EXTRA_CATEGORY_NAME);
        
     	// Get ActionBar and set back button on actionbar
 		actionbar = getSupportActionBar();
 		actionbar.setDisplayHomeAsUpEnabled(true);	
 		actionbar.setTitle(mCategoryName);
     	
     	/* In case this activity was started with special instructions from an Intent,
        Pass the Intent's extras to the fragment as arguments */
       Bundle bundle = new Bundle();
       bundle.putString(utils.EXTRA_CATEGORY_ID, mCategoryId);
       bundle.putString(utils.EXTRA_CATEGORY_NAME, mCategoryName);
       FragmentPlaceList fragObjPlaceList = new FragmentPlaceList();
       fragObjPlaceList.setArguments(bundle);

       // Add the fragment to the 'fragment_container' FrameLayout
       getSupportFragmentManager().beginTransaction()
       	.add(R.id.frame_content, fragObjPlaceList).commit();
       
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

	// Listener For list selected
	@Override
	public void onListSelected(String mIdSelected) {
		// TODO Auto-generated method stub
		
		// Call ActivityDetailPlace
		Intent i = new Intent(this, ActivityDetailPlace.class);
		i.putExtra(utils.EXTRA_LOCATION_ID, mIdSelected);
		startActivity(i);
		
		// Show transition when list selected
		overridePendingTransition (R.anim.open_next, R.anim.close_main);
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
