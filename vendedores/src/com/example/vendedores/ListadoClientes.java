package com.example.vendedores;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.example.dominio.Cliente;
import com.example.dominio.Usuario;
import com.example.vendedores.notificationReceiver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


public class ListadoClientes extends Activity {
	
	private Usuario usuario;
	private boolean ver_todos=true;
	private ListView listaClientes;
	private ClienteAdapter adaptador_lista;
	ArrayAdapter<Cliente> clientes_para_buscador;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listado_clientes);
		
		Intent servicio_datos = new Intent(this, ServicioCargaProductos.class);
    	servicio_datos.putExtra("accion", "productos");
		startService(servicio_datos);
		
		usuario=((Sistema)getApplicationContext()).getUsu();
		listaClientes=(ListView)findViewById(R.id.ViewListaVendedores);
		adaptador_lista = new ClienteAdapter(this.getApplicationContext(), R.layout.cliente_adapter , usuario.getListaClientes());
		set_lista_clientesAdapter(adaptador_lista);
		AutoCompleteTextView buscador = (AutoCompleteTextView)findViewById(R.id.buscador_vendedores);
		buscador.setAdapter(adaptador_lista);
		buscador.setDropDownHeight(0);
		buscador.setTextColor(Color.LTGRAY);
		
		final Button b = (Button) findViewById(R.id.btn_clientes_sin_visitar);
		   b.setOnClickListener(new View.OnClickListener() {
		   public void onClick(View v) {
		    	if(ver_todos)
		    	{
		    		((Button)v).setActivated(true);
		    		try {
		    			
		    			LongRunningGetIO thred=new LongRunningGetIO();
		    			AsyncTask <Void, Void, List<Cliente> >  async=thred.execute();
		    			adaptador_lista = new ClienteAdapter(getApplicationContext(), R.layout.cliente_adapter , (ArrayList<Cliente>)async.get());
		    			set_lista_clientesAdapter(adaptador_lista);
		    			ver_todos=false;
		    			b.setText("Ver todos los clientes");
		    			AutoCompleteTextView buscador = (AutoCompleteTextView)findViewById(R.id.buscador_vendedores);
		    			buscador.setAdapter(adaptador_lista);
		    			buscador.setDropDownHeight(0);
		    			buscador.setTextColor(Color.LTGRAY);
		    			
		    		}
		    		catch(Exception e){};
		    		
		    	}
		    	else
		    	{
		    		((Button)v).setActivated(false);
		    		adaptador_lista = new ClienteAdapter(getApplicationContext(), R.layout.cliente_adapter , usuario.getListaClientes());
		    		set_lista_clientesAdapter(adaptador_lista);
		    		ver_todos=true;
		    		b.setText("Ver clientes sin visitar");		
		    		AutoCompleteTextView buscador = (AutoCompleteTextView)findViewById(R.id.buscador_vendedores);
		    		buscador.setAdapter(adaptador_lista);
		    		buscador.setDropDownHeight(0);
		    		buscador.setTextColor(Color.LTGRAY);
		    	}
		    }});

	}
	
   
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		getMenuInflater().inflate(R.menu.listado_clientes, menu);
		menu.findItem(R.id.notificacion).setVisible(((Sistema)getApplicationContext()).getNotification());
		notificationReceiver nr=new notificationReceiver(menu.findItem(R.id.notificacion));
		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(nr,new IntentFilter("notificacion"));
		
		RespuestasAsincronasReceiver ra=new RespuestasAsincronasReceiver();
		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(ra,new IntentFilter("respuestaAsincrona"));
		
    	
		return true;
		
	}
	public boolean onOptionsItemSelected(MenuItem item)
		{
			switch (item.getItemId()) {
			
				case R.id.notificacion:
					Intent notificaciones= new Intent(getApplicationContext(),Notificaciones.class);
			    	startActivity(notificaciones);
			    	return true;
					
				default:
					return true;
			}
		}
	
	@Override
	protected void onResume(){
		super.onResume();
		((ProgressBar)findViewById(R.id.ListadoClientesprogressBar)).setVisibility(View.INVISIBLE);
		if(!getIntent().getBooleanExtra("deviceIsRegistered", false) && getIntent().getBooleanExtra("desdeLogin", false)) Toast.makeText(getApplicationContext(), "ESTE DISPOSITIVO NO RECIBIRA MENSAJES. INTENTE VOLVER A INGRESAR", Toast.LENGTH_LONG).show();
		getIntent().putExtra("desdeLogin", false);
	}
	
	
	
	
	
private class LongRunningGetIO extends AsyncTask <Void, Void, List<Cliente> > {

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
		protected List<Cliente> doInBackground(Void... params) {
			ArrayList<Cliente> lista = null;
			HttpClient httpClient = new DefaultHttpClient();
			 HttpContext localContext = new BasicHttpContext();
	         HttpGet httpGet = new HttpGet("http://ventas.jm-ga.com/api/clientes/sin_visitar/?key="+usuario.getKey()+"&mes="+(Calendar.getInstance().get(Calendar.MONTH)+1));
	           
	  
	             // Execute HTTP GET Request
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
					lista= new ArrayList<Cliente>();
					for(int i=0;i<jarray.length();i++)
					{
						JSONObject dic_cliente= jarray.getJSONObject(i);
						
						String direccion="";
						String rut="";
						String dia_entrega="";
						String hora_entrega_desde="";
						String hora_entrega_hasta="" ;
						String minuto_entrega_hasta="";
						String minuto_entrega_desde="";
						String tel="";
						String tel2="";
						String celular="";
						String email="";
						String web="";
						String lugar_entrega="";
						Integer tipo=-1;
						Integer descuento=0;
						String latitud="";
						String longitud="";
						String latitud_entrega="";
						String longitud_entrega="";
						Boolean tiene_mensajes=false;
						
						if(dic_cliente.has("nombre")&& dic_cliente.getString("nombre")!= null)direccion =dic_cliente.getString("nombre");
						
						if(dic_cliente.has("direccion")&& dic_cliente.getString("direccion")!= null)direccion =dic_cliente.getString("direccion");
						
						if(dic_cliente.has("rut") && dic_cliente.getString("rut")!=null)rut=dic_cliente.getString("rut");
						
						if(dic_cliente.has("dia_entrega") && dic_cliente.getString("dia_entrega")!=null)dia_entrega=dic_cliente.getString("dia_entrega");
						
						if(dic_cliente.has("hora_entrega_desde") && dic_cliente.getString("hora_entrega_desde")!=null)hora_entrega_desde=dic_cliente.getString("hora_entrega_desde");
						
						if(dic_cliente.has("hora_entrega_hasta")&& dic_cliente.getString("hora_entrega_hasta")!=null)hora_entrega_hasta=dic_cliente.getString("hora_entrega_hasta");
						
						if(dic_cliente.has("minuto_entrega_hasta")&& dic_cliente.getString("minuto_entrega_hasta")!=null)minuto_entrega_hasta=dic_cliente.getString("minuto_entrega_hasta");
						
						if(dic_cliente.has("minuto_entrega_desde")&&  dic_cliente.getString("minuto_entrega_desde")!=null)minuto_entrega_desde=dic_cliente.getString("minuto_entrega_desde");
						
						if(dic_cliente.has("tel")&&dic_cliente.getString("tel")!=null)tel=dic_cliente.getString("tel");
						
						if(dic_cliente.has("tel2")&&dic_cliente.getString("tel2")!=null)tel2=dic_cliente.getString("tel2");
						
						if(dic_cliente.has("celular")&&dic_cliente.getString("celular")!=null)celular=dic_cliente.getString("celular");
						
						if(dic_cliente.has("email")&&dic_cliente.getString("email")!=null)email=dic_cliente.getString("email");
						
						if(dic_cliente.has("web")&&dic_cliente.getString("web")!=null)web=dic_cliente.getString("web");
						
						if(dic_cliente.has("lugar_entrega")&&dic_cliente.getString("lugar_entrega")!=null)lugar_entrega=dic_cliente.getString("lugar_entrega");
						
						if(dic_cliente.has("tipo")&& dic_cliente.getInt("tipo")!= -1)tipo=dic_cliente.getInt("tipo");
						
						if(dic_cliente.has("descuento")&& dic_cliente.getInt("descuento")!= -1)descuento=dic_cliente.getInt("descuento");
						
						if(dic_cliente.has("latitud")&&dic_cliente.getString("latitud")!=null)latitud=dic_cliente.getString("latitud");
						
						if(dic_cliente.has("longitud")&&dic_cliente.getString("longitud")!=null)longitud=dic_cliente.getString("longitud");
						
						if(dic_cliente.has("latitud_entrega")&&dic_cliente.getString("latitud_entrega")!=null)latitud_entrega=dic_cliente.getString("latitud_entrega");
						
						if(dic_cliente.has("longitud_enterga")&&dic_cliente.getString("longitud_entrega")!=null)longitud_entrega=dic_cliente.getString("longitud_entrega");
						
						if(dic_cliente.has("tiene_mensajes")&&dic_cliente.getString("tiene_mensajes")!=null)tiene_mensajes=dic_cliente.getBoolean("tiene_mensajes");
						
						Cliente cli= new Cliente(dic_cliente.getString("nombre"),direccion,rut,"",dia_entrega.toString(),
								hora_entrega_desde.toString(),minuto_entrega_desde.toString(),
								hora_entrega_hasta.toString(),minuto_entrega_hasta.toString(),tel,tel2,
								celular,email,web,lugar_entrega,tipo,descuento,latitud,longitud,latitud_entrega,longitud_entrega,tiene_mensajes);
						lista.add(cli);
						
					}
					
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
					
			}
			return lista;

		}
		}
		
		 
	
		
		

	
	
private void set_lista_clientesAdapter(ArrayAdapter<Cliente> adaptador_lista)
	{
		listaClientes.setAdapter(adaptador_lista);
		listaClientes.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					((ProgressBar)findViewById(R.id.ListadoClientesprogressBar)).setVisibility(View.VISIBLE);
					Cliente seleccionado = (Cliente)parent.getItemAtPosition(position);
					Intent loc = new Intent(getApplicationContext(),DetalleCliente.class); 
			        loc.putExtra("cliente",seleccionado);
			        startActivity(loc);
			  }
		});
	}

   


	
}
