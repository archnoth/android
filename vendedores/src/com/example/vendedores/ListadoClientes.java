package com.example.vendedores;

import com.example.dominio.Cliente;
import com.example.dominio.Usuario;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListadoClientes extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listado_clientes);
		Usuario usuario=getIntent().getExtras().getParcelable("usuario");
		ListView listaClientes=(ListView)findViewById(R.id.ViewListaVendedores);
		ArrayAdapter<Cliente> adaptador_lista = new ArrayAdapter<Cliente>(this.getApplicationContext(), R.layout.lista_text_view , usuario.getListaClientes());
		listaClientes.setAdapter(adaptador_lista);
		listaClientes.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					Cliente seleccionado = (Cliente)parent.getItemAtPosition(position);
					Toast.makeText(getApplicationContext(),"RUT : " + seleccionado.getRut() , Toast.LENGTH_LONG).show();
			  }
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.listado_clientes, menu);
		return true;
	}
}
