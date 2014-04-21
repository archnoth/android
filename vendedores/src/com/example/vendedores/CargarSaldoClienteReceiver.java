package com.example.vendedores;

import java.math.BigDecimal;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

public class CargarSaldoClienteReceiver extends BroadcastReceiver{

	
	public CargarSaldoClienteReceiver()
	{
		
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		
		((Sistema)context).setSaldo_cliente(Double.parseDouble(intent.getStringExtra("saldo_cliente")));
		
	}

}
