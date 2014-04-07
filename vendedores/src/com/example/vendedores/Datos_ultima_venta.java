package com.example.vendedores;

import java.text.SimpleDateFormat;

import com.example.dominio.Cliente;
import com.example.dominio.Venta;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class Datos_ultima_venta extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.datos_ultima_venta);
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		Venta v=(Venta)getIntent().getExtras().getParcelable("venta");
		((EditText)findViewById(R.id.editTextCliente_ultima_venta)).setText(v.getCliente().toString());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String strdate = sdf.format(v.getFecha().getTime());
		((EditText)findViewById(R.id.editTextFecha_venta)).setText(strdate);
		((EditText)findViewById(R.id.EditTextCmontoValue)).setText(v.getMonto().toString());
		((EditText)findViewById(R.id.editMontoSvalue)).setText(v.getMonto_sin_descuentos().toString());
		if(v.getTipo()==0)
		((EditText)findViewById(R.id.editTextTipoUltVentaValue)).setText("Contado");
		else if(v.getTipo()==1)
		((EditText)findViewById(R.id.editTextTipoUltVentaValue)).setText("Credito");
		else if(v.getTipo()==2)
		((EditText)findViewById(R.id.editTextTipoUltVentaValue)).setText("Devolición Contado");
		else if(v.getTipo()==3)
		((EditText)findViewById(R.id.editTextTipoUltVentaValue)).setText("Devolución Credito");
		else
			((EditText)findViewById(R.id.editTextTipoUltVentaValue)).setText("Visita");
			
			
		
	}

}
