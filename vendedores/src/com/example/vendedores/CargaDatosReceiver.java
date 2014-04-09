package com.example.vendedores;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

public class CargaDatosReceiver extends BroadcastReceiver{

	
	private TextView progreso;
	
	public CargaDatosReceiver(TextView t) {
		progreso=t;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		
			progreso.setText("carga de datos: "+Integer.valueOf(intent.getIntExtra("progreso", 0)).toString()+"%");
		
		
	}

}
