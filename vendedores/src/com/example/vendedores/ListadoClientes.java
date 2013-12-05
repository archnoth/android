package com.example.vendedores;

import com.example.dominio.Cliente;
import com.example.dominio.Usuario;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListadoClientes extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listado_clientes);
		ListView listaClientes=(ListView)findViewById(R.id.ViewListaVendedores);
		Usuario usuario=getIntent().getExtras().getParcelable("usuario");
		ListAdapter adaptador_lista = new ArrayAdapter<Cliente>(this.getApplicationContext(), R.layout.lista_text_view, usuario.getListaClientes());
		listaClientes.setAdapter(adaptador_lista);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.listado_clientes, menu);
		return true;
	}

}
