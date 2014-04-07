package com.example.vendedores;

import com.example.dominio.Usuario;

import android.app.Application;

public class Sistema extends Application{

	private Usuario usu;
	private Boolean notification=false;

	public Usuario getUsu() {
		return usu;
	}

	public void setUsu(Usuario usu) {
		this.usu = usu;
	}

	public Boolean getNotification() {
		return notification;
	}

	public void setNotification(Boolean notification) {
		this.notification = notification;
	}
	
}
