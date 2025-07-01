package dao;

import model.Team;
import java.util.List;

public interface TeamDAO {

    void aggiungiTeam(Team team);
    List<Team> getTuttiTeam();
}
