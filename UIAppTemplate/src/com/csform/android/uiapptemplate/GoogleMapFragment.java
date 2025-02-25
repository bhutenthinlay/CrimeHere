package com.csform.android.uiapptemplate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.csform.android.uiapptemplate.adapter.MyStickyListHeadersAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

/**
 * A simple {@link Fragment} subclass. Use the
 * {@link GoogleMapFragment#newInstance} factory method to create an instance of
 * this fragment.
 */
public class GoogleMapFragment extends Fragment implements
		OnCameraChangeListener, AdapterView.OnItemSelectedListener,
		com.google.android.gms.maps.GoogleMap.OnMarkerClickListener,
		OnMapLongClickListener {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	static UserSessionManager session;
	String title;
	String user;
	String type_name;
	String description;
	String address;
	String image0, image1, image2;
	String dateoccured;
	String id;
    
	static public ArrayList<HashMap<String, String>> mylist;
	Marker newMarker;
	GoogleMap mMap;
	static public double lat, lon, lat12, lon12;
	static JSONArray jArray;
	static String entityResponse;
	VisibleRegion vr;
	String markerString;
	ProgressBar progressBarMapFragmentDetail;
	String searchAddress = null;
	String dateOccur;
	Location location1 = null;

	static public double left, right, top, bottom;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param //param1 Parameter 1.
	 * @param //param2 Parameter 2.
	 * @return A new instance of fragment GoogleMap.
	 */
	// TODO: Rename and change types and number of parameters

	public GoogleMapFragment(String searchAddress) {
		this.searchAddress = searchAddress;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			this.searchAddress = savedInstanceState.getString("searchAddress");

		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		// return inflater.inflate(R.layout.google_map, container, false);
		mylist = new ArrayList<HashMap<String, String>>();
		
		if (savedInstanceState != null) {

			searchAddress = savedInstanceState.getString("searchAddress");
			Log.e("searchAddress", searchAddress);
		}
		View v = inflater.inflate(R.layout.map_fragment_detail, container,
				false);

		progressBarMapFragmentDetail = (ProgressBar) v
				.findViewById(R.id.progressBarMapFragmentDetail);
		LocationManager_check locationManagerCheck = new LocationManager_check(
				getActivity());
		mMap = ((SupportMapFragment) this.getChildFragmentManager()
				.findFragmentById(R.id.mapfragmentid)).getMap();

		mMap.setInfoWindowAdapter(new InfoWindowAdapter() {

			@Override
			public View getInfoWindow(Marker arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public View getInfoContents(Marker arg0) {
				// TODO Auto-generated method stub
				LatLng l = arg0.getPosition();
				Double lat12 = l.latitude;
				Double lon12 = l.longitude;

				newMarker = arg0;
				if (lat12 == location1.getLatitude()
						&& lon12 == location1.getLongitude()) {
					Toast.makeText(getActivity(),
							"This is your current location", Toast.LENGTH_LONG)
							.show();
					return null;
				} else {

					final View v = getLayoutInflater(null).inflate(
							R.layout.windowlayout, null);
					// Getting the position from the marker
					LatLng latLng = arg0.getPosition();
					DatabaseOperations dbo = new DatabaseOperations(
							getActivity());
					Cursor c = dbo.getInformationByLatLng(dbo, latLng.latitude,
							latLng.longitude);
					c.moveToFirst();
					type_name = c.getString(c.getColumnIndex("type_name"));
					description = c.getString(c.getColumnIndex("description"));
					title = c.getString(c.getColumnIndex("title"));
					address = c.getString(c.getColumnIndex("address"));
					image0 = c.getString(c.getColumnIndex("image0"));
					image1 = c.getString(c.getColumnIndex("image1"));
					image2 = c.getString(c.getColumnIndex("image2"));
					dateoccured = c.getString(c.getColumnIndex("date_occured"));
					id = c.getString(c.getColumnIndex("id"));
					user = c.getString(c.getColumnIndex("user_login"));
					
					SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    SimpleDateFormat myFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat myFormat2 = new SimpleDateFormat("h:mma");
                    try {
                        String reformattedStr1 = myFormat1.format(fromUser.parse(dateoccured));
                        String reformattedStr2 = myFormat2.format(fromUser.parse(dateoccured)).toLowerCase();
                        dateOccur=reformattedStr1+" at "+reformattedStr2;
                        TextView date = (TextView) v.findViewById(R.id.dateoccuredwindow);
                        date.setText(dateOccur);
                        
                        
                      
                        
                        
                        
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
					
					
					
					ImageLoader imageLoader = new ImageLoader(getActivity());

					ImageView image = (ImageView) v
							.findViewById(R.id.imagewindow);
					// Getting reference to the TextView to set latitude
					TextView type = (TextView) v
							.findViewById(R.id.crime_type_window);

					// Getting reference to the TextView to set longitude
					TextView desc = (TextView) v
							.findViewById(R.id.descriptionwindow);

					TextView address1 = (TextView) v
							.findViewById(R.id.addresswindow);

					// Getting reference to the TextView to set longitude
					TextView date = (TextView) v
							.findViewById(R.id.dateoccuredwindow);

					// Setting the latitude
					type.setText(title);

					// Setting the longitude
					desc.setText(description);
					address1.setText(address);
					
					c.close();
					// Returning the view containing InfoWindow contents

					return v;
				}
			}

		});

		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker marker) {

				// Add the bundle to the intent
				Bundle b = new Bundle();
				b.putDouble("lat", newMarker.getPosition().latitude);
				b.putDouble("lng", newMarker.getPosition().longitude);
				b.putString("title", title);
				b.putString("description", description);
				b.putString("user_login", user);
				b.putString("image0", image0);
				b.putString("image1", image1);
				b.putString("image2", image2);
				b.putString("type_name", type_name);
				b.putString("identifier", id);
				b.putString("address", address);
				b.putString("date_occured", dateOccur);

				Intent i = new Intent(getActivity(), ParticularInfoOnMap.class);
				i.putExtras(b);
				startActivity(i);
			}
		});

		// india location
		LatLng latlng = new LatLng(24.821387, 78.060060);

		if (locationManagerCheck.isLocationServiceAvailable()) {

			if (locationManagerCheck.getProviderType() == 1)

				location1 = locationManagerCheck.locationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			else if (locationManagerCheck.getProviderType() == 2)
				location1 = locationManagerCheck.locationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (searchAddress == null) {
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
						location1.getLatitude(), location1.getLongitude()), 15));
				lat = location1.getAltitude();
				lon = location1.getLongitude();
				markerString = "your current location";
				session = new UserSessionManager(getActivity());
				session.setLastLocation(String.valueOf(lat),
						String.valueOf(lon));

			} else {
				Geocoder coder = new Geocoder(getActivity());
				try {
					ArrayList<Address> adresses = (ArrayList<Address>) coder
							.getFromLocationName(searchAddress, 1);
					for (Address add : adresses) {

						double longitude = add.getLongitude();
						double latitude = add.getLatitude();

						mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
								new LatLng(latitude, longitude), 7));
						markerString = "your location " + searchAddress;
					}
				} catch (IOException e) {
					e.printStackTrace();
					mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
							new LatLng(24.821387, 78.060060), 5));
					markerString = "cannot locate to address " + searchAddress;
				}
			}
		} else {

			locationManagerCheck.createLocationServiceError(getActivity());
		}

		progressBarMapFragmentDetail = (ProgressBar) v
				.findViewById(R.id.progressBarMapFragmentDetail);
		mMap.getUiSettings().setRotateGesturesEnabled(false);
		mMap.getUiSettings().setMapToolbarEnabled(false);
		mMap.setOnMarkerClickListener(new com.google.android.gms.maps.GoogleMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				// do nothing
				return false;
			}
		});

		// mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 4.5f));

		mMap.setOnCameraChangeListener(this);
		vr = mMap.getProjection().getVisibleRegion();

		left = vr.latLngBounds.southwest.longitude;
		top = vr.latLngBounds.northeast.latitude;
		right = vr.latLngBounds.northeast.longitude;
		bottom = vr.latLngBounds.southwest.latitude;
		try {

			lat = location1.getLatitude();
			lon = location1.getLongitude();

			session = new UserSessionManager(getActivity());
			session.setLastLocation(String.valueOf(lat), String.valueOf(lon));

		} catch (Exception ex) {
			mMap.setMyLocationEnabled(true);

			mMap.addMarker(new MarkerOptions().position(latlng).title(
					"could not set to your current location "));
			ex.printStackTrace();
			Toast.makeText(getActivity(),
					"problem occur while getting current location",
					Toast.LENGTH_LONG).show();
		}
		latlng = new LatLng(lat, lon);
		if (mMap == null) {
			Toast.makeText(getActivity(), "something wrong with google map",
					Toast.LENGTH_SHORT).show();
		} else {
			mMap.addMarker(new MarkerOptions().position(latlng).title(
					markerString));
			// mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
		}

		// getCrime(bottom, top, left, right);return v;
		String url = getResources().getString(R.string.URL_ShowItem);
		String a = String.valueOf(bottom);
		String b = String.valueOf(top);
		String c = String.valueOf(left);
		String d = String.valueOf(right);
		GetCrime getCrime = new GetCrime();
	    if(getCrime.getStatus().equals(AsyncTask.Status.RUNNING))
		 {
		     getCrime.cancel(true);
		    
		 }
		String[] args = { url, a, b, c, d };
		getCrime.execute(args);
		return v;

	}

	/*
	 * private void getCrime(final double a, final double b, final double c,
	 * final double d) { // TODO Auto-generated method stub new AsyncTask<Void,
	 * Long, Boolean>() {
	 * 
	 * Boolean status; String url;
	 * 
	 * protected void onPreExecute() {
	 * 
	 * url = getResources().getString(R.string.URL_ShowItem);
	 * 
	 * progressBarMapFragmentDetail.setVisibility(View.VISIBLE); };
	 * 
	 * protected void onPostExecute(Boolean result) {
	 * 
	 * try {
	 * 
	 * StickyListHeadersActivity a = new StickyListHeadersActivity( bottom, top,
	 * left, right); StickyListHeadersActivity.adapter.clear();
	 * StickyListHeadersActivity.adapter = new MyStickyListHeadersAdapter(
	 * getActivity(), mylist);
	 * StickyListHeadersActivity.adapter.notifyDataSetChanged(); a.init();
	 * 
	 * } catch (Exception ex) {
	 * 
	 * }
	 * 
	 * progressBarMapFragmentDetail.setVisibility(View.INVISIBLE);
	 * 
	 * try { DatabaseOperations dop = new DatabaseOperations( getActivity());
	 * for (int i = 0; i < mylist.size(); i++) { String id =
	 * mylist.get(i).get("id"); String identifier =
	 * mylist.get(i).get("identifier"); String title =
	 * mylist.get(i).get("title"); String description =
	 * mylist.get(i).get("description"); String type_name =
	 * mylist.get(i).get("type_name"); String address =
	 * mylist.get(i).get("address"); double lat =
	 * Double.parseDouble(mylist.get(i) .get("lat")); double lng =
	 * Double.parseDouble(mylist.get(i) .get("lng")); String image0 =
	 * mylist.get(i).get("image0"); String image1 = mylist.get(i).get("image1");
	 * String image2 = mylist.get(i).get("image2"); String user_login =
	 * mylist.get(i).get("user_login"); String date_occured =
	 * mylist.get(i).get("date_occured");
	 * 
	 * LatLng latlng = new LatLng(Double.parseDouble(mylist .get(i).get("lat")),
	 * Double.parseDouble(mylist .get(i).get("lng")));
	 * 
	 * mMap.addMarker(new MarkerOptions() .position(latlng)
	 * .title(mylist.get(i).get("title")) .snippet(
	 * mylist.get(i).get("description") + " " + mylist.get(i).get(
	 * "date_occured"))); // .setSnippet(CR.getString(3)
	 * 
	 * try { dop.putInformation(dop, id, identifier, title, description,
	 * type_name, address, lat, lng, image0, image1, image2, user_login,
	 * date_occured); } catch (Exception ex) {
	 * 
	 * } dop.close(); } } catch (Exception ex) {
	 * 
	 * }
	 * 
	 * };
	 * 
	 * @Override protected Boolean doInBackground(Void... added_item) { // TODO
	 * Auto-generated method stub status = retrieveData(url, a, b, c, d); return
	 * status; }
	 * 
	 * }.execute(null, null, null);
	 * 
	 * }
	 */

	protected Boolean retrieveData(String url, double a, double b, double c,
			double d) {
		// TODO Auto-generated method stub
		SSLContext ctx = null;
		try {
			ctx = SSLContext.getInstance("TLS");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ctx.init(null, new TrustManager[] { new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[] {};
				}
			} }, null);
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

			@Override
			public boolean verify(String hostname, SSLSession session) {
				// TODO Auto-generated method stub
				return true;
			}

		});
		HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
		HttpEntity httpEntity = null;
		// TODO Auto-generated method stub
		try {
			try {
				mylist.clear();
			} catch (Exception ex) {

			}

			final List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("a", String.valueOf(a)));
			params.add(new BasicNameValuePair("b", String.valueOf(b)));
			params.add(new BasicNameValuePair("c", String.valueOf(c)));
			params.add(new BasicNameValuePair("d", String.valueOf(d)));
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			httpEntity = httpResponse.getEntity();
			String entityResponse = EntityUtils.toString(httpEntity);
			Log.e("entity response", entityResponse);

			JSONArray jArray = new JSONArray(entityResponse);
			if (jArray.length() < 1) {
				return false;
			} else {
				for (int i = 0; i < jArray.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					JSONObject e = jArray.getJSONObject(i);
					map.put("id", e.getString("id"));
					map.put("identifier", e.getString("identifier"));
					map.put("title", e.getString("title"));
					map.put("description", e.getString("description"));
					map.put("type_name", e.getString("type_name"));
					map.put("address", e.getString("address"));
					map.put("lat", e.getString("lat"));
					map.put("lng", e.getString("lng"));
					map.put("image0", e.getString("image0"));
					map.put("image1", e.getString("image1"));
					map.put("image2", e.getString("image2"));
					map.put("user_login", e.getString("user_login"));
					map.put("date_occured", e.getString("date_occured"));

					mylist.add(map);
				}

				return true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;

		}

	}

	@Override
	public void onCameraChange(CameraPosition arg0) {
		// TODO Auto-generated method stub
		vr = mMap.getProjection().getVisibleRegion();
		left = vr.latLngBounds.southwest.longitude;
		top = vr.latLngBounds.northeast.latitude;
		right = vr.latLngBounds.northeast.longitude;
		bottom = vr.latLngBounds.southwest.latitude;
		Log.i("google zoooooom", String.valueOf(left));
		Log.i("google zoooooom", String.valueOf(arg0.zoom));
		try {
			mylist.clear();
		} catch (Exception ex) {

		}
		// getCrime(bottom, top, left, right);
		String url = getResources().getString(R.string.URL_ShowItem);
		String a = String.valueOf(bottom);
		String b = String.valueOf(top);
		String c = String.valueOf(left);
		String d = String.valueOf(right);
		String[] args = { url, a, b, c, d };
		GetCrime getCrime = new GetCrime();
		if(getCrime.getStatus().equals(AsyncTask.Status.RUNNING))
		 {
		     getCrime.cancel(true);
		 }
		getCrime.execute(args);

	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		return false;
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
					.setTitle("Turn On")
					.setCancelable(false)
					.setPositiveButton("Settings",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Intent intent = new Intent(
											Settings.ACTION_LOCATION_SOURCE_SETTINGS);
									activityObj.startActivity(intent);
									alert.dismiss();
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

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapLongClick(LatLng arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "clicked", 0).show();
	}

	private class GetCrime extends AsyncTask<String, Void, Boolean> {
		String url;
		String a, b, c, d;
		boolean status;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressBarMapFragmentDetail.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		protected Boolean doInBackground(String... args) {
			this.url = args[0];
			this.a = args[1];
			this.b = args[2];
			this.c = args[3];
			this.d = args[4];
			URL newurl = null;
			status = retrieveData(url, Double.parseDouble(a),
					Double.parseDouble(b), Double.parseDouble(c),
					Double.parseDouble(d));
			return status;

		}

		// @Override
		protected void onPostExecute(Boolean result1) {
			try {

				StickyListHeadersActivity a = new StickyListHeadersActivity(
						bottom, top, left, right);
				StickyListHeadersActivity.adapter.clear();
				StickyListHeadersActivity.adapter = new MyStickyListHeadersAdapter(
						getActivity(), mylist);
				StickyListHeadersActivity.adapter.notifyDataSetChanged();
				a.init();

			} catch (Exception ex) {

			}

			progressBarMapFragmentDetail.setVisibility(View.INVISIBLE);

			try {
				DatabaseOperations dop = new DatabaseOperations(getActivity());
				for (int i = 0; i < mylist.size(); i++) {
					String id = mylist.get(i).get("id");
					String identifier = mylist.get(i).get("identifier");
					String title = mylist.get(i).get("title");
					String description = mylist.get(i).get("description");
					String type_name = mylist.get(i).get("type_name");
					String address = mylist.get(i).get("address");
					double lat = Double.parseDouble(mylist.get(i).get("lat"));
					double lng = Double.parseDouble(mylist.get(i).get("lng"));
					String image0 = mylist.get(i).get("image0");
					String image1 = mylist.get(i).get("image1");
					String image2 = mylist.get(i).get("image2");
					String user_login = mylist.get(i).get("user_login");
					String date_occured = mylist.get(i).get("date_occured");

					LatLng latlng = new LatLng(Double.parseDouble(mylist.get(i)
							.get("lat")), Double.parseDouble(mylist.get(i).get(
							"lng")));

					
					BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.crimehere);
					mMap.addMarker(new MarkerOptions()
							.position(latlng)
							.title(mylist.get(i).get("title"))
							.icon(icon)
							.snippet(
									mylist.get(i).get("description") + " "
											+ mylist.get(i).get("date_occured")));
					// .setSnippet(CR.getString(3)

					try {
						dop.putInformation(dop, id, identifier, title,
								description, type_name, address, lat, lng,
								image0, image1, image2, user_login,
								date_occured);
					} catch (Exception ex) {

					}
					dop.close();
				}
			} catch (Exception ex) {

			}

		}
	}

}
