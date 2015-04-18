package com.cmu.timball.adapters;

import java.util.List;

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
public class AdapterPlayerList extends BaseAdapter {

		private Activity activity;
		private List<String> list;
		private boolean showX;

		// For animation transition
		private int lastPosition = -1;
		
		public AdapterPlayerList(Activity act, List<String> _list, boolean _showX) {
			activity = act;
			this.list=_list;
			this.showX=_showX;
		}
		
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
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
			
			
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) activity
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.adapter_row_player, null);
				holder = new ViewHolder();	
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			// Animation transition
			Animation animation = AnimationUtils.loadAnimation(activity, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
	        convertView.startAnimation(animation);
	        lastPosition = position;
	        
			holder.lblPlayer = (TextView) convertView.findViewById(R.id.txtPlayerName);
			holder.icMarker 	   = (ImageView) convertView.findViewById(R.id.ic_img);
			
			holder.lblPlayer.setText(list.get(position));
			if(showX){
				holder.icMarker.setVisibility(View.VISIBLE);;
					
			}
			else{
				holder.icMarker.setVisibility(View.GONE);
				
			}
			
			
			
			return convertView;
		}
		
		static class ViewHolder {
			TextView lblPlayer;
			ImageView icMarker;
		}
		
		
	}