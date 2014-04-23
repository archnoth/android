package com.example.vendedores;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dominio.Cliente;
import com.example.dominio.Usuario;
import com.google.gson.Gson;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

public class ServicioLogin extends IntentService {

	private final LoginBinder lBinder = new LoginBinder();
	private String username = "";
	private String password = "";
	private boolean logueado = false;
	private boolean deviceIsRegistered = false;
	private String error = "";

	public ServicioLogin() {
		super("");
		// TODO Auto-generated constructor stub
	}
	

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		username = intent.getStringExtra("username");
		password = intent.getStringExtra("password");
		boolean retry = true;
		for(int i = 0; i < 3 && retry; i++) {
			try{
				retry = login();
			} catch (SocketTimeoutException e) 
	        {
	             retry = true;
	        }
	        catch (ConnectTimeoutException e)
	        {
	        	 retry = true;
	        }catch (JSONException e){
	        	retry = false;
	        }catch(Exception e){
	        	retry = false;
	        }
		}
		publishResult();
		
	}
  
	private void publishResult() {
		Intent intent = new Intent("login");
		intent.putExtra("logueado", logueado);
		intent.putExtra("deviceIsRegistered", deviceIsRegistered);
		intent.putExtra("error", error);
		LocalBroadcastManager.getInstance(((Sistema)getApplicationContext())).sendBroadcast(intent);
	}
	
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
  
  private boolean login() throws Exception
  {
		 DefaultHttpClient httpClient = new DefaultHttpClient();
		 HttpContext localContext = new BasicHttpContext();
         HttpPost httpPost = new HttpPost("http://ventas.jm-ga.com/api/login/");
         
         // Add your data
         HashMap<String, String> dic_usu=new HashMap<String, String>();
         dic_usu.put("nombreUsuario", username);
         dic_usu.put("password", password);
         dic_usu.put("registration_id", ((Sistema)getApplicationContext()).getRegistration_id());
         Gson gson = new Gson();
         String dataString = gson.toJson(dic_usu).toString();
         
         // Execute HTTP Post Request
         String results = null;
         try {
        	 StringEntity se = new StringEntity(dataString);
        	 se.setContentEncoding("UTF-8");
        	 se.setContentType("application/json");
        	 httpPost.setEntity(se);
        	 ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		     NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		     if (networkInfo != null && networkInfo.isConnected()) {
		    	 HttpResponse response = httpClient.execute(httpPost, localContext);
		    	 HttpEntity entity = response.getEntity();
	        	 results = getASCIIContentFromEntity(entity);
		     }      	 
         } catch (Exception e) {
        	 error = "NO SE PUDO ESTABLECER UNA CONEXION";
        	 throw e;
         }
		if (results!=null) {			
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(results);				
				String api_key = jsonObject.getString("api_key");
				//String device_id=jsonObject.getString("device_id");
				String username=jsonObject.getString("username");
				
				String jsonConfigs=jsonObject.getString("configuraciones");
				
				JSONArray jarrayConfgs=new JSONArray(jsonConfigs);
				
				boolean notificaciones = jsonObject.getBoolean("notificaciones");
				((Sistema)getApplicationContext()).setNotification(notificaciones);
				
				for(int i=0;i<jarrayConfgs.length();i++)
				{
					JSONObject conf= jarrayConfgs.getJSONObject(i);
					
					if(conf.get("clave").toString().equals("contado"))
					{
						((Sistema)getApplicationContext()).setDescuento_contado( Integer.parseInt(conf.get("valor").toString()));
					}
			
				}
				
				String jsonClientes=jsonObject.getString("user_clients");
				JSONArray jarray=new JSONArray(jsonClientes);
				Usuario usuario = new Usuario("", "", username, "","", api_key, ((Sistema)getApplicationContext()).getRegistration_id());
				if(jarray.length()>0)
				{
					for(int i=0;i<jarray.length();i++)
					{
						JSONObject dic_cliente= jarray.getJSONObject(i);
						
						Cliente cli= new Cliente(dic_cliente.getString("nombre"),dic_cliente.getString("direccion"),
								dic_cliente.getString("rut"),"",dic_cliente.getString("dia_entrega"),dic_cliente.getString("hora_entrega_desde"),dic_cliente.getString("minuto_entrega_desde"),
								dic_cliente.getString("hora_entrega_hasta"),dic_cliente.getString("minuto_entrega_hasta"),dic_cliente.getString("tel"),dic_cliente.getString("tel2"),
								dic_cliente.getString("celular"),dic_cliente.getString("email"),dic_cliente.getString("web"),
								dic_cliente.getString("lugar_entrega"),dic_cliente.getInt("tipo"),dic_cliente.getInt("descuento"),dic_cliente.getString("latitud"),dic_cliente.getString("longitud"),
								dic_cliente.getString("latitud_entrega"),dic_cliente.getString("longitud_entrega"),dic_cliente.getBoolean("tiene_mensajes"));
						
						usuario.getListaClientes().add(cli);
					}
					
				}		
				deviceIsRegistered = jsonObject.getBoolean("device");
				((Sistema)getApplicationContext()).setUsu(usuario);
			} catch (JSONException e) {
				error = results;
				throw e;
			}	
		} else {
			error = "NO SE PUDO ESTABLECER UNA CONEXION";
			return true;
		}
		logueado = true;
		return false;
	}
  
  public class LoginBinder extends Binder {
	  public ServicioLogin getService() {
          // Return this instance of LocalService so clients can call public methods
          return ServicioLogin.this;
      }
  }
	
  @Override
  public IBinder onBind(Intent intent) {
      return lBinder;
  }

public String getError() {
	return error;
}

public boolean isLogueado(){
	return logueado;
}

public boolean deviceIsRegistered() {
	return deviceIsRegistered;
}

}
