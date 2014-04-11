package com.example.vendedores;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;

public class Historico extends Activity {

	
	
	
	private HashMap<Integer, Double> producto_porcentaje;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ventas_historico);
		// Show the Up button in the action bar.
		setupActionBar();
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		producto_porcentaje=((Sistema)getApplicationContext()).getHitorialCompras();
		Iterator<Integer> iterador = producto_porcentaje.keySet().iterator();
		Double mayor=0.0;
		while(iterador.hasNext()){ 
		   Integer codigo = iterador.next();
		   if(producto_porcentaje.get(codigo) > mayor){
			    mayor = producto_porcentaje.get(codigo);
		   }
		 }
		 iterador = producto_porcentaje.keySet().iterator();
		 LinearLayout grafica = (LinearLayout) findViewById(R.id.tabla_historico);
		 ((FrameLayout.LayoutParams)grafica.getLayoutParams()).setMargins(0,0,0,10);
		 Random id_generator = new Random();
		 while(iterador.hasNext())
		 {
		   Integer codigo = iterador.next();
		   RelativeLayout barra=new RelativeLayout(getApplicationContext());
		   RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		   barra.setLayoutParams(layoutParams);
		   
		   
		   TextView text_codigo=new TextView(getApplicationContext());
		   text_codigo.setText(codigo.toString());
		   text_codigo.setBackgroundColor(getResources().getColor(R.color.white));
		   text_codigo.setTextSize(20);
		   text_codigo.setTextColor(Color.BLACK);
		   text_codigo.setId(Math.abs(id_generator.nextInt()));
		   barra.addView(text_codigo);
		   RelativeLayout.LayoutParams text_codigo_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,45);
		   text_codigo_params.topMargin=5;
		   text_codigo_params.bottomMargin=5;
		   text_codigo_params.leftMargin=5;
		   text_codigo.setLayoutParams(text_codigo_params);
		   text_codigo.setWidth(70);
		   
		   TextView text_barra_porcentaje=new TextView(getApplicationContext());
		   text_barra_porcentaje.setTextColor(Color.BLACK);
		   text_barra_porcentaje.setTextSize(20);
		   text_barra_porcentaje.setSingleLine(true);
		   text_barra_porcentaje.setBackgroundColor(getResources().getColor(R.color.blue));
		   text_barra_porcentaje.setWidth((int)(producto_porcentaje.get(codigo)*260/mayor));
		   text_barra_porcentaje.setId(id_generator.nextInt());
		   barra.addView(text_barra_porcentaje);
		   RelativeLayout.LayoutParams text_barra_porcentaje_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,45);
		   text_barra_porcentaje_params.topMargin=5;
		   text_barra_porcentaje_params.bottomMargin=5;
		   text_barra_porcentaje_params.leftMargin=5;
		   text_barra_porcentaje_params.addRule(RelativeLayout.RIGHT_OF, text_codigo.getId());
		   text_barra_porcentaje.setLayoutParams(text_barra_porcentaje_params);
		 
		   TextView text_porcentaje=new TextView(getApplicationContext());
		   text_porcentaje.setText(producto_porcentaje.get(codigo).toString()+"%");
		   text_porcentaje.setTextSize(20);
		   text_porcentaje.setTextColor(Color.BLACK);
		   barra.addView(text_porcentaje);
		   RelativeLayout.LayoutParams text_porcentaje_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,45);
		   text_porcentaje_params.topMargin=5;
		   text_porcentaje_params.bottomMargin=5;
		   text_porcentaje_params.leftMargin=5;
		   text_porcentaje_params.addRule(RelativeLayout.RIGHT_OF, text_barra_porcentaje.getId());
		   text_porcentaje.setLayoutParams(text_porcentaje_params);
		   text_porcentaje.setWidth(70);
		   
		   grafica.addView(barra);
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
}
