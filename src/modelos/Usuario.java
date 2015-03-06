package modelos;

import java.util.ArrayList;

public class Usuario {
	
	private int idUsuario;
	private String nombre;
	private String password;
	private String direccion;
	private String poblacion;
	private int cp;
	private String pais;
	private ArrayList<Comentario> comentarios;
	
	public Usuario()
	{
		comentarios = new ArrayList<Comentario>();
	}
	
	public Usuario(String nombre, String password)
	{
		this.nombre = nombre;
		this.password = password;
		comentarios = new ArrayList<Comentario>();
	}
	
	public Usuario(String nombre, String password, String direccion, String poblacion,
			int cp, String pais)
	{
		this.nombre = nombre;
		this.password = password;
		this.direccion = direccion;
		this.poblacion = poblacion;
		this.cp = cp;
		this.pais = pais;
		comentarios = new ArrayList<Comentario>();
	}
	
	public Usuario(int idUsuario, String nombre, String password, String direccion, String poblacion,
			int cp, String pais)
	{
		this.idUsuario = idUsuario;
		this.nombre = nombre;
		this.password = password;
		this.direccion = direccion;
		this.poblacion = poblacion;
		this.cp = cp;
		this.pais = pais;
		comentarios = new ArrayList<Comentario>();
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getPoblacion() {
		return poblacion;
	}

	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}

	public int getCp() {
		return cp;
	}

	public void setCp(int cp) {
		this.cp = cp;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public int getIdUsuario() {
		return idUsuario;
	}
	
	
	// METODOS
	
	public void addComentario(Comentario c)
	{
		comentarios.add(c);
	}
	
	public int totalComentarios()
	{
		return comentarios.size();
	}
	
	public void eliminarComentario(int idProducto, int codigoUsuario)
	{
		for (int i = 0; i < comentarios.size(); i++)
		{
			if (comentarios.get(i).getCodigoUsuario() == codigoUsuario)
			{
				if (comentarios.get(i).getIdProducto() == idProducto)
				{
					comentarios.remove(i);
				}
			}
		}
	}
	
	public ArrayList<Comentario> getComentarios()
	{
		return comentarios;
	}
}
