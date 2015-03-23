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

	private ActionBar actionbar;
	TextView lbl_email_id1,lbl_game_type1,lbl_location1,lbl_email_id,lbl_game_type,lbl_location, cl1,cl2,cl3;
	Button btn_logout;
	Global_data gda;
	Context cntxt;
	String game_t="", location ="";
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
		
		lbl_email_id1 = (TextView) findViewById(R.id.lbl_email_id1);
     	lbl_game_type1 = (TextView) findViewById(R.id.lbl_game_type1);
     	lbl_location1 = (TextView) findViewById(R.id.lbl_location1);
     	
     	lbl_email_id = (TextView) findViewById(R.id.lbl_email_id);
     	lbl_game_type = (TextView) findViewById(R.id.lbl_game_type);
     	lbl_location = (TextView) findViewById(R.id.lbl_location);
     	
     	cl1  = (TextView) findViewById(R.id.cl1);
     	cl2  = (TextView) findViewById(R.id.cl2);
     	cl3  = (TextView) findViewById(R.id.cl3);
     	
     	btn_logout = (Button) findViewById(R.id.btn_logout);
     	
     	game_t = gda.loadSavedPreferences_string(gda.TAG_GAME_TYPE, cntxt);
     	location = gda.loadSavedPreferences_string(gda.TAG_LOCATION, cntxt);
     	lbl_email_id1.setText("Email Address");
     	cl1.setText(": ");
     	lbl_email_id.setText(gda.loadSavedPreferences_string(gda.TAG_EMAIL, cntxt));
     	
     	
     	btn_logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gda.savePreferences(gda.TAG_LOGIN, false, cntxt);
				gda.removePreferences(gda.TAG_EMAIL, cntxt);
				gda.removePreferences(gda.TAG_LOGIN, cntxt);
				gda.removePreferences(gda.TAG_GAME_TYPE, cntxt);
				gda.removePreferences(gda.TAG_LOCATION, cntxt);
				
				Intent i = new Intent(ActivityProfile.this, ActivityLogin.class);
				startActivity(i);
				ActivityProfile.this.finish();
			}
		});
     	
     	if(location.trim().length()>0){
     		location = location.replaceAll("," , "\n");
     		lbl_location1.setText("Location");
     		cl3.setText(": ");
     		lbl_location.setText(location);
     	}
     	
     	if(game_t.trim().length()==0){
     		new GetgameAsyncTask().execute("");
     	}else{
     		game_t = game_t.replaceAll("," , "\n");
     		lbl_game_type1.setText("Game Type");
     		lbl_game_type.setText(game_t);
     		cl2.setText(": ");
     	}
     	
     	
	}

	public class GetgameAsyncTask extends AsyncTask<String,Void,String> {

		
		
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(ActivityProfile.this);
			pd.setTitle("Loading Game & Location..");
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected String doInBackground(String... params) {
			
			String email = gda.loadSavedPreferences_string(gda.TAG_EMAIL, cntxt);
			return userFunction.getgame(email);
        }

		@Override
		protected void onPostExecute(String response) {
			try{
				if(response!=null && !(response.equals("0"))){
					gda.savePreferences(gda.TAG_GAME_TYPE, response, cntxt);
					response = response.replaceAll("," , "\n");
					lbl_game_type1.setText("Game Type");
					lbl_game_type.setText(response);
					cl2.setText(": ");
				}
				
			}
			catch(Exception e){	}
			new GetlocationAsyncTask().execute("");
		}
	}
	
	public class GetlocationAsyncTask extends AsyncTask<String,Void,String> {
		@Override
		protected String doInBackground(String... params) {
			String email = gda.loadSavedPreferences_string(gda.TAG_EMAIL, cntxt);
			return userFunction.getlocation(email);
        }

		@Override
		protected void onPostExecute(String response) {
			if(pd!=null){ pd.dismiss(); }
			
			try{
				if(response!=null && !(response.equals("0"))){
					gda.savePreferences(gda.TAG_LOCATION, response, cntxt);
					response = response.replaceAll("," , "\n");
		     		lbl_location1.setText("Location");
		     		lbl_location.setText(response);
		     		cl3.setText(": ");
		     		
				}
				
			}
			catch(Exception e){	}			
		}
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

}
