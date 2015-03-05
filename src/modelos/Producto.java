package modelos;

import java.io.Serializable;
import java.util.ArrayList;

public class Producto implements Serializable {
	
	private int idProducto;
	private String nombre;
	private String descripcion;
	private double precio;
	private int stock;
	private String imgUrl;
	private ArrayList<Comentario> comentarios;
	
	public Producto(int id, String nombre, String descripcion, double precio, int stock, String imgUrl)
	{
		this.idProducto = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.stock = stock;
		this.imgUrl = imgUrl;
		comentarios = new ArrayList<Comentario>();
	}
	
	
	// getters y setters
	
	public int getIdProducto() {
		return idProducto;
	}

	public String getNombre() {
		return nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	
	// metodos
	public void addComentario(Comentario c)
	{
		comentarios.add(c);
	}
	
	public int totalComentarios()
	{
		return comentarios.size();
	}
	
	public int mediaComentarios()
	{
		int suma = 0, contador = 0;
		
		if (comentarios.size() > 0)
		{
			for (Comentario c:comentarios)
			{
				suma += c.getValoracion();
				contador++;
			}
			
			return suma/contador;
		}
		
		return 1;
	}
	
}
