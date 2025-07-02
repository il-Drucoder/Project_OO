package implementazionePostgresDAO;

import dao.DocumentoDAO;
import model.Documento;

import java.sql.*;

public class DocumentoImplementazionePostgresDAO implements DocumentoDAO {

    private final Connection connessione;

    // costruttore
    public DocumentoImplementazionePostgresDAO(Connection connessione) {
        this.connessione = connessione;
    }


    @Override
    public void aggiungiDocumento(Documento documento) {
//        String sql = "INSERT INTO documento (nomeFile, dataAggiornamento, nomeTeam, titoloHackathon) VALUES (?, ?, ?, ?)";
//
//        try (PreparedStatement stmt = connessione.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//            stmt.setString(1, documento.getNomeFile());
//            stmt.setDate(2, java.sql.Date.valueOf(documento.getDataAggiornamento()));
//            stmt.setString(3, documento.getTeam().getNome());
//            stmt.setString(4, documento.getTeam().getHackathon().getTitolo());
//
//            stmt.executeUpdate();
//
//            // recupera id generato
//            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
//                if (generatedKeys.next()) {
//                    documento.setIdDocumento(generatedKeys.getInt(1));
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }





}
