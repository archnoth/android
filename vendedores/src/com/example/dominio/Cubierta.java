package com.example.dominio;

import java.math.BigDecimal;

import android.R.integer;
import android.os.Parcel;
import android.os.Parcelable;

public class Cubierta extends Producto implements Parcelable {

	private String tipo; 
	private String vehiculo;
	private String marca;
	private String dimension;
	private String gama;
	private String velocidad;
	private String camara;
	private String del_tras;
    private BigDecimal ancho;
	private BigDecimal serie;
	private int llanta;
	private int carga;
	
	public Cubierta(String nombre, BigDecimal precio_cliente_final,
			BigDecimal precio_distribuidor, BigDecimal precio_mayorista,
			String codigo, String descripcion) {
		super(nombre, precio_cliente_final, precio_distribuidor, precio_mayorista,
				codigo, descripcion);
		// TODO Auto-generated constructor stub
	}

	public String getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(String vehiculo) {
		this.vehiculo = vehiculo;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public String getGama() {
		return gama;
	}

	public void setGama(String gama) {
		this.gama = gama;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(String velocidad) {
		this.velocidad = velocidad;
	}

	public String getCamara() {
		return camara;
	}

	public void setCamara(String camara) {
		this.camara = camara;
	}

	public String getDel_tras() {
		return del_tras;
	}

	public void setDel_tras(String del_tras) {
		this.del_tras = del_tras;
	}

	public BigDecimal getAncho() {
		return ancho;
	}

	public void setAncho(BigDecimal ancho) {
		this.ancho = ancho;
	}

	public BigDecimal getSerie() {
		return serie;
	}

	public void setSerie(BigDecimal serie) {
		this.serie = serie;
	}

	public int getLlanta() {
		return llanta;
	}

	public void setLlanta(int llanta) {
		this.llanta = llanta;
	}

	public int getCarga() {
		return carga;
	}

	public void setCarga(int carga) {
		this.carga = carga;
	}

	/********************************/
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	private Cubierta(Parcel in) {
		
		super(in);
		this.ancho = new BigDecimal(in.readString());
		this.camara = in.readString();
		this.carga = in.readInt();
		this.del_tras = in.readString();
		this.dimension = in.readString();
		this.gama = in.readString();
		this.llanta = in.readInt();
		this.marca = in.readString();
		this.serie = new BigDecimal(in.readString());
		this.tipo = in.readString();
		this.vehiculo = in.readString();
		this.velocidad = in.readString();
		
	}

	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(this.ancho.toString());
		dest.writeString(this.camara);
		dest.writeInt(this.carga);
		dest.writeString(this.del_tras);
		dest.writeString(this.dimension);
		dest.writeString(this.gama);
		dest.writeInt(this.llanta);
		dest.writeString(this.marca);
		dest.writeString(this.serie.toString());
		dest.writeString(this.tipo);
		dest.writeString(this.vehiculo);
		dest.writeString(this.velocidad);
		

	}
	
	
	public static final Parcelable.Creator<Cubierta> CREATOR
    = new Parcelable.Creator<Cubierta>() {
		 
	 public Cubierta createFromParcel(Parcel in) {
	     return new Cubierta(in);
	 }
	
	 public Cubierta[] newArray(int size) {
	     return new Cubierta[size];
	 }
	};

	

}
