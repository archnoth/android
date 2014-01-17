package com.example.vendedores;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
		
		 Iterator<Integer> iterador = producto_porcentaje.keySet().iterator();
		 Double mayor=0.0; 
		 while(iterador.hasNext())
		  {
		   
		   Integer codigo = iterador.next();
		   if(producto_porcentaje.get(codigo) > mayor){
			    mayor = producto_porcentaje.get(codigo);
		   }
		   
		   LinearLayout linear=new LinearLayout(getApplicationContext());
		   LinearLayout .LayoutParams layoutParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 
		   LinearLayout.LayoutParams.WRAP_CONTENT);
		   layoutParams.setMargins(0, 0, 0, 20);
		   linear.setLayoutParams(layoutParams);
		   
		                      
		   TextView text_barra_porcentaje=new TextView(getApplicationContext());
		   text_barra_porcentaje.setText(codigo.toString());
		   text_barra_porcentaje.setTextColor(Color.BLACK);
		   text_barra_porcentaje.setTextSize(20);
		   text_barra_porcentaje.setSingleLine(true);
		   text_barra_porcentaje.setBackgroundColor(getResources().getColor(R.color.blue));
		   text_barra_porcentaje.setWidth((int)(producto_porcentaje.get(codigo)*260/mayor));
		   text_barra_porcentaje.setHeight(45);
		   linear.addView(text_barra_porcentaje);
		   
		   TextView text_porcentaje=new TextView(getApplicationContext());
		   text_porcentaje.setText(producto_porcentaje.get(codigo).toString()+"%");
		   text_porcentaje.setBackgroundColor(getResources().getColor(R.color.white));
		   text_porcentaje.setWidth(60);
		   text_porcentaje.setHeight(45);
		   text_barra_porcentaje.setTextSize(20);
		   text_porcentaje.setTextColor(Color.BLACK);
		   linear.addView(text_porcentaje);
		   
		   android.widget.LinearLayout grafica = (LinearLayout) findViewById(R.id.lista_historico_grafica);
		   grafica.addView(linear);
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

}
