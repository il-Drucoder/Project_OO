package dao;

import model.UtentePiattaforma;
import java.util.List;

public interface UtentePiattaformaDAO {

    void aggiungiUtente(UtentePiattaforma utente);
    List<UtentePiattaforma> getTuttiUtenti();
    List<UtentePiattaforma> getUtentiPerRuolo(String ruolo);


}
