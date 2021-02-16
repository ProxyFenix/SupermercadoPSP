package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBBDD {
    private static final String CONTROLADOR = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/superbbdd?useSSL=false";
    private static final String USUARIO = "root";
    private static final String CLAVE = "root";
    

    public Connection conectar() {
        Connection conexion = null;

        
        try {
            Class.forName(CONTROLADOR);
            conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);
            System.out.println("Conexión OK, maquinola");

        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el controlador, vaquero...");
            e.printStackTrace();

        } catch (SQLException e) {
            System.out.println("Error en la conexión, chupipandi.");
            e.printStackTrace();
        }
        
        return conexion;
    }
    

    //No se hasta que punto si quito esto deja de ir, asi que voy a dejarlo estar
    public static void main(String[] args) {

    }
    
}

