package com.example.vendedores;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.dominio.Producto;
import com.example.dominio.Usuario;
import com.example.dominio.Venta;

import android.app.Application;

public class Sistema extends Application{

	private Usuario usu;
	private Boolean notification=false;
	private Integer descuento_contado;
	private ArrayList<Producto> lista_productos= new ArrayList<Producto>();
	private Integer total_productos = 0;
	private String registration_id = "";
	private HashMap<Integer, Double> hitorialCompras= new HashMap<Integer, Double>();
	private Venta ultima_venta=null;
	private int device_width;
	private int device_height;
	private Double saldo_cliente;
	
	public void clear() {
		usu = null;
		notification = false;
		descuento_contado = 0;
		lista_productos= new ArrayList<Producto>();
		total_productos = 0;
		registration_id = "";
		hitorialCompras= new HashMap<Integer, Double>();
		ultima_venta=null;
	}
	
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

	public Integer getTotal_productos() {
		return total_productos;
	}

	public void setTotal_productos(Integer total_productos) {
		this.total_productos = total_productos;
	}

	public String getRegistration_id() {
		return registration_id;
	}

	public void setRegistration_id(String registration_id) {
		this.registration_id = registration_id;
	}
	
	public HashMap<Integer, Double> getHitorialCompras() {
		return hitorialCompras;
	}

	public void setHitorialCompras(HashMap<Integer, Double> hitorialCompras) {
		this.hitorialCompras = hitorialCompras;
	}

	public Venta getUltima_venta() {
		return ultima_venta;
	}

	public void setUltima_venta(Venta ultima_venta) {
		this.ultima_venta = ultima_venta;
	}

	public int getDevice_width() {
		return device_width;
	}

	public void setDevice_width(int device_width) {
		this.device_width = device_width;
	}

	public int getDevice_height() {
		return device_height;
	}

	public void setDevice_height(int device_height) {
		this.device_height = device_height;
	}

	public Double getSaldo_cliente() {
		return saldo_cliente;
	}

	public void setSaldo_cliente(Double saldo_cliente) {
		this.saldo_cliente = saldo_cliente;
	}
	
}
