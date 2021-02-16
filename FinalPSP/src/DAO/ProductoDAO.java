package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import Model.Producto;

public class ProductoDAO {

	static ConexionBBDD conexion = new ConexionBBDD();
	static Connection cn = null;
	static Statement stm = null;
	static ResultSet rs = null;
	static PreparedStatement st;
	static Producto product;

	public Producto pillarDatos(int idProducto) {
		product = null;
		try {

			cn = conexion.conectar();
			stm = cn.createStatement();
			String query = "select id_producto,nombre_producto,precio_venta,precio_proveedor,cantidad_stock from productos where id_producto=" + idProducto;
			rs = stm.executeQuery(query);

			while (rs.next()) {
				product = new Producto(rs.getInt("id_producto"), rs.getString("nombre_producto"), rs.getInt("precio_venta"),
						rs.getInt("precio_proveedor"), rs.getInt("cantidad_stock"));
				return product;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return product;
	}
}
