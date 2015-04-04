package com.cmu.timball.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmu.timball.fragments.FragmentHomeLatestMapsList;
import com.cmu.timball.libraries.UserFunctions;
import com.cmu.timball.loadmore.PagingBaseAdapter;
import com.cmu.timball.R;
import com.squareup.picasso.Picasso;


public class AdapterGamesJoined extends PagingBaseAdapter{

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater=null;
	
	// Declare object of userFunctions class
	private UserFunctions userFunction;

	// For animation transition
	private int lastPosition = -1;
	
	public AdapterGamesJoined(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		
		// Declare object of UserFuntions class
		userFunction = new UserFunctions();

		if(convertView == null){
			convertView = inflater.inflate(R.layout.adapter_row_joined, null);
			holder = new ViewHolder();
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		// Animation transition
		Animation animation = AnimationUtils.loadAnimation(activity, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        convertView.startAnimation(animation);
        lastPosition = position;
		
		// Connect views object and views id on xml
		holder.lblAddress 	= (TextView) convertView.findViewById(R.id.lblAddress);
		holder.lblTitle 	= (TextView) convertView.findViewById(R.id.lblTitle);
		holder.imgThumbnail = (ImageView) convertView.findViewById(R.id.imgThumbnail);
		holder.icMarker 	= (ImageView) convertView.findViewById(R.id.ic_marker);
		
		HashMap<String, String> item = new HashMap<String, String>();
        item = data.get(position);
        
		// Set image with picasso
        Picasso.with(activity)
        .load(userFunction.URLAdmin+item.get(FragmentHomeLatestMapsList.KEY_IMAGE))
        .fit().centerCrop()
        .tag(activity)
        .into(holder.imgThumbnail);
        
		// Set marker with picasso
        Picasso.with(activity)
        .load(userFunction.URLAdmin+item.get(FragmentHomeLatestMapsList.KEY_MARKER))
        .fit().centerCrop()
        .tag(activity)
        .into(holder.icMarker);
        
		holder.lblTitle.setText(item.get(FragmentHomeLatestMapsList.KEY_NAME));
		holder.lblAddress.setText(item.get(FragmentHomeLatestMapsList.KEY_ADDRESS));
		
		return convertView;
	}

	// Method to create instance of views
	static class ViewHolder {
		ImageView imgThumbnail, icMarker;
		TextView lblTitle, lblAddress;
	}
	
	
}