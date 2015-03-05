package modelos;

import java.util.ArrayList;

public class Carrito {
		
		ArrayList<Producto> carrito;
		int numProductosEnCarrito;
		
		public Carrito()
		{
			carrito = new ArrayList<Producto>();
			numProductosEnCarrito = 0;
		}

		public ArrayList<Producto> getCarrito() {
			return carrito;
		}
		
		public void addProducto(Producto p)
		{
			carrito.add(p);
		}
		
		public int getNumProductosEnCarrito()
		{
			return numProductosEnCarrito;
		}

		public void setNumProductosEnCarrito(int numProductosEnCarrito) {
			this.numProductosEnCarrito += numProductosEnCarrito;
		}


}
