package com.example.vendedores;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dominio.Producto;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class ServicioCargaProductos extends IntentService {
	
	public ServicioCargaProductos() {
		super("");
	}

	@Override
	protected void onHandleIntent(Intent intento) {
		boolean retry = true;
		for(int i = 0; i < 3 && retry; i++) {
			try{
				retry = cargaProductos();
			} catch (SocketTimeoutException e) 
	        {
	             retry = true;
	        }
	        catch (ConnectTimeoutException e)
	        {
	        	 retry = true;
	        }catch(Exception e){
	        	retry = false;
	        }
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
	
  private void progress(Integer progress){

		Intent cargaProductos=new Intent("cargaProductos");
		cargaProductos.putExtra("progreso", progress);
		LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(cargaProductos);
  }
  
  private boolean cargaProductos() throws Exception
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
	               
	         }catch (Exception e) {	 }
	         
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
					 ((Sistema)getApplicationContext()).setTotal_productos(meta.getInt("total_count"));
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
      return false;
  }
  
  
}
