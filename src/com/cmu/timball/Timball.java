package com.cmu.timball;

import com.instabug.library.Instabug;

import android.app.Application;

public class Timball extends Application {
	 
		
	 
		@Override
		public void onCreate() {
			super.onCreate();
			Instabug.initialize(this, "cab12186b0c7bfd21830486d61475936");
		}
	 
		
	 
	}