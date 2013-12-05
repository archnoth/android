package com.example.vendedores;

import com.example.dominio.Usuario;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ListadoClientes extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listado_clientes);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.listado_clientes, menu);
		return true;
	}

}
