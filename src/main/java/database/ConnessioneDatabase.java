package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {

    private static ConnessioneDatabase instance; //serve a mantenere una sola istanza della classe
    public Connection connection = null;   // è l'oggetto che permette di eseguire query sul db
    private String nome = "alessiapicari";
    private String password = "pdb2025!";
    private String url = "jdbc:postgresql://localhost:5432/projectHackathon";
    private String driver = "org.postgresql.Driver";

    // costruttore
    private ConnessioneDatabase() throws SQLException {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, nome, password);
        } catch (ClassNotFoundException ex) {
            System.out.println("Database Connection Creation Failed : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /* metodo singleton serve a garantire che venga creata una sola istanza di una classe in tutta l'applicazione
       serve ad avere una sola connessione al db
     */
    public static ConnessioneDatabase getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConnessioneDatabase();
        } else if (instance.connection.isClosed()) {
            instance = new ConnessioneDatabase();
        }
        return instance;
    }
}
