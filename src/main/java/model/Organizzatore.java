package model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Organizzatore extends UtentePiattaforma {
    // rappresentazione relazioni
    private final List<Hackathon> hackathonCreati = new ArrayList<>();
    private final List<Giudice> giudiciConvocati = new ArrayList<>();

    // Costruttore
    public Organizzatore(String nome, String cognome, String email) {
        super(nome, cognome, email); // chiama il costruttore padre
    }

    // getter per la lista di hackathon creati
    public List<Hackathon> getHackathonCreati() { return hackathonCreati; }
    // metodo per la creazione di un nuovo Hackathon da parte di un organizzatore
    public Hackathon creaHackathon(String titolo, int numMaxIscritti, int dimMaxTeam, Date inizioIscrizioni, Date fineIscrizioni, Date dataFine, String descrizioneProblema, String indirizzoSede) {
        // verifica che la data di fine iscrizioni venga dopo la data di inizio iscrizioni
        if (fineIscrizioni.before(inizioIscrizioni)) {
            throw new IllegalArgumentException("Errore: date non valide");
        }
        // verifica che la data di fine Hackathon venga almeno dopo due giorni dalla data di fine iscrizioni
        if (dataFine.before(new Date(fineIscrizioni.getTime() + 86400000 * 2))) {
            throw new IllegalArgumentException("Errore: date non valide");
        }
        Hackathon hackathon = new Hackathon(this, titolo, numMaxIscritti, dimMaxTeam, inizioIscrizioni, fineIscrizioni, dataFine, descrizioneProblema, indirizzoSede);  // Passa l'organizzatore come creatore
        hackathonCreati.add(hackathon);  // Aggiunge alla lista gestita dall'organizzatore
        return hackathon;
    }

    // getter per la lista di giudici convocati
    public List<Giudice> getGiudiciConvocati() { return giudiciConvocati; }
    // metodo per convocare un giudice in un Hackathon (può farlo solo l'organizzatore che ha creato l'Hackathon stesso)
    public void convocaGiudice(Giudice giudice, Hackathon hackathon) {
        giudice.aggiungiHackathonAssegnato(hackathon, this); // aggiunta dell'Hackathon alla lista hackathonAssegnati del giudice
        hackathon.aggiungiGiudice(giudice, this); // aggiunta del giudice alla lista giudiceList dell'Hackathon
        giudiciConvocati.add(giudice); // aggiunta del giudice assegnato alla lista giudiciConvocati
        System.out.println("Giudice " + giudice.getNome() + " convocato con successo.");
    }
}