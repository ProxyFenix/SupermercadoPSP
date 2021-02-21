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
	 * Vale, esto es simple..
	 * 15/02: Pedimos el login, comprobarEmpleado también.
	 * 16/02: Todo está hecho por algun motivo, queda arreglar cosas mañana.
	 */
	public void run() {
		comprobarEmpleado();
		if (comprobarEmpleado()) {
			// El empleado existe, luego piensa, piensa qué va a hacer, asi que pone un
			// numero del menú.
			try {
				// Hay que leer ese numero
				sc = new Scanner(socket.getInputStream());
				pw = new PrintWriter(socket.getOutputStream(), true);
				DataInputStream flujoEntrada = new DataInputStream(socket.getInputStream());
				DataOutputStream flujoSalida = new DataOutputStream(socket.getOutputStream());
				
				String fEntr = flujoEntrada.toString();
				while (!fEntr.equals("SALIR")) {
					if (fEntr.startsWith("COBRO")) {
						pw.println("COBRO");
						// Hacemos split del mensaje, y sacamos el numero de producto y la cantidad
						String[] arrayString = fEntr.split(";", 3);
						/*
						 * arrayString[0] equivaldría a "COBRO"
						 * arrayString[1] equivaldría al numero de producto tras el primer ";"
						 * arrayString[2] equivaldría a la cantidad tras el segundo ";"
						 */
						int numeroProducto = Integer.parseInt(arrayString[1].toString());
						int cantidadProducto = Integer.parseInt(arrayString[2].toString());
						compraDia(numeroProducto, cantidadProducto);
					} else if (fEntr.equals("CAJA")) {
						String[] arrayString = fEntr.split(";", 3);
						/*
						 * arrayString[0] equivaldría a "CAJA"
						 * arrayString[1] equivaldría al numero de empleado tras el primer ";"
						 */
						int numeroEmpleado = Integer.parseInt(arrayString[1].toString());
						pw.println("CAJA");
						int total = obtenerCajaDia(numeroEmpleado);
						flujoSalida.writeInt(total);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			comprobarEmpleado();
		}

	}

	/**
	 * Igual que en el cliente, con el scanner escribimos a través del socket, y con
	 * el printwriter, leemos a través del socket Con el ObjectOutputStream,
	 * mandamos lo que nos interese a modo de objeto, como las clases.
	 * @return
	 */
	private boolean comprobarEmpleado() {

		try {
			sc = new Scanner(socket.getInputStream());
			pw = new PrintWriter(socket.getOutputStream(), true);
			ObjectOutputStream outObjeto = new ObjectOutputStream(socket.getOutputStream());
			DataOutputStream flujoSalida = new DataOutputStream(socket.getOutputStream());
			DataInputStream flujoEntrada = new DataInputStream(socket.getInputStream());
			String fEntr = flujoEntrada.readUTF();
			// Basandonos en lo que hicimos en CorreoJava, extraemos el ID del mensaje
			String idEmpleadoClienteProto = fEntr.substring(fEntr.indexOf("[")+1);
			String idEmpleadoCliente = idEmpleadoClienteProto.substring(0,idEmpleadoClienteProto.indexOf("]"));
			// Aqui pillamos tooooodos los datos

			// Pide el ID hasta que tengamos uno, y comprobamos
			while (true) {
				idEmpleado = Integer.parseInt(idEmpleadoCliente);
				// Con el ID que hemos cogido, comprobamos si existe, y creamos el objeto en
				// java
				emple = empleDao.pillarDatosEmpleConcreto(idEmpleado);
				// Si emple no es nulo, significa que si habia alguien en la bbdd, escribimos el
				// objeto
				if (emple != null) {
					outObjeto.writeObject(emple);
					return true;
				} else {
					outObjeto.writeObject(emple);
				}
				flujoSalida.writeUTF("Empleado devuelto.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * En este metodo añadimos la compra segun la cantidad y el producto, casando por sus respectivos sitios.
	 * @param idProducto
	 * @param cantidadProducto
	 */
	private void compraDia(int idProducto, int cantidadProducto) {
		Timestamp horaCompra = new Timestamp(System.currentTimeMillis());
		product = productoDao.pillarDatos(idProducto);
		compraDao.insertarCompra(horaCompra, idProducto, cantidadProducto, emple.getIdEmpleado());
		compra = compraDao.pillarDatosConcretosPorFK(product.getIdProducto(), emple.getIdEmpleado());
		System.out.println("Compra añadida.");
		
		//Si el stock después de la compra es menor o igual a 0, mandamos correo al proveedor
		if (compra.getCantidadStockFK() - product.getCantidadStock() <= 0) {
			CorreoJava correo = new CorreoJava();
			correo.mandarCorreo(product.getNombreProducto(), horaCompra.toString(), product.getPrecioProveedor());
		}

	}
	/**
	 * En este metodo cogemos la compra elegida por el empleado, casamos segun el id de producto y calculamos el total.
	 * @param numeroCompra
	 * @return
	 */
	private int obtenerCajaDia(int numeroEmpleado) {
		int total = 0;
		total = compraDao.calcularCajaDia(numeroEmpleado);
		return total;
	}
}
