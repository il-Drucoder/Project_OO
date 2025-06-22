package controller;

import gui.BenvenutoConcorrente;
import gui.ClassificaHackathon;
import gui.PaginaTeam;
import model.*;

import javax.swing.*;
import java.io.File;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class Controller {

    private final List<Concorrente> Concorrenti;
    private final List<Documento> Documenti;
    private final List<Giudice> Giudici;
    private final List<Hackathon> Hackathons;
    private final List<Organizzatore> Organizzatori;
    private final List<Team> Teams;
    private final List<UtentePiattaforma> UtentiPiattaforma;
    private final List<Voto> Voti;

    /* ************************************************************************** */

    // Costruttore
    public Controller() {
        Concorrenti = new ArrayList<>();
        Documenti = new ArrayList<>();
        Giudici = new ArrayList<>();
        Hackathons = new ArrayList<>();
        Organizzatori = new ArrayList<>();
        Teams = new ArrayList<>();
        UtentiPiattaforma = new ArrayList<>();
        Voti = new ArrayList<>();

        Organizzatore organizzatore1 = new Organizzatore("Luca", "Bianchi", "luca.bianchi@organizzatore.com", "luccariello2!");
        Organizzatore organizzatore2 = new Organizzatore("Marco", "Verdi", "francesco.verdi@organizzatore.com", "verdi.7");

        Organizzatori.add(organizzatore1);
        UtentiPiattaforma.add(organizzatore1);
        Organizzatori.add(organizzatore2);
        UtentiPiattaforma.add(organizzatore2);

        Giudice giudice1 = new Giudice("Anna", "Rossi", "anna.rossi@giudice.com", "RossiPopi2");

        Giudici.add(giudice1);
        UtentiPiattaforma.add(giudice1);

        Concorrente concorrente1 = new Concorrente("Maria", "Gialli", "maria.gialli@concorrente.com", "Yellow3");
        Concorrente concorrente2 = new Concorrente("Fabrizio", "Neri", "fabrizio.neri@concorrente.com", "Sui88");
        Concorrente concorrente3 = new Concorrente("Federico", "Neri", "federico.neri@concorrente.com", "password123");

        Concorrenti.add(concorrente1);
        UtentiPiattaforma.add(concorrente1);
        Concorrenti.add(concorrente2);
        UtentiPiattaforma.add(concorrente2);
        Concorrenti.add(concorrente3);
        UtentiPiattaforma.add(concorrente3);

        // ------------------------------
        // 2. CREAZIONE DI 2 HACKATHON (MEDIANTE SOLO ORGANIZZATORE)
        // ------------------------------
        System.out.println("\n=== CREAZIONE HACKATHON ===");
        Hackathon hackathon1 = organizzatore1.creaHackathon(
                "Offline First 2025",
                1000, // numero massimo iscritti
                3, // numero massimo concorrenti di un team
                LocalDate.now(), // data inizio iscrizioni (oggi)
                LocalDate.now().plusDays(5), // +5 giorni per la data inizio
                LocalDate.now().plusDays(7), // +7 giorni per la data fine
                "Progettare strumenti digitali che funzionino perfettamente senza internet: utili, giochi, o servizi che si sincronizzano solo quando serve.", // descrizione problema
                "Via Mezzocannone 10, Napoli" // indirizzo sede svolgimento Hackathon
        );
        System.out.println("Hackathon creato: " + hackathon1.getTitolo());

        System.out.println("\n=== CREAZIONE HACKATHON ===");
        Hackathon hackathon2 = organizzatore1.creaHackathon(
                "Offline First 2024",
                1000, // numero massimo iscritti
                5, // numero massimo concorrenti di un team
                LocalDate.of(2024, 10, 25), // data inizio iscrizioni (25/10/2024)
                LocalDate.of(2024, 10, 29), // data inizio gara (29/10/2024)
                LocalDate.of(2024, 10, 31), // data fine gara (31/10/2024)
                "Progettare strumenti digitali che funzionino perfettamente senza internet: utili, giochi, o servizi che si sincronizzano solo quando serve.", // descrizione problema
                "Via Mezzocannone 10, Napoli" // indirizzo sede svolgimento Hackathon
        );
        System.out.println("Hackathon creato: " + hackathon2.getTitolo());

        Hackathons.add(hackathon1);
        Hackathons.add(hackathon2);

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

    /* ************************************************************************** */

    // metodi per la classe UtentePiattaforma

    public void addUtente(UtentePiattaforma utente) { UtentiPiattaforma.add(utente);}

    public UtentePiattaforma getUtentePiattaformaByEmail(String email) {
        for (UtentePiattaforma utentePiattaforma : UtentiPiattaforma) {
            if (utentePiattaforma.getEmail().equals(email)) return utentePiattaforma;
        }
        return null;
    }

    public void metodoSignup(JFrame frame, String nome, String cognome, String email, char[] pw) {
        if (!(nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || pw.length == 0)) {
            if (!existUtentePiattaforma(email)) {
                String pwString = new String(pw);
                if (email.toLowerCase().contains("@concorrente.com")) {
                    Concorrente concorrente = new Concorrente(nome, cognome, email, pwString);
                    addConcorrente(concorrente);
                    BenvenutoConcorrente BenvenutoConcorrenteGUI = new BenvenutoConcorrente(frame, email, this);
                    frame.setVisible(false);
                } else if (email.toLowerCase().contains("@giudice.com")) {
//                Giudice giudice = new Giudice(nome, cognome, email, pwString);
//                addGiudice(giudice);
//                BenvenutoGiudice BenvenutoGiudiceGUI = new BenvenutoGiudice(frame, email, this);
//                frame.setVisible(false);
                } else if (email.toLowerCase().contains("@organizzatore.com")) {
//                Organizzatore organizzatore = new Organizzatore(nome, cognome, email, pwString);
//                addOrganizzatore(organizzatore);
//                BenvenutoOrganizzatore BenvenutoOrganizzatoreGUI = new BenvenutoOrganizzatore(frame, email, this);
//                frame.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Indirizzo email non valido",
                            "Error page",
                            JOptionPane.ERROR_MESSAGE);
                    frame.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Indirizzo email già utilizzato",
                        "Error page",
                        JOptionPane.ERROR_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Signup non riuscito",
                    "Warning page",
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    public void metodoLogin(JFrame frame, String email, char[] pw) {
        if (!(email.isEmpty() || pw.length == 0)) {
            UtentePiattaforma utentePiattaforma = getUtentePiattaformaByEmail(email);
            if (utentePiattaforma != null) {
                String pwString = new String(pw);
                if (utentePiattaforma.verificaPassword(pwString)) {
                    if (email.toLowerCase().contains("@concorrente.com")) {
                        BenvenutoConcorrente BenvenutoConcorrenteGUI = new BenvenutoConcorrente(frame, email, this);
                        frame.setVisible(false);
                    } else if (email.toLowerCase().contains("@giudice.com")) {
//                    BenvenutoGiudice BenvenutoGiudiceGUI = new BenvenutoGiudice(frame, email, this);
//                    frame.setVisible(false);
                    } else { // l'utente è un organizzatore (@organizzatore.com)
//                    BenvenutoOrganizzatore BenvenutoOrganizzatoreGUI = new BenvenutoOrganizzatore(frame, email, this);
//                    frame.setVisible(false);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Password errata!",
                            "Error page",
                            JOptionPane.ERROR_MESSAGE);
                    frame.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Utente non registrato",
                        "Error page",
                        JOptionPane.ERROR_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Login non riuscito",
                    "Warning page",
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    public boolean existUtentePiattaforma(String email) {
        for(UtentePiattaforma utentePiattaforma : UtentiPiattaforma) {
            if (utentePiattaforma.getEmail().equals(email)) return true;
        }
        return false;
    }

    public void metodoVisualizzaClassifica(JFrame frame, String titoloHackathon, String emailUtente) {
        if(!(titoloHackathon.equals("Seleziona"))) {
            try {
                getHackathonByTitolo(titoloHackathon).getClassifica(getUtentePiattaformaByEmail(emailUtente));
                ClassificaHackathon ClassificaHackathonGUI = new ClassificaHackathon(frame, titoloHackathon, emailUtente, this);
                frame.setVisible(false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame,
                        e.getMessage(),
                        "Error page",
                        JOptionPane.ERROR_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Selezionare un Hackathon valido",
                    "Warning page",
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    /* ************************************************************************** */

    // metodi per la classe Organizzatore

    public void addOrganizzatore(Organizzatore organizzatore) { Organizzatori.add(organizzatore); addUtente(organizzatore);}

    /* ************************************************************************** */

    // metodi per la classe Giudice

    public void addGiudice(Giudice giudice) { Giudici.add(giudice); addUtente(giudice);}

    public List<String> getListaNomiGiudici() {
        List<String> listNomeGiudici = new ArrayList<>();
        for (Giudice giudice : Giudici) {
            listNomeGiudici.add(giudice.getNome());
        }
        return listNomeGiudici;
    }

    /* ************************************************************************** */

    // metodi per la classe Concorrente

    public void addConcorrente(Concorrente concorrente) { Concorrenti.add(concorrente); addUtente(concorrente);}

    public Concorrente getConcorrenteByEmail(String email) {
        for (Concorrente concorrente : Concorrenti) {
            if (concorrente.getEmail().equals(email)) return concorrente;
        }
        return null;
    }

    public void metodoCreaTeam(JFrame frame, String nomeTeam, char[] pwTeam, String titoloHackathon, String emailConcorrente) {
        String pwTeamString = new String(pwTeam);
        if (!(nomeTeam.isEmpty() || pwTeamString.isEmpty() || titoloHackathon.equals("Seleziona"))) {
            Concorrente concorrente = getConcorrenteByEmail(emailConcorrente);
            Hackathon hackathon = getHackathonByTitolo(titoloHackathon);
            try {
                Team team = concorrente.creaTeam(nomeTeam, pwTeamString, hackathon);
                addTeam(team);
                JOptionPane.showMessageDialog(frame,
                        "Creazione team avvenuta con successo",
                        "Message page",
                        JOptionPane.INFORMATION_MESSAGE);
                frame.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame,
                        e.getMessage(),
                        "Error page",
                        JOptionPane.ERROR_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Creazione team non riuscita",
                    "Warning page",
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    public void metodoPartecipaTeam(JFrame frame, String emailConcorrente, String titoloHackathon, String nomeTeam, char[] pwTeam) {
        String pwTeamString = new String(pwTeam);
        if (!(titoloHackathon.equals("Seleziona") || nomeTeam.equals("Seleziona") || pwTeamString.isEmpty())) { // verifica validità dei campi
            try {
                getConcorrenteByEmail(emailConcorrente).partecipaTeam(nomeTeam, pwTeamString, getHackathonByTitolo(titoloHackathon));
                PaginaTeam PaginaTeamGUI = new PaginaTeam(frame, nomeTeam, titoloHackathon, this);
                frame.setVisible(false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame,
                        e.getMessage(),
                        "Error page",
                        JOptionPane.ERROR_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Partecipazione al team non riuscita",
                    "Warning page",
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    public void metodoAccediTeam(JFrame frame, String titoloHackathon, String nomeTeam, String emailConcorrente) {
        if (!(titoloHackathon.equals("Seleziona") || nomeTeam.equals("Seleziona"))) {
            Concorrente concorrente = getConcorrenteByEmail(emailConcorrente);
            Team team = getTeamByNomeAndHackathon(nomeTeam, titoloHackathon);
            try {
                concorrente.accediTeam(team);
                PaginaTeam PaginaTeamGUI = new PaginaTeam(frame, nomeTeam, titoloHackathon, this);
                frame.setVisible(false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame,
                        e.getMessage(),
                        "Error page",
                        JOptionPane.ERROR_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Accesso al team non riuscito",
                    "Warning page",
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    public void metodoAggiungiDocumento(JFrame frame, File fileSelezionato, String nomeTeam, String titoloHackathon) {
        if (fileSelezionato != null) {
            try {
                getTeamByNomeAndHackathon(nomeTeam, titoloHackathon).aggiungiDocumento(setFileInDocumento(fileSelezionato));
                addDocumento(setFileInDocumento(fileSelezionato));
                JOptionPane.showMessageDialog(frame,
                        "File " + fileSelezionato.getName() + " aggiunto con successo",
                        "Message page",
                        JOptionPane.INFORMATION_MESSAGE);
                frame.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame,
                        e.getMessage(),
                        "Error page",
                        JOptionPane.ERROR_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Operazione inserimento file non riuscita",
                    "Warning page",
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(false);
        }
    }

    public void metodoVisualizzaDocumenti(JFrame frame, String selectedFile) {
        if (selectedFile != null && !selectedFile.equals("Seleziona")) {
            File file = new File(selectedFile); // Percorso del file
            if (file.exists()) {
                try {
                    // apertura file in base al sistema operativo
                    String os = System.getProperty("os.name").toLowerCase();
                    ProcessBuilder pb;
                    if (os.contains("win")) {
                        pb = new ProcessBuilder("cmd", "/c", "start", "\"\"", file.getAbsolutePath());
                    } else if (os.contains("mac")) {
                        pb = new ProcessBuilder("open", file.getAbsolutePath());
                    } else { // Linux/Unix
                        pb = new ProcessBuilder("xdg-open", file.getAbsolutePath());
                    }
                    pb.start();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame,
                            "Errore nell'aprire il file: " + e.getMessage(),
                            "Error page",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Il file selezionato non esiste: " + file.getAbsolutePath(),
                        "Error page",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Seleziona un documento valido",
                    "Warning page",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /* ************************************************************************** */

    // metodi per la classe Hackathon

    public void addHackathon(Hackathon hackathon) { Hackathons.add(hackathon);}

    public Hackathon getHackathonByTitolo(String nome) {
        for (Hackathon hackathon : Hackathons) {
            if (hackathon.getTitolo().equals(nome)) return hackathon;
        }
        return null;
    }

    public List<String> getListaTitoliHackathon() {
        List<String> listTitoloHackathon = new ArrayList<>();
        for (Hackathon hackathon : Hackathons) {
            listTitoloHackathon.add(hackathon.getTitolo());
        }
        return listTitoloHackathon;
    }

    public List<String> getListaTitoliHackathonInCorso() {
        List<String> listNomeHackathon = new ArrayList<>();
        for (Hackathon hackathon : Hackathons) {
            if(!hackathon.isTerminato()) {
                listNomeHackathon.add(hackathon.getTitolo());
            }
        }
        return listNomeHackathon;
    }

    public Collection<String> getListaNomiTeamByHackathon(String titolo) {
        List<String> listNomeTeam = new ArrayList<>();
        Hackathon hackathon = getHackathonByTitolo(titolo);
        for (Team team : hackathon.getTeamList()) {
            listNomeTeam.add(team.getNome());
        }
        return listNomeTeam;
    }

    public Collection<String> getListaNomiTeamLiberiByHackathon(String titolo) {
        List<String> listNomeTeam = new ArrayList<>();
        Hackathon hackathon = getHackathonByTitolo(titolo);
        for (Team team : hackathon.getTeamList()) {
            if(!team.isCompleto()) {
                listNomeTeam.add(team.getNome());
            }
        }
        return listNomeTeam;
    }

    // cerca se esiste un Team, a cui partecipa un determinato concorrente, iscritto a un determinato Hackathon
    public String getNomeTeamByHackathonAndConcorrente(String titolo, String emailConcorrente) {
        Hackathon hackathon = getHackathonByTitolo(titolo);
        Concorrente concorrente = getConcorrenteByEmail(emailConcorrente);
        return concorrente.isPartecipanteOfHackathon(hackathon);
    }

    /* ************************************************************************** */

    // metodi per la classe Team

    public void addTeam(Team team) { Teams.add(team); }

    public List<String> getListNomiTeam() {
        List<String> listNomeTeam = new ArrayList<>();
        for (Team Team : Teams) {
            listNomeTeam.add(Team.getNome());
        }
        return listNomeTeam;
    }

    public Team getTeamByNome(String nome) {
        for (Team team : Teams) {
            if (team.getNome().equals(nome)) return team;
        }
        return null;
    }

    public Team getTeamByNomeAndHackathon(String nome, String titolo) {
        for (Hackathon hackathon : Hackathons) {
            if (hackathon.getTitolo().equals(titolo)) {
                for (Team Team : hackathon.getTeamList()) {
                    if (Team.getNome().equals(nome)) {
                        return Team;
                    }
                }
            }
        }
        return null;
    }

    public List<String> getListaMembriByTeam(Team team) {
        List<String> listMembri = new ArrayList<>();
        for (Concorrente membro : team.getMembri()) {
            listMembri.add(membro.getNome());
        }
        return listMembri;
    }

    public List<String> getListaDocumentiByTeam(Team nome){
        List<String> listDocumento = new ArrayList<>();
        for (Team team : Teams) {
            if (team.equals(nome)) {
                for (Documento documento : team.getDocumenti())
                    listDocumento.add(documento.getFile().getAbsolutePath());
            }
        }
        return listDocumento;
    }

    /* ************************************************************************** */

    // metodi per la classe Documento

    public void addDocumento(Documento documento) { Documenti.add(documento);}

    public Documento setFileInDocumento(File file) {
        return new Documento(file);
    }

    /* ************************************************************************** */

    // metodi per la classe Voto

    public void addVoto(Voto voto) { Voti.add(voto);}

    /* ************************************************************************** */
}
