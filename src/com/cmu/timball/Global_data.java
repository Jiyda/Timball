package com.cmu.timball;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Global_data {

	public static String LOGIN_PREFERENCES ="login_status_preference";
	public static String TAG_EMAIL="email";
	public static String TAG_LOGIN="login_status";
	public static String TAG_LOCATION="u_location";
	public static String TAG_GAME_TYPE="u_game_type";
	
	public void savePreferences(String key, boolean value, Context cntxt) {
		SharedPreferences sp = cntxt.getSharedPreferences(LOGIN_PREFERENCES,cntxt.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}

	public boolean loadSavedPreferences(String key, Context cntxt) {
		SharedPreferences sp = cntxt.getSharedPreferences(LOGIN_PREFERENCES,cntxt.MODE_PRIVATE);
		boolean cbValue = sp.getBoolean(key, false);
		
		if (cbValue) {
			return true;
		} else {
			return false;
		}
	}
	
	public void removePreferences(String key, Context cntxt) {
		SharedPreferences sp = cntxt.getSharedPreferences(LOGIN_PREFERENCES,cntxt.MODE_PRIVATE);
		sp.edit().remove(key).commit();
	}
	
	public void savePreferences(String key, String value, Context cntxt) {
		SharedPreferences sp = cntxt.getSharedPreferences(LOGIN_PREFERENCES,cntxt.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}
	
	public String loadSavedPreferences_string(String key, Context cntxt) {
		SharedPreferences sp = cntxt.getSharedPreferences(LOGIN_PREFERENCES,cntxt.MODE_PRIVATE);
		String cbValue = sp.getString(key, "");
		return cbValue;
	}
}
