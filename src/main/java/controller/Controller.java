package controller;

import gui.*;
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

        // preleva dati dal DB uploadAllList() { dumpDati... }
    }

    /* ************************************************************************** */

    // metodi per la classe UtentePiattaforma

    public void addUtente(UtentePiattaforma utente) {
        UtentiPiattaforma.add(utente);
        // inserisci dati nel DB
    }

    // public void dumpDatiUtente() { ... UtentiPiattaforma.addAll(getTuttiUtenti()); }

    public List<UtentePiattaforma> getUtentiPiattaformaList() {
        return UtentiPiattaforma;
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
                    BenvenutoConcorrente BenvenutoConcorrenteGUI = new BenvenutoConcorrente(frame, email, this);
                    frame.setVisible(false);
                } else if (isGiudice(email)) {
                    Giudice giudice = new Giudice(nome, cognome, email, pwString);
                    addGiudice(giudice);
                    BenvenutoGiudice BenvenutoGiudiceGUI = new BenvenutoGiudice(frame, email, this);
                    frame.setVisible(false);
                } else if (isOrganizzatore(email)) {
                    Organizzatore organizzatore = new Organizzatore(nome, cognome, email, pwString);
                    addOrganizzatore(organizzatore);
                    BenvenutoOrganizzatore BenvenutoOrganizzatoreGUI = new BenvenutoOrganizzatore(frame, email, this);
                    frame.setVisible(false);
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

    // metodo per la pagina Login
    public void metodoLogin(JFrame frame, String email, char[] pw) {
        if (!(email.isEmpty() || pw.length == 0)) {
            UtentePiattaforma utentePiattaforma = getUtentePiattaformaByEmail(email);
            if (utentePiattaforma != null) {
                String pwString = new String(pw);
                if (utentePiattaforma.verificaPassword(pwString)) {
                    if (isConcorrente(email)) {
                        BenvenutoConcorrente BenvenutoConcorrenteGUI = new BenvenutoConcorrente(frame, email, this);
                        frame.setVisible(false);
                    } else if (isGiudice(email)) {
                        BenvenutoGiudice BenvenutoGiudiceGUI = new BenvenutoGiudice(frame, email, this);
                        frame.setVisible(false);
                    } else { // l'utente è un organizzatore (@organizzatore.com)
                        BenvenutoOrganizzatore BenvenutoOrganizzatoreGUI = new BenvenutoOrganizzatore(frame, email, this);
                        frame.setVisible(false);
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
                ClassificaHackathon ClassificaHackathonGUI = new ClassificaHackathon(frame, classifica, titoloHackathon);
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
                    "Utente non valido",
                    "Warning page",
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    /* ************************************************************************** */

    // metodi per la classe Organizzatore

    public void addOrganizzatore(Organizzatore organizzatore) {
        Organizzatori.add(organizzatore);
        addUtente(organizzatore);
        // inserisci dati nel DB
    }

    // public void dumpDati...

    public List<Organizzatore> getOrganizzatoriList() {
        return Organizzatori;
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
                        "Creazione Hackathon non riuscita. Valori inseriti non coerenti",
                        "Warning page",
                        JOptionPane.WARNING_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Creazione Hackathon non riuscita",
                    "Warning page",
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    // metodo per la pagina ConvocaGiudice
    public void metodoConvocaGiudice(JFrame frame, String titoloHackathon, String emailGiudice, String emailOrganizzatore) {
        if (!(titoloHackathon.equals("Seleziona") || emailGiudice.isEmpty())) { // verifica validità dei campi
            try {
                Organizzatore organizzatore = getOrganizzatoreByEmail(emailOrganizzatore);
                Giudice giudice = getGiudiceByEmail(emailGiudice);
                Hackathon hackathon = getHackathonByTitolo(titoloHackathon);
                organizzatore.convocaGiudice(hackathon, giudice);
                JOptionPane.showMessageDialog(frame,
                        "Convocazione giudice avvenuta con successo",
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
                    "Convocazione giudice non riuscita",
                    "Warning page",
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    /* ************************************************************************** */

    // metodi per la classe Giudice

    public void addGiudice(Giudice giudice) {
        Giudici.add(giudice);
        addUtente(giudice);
        // inserisci dati nel DB
    }

    // public void dumpDati...

    public List<Giudice> getGiudiciList() {
        return Giudici;
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
                    JOptionPane.showMessageDialog(frame,
                            "Descrizione problema inserita con successo",
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
                        "Giudice e/o Hackathon non trovato/i",
                        "Warning page",
                        JOptionPane.WARNING_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Nessuna descrizione inserita, operazione di aggiunta non riuscita",
                    "Warning page",
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    // metodo per la pagina VisionaTeam e GiudicaTeam
    public void metodoVisionaAndOrGiudicaTeam(JFrame frame, String titoloHackathon, String nomeTeam, String emailGiudice) {
        if (!(titoloHackathon.equals("Seleziona") || nomeTeam.equals("Seleziona"))) { // verifica validità dei campi
            try {
                PaginaTeam PaginaTeamGUI = new PaginaTeam(frame, nomeTeam, titoloHackathon, emailGiudice, this);
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
                    "Hackathon e/o giudice non selezionato/i. Operazione di visione/giudizio team non riuscita",
                    "Warning page",
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
                        "Giudice e/o team non trovato/i",
                        "Warning page",
                        JOptionPane.WARNING_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Operazione assegnazione voto non riuscita",
                    "Warning page",
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
                    if (firma) {
                        commento += "\n(" + giudice.getNome() + " " + giudice.getCognome() + ")";
                    }
                    documento.setCommenti(giudice, commento);
                    JOptionPane.showMessageDialog(frame,
                            "Commento inserito con successo",
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
                        "Documento/team/giudice/Hackathon non trovato/i",
                        "Warning page",
                        JOptionPane.WARNING_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Nessun commento inserito, operazione di aggiunta non riuscita",
                    "Warning page",
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    /* ************************************************************************** */

    // metodi per la classe Concorrente

    public void addConcorrente(Concorrente concorrente) {
        Concorrenti.add(concorrente);
        addUtente(concorrente);
        // inserisci dati nel DB
    }

    // public void dumpDati...

    public List<Concorrente> getConcorrentiList() {
        return Concorrenti;
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
        if (!(titoloHackathon.equals("Seleziona") || nomeTeam.isEmpty() || pwTeamString.isEmpty())) {
            Concorrente concorrente = getConcorrenteByEmail(emailConcorrente);
            Hackathon hackathon = getHackathonByTitolo(titoloHackathon);
            if (concorrente != null && hackathon != null) {
                try {
                    Team team = concorrente.creaTeam(nomeTeam, pwTeamString, hackathon);
                    addTeam(team);
                    JOptionPane.showMessageDialog(frame,
                            "Creazione team avvenuta con successo",
                            "Message page",
                            JOptionPane.INFORMATION_MESSAGE);
                    PaginaTeam PaginaTeamGUI = new PaginaTeam(frame, nomeTeam, titoloHackathon, emailConcorrente, this);
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
                        "Concorrente e/o Hackathon non trovato/i",
                        "Warning page",
                        JOptionPane.WARNING_MESSAGE);
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

    // metodo per la pagina PartecipaTeam
    public void metodoPartecipaTeam(JFrame frame, String titoloHackathon, String nomeTeam, char[] pwTeam, String emailConcorrente) {
        String pwTeamString = new String(pwTeam);
        if (!(titoloHackathon.equals("Seleziona") || nomeTeam.equals("Seleziona") || pwTeamString.isEmpty())) { // verifica validità dei campi
            Concorrente concorrente = getConcorrenteByEmail(emailConcorrente);
            if (concorrente != null) {
                try {
                    concorrente.partecipaTeam(nomeTeam, pwTeamString, getHackathonByTitolo(titoloHackathon));
                    PaginaTeam PaginaTeamGUI = new PaginaTeam(frame, nomeTeam, titoloHackathon, emailConcorrente, this);
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
                        "Concorrente non trovato",
                        "Warning page",
                        JOptionPane.WARNING_MESSAGE);
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

    // metodo per la pagina AccediTeam
    public void metodoAccediTeam(JFrame frame, String titoloHackathon, String nomeTeam, String emailConcorrente) {
        if (!(titoloHackathon.equals("Seleziona") || nomeTeam.equals("Seleziona"))) { // verifica validità dei campi
            Concorrente concorrente = getConcorrenteByEmail(emailConcorrente);
            Team team = getTeamByNomeAndHackathon(nomeTeam, titoloHackathon);
            if (concorrente != null && team != null) {
                try {
                    concorrente.accediTeam(team);
                    PaginaTeam PaginaTeamGUI = new PaginaTeam(frame, nomeTeam, titoloHackathon, emailConcorrente, this);
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
                        "Concorrente e/o team non trovato/i",
                        "Warning page",
                        JOptionPane.WARNING_MESSAGE);
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

    // metodo per la pagina AggiungiDocumento
    public void metodoAggiungiDocumento(JFrame frame, File fileSelezionato, String nomeTeam, String titoloHackathon) {
        if (fileSelezionato != null) { // verifica validità dei campi
            Team team = getTeamByNomeAndHackathon(nomeTeam, titoloHackathon);
            if (team != null) {
                try {
                    team.aggiungiDocumento(setFileInDocumento(fileSelezionato, team));
                    addDocumento(setFileInDocumento(fileSelezionato, team));
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
                        "Team non trovato",
                        "Warning page",
                        JOptionPane.WARNING_MESSAGE);
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Nessun file selezionato, operazione di inserimento non riuscita",
                    "Warning page",
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(false);
        }
    }

    // metodo per la pagina VisualizzaDocumenti
    public void metodoVisualizzaDocumenti(JFrame frame, String percorsoFile, String nomeTeam, String titoloHackathon, String emailUtente) {
        if (percorsoFile != null && !percorsoFile.equals("Seleziona")) { // verifica validità dei campi
            PaginaDocumento PaginaDocumentoGUI = new PaginaDocumento(frame, percorsoFile, nomeTeam, titoloHackathon, emailUtente, this);
            frame.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Seleziona un documento valido",
                    "Warning page",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /* ************************************************************************** */

    // metodi per la classe Hackathon

    public void addHackathon(Hackathon hackathon) {
        Hackathons.add(hackathon);
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
        return Hackathons;
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
        if (!titolo.equals("Seleziona")) { // verifica validità dei campi
            PaginaHackathon PaginaHackathonGUI = new PaginaHackathon(frame, emailUtente, titolo, this);
            frame.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Nessun Hackathon selezionato. Ricerca non riuscita",
                    "Warning page",
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(true);
        }
    }

    /* ************************************************************************** */

    // metodi per la classe Team

    public void addTeam(Team team) { Teams.add(team); }

    public List<Team> getTeamsList() {
        return Teams;
    }

    public List<String> getListaNomiTeamGiudicabiliByGiudiceAndHackathon(String emailUtente, String titoloHackathon) {
        if (!emailUtente.isEmpty() && !titoloHackathon.isEmpty()) {
            Giudice giudice = getGiudiceByEmail(emailUtente);
            Hackathon hackathon = getHackathonByTitolo(titoloHackathon);
            if (giudice != null && hackathon != null) {
                List<String> listNomeTeam = new ArrayList<>();
                for (Team team : hackathon.getTeamList()) {
                    if (giudice.getTeamGiudicabili().contains(team)) {
                        listNomeTeam.add(team.getNome());
                    }
                }
                return listNomeTeam;
            }
        }
        return null;
    }

    public List<String> getListaNomiTeamGiudicabiliByGiudice(String emailUtente) {
        if (!emailUtente.isEmpty()) {
            Giudice giudice = getGiudiceByEmail(emailUtente);
            if (giudice != null) {
                List<String> listNomeTeam = new ArrayList<>();
                for (Team team : giudice.getTeamGiudicabili()) {
                    listNomeTeam.add(team.getNome());
                }
                return listNomeTeam;
            }
        }
        return null;
    }

    public List<String> getListaNomiTeamGiudicatiByGiudiceAndHackathon(String emailUtente, String titoloHackathon) {
        if (!(emailUtente.isEmpty() && titoloHackathon.isEmpty())) {
            Giudice giudice = getGiudiceByEmail(emailUtente);
            Hackathon hackathon = getHackathonByTitolo(titoloHackathon);
            if (giudice != null && hackathon != null) {
                List<String> listNomeTeam = new ArrayList<>();
                for (Team team : hackathon.getTeamList()) {
                    if (giudice.getTeamGiudicati().contains(team)) {
                        listNomeTeam.add(team.getNome());
                    }
                }
                return listNomeTeam;
            }
        }
        return null;
    }

    public List<String> getListaNomiTeamGiudicatiByGiudice(String emailUtente) {
        if (!emailUtente.isEmpty()) {
            Giudice giudice = getGiudiceByEmail(emailUtente);
            if (giudice != null) {
                List<String> listNomeTeam = new ArrayList<>();
                for (Team team : giudice.getTeamGiudicati()) {
                    listNomeTeam.add(team.getNome());
                }
                return listNomeTeam;
            }
        }
        return null;
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
        if (!(nome.isEmpty() && !titoloHackathon.isEmpty())) {
            Team team = getTeamByNomeAndHackathon(nome, titoloHackathon);
            if (team != null) {
                List<String> listMembri = new ArrayList<>();
                for (Concorrente membro : team.getMembri()) {
                    listMembri.add(membro.getNome() + " " + membro.getCognome());
                }
                return listMembri;
            }
        }
        return null;
    }

    public List<String> getListaDocumentiByTeam(String nome, String titoloHackathon){
        if (!(nome.isEmpty() && !titoloHackathon.isEmpty())) {
            Team team = getTeamByNomeAndHackathon(nome, titoloHackathon);
            List<String> listDocumento = new ArrayList<>();
            for (Team t : getTeamsList()) {
                if (t.equals(team)) {
                    for (Documento documento : team.getDocumenti())
                        listDocumento.add(documento.getFile().getAbsolutePath());
                }
            }
            return listDocumento;
        }
        return null;
    }

    public boolean isTeamGiudicabileByGiudice(String nomeTeam, String titoloHackathon, String emailUtente) {
        if (!(nomeTeam.isEmpty() && titoloHackathon.isEmpty() && emailUtente.isEmpty())) {
            Team team = getTeamByNomeAndHackathon(nomeTeam, titoloHackathon);
            List<String> nomiTeamGiudicabiliList = getListaNomiTeamGiudicabiliByGiudiceAndHackathon(emailUtente, titoloHackathon);
            for (String nome : nomiTeamGiudicabiliList) {
                if (getTeamByNomeAndHackathon(nome, titoloHackathon).equals(team)) {
                    return true;
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
        Documenti.add(documento);
    }

    public Documento setFileInDocumento(File file, Team team) {
        return new Documento(file, team);
    }

    public LocalDate getDataDocumento(String fileSelezionato, String nomeTeam, String titoloHackathon) {
        if (!(fileSelezionato.isEmpty() &&  nomeTeam.isEmpty() && titoloHackathon.isEmpty())) {
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
            if  (team != null) {
                List<Documento> documenti = team.getDocumenti();
                for (Documento documento : documenti) {
                    if (documento.getFile().getAbsolutePath().equals(fileSelezionato)) {
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
        return null;
    }

    public void apriDocumento(JFrame frame, String percorsoFile) {
        if (percorsoFile != null && !percorsoFile.equals("Seleziona")) {
            File file = new File(percorsoFile); // ricavo il file dal suo percorso
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

    // metodi per la classe Voto

    public void addVoto(Voto voto) {
        Voti.add(voto);
    }

    /* ************************************************************************** */
}
