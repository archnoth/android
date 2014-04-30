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
 private Double monto_sin_descuentos;
 

 
 public Venta(Usuario usuario, Cliente cliente, Calendar fecha,Double monto,int tipo,Double monto_sin_descuentos)
 {
	 this.usuario=usuario;
	 this.cliente=cliente;
	 this.fecha=fecha;
	 this.setMonto(monto);
	 this.productos=new ArrayList<ProductoVenta>();
	 this.setLista_notas(new ArrayList<Nota>());
	 this.tipo=tipo;
	 this.setMonto_sin_descuentos(monto_sin_descuentos);
	 
	 
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
	setMonto_sin_descuentos(in.readDouble());
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
	dest.writeDouble(monto_sin_descuentos);
}

public Double getMonto_sin_descuentos() {
	return monto_sin_descuentos;
}

public void setMonto_sin_descuentos(Double monto_sin_descuentos) {
	this.monto_sin_descuentos = monto_sin_descuentos;
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



 public Venta(){};
@Override
public boolean equals(Object other){
	
	    Venta o=(Venta)other;
		if (o == null) return false;
		if (o== this) return true;
		if (!(o instanceof Venta))return false;
		
		return this.getFecha().get(Calendar.YEAR) == o.getFecha().get(Calendar.YEAR)
		&& this.getFecha().get(Calendar.MONTH) == o.getFecha().get(Calendar.MONTH)
		&& this.getFecha().get(Calendar.DAY_OF_MONTH) == o.getFecha().get(Calendar.DAY_OF_MONTH)
		&& this.getFecha().get(Calendar.HOUR_OF_DAY) == o.getFecha().get(Calendar.HOUR_OF_DAY)
		&& this.getFecha().get(Calendar.MINUTE) == o.getFecha().get(Calendar.MINUTE)  
		&& this.getFecha().get(Calendar.SECOND) == o.getFecha().get(Calendar.SECOND)
		&& this.getCliente().equals(o.getCliente()); 
	
	}

}

