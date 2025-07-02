package implementazionePostgresDAO;

import dao.UtentePiattaformaDAO;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class  UtentePiattaformaImplementazionePostgresDAO implements UtentePiattaformaDAO {

    private final Connection connessione;

    // costruttore
    public UtentePiattaformaImplementazionePostgresDAO(Connection connessione) {
        this.connessione = connessione;
    }

    // metodi dell'utente
    @Override
    public void aggiungiUtente(UtentePiattaforma utente) {
        // scriviamo la query per inserire i dati
        String sql = "INSERT INTO utentePiattaforma (nome, cognome, email, pw, ruolo) VALUES (?, ?, ?, ?, ?)";

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

    // metodi dell'organizzatore
    @Override
    public void convocaGiudice(Organizzatore organizzatore, Giudice giudice, Hackathon hackathon) {
        // scriviamo la query per inserire i dati
        String sql = "INSERT INTO convocazione (emailorganizzatore, emailgiudice, titolohackathon) VALUES (?, ?, ?)";

        // prepara la query evitando SQL injection
        try (PreparedStatement stmt = connessione.prepareStatement(sql)) {
            // inserisce i valori dell'utente
            stmt.setString(1, organizzatore.getEmail());
            stmt.setString(2, giudice.getEmail());
            stmt.setString(3, hackathon.getTitolo());

            stmt.executeUpdate(); // esegue l'inserimento nel db
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getEmailGiudiciConvocati() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT emailgiudice FROM convocazione";

        try (Statement stmt = connessione.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(rs.getString("emailgiudice"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<Hackathon> getHackathonAssegnatiToGiudice(Giudice giudice) {
        List<Hackathon> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT titolohackathon FROM convocazione";

        try (Statement stmt = connessione.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Hackathon h = getHackathonByTitolo(rs.getString("titolohackathon"));
                lista.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    private Hackathon getHackathonByTitolo(String titolo) {
        Hackathon hackathon = null;
        String sql = "SELECT * FROM hackathon WHERE titolo = ?";

        try (PreparedStatement ps = connessione.prepareStatement(sql)) {
            ps.setString(1, titolo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Ricostruisci l'oggetto Hackathon con tutti i dati
                String emailOrganizzatore = rs.getString("creatore");
                Organizzatore organizzatore = getOrganizzatoreByEmail(emailOrganizzatore);
                hackathon = new Hackathon(
                        organizzatore,
                        rs.getString("titolo"),
                        rs.getInt("nummaxiscritti"),
                        rs.getInt("dimmaxteam"),
                        rs.getDate("inizioiscrizioni").toLocalDate(),
                        rs.getDate("fineiscrizioni").toLocalDate(),
                        rs.getDate("datainizio").toLocalDate(),
                        rs.getDate("datafine").toLocalDate(),
                        rs.getString("descrizioneproblema"),
                        rs.getString("classifica"),
                        rs.getString("indirizzosede"),
                        rs.getInt("numiscritti"),
                        rs.getInt("numvotiassegnati")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hackathon;
    }

    private Organizzatore getOrganizzatoreByEmail(String email) {
        String sql = "SELECT * FROM utentepiattaforma WHERE email = ?";
        try (PreparedStatement ps = connessione.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Organizzatore(
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("pw")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Organizzatore> getOrganizzatoriInvitantiToGiudice(Giudice giudice) {
        List<Organizzatore> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT emailorganizzatore FROM convocazione";

        try (Statement stmt = connessione.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Organizzatore o = getOrganizzatoreByEmail(rs.getString("emailorganizzatore"));
                lista.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public void partecipaTeam(Concorrente concorrente, String nomeTeam, String titoloHackathon) {
        // scriviamo la query per inserire i dati
        String sql = "INSERT INTO concorrente_team (emailconcorrente, nometeam, titolohackathon) VALUES (?, ?, ?)";

        // prepara la query evitando SQL injection
        try (PreparedStatement stmt = connessione.prepareStatement(sql)) {
            // inserisce i valori dell'utente
            stmt.setString(1, concorrente.getEmail());
            stmt.setString(2, nomeTeam);
            stmt.setString(3, titoloHackathon);

            stmt.executeUpdate(); // esegue l'inserimento nel db
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
