package model;

import java.util.ArrayList;
import java.util.List;

public class Giudice extends UtentePiattaforma {
    // rappresentazione relazioni
    private final List<Hackathon> hackathonAssegnati = new ArrayList<>();
    private final List<Documento> documentiValutati = new ArrayList<>();
    private final List<Organizzatore> organizzatoriInvitanti = new ArrayList<>();
    private final List<Team> teamGiudicati = new ArrayList<>();

    // metodi
    // Costruttore
    public Giudice(String nome, String cognome, String email) {
        super(nome, cognome, email); // chiama il costruttore padre
    }

    // getter per la lista di hackathon assegnati
    public List<Hackathon> getHackathonAssegnati() { return hackathonAssegnati; }
    // metodo per aggiungere un Hackathon alla lista hackathonAssegnati del giudice
    public void aggiungiHackathonAssegnato(Hackathon hackathon, Organizzatore organizzatore) {
        // verifica che l'Hackathon esista
        if (hackathon == null) {
            throw new IllegalArgumentException("Hackathon non valido!");
        }
        // verifica che l'organizzatore esista
        if (organizzatore == null) {
            throw new IllegalArgumentException("Organizzatore non valido!");
        }
        // verifica che l'Hackathon dove viene convocato il giudice sia stato creato dall'organizzatore che lo vuole inserire
        if (!organizzatore.getHackathonCreati().contains(hackathon)) {
            throw new SecurityException("Solo l'organizzatore creatore può convocare giudici!");
        }
        // verifica che il giudice non sia stato già convocato nell'Hackathon selezionato
        if (organizzatore.getGiudiciConvocati().contains(this) && this.getHackathonAssegnati().contains(hackathon)) {
            throw new SecurityException("Il giudice selezionato è già stato coinvolto in questo Hackathon!");
        }
        hackathonAssegnati.add(hackathon); // aggiunta del nuovo Hackathon alla lista hackathonAssegnati
    }

    // getter per la lista di documenti valutati
    public List<Documento> getDocumentiValutati() { return documentiValutati; }
    // metodo per aggiungere un documento alla lista DocumentiValutati del giudice
    public void aggiungiDocumento(Documento documento) {
        if (documento == null) {
            throw new IllegalArgumentException("Hackathon non valido!");
        }
        documentiValutati.add(documento);
    }

    // getter per la lista di organizzatori invitanti
    public List<Organizzatore> getOrganizzatoriInvitanti() { return organizzatoriInvitanti; }
    public void aggiungiOrganizzatoriInvitanti(Organizzatore organizzatore) {
        if (organizzatore == null) {
            throw new IllegalArgumentException("Organizzatore non valido!");
        }
        organizzatoriInvitanti.add(organizzatore);
    }

    // getter per la lista di team giudicati
    public List<Team> getTeamGiudicati() { return teamGiudicati; }
    // metodo per aggiungere un team alla lista teamGiudicati
    public void aggiungiTeam(Team team) {
        teamGiudicati.add(team);
    }

    public void assegnaVoto(Team team, int voto) {
        if (voto < 0 || voto > 10) {
            throw new IllegalArgumentException("Voto non valido!");
        }
        team.aggiungiVoto(voto);
    }
}
