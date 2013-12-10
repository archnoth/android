package com.example.dominio;

public class ProductoVenta {
	
	private Producto producto;
	private Integer cantidad;
	
	
	public ProductoVenta(Producto prod,Integer cant)
	{
		this.producto=prod;
		this.cantidad=cant;
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

}
