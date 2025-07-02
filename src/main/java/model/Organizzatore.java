package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Organizzatore extends UtentePiattaforma {
    // rappresentazione relazioni
    private final List<Hackathon> hackathonCreati = new ArrayList<>();
    private final List<Giudice> giudiciConvocati = new ArrayList<>();

    // Costruttore
    public Organizzatore(String nome, String cognome, String email, String password) {
        super(nome, cognome, email, password, "organizzatore"); // chiama il costruttore padre
    }

    // getter per la lista di hackathon creati
    public List<Hackathon> getHackathonCreati() { return hackathonCreati; }
    // metodo per la creazione di un nuovo Hackathon da parte di un organizzatore
    public Hackathon creaHackathon(String titolo, int numMaxIscritti, int dimMaxTeam, LocalDate inizioIscrizioni, LocalDate dataInizio, LocalDate dataFine, String indirizzoSede) {
        // verifica che la data di inizio Hackathon non venga dopo la data di fine Hackathon
        if (dataInizio.isAfter(dataFine)) {
            throw new IllegalArgumentException("Errore: date di inizio/fine non valide");
        }
        // verifica che la data di inizio iscrizioni sia almeno 2 giorni prima dalla data di inizio Hackathon
        if (!inizioIscrizioni.isBefore(dataInizio.minusDays(1))) {
            throw new IllegalArgumentException("Errore: data di inizio iscrizioni non valida");
        }
        Hackathon hackathon = new Hackathon(this, titolo, numMaxIscritti, dimMaxTeam, inizioIscrizioni, dataInizio, dataFine, indirizzoSede);  // Passa l'organizzatore come creatore
        hackathonCreati.add(hackathon);  // aggiunta dell'Hackathon creato alla lista hackathonCreati
        return hackathon;
    }

    // getter per la lista di giudici convocati
    public List<Giudice> getGiudiciConvocati() { return giudiciConvocati; }
    // metodo per convocare un giudice in un Hackathon (può farlo solo l'organizzatore che ha creato l'Hackathon stesso)
    public void convocaGiudice(Hackathon hackathon, Giudice giudice) {
        hackathon.verificaStatoGara("Iscrizioni non ancora aperte", "convocare il giudice");

        giudice.aggiungiHackathonAssegnato(hackathon, this); // aggiunta dell'Hackathon alla lista hackathonAssegnati del giudice
        giudice.aggiungiOrganizzatoriInvitanti(this); // aggiunta dell'organizzatore alla lista organizzatoriInvitanti del giudice
        hackathon.aggiungiGiudice(giudice, this); // aggiunta del giudice alla lista giudiceList dell'Hackathon
        giudiciConvocati.add(giudice); // aggiunta del giudice assegnato alla lista giudiciConvocati
    }
}