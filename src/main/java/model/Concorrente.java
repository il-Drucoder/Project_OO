package model;

import java.util.ArrayList;
import java.util.List;

public class Concorrente extends UtentePiattaforma {
    // rappresentazione relazioni
    private final List<Team> teamAppartenenzaList = new ArrayList<>();

    // metodi
    // Costruttore
    public Concorrente(String nome, String cognome, String email, String password) {
        super(nome, cognome, email, password); // chiama il costruttore padre
    }

    // getter per la lista di team a cui appartiene il concorrente
    public List<Team> getListTeamAppartenenza() { return teamAppartenenzaList; }

    // metodo per creare un nuovo team
    public Team creaTeam(String nome, String password, Hackathon hackathon) {
        // verifica se il periodo di iscrizione è ancora attivo
        if (hackathon.iscrizioniTerminate()) {
            throw new IllegalStateException("Impossibile creare un nuovo team: periodo di iscrizioni all'Hackathon: '" + hackathon.getTitolo()+ "' terminato");
        }
        // verifica se l'Hackathon ha raggiunto il numero massimo di iscritti
        if (hackathon.isCompleto()) {
            throw new IllegalStateException("Impossibile creare un nuovo team: Hackathon: '" + hackathon.getTitolo()+ "' al completo");
        }
        // verifica se esiste già un team con lo stesso nome nello stesso Hackathon
        if (hackathon.esisteTeam(nome)) {
            throw new IllegalArgumentException("Esiste già un team con nome '" + nome + "' iscritto all'Hackathon: '" + hackathon.getTitolo()+ "'!");
        }
        // verifica che il concorrente non partecipi già all'hackathon con un altro team
        String teamEsistente = isPartecipanteOfHackathon(hackathon); // variabile che individua il team, se esiste, a cui è già iscritto il concorrente
        if (teamEsistente != null) {
            throw new IllegalStateException("Impossibile creare un nuovo team: concorrente già iscritto al team '" + teamEsistente + "'");
        }
        // crea e aggiunge il team solo se il nome è disponibile
        Team team = new Team(nome, password, hackathon, this);
        hackathon.aggiungiTeam(team, this);
        // aggiunge il team ai teamGiudicati dei giudici appartenenti a quell'Hackathon
        for (Giudice giudice : hackathon.getGiudiceList()) {
            giudice.aggiungiTeam(team);
        }
        // aggiunge il team alla lista teamAppartenenza del concorrente
        teamAppartenenzaList.add(team);
        // aggiunge il concorrente al team
        team.aggiungiMembro(this);
        // incrementa di uno il numero di concorrenti partecipanti all'Hackathon
        hackathon.addIscritto(team);
        return team;
    }

    // metodo per partecipare a un team già esistente
    public void partecipaTeam(String nomeTeam, String password, Hackathon hackathon) {
        // verifica se il periodo di iscrizione è ancora attivo
        if (hackathon.iscrizioniTerminate()) {
            throw new IllegalStateException("Impossibile creare un nuovo team: periodo di iscrizioni all'Hackathon: '" + hackathon.getTitolo()+ "' terminato");
        }
        // verifica se l'Hackathon ha raggiunto il numero massimo di iscritti
        if (hackathon.isCompleto()) {
            throw new IllegalStateException("Impossibile creare un nuovo team: Hackathon: '" + hackathon.getTitolo()+ "' al completo");
        }
        // verifica che il concorrente non partecipi già all'hackathon con un altro team
        String teamEsistente = isPartecipanteOfHackathon(hackathon); // variabile che individua il team, se esiste, a cui è già iscritto il concorrente
        if (teamEsistente != null) {
            throw new IllegalStateException("Impossibile partecipare: concorrente già iscritto al team '" + teamEsistente + "'");
        }
        // cerca il team nell'Hackathon
        Team teamTrovato = hackathon.getTeamByNome(nomeTeam);
        if (teamTrovato == null) {
            throw new IllegalArgumentException("Team non trovato!");
        }
        // verifica che il team non sia al completo
        if (teamTrovato.isCompleto()) {
            throw new IllegalStateException("Impossibile partecipare: team al completo!");
        }
        // verifica la password
        if (!teamTrovato.verificaPassword(password)) {
            throw new SecurityException("Impossibile partecipare: password errata!");
        }
        // aggiunto il concorrente al team
        teamTrovato.aggiungiMembro(this);
        // aggiunge il team alla lista teamAppartenenza del concorrente
        teamAppartenenzaList.add(teamTrovato);
        // incrementa di uno il numero di concorrenti partecipanti all'Hackathon
        hackathon.addIscritto(teamTrovato);
    }

    // metodo per accedere a un team già esistente (verifica che il concorrente appartenga al team)
    public void accediTeam(Team team) {
        for (Team teamC : teamAppartenenzaList) {
            if (team == teamC) {
                return;
            }
        }
        throw new IllegalArgumentException("Il concorrente non partecipa al team!");
    }

    // metodo per verificare che il concorrente non sia già iscritto a un determinato Hackathon attraverso un team
    public String isPartecipanteOfHackathon(Hackathon hackathon) {
        for (Team teamAppartenenza : getListTeamAppartenenza()) { // ricerca dei team a cui partecipa il concorrente
            if (teamAppartenenza.getHackathon().getTitolo().equals(hackathon.getTitolo())) {
                return teamAppartenenza.getNome();
            }
        }
        return null;
    }
}
