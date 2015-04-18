package com.cmu.timball;






import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import com.cmu.timball.libraries.UserFunctions;
import com.cmu.timball.utils.Utils;
import com.google.android.gms.internal.im;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ActivityAddLocation extends Activity  {
	/** Called when the activity is first created. */

    private Button btnBrowse,btnSubmit,btnPickPlace;
    String name,address,phone,desc,etime,stime,date,categoryid,gametype,imagePath="";
    String lat,lng="";
	UserFunctions userFunctions;
	private ImageView img;
	private Spinner spnGameType;
	private EditText edttxtName,edttxtAddress,edttxtPhone,edttxtDesc;
	private ScrollView scrollview;
	private TimePicker timepickerETime,timepickerSTime;
	private DatePicker datepicker;
	private ActionBar actionbar;
	private TextView txtLat,txtLng;
	Global_data gda;
	 private static final int SELECT_PICTURE = 1;
	 
	    private String selectedImagePath;
	    
	    
        
        private ArrayAdapter<String> gametypeAdapter;
        String[] gametype_val_text = { "5 vs 5","7 vs 7","11 vs 11"};

	    

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_location);
		
	
		// Get ActionBar and set back button on actionbar
    	actionbar = getActionBar();
     	actionbar.setDisplayHomeAsUpEnabled(true);
     	
		userFunctions= new UserFunctions();
		gda = new Global_data();
		
		
		img = (ImageView) findViewById(R.id.aal_img);
		btnBrowse = (Button) findViewById(R.id.aal_btnBrowse);
		btnSubmit = (Button) findViewById(R.id.aal_btnSubmit);
		txtLat=(TextView) findViewById(R.id.aal_txtLat);
		txtLng=(TextView) findViewById(R.id.aal_txtLng);
		
		
		spnGameType = (Spinner) findViewById(R.id.aal_spnGameType);
		scrollview = (ScrollView) findViewById(R.id.aal_Scrollview);
		
		edttxtName = (EditText) findViewById(R.id.aal_editTextName);
		edttxtAddress = (EditText) findViewById(R.id.aal_editTextAddress);
		edttxtDesc = (EditText) findViewById(R.id.aal_editTextDesc);
		btnPickPlace = (Button) findViewById(R.id.aal_btnPickPlace);
		edttxtPhone = (EditText) findViewById(R.id.aal_editTextPhone);
	    timepickerETime = (TimePicker) findViewById(R.id.aal_timePickereTime);
	    timepickerSTime = (TimePicker) findViewById(R.id.aal_timePickersTime);
	    timepickerETime.setIs24HourView(true);
	    timepickerSTime.setIs24HourView(true);
	    
	    datepicker = (DatePicker) findViewById(R.id.aal_datePicker);
	    
		
		
		gametypeAdapter = new ArrayAdapter<String>(ActivityAddLocation.this,
				android.R.layout.simple_spinner_item, gametype_val_text);
		
		gametypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

       
		spnGameType.setAdapter(gametypeAdapter);
		
		btnPickPlace.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				lat="";
				lng="";
				txtLng.setVisibility(View.GONE);
				txtLat.setVisibility(View.GONE);
				Intent intent = new Intent(ActivityAddLocation.this,ActivityPickPlace.class);
			    startActivityForResult(intent, 0); 
			}
		});
		
		btnBrowse.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
		   
			      img.setVisibility(View.GONE);
			      imagePath="";
				  Intent intent = new Intent();
                  intent.setType("image/*");
                  intent.setAction(Intent.ACTION_GET_CONTENT);
                  startActivityForResult(Intent.createChooser(intent,"Select Image"), SELECT_PICTURE);
          
			}
		});
		
		btnSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				if(validateTextFields()){

					if(validateLatLng()){

						if(validateImage()){
							if(new Utils(ActivityAddLocation.this).isNetworkAvailable()){
								getTimes();
								getDate();
								getCategoryId();
								getGameType();
								new UploadDataAsync().execute("");
							}
							else{
								Toast.makeText(ActivityAddLocation.this,"no internet connection!", Toast.LENGTH_LONG).show();

							}
						}
						else{
							Toast.makeText(ActivityAddLocation.this,"please select an image", Toast.LENGTH_LONG).show();
						}

					}
					else{
						Toast.makeText(ActivityAddLocation.this,"please pick a place first", Toast.LENGTH_LONG).show();

					}

				}
				else{
					Toast.makeText(ActivityAddLocation.this,"please fill all fields", Toast.LENGTH_LONG).show();
				}
//
			}
		});
	
	
		

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
         
            
            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();
		}
	   	
	   	if (resultCode == RESULT_OK && requestCode==0 && data!=null) {
		        
	        	Bundle b = data.getExtras();
	    		if(b!=null){
	    			if (b.containsKey("lat")) {
	    				lat= b.getString("lat");
	    			    txtLat.setText("Longitude: "+lat);
	    			    txtLat.setVisibility(View.VISIBLE);
	    	    		
	    			}
	    			if (b.containsKey("lng")) {
	    				lng= b.getString("lng");
	    			    txtLng.setText("Latitude: "+lng);
	    			    txtLng.setVisibility(View.VISIBLE);
	    			}
	   
	    		}

	    }
		
	   	
	}
	
	

	 
	 

	    private boolean validateTextFields (){
	    	name = edttxtName.getText().toString().trim();
	    	desc = edttxtDesc.getText().toString().trim();
	    	phone = edttxtPhone.getText().toString().trim();
	    	address = edttxtAddress.getText().toString().trim();
	    	if(!name.equalsIgnoreCase("") && !address.equalsIgnoreCase("") && !phone.equalsIgnoreCase("") 
	    			&& !desc.equalsIgnoreCase("") ){
	    		return true;
	    	}
	    	return false;
	    }
	    
	    private boolean validateImage(){
	    	if(!imagePath.equalsIgnoreCase("")){
	    		return true;
	    	}
	    	return false;
	    }

	    private boolean validateLatLng(){
	    	if(!lat.equalsIgnoreCase("") && !lng.equalsIgnoreCase("")){
	    		return true;
	    	}
	    	return false;
	    }
	    private void getCategoryId(){
	    	// set the category to Open when creating a game
	    	// in the database the category Open has id C0001
	    		categoryid= "C0001";
	    }
	    private void getGameType(){
	    	gametype = spnGameType.getSelectedItem().toString();
	    }
	    private void getDate(){
	        int day = datepicker.getDayOfMonth();
	        int month = datepicker.getMonth();
	        int year =  datepicker.getYear();

	        Calendar calendar = Calendar.getInstance();
	        calendar.set(year, month, day);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        date = sdf.format(calendar.getTime());
	    }
	    
	    private void getTimes(){
	    int	eHour=timepickerETime.getCurrentHour();
	    int eMin = timepickerETime.getCurrentMinute();
	    
	    int sHour = timepickerSTime.getCurrentHour();
	    int sMin = timepickerSTime.getCurrentMinute();
	    
	    
	   etime= ( (eHour < 10 ? "0" : "") + eHour
				   + ":" + (eMin < 10 ? "0" : "") +eMin
				   + ":00"); 
	    
	   stime= ( (sHour < 10 ? "0" : "") + sHour
			   + ":" + (sMin < 10 ? "0" : "") +sMin
			   + ":00"); 
    
	    
	    }
	    
	    
	    
	    public class UploadDataAsync extends AsyncTask<String,Void,JSONObject> {

			ProgressDialog pd;
			@Override
			protected void onPreExecute() {

			  	pd = new ProgressDialog(ActivityAddLocation.this);
				pd.setTitle("Uploading Data..");
				pd.setMessage("Please wait...");
				pd.setCancelable(false);
				pd.show();
			}


			@Override
			protected JSONObject doInBackground(String... params) {
				try {
					MultipartEntity ent =new MultipartEntity(); 
					ent.addPart("location_image",new FileBody(new File(imagePath)));
					ent.addPart("location_name",new StringBody(name));
					ent.addPart("category_id",new StringBody(categoryid));
					ent.addPart("address",new StringBody(address));
					ent.addPart("latitude",new StringBody(lat));
					ent.addPart("longitude",new StringBody(lng));
					ent.addPart("phone",new StringBody(phone));
					ent.addPart("website",new StringBody(""));
					ent.addPart("typeofgame",new StringBody(gametype));
					ent.addPart("cdate",new StringBody(date));
					ent.addPart("stime",new StringBody(stime));
					ent.addPart("etime",new StringBody(etime));
					ent.addPart("tot_players",new StringBody("0"));
					ent.addPart("email",new StringBody(gda.loadSavedPreferences_string(gda.TAG_EMAIL,ActivityAddLocation.this)));
					ent.addPart("description",new StringBody(desc));
					ent.addPart("placename",new StringBody(""));
					return userFunctions.postLocationData(ent);
				} catch (Exception e) {

					e.printStackTrace();
				}
				
				
			
				return null;
	        }
			
			@Override
			protected void onPostExecute(JSONObject response) {
				String error="";
				String error_code="";
				if(pd!=null){
					pd.dismiss();
				}
				if(response!=null){
					try{
						error = response.getString("error");
						error_code = response.getString("error_code");
						
						if(error.equalsIgnoreCase("false")){
				
					      showSuccessMessage();		
						}
						else{
							showFailMessage();
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
		
		
		private void showSuccessMessage(){
			Toast.makeText(ActivityAddLocation.this,"Game created successfully!", Toast.LENGTH_LONG).show();
			
		}
		
		
		private void showFailMessage(){
			Toast.makeText(ActivityAddLocation.this,"Could not create Game!", Toast.LENGTH_LONG).show();
			
		}
		
/**End: Activity lifecycle*/ 	 	
 	 	
 		// Listener for option menu
 		@Override
 		public boolean onOptionsItemSelected(MenuItem item) {
 		    switch (item.getItemId()) {
 		    	case android.R.id.home:
 		    		// previous page or exit
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
