package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import modelos.Carrito;
import modelos.Comentario;
import modelos.Producto;
import modelos.Usuario;

public class AccesoBD {
	
	Connection con = null;
	PreparedStatement sentenciaSQL;
	ResultSet result;
	
	public AccesoBD(Connection con) 
	{
		this.con = con;
	}
	
	
	
	/**
	 * Obtiene todos los productos de la base de datos con sus respectivos comentarios.
	 * Devuelve un array con todos los productos y sus respectivos comentarios.
	 */
	public ArrayList<Producto> obtenerProductos(Properties consultas)
	{
		ArrayList<Producto> listaProductos = new ArrayList<Producto>();
		try {
			sentenciaSQL = con.prepareStatement(consultas.getProperty("seleccionarProductos"));
			ResultSet resultados = sentenciaSQL.executeQuery();
			while (resultados.next())
			{
				String nombre = resultados.getString("nombre");
				String descripcion = resultados.getString("descripcion");
				String imgUrl = resultados.getString("imgUrl");
				int idProducto = resultados.getInt("idProducto");
				int stock = resultados.getInt("stock");
				double precio = resultados.getDouble("precio");
	
				Producto p = new Producto(idProducto, nombre, descripcion, precio, stock, imgUrl);
				
				// obtiene los comentarios para cada producto
				sentenciaSQL = con.prepareStatement(consultas.getProperty("seleccionarComentarios"));
				sentenciaSQL.setInt(1, idProducto);
				result = sentenciaSQL.executeQuery();		
				while (result.next())
				{
					int idUsuario = result.getInt("idUsuario");
					String comentario = result.getString("comentario");
					int valoracion = result.getInt("valoracion");
					
					// busca el autor de cada comentario para guardarlo en el array comentarios del objeto Producto
					sentenciaSQL = con.prepareStatement(consultas.getProperty("buscarAutor"));
					sentenciaSQL.setInt(1, idUsuario);
					ResultSet nombreRes = sentenciaSQL.executeQuery();
					Comentario comentarioTmp = null;
					while (nombreRes.next())
					{
						comentarioTmp = new Comentario(idProducto, nombreRes.getString("nombre"), comentario, valoracion);
					}
					
					// añade el comentario al objeto Producto
					p.addComentario(comentarioTmp);
				}
				
				sentenciaSQL.close();
				listaProductos.add(p);
				
			} // fin while
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return listaProductos;
	}
	
	
	
	
	/**
	 * Recibe un objeto de la clase usuario además del properties con las consultas
	 * Inserta los datos del usuario en la base de datos
	 * @param consultas
	 * @param usuario
	 */
	public void registrarUsuario(Properties consultas, Usuario usuario)
	{
		// recoge los datos del usuario
		String nombre = usuario.getNombre();
		String pais = usuario.getPais();
		String password = usuario.getPassword();
		String poblacion = usuario.getPoblacion();
		String direccion = usuario.getDireccion();
		int cp = usuario.getCp();
		
		// efectua la insercion en la base de datos
		try {
			sentenciaSQL = con.prepareStatement(consultas.getProperty("insertarUsuario"));
			sentenciaSQL.setString(1, nombre);
			sentenciaSQL.setString(2, password);
			sentenciaSQL.setString(3, direccion);
			sentenciaSQL.setString(4, poblacion);
			sentenciaSQL.setInt(5, cp);
			sentenciaSQL.setString(6, pais);
			sentenciaSQL.executeUpdate();
			
			sentenciaSQL.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Recupera el idUsuario de un usuario que pasamos como parametro.
	 * @param consultas
	 * @param usuario
	 * @return valor de la columna idUsuario en la BD
	 */
	public int obtenerIdUsuario(Properties consultas, Usuario usuario)
	{
		// extrae los valores nombre y password
		String nombre = usuario.getNombre();
		String password = usuario.getPassword();
		int id = 0;
		
		try {
			sentenciaSQL = con.prepareStatement(consultas.getProperty("obtenerIdUsuario"));
			sentenciaSQL.setString(1, nombre);
			sentenciaSQL.setString(2, password);
			result = sentenciaSQL.executeQuery();
			
			while (result.next())
			{
				id = result.getInt("idUsuario");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}
	
	
	/**
	 * Recupera todos los comentarios del usuario cuyo id es pasado como parametro.
	 * Utiliza el nombre pasado como parametro para incluirlo en los objetos Comentario.
	 * @param consultas
	 * @param idUsuario, nombre
	 * @return lista de comentarios
	 */
	public Usuario recuperarComentarios(Properties consultas, int idUsuario, Usuario usuario)
	{
		try {
			sentenciaSQL = con.prepareStatement(consultas.getProperty("recuperarComentariosUsuario"));
			sentenciaSQL.setInt(1, idUsuario);
			result = sentenciaSQL.executeQuery();
			while(result.next())
			{
				int idProducto = result.getInt("idProducto");
				String comentarioUsuario = result.getString("comentario");
				int valoracion = result.getInt("valoracion");
				
				Comentario review = new Comentario(idProducto, usuario.getNombre(), comentarioUsuario, valoracion);
				usuario.addComentario(review);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return usuario;
	}

}
