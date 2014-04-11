package com.example.vendedores;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;

import android.support.v4.content.LocalBroadcastManager;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class Login extends Activity {

	
	
	//for GCM
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private Sistema context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		context = (Sistema)getApplicationContext();
		 EditText username = (EditText)findViewById(R.id.editTextCMontoLabel);
    	 EditText password = (EditText)findViewById(R.id.editTextPassword);
    	 username.setHint("Nombre de usuario");
    	 password.setHint("Contrase�a");
    	 LoginReceiver receiver = new LoginReceiver((EditText)findViewById(R.id.LoginConectado),(Button)findViewById(R.id.btn_ingresar), (ProgressBar)findViewById(R.id.LoginProgressBar));
    	 LocalBroadcastManager.getInstance(context).registerReceiver(receiver, new IntentFilter("login"));
    	 Display display = getWindowManager().getDefaultDisplay();
    	 Point size = new Point();
    	 display.getSize(size);
    	 int width = size.x;
    	 int height = size.y;
    	 ((Sistema)getApplicationContext()).setDevice_width(width);
    	 ((Sistema)getApplicationContext()).setDevice_height(height);
    	 
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
		context.clear();
		findViewById(R.id.LoginConectado).setVisibility(View.INVISIBLE);
		if (checkPlayServices()) {
			Intent google_register_intent = new Intent(this,GoogleRegisterService.class);
			startService(google_register_intent);
		}
		((Button)findViewById(R.id.btn_ingresar)).setActivated(false);
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
	
}
