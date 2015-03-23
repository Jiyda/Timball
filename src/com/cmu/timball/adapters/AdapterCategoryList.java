package com.cmu.timball.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmu.timball.fragments.FragmentCategoryList;
import com.cmu.timball.libraries.UserFunctions;
import com.cmu.timball.R;
import com.squareup.picasso.Picasso;
public class AdapterCategoryList extends BaseAdapter {

		private Activity activity;

		//declare instance of userFunctions class
		private UserFunctions userFunction;
		
		// For animation transition
		private int lastPosition = -1;
		
		public AdapterCategoryList(Activity act) {
			activity = act;
		}
		
		public int getCount() {
			// TODO Auto-generated method stub
			return FragmentCategoryList.sCategoryId.length;
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
			
			//declare object of userFunctions class
			userFunction = new UserFunctions();
			
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) activity
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.adapter_row_category, null);
				holder = new ViewHolder();	
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			// Animation transition
			Animation animation = AnimationUtils.loadAnimation(activity, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
	        convertView.startAnimation(animation);
	        lastPosition = position;
	        
			holder.lblNameCategory = (TextView) convertView.findViewById(R.id.lblNameCategory);
			holder.icMarker 	   = (ImageView) convertView.findViewById(R.id.ic_marker);
			
			holder.lblNameCategory.setText(FragmentCategoryList.sCategoryName[position]);
			
			// set data to textview and imageview
			// Set image with picasso
	        Picasso.with(activity)
	        .load(userFunction.URLAdmin+FragmentCategoryList.sIcMarkerLocation[position])
	        .fit().centerCrop()
	        .tag(activity)
	        .into(holder.icMarker);
			
			return convertView;
		}
		
		static class ViewHolder {
			TextView lblNameCategory;
			ImageView icMarker;
		}
		
		
	}