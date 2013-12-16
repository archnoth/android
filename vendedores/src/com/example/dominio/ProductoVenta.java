package com.example.dominio;

import java.util.Calendar;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductoVenta  implements Parcelable{
	
	private Producto producto;
	private Integer cantidad;
	
	
	public ProductoVenta(Producto prod,Integer cant)
	{
		this.producto=prod;
		this.cantidad=cant;
	}
	
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public String toString()
	{
		return this.producto.getNombre()+"\n-Codigo :"+this.producto.getCodigo()+"\n-Cantidad :"+this.getCantidad();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

		dest.writeParcelable(producto,flags);
		dest.writeInt(cantidad);
		
	}
	

	public static final Parcelable.Creator<ProductoVenta> CREATOR
    = new Parcelable.Creator<ProductoVenta>() {
		 
	 public ProductoVenta createFromParcel(Parcel in) {
	     return new ProductoVenta(in);
	 }
	
	 public ProductoVenta[] newArray(int size) {
	     return new ProductoVenta[size];
	 }
	};
	
	private ProductoVenta(Parcel in) {
		
		producto=in.readParcelable(Producto.class.getClassLoader());
		this.cantidad=in.readInt();
		
		
	}
	
}
