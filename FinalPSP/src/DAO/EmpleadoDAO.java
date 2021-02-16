package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Empleado;

public class EmpleadoDAO {

	static ConexionBBDD conexion = new ConexionBBDD();
	static Connection cn = null;
	static Statement stm = null;
	static ResultSet rs = null;
	static PreparedStatement st;

	public void pillarDatos() {

		try {

			cn = conexion.conectar();
			stm = cn.createStatement();
			String query = "select id_empleado,ultima_sesion,fecha_contratacion from empleado";
			rs = stm.executeQuery(query);

			while (rs.next()) {
				new Empleado(rs.getInt("id_empleado"), rs.getTimestamp("ultima_sesion"),
						rs.getDate("fecha_contratacion"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Empleado pillarDatosEmpleConcreto(int idEmpleado) {
		Empleado emple = null;
		try {

			cn = conexion.conectar();
			stm = cn.createStatement();
			String query = "select id_empleado,ultima_sesion,fecha_contratacion from empleado where id_empleado=" + idEmpleado;
			rs = stm.executeQuery(query);

			while (rs.next()) {
				emple = new Empleado(idEmpleado, rs.getTimestamp("ultima_sesion"),
						rs.getDate("fecha_contratacion"));

			}
			

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return emple;
	}
}
