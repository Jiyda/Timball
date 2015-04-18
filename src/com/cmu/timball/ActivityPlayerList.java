package com.cmu.timball;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cmu.timball.adapters.AdapterPlayerList;
import com.cmu.timball.libraries.UserFunctions;
import com.cmu.timball.utils.Utils;
import com.google.android.gms.identity.intents.AddressConstants.Extras;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityPlayerList extends Activity  {
	/** Called when the activity is first created. */
	private Utils utils;
	
	private ListView listview;
   private String list,loc_id,email,game_type;
   Global_data gda;
   UserFunctions userFunction;
   int dec_players=0;
   Context cntxt;
   private AdapterPlayerList Adapter;
   private TextView lblNoResult;
  List<String> playerList;
   String emailToRemove;
   boolean isUpdated=false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_list);
	
	    listview=(ListView) findViewById(R.id.listView1);
	    lblNoResult	= (TextView) findViewById(R.id.lblNoResult);
		
	    
	   utils=new Utils(ActivityPlayerList.this);
	    gda 		 = new Global_data();
	    userFunction = new UserFunctions();
			cntxt=this;
	    Bundle b = getIntent().getExtras();
	    if(b!=null){
	    	if(b.containsKey("email")) {
	    		this.email = b.getString("email"); 
	    	}
	    	if(b.containsKey("list")) {
	    		this.list = b.getString("list"); 

	    		if(!list.equalsIgnoreCase("")){
	    			String []listarray=list.split(",");
	    			playerList=new ArrayList<String>(Arrays.asList(listarray));
	    			
	    		
	    			if(email.equalsIgnoreCase(gda.loadSavedPreferences_string(gda.TAG_EMAIL, cntxt))){
	    				 Adapter= new AdapterPlayerList(ActivityPlayerList.this, playerList,true);
	 	    			
	    			}	
	    			else{
                    Adapter= new AdapterPlayerList(ActivityPlayerList.this, playerList,false);
	    			}
	    			
	    			listview.setAdapter(Adapter);
	    			lblNoResult.setVisibility(View.GONE);
	        		listview.setVisibility(View.VISIBLE);
	    		}
	    		else{
	    			lblNoResult.setVisibility(View.VISIBLE);
	        		listview.setVisibility(View.GONE);
	    		}
	    }
	    	
	    	if(b.containsKey("loc_id")) {
	    		this.loc_id = b.getString("loc_id"); 
	    	}
	    	if(b.containsKey("game_type")) {
	    		this.game_type = b.getString("game_type"); 
	    	}
	    	if(b.containsKey("dec_players")) {
	    		this.dec_players= b.getInt("dec_players"); 
	    	}
	    	
	    else
	    {
	    	this.finish();
	    }
	}
		
	    listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
		
				if(email.equalsIgnoreCase(gda.loadSavedPreferences_string(gda.TAG_EMAIL, cntxt))){
					if(email.equalsIgnoreCase(gda.loadSavedPreferences_string(gda.TAG_EMAIL, cntxt))&& playerList.get(pos).equalsIgnoreCase(email)){
		    			
						new AlertDialog.Builder(ActivityPlayerList.this)
					    .setTitle("Please use the leave game function on detail screen")
					    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) { 
					            // continue with delete
					        }
					     })
					  
					     .show();
	    			
	    			}
					else{
				      ShowPopup(pos);
					}
					
				}
				
			}
		});
		

	}
	private void ShowPopup(final int itempos) {
		AlertDialog alertDialog = new AlertDialog.Builder(ActivityPlayerList.this)
				.create();
		alertDialog.setTitle("would you like to remove this user?");
		// alertDialog.setMessage("Would you like to try again?");
		

		alertDialog.setButton2("Yes",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					emailToRemove=playerList.get(itempos);
					new LeavegameAsyncTask().execute("");

					}
				});
		alertDialog.setButton3("No",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						
					}
				});
		// alertDialog.setIcon(R.drawable.icon);
		alertDialog.show();
	}

	public class LeavegameAsyncTask extends AsyncTask<String,Void,String> {

		ProgressDialog pd;
		String game_string, location_string;
		
		@Override
		protected void onPreExecute() {

			pd = new ProgressDialog(ActivityPlayerList.this);
			pd.setTitle("Removing from Game..");
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}


		@Override
		protected String doInBackground(String... params) {
			
			dec_players++;
		//	String email = gda.loadSavedPreferences_string(gda.TAG_EMAIL, cntxt);
			String players = String.valueOf(dec_players);
			
			game_string = gda.loadSavedPreferences_string(gda.TAG_GAME_TYPE, cntxt);
			
			
			if(game_string.trim().length()>0){
				
				// Iterate over the list of Games joined (their ids). The game_string
				// is of the format "L0004, L0003, L0005"
				List<String> items = Arrays.asList(game_string.split("\\s*,\\s*"));
				
				
					game_string = "";
				for(int i=0;i<items.size();i++){ 
					if(!items.get(i).trim().equals(loc_id)){ 
						 game_string +=   items.get(i)+",";
						} 
				}
				
				if (game_string == ""){
					game_string = "";
				}
				//remove trailing comma
				game_string = game_string.replaceAll(",$", "");
				
				
				
			}
			
			
			
			
           	return userFunction.leavegame2(emailToRemove, game_string, players, loc_id, game_string);
        }

		@Override
		protected void onPostExecute(String response) {
			
			if(pd!=null){
				pd.dismiss();
			}
			try{
				if(response!=null && Integer.parseInt(response)>0){
					
					gda.savePreferences(gda.TAG_LOCATION, location_string, cntxt);
					gda.savePreferences(gda.TAG_GAME_TYPE, game_string, cntxt);
					Toast.makeText(getBaseContext(), "Player Removed", Toast.LENGTH_SHORT).show();
				//	Toast.makeText(getBaseContext(), dec_players + " Players Needed Now ", Toast.LENGTH_SHORT).show();
					playerList.remove(emailToRemove);
					Adapter.notifyDataSetChanged();
					isUpdated=true;
					
				}
				
			}
			catch(Exception e){
				Toast.makeText(getBaseContext(), "something went wrong!", Toast.LENGTH_SHORT).show();
				
			}

			
			
		}
	}
	@Override
	public void onBackPressed() {
//		if(!isUpdated){
//		super.onBackPressed();
//		}
//		else{
		Intent i = new Intent(ActivityPlayerList.this, ActivityDetailPlace.class);
		 i.putExtra(utils.EXTRA_LOCATION_ID,loc_id);
		 startActivity(i); 
		 finish();
	     overridePendingTransition (R.anim.open_main, R.anim.close_next);
//		}
	
	}
	
	 	
		
		
}
