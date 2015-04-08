package com.cmu.timball.fragments;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.timball.adapters.AdapterGamesJoined;
import com.cmu.timball.libraries.UserFunctions;
import com.cmu.timball.loadmore.PagingListView;
import com.cmu.timball.utils.Utils;
import com.cmu.timball.ActivityProfile;
import com.cmu.timball.R;

public class FragmentGamesJoined extends Fragment implements OnClickListener{
	
	// Create interface for MapsListFragment
	private OnListSelectedListener mCallback;

	boolean loadingMore = false;

	private ArrayList<HashMap<String, String>> mItems;
	private ProgressDialog pDialog;
    
    // Declare object of userFunctions class
	private UserFunctions userFunction;
	private Utils utils;
	
	// Create instance of list and ListAdapter
	private PagingListView list;
	private AdapterGamesJoined la;
	private LinearLayout lytRetry;
	
	private Button btnRetry;
	private TextView lblNoResult, lblAlert;
	
	// Flag for current page
	private JSONObject json;
    private int mCurrentPage = 0;
    private int mPreviousPage;
	
	// Flag for loadmore
	private int mCurrentPositon = 0;
	
	private String mEmail;
	private int intLengthData;
	
	// Create array variables to store data
	public String[] mLocationId;
	public String[] mLocationName;
	public String[] mAddress;
	public String[] mIcMarkerLocation;
	public String[] mImgLocation;
	
	public final String KEY_ID 		= "id";
	public final String KEY_NAME 	= "location_name";
	public final String KEY_ADDRESS = "address";
	public final String KEY_IMAGE 	= "image";
	public final String KEY_MARKER 	= "marker";
	
	// To handle parameter loadmore gone or visible(1 = visible ; 0 = gone)
	private int paramLoadmore=0;
	
	// Declare OnListSelected interface
	public interface OnListSelectedListener{
		public void onListSelected(String idSelected);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View v = inflater.inflate(R.layout.fragment_home_list, container, false);
		
		list 		= (PagingListView) v.findViewById(R.id.list);
		lblNoResult	= (TextView) v.findViewById(R.id.lblNoResult);
		lblAlert	= (TextView) v.findViewById(R.id.lblAlert);
		lytRetry 	= (LinearLayout) v.findViewById(R.id.lytRetry);
		btnRetry 	= (Button) v.findViewById(R.id.btnRetry);
		
		btnRetry.setOnClickListener(this);
		
		// Declare object of userFunctions, utils and mItems class
		userFunction = new UserFunctions();
		utils 		 = new Utils(getActivity());
		mItems 	 	 = new ArrayList<HashMap<String, String>>();

		// Get Bundle data from ActivitySearch
  		Bundle bundle = this.getArguments();
  		mEmail	  = bundle.getString(ActivityProfile.EXTRA_EMAIL);
  		
		// Listener to get selected id when list item clicked
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				// Conditional if mCurrentPositon == position it means addlist of loadmore
				if(mCurrentPositon!=position){
					HashMap<String, String> item = new HashMap<String, String>();
			        item = mItems.get(position);

					// Pass id to onListSelected method on HomeActivity
					mCallback.onListSelected(item.get("id"));

					// Set the item as checked to be highlighted when in two-pane layout
					list.setItemChecked(position, true);	
				}
				
			}
		});
		
		if(utils.isNetworkAvailable()){	
			new loadFirstListView().execute();
		} else {
			lblNoResult.setVisibility(View.GONE);
    		lytRetry.setVisibility(View.VISIBLE);
    		lblAlert.setText(R.string.no_connection);
		}
		
		// Set loadmore
		list.setHasMoreItems(true);
		list.setPagingableListener(new PagingListView.Pagingable() {
			@Override
			public void onLoadMoreItems() {
				if(utils.isNetworkAvailable()){	
					json = null;
					if(paramLoadmore==1){
						new loadMoreListView().execute();
					} else {
						list.onFinishLoading(false, null);
					}
	                
				} else {
					Toast.makeText(getActivity(), R.string.no_connection, Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		return v;
	}
	
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // The callback interface. If not, it throws an exception.
        try {
            mCallback = (OnListSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
		
	// Load first 10 games
	private class loadFirstListView extends AsyncTask<Void, Void, Void> {
		 
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
 
        protected Void doInBackground(Void... unused) {
        	// Call method getDataFromServer
        	getDataFromServer();
        	return (null);
        }
 
        protected void onPostExecute(Void unused) {
        	
        	if(isAdded()){
	            if(mItems.size() != 0){

	                // Adding load more button to lisview at bottom
	            	lytRetry.setVisibility(View.GONE);
	            	list.setVisibility(View.VISIBLE);
	            	lblNoResult.setVisibility(View.GONE);
	            	
	            	// Getting adapter
	            	la = new AdapterGamesJoined(getActivity(), mItems);
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
        	// Closing progress dialog
            if(pDialog.isShowing()) {
            	pDialog.dismiss();
			}
            
        }
    }
	// Load more games
    private class loadMoreListView extends AsyncTask<Void, Void, Void> {
    	
        @Override
        protected void onPreExecute() {

        }
 
        protected Void doInBackground(Void... unused) {
        	
			// Store previous value of current page
			mPreviousPage = mCurrentPage;
            // Increment current page
			mCurrentPage += FragmentHomeLatestMapsList.paramValueItemPerPage;
			getDataFromServer();
            return (null);
        }
 
        protected void onPostExecute(Void unused) {

            if(json != null){
	            	// Get listview current position - used to maintain scroll position
		            int currentPosition = list.getFirstVisiblePosition();
		
		            list.setVisibility(View.VISIBLE);
		            
	            	lytRetry.setVisibility(View.GONE);
		            // Appending new data to mItems ArrayList
		            la = new AdapterGamesJoined(
		                    getActivity(),
		                    mItems);
		            
		            if(list.getAdapter() == null){
		            	list.setAdapter(la);
		            }
		            
		            // Setting new scroll position
		            list.setSelectionFromTop(currentPosition + 1, 0);
		            list.onFinishLoading(true, mItems);
            	
            } else {
            	mCurrentPage = mPreviousPage;
        		Toast.makeText(getActivity(), R.string.error_server, Toast.LENGTH_SHORT).show();
            }
            // Closing progress dialog
            if(pDialog.isShowing()) {
            	pDialog.dismiss();
			}
        }
    }
    
	
	// Method get data from server
	public void getDataFromServer(){
	       
        try {
        	
        	json = userFunction.gamesJoined(mEmail, mCurrentPage, FragmentHomeLatestMapsList.paramValueItemPerPage);
        	mEmail = mEmail.replace("%20", " ");
            
            if(json != null){
	            JSONArray dataLocationArray = json.getJSONArray(userFunction.array_games_joined);
	            
	            // Get Count_Total from server
            	int mCountTotal = Integer.valueOf(json.getString(userFunction.key_total_data));
            	// Because it array, it start from 0 not 1, so mCountTotal - 1
            	mCountTotal-=1;
            	
            	/* Conditional if mCountTotal equal or more than mCurrentPage it means 
            	all data from server is already load */
            	if(((mCountTotal-=FragmentHomeLatestMapsList.paramValueItemPerPage)<mCurrentPage)){
            		paramLoadmore=0;
            		list.onFinishLoading(false, null);
            	} else {
            		paramLoadmore=1;
            	}
            	
	            intLengthData = dataLocationArray.length();
	            mLocationId 		= new String[intLengthData];
	            mLocationName 		= new String[intLengthData];
	            mAddress 			= new String[intLengthData];
	            mIcMarkerLocation 	= new String[intLengthData];
	            mImgLocation 		= new String[intLengthData];
	            
	            // Store data to variable array
	            for (int i = 0; i < intLengthData; i++) {
	            	JSONObject locationObject = dataLocationArray.getJSONObject(i);
	
	            	HashMap<String, String> map = new HashMap<String, String>();
				    
	                mLocationId[i] 		= locationObject.getString(userFunction.key_location_id);
	                mLocationName[i] 	= locationObject.getString(userFunction.key_location_name);
	                mAddress[i] 		= locationObject.getString(userFunction.key_location_address);
	                mIcMarkerLocation[i]= locationObject.getString(userFunction.key_category_marker);
	                mImgLocation[i] 	= locationObject.getString(userFunction.key_location_image);
	                
				    map.put(KEY_ID, mLocationId[i]); // id not using any where
		            map.put(KEY_NAME, mLocationName[i]);
		            map.put(KEY_ADDRESS, mAddress[i]);
		            map.put(KEY_IMAGE, mImgLocation[i]);
		            map.put(KEY_MARKER, mIcMarkerLocation[i]);
		            
		            // Adding HashList to ArrayList
		            mItems.add(map);
	            }
	            
	            // Increment current positon to handle loadmore
				mCurrentPositon += FragmentHomeLatestMapsList.paramValueItemPerPage;
            }        
                               
        } catch (JSONException e) {
            // TODO Auto-generated catch block
        	Log.i("FragmentGamesJoined", "getDataFromServer: "+e);
        }
            
    }
	
    @Override
    public void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.btnRetry:
			if(utils.isNetworkAvailable()){	
				json = null;
				new loadFirstListView().execute();
			} else {
				lblNoResult.setVisibility(View.GONE);
	    		lytRetry.setVisibility(View.VISIBLE);
	    		lblAlert.setText(R.string.no_connection);
			}
			break;
		default:
			break;
		}
	}
	    
}
