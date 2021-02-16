package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/*
 * Codigo de https://www.javatpoint.com/example-of-sending-email-using-java-mail-api
 * con modificaciones
 * 
 * 15/02/21 En proceso
 */
public class CorreoJava {

	public void mandarCorreo(String nombreProducto, String hora, int precioProveedor) {

		
		String host = "localhost";
		final String user = "SupermercadosWhiteBase@Argama.com";// Correo cualquiera
		// final String passwd = "LaBebesitaBebelin";
		String toUser = "";

		String finalToUser = "";
		String firstToUser = "";

		URL resource = getClass().getResource("correo.xml");
		File correoXML = new File(resource.getPath());
		System.out.println(correoXML);

		//Aqui antes habia un StringBuilder, pero era menos eficiente
		String lineaProto = "";

		// Encontramos el email en el XML
		try {
			BufferedReader lector = new BufferedReader(new FileReader(correoXML));
			String linea;
			while ((linea = lector.readLine()) != null) {
				if (linea.contains("<EmailEmergencia>")) {
					lineaProto = linea;
				}

			}
			// Aislamos todo del email
			System.out.println(lineaProto);
			firstToUser = lineaProto.substring(lineaProto.indexOf(">") + 1);
			System.out.println(firstToUser);
			finalToUser = firstToUser.substring(0, firstToUser.indexOf("<"));
			System.out.println(finalToUser);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		// Get the session object
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		Session session = Session.getDefaultInstance(properties);
		// Compose the message
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(finalToUser));
			message.setSubject("Falta de producto" + nombreProducto);
			message.setText("El producto" + nombreProducto + "ha acabado sus existencias a las " + hora
					+ ". El precio por el cual se adquirió a su empresa fue de " + precioProveedor + " bitcoins.");

			// send the message
			Transport.send(message);
			System.out.println(message);
			System.out.println("message sent successfully...");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}