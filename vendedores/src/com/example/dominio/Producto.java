package com.example.dominio;

public class Producto {
	
	private String nombre;
	private Double precio;
	private String codigo;
	private String descripcion;
	
	
	public Producto(String nombre, Double precio, String codigo,String descripcion)
	{
		this.nombre=nombre;
		this.precio=precio;
		this.codigo=codigo;
		this.descripcion=descripcion;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public String toString(){
		return this.nombre + "  -  " + this.codigo; 
	}

}
