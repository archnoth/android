package com.example.dominio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Venta {
 
 private Cliente cliente;
 private Usuario usuario;
 private ArrayList<Producto> productos;
 private Calendar fecha;
 private double monto;
 
 
 public Venta(Usuario usuario, Cliente cliente, Calendar fecha,Double monto)
 {
	 this.usuario=usuario;
	 this.cliente=cliente;
	 this.fecha=fecha;
	 this.monto=monto;
	 
	 
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
}