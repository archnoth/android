package com.example.dominio;

import android.os.Parcel;
import android.os.Parcelable;

public class Cliente implements Parcelable{

	private String nombre;
	private String direccion;
	private String rut;
	private String url_imagen;
	
	
	public Cliente(String nombre,String direccion,String rut,String url_imagen)
	{
		this.nombre=nombre;
		this.direccion=direccion;
		this.rut=rut;
		this.setUrl_imagen(url_imagen);
		
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getRut() {
		return rut;
	}
	public void setRut(String rut) {
		this.rut = rut;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(nombre);
		dest.writeString(direccion);
		dest.writeString(rut);
		dest.writeString(url_imagen);
	}
	public static final Parcelable.Creator<Cliente> CREATOR
    = new Parcelable.Creator<Cliente>() {
		 
	 public Cliente createFromParcel(Parcel in) {
	     return new Cliente(in);
	 }
	
	 public Cliente[] newArray(int size) {
	     return new Cliente[size];
	 }
	};

	private Cliente(Parcel in) {
		nombre = in.readString();
		direccion = in.readString();
		rut = in.readString();
		url_imagen=in.readString();
	}
	
	@Override
	public String toString(){
		return this.nombre + "  -  " + this.rut; 
	}
	public String getUrl_imagen() {
		return url_imagen;
	}
	public void setUrl_imagen(String url_imagen) {
		this.url_imagen = url_imagen;
	}
}
				
