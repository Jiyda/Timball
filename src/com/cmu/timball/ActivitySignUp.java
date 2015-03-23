package com.cmu.timball;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;




import org.json.JSONObject;







import com.cmu.timball.libraries.UserFunctions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivitySignUp extends Activity  {
	/** Called when the activity is first created. */

	private EditText edtxtEmail, edtxtpassword,edtxtusername;
    private Button btnSignUp;
    String email, pass,user;
	UserFunctions userFunctions;

	Global_data gda;
	Context cntxt;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_signup);
	
		userFunctions= new UserFunctions();
		edtxtEmail = (EditText) findViewById(R.id.as_editTextEmailUse);
		edtxtpassword = (EditText) findViewById(R.id.as_editTextPassUse);
		edtxtusername = (EditText) findViewById(R.id.as_editTextUser);
		btnSignUp = (Button) findViewById(R.id.as_btnSignup);
		
		gda = new Global_data();
		cntxt = this;
		
		btnSignUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				email=edtxtEmail.getText().toString().trim();
				pass=edtxtpassword.getText().toString().trim();
				user=edtxtusername.getText().toString().trim();
				if(!email.equalsIgnoreCase("") && !pass.equalsIgnoreCase("") && !user.equalsIgnoreCase("")){

					new signupAsyncTask().execute("");
					
				}


			}
		});
		

	}

	public class signupAsyncTask extends AsyncTask<String,Void,String> {

		ProgressDialog pd;
		@Override
		protected void onPreExecute() {

			pd = new ProgressDialog(ActivitySignUp.this);
			pd.setTitle("Registering..");
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}


		@Override
		protected String doInBackground(String... params) {
           	return userFunctions.signup(email, user, pass);
        }

		@Override
		protected void onPostExecute(String response) {
			
			if(pd!=null){
				pd.dismiss();
			}
			try{
				if(response!=null && !response.equalsIgnoreCase("")){
                              
					if(Integer.parseInt(response)>0){
						gda.savePreferences(gda.TAG_LOGIN, true, cntxt);	
						gda.savePreferences(gda.TAG_EMAIL, email, cntxt);
						gda.savePreferences(gda.TAG_LOCATION, "", cntxt);
						gda.savePreferences(gda.TAG_GAME_TYPE, "", cntxt);
					Toast.makeText(getBaseContext(), "registered successfully!", Toast.LENGTH_SHORT).show();
					Intent i = new Intent(ActivitySignUp.this, ActivityHome.class);
					startActivity(i);
					ActivitySignUp.this.finish();
					}
					
				}
				else{
					Toast.makeText(getBaseContext(), "username not available", Toast.LENGTH_SHORT).show();
				}
			}
			catch(Exception e){
				Toast.makeText(getBaseContext(), "something went wrong!", Toast.LENGTH_SHORT).show();
				
			}
    	}
	}
 
}
