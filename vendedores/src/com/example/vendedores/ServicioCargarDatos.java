package com.example.vendedores;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dominio.Producto;
import com.example.dominio.Usuario;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

public class ServicioCargarDatos extends IntentService {

	HashMap<String, Integer> accion_carga_datos;
	
	public ServicioCargarDatos() {
		super("");
		accion_carga_datos=new HashMap<String, Integer>();
		
		//hash con accion para cargar datos desde el servidor
		accion_carga_datos.put("productos", 0);
	}

	@Override
	protected void onHandleIntent(Intent intento) {
		
		String data=intento.getStringExtra("accion");
		
		Integer accion=accion_carga_datos.get(data);
		
		switch (accion) {
		
		case 0:
			cargaProductos();
			break;

		default:
			break;
		}
		
		
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
	
	
	
 /* private class CargaProductos extends AsyncTask <Void, Integer, Void > {
		 
		@Override
		protected   Void doInBackground(Void... params) {
			
			 HttpClient httpClient = new DefaultHttpClient();
			 HttpContext localContext = new BasicHttpContext();
	         HttpGet httpGet = new HttpGet("http://ventas.jm-ga.com/api/productos?key="+((Sistema)getApplicationContext()).getUsu().getKey());
	           
	         // Execute HTTP Post Request
	         String text = null;
	         JSONObject meta = null;
	         String next=null;
	         do
	         {
	        	 
		         try {
		        	 HttpResponse response = httpClient.execute(httpGet, localContext);
		        	 HttpEntity entity = response.getEntity();
		               
		             text = getASCIIContentFromEntity(entity);
		               
		         } catch (Exception e) {
		        	 
		         }
					// return text;
					if (text != null) {
						
	
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(text);
							meta=jsonObject.getJSONObject("meta");
							JSONArray jarray = (JSONArray) jsonObject.get("objects");
							
							for (int i = 0; i < jarray.length(); i++) {
								JSONObject dic_producto = jarray.getJSONObject(i);
								Producto prod = new Producto(
										dic_producto.getString("nombre"),
										new BigDecimal(dic_producto.getDouble("precio_cliente_final")),
										new BigDecimal(dic_producto.getDouble("precio_distribuidor")),
										new BigDecimal(dic_producto.getDouble("precio_mayorista")),
										dic_producto.getString("codigo"),
										dic_producto.getString("descripcion"));
								((Sistema)getApplicationContext()).getLista_productos().add(prod);
								
							}
							publishProgress(((Sistema)getApplicationContext()).getLista_productos().size()*100/meta.getInt("total_count"));
	
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					
					try {
						next=meta.getString("next");
						httpGet=new HttpGet("http://ventas.jm-ga.com/"+next);
					} catch (JSONException e) {
						next=null;
						
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	         }while(next!=null);
					
	         return null;
			}
		
		@Override
		protected void onProgressUpdate(Integer... progress) {
			
			Intent cargaProductos=new Intent("cargaProductos");
			cargaProductos.putExtra("progreso", progress[0]);
			LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(cargaProductos);
	     }

		}*/

  
  private void progress(Integer progress){

		Intent cargaProductos=new Intent("cargaProductos");
		cargaProductos.putExtra("progreso", progress);
		LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(cargaProductos);
  }
  
  private void cargaProductos()
  {
	  HttpClient httpClient = new DefaultHttpClient();
		 HttpContext localContext = new BasicHttpContext();
      HttpGet httpGet = new HttpGet("http://ventas.jm-ga.com/api/productos?key="+((Sistema)getApplicationContext()).getUsu().getKey());
        
      // Execute HTTP Post Request
      String text = null;
      JSONObject meta = null;
      String next=null;
      do
      {
     	 
	         try {
	        	 HttpResponse response = httpClient.execute(httpGet, localContext);
	        	 HttpEntity entity = response.getEntity();
	               
	             text = getASCIIContentFromEntity(entity);
	               
	         } catch (Exception e) {
	        	 
	         }
				// return text;
				if (text != null) {
					

					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(text);
						meta=jsonObject.getJSONObject("meta");
						JSONArray jarray = (JSONArray) jsonObject.get("objects");
						
						for (int i = 0; i < jarray.length(); i++) {
							JSONObject dic_producto = jarray.getJSONObject(i);
							Producto prod = new Producto(
									dic_producto.getString("nombre"),
									new BigDecimal(dic_producto.getDouble("precio_cliente_final")),
									new BigDecimal(dic_producto.getDouble("precio_distribuidor")),
									new BigDecimal(dic_producto.getDouble("precio_mayorista")),
									dic_producto.getString("codigo"),
									dic_producto.getString("descripcion"));
							((Sistema)getApplicationContext()).getLista_productos().add(prod);
							
						}
					 progress(((Sistema)getApplicationContext()).getLista_productos().size()*100/meta.getInt("total_count"));

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
				try {
					next=meta.getString("next");
					httpGet=new HttpGet("http://ventas.jm-ga.com/"+next);
				} catch (JSONException e) {
					next=null;
					
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
      }while(next!=null);		

  }
  
  
}
