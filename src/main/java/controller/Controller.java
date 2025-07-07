package controller;

import dao.*;
import database.ConnessioneDatabase;
import gui.*;
import implementazione_postgres_dao.*;
import model.*;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class Controller {
    private static final String EPDEFAULT = "Error page";
    private static final String WPDEFAULT = "Warning page";
    private static final String MPDEFAULT = "Message page";
    private static final String SDEFAULT = "Seleziona";

    private final List<Concorrente> concorrenti;
    private final List<Documento> documenti;
    private final List<Giudice> giudici;
    private final List<Hackathon> hackathons;
    private final List<Organizzatore> organizzatori;
    private final List<Team> teams;
    private final List<UtentePiattaforma> utentiPiattaforma;
    private final List<Voto> voti;

    // collegamento Controller al DAO
    private final UtentePiattaformaDAO utenteDAO;
    private final HackathonDAO hackathonDAO;
    private final TeamDAO teamDAO;
    private final DocumentoDAO documentoDAO;
    private final VotoDAO votoDAO;

    /* ************************************************************************** */    
    // Costruttore
    public Controller() throws SQLException {
        concorrenti = new ArrayList<>();
        documenti = new ArrayList<>();
        giudici = new ArrayList<>();
        hackathons = new ArrayList<>();
        organizzatori = new ArrayList<>();
        teams = new ArrayList<>();
        utentiPiattaforma = new ArrayList<>();
        voti = new ArrayList<>();

        Connection connessione = ConnessioneDatabase.getInstance().getConnection();
        this.utenteDAO = new UtentePiattaformaImplementazionePostgresDAO(connessione);
        this.hackathonDAO = new HackathonImplementazionePostgresDAO(connessione);
        this.teamDAO = new TeamImplementazionePostgresDAO(connessione);
        this.documentoDAO = new DocumentoImplementazionePostgresDAO(connessione);
        this.votoDAO = new VotoImplementazionePostgresDAO(connessione);

        // preleva dati dal DB
        uploadAllList();
    }

    /* ************************************************************************** */

    // metodi per la connessione al DataBase

    private void uploadAllList() {
        dumpDatiUtente();
        dumpDatiHackathon();
        dumpDatiTeam();
        dumpDatiDocumento();
        dumpDatiVoto();
        dumpDatiConvocazioni();
        dumpDatiPartecipazioniAiTeam();
        dumpDatiVotiAssegnati();
    }

    private void dumpDatiUtente() {
        utentiPiattaforma.addAll(utenteDAO.getTuttiUtenti());
        for (UtentePiattaforma utente : utentiPiattaforma) {
            if (isOrganizzatore(utente.getEmail())) {
                organizzatori.add(new Organizzatore(utente.getNome(), utente.getCognome(), utente.getEmail(), utente.getPw()));
            } else if (isGiudice(utente.getEmail())) {
                giudici.add(new Giudice(utente.getNome(), utente.getCognome(), utente.getEmail(), utente.getPw()));
            } else {
                concorrenti.add(new Concorrente(utente.getNome(), utente.getCognome(), utente.getEmail(), utente.getPw()));
            }
        }
    }

    private void dumpDatiHackathon() {
        hackathons.addAll(hackathonDAO.getTuttiHackathon());
        // aggiunta degli Hackathon alla lista degli Hackathon creati degli organizzatori
        for (Hackathon hackathon : hackathons) {
            Organizzatore organizzatore = getOrganizzatoreByEmail(hackathon.getCreatore().getEmail());
            organizzatore.addHackathonCreati(hackathon);
        }
    }

    private void dumpDatiTeam() {
        teams.addAll(teamDAO.getTuttiTeam());
        // aggiunta dei Team alla lista di team degli Hackathon
        for (Team team : teams) {
            Hackathon hackathon = getHackathonByTitolo(team.getHackathon().getTitolo());
            hackathon.addTeam(team);
        }
    }

    private void dumpDatiDocumento() {
        documenti.addAll(documentoDAO.getTuttiDocumenti());
        // aggiunta dei Documenti alla lista di documenti dei team
        for (Documento documento : documenti) {
            for (Team team : teams) {
                if (team.getNome().equals(documento.getTeam().getNome()) && team.getHackathon().getTitolo().equals(documento.getTeam().getHackathon().getTitolo())) {
                    team.addDocumenti(documento);
                    break;
                }
            }
        }
        // aggiunta dei Commenti alla lista di commenti dei documenti
        for (Documento documento : documenti) {
            documento.addTuttiCommenti(documentoDAO.getTuttiCommentiByDocumento(documento));
        }
    }

    private void dumpDatiVoto() {
        voti.addAll(votoDAO.getTuttiVoti());
        for (Voto voto : voti) {
            // aggiunta dei Voti alla lista di voti assegnati dei giudici
            Giudice giudice = getGiudiceByEmail(voto.getGiudice().getEmail());
            giudice.addVotiAssegnati(voto);
            // aggiunta dei Voti alla lista di voti dei team
            Team team = getTeamByNomeAndHackathon(voto.getTeam().getNome(), voto.getTeam().getHackathon().getTitolo());
            team.addVoti(voto);
        }
    }

    private void dumpDatiConvocazioni() {
        for (Giudice giudice : giudici) {
            // aggiunta degli Hackathon nella lista degli Hackathon assegnati dei giudici
            for (Hackathon hackathonG : utenteDAO.getHackathonAssegnatiToGiudice(giudice)) {
                Hackathon hackathon =  getHackathonByTitolo(hackathonG.getTitolo());
                giudice.addHackathonAssegnati(hackathon);
            }
            // aggiunta dei Giudici alla lista dei giudici degli Hackathon
            for (Hackathon hackathon : giudice.getHackathonAssegnati()) {
                hackathon.addGiudice(giudice);
            }
            // aggiunta degli Organizzatori nella lista degli organizzatori invitanti dei giudici
            for (Organizzatore organizzatoreG : utenteDAO.getOrganizzatoriInvitantiToGiudice(giudice)) {
                Organizzatore organizzatore = getOrganizzatoreByEmail(organizzatoreG.getEmail());
                giudice.addOrganizzatoriInvitanti(organizzatore);
            }
            // aggiunta dei Giudici alla lista dei giudici convocati degli organizzatori
            for (Organizzatore organizzatore : giudice.getOrganizzatoriInvitanti()) {
                organizzatore.getGiudiciConvocati().add(giudice);
            }
        }
    }

    private void dumpDatiPartecipazioniAiTeam() {
        for (Team team : teams) {
            // aggiunta dei Concorrenti alla lista dei membri dei team
            for (Concorrente concorrenteT : teamDAO.getConcorrentiOfTeam(team)) {
                Concorrente concorrente = getConcorrenteByEmail(concorrenteT.getEmail());
                team.addMembri(concorrente);
            }
            for (Concorrente concorrente : team.getMembri()) {
                // aggiunta dei Team alla lista dei team dei concorrenti
                concorrente.addTeamAppartenenza(team);
            }
        }
    }

    private void dumpDatiVotiAssegnati() {
        for (Giudice giudice : giudici) {
            for (Hackathon hackathon : giudice.getHackathonAssegnati()) {
                for (Team team : hackathon.getTeamList()) {
                    // aggiunta dei Team alla lista dei team giudicati del giudice
                    addTeamToListaTeamGiudicatiOfGiudice(team, giudice);
                    if (!giudice.getTeamGiudicati().contains(team)) {
                        // aggiunta dei Team alla lista dei team giudicabili del giudice
                        giudice.addTeamGiudicabili(team);
                    }
                }
            }
        }
    }

    private void addTeamToListaTeamGiudicatiOfGiudice(Team team, Giudice giudice) {
        for (Voto voto : team.getVoti()) {
            if (voto.getGiudice().getEmail().equals(giudice.getEmail())) {
                // aggiunta dei Team alla lista dei team giudicati del giudice
                giudice.addTeamGiudicati(team);
            }
        }
    }

    /* ************************************************************************** */

    // metodi per la classe UtentePiattaforma

    public void addUtente(UtentePiattaforma utente) {
        utentiPiattaforma.add(utente);
        // inserisci dati nel DB
        utenteDAO.aggiungiUtente(utente);
    }

    public List<UtentePiattaforma> getUtentiPiattaformaList() {
        return utentiPiattaforma;
    }

    public UtentePiattaforma getUtentePiattaformaByEmail(String email) {
        for (UtentePiattaforma utentePiattaforma : getUtentiPiattaformaList()) {
            if (utentePiattaforma.getEmail().equalsIgnoreCase(email)) {
                return utentePiattaforma;
            }
        }
        return null;
    }

    // metodo per la pagina Signup
    public void metodoSignup(JFrame frame, String nome, String cognome, String email, char[] pw) {
        if (!(nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || pw.length == 0)) {
            if (!existUtentePiattaforma(email)) {
                String pwString = new String(pw);
                if (isConcorrente(email)) {
                    Concorrente concorrente = new Concorrente(nome, cognome, email, pwString);
                    addConcorrente(concorrente);
                    new BenvenutoConcorrente(frame, email, this);
                    frame.setVisible(false);
                } else if (isGiudice(email)) {
                    Giudice giudice = new Giudice(nome, cognome, email, pwString);
                    addGiudice(giudice);
                    new BenvenutoGiudice(frame, email, this);
                    frame.setVisible(false);
                } else if (isOrganizzatore(email)) {
                    Organizzatore organizzatore = new Organizzatore(nome, cognome, email, pwString);
                    addOrganizzatore(organizzatore);
                    new BenvenutoOrganizzatore(frame, email, this);
                    frame.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Indirizzo email non valido",
                            EPDEFAULT,
                            JOptionPane.ERROR_MESSAGE);
                    frame.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Indirizzo email già utilizzato",
                        EPDEFAULT,
                        JOptionPane.ERROR_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Signup non riuscito",
                    WPDEFAULT,
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    // metodo per la pagina Login
    public void metodoLogin(JFrame frame, String email, char[] pw) {
        if (!(email.isEmpty() || pw.length == 0)) {
            UtentePiattaforma utentePiattaforma = getUtentePiattaformaByEmail(email);
            if (utentePiattaforma != null) {
                String pwString = new String(pw);
                if (utentePiattaforma.verificaPassword(pwString)) {
                    riconoscimentoTipoUtente(frame, utentePiattaforma.getEmail());
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Password errata!",
                            EPDEFAULT,
                            JOptionPane.ERROR_MESSAGE);
                    frame.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Utente non registrato",
                        EPDEFAULT,
                        JOptionPane.ERROR_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Login non riuscito",
                    WPDEFAULT,
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    // metodo usato dal metodoLogin
    private void riconoscimentoTipoUtente(JFrame frame, String email) {
        if (isConcorrente(email)) {
            new BenvenutoConcorrente(frame, email, this);
            frame.setVisible(false);
        } else if (isGiudice(email)) {
            new BenvenutoGiudice(frame, email, this);
            frame.setVisible(false);
        } else { // l'utente è un organizzatore (@organizzatore.com)
            new BenvenutoOrganizzatore(frame, email, this);
            frame.setVisible(false);
        }
    }

    public boolean existUtentePiattaforma(String email) {
        for (UtentePiattaforma utentePiattaforma : getUtentiPiattaformaList()) {
            if (utentePiattaforma.getEmail().equalsIgnoreCase(email)) return true;
        }
        return false;
    }

    public boolean isOrganizzatore(String email) {
        return email.toLowerCase().endsWith("@organizzatore.com");
    }

    public boolean isGiudice(String email) {
        return email.toLowerCase().endsWith("@giudice.com");
    }

    public boolean isConcorrente(String email) {
        return email.toLowerCase().endsWith("@concorrente.com");
    }

    // metodo per la pagina VisualizzaClassifica
    public void metodoVisualizzaClassifica(JFrame frame, String titoloHackathon, String emailUtente) {
        if (existUtentePiattaforma(emailUtente)) {
            try {
                List<String> classifica = getHackathonByTitolo(titoloHackathon).getClassifica(getUtentePiattaformaByEmail(emailUtente));
                if (!classifica.isEmpty()) {
                    hackathonDAO.setClassificaHackathon(getHackathonByTitolo(titoloHackathon), Arrays.toString(classifica.toArray(new String[0])));
                }
                new ClassificaHackathon(frame, classifica, titoloHackathon);
                frame.setVisible(false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame,
                        e.getMessage(),
                        EPDEFAULT,
                        JOptionPane.ERROR_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Utente non valido",
                    WPDEFAULT,
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    /* ************************************************************************** */

    // metodi per la classe Organizzatore

    public void addOrganizzatore(Organizzatore organizzatore) {
        organizzatori.add(organizzatore);
        addUtente(organizzatore);
    }

    public List<Organizzatore> getOrganizzatoriList() {
        return organizzatori;
    }

    public Organizzatore getOrganizzatoreByEmail(String email) {
        for (Organizzatore organizzatore : getOrganizzatoriList()) {
            if (organizzatore.getEmail().equalsIgnoreCase(email)) {
                return organizzatore;
            }
        }
        return null;
    }

    public List<String> getListaTitoliHackathonCreatiByCreatore(String email) {
        Organizzatore creatore = getOrganizzatoreByEmail(email);
        List<String> listNomeHackathon = new ArrayList<>();
        for (Hackathon hackathon : creatore.getHackathonCreati()) {
            listNomeHackathon.add(hackathon.getTitolo());
        }
        return listNomeHackathon;
    }

    public List<String> getListaTitoliHackathonAnnunciatiByCreatore(String email) {
        Organizzatore creatore = getOrganizzatoreByEmail(email);
        List<String> listNomeHackathon = new ArrayList<>();
        for (Hackathon hackathon : creatore.getHackathonCreati()) {
            if (hackathon.verificaStatoGara("Iscrizioni non ancora aperte")) {
                listNomeHackathon.add(hackathon.getTitolo());
            }
        }
        return listNomeHackathon;
    }

    // metodo per la pagina CreaHackathon
    public void metodoCreaHackathon(JFrame frame, String titolo, LocalDate dataInizio, LocalDate dataFine, int numMaxIscritti, int dimMaxTeam, LocalDate inizioIscrizioni, String indirizzoSede, String emailOrganizzatore) {
        if (!(titolo.isEmpty() || dataInizio == null || dataFine == null || inizioIscrizioni == null || indirizzoSede.isEmpty())) { // verifica validità dei campi
            if (numMaxIscritti > 1 && dimMaxTeam > 0 && !(dataInizio.isBefore(LocalDate.now()) || dataFine.isBefore(LocalDate.now()) || inizioIscrizioni.isBefore(LocalDate.now().plusDays(1)))) { // verifica correttezza dei campi
                try {
                    Organizzatore organizzatore = getOrganizzatoreByEmail(emailOrganizzatore);
                    // verifica se esiste già un Hackathon con lo stesso titolo
                    if (existHackathon(titolo)) {
                        throw new IllegalArgumentException("Esiste già un Hackathon intitolato '" + titolo + "'!");
                    }
                    Hackathon hackathon = organizzatore.creaHackathon(titolo, numMaxIscritti, dimMaxTeam, inizioIscrizioni, dataInizio, dataFine, indirizzoSede);
                    addHackathon(hackathon);
                    JOptionPane.showMessageDialog(frame,
                            "Creazione Hackathon: '" + titolo + "' avvenuta con successo",
                            MPDEFAULT,
                            JOptionPane.INFORMATION_MESSAGE);
                    frame.setVisible(true);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame,
                            e.getMessage(),
                            EPDEFAULT,
                            JOptionPane.ERROR_MESSAGE);
                    frame.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Creazione Hackathon non riuscita. Valori inseriti non coerenti",
                        WPDEFAULT,
                        JOptionPane.WARNING_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Creazione Hackathon non riuscita",
                    WPDEFAULT,
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    // metodo per la pagina ConvocaGiudice
    public void metodoConvocaGiudice(JFrame frame, String titoloHackathon, String emailGiudice, String emailOrganizzatore) {
        if (!(titoloHackathon.equals(SDEFAULT) || emailGiudice.isEmpty())) { // verifica validità dei campi
            try {
                Organizzatore organizzatore = getOrganizzatoreByEmail(emailOrganizzatore);
                Giudice giudice = getGiudiceByEmail(emailGiudice);
                Hackathon hackathon = getHackathonByTitolo(titoloHackathon);
                organizzatore.convocaGiudice(hackathon, giudice);
                utenteDAO.convocaGiudice(organizzatore, giudice, hackathon);
                JOptionPane.showMessageDialog(frame,
                        "Convocazione giudice avvenuta con successo",
                        MPDEFAULT,
                        JOptionPane.INFORMATION_MESSAGE);
                frame.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame,
                        e.getMessage(),
                        EPDEFAULT,
                        JOptionPane.ERROR_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Convocazione giudice non riuscita",
                    WPDEFAULT,
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    /* ************************************************************************** */

    // metodi per la classe Giudice

    public void addGiudice(Giudice giudice) {
        giudici.add(giudice);
        addUtente(giudice);
    }

    public List<Giudice> getGiudiciList() {
        return giudici;
    }

    public Giudice getGiudiceByEmail(String email) {
        for (Giudice giudice : getGiudiciList()) {
            if (giudice.getEmail().equalsIgnoreCase(email)) {
                return giudice;
            }
        }
        return null;
    }

    public List<String> getListaNominativiGiudici() {
        List<String> listNomeGiudici = new ArrayList<>();
        for (Giudice giudice : getGiudiciList()) {
            listNomeGiudici.add(giudice.getNome() + " " + giudice.getCognome());
        }
        return listNomeGiudici;
    }

    public List<String> getListaEmailGiudici() {
        List<String> listEmailGiudici = new ArrayList<>();
        for (Giudice giudice : getGiudiciList()) {
            listEmailGiudici.add(giudice.getEmail());
        }
        return listEmailGiudici;
    }

    public List<String> getListaTitoliHackathonAssegnatiToGiudice(String email) {
        Giudice giudice = getGiudiceByEmail(email);
        List<String> listNomeHackathon = new ArrayList<>();
        for (Hackathon hackathon : giudice.getHackathonAssegnati()) {
            listNomeHackathon.add(hackathon.getTitolo());
        }
        return listNomeHackathon;
    }

    // metodo per la pagina PaginaHackathon, funzione inserimento descrizione problema
    public void metodoSetDescrizioneProblemaHackathon(JFrame frame, String descrizioneProblema, String titoloHackathon, String emailUtente){
        if (descrizioneProblema != null) { // verifica validità dei campi
            Giudice giudice = getGiudiceByEmail(emailUtente);
            Hackathon hackathon = getHackathonByTitolo(titoloHackathon);
            if (giudice != null && hackathon != null) {
                try {
                    hackathon.setDescrizioneProblema(giudice, descrizioneProblema);
                    hackathonDAO.setDescrizioneHackathon(getHackathonByTitolo(titoloHackathon), descrizioneProblema);
                    JOptionPane.showMessageDialog(frame,
                            "Descrizione problema inserita con successo",
                            MPDEFAULT,
                            JOptionPane.INFORMATION_MESSAGE);
                    frame.setVisible(true);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame,
                            e.getMessage(),
                            EPDEFAULT,
                            JOptionPane.ERROR_MESSAGE);
                    frame.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Giudice e/o Hackathon non trovato/i",
                        WPDEFAULT,
                        JOptionPane.WARNING_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Nessuna descrizione inserita, operazione di aggiunta non riuscita",
                    WPDEFAULT,
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    // metodo per la pagina VisionaTeam e GiudicaTeam
    public void metodoVisionaAndOrGiudicaTeam(JFrame frame, String titoloHackathon, String nomeTeam, String emailGiudice) {
        if (!(titoloHackathon.equals(SDEFAULT) || nomeTeam.equals(SDEFAULT))) { // verifica validità dei campi
            try {
                new PaginaTeam(frame, nomeTeam, titoloHackathon, emailGiudice, this);
                frame.setVisible(false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame,
                        e.getMessage(),
                        EPDEFAULT,
                        JOptionPane.ERROR_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Hackathon e/o giudice non selezionato/i. Operazione di visione/giudizio team non riuscita",
                    WPDEFAULT,
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    // metodo per la pagina PaginaTeam, funzione assegnazione voto dal giudice al team
    public void metodoAssegnaVoto(JFrame frame, String emailGiudice, int voto, String nomeTeam, String titoloHackathon) {
        if (voto >= 0 && voto <= 10) {
            Giudice giudice =  getGiudiceByEmail(emailGiudice);
            Team team = getTeamByNomeAndHackathon(nomeTeam, titoloHackathon);
            if (giudice != null && team != null) {
                try {
                    giudice.assegnaVoto(team, voto);
                    Voto v = new Voto(giudice, team, voto);
                    addVoto(v);
                    JOptionPane.showMessageDialog(frame,
                            "Votazione: " + voto + " (" + v + ") inserita con successo",
                            MPDEFAULT,
                            JOptionPane.INFORMATION_MESSAGE);
                    frame.setVisible(true);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame,
                            e.getMessage(),
                            EPDEFAULT,
                            JOptionPane.ERROR_MESSAGE);
                    frame.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Giudice e/o team non trovato/i",
                        WPDEFAULT,
                        JOptionPane.WARNING_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Operazione assegnazione voto non riuscita",
                    WPDEFAULT,
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    // metodo per la pagina PaginaDocumento, funzione aggiunta commento dal giudice al documento del team
    public void metodoAggiungiCommento(JFrame frame, String emailUtente, String commento, Boolean firma, String fileSelezionato, String nomeTeam, String titoloHackathon) {
        if (!commento.isEmpty()) {
            Documento documento = getDocumentoByTeam(fileSelezionato,  nomeTeam, titoloHackathon);
            Team team = getTeamByNomeAndHackathon(nomeTeam, titoloHackathon);
            Hackathon hackathon = getHackathonByTitolo(titoloHackathon);
            Giudice giudice = getGiudiceByEmail(emailUtente);
            if (documento != null && team != null && hackathon != null && giudice != null) {
                try {
                    if (Boolean.TRUE.equals(firma)) {
                        commento += "\n(" + giudice.getNome() + " " + giudice.getCognome() + ")";
                    }
                    documento.setCommenti(giudice, commento);
                    documentoDAO.addCommento(giudice, documento, commento);
                    JOptionPane.showMessageDialog(frame,
                            "Commento inserito con successo",
                            MPDEFAULT,
                            JOptionPane.INFORMATION_MESSAGE);
                    frame.setVisible(true);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame,
                            e.getMessage(),
                            EPDEFAULT,
                            JOptionPane.ERROR_MESSAGE);
                    frame.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Documento/team/giudice/Hackathon non trovato/i",
                        WPDEFAULT,
                        JOptionPane.WARNING_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Nessun commento inserito, operazione di aggiunta non riuscita",
                    WPDEFAULT,
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    /* ************************************************************************** */

    // metodi per la classe Concorrente

    public void addConcorrente(Concorrente concorrente) {
        concorrenti.add(concorrente);
        addUtente(concorrente);
    }

    public List<Concorrente> getConcorrentiList() {
        return concorrenti;
    }

    public Concorrente getConcorrenteByEmail(String email) {
        for (Concorrente concorrente : getConcorrentiList()) {
            if (concorrente.getEmail().equalsIgnoreCase(email)) {
                return concorrente;
            }
        }
        return null;
    }

    // metodo per la pagina CreaTeam
    public void metodoCreaTeam(JFrame frame,String titoloHackathon, String nomeTeam, char[] pwTeam, String emailConcorrente) {
        String pwTeamString = new String(pwTeam);
        if (!(titoloHackathon.equals(SDEFAULT) || nomeTeam.isEmpty() || pwTeamString.isEmpty())) {
            Concorrente concorrente = getConcorrenteByEmail(emailConcorrente);
            Hackathon hackathon = getHackathonByTitolo(titoloHackathon);
            if (concorrente != null && hackathon != null) {
                try {
                    Team team = concorrente.creaTeam(nomeTeam, pwTeamString, hackathon);
                    addTeam(team);
                    utenteDAO.partecipaTeam(concorrente, nomeTeam, titoloHackathon);
                    // incremento dei team nell'Hackathon
                    hackathonDAO.incrementaTeam(titoloHackathon);
                    JOptionPane.showMessageDialog(frame,
                            "Creazione team avvenuta con successo",
                            MPDEFAULT,
                            JOptionPane.INFORMATION_MESSAGE);
                    new PaginaTeam(frame, nomeTeam, titoloHackathon, emailConcorrente, this);
                    frame.setVisible(false);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame,
                            e.getMessage(),
                            EPDEFAULT,
                            JOptionPane.ERROR_MESSAGE);
                    frame.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Concorrente e/o Hackathon non trovato/i",
                        WPDEFAULT,
                        JOptionPane.WARNING_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Creazione team non riuscita",
                    WPDEFAULT,
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    // metodo per la pagina PartecipaTeam
    public void metodoPartecipaTeam(JFrame frame, String titoloHackathon, String nomeTeam, char[] pwTeam, String emailConcorrente) {
        String pwTeamString = new String(pwTeam);
        if (!(titoloHackathon.equals(SDEFAULT) || nomeTeam.equals(SDEFAULT) || pwTeamString.isEmpty())) { // verifica validità dei campi
            Concorrente concorrente = getConcorrenteByEmail(emailConcorrente);
            if (concorrente != null) {
                try {
                    concorrente.partecipaTeam(nomeTeam, pwTeamString, getHackathonByTitolo(titoloHackathon));
                    utenteDAO.partecipaTeam(concorrente, nomeTeam, titoloHackathon);
                    // incremento dei team nell'Hackathon
                    hackathonDAO.incrementaTeam(titoloHackathon);
                    new PaginaTeam(frame, nomeTeam, titoloHackathon, emailConcorrente, this);
                    frame.setVisible(false);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame,
                            e.getMessage(),
                            EPDEFAULT,
                            JOptionPane.ERROR_MESSAGE);
                    frame.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Concorrente non trovato",
                        WPDEFAULT,
                        JOptionPane.WARNING_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Partecipazione al team non riuscita",
                    WPDEFAULT,
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    // metodo per la pagina AccediTeam
    public void metodoAccediTeam(JFrame frame, String titoloHackathon, String nomeTeam, String emailConcorrente) {
        if (!(titoloHackathon.equals(SDEFAULT) || nomeTeam.equals(SDEFAULT))) { // verifica validità dei campi
            Concorrente concorrente = getConcorrenteByEmail(emailConcorrente);
            Team team = getTeamByNomeAndHackathon(nomeTeam, titoloHackathon);
            if (concorrente != null && team != null) {
                try {
                    concorrente.accediTeam(team);
                    new PaginaTeam(frame, nomeTeam, titoloHackathon, emailConcorrente, this);
                    frame.setVisible(false);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame,
                            e.getMessage(),
                            EPDEFAULT,
                            JOptionPane.ERROR_MESSAGE);
                    frame.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Concorrente e/o team non trovato/i",
                        WPDEFAULT,
                        JOptionPane.WARNING_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Accesso al team non riuscito",
                    WPDEFAULT,
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    // metodo per la pagina AggiungiDocumento
    public void metodoAggiungiDocumento(JFrame frame, String fileSelezionato, String nomeTeam, String titoloHackathon) {
        if (fileSelezionato != null) { // verifica validità dei campi
            Team team = getTeamByNomeAndHackathon(nomeTeam, titoloHackathon);
            if (team != null) {
                try {
                    team.aggiungiDocumento(setFileInDocumento(fileSelezionato, team));
                    addDocumento(setFileInDocumento(fileSelezionato, team));
                    JOptionPane.showMessageDialog(frame,
                            "File " + fileSelezionato + " aggiunto con successo",
                            MPDEFAULT,
                            JOptionPane.INFORMATION_MESSAGE);
                    frame.setVisible(true);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame,
                            e.getMessage(),
                            EPDEFAULT,
                            JOptionPane.ERROR_MESSAGE);
                    frame.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Team non trovato",
                        WPDEFAULT,
                        JOptionPane.WARNING_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Nessun file selezionato, operazione di inserimento non riuscita",
                    WPDEFAULT,
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(false);
        }
    }

    // metodo per la pagina VisualizzaDocumenti
    public void metodoVisualizzaDocumenti(JFrame frame, String percorsoFile, String nomeTeam, String titoloHackathon, String emailUtente) {
        if (percorsoFile != null && !percorsoFile.equals(SDEFAULT)) { // verifica validità dei campi
            new PaginaDocumento(frame, percorsoFile, nomeTeam, titoloHackathon, emailUtente, this);
            frame.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Seleziona un documento valido",
                    WPDEFAULT,
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /* ************************************************************************** */

    // metodi per la classe Hackathon

    public void addHackathon(Hackathon hackathon) {
        hackathons.add(hackathon);
        // inserisci dati nel DB
        hackathonDAO.aggiungiHackathon(hackathon);
    }

    public Hackathon getHackathonByTitolo(String titolo) {
        for (Hackathon hackathon : getHackathonsList()) {
            if (hackathon.getTitolo().equals(titolo)) {
                return hackathon;
            }
        }
        return null;
    }

    public List<Hackathon> getHackathonsList() {
        return hackathons;
    }

    public boolean existHackathon(String titolo) {
        for (Hackathon hackathon : getHackathonsList()) {
            if (hackathon.getTitolo().equals(titolo)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getListaTitoliHackathon() {
        List<String> listTitoloHackathon = new ArrayList<>();
        for (Hackathon hackathon : getHackathonsList()) {
            listTitoloHackathon.add(hackathon.getTitolo());
        }
        return listTitoloHackathon;
    }

    public List<String> getListaTitoliHackathonNonTerminati() {
        List<String> listNomeHackathon = new ArrayList<>();
        for (Hackathon hackathon : getHackathonsList()) {
            if(!hackathon.isTerminato()) {
                listNomeHackathon.add(hackathon.getTitolo());
            }
        }
        return listNomeHackathon;
    }

    public List<String> getListaNomiTeamLiberiByHackathon(String titolo) {
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

    // metodo per la pagina SelezionaHackathon
    public void metodoSelezionaHackathon(JFrame frame, String titolo, String emailUtente) {
        if (!titolo.equals(SDEFAULT)) { // verifica validità dei campi
            new PaginaHackathon(frame, emailUtente, titolo, this);
            frame.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Nessun Hackathon selezionato. Ricerca non riuscita",
                    WPDEFAULT,
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    /* ************************************************************************** */

    // metodi per la classe Team

    public void addTeam(Team team) {
        teams.add(team);
        // aggiungi dati nel DB
        teamDAO.aggiungiTeam(team);
    }

    public List<Team> getTeamsList() {
        return teams;
    }

    public List<String> getListaNomiTeamGiudicabiliByGiudiceAndHackathon(String emailUtente, String titoloHackathon) {
        List<String> listNomeTeam = new ArrayList<>();
        if (!emailUtente.isEmpty() && !titoloHackathon.isEmpty()) {
            Giudice giudice = getGiudiceByEmail(emailUtente);
            Hackathon hackathon = getHackathonByTitolo(titoloHackathon);
            if (giudice != null && hackathon != null) {
                for (Team team : hackathon.getTeamList()) {
                    if (giudice.getTeamGiudicabili().contains(team)) {
                        listNomeTeam.add(team.getNome());
                    }
                }
            }
        }
        return listNomeTeam;
    }

    public List<String> getListaNomiTeamGiudicabiliByGiudice(String emailUtente) {
        List<String> listNomeTeam = new ArrayList<>();
        if (!emailUtente.isEmpty()) {
            Giudice giudice = getGiudiceByEmail(emailUtente);
            if (giudice != null) {
                for (Team team : giudice.getTeamGiudicabili()) {
                    listNomeTeam.add(team.getNome());
                }
            }
        }
        return listNomeTeam;
    }

    public List<String> getListaNomiTeamGiudicatiByGiudiceAndHackathon(String emailUtente, String titoloHackathon) {
        List<String> listNomeTeam = new ArrayList<>();
        if (!(emailUtente.isEmpty() && titoloHackathon.isEmpty())) {
            Giudice giudice = getGiudiceByEmail(emailUtente);
            Hackathon hackathon = getHackathonByTitolo(titoloHackathon);
            if (giudice != null && hackathon != null) {
                for (Team team : hackathon.getTeamList()) {
                    if (giudice.getTeamGiudicati().contains(team)) {
                        listNomeTeam.add(team.getNome());
                    }
                }
            }
        }
        return listNomeTeam;
    }

    public List<String> getListaNomiTeamGiudicatiByGiudice(String emailUtente) {
        List<String> listNomeTeam = new ArrayList<>();
        if (!emailUtente.isEmpty()) {
            Giudice giudice = getGiudiceByEmail(emailUtente);
            if (giudice != null) {
                for (Team team : giudice.getTeamGiudicati()) {
                    listNomeTeam.add(team.getNome());
                }
            }
        }
        return listNomeTeam;
    }

    public Team getTeamByNomeAndHackathon(String nome, String titoloHackathon) {
        for (Hackathon hackathon : getHackathonsList()) {
            if (hackathon.getTitolo().equals(titoloHackathon)) {
                for (Team Team : hackathon.getTeamList()) {
                    if (Team.getNome().equals(nome)) {
                        return Team;
                    }
                }
            }
        }
        return null;
    }

    public List<String> getListaNominativiMembriByTeam(String nome, String titoloHackathon) {
        List<String> listMembri = new ArrayList<>();
        if (!(nome.isEmpty() && !titoloHackathon.isEmpty())) {
            Team team = getTeamByNomeAndHackathon(nome, titoloHackathon);
            if (team != null) {
                for (Concorrente membro : team.getMembri()) {
                    listMembri.add(membro.getNome() + " " + membro.getCognome());
                }
            }
        }
        return listMembri;
    }

    public List<String> getListaDocumentiByTeam(String nome, String titoloHackathon) {
        List<String> listDocumento = new ArrayList<>();
        if (!(nome.isEmpty() && !titoloHackathon.isEmpty())) {
            Team team = getTeamByNomeAndHackathon(nome, titoloHackathon);
            for (Team t : getTeamsList()) {
                if (t.equals(team)) {
                    for (Documento documento : team.getDocumenti())
                        listDocumento.add(documento.getNomeFile());
                }
            }
        }
        return listDocumento;
    }

    public boolean isTeamGiudicabileByGiudice(String nomeTeam, String titoloHackathon, String emailUtente) {
        if (!(nomeTeam.isEmpty() || titoloHackathon.isEmpty() || emailUtente.isEmpty())) {
            Team team = getTeamByNomeAndHackathon(nomeTeam, titoloHackathon);
            List<String> nomiTeamGiudicabiliList = getListaNomiTeamGiudicabiliByGiudiceAndHackathon(emailUtente, titoloHackathon);
            if (nomiTeamGiudicabiliList != null) {
                for (String nome : nomiTeamGiudicabiliList) {
                    if (getTeamByNomeAndHackathon(nome, titoloHackathon).equals(team)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isPartecipanteInTeam(String nomeTeam, String titoloHackathon, String emailConcorrente) {
        if (!(nomeTeam.isEmpty() && titoloHackathon.isEmpty() && emailConcorrente.isEmpty())) {
            Team team = getTeamByNomeAndHackathon(nomeTeam, titoloHackathon);
            Concorrente concorrente = getConcorrenteByEmail(emailConcorrente);
            if (team != null &&  concorrente != null) {
                return team.isPartecipante(concorrente);
            }
        }
        return false;
    }

    /* ************************************************************************** */

    // metodi per la classe Documento

    public void addDocumento(Documento documento) {
        documenti.add(documento);
        // aggiungi dati nel DB
        documentoDAO.aggiungiDocumento(documento);
    }

    public Documento setFileInDocumento(String nomeFile, Team team) {
        return new Documento(nomeFile, team);
    }

    public LocalDate getDataDocumento(String fileSelezionato, String nomeTeam, String titoloHackathon) {
        if (!(fileSelezionato.isEmpty() && nomeTeam.isEmpty() && titoloHackathon.isEmpty())) {
            Documento documento = getDocumentoByTeam(fileSelezionato, nomeTeam, titoloHackathon);
            if (documento != null) {
                return documento.getDataAggiornamento();
            }
        }
        return null;
    }

    public Documento getDocumentoByTeam(String fileSelezionato, String nomeTeam, String titoloHackathon) {
        if (!(nomeTeam.isEmpty() && titoloHackathon.isEmpty())) {
            Team team = getTeamByNomeAndHackathon(nomeTeam, titoloHackathon);
            if (team != null) {
                List<Documento> documentiList = team.getDocumenti();
                for (Documento documento : documentiList) {
                    if (documento.getNomeFile().equals(fileSelezionato)) {
                        return documento;
                    }
                }
            }
        }
        return null;
    }

    public List<String> getCommentiToTeamDocumento(String fileSelezionato, String nomeTeam, String titoloHackathon) {
        if (!(fileSelezionato.isEmpty() && nomeTeam.isEmpty() && titoloHackathon.isEmpty())) {
            Documento documento = getDocumentoByTeam(fileSelezionato, nomeTeam, titoloHackathon);
            if  (documento != null) {
              return new ArrayList<>(documento.getCommenti());
            }
        }
        return new ArrayList<>();
    }

    public void apriDocumento(JFrame frame, String percorsoFile) {
        if (percorsoFile != null && !percorsoFile.equals(SDEFAULT)) {
            try {
                // apertura file in base al sistema operativo
                String os = System.getProperty("os.name").toLowerCase();
                ProcessBuilder pb;
                if (os.contains("win")) {
                    pb = new ProcessBuilder("cmd", "/c", "start", "\"\"", percorsoFile);
                } else if (os.contains("mac")) {
                    pb = new ProcessBuilder("open", percorsoFile);
                } else { // Linux/Unix
                    pb = new ProcessBuilder("xdg-open", percorsoFile);
                }
                pb.start();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame,
                        "Errore nell'aprire il file: " + e.getMessage(),
                        EPDEFAULT,
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Seleziona un documento valido",
                    WPDEFAULT,
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /* ************************************************************************** */

    // metodi per la classe Voto

    public void addVoto(Voto voto) {
        voti.add(voto);
        // incremento dei Voti all'Hackathon che li ha ricevuti
        Hackathon hackathon = getHackathonByTitolo(voto.getTeam().getHackathon().getTitolo());
        hackathon.addNumVotiAssegnati();
        // aggiungi dati nel DB
        votoDAO.aggiungiVoto(voto);
        hackathonDAO.incrementaVoti(voto.getTeam().getHackathon().getTitolo());
    }

    /* ************************************************************************** */
}
