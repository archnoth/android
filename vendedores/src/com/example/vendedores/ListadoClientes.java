package com.example.vendedores;

import com.example.dominio.Cliente;
import com.example.dominio.Usuario;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;


public class ListadoClientes extends Activity {
	
	private Usuario usuario;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listado_clientes);
		usuario=getIntent().getExtras().getParcelable("usuario");
		ListView listaClientes=(ListView)findViewById(R.id.ViewListaVendedores);
		ArrayAdapter<Cliente> adaptador_lista = new ArrayAdapter<Cliente>(this.getApplicationContext(), R.layout.lista_text_view , usuario.getListaClientes());
		listaClientes.setAdapter(adaptador_lista);
		listaClientes.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					((ProgressBar)findViewById(R.id.ListadoClientesprogressBar)).setVisibility(View.VISIBLE);
					Cliente seleccionado = (Cliente)parent.getItemAtPosition(position);
					Intent loc = new Intent(getApplicationContext(),DetalleCliente.class); 
			        loc.putExtra("cliente",seleccionado);
			        loc.putExtra("usuario",usuario);
			        startActivity(loc);
					//Toast.makeText(getApplicationContext(),"RUT : " + seleccionado.getRut() , Toast.LENGTH_LONG).show();
			  }
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.listado_clientes, menu);
		return true;
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		((ProgressBar)findViewById(R.id.ListadoClientesprogressBar)).setVisibility(View.INVISIBLE);
	}
}
