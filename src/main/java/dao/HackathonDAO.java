package dao;

import model.Hackathon;
import java.util.List;

public interface HackathonDAO {
    void aggiungiHackathon(Hackathon hackathon);
    List<Hackathon> getTuttiHackathon();
}
