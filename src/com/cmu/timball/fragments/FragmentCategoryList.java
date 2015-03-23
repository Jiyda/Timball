package com.cmu.timball.fragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cmu.timball.adapters.AdapterCategoryList;
import com.cmu.timball.libraries.UserFunctions;
import com.cmu.timball.utils.Utils;
import com.cmu.timball.R;

public class FragmentCategoryList extends Fragment implements OnClickListener {
	OnCategoryListSelectedListener mCallback;
	
	private ListView list;
	
	
	// Declare instance of UserFunctions, AdapterCategory and JSONObject class
	private AdapterCategoryList la;
	private UserFunctions userFunction;
	private Utils utils;
	private JSONObject json;
	private ProgressDialog pDialog;
	
	// Declare view objects
	private TextView lblNoResult, lblAlert;
	private Button btnRetry; 
	private LinearLayout lytRetry;
		
	public static String[] Categories;
	public int mCurrentPosition;

	// Get length array from server
	private int intLengthData;
	
	// Create array variables to store data
	public static String[] sCategoryId;
	public static String[] sCategoryName;
	public static String[] sIcMarkerLocation;
	
	public interface OnCategoryListSelectedListener{
		
		public void onCategoryListSelected(String mCategoryId, String mCategoryName);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_category, null);
		
		list 		= (ListView) v.findViewById(R.id.list);
		lblNoResult	= (TextView) v.findViewById(R.id.lblNoResult);
		lblAlert	= (TextView) v.findViewById(R.id.lblAlert);
		lytRetry 	= (LinearLayout) v.findViewById(R.id.lytRetry);
		btnRetry 	= (Button) v.findViewById(R.id.btnRetry);

		btnRetry.setOnClickListener(this);
		
		// Declare object of UserFuntions class
		userFunction= new UserFunctions();
		utils 		= new Utils(getActivity());

		if(utils.isNetworkAvailable()){	
			new getCategoryList().execute();
		} else {
			lblNoResult.setVisibility(View.GONE);
    		lytRetry.setVisibility(View.VISIBLE);
    		lblAlert.setText(R.string.no_connection);
		}	
		

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				mCallback.onCategoryListSelected(sCategoryId[position], sCategoryName[position]);
				mCurrentPosition = position;
				list.setItemChecked(position, true);
				
			}
		});
		
        return v;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnCategoryListSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnCategoryListSelectedListener");
        }
    }
	
	// AsynTask to get list Category data 
	public class getCategoryList extends AsyncTask<Void, Void, Void>{

    	@Override
		 protected void onPreExecute() {
            // Showing progress dialog before sending http request
            pDialog = new ProgressDialog(
                    getActivity());
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
    		
    	}

    	@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
    		// Method to get data from server
			getDataFromServer();
			return null;
		}
    	
    	@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
    		
    		if(isAdded()){
	            if(intLengthData != 0){
	            	lytRetry.setVisibility(View.GONE);
	            	lblNoResult.setVisibility(View.GONE);
	            	list.setVisibility(View.VISIBLE);
	            	
	        		la = new AdapterCategoryList(getActivity());

	            	if(list.getAdapter() == null){
	            		list.setAdapter(la);
		            }
	            	
	            } else {
	            	if(json != null){
						lblNoResult.setVisibility(View.VISIBLE);
	            		lytRetry.setVisibility(View.GONE);
	            		
		            } else {
						lblNoResult.setVisibility(View.GONE);
	            		lytRetry.setVisibility(View.VISIBLE);
	            		lblAlert.setText(R.string.error_server);
	            	}
	            }
        	}    		
        	// Closing pDialog dialog
            if(pDialog.isShowing()) {
            	pDialog.dismiss();
			}			
		}
	}
	
	// Method to get data from server
	public void getDataFromServer(){
	       
        try {
            json = userFunction.categoryList();
            if(json != null){
		        JSONArray dataLocationArray = json.getJSONArray(userFunction.array_category_list);
		        
		        intLengthData = dataLocationArray.length();
		        sCategoryId 		= new String[intLengthData];
		        sCategoryName 		= new String[intLengthData];
		        sIcMarkerLocation 	= new String[intLengthData];
		                   
		        // Store data to variable array
		        for (int i = 0; i < intLengthData; i++) {
		        	JSONObject locationObject = dataLocationArray.getJSONObject(i);
		
		            sCategoryId[i] 		= locationObject.getString(userFunction.key_category_id);
		            sCategoryName[i] 	= locationObject.getString(userFunction.key_category_name);
		            sIcMarkerLocation[i]= locationObject.getString(userFunction.key_category_marker);
		            
		        }
            }                
        } catch (JSONException e) {
            // TODO Auto-generated catch block
        	Log.i("FragmentCategoryList", "getDataFromServer: "+e);
        }      
    }

	@Override
	public void onClick(View v) {
	// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btnRetry:
				if(utils.isNetworkAvailable()){	
					json = null;
					new getCategoryList().execute();
				} else {
					lblNoResult.setVisibility(View.GONE);
		    		lytRetry.setVisibility(View.VISIBLE);
		    		lblAlert.setText(R.string.no_connection);
				}

			default:
				break;
			}
		
	}
}

