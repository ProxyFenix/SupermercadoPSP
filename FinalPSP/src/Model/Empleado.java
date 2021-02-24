package Model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class Empleado implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int idEmpleado;
	private Timestamp ultimaSesion;
	private Date fechaContratacion;
	
	public Empleado(int idEmpleado, Timestamp ultimaSesion, Date fechaContratacion) {
		super();
		this.idEmpleado = idEmpleado;
		this.ultimaSesion = ultimaSesion;
		this.fechaContratacion = fechaContratacion;
	}

	public int getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(int idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public Timestamp getUltimaSesion() {
		return ultimaSesion;
	}

	public void setUltimaSesion(Timestamp ultimaSesion) {
		this.ultimaSesion = ultimaSesion;
	}

	public Date getFechaContratacion() {
		return fechaContratacion;
	}

	public void setFechaContratacion(Date fechaContratacion) {
		this.fechaContratacion = fechaContratacion;
	}		
	
	public String toString() {
		String linea = "";
		linea = "El empleado de id: " + idEmpleado + " cuya última conexión fue el " + ultimaSesion.toString() + "y que fue contratado el " + fechaContratacion + ".\n";
		return linea;
	}
}
