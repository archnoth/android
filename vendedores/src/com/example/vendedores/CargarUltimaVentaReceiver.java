package com.example.vendedores;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;

public  class CargarUltimaVentaReceiver extends BroadcastReceiver{

	private Button btnUltimaVenta;
	
	public CargarUltimaVentaReceiver(Button t) {
		btnUltimaVenta=t;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		btnUltimaVenta.setEnabled(true);
		
	}

}
