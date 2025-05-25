package controller;

import model.*;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

public class Controller {

    private final List<UtentePiattaforma> UtentiPiattaforma;
    private final List<Hackathon> Hackathons;
    private final List<Concorrente> Concorrenti;
    private final List<Organizzatore> Organizzatori;
    private final List<Giudice> Giudici;
    private final List<Team> Teams;


    public Controller() {
        UtentiPiattaforma = new ArrayList<>();
        Hackathons = new ArrayList<>();
        Concorrenti = new ArrayList<>();
        Organizzatori = new ArrayList<>();
        Giudici = new ArrayList<>();
        Teams = new ArrayList<>();

        Organizzatore organizzatore1 = new Organizzatore("Luca", "Bianchi", "luca.bianchi@organizzatore.com");
        Organizzatore organizzatore2 = new Organizzatore("Marco", "Verdi", "francesco.verdi@organizzatore.com");

        Organizzatori.add(organizzatore1);
        UtentiPiattaforma.add(organizzatore1);
        Organizzatori.add(organizzatore2);
        UtentiPiattaforma.add(organizzatore2);

        Giudice giudice1 = new Giudice("Anna", "Rossi", "anna.rossi@giudice.com");

        Giudici.add(giudice1);
        UtentiPiattaforma.add(giudice1);

        Concorrente concorrente1 = new Concorrente("Maria", "Gialli", "maria.gialli@concorrente.com");
        Concorrente concorrente2 = new Concorrente("Fabrizio", "Neri", "fabrizio.neri@concorrente.com");
        Concorrente concorrente3 = new Concorrente("Federico", "Neri", "federico.neri@concorrente.com");

        Concorrenti.add(concorrente1);
        UtentiPiattaforma.add(concorrente1);
        Concorrenti.add(concorrente2);
        UtentiPiattaforma.add(concorrente2);
        Concorrenti.add(concorrente3);
        UtentiPiattaforma.add(concorrente3);

        // ------------------------------
        // 2. CREAZIONE HACKATHON (MEDIANTE SOLO ORGANIZZATORE)
        // ------------------------------
        System.out.println("\n=== CREAZIONE HACKATHON ===");
        Hackathon hackathon1 = organizzatore1.creaHackathon(
                "Offline First 2025",
                1000, // numero massimo iscritti
                5, // numero massimo concorrenti di un team
                new Date(), // data inizio iscrizioni (oggi)
                new Date(System.currentTimeMillis() + 86400000), // +1 giorno per le iscrizioni
                new Date(System.currentTimeMillis() + 86400000 * 6), // +6 giorni per la data fine
                "Progettare strumenti digitali che funzionino perfettamente senza internet: utili, giochi, o servizi che si sincronizzano solo quando serve.", // descrizione problema
                "Via Mezzocannone 10, Napoli" // inidirizzo sede svolgimento Hackathon
        );
        System.out.println("Hackathon creato: " + hackathon1.getTitolo());
        Hackathons.add(hackathon1);

        // ------------------------------
        // 4. REGISTRAZIONE TEAM (MEDIANTE SOLO CONCORRENTI)
        // ------------------------------
        System.out.println("\n=== REGISTRAZIONE TEAM ===");

        Team team1 = concorrente3.creaTeam("Forza Napoli!", "Maradona", hackathon1); // aggiunto all'Hackathon1
        concorrente2.partecipaTeam("Forza Napoli!", "Maradona", hackathon1); // aggiunta di un nuovo membro al team

        Team team2 = concorrente1.creaTeam("Terminator", "Schwarzenegger", hackathon1); // aggiunto all'Hackathon1

        Teams.add(team1);
        Teams.add(team2);
    }

    public Concorrente getConcorrenteByEmail(String email) {
        for (Concorrente concorrente : Concorrenti) {
            if (concorrente.getEmail().equals(email)) return concorrente;
        }
        return null;
    }

    public List<String> getNomeGiudici() {
        List<String> listNomeGiudici = new ArrayList<>();
        for (Giudice giudice : Giudici) listNomeGiudici.add(giudice.getNome());

        return listNomeGiudici;
    }

    public List<String> getNomeHackathon() {
        List<String> listNomeHackathon = new ArrayList<>();
        for (Hackathon hackathon : Hackathons) listNomeHackathon.add(hackathon.getTitolo());

        return listNomeHackathon;
    }

    public Hackathon getHackathonByName(String nome) {
        System.out.println(nome);
        for (Hackathon hackathon : Hackathons) {
            if (hackathon.getTitolo().equals(nome)) return hackathon;
        }
        return null;
    }

    public List<String> getNomeTeam() {
        List<String> listNomeTeam = new ArrayList<>();
        for (Team Team : Teams) listNomeTeam.add(Team.getNome());

        return listNomeTeam;
    }

    public Team getTeamByName(String nome) {
        for (Team team : Teams) {
            if (team.getNome().equals(nome)) return team;
        }
        return null;
    }

    public void addTeam(Team team) { Teams.add(team); }

    public void addConcorrente(Concorrente concorrente) { Concorrenti.add(concorrente); UtentiPiattaforma.add(concorrente);}

    public void addGiudice(Giudice giudice) { Giudici.add(giudice); UtentiPiattaforma.add(giudice);}

    public void addOrganizzatore(Organizzatore organizzatore) { Organizzatori.add(organizzatore); UtentiPiattaforma.add(organizzatore);}

    public Collection<String> getListaNomiTeamByHackathon(String titolo) {
        List<String> listNomeTeam = new ArrayList<>();
        for (Hackathon hackathon : Hackathons) {
            if (hackathon.getTitolo().equals(titolo)) {
                for (Team Team : hackathon.getTeamList()) listNomeTeam.add(Team.getNome());
            }
        }
        return listNomeTeam;
    }

    public Team getTeamByNameAndHackathon(String titolo, String nome) {
        for (Hackathon hackathon : Hackathons) {
            if (hackathon.getTitolo().equals(titolo)) {
                for (Team Team : hackathon.getTeamList()) {
                    if (Team.getNome().equals(nome)) return Team;
                }
            }
        }
        return null;
    }

    public Documento setFileInDocumenti(File file) {
        Documento doc = new Documento(file);
        return doc;
    }

    public void aggiungiDocumentoInTeam(Documento documento, Team team) {
        team.aggiungiDocumento(documento);
    }

    public List<String> getListaDocumentiPerTeam(Team nome){
        List<String> listaDocumentiPerTeam = new ArrayList<>();
        for (Team team : Teams) {
            if (team.equals(nome)) {
                for (Documento documento : team.getDocumenti())
                    listaDocumentiPerTeam.add(documento.getFile().getAbsolutePath());
            }
        }
        return listaDocumentiPerTeam;
    }

    public UtentePiattaforma getUtentePiattaformaByEmail(String email) {
        for (UtentePiattaforma utentePiattaforma : UtentiPiattaforma) {

            if (utentePiattaforma.getEmail().equals(email)) {
                return utentePiattaforma;
            }
        }
        return null;
    }
}
