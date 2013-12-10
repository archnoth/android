package com.example.dominio;

import java.util.ArrayList;
import java.util.Calendar;


public class Venta {
 
 private Cliente cliente;
 private Usuario usuario;
 private ArrayList<ProductoVenta> productos;
 private Calendar fecha;
 private Double monto;
 
 
 public Venta(Usuario usuario, Cliente cliente, Calendar fecha,Double monto)
 {
	 this.usuario=usuario;
	 this.cliente=cliente;
	 this.fecha=fecha;
	 this.setMonto(monto);
	 this.productos=new ArrayList<ProductoVenta>();
	 
	 
 } 
 
 public Cliente getCliente() {
  return cliente;
 }
 public void setCliente(Cliente cliente) {
  this.cliente = cliente;
 }
 public ArrayList<ProductoVenta> getProductos() {
  return productos;
 }
 public void setProductos(ArrayList<ProductoVenta> productos) {
  this.productos = productos;
 }
 public Calendar getFecha() {
  return fecha;
 }
 public void setFecha(Calendar fecha) {
  this.fecha = fecha;
 }
public Usuario getUsuario() {
	return usuario;
}
public void setUsuario(Usuario usuario) {
	this.usuario = usuario;
}

public Double getMonto() {
	return monto;
}

public void setMonto(Double monto) {
	this.monto = monto;
}
}

