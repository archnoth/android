package com.example.vendedores;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.dominio.Producto;
import com.example.dominio.Usuario;

import android.app.Application;

public class Sistema extends Application{

	private Usuario usu;
	private Boolean notification=false;
	private Integer descuento_contado;
	private ArrayList<Producto> lista_productos= new ArrayList<Producto>();
	private HashMap<Integer, Double> hitorialCompras= new HashMap<Integer, Double>();
	
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

	public Integer getDescuento_contado() {
		return descuento_contado;
	}

	public void setDescuento_contado(Integer descuento_contado) {
		this.descuento_contado = descuento_contado;
	}

	public ArrayList<Producto> getLista_productos() {
		return lista_productos;
	}

	public void setLista_productos(ArrayList<Producto> lista_productos) {
		this.lista_productos = lista_productos;
	}

	public HashMap<Integer, Double> getHitorialCompras() {
		return hitorialCompras;
	}

	public void setHitorialCompras(HashMap<Integer, Double> hitorialCompras) {
		this.hitorialCompras = hitorialCompras;
	}
	
}
