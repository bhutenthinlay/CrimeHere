/*
 * Copyright 2014 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.csform.android.uiapptemplate.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.csform.android.uiapptemplate.DatabaseOperations;
import com.csform.android.uiapptemplate.GoogleMapFragment;
import com.csform.android.uiapptemplate.ListViewFragment;
import com.csform.android.uiapptemplate.ParticularInfoOnMap;
import com.csform.android.uiapptemplate.R;
import com.csform.android.uiapptemplate.util.ImageUtil;
import com.nhaarman.listviewanimations.ArrayAdapter;

public class MyStickyListHeadersAdapter extends ArrayAdapter<String> implements
		StickyListHeadersAdapter {

	private final Context mContext;
	private LayoutInflater mInflater;

	ArrayList<Integer> crimeTypeCount = new ArrayList<Integer>();
	ArrayList<Integer> headerPosition = new ArrayList<Integer>();
	ArrayList<String> crimeTypeName = new ArrayList<String>();
	static ArrayList<String> crime = new ArrayList<String>();
	private int count = 0;
	int header;

	static private ArrayList<HashMap<String, String>> mylist;

	public MyStickyListHeadersAdapter(final Context context,
			ArrayList<HashMap<String, String>> mylist) {
		mContext = context;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		clear();
		mylist = new ArrayList<HashMap<String, String>>();
		this.mylist = mylist;
		/*
		 * 
		 * for (int i = 0; i < crime.size(); i++) {
		 * 
		 * }
		 * 
		 */
		// Log.e("database count", String.valueOf(cr1.getCount()));
		
		
		
		for (int i = 0; i < GoogleMapFragment.mylist.size(); i++) {
			// for (int i = 0; i < ; i++) {
			add(GoogleMapFragment.mylist.get(i).get("title"));
		}

	}

	public MyStickyListHeadersAdapter(ListViewFragment listViewFragment) {
		// TODO Auto-generated constructor stub
		mContext = listViewFragment.getActivity();
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i = 0; i < 1000; i++) {
			add("Row number " + i);
		}
	}

	@Override
	public long getItemId(final int position) {
		return getItem(position).hashCode();
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item_default, parent,
					false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.image123);
			holder.textTitle = (TextView) convertView.findViewById(R.id.textTitleKyakpa);
			holder.textType = (TextView) convertView.findViewById(R.id.textType);
			holder.textDate = (TextView) convertView.findViewById(R.id.textDate);
		//holder.icon = (TextView) convertView.findViewById(R.id.icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// TODO Change image URL
		
		try{
		holder.textTitle.setText(GoogleMapFragment.mylist.get(position).get("title"));
		holder.textType.setText(GoogleMapFragment.mylist.get(position).get("type_name"));
		holder.textDate.setText(GoogleMapFragment.mylist.get(position).get("date_occured"));
		holder.image.setBackgroundResource(R.drawable.crimehere);
		//ImageUtil.displayRoundImage(holder.image, "", null);
		}catch(Exception ex)
		{
			
		}
        convertView.setHorizontalFadingEdgeEnabled(true);
		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(mContext, getItem(position), 0).show();
				Double lat=Double.parseDouble(GoogleMapFragment.mylist.get(position).get("lat"));
				Double lng=Double.parseDouble(GoogleMapFragment.mylist.get(position).get("lng"));
				String title=GoogleMapFragment.mylist.get(position).get("title");
				String description=GoogleMapFragment.mylist.get(position).get("description");
				String user_login=GoogleMapFragment.mylist.get(position).get("user_login");
				String image0=GoogleMapFragment.mylist.get(position).get("image0");
				String image1=GoogleMapFragment.mylist.get(position).get("image1");
				String image2=GoogleMapFragment.mylist.get(position).get("image2");
				String type_name=GoogleMapFragment.mylist.get(position).get("type_name");
				String identifier=GoogleMapFragment.mylist.get(position).get("identifier");
				String address=GoogleMapFragment.mylist.get(position).get("address");
				String date_occured=GoogleMapFragment.mylist.get(position).get("date_occured");
				Bundle b=new Bundle();
	    	    b.putDouble("lat", lat);		 
			     b.putDouble("lng", lng);
			     b.putString("title", title);
			     b.putString("description",description);
			     b.putString("user_login",user_login);
			     b.putString("image0",image0);
			     b.putString("image1",image1);
			     b.putString("image2",image2);
			     b.putString("type_name",type_name);
			     b.putString("identifier", identifier);
			     b.putString("address", address);
			     b.putString("date_occured",date_occured);
			     
			        		     
			     
	       Intent i= new Intent(mContext,ParticularInfoOnMap.class);
	       i.putExtras(b);
	       mContext.startActivity(i);
			}
		});
		return convertView;
	}

	private static class ViewHolder {
		public ImageView image;
		public/* Roboto */TextView textTitle;
		public/* Roboto */TextView textType;
		public/* Roboto */TextView textDate;
		public/* Fontello */TextView icon;
	}

	@Override
	public View getHeaderView(final int position, final View convertView,
			final ViewGroup parent) {
		TextView view = (TextView) convertView;
		if (view == null) {
			view = (TextView) LayoutInflater.from(mContext).inflate(
					R.layout.list_header, parent, false);
		}

		try {
			view.setText("Crimes");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return view;
	}

	@Override
	public long getHeaderId(final int position) {
		return 0;
	}

}