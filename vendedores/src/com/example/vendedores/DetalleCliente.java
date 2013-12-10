package com.example.vendedores;

import com.example.dominio.Cliente;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

import android.widget.TextView;

public class DetalleCliente extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_cliente);
		Cliente cliente=getIntent().getExtras().getParcelable("cliente");
		TextView nombre=(TextView)findViewById(R.id.editText1);
		TextView rut=(TextView)findViewById(R.id.editText2);
		TextView direccion=(TextView)findViewById(R.id.editText3);
		nombre.setText(cliente.getNombre());
		rut.setText(cliente.getRut());
		direccion.setText(cliente.getDireccion());
		
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detalle_cliente, menu);
		return true;
	}
	
	
	
	public void to_factura_activity(View view){
		
		Intent fac_intent = new Intent(getApplicationContext(),Factura.class); 
		fac_intent.putExtra("usuario",getIntent().getExtras().getParcelable("usuario")); 
		fac_intent.putExtra("cliente",getIntent().getExtras().getParcelable("cliente"));  
	    startActivity(fac_intent);
	}
	
public void to_historico_activity(View view){
		
		Intent hist_intent = new Intent(getApplicationContext(),Historico.class); 
		hist_intent.putExtra("usuario",getIntent().getExtras().getParcelable("usuario"));  
	    startActivity(hist_intent);
	}

}
