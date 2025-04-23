package model;

import java.util.*;

public class Hackathon {
    // attributi
    private String titolo;
    private Date dataInizio;
    private Date dataFine;
    private int numMaxIscritti;
    private int dimMaxTeam;
    private Date inizioIscrizioni;
    private Date fineIscrizioni;
    private String descrizioneProblema;
    private String classifica;
    private String indirizzoSede;

    // rappresentazione relazioni
    private final Organizzatore creatore;  // riferimento all'organizzatore
    private final List<Giudice> giudiceList = new ArrayList<>();
    private final List<Team> teamList = new ArrayList<>();

    // metodi
    // Costruttore (solo l'organizzatore può creare un Hackathon)
    public Hackathon(Organizzatore creatore, String titolo, int numMaxIscritti, int dimMaxTeam, Date inizioIscrizioni, Date fineIscrizioni, Date dataFine, String descrizioneProblema, String indirizzoSede) {
        if (creatore == null) throw new IllegalArgumentException("Creatore mancante!");
        this.creatore = creatore;
        this.titolo = titolo;
        this.numMaxIscritti = numMaxIscritti;
        this.dimMaxTeam = dimMaxTeam;
        this.inizioIscrizioni = inizioIscrizioni;
        this.fineIscrizioni = fineIscrizioni;
        this.dataInizio = new Date(fineIscrizioni.getTime() + 86400000 * 2); // l'inizio avviene 2 giorni dopo la fine delle iscrizioni
        this.dataFine = dataFine;
        this.descrizioneProblema = descrizioneProblema;
        this.classifica = null;
        this.indirizzoSede = indirizzoSede;
    }

    public String getTitolo() { return titolo; }
    // setter con controllo del creatore
    public void setTitolo(Organizzatore creatore, String titolo) {
        if (!this.creatore.equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.titolo = titolo;
    }

    public Date getDataInizio() { return dataInizio; }
    // setter con controllo del creatore
    public void setDataInizio(Organizzatore creatore, Date dataInizio) {
        if (!this.creatore.equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.dataInizio = dataInizio;
    }

    public Date getDataFine() { return dataFine; }
    // setter con controllo del creatore
    public void setDataFine(Organizzatore creatore, Date dataFine) {
        if (!this.creatore.equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.dataFine = dataFine;
    }

    public int getNumMaxIscritti() { return numMaxIscritti; }
    // setter con controllo del creatore
    public void setNumMaxIscritti(Organizzatore creatore, int numMaxIscritti) {
        if (!this.creatore.equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.numMaxIscritti = numMaxIscritti;
    }

    public int getDimMaxTeam() { return dimMaxTeam; }
    // setter con controllo del creatore
    public void setDimMaxTeam(Organizzatore creatore, int dimMaxTeam) {
        if (!this.creatore.equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.dimMaxTeam = dimMaxTeam;
    }

    public Date getInizioIscrizioni() { return inizioIscrizioni; }
    // setter con controllo del creatore
    public void setInizioIscrizioni(Organizzatore creatore, Date inizioIscrizioni) {
        if (!this.creatore.equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.inizioIscrizioni = inizioIscrizioni;
    }

    public Date getFineIscrizioni() { return fineIscrizioni; }
    // setter con controllo del creatore
    public void setFineIscrizioni(Organizzatore creatore, Date fineIscrizioni) {
        if (!this.creatore.equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.fineIscrizioni = fineIscrizioni;
    }

    public String getDescrizioneProblema() { return descrizioneProblema; }
    // setter con controllo del creatore
    public void setDescrizioneProblema(Organizzatore creatore, String descrizioneProblema) {
        if (!this.creatore.equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.descrizioneProblema = descrizioneProblema;
    }

    // getter con controllo dell'utente
    public String getClassifica(UtentePiattaforma utente) {
        // verifica che la richiesta sia effettuata da un utente
        if (utente == null) {
            throw new SecurityException("Accesso negato: utente non esistente!");
        }
        // verifica se la classifica è già stata calcolata
        if (classifica == null) {
            setClassifica(calcolaClassifica());
        }
        return this.classifica;
    }
    // setter privato, invocabile solo dalla funzione calcolaClassifica
    private void setClassifica(String classifica) { this.classifica = classifica; }

    // getter con controllo dell'utente
    public String getIndirizzoSede(UtentePiattaforma utente) {
        // verifica che la richiesta sia effettuata da un utente
        if (utente == null) {
            throw new SecurityException("Accesso negato: utente non esistente!");
        }

        return indirizzoSede;
    }
    // setter con controllo del creatore
    public void setIndirizzoSede(Organizzatore creatore, String indirizzoSede) {
        if (!this.creatore.equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.indirizzoSede = indirizzoSede;
    }


    // vedi da qui!!

    // Metodo utilizzato per il calcolo della classifica di un hackathon
    private String calcolaClassifica() {
        // verifica che l'Hackathon sia concluso
        if(!isTerminato()) {
            throw new IllegalStateException("L'Hackathon è ancora in corso. Impossibile calcolare la classifica!");
        }

        // RIVEDI QUI (prende i team della lista, li ordina per punteggio, li converte in stringa
        teamList.sort(Comparator.comparing(Team::getPunteggio).reversed());
        StringBuilder classificaStr = new StringBuilder("Classifica:\n");
        for (int i = 0; i < teamList.size(); i++) {
            classificaStr.append(String.format("%d. %s\n", i + 1, teamList.get(i).toString()));
        }
        return classificaStr.toString();
    }

    public boolean esisteTeam(String nomeTeam) {
        return this.teamList.stream()
                .anyMatch(team -> team.getNome().equalsIgnoreCase(nomeTeam)); // Controllo case-insensitive
    }

    // metodo che cerca e restituisce un team, se viene trovato nella teamList, mediante nome del team
    public Team getTeamPerNome(String nomeTeam) {
        for (Team team : teamList) {
            if (team.getNome().equalsIgnoreCase(nomeTeam)) {
                return team;
            }
        }
        return null;
    }

    // metodo per aggiungere un team alla lista teamList
    public void aggiungiTeam(Team team, Concorrente creatore) {
        // verifica che il team venga aggiunto all'Hackathon solo tramite il creatore
        if(!team.getCreatore().equals(creatore)) {
            throw new SecurityException("Impossibile aggiungere team!");
        }
        teamList.add(team);
    }

    // metodo per aggiungere un giudice alla lista giudiceList
    public void aggiungiGiudice(Giudice giudice, Organizzatore organizzatore) {
        // verifica che il giudice venga aggiunto all'Hackathon solo tramite il creatore
        if(!organizzatore.equals(creatore)) {
            throw new SecurityException("Impossibile aggiungere giudice!");
        }
        giudiceList.add(giudice);
    }

    public boolean isTerminato() {
        Date now = new Date();
        return dataFine != null && now.after(dataFine);
    }

    public boolean iscrizioniTerminate() {
        Date now = new Date();
        return fineIscrizioni != null && now.before(fineIscrizioni);
    }

    // getter per la lista di giudici che supervisionano l'Hackathon
    public List<Giudice> getGiudiceList() {
        return new ArrayList<>(giudiceList); // restituisce una copia per sicurezza
    }
}
