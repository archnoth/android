package com.example.dominio;


import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Usuario implements Parcelable {
	
	private String nombre;
	private String apellido;
	private String nombreUsuario;
	private String password;
	private String email;
	private String key;
	private ArrayList<Cliente> listaClientes;
	public Usuario(String nom,String apell,String nomUsu,String pas,String emai,String k)
	{
		nombre=nom;
		apellido=apell;
		nombreUsuario=nomUsu;
		password=pas;
		email=emai;
		key=k;
		listaClientes=new ArrayList<Cliente>();

	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	public ArrayList<Cliente> getListaClientes() {
		return listaClientes;
	}

	public void setListaClientes(ArrayList<Cliente> listaClientes) {
		this.listaClientes = listaClientes;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(nombre);
		dest.writeString(apellido);
		dest.writeString(nombreUsuario);
		dest.writeString(password);
		dest.writeString(email);
		dest.writeString(key);
		dest.writeTypedList(listaClientes);
	}
	
	public static final Parcelable.Creator<Usuario> CREATOR
     = new Parcelable.Creator<Usuario>() {
		 
	 public Usuario createFromParcel(Parcel in) {
	     return new Usuario(in);
	 }
	
	 public Usuario[] newArray(int size) {
	     return new Usuario[size];
	 }
	};
	
private Usuario(Parcel in) {
	nombre = in.readString();
	apellido = in.readString();
	nombreUsuario = in.readString();
	password = in.readString();
	email = in.readString();
	key = in.readString();
	listaClientes = in.createTypedArrayList(Cliente.CREATOR);
}
	

}
