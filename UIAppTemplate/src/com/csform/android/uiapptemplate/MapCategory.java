package com.csform.android.uiapptemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tabs.SlidingTabLayout;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.csform.android.uiapptemplate.R;
import com.csform.android.uiapptemplate.adapter.DrawerAdapter;
import com.csform.android.uiapptemplate.model.DrawerItem;
import com.csform.android.uiapptemplate.util.ImageUtil;

public class MapCategory extends ActionBarActivity{
	public static final String LEFT_MENU_OPTION = "com.csform.android.uiapptemplate.LeftMenusActivity";
	public static final String LEFT_MENU_OPTION_1 = "Left Menu Option 1";
	public static final String LEFT_MENU_OPTION_2 = "Left Menu Option 2";

	private ListView mDrawerList;
	private List<DrawerItem> mDrawerItems;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	// my added
	ViewPager mPager;
	SlidingTabLayout mTabs;

	private Handler mHandler;
	static private ArrayList<HashMap<String, String>> mylist;
	public static final int GOOGLEMAP = 0;

	static String searchAddress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_category);
        mylist = new ArrayList<HashMap<String, String>>();
		try {
			Bundle b = getIntent().getExtras();
			searchAddress = b.getString("searchAddress");
			Log.e("left menu", searchAddress);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		mPager = (ViewPager) findViewById(R.id.pagerCategory);
		mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		mTabs = (SlidingTabLayout) findViewById(R.id.tabsCategory);
		mTabs.setDistributeEvenly(true);

		mTabs.setViewPager(mPager);
		mPager.setCurrentItem(0);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_view);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		prepareNavigationDrawerItems();
		setAdapter();
		// mDrawerList.setAdapter(new DrawerAdapter(this, mDrawerItems));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
				R.string.drawer_open, R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		/*
		 * if (savedInstanceState == null) {
		 * mDrawerLayout.openDrawer(mDrawerList); }
		 */
		
		mDrawerLayout.closeDrawers();
		android.support.v7.app.ActionBar actionBar=getSupportActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
	    actionBar.setIcon(android.R.color.transparent);
		
	}
    private void setAdapter() {
		String option = LEFT_MENU_OPTION_1;
		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey(LEFT_MENU_OPTION)) {
			option = extras.getString(LEFT_MENU_OPTION, LEFT_MENU_OPTION_1);
		}

		boolean isFirstType = true;

		View headerView = null;
		if (option.equals(LEFT_MENU_OPTION_1)) {
			headerView = prepareHeaderView(R.layout.header_navigation_drawer_1,
					"http://pengaja.com/uiapptemplate/avatars/0.jpg",
					"dev@csform.com");
		} else if (option.equals(LEFT_MENU_OPTION_2)) {
			headerView = prepareHeaderView(R.layout.header_navigation_drawer_2,
					"http://pengaja.com/uiapptemplate/avatars/0.jpg",
					"dev@csform.com");
			isFirstType = false;
		}

		BaseAdapter adapter = new DrawerAdapter(this, mDrawerItems, isFirstType);

		mDrawerList.addHeaderView(headerView);// Add header before adapter (for
												// pre-KitKat)
		mDrawerList.setAdapter(adapter);
	}

	private View prepareHeaderView(int layoutRes, String url, String email) {
		View headerView = getLayoutInflater().inflate(layoutRes, mDrawerList,
				false);
		ImageView iv = (ImageView) headerView.findViewById(R.id.image);
		TextView tv = (TextView) headerView.findViewById(R.id.email);

		ImageUtil.displayRoundImage(iv, url, null);
		tv.setText(email);

		return headerView;
	}

	private void prepareNavigationDrawerItems() {
		mDrawerItems = new ArrayList<>();
		mDrawerItems.add(new DrawerItem(R.string.fontello_user,
				R.string.drawer_title_home,
				DrawerItem.DRAWER_ITEM_TAG_LINKED_IN));
		mDrawerItems.add(new DrawerItem(R.string.fontello_drag_and_drop,
				R.string.drawer_title_tips, DrawerItem.DRAWER_ITEM_TAG_BLOG));
		mDrawerItems.add(new DrawerItem(R.string.drawer_icon_search_bars,
				R.string.drawer_title_search_by_category,
				DrawerItem.DRAWER_ITEM_TAG_GIT_HUB));
		mDrawerItems.add(new DrawerItem(R.string.fontello_search,
				R.string.drawer_title_advance_search,
				DrawerItem.DRAWER_ITEM_TAG_GIT_HUB));
		mDrawerItems.add(new DrawerItem(R.string.drawer_icon_splash_screens,
				R.string.drawer_title_create_alreat,
				DrawerItem.DRAWER_ITEM_TAG_GIT_HUB));
		
		
		mDrawerItems.add(new DrawerItem(R.string.drawer_icon_login_page,
				R.string.drawer_title_logout,
				DrawerItem.DRAWER_ITEM_TAG_INSTAGRAM));
	}

	

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position/* , mDrawerItems.get(position - 1).getTag() */);
		}
	}

	private void selectItem(int position/* , int drawerTag */) {
		// minus 1 because we have header that has 0 position
		if (position < 1) { // because we have header, we skip clicking on it
			return;
		}
		Intent i;

		switch (position) {
		case 1:
			 i = new Intent(getApplicationContext(), LeftMenusActivity.class);
				startActivity(i);
				finish();
			break;
		case 2:
			 i = new Intent(getApplicationContext(), Map.class);
			startActivity(i);
			finish();
			break;
		case 3:
			 i = new Intent(getApplicationContext(), MapCategory.class);
			startActivity(i);
			finish();
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			UserSessionManager session=new UserSessionManager(getApplicationContext());
			session.logoutUserAgain();
			Intent intent = new Intent(getApplicationContext(), LogInPageActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			String drawerTitle = getString(mDrawerItems.get(position - 1)
					.getTitle());
			Toast.makeText(
					this,
					"You selected " + drawerTitle + " at position: " + position,
					Toast.LENGTH_SHORT).show();

		}
		mDrawerList.setItemChecked(position, true);
		setTitle(mDrawerItems.get(position - 1).getTitle());
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(int titleId) {
		setTitle(getString(titleId));
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	class MyPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
		String[] tabs;

		public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
			super(fm);
			tabs = getResources().getStringArray(R.array.tabs);
		}

		@Override
		public int getCount() {
			return 1;
		}

		

		@Override
		public android.support.v4.app.Fragment getItem(int arg0) {
			// TODO Auto-generated method stubf
			android.support.v4.app.Fragment fragment = null;
			// MapFragmentDetail mf=null;

			switch (arg0) {
			case GOOGLEMAP:
				fragment = new MapCategoryFragment(false);
				/*
		        FragmentManager manager=getSupportFragmentManager();
		        FragmentTransaction transaction=manager.beginTransaction();

		       transaction.add(R.id.main_activity, fragment, "mapfragment");
		        transaction.commit();*/
				
			
				break;
			
			}

			return fragment;
		}
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

   