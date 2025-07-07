package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {

    private static ConnessioneDatabase instance; // serve a mantenere una sola istanza della classe
    private Connection connection = null;   // è l'oggetto che permette di eseguire query sul db
    String nome = "alessiapicari";
    String password = "pdb2025!";
    String url = "jdbc:postgresql://localhost:5432/projectHackathon";
    String driver = "org.postgresql.Driver";

    // Costruttore
    public ConnessioneDatabase() throws SQLException {
        try {
            Class.forName(driver);
            setConnection(DriverManager.getConnection(url, nome, password));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Impossibile effettuare la connessione al DB!", e);
        }
    }

    // metodo singleton serve a garantire che venga creata una sola istanza di una classe in tutta l'applicazione serve ad avere una sola connessione al db
    public static ConnessioneDatabase getInstance() throws SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new ConnessioneDatabase();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
