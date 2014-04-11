package com.example.vendedores;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

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

import com.example.dominio.Cliente;
import com.example.dominio.Producto;
import com.example.dominio.ProductoVenta;
import com.example.dominio.Venta;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;



public class ServicioCargarUltimaVenta extends IntentService {

	public ServicioCargarUltimaVenta() {
		super("");
	
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
	
	@Override
	protected void onHandleIntent(Intent intento) {
		Cliente cliente=intento.getParcelableExtra("cliente");
		((Sistema)getApplicationContext()).setUltima_venta(cargarUltimaVenta(cliente));
		if(((Sistema)getApplicationContext()).getUltima_venta()!=null){
			Intent ultima_venta=new Intent("ultimaVenta");
			LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ultima_venta);
		}
	}
	
	
	
	private Venta cargarUltimaVenta(Cliente cliente)
	{
		Venta venta=null;
		ArrayList<ProductoVenta> lista = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		 
		 //http://ventas.jm-ga.com/api/ventas/ultima/?nombre=luis&rut=123132&key=39b212ca41d9564d917bf7a9748746d52ffe28c9
		 Sistema sys=(Sistema)getApplicationContext();
         HttpGet httpGet = new HttpGet("http://ventas.jm-ga.com/api/estadisticas/ventas/ultima/?nombre="+sys.getUsu().getNombreUsuario()+"&key="+sys.getUsu().getKey()+"&rut="+cliente.getRut());
           
  
             // Execute HTTP Post Request
         String text = null;
         try {
        	 HttpResponse response = httpClient.execute(httpGet, localContext);
        	 HttpEntity entity = response.getEntity();
               
             text = getASCIIContentFromEntity(entity);
               
         } catch (Exception e) {
        	 
         }
         //return text; 
         if (text!=null) {
        	 if(!text.equals("No hay ventas"))
        	 {			
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(text);
					
					Calendar fecha_venta_registrada=Calendar.getInstance();
					fecha_venta_registrada.set(Calendar.YEAR,Integer.parseInt(((JSONObject)jsonObject.get("fecha")).getString("year")));
					fecha_venta_registrada.set(Calendar.MONTH,Integer.parseInt(((JSONObject)jsonObject.get("fecha")).getString("month"))-1);
					fecha_venta_registrada.set(Calendar.DAY_OF_MONTH,Integer.parseInt(((JSONObject)jsonObject.get("fecha")).getString("dayOfMonth")));
					fecha_venta_registrada.set(Calendar.HOUR_OF_DAY,Integer.parseInt(((JSONObject)jsonObject.get("fecha")).getString("hourOfDay")));
					fecha_venta_registrada.set(Calendar.MINUTE,Integer.parseInt(((JSONObject)jsonObject.get("fecha")).getString("minute")));
					fecha_venta_registrada.set(Calendar.SECOND,Integer.parseInt(((JSONObject)jsonObject.get("fecha")).getString("second")));
					
					venta=new Venta(sys.getUsu(),cliente,fecha_venta_registrada,Double.parseDouble(jsonObject.get("precio").toString()),Integer.parseInt(jsonObject.get("tipo").toString()),Double.parseDouble(jsonObject.get("precio_sin_descuento").toString()));
					
					JSONArray jarray =(JSONArray)jsonObject.get("productos");
					lista= new ArrayList<ProductoVenta>();
					for(int i=0;i<jarray.length();i++)
					{
						JSONObject jsonPvp=(JSONObject)((JSONObject)jarray.get(i)).get("producto");
						int cant=((JSONObject)jarray.get(i)).getInt("cantidad");
						int desc=((JSONObject)jarray.get(i)).getInt("descuento");
						boolean sin_costo=((JSONObject)jarray.get(i)).getBoolean("sin_cargo");
						int s_costo=1;
						if(sin_costo)
							s_costo=0;
						Producto prod= new Producto(jsonPvp.getString("nombre"),
								new BigDecimal(jsonPvp.getDouble("precio_cliente_final")),
								new BigDecimal(jsonPvp.getDouble("precio_distribuidor")),
								new BigDecimal(jsonPvp.getDouble("precio_mayorista")),
								jsonPvp.getString("codigo"),jsonPvp.getString("descripcion"));
						ProductoVenta pv=new ProductoVenta(prod,cant,desc,s_costo);
						venta.getProductos().add(pv);
					}
				
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
        	 } 
				
		}
         return venta;
	}

	
	

}
