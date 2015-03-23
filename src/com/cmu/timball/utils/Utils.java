package com.cmu.timball.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
	
	// Declare object of Contecxt class
	Context ctx;
	
	// Declare Variables for Intent Put Extra
	public final String EXTRA_LOCATION_ID 	= "locationId";
	public final String EXTRA_LOCATION_NAME	= "locationName";
	public final String EXTRA_LOCATION_URL	= "locationUrl";
	public final String EXTRA_LOCATION_DESC	= "locationDesc";
	public final String EXTRA_LOCATION_IMG	= "locationImg";	
	public final String EXTRA_DEST_LAT		= "destLatitude";
	public final String EXTRA_DEST_LNG		= "destLongitude";
	public final String EXTRA_DEST_MARKER	= "destMarker";
	public final String EXTRA_CATEGORY_ID	= "categoryId";
	public final String EXTRA_CATEGORY_NAME	= "categoryName";
	public final String EXTRA_KEYWORD		= "keywordSeach";
	
	// Declare variables for setting preference
	public final String REG_ID 			= "RegisterID";
	public final String UTILS_OVERLAY = "utilOverlay";
	public final String VALUE_DEFAULT	= "0";
	public final String APP_VERSION		= "appVersion";

	public final String FACEBOOK_NAME 	= "facebookName";
	public final String TWITTER_NAME 	= "twitterName";
	public final String CHECK_PLAY_SERV	= "playService";
	
	// Condition for admob and social media (0=gone, 1=visible)
    public final int paramAdmob=0;
    public static final int paramSocialMedia=1;
    
    public final String timeoutMessageHtml = "<html><body><p>Error loading url: No Connection or connection down</p></body></html>";
    
	public final String ITEM_PAGE_LIST	= "itemPageList";
	
	/* mItem per page it must more then 1 or it will error, item per page it using for
	 handle much item in 1 page. */
	
	// XXLarge screen more than 7"
	public int mItemInXXlarge=12;
	// XLarge screen are at 7"
	public int mItemInXlarge=9;
	// Large screens are at 5"
	public int mItemInLarge=7;
	// Medium screens are at 4"
	public int mItemInMedium=5;

	// Screen size 7"
	public int mXlarge=7;
	// Screen size 5"
	public int mLarge=5;
	// Screen size 4"
	public int mMedium=4;
	
	public Utils(Context c){
		ctx = c;
	}
	
	// Method to check internet connection
	public boolean isNetworkAvailable() {
		ConnectivityManager connectivity = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
		
	// Method to save map type setting to SharedPreferences
	public void savePreferences(String param, int value){
	    SharedPreferences sharedPreferences = ctx.getSharedPreferences("user_data", 0);
	    SharedPreferences.Editor editor = sharedPreferences.edit();
	    editor.putInt(param, value);
	    editor.commit();
	}
	
	// Method to save map type setting to SharedPreferences
	public void saveString(String param, String value){
	    SharedPreferences sharedPreferences = ctx.getSharedPreferences("user_data1", 0);
	    SharedPreferences.Editor editor = sharedPreferences.edit();
	    editor.putString(param, value);
	    editor.commit();
	}
	
	// Method to load map type setting 
	public int loadPreferences(String param){
	    SharedPreferences sharedPreferences = ctx.getSharedPreferences("user_data", 0);
	    int selectedPosition = sharedPreferences.getInt(param, 0);
	    
	    return selectedPosition;
	}
	
	// Method to load map type setting 
	public String loadString(String param){
	    SharedPreferences sharedPreferences = ctx.getSharedPreferences("user_data1", 0);
	    String selectedPosition = sharedPreferences.getString(param, "unknown");
	    
	    return selectedPosition;
	}
}
