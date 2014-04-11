package com.example.vendedores;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LoginReceiver extends BroadcastReceiver{
	
	private TextView text;
	private Button boton;
	private ProgressBar progressBar;
	
	public LoginReceiver(EditText text, Button b, ProgressBar progressBar) {
		this.text = text;
		this.boton = b;
		this.progressBar = progressBar;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// Luego de intentar logearse
		if(intent.getBooleanExtra("logueado", false))
		{	
			progressBar.setVisibility(View.INVISIBLE);
			text.setVisibility(View.VISIBLE);
			Intent loginIntent = new Intent(context,ListadoClientes.class);
			loginIntent.putExtra("deviceIsRegistered",intent.getBooleanExtra("deviceIsRegistered",false));
			loginIntent.putExtra("desdeLogin", true);
			loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(loginIntent);
		}
		else Toast.makeText(context, intent.getStringExtra("error"), Toast.LENGTH_LONG).show();
		this.boton.setActivated(false);
		this.boton.setClickable(true);
	}
}
