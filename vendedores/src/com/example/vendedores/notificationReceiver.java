package com.example.vendedores;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

public class notificationReceiver extends BroadcastReceiver{

	private MenuItem mi;
	
	public notificationReceiver(MenuItem m) {
		mi=m;
	}
		
	@Override
	public void onReceive(Context context, Intent intent) {
		try{
			if(intent.getBooleanExtra("mensajes_sin_leer",true)){
				mi.setVisible(true);
				((Sistema)context).setNotification(true);
			}else{
				((Sistema)context).setNotification(false);
				mi.setVisible(false);
				}
		}catch(Exception e){}
	}

}
