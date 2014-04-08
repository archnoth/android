package com.example.vendedores;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class RespuestasAsincronasReceiver extends BroadcastReceiver{
		
			
		@Override
		public void onReceive(Context context, Intent intent) {
			
			Toast.makeText(context, intent.getStringExtra("respuesta"),Toast.LENGTH_LONG).show();
			
		}

	

}
