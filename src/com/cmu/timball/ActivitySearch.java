/**
    * File        : ActivitySearch.java
    * App name    : Perkutut
    * Version     : 1.2.0
    * Created     : 01/19/14

    * Created by Taufan Erfiyanto & Cahaya Pangripta Alam on 11/24/13.
    * Copyright (c) 2013 pongodev. All rights reserved.
    */

package com.cmu.timball;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cmu.timball.ads.Ads;
import com.cmu.timball.fragments.FragmentSearchList;
import com.cmu.timball.libraries.SuggestionProvider;
import com.cmu.timball.utils.Utils;
import com.google.android.gms.ads.AdView;
import com.cmu.timball.R;

public class ActivitySearch extends ActionBarActivity implements FragmentSearchList.OnListSelectedListener{
	
	// Create an instance of ActionBar, AdView and Utils
	private ActionBar actionbar;
	private AdView adView;
	private Utils utils;
	
	private String keyword = "";
	
	protected Fragment mFrag;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		// Declare object of Utils class
		utils = new Utils(this);
		
		// connect view objects and xml ids
		adView = (AdView)this.findViewById(R.id.adView);
		
		// Get ActionBar and set back button on actionbar
		actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle(getString(R.string.page_search));
		
		// Get the intent, verify the action and get the query
	    Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      keyword = intent.getStringExtra(SearchManager.QUERY);
	      SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
	                SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
	        suggestions.saveRecentQuery(keyword, null);
	    }
     	
	   Bundle bundle = new Bundle();
       bundle.putString(utils.EXTRA_KEYWORD, keyword);
       
       FragmentSearchList fragObjList = new FragmentSearchList();
       fragObjList.setArguments(bundle);

       // Add the fragment to the 'frame_content' FrameLayout
       getSupportFragmentManager().beginTransaction()
       	.add(R.id.frame_content, fragObjList).commit();
       
		
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
 			} else {
				Toast.makeText(this, getString(R.string.internet_alert), Toast.LENGTH_SHORT).show();
			}
 		}
	
	}

	// Listener for List Selected
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
