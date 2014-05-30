package com.example.vendedores;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;

import com.example.dominio.Cliente;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class ServicioCargarHistorico extends IntentService {

 
	    
		//HashMap<String, Integer> accion_carga_datos;
		
		public ServicioCargarHistorico() {
			super("");
		
		}

		@Override
		protected void onHandleIntent(Intent intento) {
			
			Cliente cliente=intento.getParcelableExtra("cliente");
			cargaHistorico(cliente);
			if(((Sistema)getApplicationContext()).getHitorialCompras().size()>0){
				Intent historico=new Intent("historico");
				LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(historico);
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
		
	 
	  
	  
	  private void cargaHistorico(Cliente cliente)
	  {
		  ((Sistema)getApplicationContext()).getHitorialCompras();
		 
		  HttpClient httpClient = new DefaultHttpClient();
		  HttpContext localContext = new BasicHttpContext();
	      HttpGet httpGet = new HttpGet("http://ventas.jm-ga.com/api/estadisticas/ventas/porcentaje_productos_cliente/?rut="+cliente.getRut());
	      // Execute HTTP Post Request
	      String text = null;
		  try {
		        HttpResponse response = httpClient.execute(httpGet, localContext);
		        HttpEntity entity = response.getEntity();       
		        text = getASCIIContentFromEntity(entity);	
				if (text != null) {
					if(!text.contains("Error")){
						
						JSONArray jarray =new JSONArray(text);
						for(int i=0;i<jarray.length();i++){
							JSONArray aux =(JSONArray)jarray.get(i);
							String codigo=aux.get(0).toString();
							Double porcentaje=Double.parseDouble(aux.get(1).toString());
							((Sistema)getApplicationContext()).getHitorialCompras().put(codigo, porcentaje);
						}
					}
						
				}
		  }catch(Exception e)
		  {
			  e.printStackTrace();
		  }
	  }


}
