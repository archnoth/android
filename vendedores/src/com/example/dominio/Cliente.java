package com.example.dominio;

import android.os.Parcel;
import android.os.Parcelable;

public class Cliente implements Parcelable{

	private String nombre;
	private String direccion;
	private String rut;
	
	
	public Cliente(String nombre,String direccion,String rut)
	{
		this.nombre=nombre;
		this.direccion=direccion;
		this.rut=rut;
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
		// TODO Auto-generated method stub
		
	}
}
				
