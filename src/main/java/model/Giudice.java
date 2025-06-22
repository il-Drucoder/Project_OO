package model;

import java.util.ArrayList;
import java.util.List;

public class Giudice extends UtentePiattaforma {
    // rappresentazione relazioni
    private final List<Hackathon> hackathonAssegnati = new ArrayList<>(); // lista di Hackathon supervisionati dal giudice
    private final List<Voto> votiAsseganti = new ArrayList<>(); // lista di voti assegnati
    private final List<Documento> documentiValutati = new ArrayList<>(); // lista di documenti giudicabili
    private final List<Organizzatore> organizzatoriInvitanti = new ArrayList<>(); // lista di organizzatori da cui si è stati invitati
    private final List<Team> teamGiudicati = new ArrayList<>(); // lista dei team valutabili

    // metodi
    // Costruttore
    public Giudice(String nome, String cognome, String email, String password) {
        super(nome, cognome, email, password); // chiama il costruttore padre
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

    // getter per la lista di voti assegnati ai team
    public List<Voto> getVotiAsseganti() { return votiAsseganti; }
    // metodo per assegnare un voto a un team
    public void assegnaVoto(Team team, int valore) {
        // verica che il valore da assegnare come voto sia compreso tra 0 e 10
        if (valore < 0 || valore > 10) {
            throw new IllegalArgumentException("Voto non valido!");
        }

        // verifica che il giudice sia abilitato a valutare un team in un determinato Hackathon
        // verifica che team è in teamGiudicati e che team.getHackathon() è in hackathonGiudicati
        if(!(teamGiudicati.contains(team) || hackathonAssegnati.contains(team.getHackathon()))) {
            throw new SecurityException("Il giudice selezionato non ha il permesso di giudicare questo team!");
        }

        Voto voto = new Voto(this, team, valore);
        votiAsseganti.add(voto);
        team.aggiungiVoto(this, voto);
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
    // metodo per aggiungere un organizzatore alla lista OrganizzatoriInvitanti del giudice
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
}
