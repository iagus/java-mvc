package modelos;

import java.io.Serializable;

public class Comentario implements Serializable {
	
	private int idProducto;
	private String idUsuario;
	private int codigoUsuario;
	private String texto;
	private int valoracion;
	
	public Comentario(int idProducto, String idUsuario, String texto, int valoracion)
	{
		this.idProducto = idProducto;
		this.idUsuario = idUsuario;
		this.texto = texto;
		this.valoracion = valoracion;
	}
	
	public Comentario(int idProducto, int codigoUsuario, String texto, int valoracion)
	{
		this.idProducto = idProducto;
		this.texto = texto;
		this.valoracion = valoracion;
		this.codigoUsuario = codigoUsuario;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public int getValoracion() {
		return valoracion;
	}

	public void setValoracion(int valoracion) {
		this.valoracion = valoracion;
	}

	public int getIdProducto() {
		return idProducto;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public int getCodigoUsuario() {
		return codigoUsuario;
	}
	
	

}
