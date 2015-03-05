<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="modelos.Producto" %>
<%@ page import="modelos.Carrito" %>
<%@ page import="modelos.Comentario" %>
<%@ page import="modelos.Usuario" %>
<%@page import = "java.util.*"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Shop Homepage</title>
    <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="bootstrap/css/shop-homepage.css" rel="stylesheet">
</head>
<body>
<!-- Navigation -->
    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <div class="container">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">Daw05 Shop!</a>
            </div>
            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li>
                        <a href="control?oculto=1">Home</a>
                    </li>
                    <li>
                        <a href="#">Profile</a>
                    </li>
                    <li>
                        <a href="#">Cart</a>
                    </li>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container -->
    </nav>
    <div class="container">

        <div class="row">

            <div class="col-md-3">
                <p class="lead">Welcome to Daw05Shop!</p>
                <div class="list-group">
                    <a href="#" class="list-group-item">Clothing</a>
                    <a href="#" class="list-group-item">Accesories</a>
                </div>
            </div>
        
        <div class="col-md-9">
        	<%
        		HttpSession sesion = request.getSession();
        		Usuario usuario = (Usuario) sesion.getAttribute("usuario");
            		
            	if (usuario == null)
            	{%>
            		<h1>Logueo o Registro?</h1>
            		<hr>
            		<h1>Logueo</h1>
            		<form action="control?oculto=7" method="post">
            			<div class="form-group">
            				<label for="nombre">Nombre de usuario: </label>
            				<input type="text" name="nombre" class="form-control" />
            			</div>
            			<div class="form-group">
            				<label for="password">Password:</label>
            				<input type="password" name="password" class="form-control" />
            			</div>
            			<input type="submit" value="Loguearse" class="btn-btn-default"/>
            		</form>
            		<hr/>
            		<h2>Registro</h2>
            		<form action="control?oculto=2" method="post">
            			<div class="form-group">
            				<label for="nombre">Nombre de usuario: </label>
            				<input type="text" name="nombre" class="form-control" />
            			</div>
            			<div class="form-group">
            				<label for="password">Password:</label>
            				<input type="password" name="password" class="form-control" />
            			</div>
            			<div class="form-group">
            				<label for="direccion">Direccion:</label>
            				<input type="text" name="direccion" class="form-control" />
            			</div>
            			<div class="form-group">
            				<label for="poblacion">Poblacion:</label>
            				<input type="text" name="poblacion" class="form-control" />
            			</div>
            			<div class="form-group">
            				<label for="cp">CP:</label>
            				<input type="number" name="cp" class="form-control" />
            			</div>
            			<div class="form-group">
            				<label for="pais">Pais:</label>
            				<input type="text" name="pais" class="form-control" />
            			</div>
            			<input type="submit" value="Registrarse" class="btn-btn-default"/>
            		</form>	
            		
            <%	} else {
        	%>			<h1>Hola, <%= usuario.getNombre() %></h1>
        				<hr/>
        				<h3>Historial de Reviews</h3>
        				<table class="table">
        					<th>Producto</th>
        					<th>Comentario</th>
        					<th>Valoracion</th>
        					<th>Eliminar?</th>
        				<% for(Comentario c:usuario.getComentarios()) { %>
        					<tr>
        						<td><a href="control?codigo=4&idP=<%= c.getIdProducto() %>"><%= c.getIdProducto() %></a></td>
        						<td><%= c.getTexto() %></td>
        						<td>
        							<% for(int i = 0; i < c.getValoracion(); i++){ %>
        								<span class="glyphicon glyphicon-star"></span>
        							<%} %>
        						</td>
        						<td>
        							<form method="post" action="control?oculto=6" />
        								<input type="hidden" value="<%= c.getIdProducto() %>" name="producto" />
        								<input type="hidden" value="<%= c.getCodigoUsuario() %>" name="usuario" />
        								<input type="submit" value="Borrar comentario" class="btn btn-default"/>
        							</form>
        						</td>
        					</tr>
        				<%} %>
        				</table>
        				<hr>
        				<h4>Inicio de sesion: <%= new Date(sesion.getCreationTime()) %></h4>
        				<% ArrayList<String> operaciones = (ArrayList<String>) sesion.getAttribute("operaciones"); %>
        				<h4>Operaciones totales: <%= operaciones.size() %> </h4>
        				<h4>Operaciones:</h4>
        					<ul>
        					<% for(int i = 0; i < operaciones.size(); i++){ %>
        				<%{ %>
        						<li><%= operaciones.get(i) %></li>
        				<%} %>
        					</ul>
        	<% } %>
        <%} %>	
        </div>
        <div class="container">
        <hr>
        <!-- Footer -->
        <footer>
            <div class="row">
                <div class="col-lg-12">
                    <p>Copyright &copy; santiago@Daw 2015</p>
                </div>
            </div>
        </footer>

    </div>
    <!-- /.container -->

    <!-- Bootstrap Core JavaScript -->
    <script src="js/bootstrap.min.js"></script>
</body>
</html>