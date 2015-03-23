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

public class ActivityLogin extends Activity  {
	/** Called when the activity is first created. */

	private EditText edtxtEmail, edtxtpassword;
    private Button btnLogin,btnSignUp;
    String email, pass;
	UserFunctions userFunctions;
	Global_data gda;
	Context cntxt;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
	
		userFunctions= new UserFunctions();
		edtxtEmail = (EditText) findViewById(R.id.al_editTextEmailUse);
		edtxtpassword = (EditText) findViewById(R.id.al_editTextPassUse);
		btnLogin = (Button) findViewById(R.id.al_btnLogin);
		btnSignUp = (Button) findViewById(R.id.al_btnSignup);
	
		gda = new Global_data();
		cntxt = this;
				
		btnSignUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
		
				Intent i = new Intent(ActivityLogin.this, ActivitySignUp.class);
				startActivity(i);
				ActivityLogin.this.finish();
			}
		});
	
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				email=edtxtEmail.getText().toString().trim();
				pass=edtxtpassword.getText().toString().trim();
				
				if(!email.equalsIgnoreCase("") && !pass.equalsIgnoreCase("")){

					new loginAsyncTask().execute("");
					
				}


			}
		});
		

	}

	public class loginAsyncTask extends AsyncTask<String,Void,String> {

		ProgressDialog pd;
		@Override
		protected void onPreExecute() {

			pd = new ProgressDialog(ActivityLogin.this);
			pd.setTitle("Authenticating..");
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}


		@Override
		protected String doInBackground(String... params) {
           	return userFunctions.login(email, pass);
        }

		@Override
		protected void onPostExecute(String response) {
			
			if(pd!=null){
				pd.dismiss();
			}
			try{
				
				if(response!=null && Integer.parseInt(response)>0){
					gda.savePreferences(gda.TAG_LOGIN, true, cntxt);  
					gda.savePreferences(gda.TAG_EMAIL, email, cntxt);
					gda.savePreferences(gda.TAG_LOCATION, "", cntxt);
					gda.savePreferences(gda.TAG_GAME_TYPE, "", cntxt);
				//	edtxtEmail.setText("");
				//	edtxtpassword.setText("");
					Intent i = new Intent(ActivityLogin.this, ActivityHome.class);
					startActivity(i);
					ActivityLogin.this.finish();
				}
				else{
					Toast.makeText(getBaseContext(), "wrong email or password", Toast.LENGTH_SHORT).show();
				}
			}
			catch(Exception e){
				Toast.makeText(getBaseContext(), "something went wrong!", Toast.LENGTH_SHORT).show();
				
			}

			
			
		}
	}
	
	@Override
	public void onBackPressed() {
		Intent homeIntent = new Intent(Intent.ACTION_MAIN);
		homeIntent.addCategory( Intent.CATEGORY_HOME );
		homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(homeIntent);
		finish();
	}
 
}
