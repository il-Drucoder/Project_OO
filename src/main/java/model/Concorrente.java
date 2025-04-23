package model;

import java.util.ArrayList;
import java.util.List;

public class Concorrente extends UtentePiattaforma {
    // rappresentazione relazioni
    private final List<Team> teamAppartenenza = new ArrayList<>();

    // metodi
    // Costruttore
    public Concorrente(String nome, String cognome, String email) {
        super(nome, cognome, email); // chiama il costruttore padre
    }

    // getter per la lista di team a cui appartiene il concorrente
    public List<Team> getTeamAppartenenza() { return teamAppartenenza; }

    // metodo per creare un nuovo team
    public Team creaTeam(String nome, String password, Hackathon hackathon) {
        // verifica se esiste già un team con lo stesso nome nello stesso Hackathon
        if (hackathon.esisteTeam(nome)) {
            throw new IllegalArgumentException("Esiste già un team con nome '" + nome + "' iscritto all'Hackathon: '" + hackathon.getTitolo()+ "'!");
        }

        // crea e aggiunge il team solo se il nome è disponibile
        Team team = new Team(nome, password, hackathon, this);
        hackathon.aggiungiTeam(team, this);

        // aggiunge il team ai teamGiudicati dei giudici appartenenti a quell'Hackathon
        for (Giudice giudice : hackathon.getGiudiceList()) {
            giudice.aggiungiTeam(team);
        }

        // aggiunge il team alla lista teamAppartenenza del concorrente
        teamAppartenenza.add(team);

        System.out.println("Team '" + nome + "' creato con successo!");
        return team;
    }

    // metodo per partecipare a un team già esistente
    public void partecipaTeam(String nomeTeam, String password, Hackathon hackathon) {
        // verifica che il periodo di iscrizione all'Hackathon non sia terminato
        if (!hackathon.iscrizioniTerminate()) {
            throw new IllegalStateException("Impossibile partecipare: periodo di iscrizione all'Hackathon terminato!");
        }
        // cerca il team nell'Hackathon
        Team teamTrovato = hackathon.getTeamPerNome(nomeTeam);
        if (teamTrovato == null) {
            throw new IllegalArgumentException("Team non trovato!");
        }
        // verifica la password
        if (!teamTrovato.verificaPassword(password)) {
            throw new SecurityException("Password errata!");
        }

        // aggiunto il concorrente al team
        teamTrovato.aggiungiMembro(this);
        // aggiunge il team alla lista teamAppartenenza del concorrente
        teamAppartenenza.add(teamTrovato);

        System.out.println(this.nome + " aggiunto al team " + nomeTeam);
    }
}
