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

	public Compra pillarDatosConcretosPorFK(int idEmpleado, int idProducto) {
		compra = null;
		try {

			cn = conexion.conectar();
			stm = cn.createStatement();
			String query = "select id_compra,fecha,id_producto,cantidad_producto,id_empleado from compra where "
					+ "id_producto=" + idProducto + " and id_empleado=" + idEmpleado;
			rs = stm.executeQuery(query);

			while (rs.next()) {
				compra = new Compra(rs.getInt("id_compra"), rs.getTimestamp("fecha"), rs.getInt("id_producto"),
						rs.getInt("cantidad_producto"), rs.getInt("id_empleado"));
				return compra;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return compra;
	}
	
	public Compra pillarDatosConcretosPorID(int idCompra) {
		compra = null;
		try {

			cn = conexion.conectar();
			stm = cn.createStatement();
			String query = "select id_compra,fecha,id_producto,cantidad_producto,id_empleado from compra where "
					+ "id_compra=" + idCompra;
			rs = stm.executeQuery(query);

			while (rs.next()) {
				compra = new Compra(rs.getInt("id_compra"), rs.getTimestamp("fecha"), rs.getInt("id_producto"),
						rs.getInt("cantidad_producto"), rs.getInt("id_empleado"));
				return compra;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return compra;
	}

	public void insertarCompra(Timestamp fecha, int idProducto, int cantidadProducto, int idEmpleado) {

		try {
			cn = conexion.conectar();
			String query = "insert into compras (id_compra,fecha,id_producto,cantidad_producto,id_empleado) "
					+ "values (0,?,?,?,?)";
			st = cn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			st.setTimestamp(1, fecha);
			st.setInt(2, idProducto);
			st.setInt(3, cantidadProducto);
			st.setInt(4, idEmpleado);
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
