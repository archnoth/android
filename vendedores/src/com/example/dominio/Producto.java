package com.example.dominio;

import java.util.Calendar;

import android.os.Parcel;
import android.os.Parcelable;

public class Producto implements Parcelable{
	
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
		return this.nombre + "  -  " + this.codigo+ "  -  $" + this.precio; 
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	private Producto(Parcel in) {
		
		
		nombre=in.readString();
		precio=in.readDouble();
		codigo=in.readString();
		descripcion=in.readString();
		
	}

	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(nombre);
		dest.writeDouble(precio);
		dest.writeString(codigo);
		dest.writeString(descripcion);

	}
	
	
	public static final Parcelable.Creator<Producto> CREATOR
    = new Parcelable.Creator<Producto>() {
		 
	 public Producto createFromParcel(Parcel in) {
	     return new Producto(in);
	 }
	
	 public Producto[] newArray(int size) {
	     return new Producto[size];
	 }
	};

}
