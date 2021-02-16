package Server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public class Servidor {

    public static void main(String[] args) throws Exception {
        System.out.println("El server va a pedales, pero va...");
        var pool = Executors.newFixedThreadPool(500);
        //Buscamos este puerto, que coincide con el del cliente, y que encima está libre siempre
        try (var listener = new ServerSocket(59001)) {
            while (true) {
                pool.execute(new Manipulahilos(listener.accept()));
            }
        }
    }

}
