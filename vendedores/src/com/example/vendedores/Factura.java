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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dominio.Cliente;
import com.example.dominio.Producto;
import com.example.dominio.ProductoVenta;
import com.example.dominio.Usuario;
import com.example.dominio.Venta;
import com.google.gson.Gson;



import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import android.widget.Toast;


@SuppressLint("NewApi")
public class Factura extends Activity {

	private static List<Producto> lista_productos;

	private EditText last_text_cantidad;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_factura);
		LongRunningGetIO thred=new LongRunningGetIO();//llamo un proceso en backgroud para cargar los productos de la empresa
		 AsyncTask<Void, Void, List<Producto>> async=thred.execute();
		try {
			lista_productos = (ArrayList<Producto>)async.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 Button btn_add=(Button)findViewById(R.id.btn_add);
			
			btn_add.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				
					addRowToTableProductos();
				}
			});
			
		Button btn_rem=(Button)findViewById(R.id.btn_rem);
		
			btn_rem.setOnClickListener(new OnClickListener() {
			
				@Override
				public void onClick(View v) {
					TableLayout tbl=(TableLayout)findViewById(R.id.tablaProductos);
					if (tbl.getChildCount() > 0) {
						tbl.removeViewAt(tbl.getChildCount()-1);
					}
				}
			});
		((Button)findViewById(R.id.btn_add)).callOnClick();
	

		
		private EditText generarEditText() 
		{
			EditText editText=new EditText(getApplicationContext());
			editText.setTextColor(Color.BLACK);
			editText.setBackgroundColor(Color.WHITE);
			editText.setMinEms(1);

			
			editText.addTextChangedListener(new TextWatcher(){ 
			   
				boolean agregar=true;
				@Override
				public void onTextChanged(CharSequence s, int start, int before,int count) {
					// TODO Auto-generated method stub
					
				}


				@Override
				public void afterTextChanged(Editable s) {
					TableLayout tbl=(TableLayout)findViewById(R.id.tablaProductos);
					if(tbl.getChildCount()> 0)
					{
						if(last_text_cantidad.getParent()==tbl.getChildAt(tbl.getChildCount()-1))
						{
							if(agregar)
							{
								addRowToTableProductos();
								agregar=false;
							}
								
						};
					}
					
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					
					
				}
		});
		

		last_text_cantidad=editText;
		return editText;
		}
		
		
		private void addRowToTableProductos()
		{
			AutoCompleteTextView autocomplete=Factura.this.generarAutocomplete();
			   EditText text_cant =Factura.this.generarEditText();
			     TableRow tbr= new TableRow(getApplicationContext());
			     EditText divider = Factura.this.generarEditText(); 
			     divider.setBackgroundColor(Color.BLACK);
			     divider.setWidth(2);
			     divider.setInputType(android.text.InputType.TYPE_NULL);
			     Button cruz = Factura.this.generarCruz();
			     tbr.addView(text_cant, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			     tbr.addView(divider);
			     tbr.addView(autocomplete);
			     tbr.addView(cruz);
			     tbr.setLayoutParams(new ViewGroup.LayoutParams(
			                 ViewGroup.LayoutParams.WRAP_CONTENT,
			                 ViewGroup.LayoutParams.WRAP_CONTENT));
			     tbr.setBackgroundColor(Color.LTGRAY);
			     tbr.setPadding(2, 1, 2, 1);
			     TableLayout tbl=(TableLayout)findViewById(R.id.tablaProductos);
			     tbl.addView(tbr,tbl.getChildCount());	
		}
		
		
		private AutoCompleteTextView generarAutocomplete()
		 {
			  // Toast.makeText(Factura.this," selected", Toast.LENGTH_LONG).show();
		     AutoCompleteTextView auto_gen = new AutoCompleteTextView(getApplicationContext());
		     auto_gen.setAdapter(new ArrayAdapter<Producto>(this, android.R.layout.simple_list_item_1, lista_productos));
		     auto_gen.setEms(9);
		     auto_gen.setMaxLines(1);
		     auto_gen.setHighlightColor(Color.BLUE);
		     auto_gen.setHorizontallyScrolling(true);
		     auto_gen.setCursorVisible(true);
		     auto_gen.setTextColor(Color.BLACK);
		     auto_gen.setBackgroundColor(Color.WHITE);
		     return auto_gen;

		 }
		

		private Button generarCruz()
		 {
		  Button cruz = new Button(getApplicationContext());
		  cruz.setText("X");
		  cruz.setOnClickListener(new OnClickListener() {
		   
		   @Override
		   public void onClick(View v) {
		    TableLayout tbl=(TableLayout)findViewById(R.id.tablaProductos);
		    tbl.removeView((View)v.getParent());
		   }
		  });
		  return cruz;
		 }

    }

	@Override
	protected void onResume()
	{
		 super.onResume();
		 
		 	 
	}
	
	
	
	
	
		
	public void Facturar(View view) throws JSONException {
		
		Button b = (Button)findViewById(R.id.Facturar);
		b.setClickable(false);
	    Usuario vendedor=(Usuario)getIntent().getExtras().getParcelable("usuario");
	    Cliente cliente=(Cliente)getIntent().getExtras().getParcelable("cliente");
	    Calendar fecha=Calendar.getInstance();
	    Double monto=0.0;
	    TableLayout tbl=(TableLayout)this.findViewById(R.id.tablaProductos);
	    
	    Venta nueva_venta=new Venta(vendedor, cliente, fecha,0.0);
	    
	    for(int i=0;i<tbl.getChildCount();)
	    {  	
	    	TableRow tr=(TableRow)tbl.getChildAt(i);
    		AutoCompleteTextView auto=(AutoCompleteTextView)tr.getChildAt(2);
    		EditText cant=(EditText)tr.getChildAt(0);
    	
    		String codigo="";
    		try{
    			codigo=auto.getText().toString().split("\\s - \\s")[1];
    			
    			
    			}catch (Exception e) {
    				// TODO: handle exception
    			}
    			for(int j=0;j<auto.getAdapter().getCount();j++){
    	
    				if(((Producto)auto.getAdapter().getItem(j)).getCodigo().equals(codigo))
    				{
    					monto=monto+((Producto)auto.getAdapter().getItem(j)).getPrecio()*Integer.parseInt(cant.getText().toString());
    					nueva_venta.getProductos().add(new ProductoVenta((Producto)auto.getAdapter().getItem(j),Integer.parseInt(cant.getText().toString())));
    				}
    			}

	    	i=i+1;
	    }
	     
		nueva_venta.setMonto(monto);
		Gson gson = new Gson();
        String dataString = gson.toJson(nueva_venta, nueva_venta.getClass()).toString();
        PostNuevaVenta thred=new PostNuevaVenta();//llamo un proceso en backgroud para cargar los productos de la empresa
        
        AsyncTask<String, Void, String> async=thred.execute(dataString);
		
     
		try {
			String respuesta= (String)async.get();
			JSONObject json=new JSONObject(respuesta);
			respuesta=json.getString("response").toString()+" Para el cliente "+ json.getString("cliente") + "  Con un monto de: "+json.getString("monto").toString();
			Toast.makeText(Factura.this,respuesta, Toast.LENGTH_LONG).show();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
         
        	    	
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
			ArrayList<Producto> lista = null;
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
 					lista= new ArrayList<Producto>();
 					for(int i=0;i<jarray.length();i++)
 					{
 						JSONObject dic_producto = jarray.getJSONObject(i);
 						Producto prod= new Producto(dic_producto.getString("nombre"),dic_producto.getDouble("precio"),dic_producto.getString("codigo"),dic_producto.getString("descripcion"));
 						lista.add(prod);
 					}
 					
 					
 				} catch (JSONException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 					
 					
 			}
			return lista;
	}


	}	
    
private class PostNuevaVenta extends AsyncTask <String, Void, String > {
		
	 
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
		protected  String doInBackground(String... params) {
			 
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost("http://ventas.jm-ga.com/api/ventas/");
             // Execute HTTP Post Request
             String text = null;
             try {
            	 StringEntity se = new StringEntity(params[0].toString());
            	 se.setContentEncoding("UTF-8");
            	 se.setContentType("application/json");
            	 httpPost.setEntity(se);
            	 HttpResponse response = httpClient.execute(httpPost, localContext);
            	 HttpEntity entity = response.getEntity();
                   
                 text = getASCIIContentFromEntity(entity);
                 return text;
                   
             } catch (Exception e) {}
             //return text; 
             
 			
			return null;
	}
		@Override
		protected void onPostExecute(String results) {
			//((ProgressBar)findViewById(R.id.progressBarFactura)).setVisibility(View.INVISIBLE);
		}

	}
}
