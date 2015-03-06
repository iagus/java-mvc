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
					
					// a�ade el comentario al objeto Producto
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
	 * Recibe un objeto de la clase usuario adem�s del properties con las consultas
	 * Inserta los datos del usuario en la base de datos
	 * @param consultas
	 * @param usuario
	 */
	public void registrarUsuario(Properties consultas, Usuario usuario)
	{
		// recoge los datos del objeto usuario
		// para incluirlos en la sentencia posterior
		String nombre = usuario.getNombre();
		String pais = usuario.getPais();
		String password = usuario.getPassword();
		String poblacion = usuario.getPoblacion();
		String direccion = usuario.getDireccion();
		int cp = usuario.getCp();
		
		try {
			// extrae la consulta del properties, efectua la insercion
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
			// extrae la consulta del properties, la ejecuta
			sentenciaSQL = con.prepareStatement(consultas.getProperty("obtenerIdUsuario"));
			sentenciaSQL.setString(1, nombre);
			sentenciaSQL.setString(2, password);
			result = sentenciaSQL.executeQuery();
			
			// recupera el idUsuario del conjunto de resultados (deberia ser solo 1)
			while (result.next())
			{
				id = result.getInt("idUsuario");
			}
			
			sentenciaSQL.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}
	
	
	/**
	 * Recupera todos los comentarios del usuario cuyo id es pasado como parametro
	 * y los a�ade al modelo de datos del objeto Usuario.
	 * @param consultas
	 * @param idUsuario, usuario (sin datos de los comentarios)
	 * @return lista de comentarios
	 */
	public Usuario recuperarComentariosUsuario(Properties consultas, int idUsuario, Usuario usuario)
	{
		try {
			// recoge la consulta del properties, la ejecuta
			sentenciaSQL = con.prepareStatement(consultas.getProperty("recuperarComentariosUsuario"));
			sentenciaSQL.setInt(1, idUsuario);
			result = sentenciaSQL.executeQuery();
			
			// recupera las variables del resultado y 
			// crea un objeto del tipo Comentario con ellas
			while(result.next())
			{
				int idProducto = result.getInt("idProducto");
				String comentarioUsuario = result.getString("comentario");
				int valoracion = result.getInt("valoracion");
				
				Comentario review = new Comentario(idProducto, usuario.getNombre(), comentarioUsuario, valoracion);
				
				// a�ade el objeto comentario a los comentarios del objeto Usuario
				usuario.addComentario(review);
			}
			
			sentenciaSQL.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// devuelve el objeto con los datos de los comentarios actualizados
		return usuario;
	}
	
	
	/**
	 * Obtiene el producto a traves del idProducto.
	 * 
	 * @param consultas
	 * @param idProducto
	 * @return objeto Producto (sin los comentarios)
	 */
	public Producto obtenerProducto(Properties consultas, int idProducto)
	{
		Producto producto = null;
		try {
			// ejecuta la sentencia pasando el idProducto como parametro
			sentenciaSQL = con.prepareStatement(consultas.getProperty("seleccionarProducto"));
			sentenciaSQL.setInt(1, idProducto);
			result = sentenciaSQL.executeQuery();
			String imgUrl = "", nombre = "", descripcion = "";
			int stock = 0;
			double precio = 0;
			
			while (result.next())
			{
				// recoge los datos del producto para crear el objeto
				imgUrl = result.getString("imgUrl");
				nombre = result.getString("nombre");
				stock = result.getInt("stock");
				precio = result.getDouble("precio");
				descripcion = result.getString("descripcion");
			}
			
			sentenciaSQL.close();
			
			// crea el objeto para devolverlo
			producto = new Producto(idProducto, nombre, descripcion, precio, stock, imgUrl);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return producto;
	}
	
	
	
	/**
	 * A�ade los comentarios del producto a su modelo de datos y lo devuelve.
	 * 
	 * @param consultas
	 * @param Producto p sin sus respectivos comentarios en sus datos
	 * @param idProducto
	 * @return
	 */
	public Producto recuperarComentariosProducto(Properties consultas, Producto p, int idProducto) 
	{
		try {
			sentenciaSQL = con.prepareStatement(consultas.getProperty("recuperarComentariosProducto"));
			sentenciaSQL.setInt(1, idProducto);
			result = sentenciaSQL.executeQuery();
			
			while (result.next())
			{
				// extrae las variables necesarias para crear el objeto Comentario
				int idUsuario = result.getInt("idUsuario");
				String comentario = result.getString("comentario");
				int valoracion = result.getInt("valoracion");
				
				// busca el nombre del autor del comentario actual
				sentenciaSQL = con.prepareStatement(consultas.getProperty("buscarAutor"));
				sentenciaSQL.setInt(1, idUsuario);
				ResultSet resultado = sentenciaSQL.executeQuery();
				String nombre = "";
				
				// recupera el valor del nombre del resultado de la consulta
				while (resultado.next())
				{
					nombre = resultado.getString("nombre");
				}
				
				// crea el objeto comentario
				Comentario c = new Comentario(idUsuario, idProducto, comentario, valoracion);
				
				// lo a�ade al objeto producto
				p.addComentario(c);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// una vez fuera del bucle, devuelve el objeto con los comentarios actualizados
		return p;
	}

}
