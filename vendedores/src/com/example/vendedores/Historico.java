package com.example.vendedores;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.example.dominio.Cliente;
import com.example.dominio.Usuario;
import com.example.dominio.Venta;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;

public class Historico extends Activity {

	private static List<Venta> lista_ventas;
	ArrayAdapter<Venta> adapter ;
	private Usuario usuario;
	private Cliente cliente;
	private HashMap<Integer, Double> producto_porcentaje;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ventas_historico);
		// Show the Up button in the action bar.
		setupActionBar();
		usuario=(Usuario)getIntent().getExtras().getParcelable("usuario"); 
		cliente=(Cliente)getIntent().getExtras().getParcelable("cliente");
		producto_porcentaje=(HashMap<Integer, Double>)getIntent().getExtras().getSerializable("dict");
		
		while(producto_porcentaje.keySet().iterator().hasNext())
		{
			
			Integer codigo = producto_porcentaje.keySet().iterator().next();
			
			LinearLayout linear=new LinearLayout(getApplicationContext());
			LinearLayout .LayoutParams layoutParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 
			LinearLayout.LayoutParams.WRAP_CONTENT);
			linear.setLayoutParams(layoutParams);
			
		                    
			TextView text_barra_porcentaje=new TextView(getApplicationContext());
			text_barra_porcentaje.setText(codigo.toString());
			text_barra_porcentaje.setSingleLine(true);
			text_barra_porcentaje.setBackgroundColor(Color.BLUE);
			
			
			text_barra_porcentaje.setWidth((int)(producto_porcentaje.get(codigo)*100/260));
			text_barra_porcentaje.setHeight(30);
			
			
			TextView text_porcentaje=new TextView(getApplicationContext());
			text_porcentaje.setText(""+producto_porcentaje.get(codigo));
			text_porcentaje.setBackgroundColor(Color.WHITE);
			text_porcentaje.setWidth(LayoutParams.MATCH_PARENT);
			text_porcentaje.setHeight(30);
			
			
		}
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ventas_historico, menu);
		return true;
	}
/*
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpTo(getParent(), NavUtils.getParentActivityIntent(getParent()));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
*/	
	@Override
	protected void onResume()
	{
		 super.onResume();
		 LongRunningGetIO thred=new LongRunningGetIO();//llamo un proceso en backgroud para cargar los productos de la empresa
		 AsyncTask<Void, Void, List<Venta>> async=thred.execute();
		 ArrayList<Venta> list;
		try {
			list = (ArrayList<Venta>)async.get();
			 adapter= new ArrayAdapter<Venta>(this, android.R.layout.simple_list_item_1, list);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private class LongRunningGetIO extends AsyncTask <Void, Void, List<Venta> > {
		
		 
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
		protected  List<Venta> doInBackground(Void... params) {
			 
			HttpClient httpClient = new DefaultHttpClient();
			 HttpContext localContext = new BasicHttpContext();
             HttpGet httpGet = new HttpGet("http://ventas.jm-ga.com/api/historico/"+((Usuario)getIntent().getExtras().getParcelable("usuario")).getKey()+"/");
           
  
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
 					lista_ventas= new ArrayList<Venta>();
 					
 				} catch (JSONException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 					
 					
 			}
			return null;
		}
	}
}
