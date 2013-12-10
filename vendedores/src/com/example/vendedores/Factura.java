package com.example.vendedores;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.example.dominio.Producto;
import com.example.dominio.Usuario;
import com.example.dominio.Venta;



import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;


import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ScrollView;

import android.widget.Toast;




@SuppressLint("NewApi")
public class Factura extends Activity {

	private static List<Producto> lista_productos;
	private AutoCompleteTextView auto;
	private ArrayAdapter<Producto> adapter ;
	private Integer cant_productos=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_factura);
		cant_productos=cant_productos+1;
		
    }

  
	@Override
	protected void onResume()
	{
		 super.onResume();
		 LongRunningGetIO thred=new LongRunningGetIO();//llamo un proceso en backgroud para cargar los productos de la empresa
		 AsyncTask<Void, Void, List<Producto>> async=thred.execute();
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
		
		 auto = (AutoCompleteTextView)findViewById(R.id.autocompleteproducto);
		 auto.setAdapter(adapter);
		 auto.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long larg3) {
					// TODO Auto-generated method stub
					 
					 
					LinearLayout myLayout=((LinearLayout)findViewById(R.id.layout_scroll));
					AutoCompleteTextView auto_gen=generarAutocomplete();
					myLayout.addView(auto_gen);
					myLayout.setNextFocusDownId(auto_gen.getId());
					//Factura.this.addContentView(auto_gen,auto_gen.getLayoutParams());
					
						 
					
				}
		    });
			
		 
		
		
	   
	}
	
	private AutoCompleteTextView generarAutocomplete()
	 {
				// Toast.makeText(Factura.this," selected", Toast.LENGTH_LONG).show();
				 AutoCompleteTextView auto_gen = new AutoCompleteTextView(getApplicationContext());
				 auto_gen.setAdapter(adapter);
				 auto_gen.setEms(10);
				 auto_gen.setLayoutParams(new ViewGroup.LayoutParams(
	             ViewGroup.LayoutParams.WRAP_CONTENT,
	             ViewGroup.LayoutParams.WRAP_CONTENT));
				 auto_gen.setOnItemClickListener(auto.getOnItemClickListener());
				 return auto_gen;
	 }
	

	public void Facturar(View view) {
		       
		
		Button b = (Button)findViewById(R.id.Facturar);
		b.setClickable(false);
	    Usuario vendedor=(Usuario)getIntent().getExtras().getParcelable("usuario");
	    Cliente cliente=(Cliente)getIntent().getExtras().getParcelable("cliente");
	    Calendar fecha=Calendar.getInstance();
	    Double monto=0.0;
	    ScrollView sc=(ScrollView)this.findViewById(R.id.scroll);
	    int size_focusables=sc.getFocusables(1).size();
	   
	    for(int i=0;i<size_focusables;)
	    {
	    	
	    	LinearLayout auto=(LinearLayout)sc.getChildAt(i);
	    	String codigo=((AutoCompleteTextView)auto.getChildAt(0)).getText().toString().split("\\s - \\s")[1];
	    	
	    	for(int j=0;j<adapter.getCount();j++){
	    	
	    		if(((Producto)adapter.getItem(j)).getCodigo().equals(codigo))
	    		{
	    			monto=((Producto)adapter.getItem(j)).getPrecio();
	    		}
	    		
	    	}
	    	String strin=((AutoCompleteTextView)auto.getChildAt(0)).getContext().toString();    	
	    	i=i+1;
	    }
	    
	    
		Venta nueva_venta=new Venta(vendedor, cliente, fecha,monto);
		
		
			
	    	
			
			
	    	
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
