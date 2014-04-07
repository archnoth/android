package com.example.vendedores;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;



public class EleccionFactura extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eleccion_factura_activity);
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	
	//buttons listeners
	final Button btn_contado = (Button) findViewById(R.id.btn_factura_contado);  
	btn_contado.setOnClickListener(new View.OnClickListener() {
	   
		   public void onClick(View v) {
			   ((Button)v).setActivated(true);
			   findViewById(R.id.progressBarDetalleAFactura).setVisibility(View.VISIBLE);
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
			   findViewById(R.id.progressBarDetalleAFactura).setVisibility(View.VISIBLE);
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
			   findViewById(R.id.progressBarDetalleAFactura).setVisibility(View.VISIBLE);
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
			   findViewById(R.id.progressBarDetalleAFactura).setVisibility(View.VISIBLE);
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
			   findViewById(R.id.progressBarDetalleAFactura).setVisibility(View.VISIBLE);
			   Intent fac_intent = new Intent(getApplicationContext(),VisitaActivity.class); 
				fac_intent.putExtra("usuario",getIntent().getExtras().getParcelable("usuario")); 
				fac_intent.putExtra("cliente",getIntent().getExtras().getParcelable("cliente"));
				startActivity(fac_intent);
		   
		   }
		   
		   
	  
	   });
	
	}//onCreate
	
	@Override
	protected void onResume(){
		super.onResume();
		findViewById(R.id.progressBarDetalleAFactura).setVisibility(View.INVISIBLE);
		((Button)findViewById(R.id.btn_factura_credito)).setActivated(false);
		((Button)findViewById(R.id.btn_factura_contado)).setActivated(false);
		((Button)findViewById(R.id.btn_visita)).setActivated(false);
		((Button)findViewById(R.id.btn_nota_contado)).setActivated(false);
		((Button)findViewById(R.id.btn_nota_credito)).setActivated(false);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.factura, menu);
		notificationReceiver nr=new notificationReceiver(menu.findItem(R.id.notificacion));
		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(nr,new IntentFilter("notificacion"));
		return true;
	}
}
	
