package com.example.vendedores;

import java.io.IOException;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class GoogleRegisterService extends IntentService {

	private static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private GoogleCloudMessaging gcm;
	private String SENDER_ID = "354046703161";
	
	public GoogleRegisterService() {
		super("");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		
  	        // If this check succeeds, proceed with normal processing.
  	        // Otherwise, prompt user to get valid Play Services APK.
	        try {
	            if (gcm == null) {
	                gcm = GoogleCloudMessaging.getInstance((Sistema)getApplicationContext());
	            }
	            ((Sistema)getApplicationContext()).setRegistration_id(gcm.register(SENDER_ID));
	            
	            //sendRegistrationIdToBackend();
	            // Persist the regID - no need to register again.
	            storeRegistrationId((Sistema)getApplicationContext(), ((Sistema)getApplicationContext()).getRegistration_id());
	        } catch (IOException ex) {
	        	((Sistema)getApplicationContext()).setRegistration_id("Error :" + ex.getMessage());
	        	// If there is an error, don't just keep trying to register.
	            // Require the user to click a button again, or perform
	            // exponential back-off.
	        }
	}
	
	private void storeRegistrationId(Context context, String regId) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    int appVersion = getAppVersion(context);
	  
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(PROPERTY_REG_ID, regId);
	    editor.putInt(PROPERTY_APP_VERSION, appVersion);
	    editor.commit();
	}
	
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
	
	private SharedPreferences getGCMPreferences(Context context) {
	    // This sample app persists the registration ID in shared preferences, but
	    // how you store the regID in your app is up to you.
	    return getSharedPreferences(GCMActivity.class.getSimpleName(),
    		            Context.MODE_PRIVATE);
	}

}
