package com.csform.android.uiapptemplate;

import java.util.HashMap;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserSessionManager {
    
    // Shared Preferences reference
    SharedPreferences pref;
     
    // Editor reference for Shared preferences
    Editor editor;
     
    // Context
    Context _context;
     
    // Shared pref mode
    int PRIVATE_MODE = 0;
     
    // Sharedpref file name
    private static final String PREFER_NAME = "AndroidExamplePref";
     
    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
     
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    public static final String KEY_CODE = "code";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_ID = "id";
    public static final String KEY_PHONE = "phone";
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    public static final String LAST_LAT = "lat";
    public static final String LAST_LON = "lon";
    // Constructor
    public UserSessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLastLocation(String lat,String lon)
    {
    	editor.putString(LAST_LAT, lat);
    	editor.putString(LAST_LON, lon);
    	editor.commit();
    }
    

    public void setCode(int code)
    {
    	editor.putInt(KEY_CODE, code);
    	editor.commit();
    	
    }
    public int getCode()
    {
    	return pref.getInt(KEY_CODE, 0);
    }
    
    public void setEmail(int email)
    {
    	editor.putInt(KEY_EMAIL, email);
    	editor.commit();
    	
    }
    public int getEmail()
    {
    	return pref.getInt(KEY_EMAIL, 0);
    }
    
    
    public void setToken(String token)
    {
    	editor.putString(KEY_TOKEN, token);
    	editor.commit();
    	
    }
    public String getToken()
    {
    	return pref.getString(KEY_TOKEN, "");
    }
    

    public void setPhone(String phone)
    {
    	editor.putString(KEY_PHONE, phone);
    	editor.commit();
    	
    }
    public String getPhone()
    {
    	return pref.getString(KEY_PHONE, "");
    }
  
    public void setID(String id)
    {
    	editor.putString(KEY_ID, id);
    	editor.commit();
    	
    }
    public String getID()
    {
    	return pref.getString(KEY_ID, "");
    }
    

    public void loginWithNoPassword(String name)
    {
    	editor.putString(KEY_NAME, name);
    	editor.commit();
    }
     
    //Create login session
    public void createUserLoginSession(String name){
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);
         
        // Storing name in pref
        editor.putString(KEY_NAME, name);
         
        // Storing email in pref
        //editor.putString(KEY_EMAIL, email);
         
        // commit changes
        editor.commit();
    } 
    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */
    
    public String  getUsername()
    {
    	return pref.getString(KEY_NAME, "");
    }
    
   
    public LatLng getLastLocation()
    {
    	LatLng latlng = null;
    	try
    	{
    		if(pref.getString(LAST_LAT, null).equals(null))
    		{
    			latlng=new LatLng(0, 0);
    			return  latlng;
    		}
    		else
    		{
    			double lat=Double.valueOf(pref.getString(LAST_LAT, "0"));
    			double lon=Double.valueOf(pref.getString(LAST_LON, "0"));
    			latlng=new LatLng(lat, lon);
    			return latlng;
    		}
    	}catch(Exception ex)
    	{
    		latlng=new LatLng(0, 0);
			return  latlng;
    	}
    }
    /**
     * @return
     */
    public boolean checkLogin(){
    	// Check login status
    	try{
    		if(pref.getString(KEY_NAME, null).equals("no_user_login_in_preference"))
        	{
        		/*Intent i = new Intent(_context, LoginActivity.class);
                
                // Closing all the Activities from stack
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 
                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 
                // Staring Login Activity
                _context.startActivity(i);
                return true;*/
        	}
    	}catch(NullPointerException ex)
    	{/*
    		Intent i = new Intent(_context, LoginActivity.class);
            
            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             
            // Staring Login Activity
            _context.startActivity(i);
            return true;*/
    	}
    		
    		if(!pref.getBoolean(IS_USER_LOGIN, false)){
                
             /*   // user is not logged in redirect him to Login Activity
                Intent i = new Intent(_context, LoginActivity2.class);
                 
                // Closing all the Activities from stack
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 
                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 
                // Staring Login Activity
                _context.startActivity(i);
*/                 
                return true;
            }
        return false;
    }
     
     
     
    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
         
        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();
         
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
         
        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
         
        // return user
        return user;
    }
     
    /**
     * Clear session details
     * */
    public void logoutUser(){
         
        // Clearing all user data from Shared Preferences
    	// editor.clear();
        //editor.commit();
         
    	editor.putBoolean(IS_USER_LOGIN, false);
    	editor.commit();
        // After logout redirect user to Login Activity
/*        Intent i = new Intent(_context, LoginActivity2.class);
         
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         
        // Staring Login Activity
        _context.startActivity(i);
*/    }
     public void logoutUserAgain()
     {
    	 editor.clear();
    	 editor.putString(KEY_NAME, "no_user_login_in_preference");
         editor.commit();
      /*   Intent i=new Intent(_context, LoginActivity.class);
         i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         
         // Add new Flag to start new Activity
         i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          
         // Staring Login Activity
         _context.startActivity(i);
      */    
     }
     
     
    // Check for login
    public boolean isUserLoggedIn(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

	public void logout() {
		// TODO Auto-generated method stub
		editor.clear();
        editor.commit();
	}
}