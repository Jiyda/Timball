package com.cmu.timball;

import java.io.File;
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
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;




import org.json.JSONObject;

















import com.cmu.timball.libraries.UserFunctions;
import com.google.android.gms.internal.im;
import com.koushikdutta.ion.Ion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

public class ActivitySignUp2 extends Activity {
	/** Called when the activity is first created. */

	private EditText edtxtEmail, edtxtpassword,edtxtusername;
    private Button btnSignUp;
    String email, pass,user,imagePath="";
	UserFunctions userFunctions;
	private ImageView img,imgAddImage;
	
	Global_data gda;
	Context cntxt;
	private static final int SELECT_PICTURE = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_signup2);
	
		userFunctions= new UserFunctions();
		img = (ImageView) findViewById(R.id.as_img);
		imgAddImage = (ImageView) findViewById(R.id.as_imgAddImage);
		
		edtxtEmail = (EditText) findViewById(R.id.as_editTextEmailUse);
		edtxtpassword = (EditText) findViewById(R.id.as_editTextPassUse);
		edtxtusername = (EditText) findViewById(R.id.as_editTextUser);
		btnSignUp = (Button) findViewById(R.id.as_btnSignup);
		//btnBrowse = (Button) findViewById(R.id.as_btnBrowse);	
		
		gda = new Global_data();
		cntxt = this;
		
//		btnBrowse.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//		   
//			      img.setVisibility(View.GONE);
//			      imagePath="";
//				  Intent intent = new Intent();
//                  intent.setType("image/*");
//                  intent.setAction(Intent.ACTION_GET_CONTENT);
//                  startActivityForResult(Intent.createChooser(intent,"Select Image"), SELECT_PICTURE);
//          
//			}
//		});
//		
		imgAddImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				   //  img.setVisibility(View.GONE);
				      imagePath="";
				    
					  Intent intent = new Intent();
	                  intent.setType("image/*");
	                  intent.setAction(Intent.ACTION_GET_CONTENT);
	                  startActivityForResult(Intent.createChooser(intent,"Select Image"), SELECT_PICTURE);
	                 
			}
		});
		
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
		

		Ion.with(img).load("https://d13yacurqjgara.cloudfront.net/users/99875/screenshots/1635187/lonely-goalie.gif");
		
	}

//	public class signupAsyncTask extends AsyncTask<String,Void,String> {
//
//		ProgressDialog pd;
//		@Override
//		protected void onPreExecute() {
//
//			pd = new ProgressDialog(ActivitySignUp.this);
//			pd.setTitle("Registering..");
//			pd.setMessage("Please wait...");
//			pd.setCancelable(false);
//			pd.show();
//		}
//
//
//		@Override
//		protected String doInBackground(String... params) {
//           	return userFunctions.signup(email, user, pass);
//        }
//
//		@Override
//		protected void onPostExecute(String response) {
//			
//			if(pd!=null){
//				pd.dismiss();
//			}
//			try{
//				if(response!=null && !response.equalsIgnoreCase("")){
//                              
//					if(Integer.parseInt(response)>0){
//						gda.savePreferences(gda.TAG_LOGIN, true, cntxt);	
//						gda.savePreferences(gda.TAG_EMAIL, email, cntxt);
//						gda.savePreferences(gda.TAG_LOCATION, "", cntxt);
//						gda.savePreferences(gda.TAG_GAME_TYPE, "", cntxt);
//					Toast.makeText(getBaseContext(), "registered successfully!", Toast.LENGTH_SHORT).show();
//					Intent i = new Intent(ActivitySignUp.this, ActivityHome.class);
//					startActivity(i);
//					ActivitySignUp.this.finish();
//					}
//					
//				}
//				else{
//					Toast.makeText(getBaseContext(), "username not available", Toast.LENGTH_SHORT).show();
//				}
//			}
//			catch(Exception e){
//				Toast.makeText(getBaseContext(), "something went wrong!", Toast.LENGTH_SHORT).show();
//				
//			}
//    	}
//	}
	
	   public class signupAsyncTask extends AsyncTask<String,Void,JSONObject> {

				ProgressDialog pd;
				@Override
				protected void onPreExecute() {

				  	pd = new ProgressDialog(ActivitySignUp2.this);
				  	pd.setTitle("Registering..");
					pd.setMessage("Please wait...");
					pd.setCancelable(false);
					pd.show();
				}


				@Override
				protected JSONObject doInBackground(String... params) {

					try {
						MultipartEntity ent =new MultipartEntity(); 
						if(!imagePath.equalsIgnoreCase("")){
						ent.addPart("user_image",new FileBody(new File(imagePath)));
						}
						ent.addPart("email",new StringBody(email));
						ent.addPart("username",new StringBody(user));
						ent.addPart("password",new StringBody(pass));
						
						return userFunctions.postSignup(ent);
					} catch (Exception e) {

						e.printStackTrace();
					}
					return null;
		        }
				
				@Override
				protected void onPostExecute(JSONObject response) {
					String error="";
					String error_code="";
					String image_url="";
					if(pd!=null){
						pd.dismiss();
					}
					if(response!=null){
						try{
							error = response.getString("error");
							error_code = response.getString("error_code");
							
							if(error.equalsIgnoreCase("false")){
								image_url = response.getString("image_url");
								
								gda.savePreferences(gda.TAG_LOGIN, true, cntxt);	
								gda.savePreferences(gda.TAG_EMAIL, email, cntxt);
								gda.savePreferences(gda.TAG_LOCATION, "", cntxt);
								gda.savePreferences(gda.TAG_GAME_TYPE, "", cntxt);
								gda.savePreferences(gda.TAG_USER_IMAGE_URL,image_url, cntxt);
							Toast.makeText(getBaseContext(), "registered successfully!", Toast.LENGTH_SHORT).show();
							Intent i = new Intent(ActivitySignUp2.this, ActivityHome.class);
							startActivity(i);
							ActivitySignUp2.this.finish();
							}
							else{
								if(error_code.equalsIgnoreCase("2")){
									Toast.makeText(getBaseContext(), "username not available", Toast.LENGTH_SHORT).show();
								}
								else{
									showFailMessage();
								}
							}
							
						}
						catch(Exception e){
	                       showFailMessage();
						}

					}
					else{
						showFailMessage();
					}

				}
			}
			
			
			
			
			
			private void showFailMessage(){
				Toast.makeText(getBaseContext(), "could not register, something went wrong!", Toast.LENGTH_SHORT).show();
						
			}
			
 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	   	if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null) {
			// Let's read picked image data - its URI
			Uri pickedImage = data.getData();
			// Let's read picked image path using content resolver
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            
            // Now we need to set the GUI ImageView data with data read from the picked file.  /storage/extSdCard/ThetaLogo.png
            img.setImageBitmap(BitmapFactory.decodeFile(imagePath));
            img.setVisibility(View.VISIBLE);
            img.setScaleType(ScaleType.CENTER_CROP);
         
            
            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();
		}
	   	
	   
	   	
	}
	
	// Listener when back button pressed
		@Override
		public void onBackPressed() {

			Intent i = new Intent(ActivitySignUp2.this, ActivityWelcome.class);
			 startActivity(i); 
			 finish();
		     overridePendingTransition (R.anim.open_main, R.anim.close_next);

		
		}

}
