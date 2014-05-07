package com.example.vendedores;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
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

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Notificaciones extends Activity {
	
	private ListView listaMensajes;
	private ArrayList<Integer> para_borrar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AsyncTask<Void, Void, String> async=new LongRunningGetIO().execute();	     
        String respuesta=null;
        try {				
			//obtengo la respuesta asincrona
			respuesta=(String)async.get();
			onPostExecute(respuesta);
			
		}catch(Exception e){}
	
		setContentView(R.layout.activity_listado_mensajes);
		listaMensajes=(ListView)findViewById(R.id.ViewListaMensajes);
		MensajeAdapter adaptador_lista = new MensajeAdapter(this.getApplicationContext(), R.layout.mensajes_adapter,((Sistema)getApplicationContext()).getUsu().getListaMensajes());
		listaMensajes.setAdapter(adaptador_lista);
		para_borrar=new ArrayList<Integer>(); 
		
		final Button b = (Button) findViewById(R.id.btn_marcar_leido);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				for(int i=0; i< listaMensajes.getChildCount(); i++)
				{
					RelativeLayout lista_item=(RelativeLayout)listaMensajes.getChildAt(i);
					CheckBox ch=(CheckBox)lista_item.findViewById(R.id.check);
					if(ch.isChecked())
					{
						para_borrar.add(((Mensaje)listaMensajes.getItemAtPosition(i)).getId_mensajeVendedor());
					}
				}
				new MarcarLeidos().execute();
				finish();
				
				
				
				
			}
		});
		
		
	}
	
	protected void onPostExecute(String results) {
		
		try {
			JSONObject jsonObject = new JSONObject(results);
			JSONObject jsonMeta = jsonObject.getJSONObject("meta");
			String jsonMensajes=jsonObject.getString("mensajes");
			JSONArray jarray = new JSONArray(jsonMensajes);
			Usuario usu=((Sistema)getApplicationContext()).getUsu();
			usu.getListaMensajes().clear();
			for(int i=0;i<jarray.length();i++)
			{
				JSONObject dic_mensaje= jarray.getJSONObject(i);
				JSONObject dict_cliente=dic_mensaje.getJSONObject("cliente");
				JSONObject dict_producto=dic_mensaje.getJSONObject("producto");
				Cliente c=new Cliente();
				Producto p= new Producto();
				p.setCodigo(dict_producto.getString("codigo"));
				p.setNombre(dict_producto.getString("nombre"));
				c.setRut(dict_cliente.getString("persona_id"));
				
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
			notification.putExtra("mensajes_sin_leer", jsonMeta.getInt("total_count")!=0);
			LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(notification);
			
		
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		
	}
	
	
	
	private class MarcarLeidos extends AsyncTask<JSONObject, Void, String> {

		protected String getASCIIContentFromEntity(HttpEntity entity)
				throws IllegalStateException, IOException {
			InputStream in = entity.getContent();
			StringBuffer out = new StringBuffer();
			int n = 1;
			while (n > 0) {
				byte[] b = new byte[4096];
				n = in.read(b);
				if (n > 0)
					out.append(new String(b, 0, n));
			}
			return out.toString();
		}
		
		@Override
		protected String doInBackground(JSONObject... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			Usuario usuario=((Sistema)getApplicationContext()).getUsu();
			HashMap<String, ArrayList<Integer>> dic_parametros=new HashMap<String, ArrayList<Integer>>();
			HttpPost httpPost = new HttpPost("http://ventas.jm-ga.com/api/mensajesVendedor/?key="+usuario.getKey());
			dic_parametros.put("ids",para_borrar);
			// Execute HTTP Post Request
			Gson gson = new Gson();
            String dataString = gson.toJson(dic_parametros).toString();
			String text = null;
			try {
				 StringEntity se = new StringEntity(dataString);
				 se.setContentEncoding("UTF-8");
            	 se.setContentType("application/json");
            	 httpPost.setEntity(se);
				 HttpResponse response = httpClient.execute(httpPost,
						localContext);
				 HttpEntity entity = response.getEntity();
				 JSONObject jsonObject;
				 text = getASCIIContentFromEntity(entity);
				 jsonObject = new JSONObject(text);
				switch (response.getStatusLine().getStatusCode()) {
					case 201:
						try {
							
							boolean sin_leer=jsonObject.getBoolean("mensajes_sin_leer");
							Intent notification=new Intent("notificacion");
							notification.putExtra("hay_mensajes", sin_leer);
							
						} catch (JSONException e) {
							e.printStackTrace();
							
						}
						break;
					case 402:
						String sin_leer=jsonObject.getString("error");
						break;
						
					default:
						break;
				}
				 
				 
				if (text != null & text !="") {

					try {
						jsonObject = new JSONObject(text);
						boolean sin_leer=jsonObject.getBoolean("mensajes_sin_leer");
						Intent receiverNoti =new Intent("notificacion");
						receiverNoti.putExtra("mensajes_sin_leer", sin_leer);
						LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(receiverNoti);
					    //si quiero hacer que no muestre la cartita lo mando aca!!!
						text="Mensajes Borrados";
						
						
					} catch (JSONException e) {
						e.printStackTrace();
						
					}
				}

				

			} catch (Exception e) {}
			
			return text;
		}
		@Override
		protected void onPostExecute(String results) {
			
			Intent respuestaAsincrona=new Intent("respuestaAsincrona");
			respuestaAsincrona.putExtra("respuesta", results);
			LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(respuestaAsincrona);
		}
	}
}


