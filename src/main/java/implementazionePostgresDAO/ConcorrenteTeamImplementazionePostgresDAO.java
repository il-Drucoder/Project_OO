package implementazionePostgresDAO;

import dao.ConcorrenteTeamDAO;

import java.sql.*;

public class ConcorrenteTeamImplementazionePostgresDAO implements ConcorrenteTeamDAO {
    private Connection connessione;

    public ConcorrenteTeamImplementazionePostgresDAO(Connection connessione) {
        this.connessione = connessione;
    }


    @Override
    public void aggiungiConcorrenteTeam(String emailConcorrente, String nomeTeam, String titoloHackathon, String idCreatore) {

    }
}
