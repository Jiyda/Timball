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
import com.google.android.gms.internal.gd;
import com.google.android.gms.internal.ll;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityLogin2 extends Activity  {
	/** Called when the activity is first created. */

	private EditText edtxtEmail, edtxtpassword;
    private Button btnLogin;
    String email, pass;
	UserFunctions userFunctions;
	Global_data gda;
	private TextView txtMsg;
	Context cntxt;
	private LinearLayout llMsg;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login2);
	
		userFunctions= new UserFunctions();
		edtxtEmail = (EditText) findViewById(R.id.al_editTextEmailUse);
		edtxtpassword = (EditText) findViewById(R.id.al_editTextPassUse);
		btnLogin = (Button) findViewById(R.id.al_btnLogin);
		txtMsg= (TextView) findViewById(R.id.al2_txtmsg);
		llMsg = (LinearLayout) findViewById(R.id.al2_llmsg);
		
		gda = new Global_data();
		cntxt = this;
				
		
	
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

	public class loginAsyncTask extends AsyncTask<String,Void,JSONObject> {

		ProgressDialog pd;
		@Override
		protected void onPreExecute() {

			txtMsg.setText("");
			llMsg.setVisibility(View.INVISIBLE);
			pd = new ProgressDialog(ActivityLogin2.this);
			pd.setTitle("Authenticating..");
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}


		@Override
		protected JSONObject doInBackground(String... params) {
           	return userFunctions.login(email, pass);
        }

		@Override
		protected void onPostExecute(JSONObject response) {
			String error="";
			String image_url="";
		
			if(pd!=null){
				pd.dismiss();
			}
//			if(response!=null ){
//				gda.savePreferences(gda.TAG_LOGIN, true, cntxt);  
//				gda.savePreferences(gda.TAG_EMAIL, email, cntxt);
//				gda.savePreferences(gda.TAG_LOCATION, "", cntxt);
//				gda.savePreferences(gda.TAG_GAME_TYPE, "", cntxt);
//			//	edtxtEmail.setText("");
//			//	edtxtpassword.setText("");
//				Intent i = new Intent(ActivityLogin.this, ActivityHome.class);
//				startActivity(i);
//				ActivityLogin.this.finish();
//			}
//			else{
//				Toast.makeText(getBaseContext(), "wrong email or password", Toast.LENGTH_SHORT).show();
//			}
			
			
			if(response!=null ){
				try{
					error = response.getString("error");
					
					if(error.equalsIgnoreCase("false")){
					    image_url=response.getString("image_url");
						gda.savePreferences(gda.TAG_LOGIN, true, cntxt);  
						gda.savePreferences(gda.TAG_EMAIL, email, cntxt);
						gda.savePreferences(gda.TAG_LOCATION, "", cntxt);
						gda.savePreferences(gda.TAG_GAME_TYPE, "", cntxt);
						gda.savePreferences(gda.TAG_USER_IMAGE_URL, image_url, cntxt);
					//	edtxtEmail.setText("");
					//	edtxtpassword.setText("");
						Intent i = new Intent(ActivityLogin2.this, ActivityHome.class);
						startActivity(i);
						ActivityLogin2.this.finish();

						
					}
					else{
						
						txtMsg.setText( "YOUR EMAIL OR PASSWORD WAS ENTERED INCORRECTLY");
						llMsg.setVisibility(View.VISIBLE);
						edtxtEmail.setTextColor(Color.parseColor("#c0372e"));
						edtxtpassword.setTextColor(Color.parseColor("#c0372e"));
				//		Toast.makeText(getBaseContext(), "wrong email or password", Toast.LENGTH_SHORT).show();
					}
					
				}
				catch(Exception e){
					txtMsg.setText( "SOMETHING WENT WRONG!");
					llMsg.setVisibility(View.VISIBLE);
				//	Toast.makeText(getBaseContext(), "something went wrong!", Toast.LENGTH_SHORT).show();
					
				}

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
