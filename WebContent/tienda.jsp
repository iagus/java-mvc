<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="modelos.Producto" %>
<%@ page import="modelos.Carrito" %>
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
                        <a href="perfil.jsp">Profile</a>
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
	  	Carrito carrito = (Carrito) sesion.getAttribute("carrito");
		ArrayList<Producto> listaProductos = new ArrayList<Producto>();
		listaProductos = (ArrayList<Producto>) sesion.getAttribute("listaProductos");
		int contador = 0;
	%>
		
		<% for (Producto p:listaProductos)
		{%>
			
					<% if (contador % 3 == 0 || contador == 0) { %> <div class="row"><%} %>
                    <div class="col-sm-4 col-lg-4 col-md-4">
                        <div class="thumbnail">
                            <img src="<%= p.getImgUrl() %>" alt="">
                            <div class="caption">
                                <h4 class="pull-right"><%= p.getPrecio() %></h4>
                                <h4><a href="control?oculto=4&idP=<%= p.getIdProducto() %>"><%= p.getNombre() %></a>
                                </h4>
                            </div>
                            <div class="ratings">
                                <p class="pull-right"><%= p.totalComentarios() %> reviews</p>
                                <p>
                                	<% for (int i = 0; i < p.mediaComentarios(); i++) { %>
                                    	<span class="glyphicon glyphicon-star"></span>
                                    <%} %>
                                </p>
                            </div>
                        </div>
                 </div>
                 <% contador++; %>
                 <% if (contador % 3 == 0) { %> </div><%} %>
		<%}
	%>
	</div>
	 <div class="container">

        <hr>

        <!-- Footer -->
        <footer>
            <div class="row">
                <div class="col-lg-12">
                    <p>Copyright &copy; santiago@DAW 2015</p>
                </div>
            </div>
        </footer>

    </div>
    <!-- /.container -->

    <!-- Bootstrap Core JavaScript -->
    <script src="js/bootstrap.min.js"></script>
</body>
</html>