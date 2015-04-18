package com.cmu.timball;

import com.cmu.timball.ActivityDetailPlace.JoingameAsyncTask;
import com.cmu.timball.libraries.UserFunctions;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityProfile extends ActionBarActivity {
	
	public final static String EXTRA_EMAIL = "email";
	private ActionBar actionbar;
	TextView lbl_email_id;
	Button btn_logout;
	Global_data gda;
	Context cntxt;
	
	private UserFunctions userFunction;
	ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		// Get ActionBar and set back button on actionbar
     	actionbar = getSupportActionBar();
     	actionbar.setDisplayHomeAsUpEnabled(true);  
     	
     	gda = new Global_data();
		cntxt = this;
		userFunction = new UserFunctions();
		
	
     	
     	
     	lbl_email_id = (TextView) findViewById(R.id.lbl_email_id);
     	
     	btn_logout = (Button) findViewById(R.id.btn_logout);
   
     	lbl_email_id.setText(gda.loadSavedPreferences_string(gda.TAG_EMAIL, cntxt));
     	
     	
     	btn_logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gda.savePreferences(gda.TAG_LOGIN, false, cntxt);
				gda.removePreferences(gda.TAG_EMAIL, cntxt);
				gda.removePreferences(gda.TAG_LOGIN, cntxt);
				gda.removePreferences(gda.TAG_GAME_TYPE, cntxt);
				gda.removePreferences(gda.TAG_LOCATION, cntxt);
				
				Intent i = new Intent(ActivityProfile.this, ActivityLogin2.class);
				startActivity(i);
				ActivityProfile.this.finish();
			}
		});
     	
     	
     	
     	
	}

	// Listener for option menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    	case android.R.id.home:
	    		
	    		// Previous page or exit
	    		onBackPressed();
	    		
	    		return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
super.onBackPressed();
		
		// Show transition when back button pressed
		overridePendingTransition (R.anim.open_main, R.anim.close_next);
		
	}


	
	public void list_of_joined_games(View view) {
	    Intent intent = new Intent(this, ActivityGamesJoined.class);
	    intent.putExtra(EXTRA_EMAIL, gda.loadSavedPreferences_string(gda.TAG_EMAIL, cntxt));
	    startActivity(intent);
	}
	
	public void report_user(View view) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("plain/text");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "jiyda@cmu.edu" });
		intent.putExtra(Intent.EXTRA_SUBJECT, "Report user " +  gda.loadSavedPreferences_string(gda.TAG_EMAIL, cntxt));
		intent.putExtra(Intent.EXTRA_TEXT, "I would like to report the User with the email address " + gda.loadSavedPreferences_string(gda.TAG_EMAIL, cntxt) + " because he/she [Please include the reason why you are reporting this user.]");
		startActivity(Intent.createChooser(intent, ""));
	
	}



}
