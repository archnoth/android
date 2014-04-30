package com.example.vendedores;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;



public class EleccionFactura extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eleccion_factura_activity);
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
	    //quedo escuchando la carga de productos
	    CargaDatosReceiver productosReceiver=new CargaDatosReceiver((TextView)findViewById(R.id.progressText));
		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(productosReceiver,new IntentFilter("cargaProductos"));
	    
		checkearProductos();
	    
	//buttons listeners
	final Button btn_contado = (Button) findViewById(R.id.btn_factura_contado);  
	btn_contado.setOnClickListener(new View.OnClickListener() {
	   
		   public void onClick(View v) {
			   ((Button)v).setActivated(true);
			   
			   Intent fac_intent = new Intent(getApplicationContext(),Factura.class); 
			   fac_intent.putExtra("cliente",getIntent().getExtras().getParcelable("cliente"));
			   fac_intent.putExtra("tipo",0);
			   startActivity(fac_intent);
		   }
	   });
	final Button btn_credito = (Button) findViewById(R.id.btn_factura_credito);  
	btn_credito.setOnClickListener(new View.OnClickListener() {
	   
		   public void onClick(View v) {
			   ((Button)v).setActivated(true);
			   
			   Intent fac_intent = new Intent(getApplicationContext(),Factura.class); 
			   fac_intent.putExtra("cliente",getIntent().getExtras().getParcelable("cliente"));
			   fac_intent.putExtra("tipo",1);
			   startActivity(fac_intent);
		   }
	   });
	final Button btn_dev_credito = (Button) findViewById(R.id.btn_nota_credito);  
	btn_dev_credito.setOnClickListener(new View.OnClickListener() {
	   
		   public void onClick(View v) {
			   ((Button)v).setActivated(true);
			   
			   Intent fac_intent = new Intent(getApplicationContext(),Factura.class); 
			   fac_intent.putExtra("cliente",getIntent().getExtras().getParcelable("cliente"));
			   fac_intent.putExtra("tipo",3);
			   startActivity(fac_intent);
		   
		   }
	  
	   });
	final Button btn_dev_contado = (Button) findViewById(R.id.btn_nota_contado);  
	btn_dev_contado.setOnClickListener(new View.OnClickListener() {
	   
		   public void onClick(View v) {
			   ((Button)v).setActivated(true);
			   
			    Intent fac_intent = new Intent(getApplicationContext(),Factura.class);  
				fac_intent.putExtra("cliente",getIntent().getExtras().getParcelable("cliente"));
				fac_intent.putExtra("tipo",2);
				startActivity(fac_intent);
		   
		   }
	  
	   });
	
	final Button visita = (Button) findViewById(R.id.btn_visita);  
	visita.setOnClickListener(new View.OnClickListener() {
	   
		   public void onClick(View v) {
			   ((Button)v).setActivated(true);
			   
			   Intent fac_intent = new Intent(getApplicationContext(),VisitaActivity.class); 
				fac_intent.putExtra("cliente",getIntent().getExtras().getParcelable("cliente"));
				startActivity(fac_intent);
		   
		   }
		   
		   
	  
	   });
	final Button rep_ultima_venta=(Button)findViewById(R.id.btn_repetir_venta);
	rep_ultima_venta.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			((Button)findViewById(R.id.btn_repetir_venta)).setActivated(true);
			
	    	Intent fac_intent = new Intent(getApplicationContext(),Factura.class); 
			fac_intent.putExtra("cliente",getIntent().getExtras().getParcelable("cliente"));
			fac_intent.putExtra("tipo",((Sistema)getApplicationContext()).getUltima_venta().getTipo());
			fac_intent.putExtra("ultima_venta", true);
		    startActivity(fac_intent);
			
		}
	});
	
	final TextView textoCagaProd = (TextView)findViewById(R.id.progressText);  
	textoCagaProd.addTextChangedListener(new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {	
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		@Override
		public void afterTextChanged(Editable s) {
			checkearProductos();	
		}
	});
	
	
	
	//limpio la ultima venta por las dudas de que venga desde otro cliente
	((Sistema)getApplicationContext()).setUltima_venta(null);		
	//escucho los al servicio ultimaVenta
	CargarUltimaVentaReceiver ultVentaReceiver=new CargarUltimaVentaReceiver((Button)findViewById(R.id.btn_repetir_venta));
	LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(ultVentaReceiver,new IntentFilter("ultimaVenta"));
	
	
	
	}//onCreate
	
	@Override
	protected void onResume(){
		super.onResume();
		((Button)findViewById(R.id.btn_factura_credito)).setActivated(false);	
		((Button)findViewById(R.id.btn_factura_contado)).setActivated(false);
		((Button)findViewById(R.id.btn_visita)).setActivated(false);
		((Button)findViewById(R.id.btn_nota_contado)).setActivated(false);
		((Button)findViewById(R.id.btn_nota_credito)).setActivated(false);
		checkearProductos();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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
	
	private void checkearProductos()
	{
		if(((Sistema)getApplicationContext()).getTotal_productos() == ((Sistema)getApplicationContext()).getLista_productos().size())
		{
			findViewById(R.id.progressText).setVisibility(View.INVISIBLE);
			((Button)findViewById(R.id.btn_factura_credito)).setEnabled(true);	
			((Button)findViewById(R.id.btn_factura_contado)).setEnabled(true);
			((Button)findViewById(R.id.btn_visita)).setEnabled(true);
			((Button)findViewById(R.id.btn_nota_contado)).setEnabled(true);
			((Button)findViewById(R.id.btn_nota_credito)).setEnabled(true);
			
		}
	}
}
	
