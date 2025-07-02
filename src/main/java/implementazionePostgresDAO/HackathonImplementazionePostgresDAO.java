package implementazionePostgresDAO;

import dao.HackathonDAO;
import model.Hackathon;
import model.Organizzatore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HackathonImplementazionePostgresDAO implements HackathonDAO {

    private final Connection connessione;

    public HackathonImplementazionePostgresDAO(Connection connessione) {
        this.connessione = connessione;
    }

    @Override
    public void aggiungiHackathon(Hackathon hackathon) {
        String sql = "INSERT INTO hackathon (titolo, datainizio, datafine, nummaxiscritti, dimmaxteam, inizioiscrizioni, fineiscrizioni, descrizioneproblema, classifica, indirizzosede, creatore, numiscritti, numvotiassegnati) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connessione.prepareStatement(sql)) {
            stmt.setString(1, hackathon.getTitolo());
            stmt.setDate(2, Date.valueOf(hackathon.getDataInizio()));
            stmt.setDate(3, Date.valueOf(hackathon.getDataFine()));
            stmt.setInt(4, hackathon.getNumMaxIscritti());
            stmt.setInt(5, hackathon.getDimMaxTeam());
            stmt.setDate(6, Date.valueOf(hackathon.getInizioIscrizioni()));
            stmt.setDate(7, Date.valueOf(hackathon.getFineIscrizioni()));
            stmt.setString(8, null);
            stmt.setString(9, null);
            stmt.setString(10, hackathon.getIndirizzoSede());
            stmt.setString(11, hackathon.getCreatore().getEmail());
            stmt.setInt(12, 0);
            stmt.setInt(13,0);

            stmt.executeUpdate();
            System.out.println("Hackathon inserito con successo.");
        } catch (SQLException e) {
            System.out.println("Errore durante l'inserimento dell'hackathon:");
            e.printStackTrace();
        }
    }

    @Override
    public List<Hackathon> getTuttiHackathon() {
        List<Hackathon> lista = new ArrayList<>();
        String sql = "SELECT * FROM hackathon";

        try (Statement stmt = connessione.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String emailOrganizzatore = rs.getString("creatore");
                Organizzatore organizzatore = getOrganizzatoreByEmail(emailOrganizzatore);

                Hackathon h = new Hackathon(
                        // query che restituisce un organizzatore tramite email
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
                lista.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void setDescrizioneHackathon(Hackathon hackathon, String descrizione) {
        String sql = "UPDATE hackathon SET descrizione = ? WHERE titolo = ?";
        try (PreparedStatement stmt = connessione.prepareStatement(sql)) {
            stmt.setString(1,descrizione);
            stmt.setString(2,hackathon.getTitolo());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setClassificaHackathon(Hackathon hackathon, String classifica) {
        String sql = "UPDATE hackathon SET classifica = ? WHERE titolo = ?";
        try (PreparedStatement stmt = connessione.prepareStatement(sql)) {
            stmt.setString(1,classifica);
            stmt.setString(2,hackathon.getTitolo());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
}
