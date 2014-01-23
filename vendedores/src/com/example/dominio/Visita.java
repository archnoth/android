package com.example.dominio;

import java.util.Calendar;

import android.os.Parcel;

public class Visita {
	
	private Cliente cliente;
	private Usuario usuario;
	private String motivo;
	private String descripcion;
	
	public Visita(Cliente cliente, Usuario vendedor, String motivo, String descripcion)
	{
		this.cliente=cliente;
		this.usuario=vendedor;
		this.motivo=motivo;
		this.descripcion=descripcion;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	
	
	
	
	private Visita(Parcel in) {
		cliente = in.readParcelable(Cliente.class.getClassLoader());
		usuario = in.readParcelable(Usuario.class.getClassLoader());
		motivo=in.readString();
		descripcion=in.readString();
	}

	
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		
		
		dest.writeParcelable(cliente,flags);
		dest.writeParcelable(usuario,flags);
		dest.writeString(motivo);
		dest.writeString(descripcion);
		
	}

	
	
}
