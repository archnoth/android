package com.example.vendedores;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dominio.Cliente;
import com.example.dominio.Producto;
import com.example.dominio.ProductoVenta;
import com.example.dominio.Venta;
import com.example.vendedores.R.color;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

public class DetalleCliente extends Activity {
	private String[] dias_de_semana = {"Domingo","Lunes","Martes","Miércoles","Jueves","Viernes","Sábado"}; 
	private Venta venta;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_cliente);
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		final Cliente cliente=getIntent().getExtras().getParcelable("cliente");
		
		TextView nombre=(TextView)findViewById(R.id.editTextNombreCliente);
		TextView rut=(TextView)findViewById(R.id.editTextRutCliente);
		final TextView direccion=(TextView)findViewById(R.id.editDireccionCliente);
		TextView tel=(TextView)findViewById(R.id.editTelCliente);
		TextView tel2=(TextView)findViewById(R.id.editTel2Cliente);
		TextView celular=(TextView)findViewById(R.id.editCelularCliente);
		TextView email=(TextView)findViewById(R.id.editEmailCliente);
		TextView web=(TextView)findViewById(R.id.editWebCliente);
		TextView lugar_entrega=(TextView)findViewById(R.id.editLugarEntrega);
		TextView dia_entrega=(TextView)findViewById(R.id.editDia_de_entregaCliente);
		TextView hora_entrega_desde=(TextView)findViewById(R.id.editHora_de_entrega_desde);
		TextView hora_entrega_hasta=(TextView)findViewById(R.id.editHora_de_entrega_hasta);
		
		if(cliente.getNombre()!=null) nombre.setText(cliente.getNombre());
		else nombre.setVisibility(View.GONE);
		if(cliente.getRut()!=null) rut.setText(cliente.getRut());
		else rut.setVisibility(View.GONE);
		
		if(cliente.getDireccion()!=null){ 
			direccion.setText(cliente.getDireccion());
			if (cliente.getLatitud()!=null & cliente.getLatitud()!=null)
			direccion.setTextColor(getResources().getColor(color.azul));
			direccion.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (cliente.getLatitud()!=null & cliente.getLatitud()!=null)
					{
						Uri location = Uri.parse("geo:"+cliente.getLatitud()+","+cliente.getLongitud()+"?z=14"); // z param is zoom level
						Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
						startActivity(mapIntent);
					}
					
				}
			});
			
			
		}
		else direccion.setVisibility(View.GONE);
		if(cliente.getTel()!=null)
		{
			tel.setTextColor(getResources().getColor(R.color.azul));
			tel.setText(cliente.getTel());
			tel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Uri number = Uri.parse("tel:"+cliente.getTel());
					Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
					startActivity(callIntent);
					
				}
			});
		}
		else tel.setVisibility(View.GONE);
		if(cliente.getTel2()!=null)
		{
			tel2.setTextColor(getResources().getColor(R.color.azul));
			tel2.setText(cliente.getTel2());
			
			tel2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Uri number = Uri.parse("tel:"+cliente.getTel2());
					Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
					startActivity(callIntent);
					
				}
			});
		}
		else tel2.setVisibility(View.GONE);
		if(cliente.getCelular()!=null)
		{
			celular.setTextColor(getResources().getColor(R.color.azul));
			celular.setText(cliente.getCelular());
			celular.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Uri number = Uri.parse("tel:"+cliente.getCelular());
					Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
					startActivity(callIntent);
					
				}
			});
		}
		else celular.setVisibility(View.GONE);
		if(cliente.getEmail()!=null)
		{
	
			email.setTextColor(getResources().getColor(R.color.azul));
			email.setText(cliente.getEmail());
			email.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent emailIntent = new Intent(Intent.ACTION_SEND);
					emailIntent.setType(HTTP.PLAIN_TEXT_TYPE);
					emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {cliente.getEmail()}); // recipients
					emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto del mail");
					emailIntent.putExtra(Intent.EXTRA_TEXT, "Mensaje del mail");
					startActivity(emailIntent);
					
				}
			});
			
		}
		else email.setVisibility(View.GONE);
		if(cliente.getWeb()!=null)
		{
			web.setTextColor(getResources().getColor(R.color.azul));
			web.setText(cliente.getWeb());
			web.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Uri webpage = Uri.parse(cliente.getWeb());
					Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
					startActivity(webIntent);
					
				}
			});
		}
		else web.setVisibility(View.GONE);
		if(cliente.getLugar_entrega()!=null)
		{
			if (cliente.getLatitud_entrega()!=null & cliente.getLatitud_entrega()!=null)
				lugar_entrega.setTextColor(getResources().getColor(color.azul));
			
			lugar_entrega.setText(cliente.getLugar_entrega());
			lugar_entrega.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (cliente.getLatitud_entrega()!=null & cliente.getLongitud_entrega()!=null)
					{
						Uri location = Uri.parse("geo:"+cliente.getLatitud_entrega()+","+cliente.getLongitud_entrega()+"?z=14"); // z param is zoom level
						Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
						startActivity(mapIntent);
					}
					
				}
			});
		}
		else lugar_entrega.setVisibility(View.GONE);
		if(cliente.getDia_de_entrega()!=null)dia_entrega.setText(dias_de_semana[cliente.getDia_de_entrega()]);
		else dia_entrega.setVisibility(View.GONE);
		if(cliente.getHora_de_entrega_desde()!=null)hora_entrega_desde.setText(cliente.getHora_de_entrega_desde().get(Calendar.HOUR_OF_DAY) + ":" + cliente.getHora_de_entrega_desde().get(Calendar.MINUTE) + "hs.");
		else hora_entrega_desde.setVisibility(View.GONE);
		if(cliente.getHora_de_entrega_hasta()!=null)hora_entrega_hasta.setText(cliente.getHora_de_entrega_hasta().get(Calendar.HOUR_OF_DAY) + ":" + cliente.getHora_de_entrega_hasta().get(Calendar.MINUTE) + "hs.");
	
		//limpio los historicos por las dudas de que venga desde otro cliente
		((Sistema)getApplicationContext()).getHitorialCompras().clear();
		
		//escucho los al servicio historico
		CargarHistoricoReceiver historicoReceiver=new CargarHistoricoReceiver((Button)findViewById(R.id.btn_historico));
		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(historicoReceiver,new IntentFilter("historico"));
		
		//limpio los historicos por las dudas de que venga desde otro cliente
		((Sistema)getApplicationContext()).setUltima_venta(null);		
		//escucho los al servicio ultimaVenta
		CargarUltimaVentaReceiver ultVentaReceiver=new CargarUltimaVentaReceiver((Button)findViewById(R.id.btn_repetir_venta));
		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(ultVentaReceiver,new IntentFilter("ultimaVenta"));
				
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.default_menu, menu);
		
		menu.findItem(R.id.notificacion).setVisible(((Sistema)getApplicationContext()).getNotification());
		notificationReceiver nr=new notificationReceiver(menu.findItem(R.id.notificacion));
		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(nr,new IntentFilter("notificacion"));
		
		return true;
		
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
		
			case R.id.notificacion:
				Intent notificaciones= new Intent(getApplicationContext(),Notificaciones.class);
		    	startActivity(notificaciones);
		    	return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	
	@Override
	protected void onResume(){
		super.onResume();
		((ProgressBar)findViewById(R.id.progressText)).setVisibility(View.INVISIBLE);
		((Button)findViewById(R.id.btn_repetir_venta)).setActivated(false);
		((Button)findViewById(R.id.btn_nueva_venta)).setActivated(false);
		((Button)findViewById(R.id.btn_historico)).setActivated(false);
		
		
		Intent servicio_ultima_venta = new Intent(this, ServicioCargarUltimaVenta.class);
		servicio_ultima_venta.putExtra("cliente",getIntent().getExtras().getParcelable("cliente"));
		startService(servicio_ultima_venta);
		
		Intent servicio_historico = new Intent(this, ServicioCargarHistorico.class);
		servicio_historico.putExtra("cliente",getIntent().getExtras().getParcelable("cliente"));
		startService(servicio_historico);
		
		
		
	}
	
	public void to_factura_activity(View view){
		
		((Button)findViewById(R.id.btn_nueva_venta)).setActivated(true);
		((ProgressBar)findViewById(R.id.progressText)).setVisibility(View.VISIBLE);
		Intent fac_intent = new Intent(getApplicationContext(),EleccionFactura.class); 
		fac_intent.putExtra("cliente",getIntent().getExtras().getParcelable("cliente"));
	    startActivity(fac_intent);
	}
	
	public void to_factura_activity_con_venta(View view){
			
		((Button)findViewById(R.id.btn_repetir_venta)).setActivated(true);
		((ProgressBar)findViewById(R.id.progressText)).setVisibility(View.VISIBLE);
    	Intent fac_intent = new Intent(getApplicationContext(),Factura.class); 
		fac_intent.putExtra("cliente",getIntent().getExtras().getParcelable("cliente"));
		fac_intent.putExtra("tipo",((Sistema)getApplicationContext()).getUltima_venta().getTipo());
		
	    startActivity(fac_intent);
      
	}
	
	
	public void to_historico_activity(View view){
		
		((Button)findViewById(R.id.btn_historico)).setActivated(true);
		((ProgressBar)findViewById(R.id.progressText)).setVisibility(View.VISIBLE);
    	Intent hist_intent = new Intent(getApplicationContext(),Historico.class);   
    	hist_intent.putExtra("cliente",getIntent().getExtras().getParcelable("cliente"));
    	startActivity(hist_intent);
     
	}
	 
}
