package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Scanner;

import Model.Compra;
import Model.Empleado;
import Model.Producto;
import Util.CorreoJava;
import DAO.EmpleadoDAO;
import DAO.CompraDAO;
import DAO.ProductoDAO;

public class Manipulahilos implements Runnable {

	private int idEmpleado;
	private Socket socket;
	private Scanner sc;
	private PrintWriter pw;

	private Empleado emple;
	private Producto product;
	private Compra compra;

	private EmpleadoDAO empleDao = new EmpleadoDAO();
	private CompraDAO compraDao = new CompraDAO();
	private ProductoDAO productoDao = new ProductoDAO();

	public Manipulahilos(Socket socket) {
		this.socket = socket;
	}

	/**
	 * Vale, esto es simple.. 15/02: Pedimos el login, comprobarEmpleado tambi�n.
	 * 16/02: Todo est� hecho por algun motivo, queda arreglar cosas ma�ana.
	 */
	public void run() {
		comprobarEmpleado();
		// El empleado existe, luego piensa, piensa qu� va a hacer, asi que pone un
		// numero del men�.
		try {
			// Hay que leer ese numero
			sc = new Scanner(socket.getInputStream());
			pw = new PrintWriter(socket.getOutputStream(), true);
			DataInputStream flujoEntrada = new DataInputStream(socket.getInputStream());
			DataOutputStream flujoSalida = new DataOutputStream(socket.getOutputStream());

			String fEntr = flujoEntrada.readUTF().toString();
			do {
				if (fEntr.startsWith("COBRO")) {
					pw.println("COBRO");
					// Hacemos split del mensaje, y sacamos el numero de producto y la cantidad
					String[] arrayString = fEntr.split(";", 3);
					/*
					 * arrayString[0] equivaldr�a a "COBRO" arrayString[1] equivaldr�a al numero de
					 * producto tras el primer ";" arrayString[2] equivaldr�a a la cantidad tras el
					 * segundo ";"
					 */
					int numeroProducto = Integer.parseInt(arrayString[1].toString());
					int cantidadProducto = Integer.parseInt(arrayString[2].toString());
					compraDia(numeroProducto, cantidadProducto);
				} else if (fEntr.startsWith("CAJA")) {
					String[] arrayString = fEntr.split(";", 3);
					/*
					 * arrayString[0] equivaldr�a a "CAJA" arrayString[1] equivaldr�a al numero de
					 * empleado tras el primer ";"
					 */
					int numeroEmpleado = Integer.parseInt(arrayString[1].toString());
					pw.println("CAJA");
					int total = obtenerCajaDia(numeroEmpleado);
					flujoSalida.writeInt(total);
				}
			} while (!fEntr.equals("SALIR"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Igual que en el cliente, con el scanner escribimos a trav�s del socket, y con
	 * el printwriter, leemos a trav�s del socket Con el ObjectOutputStream,
	 * mandamos lo que nos interese a modo de objeto, como las clases.
	 * 
	 * @return
	 */
	private void comprobarEmpleado() {

		try {
			sc = new Scanner(socket.getInputStream());
			pw = new PrintWriter(socket.getOutputStream(), true);
			ObjectOutputStream outObjeto = new ObjectOutputStream(socket.getOutputStream());
			DataOutputStream flujoSalida = new DataOutputStream(socket.getOutputStream());
			DataInputStream flujoEntrada = new DataInputStream(socket.getInputStream());
			String fEntr = flujoEntrada.readUTF();
			// Basandonos en lo que hicimos en CorreoJava, extraemos el ID del mensaje
			String idEmpleadoClienteProto = fEntr.substring(fEntr.indexOf("[") + 1);
			System.out.println(idEmpleadoClienteProto);
			String idEmpleadoCliente = idEmpleadoClienteProto.substring(0, 1);
			System.out.println(idEmpleadoCliente);
			// Aqui pillamos tooooodos los datos

			// Pide el ID hasta que tengamos uno, y comprobamos
			idEmpleado = Integer.parseInt(idEmpleadoCliente);
			// Con el ID que hemos cogido, comprobamos si existe, y creamos el objeto en
			// java
			emple = empleDao.pillarDatosEmpleConcreto(idEmpleado);
			// Si emple no es nulo, significa que si habia alguien en la bbdd, escribimos el
			// objeto
			if (emple != null) {
				outObjeto.writeObject(emple.toString());
				flujoSalida.writeUTF("Empleado devuelto.");
			} else {
				outObjeto.writeObject(null);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * En este metodo a�adimos la compra segun la cantidad y el producto, casando
	 * por sus respectivos sitios.
	 * 
	 * @param idProducto
	 * @param cantidadProducto
	 */
	private void compraDia(int idProducto, int cantidadProducto) {
		Timestamp horaCompra = new Timestamp(System.currentTimeMillis());
		compraDao.insertarCompra(horaCompra, idProducto, cantidadProducto, emple.getIdEmpleado());
		compra = compraDao.pillarDatosConcretosPorFK(idProducto);
		System.out.println("Compra a�adida.");
		Producto productN = productoDao.pillarDatos(idProducto);
		// Si el stock despu�s de la compra es menor o igual a 0, mandamos correo al
		// proveedor
		if (productN.getCantidadStock() <= 0) {
			CorreoJava correo = new CorreoJava();
			correo.mandarCorreo(productN.getNombreProducto(), horaCompra.toString(), productN.getPrecioProveedor());
		}

	}

	/**
	 * En este metodo cogemos la compra elegida por el empleado, casamos segun el id
	 * de producto y calculamos el total.
	 * 
	 * @param numeroCompra
	 * @return
	 */
	private int obtenerCajaDia(int numeroEmpleado) {
		int total = 0;
		total = compraDao.calcularCajaDia(numeroEmpleado);
		return total;
	}
}
