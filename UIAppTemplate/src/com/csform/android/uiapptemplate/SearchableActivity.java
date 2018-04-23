package com.csform.android.uiapptemplate;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SearchableActivity extends Activity{
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     
        Bundle b=getIntent().getExtras();
        String searchAddress= b.getString("searchAddress");
        Intent i=new Intent(getApplicationContext(), LeftMenusActivity.class);
        Bundle b1=new Bundle();
        
        b1.putString("searchAddress",searchAddress);
        Log.e("Searchable Activity", searchAddress);
        i.putExtras(b1);
        startActivity(i);
        finish();
    }
 
   
}
