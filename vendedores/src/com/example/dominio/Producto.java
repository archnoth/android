package com.example.dominio;

import java.util.Calendar;

import android.os.Parcel;
import android.os.Parcelable;

public class Producto implements Parcelable{
	
	private String nombre;
	private Double precio_cliente_final;
	private Double precio_distribuidor;
	private Double precio_mayorista;
	private String codigo;
	private String descripcion;
	
	
	public Producto(String nombre, Double precio_cliente_final,Double precio_distribuidor,Double precio_mayorista, String codigo,String descripcion)
	{
		this.nombre=nombre;
		this.setPrecio_cliente_final(precio_cliente_final);
		this.setPrecio_distribuidor(precio_distribuidor);
		this.setPrecio_mayorista(precio_mayorista);
		
		this.codigo=codigo;
		this.descripcion=descripcion;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	private Producto(Parcel in) {
		
		
		nombre=in.readString();
		precio_cliente_final=in.readDouble();
		precio_distribuidor=in.readDouble();
		precio_mayorista=in.readDouble();
		codigo=in.readString();
		descripcion=in.readString();
		
	}

	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(nombre);
		dest.writeDouble(precio_cliente_final);
		dest.writeDouble(precio_distribuidor);
		dest.writeDouble(precio_mayorista);
		dest.writeString(codigo);
		dest.writeString(descripcion);

	}
	
	
	public Double getPrecio_cliente_final() {
		return precio_cliente_final;
	}

	public void setPrecio_cliente_final(Double precio_cliente_final) {
		this.precio_cliente_final = precio_cliente_final;
	}


	public Double getPrecio_distribuidor() {
		return precio_distribuidor;
	}

	public void setPrecio_distribuidor(Double precio_distribuidor) {
		this.precio_distribuidor = precio_distribuidor;
	}


	public Double getPrecio_mayorista() {
		return precio_mayorista;
	}

	public void setPrecio_mayorista(Double precio_mayorista) {
		this.precio_mayorista = precio_mayorista;
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
