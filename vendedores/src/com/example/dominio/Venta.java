package com.example.dominio;

import java.util.ArrayList;
import java.util.Date;

public class Venta {
	
	private Cliente cliente;
	private ArrayList<Producto> productos;
	private Date fecha;
	private Usuario usuario;
	
	public Venta(Cliente cliente, ArrayList<Producto> productos, Date fecha)
	{
		this.cliente=cliente;
		this.productos=productos;
		this.fecha=fecha;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public ArrayList<Producto> getProductos() {
		return productos;
	}
	public void setProductos(ArrayList<Producto> productos) {
		this.productos = productos;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
