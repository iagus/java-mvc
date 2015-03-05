<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="modelos.Producto" %>
<%@ page import="modelos.Carrito" %>
<%@ page import="modelos.Comentario" %>
<%@ page import="modelos.Usuario" %>
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
        		HttpSession sesion=request.getSession();
        		Producto producto = (Producto) sesion.getAttribute("producto");
            	ArrayList<Comentario> comentarios = new ArrayList<Comentario>();
            	comentarios = (ArrayList<Comentario>) sesion.getAttribute("listaComentarios");
        	%>
        	<h2><%= producto.getNombre() %></h2>
        	<img src="<%= producto.getImgUrl() %>" width="400px" />
        	<p><%= producto.getDescripcion() %></p>
        	<a href="control?oculto=3" class="btn btn-default">Add to cart!</a>
        	
        	<!--  comentarios -->
        	<table class="table">
        	<caption>Reviews</caption>
        			<th>Usuario</th>
        			<th>Comentario</th>
        			<th>Valoracion</th>
        	<% for (Comentario c:comentarios) { %>
        			<tr>
        				<td><%= c.getIdUsuario() %></td>
        				<td><%= c.getTexto() %></td>
        				<td><% for (int i = 0; i < c.getValoracion(); i++) { %>
        						<span class="glyphicon glyphicon-star"></span>
        					<% } %>
        				</td>
        				<% Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        					if (usuario != null && usuario.getNombre().equalsIgnoreCase(c.getIdUsuario())) {%>
        					<a href="control?oculto=6" class="btn btn-default">Eliminar comentario</a>
        				<%} %>
        			</tr>
        	<% } %>
        	</table>
        	<% Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        		if (usuario == null){ %>
        		<a href="perfil.jsp" class="btn btn-default">Regístrate para comentar este producto</a>
        	<% } else { %>
        		<form method="post" action="control?oculto=5">
        			
        			<div class="form-group">
        				<label for="comentario">Comentario:</label>
        				<textarea name="comentario" class="form-control"></textarea>
        			</div>
        			
        			<div class="form-group">
        				<label for="valoracion">Valoracion:</label>
        				<select name="valoracion">
        					<option value="5">5</option>
        					<option value="4">4</option>
        					<option value="3">3</option>
        					<option value="2">2</option>
        					<option value="1">1</option>
        				</select>
        			</div>
        			
        			<input type="hidden" name="producto" value="<%= producto.getIdProducto() %>" />
        			<input type="hidden" name="usuario" value="<%= usuario.getIdUsuario() %>" />
        			<input type="submit" value="Comentar" class="btn btn-default"/>
        		</form>
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