package com.example.dominio;

public class Usuario {
	
	private String nombre;
	private String apellido;
	private String nombreUsuario;
	private String password;
	private String email;
	private String key;
	
	public Usuario(String nom,String apell,String nomUsu,String pas,String emai,String k)
	{
		nombre=nom;
		apellido=apell;
		nombreUsuario=nomUsu;
		password=pas;
		email=emai;
		key=k;

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
	

}
