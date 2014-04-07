package com.example.dominio;

import java.util.Calendar;

public class Mensaje {
	
	 
	private Cliente cliente;
	private Producto producto;
	private Integer cantidad;
	private Calendar fecha;
	private Boolean	recibido;
	
	public Mensaje(Cliente cli,Producto pro,Integer cant,Calendar fecha,Boolean recibido)
	{
		this.setCantidad(cant);
		this.setProducto(pro);
		this.setFecha(fecha);
		this.setCliente(cli);
		this.setRecibido(recibido);
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Calendar getFecha() {
		return fecha;
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}

	public Boolean getRecibido() {
		return recibido;
	}

	public void setRecibido(Boolean recibido) {
		this.recibido = recibido;
	}

}
