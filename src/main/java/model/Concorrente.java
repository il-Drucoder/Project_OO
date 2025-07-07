package model;

import java.util.ArrayList;
import java.util.List;

public class Concorrente extends UtentePiattaforma {
    // rappresentazione relazioni
    private final List<Team> teamsAppartenenza = new ArrayList<>();

    // metodi
    // Costruttore
    public Concorrente(String nome, String cognome, String email, String password) {
        super(nome, cognome, email, password, "concorrente"); // chiama il costruttore padre
    }

    // getter per la lista di team a cui appartiene il concorrente
    public List<Team> getListTeamAppartenenza() { return teamsAppartenenza; }
    // metodo utilizzato dal dumpDatiPartecipazioniAiTeam
    public void addTeamAppartenenza(Team team) {
        teamsAppartenenza.add(team);
    }

    // metodo per creare un nuovo team
    public Team creaTeam(String nome, String password, Hackathon hackathon) {
        hackathon.verificaStatoGara("Iscrizioni aperte", "creare un nuovo team");

        // verifica se esiste già un team con lo stesso nome nello stesso Hackathon
        if (hackathon.esisteTeam(nome)) {
            throw new IllegalArgumentException("Esiste già un team con nome '" + nome + "' iscritto all'Hackathon: '" + hackathon.getTitolo()+ "'!");
        }
        // verifica che il concorrente non partecipi già all'hackathon con un altro team
        String teamEsistente = isPartecipanteOfHackathon(hackathon); // variabile che individua il team, se esiste, a cui è già iscritto il concorrente
        if (teamEsistente != null) {
            throw new IllegalStateException("Impossibile creare un nuovo team: concorrente già iscritto al team '" + teamEsistente + "'");
        }

        Team team = new Team(nome, password, hackathon, this);
        hackathon.aggiungiTeam(team, this);
        // aggiunge il team alla lista teamAppartenenza del concorrente
        teamsAppartenenza.add(team);
        // aggiunge il concorrente al team
        team.aggiungiMembro(this);
        // incrementa di uno il numero di concorrenti partecipanti all'Hackathon
        hackathon.addIscritto(team);
        return team;
    }

    // metodo per partecipare a un team già esistente
    public void partecipaTeam(String nomeTeam, String password, Hackathon hackathon) {
        hackathon.verificaStatoGara("Iscrizioni aperte", "partecipare al team");

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
        teamsAppartenenza.add(teamTrovato);
        // incrementa di uno il numero di concorrenti partecipanti all'Hackathon
        hackathon.addIscritto(teamTrovato);
    }

    // metodo per accedere a un team già esistente (verifica che il concorrente appartenga al team)
    public void accediTeam(Team team) {
        for (Team teamC : teamsAppartenenza) {
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
