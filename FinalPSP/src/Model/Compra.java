package Model;

import java.sql.Timestamp;

public class Compra {
	private int idCompra;
	private Timestamp fecha;
	private int idProductoFK;
	private int cantidadStockFK;
	private int idEmpleadoFK;
	
	public Compra(int idCompra, Timestamp fecha, int idProductoFK, int cantidadStockFK, int idEmpleadoFK) {
		super();
		this.idCompra = idCompra;
		this.fecha = fecha;
		this.idProductoFK = idProductoFK;
		this.cantidadStockFK = cantidadStockFK;
		this.idEmpleadoFK = idEmpleadoFK;
	}

	public int getIdCompra() {
		return idCompra;
	}

	public void setIdCompra(int idCompra) {
		this.idCompra = idCompra;
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public int getIdProductoFK() {
		return idProductoFK;
	}

	public void setIdProductoFK(int idProductoFK) {
		this.idProductoFK = idProductoFK;
	}

	public int getCantidadStockFK() {
		return cantidadStockFK;
	}

	public void setCantidadStockFK(int cantidadStockFK) {
		this.cantidadStockFK = cantidadStockFK;
	}

	public int getIdEmpleadoFK() {
		return idEmpleadoFK;
	}

	public void setIdEmpleadoFK(int idEmpleadoFK) {
		this.idEmpleadoFK = idEmpleadoFK;
	}	
}
