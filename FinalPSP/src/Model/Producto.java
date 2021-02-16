package Model;

public class Producto {

	private int idProducto;
	private String nombreProducto;
	private int precioVenta;
	private int precioProveedor;
	private int cantidadStock;
	
	public Producto(int idProducto, String nombreProducto, int precioVenta, int precioProveedor, int cantidadStock) {
		super();
		this.idProducto = idProducto;
		this.nombreProducto = nombreProducto;
		this.precioVenta = precioVenta;
		this.precioProveedor = precioProveedor;
		this.cantidadStock = cantidadStock;
	}

	public int getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(int idProducto) {
		this.idProducto = idProducto;
	}

	public String getNombreProducto() {
		return nombreProducto;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	public int getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(int precioVenta) {
		this.precioVenta = precioVenta;
	}

	public int getPrecioProveedor() {
		return precioProveedor;
	}

	public void setPrecioProveedor(int precioProveedor) {
		this.precioProveedor = precioProveedor;
	}

	public int getCantidadStock() {
		return cantidadStock;
	}

	public void setCantidadStock(int cantidadStock) {
		this.cantidadStock = cantidadStock;
	}
		
}
