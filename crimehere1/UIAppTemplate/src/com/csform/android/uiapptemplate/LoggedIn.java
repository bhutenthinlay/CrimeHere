package com.csform.android.uiapptemplate;

import java.util.List;

import tabs.SlidingTabLayout;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.media.audiofx.BassBoost.Settings;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.csform.android.uiapptemplate.model.DrawerItem;

import fragments.AllNewsFragment;

public class LoggedIn extends ActionBarActivity {

	ViewPager mPager;
	SlidingTabLayout mTabs;
	public static final int GOOGLEMAP = 0;
	public static final int LISTVIEW = 1;
	private ListView mDrawerList;
	private List<DrawerItem> mDrawerItems;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loggedin);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
		mTabs.setDistributeEvenly(true);

		mTabs.setViewPager(mPager);
		LocationManager_check locationManagerCheck = new LocationManager_check(
				this);
		Location location = null;

		if (locationManagerCheck.isLocationServiceAvailable()) {

			if (locationManagerCheck.getProviderType() == 1)
				location = locationManagerCheck.locationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			else if (locationManagerCheck.getProviderType() == 2)

				location = locationManagerCheck.locationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		} else {
			locationManagerCheck.createLocationServiceError(LoggedIn.this);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.search) {
			// query...
		}

		return super.onOptionsItemSelected(item);
	}

	class MyPagerAdapter extends FragmentPagerAdapter {
		String[] tabs;

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			tabs = getResources().getStringArray(R.array.tabs);
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return tabs[position];
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stubf
			Fragment fragment = null;
			// MapFragmentDetail mf=null;

			switch (position) {
			case GOOGLEMAP:
				fragment = GoogleMapFragment.newInstance("0", "0", "0", "All");
				// mf = new MapFragmentDetail();
				// mf.MapFragmentDetail1("0", "0", "0", "All");
				// FragmentManager manager = getSupportFragmentManager();
				// FragmentTransaction transaction = manager.beginTransaction();
				// transaction.add(R.id.main_activity23, fragment,
				// "mapfragment");
				// transaction.commit();

				break;
			case LISTVIEW:
				fragment = ListViewFragment.newInstance("", "");

				break;
			}

			return fragment;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		return true;
	}

	public class LocationManager_check {

		LocationManager locationManager;
		Boolean locationServiceBoolean = false;
		int providerType = 0;
		AlertDialog alert;

		public LocationManager_check(Context context) {
			locationManager = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
			boolean gpsIsEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			boolean networkIsEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (networkIsEnabled == true && gpsIsEnabled == true) {
				locationServiceBoolean = true;
				providerType = 1;

			} else if (networkIsEnabled != true && gpsIsEnabled == true) {
				locationServiceBoolean = true;
				providerType = 2;

			} else if (networkIsEnabled == true && gpsIsEnabled != true) {
				locationServiceBoolean = true;
				providerType = 1;
			}

		}

		public Boolean isLocationServiceAvailable() {
			return locationServiceBoolean;
		}

		public int getProviderType() {
			return providerType;
		}

		public void createLocationServiceError(final Activity activityObj) {

			// show alert dialog if Internet is not connected
			AlertDialog.Builder builder = new AlertDialog.Builder(activityObj);

			builder.setMessage(
					"You need to activate location service to use this feature. Please turn on network or GPS mode in location settings")
					.setTitle("LostyFound")
					.setCancelable(false)
					.setPositiveButton("Settings",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// Intent intent = new
									// Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
									// activityObj.startActivity(intent);
									// alert.dismiss();
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									alert.dismiss();
								}
							});
			alert = builder.create();
			alert.show();
		}

	}

}
