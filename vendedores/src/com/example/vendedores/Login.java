package com.example.vendedores;

import java.io.IOException;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class Login extends Activity {

	
	
	//for GCM
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	
	private String registrationID;
	private GoogleCloudMessaging gcm;
	private String SENDER_ID = "354046703161";

	private Sistema context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		context = (Sistema)getApplicationContext();
		 EditText username = (EditText)findViewById(R.id.editTextCMontoLabel);
    	 EditText password = (EditText)findViewById(R.id.editTextPassword);
    	 username.setHint("Nombre de usuario");
    	 password.setHint("Contraseña");
    	 LoginReceiver receiver = new LoginReceiver((EditText)findViewById(R.id.LoginConectado),(Button)findViewById(R.id.btn_ingresar), (ProgressBar)findViewById(R.id.LoginProgressBar));
    	 LocalBroadcastManager.getInstance(context).registerReceiver(receiver, new IntentFilter("login"));
    	 /*try{//checkeo si vengo de una notificacion
	    	 if(getIntent().getExtras().getString("mensaje")!=null)
	    	 {
	    		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage(getIntent().getExtras().getString("mensaje"));
					builder.setTitle(getIntent().getExtras().getString("titulo"))
					        .setCancelable(false)
					        .setPositiveButton("Aceptar",
					                new DialogInterface.OnClickListener() {
					                    public void onClick(DialogInterface dialog, int id) {
					                     try{
					                    	
					                    	            dialog.cancel();
								                        
								                    							               
					                     }catch(Exception e){}
					                    }
					                });
					AlertDialog alert = builder.create();
					alert.show();
				
	    		 
	    	 } 
    	 }catch(Exception e)
    	 {
    		 
    	 }*/
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

	public boolean checkPlayServices() {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	        	GooglePlayServicesUtil.getErrorDialog(resultCode, this,
	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        } else {
	         
	            finish();
	        }
	        return false;
	    }
	    return true;
	}
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}*/
	
	
	public void post(View view) {
		findViewById(R.id.LoginProgressBar).setVisibility(View.VISIBLE);
		Button b = (Button)findViewById(R.id.btn_ingresar);
		b.setClickable(false);
		b.setActivated(true);
		Intent login_service_intent = new Intent(this,ServicioLogin.class);
		login_service_intent.putExtra("username", ((EditText)findViewById(R.id.editTextCMontoLabel)).getText().toString());
		login_service_intent.putExtra("password",((EditText)findViewById(R.id.editTextPassword)).getText().toString());
		startService(login_service_intent);
    }
	
	@Override
	public void onDestroy() {
	    super.onDestroy();  // Always call the superclass
	    
	    // Stop method tracing that the activity started during onCreate()
	    
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		findViewById(R.id.LoginConectado).setVisibility(View.INVISIBLE);
		if (checkPlayServices()) {
  	        // If this check succeeds, proceed with normal processing.
  	        // Otherwise, prompt user to get valid Play Services APK.
	        try {
	            if (gcm == null) {
	                gcm = GoogleCloudMessaging.getInstance((Sistema)getApplicationContext());
	            }
	            registrationID = gcm.register(SENDER_ID);
	            
	            //sendRegistrationIdToBackend();
	            // Persist the regID - no need to register again.
	            storeRegistrationId((Sistema)getApplicationContext(), registrationID);
	        } catch (IOException ex) {
	        	registrationID = "Error :" + ex.getMessage();
	        	// If there is an error, don't just keep trying to register.
	            // Require the user to click a button again, or perform
	            // exponential back-off.
	        }
 		}
		((Button)findViewById(R.id.btn_ingresar)).setActivated(false);
	}
	
}
