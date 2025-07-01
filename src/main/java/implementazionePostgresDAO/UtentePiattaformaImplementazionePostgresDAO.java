package implementazionePostgresDAO;

import dao.UtentePiattaformaDAO;
import model.UtentePiattaforma;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class  UtentePiattaformaImplementazionePostgresDAO implements UtentePiattaformaDAO {

    private final Connection connessione;

    // costruttore
    public UtentePiattaformaImplementazionePostgresDAO(Connection connessione) {
        this.connessione = connessione;
    }


    @Override
    public void aggiungiUtente(UtentePiattaforma utente) {
        // scriviamo la query per inserire i dati
        String sql = "INSERT INTO utentePiattaforma (nome, cognome, email, pw, ruolo) VALUES (?, ?, ?, ?,?)";

        // prepara la query evitando SQL injection
        try (PreparedStatement stmt = connessione.prepareStatement(sql)) {
            // inserisce i valori dell'utente
            stmt.setString(1, utente.getNome());
            stmt.setString(2, utente.getCognome());
            stmt.setString(3, utente.getEmail());
            stmt.setString(4, utente.getPw());
            stmt.setString(5, utente.getRuolo());

            stmt.executeUpdate(); // esegue l'inserimento nel db
            System.out.println("Utente inserito con successo.");
        } catch (SQLException e) {
            System.out.println("Errore durante l'inserimento dell'utente:");
            e.printStackTrace();
        }
    }



    @Override
    public List<UtentePiattaforma> getTuttiUtenti() {
        List<UtentePiattaforma> lista = new ArrayList<>();
        String sql = "SELECT * FROM utentePiattaforma";

        try (Statement stmt = connessione.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                UtentePiattaforma u = new UtentePiattaforma(
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("pw"),
                        rs.getString("ruolo")
                );
                lista.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<UtentePiattaforma> getUtentiPerRuolo(String ruolo) {
        List<UtentePiattaforma> lista = new ArrayList<>();
        String sql = "SELECT nome, cognome, email, pw, ruolo FROM utente WHERE ruolo = ?";

        try (PreparedStatement stmt = connessione.prepareStatement(sql)) {
            stmt.setString(1, ruolo);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UtentePiattaforma u = new UtentePiattaforma(
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("email"),
                            rs.getString("pw"),
                            rs.getString("ruolo")
                    );
                    lista.add(u);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }




}
