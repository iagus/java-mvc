package servlets;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import persistencia.AccesoBD;
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
import java.util.Properties;


/**
 * Servlet implementation class Control
 */
@WebServlet("/control")
public class Control extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	Connection con = null;
	Properties consultas;
	AccesoBD consultasBD;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Control() {
        super();
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://localhost/tienda", "root", "root");
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
		
		//cargar el properties con las sentencias SQL	
		consultas = new Properties();
		try {
			consultas.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("script.properties"));
			System.out.println("Cogido el properties");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Problema al cargar el properties");
		}
		
	
		consultasBD = new AccesoBD(con);
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
		case 1: 
				// extraigo la lista de productos pasando el properties con las sentencias al metodo
				listaProductos = consultasBD.obtenerProductos(consultas);
				
				// añado el carrito y la lista anterior a la sesion
				Carrito carrito = new Carrito();
				sesion.setAttribute("carrito", carrito);
				sesion.setAttribute("listaProductos", listaProductos);
				
				// envio al usuario a la pagina principal
				request.getRequestDispatcher("tienda.jsp").forward(request,response);
				break;
			
				
				
		// registro de usuarios (logeo es el 7)
		case 2: 
				// recupera los datos de la request
				nombre = request.getParameter("nombre");
				String password = request.getParameter("password");
				String direccion = request.getParameter("direccion");
				String poblacion = request.getParameter("poblacion");
				int cp = Integer.parseInt(request.getParameter("cp"));
				String pais = request.getParameter("pais");
				
				// crea el objeto usuario necesario para los metodos posteriores de conexion a la BD
				Usuario usuario = new Usuario(nombre, password, direccion, poblacion, cp, pais);
				
				// registra el usuario, obtiene el idUsuario autoincrementado generado
				consultasBD.registrarUsuario(consultas, usuario);
				usuario = consultasBD.obtenerIdUsuario(consultas, usuario);
				
				// recupera los comentarios de ese usuario
				// recibe mismo objeto con los comentarios incluidos en sus datos
				usuario = consultasBD.recuperarComentariosUsuario(consultas, usuario.getIdUsuario(), usuario);
				
				// añade el usuario a la sesion
				sesion.setAttribute("usuario", usuario);
				
				break;
		
		// carrito
		case 3: break;
		
		
		// vista individual del producto con sus respectivos comentarios
		case 4: 
				// recupera el idProducto de la request
				idProducto = Integer.parseInt(request.getParameter("idP"));
				
				// extrae el objeto Producto de la base de datos
				// pero SIN los comentarios correspondientes a este producto
				Producto p = consultasBD.obtenerProducto(consultas, idProducto);
				
				// sobreescribe la variable p anterior CON los comentarios 
				// correspondientes al producto
				p = consultasBD.recuperarComentariosProducto(consultas, p, idProducto);
				
				// añade el objeto p a la sesion para acceder a el desde la vista
				sesion.setAttribute("producto", p);
				
				//redirige al usuario a la vista
				request.getRequestDispatcher("producto.jsp").forward(request,response);
				
				break;
	
				
		//insertar nuevo comentario
		case 5:	
				// recupera al usuario (que es autor del comentario) y los datos
				usuario = (Usuario) sesion.getAttribute("usuario");
				String texto = request.getParameter("comentario");
				valoracion = Integer.parseInt(request.getParameter("valoracion"));
				idProducto = Integer.parseInt(request.getParameter("producto"));
				
				// inserta en la BD y en el modelo de datos del Usuario
				consultasBD.insertarComentario(consultas, usuario, idProducto, valoracion, texto);
				break; 
					
		//eliminar comentario
		case 6:
				idUsuario = Integer.parseInt(request.getParameter("usuario"));
				idProducto = Integer.parseInt(request.getParameter("producto"));
				
				//extrae el usuario de la sesion para modificar sus datos
				usuario = (Usuario) sesion.getAttribute("usuario");
					
				// borra el comentario de la BD y del modelo, devuelve este ultimo
				usuario = consultasBD.eliminarComentario(consultas, idProducto, usuario);
					
				// devuelve el modelo a la sesion y redirige al perfil
				sesion.setAttribute("usuario", usuario);
				request.getRequestDispatcher("perfil.jsp").forward(request, response);
							
				break;
				
			
		//loguear user
		case 7: 
				// extrae los valores de la request
				nombre = request.getParameter("nombre");
				String pass = request.getParameter("password");
				
				// extrae todos los datos del usuario
				usuario = consultasBD.loguearUsuario(consultas, nombre, pass);
				
				// incluye el usuario en las variables de sesion
				sesion.setAttribute("usuario", usuario);
				
				//redirige a la pagina principal
				request.getRequestDispatcher("index.html").forward(request, response);				
		}
	}
}
