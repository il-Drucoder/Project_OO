package dao;

import model.*;

import java.util.List;

public interface UtentePiattaformaDAO {

    // metodi dell'utente
    void aggiungiUtente(UtentePiattaforma utente);
    List<UtentePiattaforma> getTuttiUtenti();

    // metodi dell'organizzatore
    void convocaGiudice(Organizzatore organizzatore, Giudice giudice, Hackathon hackathon);
    List<String> getEmailGiudiciConvocati();
    List<Hackathon> getHackathonAssegnatiToGiudice(Giudice giudice);
    List<Organizzatore> getOrganizzatoriInvitantiToGiudice(Giudice giudice);
    void partecipaTeam(Concorrente concorrente, String nomeTeam, String titoloHackathon);
}
