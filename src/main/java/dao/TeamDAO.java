package dao;

import model.Concorrente;
import model.Team;
import java.util.List;

public interface TeamDAO {

    void aggiungiTeam(Team team);
    List<Team> getTuttiTeam();
    List<Concorrente> getConcorrentiOfTeam(Team team);
}
