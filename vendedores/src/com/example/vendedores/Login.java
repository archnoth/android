package com.example.vendedores;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dominio.Cliente;
import com.example.dominio.Producto;
import com.example.dominio.Usuario;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

import android.R.menu;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.support.v4.view.MenuItemCompat;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

public class Login extends Activity {

	public Usuario usuario=null;
	
	//for GCM
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static final String PROPERTY_REG_ID = "registration_id";
	
	String registrationID;
	GoogleCloudMessaging gcm;
	String SENDER_ID = "354046703161";

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
    	 
    	 if (checkPlayServices()) {
 	        // If this check succeeds, proceed with normal processing.
 	        // Otherwise, prompt user to get valid Play Services APK.
    		startService(new Intent(this, ServicioGoogleRegister.class));
 	    }
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
    	Button b = (Button)findViewById(R.id.btn_ingresar);
		b.setClickable(false);
		b.setActivated(true);
		findViewById(R.id.LoginSeekBar).setVisibility(View.VISIBLE);
		Intent login_service_intent = new Intent(this,ServicioLogin.class);
		login_service_intent.putExtra("nombreUsuario", ((EditText)findViewById(R.id.editTextCMontoLabel)).getText().toString());
		login_service_intent.putExtra("password", ((EditText)findViewById(R.id.editTextPassword)).getText().toString());
		startService(login_service_intent);
		
		
		// Luego de intentar logearse
		if(usuario.getNombreUsuario()!=null)
		{
			findViewById(R.id.LoginConectado).setVisibility(View.VISIBLE);
			Intent loc = new Intent(getApplicationContext(),ListadoClientes.class); 
	        loc.putExtra("usuario",usuario); 
	        loc.putExtra("descuento_contado",context.getDescuento_contado());
	        startActivity(loc);
		}
		b.setActivated(false);
		b.setClickable(true);
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
		((Button)findViewById(R.id.btn_ingresar)).setActivated(false);
	}
	
}
