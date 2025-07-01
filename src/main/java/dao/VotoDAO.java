package dao;

import model.Voto;
import java.util.List;

public interface VotoDAO {

    void aggiungiVoto(Voto voto);
    List<Voto> getTuttiVoti();
}
