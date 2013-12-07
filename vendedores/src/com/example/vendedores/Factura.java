package com.example.vendedores;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;



public class Factura extends Activity {

	private static List<Producto> lista_productos;
	AutoCompleteTextView auto;
	ArrayAdapter<Producto> adapter ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_factura);
		
		
    }

  
	@Override
	protected void onResume()
	{
		 super.onResume();
		 LongRunningGetIO thred=new LongRunningGetIO();//llamo un proceso en backgroud para cargar los productos de la empresa
		 AsyncTask async=thred.execute();
		 ArrayList<Producto> list;
		try {
			list = (ArrayList<Producto>)async.get();
			 adapter= new ArrayAdapter<Producto>(this, android.R.layout.simple_list_item_1, list);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 auto = (AutoCompleteTextView)findViewById(R.id.autoCompleteProducto);
		 auto.setAdapter(adapter);
			
		
	   
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.factura, menu);
		
		return true;
	}
	
	
    
private class LongRunningGetIO extends AsyncTask <Void, Void, List<Producto> > {
		
	 
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
		protected  List<Producto> doInBackground(Void... params) {
			 
			HttpClient httpClient = new DefaultHttpClient();
			 HttpContext localContext = new BasicHttpContext();
             HttpGet httpGet = new HttpGet("http://ventas.jm-ga.com/api/productos/"+((Usuario)getIntent().getExtras().getParcelable("usuario")).getKey()+"/");
           
  
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
 				
 				
 				JSONObject jsonObject;
 				try {
 					jsonObject = new JSONObject(text);
 										
 					JSONArray jarray =(JSONArray)jsonObject.get("objects");
 					lista_productos= new ArrayList<Producto>();
 					for(int i=0;i<jarray.length();i++)
 					{
 						JSONObject dic_producto = jarray.getJSONObject(i);
 						Producto prod= new Producto(dic_producto.getString("nombre"),dic_producto.getDouble("precio"),dic_producto.getString("codigo"),dic_producto.getString("descripcion"));
 						lista_productos.add(prod);
 						return lista_productos; 
 					}
 					 
 					
 				} catch (JSONException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 					
 					
 			}
			return null;
	}


	}
}
