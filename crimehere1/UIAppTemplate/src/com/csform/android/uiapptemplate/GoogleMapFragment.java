package com.csform.android.uiapptemplate;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoogleMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoogleMapFragment extends Fragment implements OnCameraChangeListener,AdapterView.OnItemSelectedListener, com.google.android.gms.maps.GoogleMap.OnMarkerClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String day,month,year,category;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    GoogleMap mMap;
    double lat,lon,lat12,lon12;
    static JSONArray jArray;
    static String entityResponse;
    VisibleRegion vr;
    String markerString;
    Spinner spinnerDay,spinnerMonth,spinnerYear,spinnerCategory;
    ProgressBar progressBarMapFragmentDetail;

    double left,right,top,bottom;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param //param1 Parameter 1.
     * @param //param2 Parameter 2.
     * @return A new instance of fragment GoogleMap.
     */
    // TODO: Rename and change types and number of parameters
    public static GoogleMapFragment newInstance(String day, String month,String year, String category) {
        GoogleMapFragment fragment = new GoogleMapFragment();
        Bundle args = new Bundle();
        args.putString("days", day);
        args.putString("month", month);
        args.putString("year", year);
        args.putString("category", category);
        fragment.setArguments(args);
        return fragment;
    }

    public GoogleMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (savedInstanceState != null) {
            this.day = savedInstanceState.getString("day");
            this.month = savedInstanceState.getString("month");
            this.year = savedInstanceState.getString("year");
            this.category = savedInstanceState.getString("category");

        }
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.google_map, container, false);
        View v = inflater.inflate(R.layout.map_fragment_detail, container, false);


        LocationManager_check locationManagerCheck = new LocationManager_check(
                getActivity());
        Location location1 = null;

        if(locationManagerCheck.isLocationServiceAvailable()){

            if (locationManagerCheck.getProviderType() == 1)
                location1 = locationManagerCheck.locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            else if (locationManagerCheck.getProviderType() == 2)
                location1 = locationManagerCheck.locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }else{
            locationManagerCheck .createLocationServiceError(getActivity());
        }

        progressBarMapFragmentDetail=(ProgressBar) v.findViewById(R.id.progressBarMapFragmentDetail);
        mMap = ((SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapfragmentid)).getMap();
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setOnMarkerClickListener( new com.google.android.gms.maps.GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick ( Marker marker )
            {
                // do nothing
                return false;
            }
        });
        LatLng latlng=new LatLng(24.821387, 78.060060);
        Location loc=getCurrentLocation();
       //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 4.5f));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 15));
        mMap.setOnCameraChangeListener(this);
        vr=mMap.getProjection().getVisibleRegion();

        left = vr.latLngBounds.southwest.longitude;
        top = vr.latLngBounds.northeast.latitude;
        right = vr.latLngBounds.northeast.longitude;
        bottom = vr.latLngBounds.southwest.latitude;
        Location location = getCurrentLocation();
        try
        {

            lat = location.getLatitude();
            lon = location.getLongitude();


            markerString="your current location";
        }catch(Exception ex)
        {
            mMap.setMyLocationEnabled(true);
            location=mMap.getMyLocation();
            markerString="could not set to your current location ";
            ex.printStackTrace();
            Toast.makeText(getActivity(), "problem occur while getting current location", Toast.LENGTH_LONG).show();
        }
        latlng = new LatLng(lat, lon);
        if (mMap == null) {
            Toast.makeText(getActivity(), "something wrong with google map",
                    Toast.LENGTH_SHORT).show();
        }else {
            mMap.addMarker(new MarkerOptions().position(latlng).title(
                    markerString)).showInfoWindow();
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
        }


        return v;

    }
    @Override
    public void onCameraChange(CameraPosition arg0) {
        // TODO Auto-generated method stub
        vr=mMap.getProjection().getVisibleRegion();
        left = vr.latLngBounds.southwest.longitude;
        top = vr.latLngBounds.northeast.latitude;
        right = vr.latLngBounds.northeast.longitude;
        bottom = vr.latLngBounds.southwest.latitude;
        double zoomLevel=arg0.zoom;
        Log.i("google zoooooom", String.valueOf(left));
        Log.i("google zoooooom", String.valueOf(arg0.zoom));
        retrieveData(left,top,right,bottom,zoomLevel);


    }

    private void retrieveData(double left, double top, double right, double bottom, double zoomLevel) {
        // TODO Auto-generated method stub
        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("category", category));
        params.add(new BasicNameValuePair("zoomlevel", String.valueOf(zoomLevel)));
        params.add(new BasicNameValuePair("left", String.valueOf(left)));
        params.add(new BasicNameValuePair("top", String.valueOf(top)));
        params.add(new BasicNameValuePair("right", String.valueOf(right)));
        params.add(new BasicNameValuePair("bottom", String.valueOf(bottom)));
        params.add(new BasicNameValuePair("day", day));
        params.add(new BasicNameValuePair("month", month));
        params.add(new BasicNameValuePair("year", year));
        new AsyncTask<GoogleMapFragment, Long, Boolean>() {

            Boolean status;
            String url;

            protected void onPreExecute() {
                progressBarMapFragmentDetail.setVisibility(View.VISIBLE);
                url = getResources().getString(R.string.URL_ShowGoogleMap);
                //  url = "http://5.101.104.178/index.php";

            };

            protected void onPostExecute(Boolean result) {
                progressBarMapFragmentDetail.setVisibility(View.INVISIBLE);
                try{
                    jArray = new JSONArray(entityResponse);
                    if(jArray.length()<1)
                    {
                        Log.e("la la la", "somehow u havenot crashed");
                    }
                    else
                    {

                        for (int i = 0; i < jArray.length(); i++) {

                            JSONObject e = jArray.getJSONObject(i);
                            double lat=Double.valueOf(e.getString("lat"));
                            double lon=Double.valueOf(e.getString("lon"));
                            LatLng latlng=new LatLng(lat, lon);
                            String title=e.getString("title");
                            String description=e.getString("description");
                            String category=e.getString("category");
                            BitmapDescriptor icon;
                            if(category.trim().equalsIgnoreCase("Homicide"))
                            {
                                icon = BitmapDescriptorFactory.fromResource(R.drawable.murdericon);
                            }
                            else if(category.trim().equalsIgnoreCase("Sexual Offences"))
                            {
                                icon = BitmapDescriptorFactory.fromResource(R.drawable.sexualoffence);
                            }
                            else if(category.trim().equalsIgnoreCase("Theft"))
                            {
                                icon = BitmapDescriptorFactory.fromResource(R.drawable.theft);
                            }
                            else if(category.trim().equalsIgnoreCase("Vehical Theft"))
                            {
                                icon = BitmapDescriptorFactory.fromResource(R.drawable.vehicletheft);

                            }
                            else
                            {
                                icon=BitmapDescriptorFactory.fromResource(R.drawable.othercrime3);
                                // icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
                            }
                            mMap.addMarker(new MarkerOptions().position(latlng)
                                    .title(title)
                                    .snippet(description)
                                    .icon(icon));
                            mMap.getUiSettings().setMapToolbarEnabled(false);
                            mMap.setOnMarkerClickListener( new com.google.android.gms.maps.GoogleMap.OnMarkerClickListener()
                            {
                                @Override
                                public boolean onMarkerClick ( Marker marker )
                                {
                                    // do nothing
                                    return false;
                                }
                            });


                        }
                    }
                }catch(Exception ex)
                {
                    ex.printStackTrace();
                }



            };

            @Override
            protected Boolean doInBackground(GoogleMapFragment... mapFragmentDetail) {
                // TODO Auto-generated method stub
                try
                {
                    status = mapFragmentDetail[0].retrieveData(params, url);
                    return status;
                }catch(Exception ex)
                {

                    return false;
                }
            }

        }.execute(new GoogleMapFragment());

    }
    protected Boolean retrieveData(List<NameValuePair> params, String url) {
        // TODO Auto-generated method stub
        HttpEntity httpEntity = null;
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            entityResponse = EntityUtils.toString(httpEntity);
            Log.e("entity response", entityResponse);


        }
        catch(Exception ex)
        {

        }
        return null;

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
    private Location getCurrentLocation(){
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria,
                false);

        LocationListener loc_listener = new LocationListener() {

            public void onLocationChanged(Location l) {
            }

            public void onProviderEnabled(String p) {
            }

            public void onProviderDisabled(String p) {
            }

            public void onStatusChanged(String p, int status, Bundle extras) {
            }
        };
        locationManager.requestLocationUpdates(bestProvider, 1000, 0,
                loc_listener);
        Location location = locationManager
                .getLastKnownLocation(bestProvider);

        return location;

    }/*-------------------*/

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
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(
                                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    activityObj.startActivity(intent);
                                    alert.dismiss();
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    alert.dismiss();
                                }
                            });
            alert = builder.create();
            alert.show();
        }

    }

}
