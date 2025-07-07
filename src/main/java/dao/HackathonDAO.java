package dao;

import model.Hackathon;
import java.util.List;

public interface HackathonDAO {
    void aggiungiHackathon(Hackathon hackathon);
    List<Hackathon> getTuttiHackathon();
    void setDescrizioneHackathon(Hackathon hackathon, String descrizione);
    void setClassificaHackathon(Hackathon hackathon, String classifica);
    void incrementaTeam(String titoloHackathon);
    void incrementaVoti(String titoloHackathon);
}
