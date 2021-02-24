package Cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {

	public static void main(String[] args) throws UnknownHostException, IOException {

		var client = new Cliente();
		System.out.println("Cliente encendido.");
		client.run();

	}

	private static void run() throws IOException {
		String host = "localhost";
		int puerto = 59001;
		Socket clienteSoc = new Socket(host, puerto);
		Scanner sc = new Scanner(System.in);
		DataOutputStream flujoSalida = new DataOutputStream(clienteSoc.getOutputStream());
		System.out.println("Introduce tu id de empleado");
		int id = sc.nextInt();
		// Con esto mandamos un mensaje al chervidor digo servidor
		flujoSalida.writeUTF("LOGIN;[" + id + "]");
		// Y con esto, lo recibimos
		DataInputStream flujoEntrada = new DataInputStream(clienteSoc.getInputStream());
		ObjectInputStream objetoEntrada = new ObjectInputStream(clienteSoc.getInputStream());
		try {
			System.out.println(objetoEntrada.readObject().toString());
		} catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
		}
		if (flujoEntrada.readUTF().equals("Empleado devuelto.")) {
			System.out.println("Elija qu� desea hacer, por favor./n" + "Pulse 1 para tratar la compra m�s reciente./n"
					+ "Pulse 2 para obtener el total de beneficio del d�a./n" + "Pulse 3 para salir.");
			int numeroMenu = sc.nextInt();
			switch (numeroMenu) {
			case 1:
				flujoSalida.writeUTF("COBRO");
				System.out.println("ART�CULOS DE LOS BUENOS:/n" + "1. Disco duro/n" + "2. USB/n" + "3. Monitor/n"
						+ "4. Rat�n/n" + "Por favor, no pongas otro numero que me asusto");
				System.out.println("Seleccione el art�culo que desea:");
				int pro = getProductoEspecifico();
				// Si el numero es 0, vuelve a preguntar.
				do {
					System.out.println("Error, has seleccionado un n�mero no v�lido");
					System.out.println("Seleccione el art�culo que desea:");
					pro = getProductoEspecifico();
				} while (pro == 0);
				System.out.println("Ha elegido el n�mero " + pro);

				// Lo mismo con la cantidad
				System.out.println("�Cu�ntas unidades?");
				int can = getCantidadEspecifica();
				do {
					System.out.println("Error, has seleccionado un n�mero no v�lido");
					can = getCantidadEspecifica();
					System.out.println("�Cu�ntas unidades?");
				} while (can == 0);
				System.out.println("Ha a�adido al carrito " + can + "unidades del producto n�mero " + pro);
				flujoSalida.writeUTF("COBRO;" + pro + ";" + can);
				break;

			case 2:
				// Pasamos el id de compra para elegir una en concreto
				System.out.println("Introduzca el id de la compra: ");
				int idCompra = sc.nextInt();
				do {
					System.out.println("Error, id incorrecto o ninguna compra efectuada durante el d�a de hoy. Pruebe de nuevo.");
					idCompra = sc.nextInt();
				} while (idCompra <= 0);
				flujoSalida.writeUTF("CAJA;" + idCompra);
				int total = flujoEntrada.readInt();
				System.out.println("El valor total de la caja del d�a es de " + total + " bitcoins.");
				break;

			case 3:
				flujoSalida.writeUTF("SALIR");
				System.out.println("Pase un buen d�a.");
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				clienteSoc.close();
				System.exit(0);
			}

		}
	}

	private static int getProductoEspecifico() {
		Scanner sc = new Scanner(System.in);
		int producto = sc.nextInt();
		while (producto > 0) {
			while (producto < 5) {
				return producto;
			}
		}

		return 0;
	}

	private static int getCantidadEspecifica() {
		Scanner sc = new Scanner(System.in);
		int cantidad = sc.nextInt();
		while (cantidad > 0) {
			return cantidad;
		}
		return 0;
	}

}
