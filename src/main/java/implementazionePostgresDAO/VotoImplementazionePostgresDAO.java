package implementazionePostgresDAO;

import dao.VotoDAO;
import model.Giudice;
import model.Team;
import model.Voto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VotoImplementazionePostgresDAO implements VotoDAO {
    private final Connection connessione;

    public VotoImplementazionePostgresDAO(Connection connessione) {
        this.connessione = connessione;
    }

    public void aggiungiVoto(Voto voto) {
        String sql = "INSERT INTO voto (emailGiudice, nomeTeam, titoloHackathon ,voto) VALUES (?, ?, ?,?)";

        try (PreparedStatement stmt = connessione.prepareStatement(sql)) {
            stmt.setString(1, voto.getGiudice().getEmail());
            stmt.setString(2, voto.getTeam().getNome());
            stmt.setString(3, voto.getTeam().getHackathon().getTitolo());
            stmt.setInt(4, voto.getValore());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Voto> getTuttiVoti() {
        List<Voto> lista = new ArrayList<>();
        String sql = "SELECT * FROM voto";

        try (Statement stmt = connessione.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                // Qui devi caricare giudice e team (puoi usare DAO dedicati o lasciare null se non vuoi caricarli subito)
                Giudice giudice = null;  // oppure caricarlo da GiudiceDAO
                Team team = null;        // oppure caricarlo da TeamDAO

                int valore = rs.getInt("valore");

                Voto voto = new Voto(giudice, team, valore);
                lista.add(voto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
