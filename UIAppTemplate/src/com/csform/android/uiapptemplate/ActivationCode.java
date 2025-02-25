package com.csform.android.uiapptemplate;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
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
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.csform.android.uiapptemplate.view.FloatLabeledEditText;

public class ActivationCode extends Activity implements OnClickListener {

	public static final String LOGIN_PAGE_AND_LOADERS_CATEGORY = "com.csform.android.uiapptemplate.LogInPageAndLoadersActivity";
	public static final String DARK = "Dark";
	public static final String LIGHT = "Light";

	String message;
	TextView activation, back;
	private com.csform.android.uiapptemplate.view.FloatLabeledEditText editTextActivationCde,editTextActivationPhone;
	
	ProgressBar progressBarMapFragmentDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE); //Removing ActionBar
	
		String category = LIGHT;
		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey(LOGIN_PAGE_AND_LOADERS_CATEGORY)) {
			category = extras.getString(LOGIN_PAGE_AND_LOADERS_CATEGORY, LIGHT);
		}
		setContentView(category);
	}
	
	private void setContentView(String category) {
		if (category.equals(DARK)) {
			setContentView(R.layout.activity_login_page_dark);
		} else if (category.equals(LIGHT)) {
			setContentView(R.layout.activity_activation_code);
			
		}
		
		
		 progressBarMapFragmentDetail = (ProgressBar) 
					findViewById(R.id.progressBarMapFragmentDetail2);
		
		editTextActivationCde=(FloatLabeledEditText) findViewById(R.id.editTextActivationCode);
		editTextActivationPhone=(FloatLabeledEditText) findViewById(R.id.editTextActivationPhone);
		UserSessionManager session=new UserSessionManager(getApplicationContext());
		editTextActivationPhone.setText(session.getPhone());
		back = (TextView) findViewById(R.id.back_activation);
		activation = (TextView) findViewById(R.id.activate);
		
		back.setOnClickListener(this);
		activation.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		/*if (v instanceof TextView) {
			TextView tv = (TextView) v;
			Toast.makeText(this, tv.getText(), Toast.LENGTH_SHORT).show();
		}*/
		if(v.getId()==R.id.activate)
		{
			
			String stringActivationCde=editTextActivationCde.getText().toString().trim();
			String stringPhone=editTextActivationPhone.getText().toString().trim();
			  
			activate(stringActivationCde,stringPhone);
			
		}
		if(v.getId()==R.id.back_activation)
		{
		    Intent i=new Intent(getApplicationContext(), LogInPageActivity.class);
		    startActivity(i);
		    finish();
		}
		if(v.getId()==R.id.register)
		{
			startActivity(new Intent(getApplicationContext(), Registration.class));
		}
		if(v.getId()==R.id.skip)
		{
			
		}
	}

	private void activate(final String stringActivationCode,final String stringPhone) {
		// TODO Auto-generated method stub
		new AsyncTask<Void, Long, Boolean>() {

			Boolean status;
			String url;

			protected void onPreExecute() {

				url = getResources().getString(R.string.URL_ACTIVATION);

				progressBarMapFragmentDetail.setVisibility(View.VISIBLE);
			};

			protected void onPostExecute(Boolean result) {
				progressBarMapFragmentDetail.setVisibility(View.INVISIBLE);
				if(result)
				{
					showAlear();
				}
				else{
				Toast.makeText(getApplicationContext(), "wrong activation code", 0).show();
				}
				
			};

			@Override
			protected Boolean doInBackground(Void... added_item) {
				// TODO Auto-generated method stub
				status = retrieveData(url, stringActivationCode,stringPhone);
				return status;
			}

			

		}.execute(null, null, null);


	}

	protected Boolean retrieveData(String url, String stringActivationCode,String stringPhone) {
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

			
			final List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("User_Phone", String.valueOf(stringPhone)));
			params.add(new BasicNameValuePair("AuthCode", String.valueOf(stringActivationCode)));
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpPost);
			httpEntity = httpResponse.getEntity();
			String entityResponse = EntityUtils.toString(httpEntity);
			Log.e("entity response", entityResponse);

			
			JSONObject json=new JSONObject(entityResponse);
							
			if(json.getString("Status").equalsIgnoreCase("Success"))
			{
			
				message=json.getString("Message");
				return true;
			}
			else
			{
				return false;
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;

		}

	
	}
	public void showAlear()
	{
		new AlertDialog.Builder(ActivationCode.this)
		.setTitle("Activation")
		.setMessage(
				message)
		.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface dialog,
							int which) {
						// continue with delete
						Intent i = new Intent(
								getApplicationContext(),
								LogInPageActivity.class);
											startActivity(i);
						finish();
					}
				})
		.setIcon(android.R.drawable.ic_dialog_alert)
		.show();
	}
}
