package com.example.vendedores;

import java.io.IOException;
import java.io.InputStream;
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
import com.example.dominio.Mensaje;
import com.example.dominio.Producto;
import com.example.dominio.Usuario;
import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView.FindListener;


public class ServicioMensajes extends IntentService  {

	public ServicioMensajes(){
		super("");
	};
	public ServicioMensajes(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		// TODO Auto-generated method stub
		String data=arg0.getDataString();
		Usuario u=((Sistema)getApplicationContext()).getUsu();
		try
		{
			Log.i("Prueba de clientes", u.getNombre());
			new LongRunningGetIO().execute();
		}catch(Exception e){};
		
		
	}
	class LongRunningGetIO extends AsyncTask <Void, Void, String> 
	{

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
		protected String doInBackground(Void... arg0) {
			 HttpClient httpClient = new DefaultHttpClient();
			 HttpContext localContext = new BasicHttpContext();
             HttpGet httpGet = new HttpGet("http://ventas.jm-ga.com/api/mensajesVendedor?key="+((Sistema)getApplicationContext()).getUsu().getKey());
             
             // Execute HTTP Post Request
             String text = null;
             try {
            	 
            	 HttpResponse response = httpClient.execute(httpGet, localContext);
            	 HttpEntity entity = response.getEntity();
                 text = getASCIIContentFromEntity(entity);
                   
             } catch (Exception e) {
            	 return e.getLocalizedMessage();
             }
             return text; 
		}
		@Override
		protected void onPostExecute(String results) {
			
			try {
				JSONObject jsonObject = new JSONObject(results);
				String jsonClientes=jsonObject.getString("mensajes");
				JSONArray jarray = new JSONArray(jsonClientes);
				Usuario usu=((Sistema)getApplicationContext()).getUsu();
				for(int i=0;i<jarray.length();i++)
				{
					JSONObject dic_mensaje= jarray.getJSONObject(i);
					JSONObject dict_cliente=dic_mensaje.getJSONObject("cliente");
					JSONObject dict_producto=dic_mensaje.getJSONObject("producto");
					Cliente c=new Cliente();
					Producto p= new Producto();
					p.setCodigo(dict_producto.getString("codigo"));
					p.setNombre(dict_producto.getString("nombre"));
					c.setRut(dict_cliente.getString("rut"));
					
					ArrayList<Cliente> list=usu.getListaClientes();
					for(int j =0; j<list.size();j++)
					{
						if( list.get(j).equals(c))
						{
							c=list.get(j);
							break;
						}
						
					}
					try{
						Mensaje m = new Mensaje(c,p,dic_mensaje.getInt("cantidad"),Calendar.getInstance(),dic_mensaje.getBoolean("recibido")); 
						m.setId_mensajeVendedor(dic_mensaje.getInt("id"));
						m.setMensaje(dic_mensaje.getString("mensaje"));
						usu.getListaMensajes().add(m);
					}catch(Exception e){}
				}
				Intent notification=new Intent("notificacion");
				LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(notification);
				
				
			
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		}	
	

}
