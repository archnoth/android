package com.example.vendedores;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;

public class CargarHistoricoReceiver extends BroadcastReceiver{

	
	private Button btnHistorico;
	
	public CargarHistoricoReceiver(Button t) {
		btnHistorico=t;
	}
	
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		
		btnHistorico.setEnabled(true);
		
	}

}
