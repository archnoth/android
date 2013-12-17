package com.example.dominio;

import java.sql.Date;
import java.util.Calendar;

import android.os.Parcel;
import android.os.Parcelable;

public class Nota implements Parcelable{
	
	private String redaccion;
	private Calendar fecha_de_entrega;
	private Calendar hora_de_entrega_desde;
	private Calendar hora_de_entrega_hasta;
	
	
	
	public Nota(String redaccion, Calendar hora_entrega_ini,Calendar hora_entrega_fin,Calendar fecha_entrega)
	{
		this.redaccion=redaccion;
		this.hora_de_entrega_desde=hora_entrega_ini;
		this.hora_de_entrega_hasta=hora_entrega_fin;
		this.fecha_de_entrega=fecha_entrega;
		
	}
	public String getRedaccion() {
		return redaccion;
	}
	public void setRedaccion(String redaccion) {
		this.redaccion = redaccion;
	}
	public Calendar getFecha_de_entrega() {
		return fecha_de_entrega;
	}
	public void setFecha_de_entrega(Calendar fecha_de_entrega) {
		this.fecha_de_entrega = fecha_de_entrega;
	}
	public Calendar getHora_de_entrega_desde() {
		return hora_de_entrega_desde;
	}
	public void setHora_de_entrega_desde(Calendar hora_de_entrega_desde) {
		this.hora_de_entrega_desde = hora_de_entrega_desde;
	}
	public Calendar getHora_de_entrega_hasta() {
		return hora_de_entrega_hasta;
	}
	public void setHora_de_entrega_hasta(Calendar hora_de_entrega_hasta) {
		this.hora_de_entrega_hasta = hora_de_entrega_hasta;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(fecha_de_entrega.toString());
		dest.writeString(hora_de_entrega_desde.toString());
		dest.writeString(hora_de_entrega_hasta.toString());
		dest.writeString(redaccion);
		
	}
	
	
private Nota(Parcel in) {
		
	fecha_de_entrega = in.readParcelable(Calendar.class.getClassLoader());
	hora_de_entrega_desde=in.readParcelable(Calendar.class.getClassLoader());
	hora_de_entrega_hasta=in.readParcelable(Calendar.class.getClassLoader());
	redaccion=in.readString();
			
	}

	

	
	
	public static final Parcelable.Creator<Nota> CREATOR
    = new Parcelable.Creator<Nota>() {
		 
	 public Nota createFromParcel(Parcel in) {
	     return new Nota(in);
	 }
	
	 public Nota[] newArray(int size) {
	     return new Nota[size];
	 }
	};

	

}
