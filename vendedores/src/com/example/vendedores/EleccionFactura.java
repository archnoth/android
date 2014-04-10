package com.example.vendedores;
import java.util.ArrayList;

import com.example.dominio.Producto;

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
import android.widget.Button;
import android.widget.TextView;



public class EleccionFactura extends Activity {
	
	private ArrayList<Producto> lista_productos=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eleccion_factura_activity);
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
	    //quedo escuchando la carga de productos
	    CargaDatosReceiver productosReceiver=new CargaDatosReceiver((TextView)findViewById(R.id.progressText));
		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(productosReceiver,new IntentFilter("cargaProductos"));
		
	    
	    lista_productos=((Sistema)getApplicationContext()).getLista_productos();
	    
	    //checkearProductos();
	    
	//buttons listeners
	final Button btn_contado = (Button) findViewById(R.id.btn_factura_contado);  
	btn_contado.setOnClickListener(new View.OnClickListener() {
	   
		   public void onClick(View v) {
			   ((Button)v).setActivated(true);
			   
			   Intent fac_intent = new Intent(getApplicationContext(),Factura.class); 
			   fac_intent.putExtra("usuario",getIntent().getExtras().getParcelable("usuario")); 
			   fac_intent.putExtra("cliente",getIntent().getExtras().getParcelable("cliente"));
			   fac_intent.putExtra("tipo",0);
			   fac_intent.putExtra("descuento_contado",getIntent().getExtras().getInt("descuento_contado"));
			   startActivity(fac_intent);
		   }
	   });
	final Button btn_credito = (Button) findViewById(R.id.btn_factura_credito);  
	btn_credito.setOnClickListener(new View.OnClickListener() {
	   
		   public void onClick(View v) {
			   ((Button)v).setActivated(true);
			   
			   Intent fac_intent = new Intent(getApplicationContext(),Factura.class); 
			   fac_intent.putExtra("usuario",getIntent().getExtras().getParcelable("usuario")); 
			   fac_intent.putExtra("cliente",getIntent().getExtras().getParcelable("cliente"));
			   fac_intent.putExtra("tipo",1);
			   fac_intent.putExtra("descuento_contado",getIntent().getExtras().getInt("descuento_contado"));
			   startActivity(fac_intent);
		   }
	   });
	final Button btn_dev_credito = (Button) findViewById(R.id.btn_nota_credito);  
	btn_dev_credito.setOnClickListener(new View.OnClickListener() {
	   
		   public void onClick(View v) {
			   ((Button)v).setActivated(true);
			   
			   Intent fac_intent = new Intent(getApplicationContext(),Factura.class); 
			   fac_intent.putExtra("usuario",getIntent().getExtras().getParcelable("usuario")); 
			   fac_intent.putExtra("cliente",getIntent().getExtras().getParcelable("cliente"));
			   fac_intent.putExtra("tipo",3);
			   fac_intent.putExtra("descuento_contado",getIntent().getExtras().getInt("descuento_contado"));
			   startActivity(fac_intent);
		   
		   }
	  
	   });
	final Button btn_dev_contado = (Button) findViewById(R.id.btn_nota_contado);  
	btn_dev_contado.setOnClickListener(new View.OnClickListener() {
	   
		   public void onClick(View v) {
			   ((Button)v).setActivated(true);
			   
			    Intent fac_intent = new Intent(getApplicationContext(),Factura.class); 
				fac_intent.putExtra("usuario",getIntent().getExtras().getParcelable("usuario")); 
				fac_intent.putExtra("cliente",getIntent().getExtras().getParcelable("cliente"));
				fac_intent.putExtra("tipo",2);
				fac_intent.putExtra("descuento_contado",getIntent().getExtras().getInt("descuento_contado"));
				startActivity(fac_intent);
		   
		   }
	  
	   });
	
	final Button visita = (Button) findViewById(R.id.btn_visita);  
	visita.setOnClickListener(new View.OnClickListener() {
	   
		   public void onClick(View v) {
			   ((Button)v).setActivated(true);
			   
			   Intent fac_intent = new Intent(getApplicationContext(),VisitaActivity.class); 
				fac_intent.putExtra("usuario",getIntent().getExtras().getParcelable("usuario")); 
				fac_intent.putExtra("cliente",getIntent().getExtras().getParcelable("cliente"));
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
	
	}//onCreate
	
	@Override
	protected void onResume(){
		super.onResume();
		
		checkearProductos();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.factura, menu);
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
				return true;
		}
	}
	
	private void checkearProductos()
	{
		if(((TextView)findViewById(R.id.progressText)).getText().toString().contains("100"))
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
	
