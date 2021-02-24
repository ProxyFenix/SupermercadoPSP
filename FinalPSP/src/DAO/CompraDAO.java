package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;

import Model.Compra;
import Model.Producto;

public class CompraDAO {

	static ConexionBBDD conexion = new ConexionBBDD();
	static Connection cn = null;
	static Statement stm = null;
	static ResultSet rs = null;
	static PreparedStatement st;
	
	static Compra compra;

	public Compra pillarDatosConcretosPorFK(int idProducto) {
		Compra compraN = null;
		try {

			cn = conexion.conectar();
			stm = cn.createStatement();
			String query = "select id_compra,fecha,id_producto,cantidad_producto,id_empleado from compra where "
					+ "id_producto=" + idProducto;
			rs = stm.executeQuery(query);

			while (rs.next()) {
				compraN = new Compra(rs.getInt("id_compra"), rs.getTimestamp("fecha"), rs.getInt("id_producto"),
						rs.getInt("cantidad_producto"), rs.getInt("id_empleado"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return compraN;
	}
	
	public Compra pillarDatosConcretosPorID(int idCompra) {
		Compra compraN = null;
		try {

			cn = conexion.conectar();
			stm = cn.createStatement();
			String query = "select id_compra,fecha,id_producto,cantidad_producto,id_empleado from compra where "
					+ "id_compra=" + idCompra;
			rs = stm.executeQuery(query);

			while (rs.next()) {
				compraN = new Compra(rs.getInt("id_compra"), rs.getTimestamp("fecha"), rs.getInt("id_producto"),
						rs.getInt("cantidad_producto"), rs.getInt("id_empleado"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return compraN;
	}

	public void insertarCompra(Timestamp fecha, int idProducto, int cantidadProducto, int idEmpleado) {

		try {
			cn = conexion.conectar();
			String query = "insert into compra (fecha,id_producto,cantidad_producto,id_empleado) "
					+ "values ('" + fecha + "'," + idProducto + "," + cantidadProducto + ", " + idEmpleado + ")";
			st = cn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			st.executeUpdate(query);
			
			String queryUpd = "UPDATE producto SET cantidad_stock = "
                    + "(cantidad_stock - " + cantidadProducto + ") "
                    + "WHERE id_producto = " + idProducto; 
			st.executeUpdate(queryUpd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int calcularCajaDia(int idEmpleado) {
		LocalDate dia = LocalDate.now();
		String diaString = dia.toString();
		int total = 0;
		try {
			cn = conexion.conectar();
			stm = cn.createStatement();
			String query = "select SUM((precio_venta - precio_proveedor) * cantidad_producto) as Resultado from compra,producto where "
					+ "compra.id_producto=producto.id_producto and "
					+ "id_empleado=" + idEmpleado + " and fecha.compra like '%" + diaString + "%'";
			rs = stm.executeQuery(query);
			
			while (rs.next()) {
				total = rs.getInt("Resultado");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}

}
