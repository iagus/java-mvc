package servlets;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import modelos.Carrito;
import modelos.Comentario;
import modelos.Producto;
import modelos.Usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


/**
 * Servlet implementation class Control
 */
@WebServlet("/control")
public class Control extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	Connection con = null;
	PreparedStatement sentencia;
	Statement sentenciaSQL;
	ArrayList<String> operacionesTexto;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Control() {
        super();
        operacionesTexto = new ArrayList<String>();
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://localhost/tienda", "root", "");
			if (con!=null)
			{
				System.out.println("Conexion OK");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Error al cargar e driver");
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error en la conexion");
			
		} catch (InstantiationException e) {
			e.printStackTrace();
			
		} catch (IllegalAccessException e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String ocul = request.getParameter("oculto");
		System.out.println("Oculto: "+ocul);
		int oculto = Integer.parseInt(ocul);
		HttpSession sesion = request.getSession();
		sesion.setAttribute("operaciones", operacionesTexto);
		ArrayList<Producto> listaProductos = new ArrayList<Producto>();
		//variables producto
		String nombre = "", descripcion = "", imgUrl = "";
		int idProducto = 0, stock = 0;
		double precio = 0;
		
		//variables comentario
		int valoracion = 0, idUsuario = 0;
		String comentario = "";
		
		switch (oculto)
		{
		// pagina principal, listado de productos
		case 1: String sql = "select * from productos";
			try {
				sentenciaSQL = con.createStatement();
				ResultSet resultados = sentenciaSQL.executeQuery(sql);
				while (resultados.next())
				{
					nombre = resultados.getString("nombre");
					descripcion = resultados.getString("descripcion");
					imgUrl = resultados.getString("imgUrl");
					idProducto = resultados.getInt("idProducto");
					stock = resultados.getInt("stock");
					precio = resultados.getDouble("precio");
		
					Producto p = new Producto(idProducto, nombre, descripcion, precio, stock, imgUrl);
					Carrito carrito = new Carrito();
					listaProductos.add(p);
					sesion.setAttribute("carrito", carrito);
					sesion.setAttribute("listaProductos", listaProductos);
					
					// obtener comentarios
					sentencia = con.prepareStatement("select * from reviews where idProducto = ?");
					sentencia.setInt(1, idProducto);
					ResultSet cmnts = sentencia.executeQuery();
					while (cmnts.next())
					{
						idUsuario = cmnts.getInt("idUsuario");
						comentario = cmnts.getString("comentario");
						valoracion = cmnts.getInt("valoracion");
						
						sentencia = con.prepareStatement("select nombre from usuarios where idUsuario = ?");
						sentencia.setInt(1, idUsuario);
						ResultSet nombreRes = sentencia.executeQuery();
						Comentario comentarioTmp = null;
						while (nombreRes.next())
						{
							comentarioTmp = new Comentario(idProducto, nombreRes.getString("nombre"), comentario, valoracion);
						}
						
						p.addComentario(comentarioTmp);
					}
				}
				operacionesTexto = (ArrayList<String>) sesion.getAttribute("operaciones");
				operacionesTexto.add("Obtenida la lista de productos de la base de datos");
				sesion.setAttribute("operaciones", operacionesTexto);
				request.getRequestDispatcher("tienda.jsp").forward(request,response);
				break;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		// registro de usuarios (logeo es el 7)
		case 2: nombre = request.getParameter("nombre");
				String password = request.getParameter("password");
				String direccion = request.getParameter("direccion");
				String poblacion = request.getParameter("poblacion");
				int cp = Integer.parseInt(request.getParameter("cp"));
				String pais = request.getParameter("pais");
				
			try {
				sentencia = con.prepareStatement("INSERT INTO usuarios(nombre, password, direccion, poblacion, cp, pais) "
						+ "VALUES(?, ?, ?, ?, ?, ?)");
				sentencia.setString(1, nombre);
				sentencia.setString(2, password);
				sentencia.setString(3, direccion);
				sentencia.setString(4, poblacion);
				sentencia.setInt(5, cp);
				sentencia.setString(6, pais);
				sentencia.executeUpdate();
				
				sentencia = con.prepareStatement("SELECT * from usuarios WHERE nombre = ? AND password = ?");
				sentencia.setString(1, nombre);
				sentencia.setString(2, password);
				ResultSet resultados = sentencia.executeQuery();
				int id = 0;
				while(resultados.next())
				{
					id = resultados.getInt("idUsuario");
				}
				Usuario usuario = new Usuario(id, nombre, password, direccion, poblacion, cp, pais);
				
				// buscamos el id del nuevo usuario que acabamos de insertar
				sentencia = con.prepareStatement("select * from reviews r, usuarios u where r.idUsuario = u.idUsuario "
						+ "and u.nombre = ? and u.password = ?");
				sentencia.setString(1, nombre);
				sentencia.setString(2, password);
				ResultSet reviewsDelUsuario = sentencia.executeQuery();
				while(reviewsDelUsuario.next())
				{
					idProducto = reviewsDelUsuario.getInt("idProducto");
					String comentarioUsuario = reviewsDelUsuario.getString("comentario");
					valoracion = reviewsDelUsuario.getInt("valoracion");
					
					Comentario review = new Comentario(idProducto, nombre, comentarioUsuario, valoracion);
					usuario.addComentario(review);
				}
				
				sesion.setAttribute("usuario", usuario);
				operacionesTexto = (ArrayList<String>) sesion.getAttribute("operaciones");
				operacionesTexto.add("Registro de un usuario");
				sesion.setAttribute("operaciones", operacionesTexto);
				request.getRequestDispatcher("perfil.jsp").forward(request,response);
				
				break;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		// carrito
		case 3: break;
		
		// vista individual del producto con sus respectivos comentarios
		case 4: idProducto = Integer.parseInt(request.getParameter("idP"));
				sql = "select * from productos where idProducto = ?";
				ArrayList<Comentario> listaComentarios = new ArrayList<Comentario>();
				try {
					sentencia = con.prepareStatement(sql);
					sentencia.setInt(1, idProducto);
					ResultSet resultados = sentencia.executeQuery();
					
					//obtener los productos
					while (resultados.next())
					{
						nombre = resultados.getString("nombre");
						descripcion = resultados.getString("descripcion");
						imgUrl = resultados.getString("imgUrl");
						idProducto = resultados.getInt("idProducto");
						stock = resultados.getInt("stock");
						precio = resultados.getDouble("precio");
			
						Producto p = new Producto(idProducto, nombre, descripcion, precio, stock, imgUrl);
						sesion.setAttribute("producto", p);
						
						
						//obtener los comentarios
						sentencia = con.prepareStatement("select * from reviews where idProducto = ?");
						sentencia.setInt(1, idProducto);
						ResultSet comentarios = sentencia.executeQuery();
						while (comentarios.next())
						{
							
							comentario = comentarios.getString("comentario");
							valoracion = comentarios.getInt("valoracion");
							idUsuario = comentarios.getInt("idUsuario");
							
							sentencia = con.prepareStatement("select nombre from usuarios where idUsuario = ?");
							sentencia.setInt(1, idUsuario);
							ResultSet usuarioRes = sentencia.executeQuery();
							String nombreUser = "";
							while (usuarioRes.next())
							{
								nombreUser = usuarioRes.getString("nombre");
							}
							
							Comentario objetoComentario = new Comentario(idProducto, nombreUser, comentario, valoracion);
							listaComentarios.add(objetoComentario);
						}
						sesion.setAttribute("listaComentarios", listaComentarios);
					}
					
					operacionesTexto = (ArrayList<String>) sesion.getAttribute("operaciones");
					operacionesTexto.add("Obteniendo vista individual de un producto, listando sus comentarios");
					sesion.setAttribute("operaciones", operacionesTexto);
					request.getRequestDispatcher("producto.jsp").forward(request,response);
					break;
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//insertar nuevo comentario
				case 5:
					Usuario usuario = (Usuario) sesion.getAttribute("usuario");
					String texto = request.getParameter("comentario");
					valoracion = Integer.parseInt(request.getParameter("valoracion"));
					idProducto = Integer.parseInt(request.getParameter("producto"));
					String nombreUsuario = usuario.getNombre();
					idUsuario = Integer.parseInt(request.getParameter("usuario"));
					Comentario comentarioParaInsertar = new Comentario(idProducto, idUsuario, texto, valoracion);
					usuario.addComentario(comentarioParaInsertar);
					
					// guardamos el comentario en la BD
					try {
						sentencia = con.prepareStatement("INSERT INTO reviews(idProducto, idUsuario, comentario, valoracion)"
						+ " VALUES(?, ?, ?, ?)");
						sentencia.setInt(1, idProducto);
						sentencia.setInt(2, idUsuario);
						sentencia.setString(3, texto);
						sentencia.setInt(4, valoracion);
						
						sentencia.executeUpdate();
						sesion.setAttribute("usuario", usuario);
						
						operacionesTexto = (ArrayList<String>) sesion.getAttribute("operaciones");
						operacionesTexto.add("insertando un nuevo comentario en la base de datos");
						sesion.setAttribute("operaciones", operacionesTexto);
						request.getRequestDispatcher("producto.jsp?oculto=4&idP=" + idProducto).forward(request, response);
						break;
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				//eliminar comentario
				case 6:
						idUsuario = Integer.parseInt(request.getParameter("usuario"));
						idProducto = Integer.parseInt(request.getParameter("producto"));
						usuario = (Usuario) sesion.getAttribute("usuario");
						
						try {
							sentencia = con.prepareStatement("SELECT * from reviews WHERE idProducto = ? AND idUsuario = ?");
							sentencia.setInt(1, idUsuario);
							sentencia.setInt(2, idProducto);
							
							ResultSet comentariosParaEliminar = sentencia.executeQuery();
							
							while (comentariosParaEliminar.next())
							{
								idUsuario = comentariosParaEliminar.getInt("idUsuario");
								idProducto = comentariosParaEliminar.getInt("idProducto");
								comentario = comentariosParaEliminar.getString("comentario");
								valoracion = comentariosParaEliminar.getInt("valoracion");
								
								Comentario comEliminar = new Comentario(idProducto, idUsuario, comentario, valoracion);
								usuario.eliminarComentario(idProducto, idUsuario);
							}
							
							sentencia = con.prepareStatement("DELETE from reviews WHERE idProducto = ? AND idUsuario = ?");
							sentencia.setInt(1, idProducto);
							sentencia.setInt(2, idUsuario);
							sentencia.executeUpdate();
							System.out.println(idUsuario);
							System.out.println(idProducto);
							sesion.setAttribute("usuario", usuario);
							
							
							operacionesTexto = (ArrayList<String>) sesion.getAttribute("operaciones");
							operacionesTexto.add("Eliminando un comentario de la base de datos");
							sesion.setAttribute("operaciones", operacionesTexto);
							request.getRequestDispatcher("perfil.jsp").forward(request, response);
							
							break;
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						break;
				
				//loguear user
				case 7: 
						nombre = request.getParameter("nombre");
						String pass = request.getParameter("password");
						try {
							sentencia = con.prepareStatement("SELECT * from usuarios WHERE nombre = ? AND password = ?");
							sentencia.setString(1, nombre);
							sentencia.setString(2, pass);
							Usuario u = new Usuario();
							ResultSet usuarioBD = sentencia.executeQuery();
							while(usuarioBD.next())
							{
								idUsuario = usuarioBD.getInt("idUsuario");
								nombre = usuarioBD.getString("nombre");
								password = usuarioBD.getString("password");
								direccion = usuarioBD.getString("direccion");
								poblacion = usuarioBD.getString("poblacion");
								cp = usuarioBD.getInt("cp");
								pais = usuarioBD.getString("pais");
								
								u = new Usuario(idUsuario, nombre, password, direccion, poblacion, cp, pais);
							}
							
							// recogemos los comentarios de este usuario
							sentencia = con.prepareStatement("SELECT * from reviews WHERE idUsuario = ?");
							sentencia.setInt(1, idUsuario);
							ResultSet comentarios = sentencia.executeQuery();
							while(comentarios.next())
							{
								idUsuario = comentarios.getInt("idUsuario");
								idProducto = comentarios.getInt("idProducto");
								texto = comentarios.getString("comentario");
								valoracion = comentarios.getInt("valoracion");
								
								Comentario com = new Comentario(idProducto, idUsuario, texto, valoracion);
								
								u.addComentario(com);
							}
							
							
							sesion.setAttribute("usuario", u);

							operacionesTexto = (ArrayList<String>) sesion.getAttribute("operaciones");
							operacionesTexto.add("Logueando un usuario");
							sesion.setAttribute("operaciones", operacionesTexto);
							request.getRequestDispatcher("index.html").forward(request, response);
							break;
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
		}
	}
}
