package com.cmu.timball;




import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.go;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityPickPlace extends Activity  {

	private GoogleMap googleMap;
	private Button btnDone,btnClear;
	double lat,lng=0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
	    setContentView(R.layout.activity_pickplace_map);
		
	    btnClear=(Button) findViewById(R.id.apm_btnClear);
	    btnDone=(Button) findViewById(R.id.apm_btnDone);
		
	    
	    btnDone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				Bundle b= new Bundle();
	 		 	b.putString("lat",String.valueOf(lat));
	 		 	b.putString("lng",String.valueOf(lng));
	 	        Intent intent = new Intent();
	            intent.putExtras(b);
	            setResult(RESULT_OK,intent);  
	            finish();
			
			}
		});
	    
	    btnClear.setOnClickListener(new OnClickListener() {
	    	@Override
	    	public void onClick(View arg0) {
	    		if(googleMap!=null){
                   googleMap.clear();
                   lat=0;
                   lng=0;
	    		}
	    	}
	    });

	    int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

	    // Showing status
	    if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

	        int requestCode = 10;
	        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
	        dialog.show();

	    }
	    else{
			// Loading map
			initilizeMap();

			// Changing map type
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

			// Showing / hiding your current location
			googleMap.setMyLocationEnabled(true);
			
		
			// Enable / Disable zooming controls
			googleMap.getUiSettings().setZoomControlsEnabled(false);

			// Enable / Disable my location button
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);

			// Enable / Disable Compass icon
			googleMap.getUiSettings().setCompassEnabled(true);

			// Enable / Disable Rotate gesture
			googleMap.getUiSettings().setRotateGesturesEnabled(true);

			// Enable / Disable zooming functionality
			googleMap.getUiSettings().setZoomGesturesEnabled(true);
			
		
			googleMap.setOnMapLongClickListener(new OnMapLongClickListener() {
				@Override
				public void onMapLongClick(LatLng loc) {
				
					addMarker(loc);	
				
				
				
				}
			});
		
		
	    }
		//	googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble("31.54505000000"), Double.parseDouble("74.340683000000"))));
			
			// Zoom in the Google Map
		//	googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));		

	}

	private void addMarker(LatLng loc) {
		
		if (googleMap != null) {
			googleMap.clear();
			lat=0;
			lng=0;
			googleMap.addMarker(new MarkerOptions().position(loc)
			.icon(BitmapDescriptorFactory
	        .defaultMarker(BitmapDescriptorFactory.HUE_RED))
			.draggable(false));		
			lat=loc.latitude;
			lng=loc.longitude;
	//		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 13));

		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}


	private void initilizeMap() {
	
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.mapactivity_map)).getMap();

			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create map", Toast.LENGTH_SHORT)
						.show();
			}
		}
		
	}

	
	
}
