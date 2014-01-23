package com.example.dominio;

import java.util.ArrayList;
import java.util.Calendar;

import android.os.Parcel;
import android.os.Parcelable;


public class Venta implements Parcelable{
 
 private Cliente cliente;
 private Usuario usuario;
 private ArrayList<ProductoVenta> productos;
 private Calendar fecha;
 private Double monto;
 private ArrayList<Nota> lista_notas;
 private int tipo;
 
 
 public Venta(Usuario usuario, Cliente cliente, Calendar fecha,Double monto,int tipo)
 {
	 this.usuario=usuario;
	 this.cliente=cliente;
	 this.fecha=fecha;
	 this.setMonto(monto);
	 this.productos=new ArrayList<ProductoVenta>();
	 this.setLista_notas(new ArrayList<Nota>());
	 this.tipo=tipo;
	 
	 
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

public ArrayList<Nota> getLista_notas() {
	return lista_notas;
}

public void setLista_notas(ArrayList<Nota> lista_notas) {
	this.lista_notas = lista_notas;
}


public int getTipo() {
	return tipo;
}

public void setTipo(int tipo) {
	this.tipo = tipo;
}


private Venta(Parcel in) {
	fecha = Calendar.getInstance();
	fecha.setTimeInMillis(in.readLong());
	monto= in.readDouble();
	cliente = in.readParcelable(Cliente.class.getClassLoader());
	usuario = in.readParcelable(Usuario.class.getClassLoader());
	productos = in.createTypedArrayList(ProductoVenta.CREATOR);
	tipo=in.readInt();
}

@Override
public int describeContents() {
	// TODO Auto-generated method stub
	return 0;
}

@Override
public void writeToParcel(Parcel dest, int flags) {
	
	dest.writeLong(fecha.getTimeInMillis());
	dest.writeDouble(monto);
	dest.writeParcelable(cliente,flags);
	dest.writeParcelable(usuario,flags);
	dest.writeTypedList(productos);
	dest.writeInt(tipo);
	
}

public static final Parcelable.Creator<Venta> CREATOR
= new Parcelable.Creator<Venta>() {
	 
public Venta createFromParcel(Parcel in) {
    return new Venta(in);
}

public Venta[] newArray(int size) {
    return new Venta[size];
}
};




}

