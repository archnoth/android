package com.example.vendedores;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

public class Login extends Activity {

	public Usuario usuario=null;
	
	//for GCM
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private GCMActivity googleGCMClient;
	private Integer decuento_contado;
	
	String registrationID;
	GoogleCloudMessaging gcm;
	String SENDER_ID = "354046703161";

	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		context = getApplicationContext();
		 EditText username = (EditText)findViewById(R.id.editText1);
    	 EditText password = (EditText)findViewById(R.id.editTextPassword);
    	 username.setHint("Nombre de usuario");
    	 password.setHint("Contraseña");
    	 
    	 try{//checkeo si vengo de una notificacion
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
    		 
    	 }
    	 
    	 if (checkPlayServices()) {
 	        // If this check succeeds, proceed with normal processing.
 	        // Otherwise, prompt user to get valid Play Services APK.
    		 registerInBackground();//llamo un proceso en backgroud para cargar los productos de la empresa
    		 
 	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public void post(View view) {
    	Button b = (Button)findViewById(R.id.btn_ingresar);
		b.setClickable(false);
		findViewById(R.id.LoginSeekBar).setVisibility(View.VISIBLE);
		new LongRunningGetIO().execute();
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
		
	}
	
	private class LongRunningGetIO extends AsyncTask <Void, Void, String> {
		
		protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
	       InputStream in = entity.getContent();
	         StringBuffer out = new StringBuffer();
	         int n = 1;
	         while (n>0) {
	             byte[] b = new byte[4096];
	             n =  in.read(b);
	             if (n>0) out.append(new String(b, 0, n));
	         }
	         return out.toString();
	    }
		
		@Override
		protected String doInBackground(Void... params) {
			 HttpClient httpClient = new DefaultHttpClient();
			 HttpContext localContext = new BasicHttpContext();
             HttpPost httpPost = new HttpPost("http://ventas.jm-ga.com/api/login/");
             
             // Add your data
        	 EditText username = (EditText)findViewById(R.id.editText1);
        	 EditText password = (EditText)findViewById(R.id.editTextPassword);

             Usuario usuario_login = new Usuario("","",username.getText().toString(),password.getText().toString(),"","",registrationID);
        	 Gson gson = new Gson();
             String dataString = gson.toJson(usuario_login, usuario_login.getClass()).toString();
             
             // Execute HTTP Post Request
             String text = null;
             try {
            	 StringEntity se = new StringEntity(dataString);
            	 se.setContentEncoding("UTF-8");
            	 se.setContentType("application/json");
            	 httpPost.setEntity(se);
            	 ((SeekBar)findViewById(R.id.LoginSeekBar)).setProgress(10);
            	 HttpResponse response = httpClient.execute(httpPost, localContext);
            	 ((SeekBar)findViewById(R.id.LoginSeekBar)).setProgress(40);
            	 HttpEntity entity = response.getEntity();
                   
                   text = getASCIIContentFromEntity(entity);
                   
             } catch (Exception e) {
            	 return e.getLocalizedMessage();
             }
             return text; 
        
	}	
		
		protected void onPostExecute(String results) {
			if (results!=null) {			
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(results);				
					String api_key = jsonObject.getString("api_key");
					//String device_id=jsonObject.getString("device_id");
					String username=jsonObject.getString("username");
					
					String jsonConfigs=jsonObject.getString("configuraciones");
					JSONArray jarrayConfgs=new JSONArray(jsonConfigs);
					
					for(int i=0;i<jarrayConfgs.length();i++)
					{
						JSONObject conf= jarrayConfgs.getJSONObject(i);
						
						if(conf.get("clave").toString().equals("contado"))
						{
							decuento_contado= Integer.parseInt(conf.get("valor").toString());
						}
				
					}
					
					String jsonClientes=jsonObject.getString("user_clients");
					JSONArray jarray=new JSONArray(jsonClientes);
					usuario = new Usuario("", "", username, "", "", api_key,""/*device_id*/);
					((SeekBar)findViewById(R.id.LoginSeekBar)).setProgress(10);
					int porcentaje_progreso=0;
					if(jarray.length()>0)
					{
						porcentaje_progreso = 60 + (40/jarray.length());
						for(int i=0;i<jarray.length();i++)
						{
							JSONObject dic_cliente= jarray.getJSONObject(i);
							
							Cliente cli= new Cliente(dic_cliente.getString("nombre"),dic_cliente.getString("direccion"),
									dic_cliente.getString("rut"),"",dic_cliente.getString("dia_entrega"),dic_cliente.getString("hora_entrega_desde"),dic_cliente.getString("minuto_entrega_desde"),
									dic_cliente.getString("hora_entrega_hasta"),dic_cliente.getString("minuto_entrega_hasta"),dic_cliente.getString("tel"),dic_cliente.getString("tel2"),
									dic_cliente.getString("celular"),dic_cliente.getString("email"),dic_cliente.getString("web"),
									dic_cliente.getString("lugar_entrega"),dic_cliente.getInt("tipo"));
							
							usuario.getListaClientes().add(cli);
							((SeekBar)findViewById(R.id.LoginSeekBar)).setProgress(porcentaje_progreso);
						}
						
					}else ((SeekBar)findViewById(R.id.LoginSeekBar)).setProgress(100);
					
					if(usuario.getNombreUsuario()!=null)
					{
						findViewById(R.id.LoginConectado).setVisibility(View.VISIBLE);
						Intent loc = new Intent(getApplicationContext(),ListadoClientes.class); 
				        loc.putExtra("usuario",usuario); 
				        loc.putExtra("descuento_contado",decuento_contado);
				        startActivity(loc);
					}
					
				} catch (JSONException e) {
					Toast.makeText(Login.this,results, Toast.LENGTH_LONG).show();
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e.getCause());
				}	
			}
			findViewById(R.id.LoginSeekBar).setVisibility(View.INVISIBLE);
			Button b = (Button)findViewById(R.id.btn_ingresar);
			b.setClickable(true);
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
	private void storeRegistrationId(Context context, String regId) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    int appVersion = getAppVersion(context);
	  
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(PROPERTY_REG_ID, regId);
	    editor.putInt(PROPERTY_APP_VERSION, appVersion);
	    editor.commit();
	}
	

	
	private void registerInBackground(){
		
		 new AsyncTask() {

			@Override
			protected String doInBackground(Object... params) {
				
	            try {
	                if (gcm == null) {
	                    gcm = GoogleCloudMessaging.getInstance(context);
	                }
	                registrationID = gcm.register(SENDER_ID);
	                
	                //sendRegistrationIdToBackend();
	                // Persist the regID - no need to register again.
	                storeRegistrationId(context, registrationID);
	            } catch (IOException ex) {
	            	registrationID = "Error :" + ex.getMessage();
	                // If there is an error, don't just keep trying to register.
	                // Require the user to click a button again, or perform
	                // exponential back-off.
	            }
	            return registrationID;
			}
			@Override
	        protected void onPostExecute(Object msg) {
	           // mDisplay.append(msg.toString() + "\n");
	        }
			
			}.execute(null, null, null);
	}


	
}
