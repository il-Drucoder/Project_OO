package model;

import java.time.LocalDate;
import java.util.*;

public class Hackathon {
    // attributi
    private String titolo;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private int numMaxIscritti;
    private int numIscritti;
    private int dimMaxTeam;
    private LocalDate inizioIscrizioni;
    private LocalDate fineIscrizioni;
    private String descrizioneProblema;
    private String classifica;
    private String indirizzoSede;

    // rappresentazione relazioni
    private final Organizzatore creatore;  // riferimento all'organizzatore
    private final List<Giudice> giudiceList = new ArrayList<>();
    private final List<Team> teamList = new ArrayList<>();

    // metodi
    // Costruttore (solo l'organizzatore può creare un Hackathon)
    public Hackathon(Organizzatore creatore, String titolo, int numMaxIscritti, int dimMaxTeam, LocalDate inizioIscrizioni, LocalDate dataInizio, LocalDate dataFine, String descrizioneProblema, String indirizzoSede) {
        if (creatore == null) throw new IllegalArgumentException("Creatore mancante!");
        this.creatore = creatore;
        this.titolo = titolo;
        this.numMaxIscritti = numMaxIscritti;
        this.numIscritti = 0;
        this.dimMaxTeam = dimMaxTeam;
        this.inizioIscrizioni = inizioIscrizioni;
        this.fineIscrizioni = dataInizio.minusDays(2); // le iscrizioni chiudono 2 giorni prima l'inizio della gara
        this.dataInizio = dataInizio;
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

    public LocalDate getDataInizio() { return dataInizio; }
    // setter con controllo del creatore
    public void setDataInizio(Organizzatore creatore, LocalDate dataInizio) {
        if (!this.creatore.equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataFine() { return dataFine; }
    // setter con controllo del creatore
    public void setDataFine(Organizzatore creatore, LocalDate dataFine) {
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

    public int getNumIscritti() { return numIscritti; }
    // metodo che aggiunge un iscritto (invocato nel metodo creaTeam e partecipaTeam in concorrente), con controllo del team
    public void addIscritto(Team team) {
        for (Team t : this.teamList) {
            if (t.equals(team)) {
                this.numIscritti ++;
                return;
            }
        }
        throw new SecurityException("Accesso negato!");
    }

    public int getDimMaxTeam() { return dimMaxTeam; }
    // setter con controllo del creatore
    public void setDimMaxTeam(Organizzatore creatore, int dimMaxTeam) {
        if (!this.creatore.equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.dimMaxTeam = dimMaxTeam;
    }

    public LocalDate getInizioIscrizioni() { return inizioIscrizioni; }
    // setter con controllo del creatore
    public void setInizioIscrizioni(Organizzatore creatore, LocalDate inizioIscrizioni) {
        if (!this.creatore.equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.inizioIscrizioni = inizioIscrizioni;
    }

    public LocalDate getFineIscrizioni() { return fineIscrizioni; }
    // setter con controllo del creatore
    public void setFineIscrizioni(Organizzatore creatore, LocalDate fineIscrizioni) {
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

    // metodo utilizzato per il calcolo della classifica di un hackathon
    private String calcolaClassifica() throws IllegalStateException {
        // verifica che l'Hackathon sia concluso
        if(!isTerminato()) {
            throw new IllegalStateException("L'Hackathon è ancora in corso. Impossibile calcolare la classifica!");
        }

        // prende i team della lista, li ordina per punteggio (decrescente), li converte in formato stringa
        teamList.sort(Comparator.comparing(Team::getPunteggio).reversed());
        StringBuilder classificaStr = new StringBuilder("Classifica:\n");
        for (int i = 0; i < teamList.size(); i++) {
            classificaStr.append(String.format("%d. %s\n", i + 1, teamList.get(i).toString()));
        }
        return classificaStr.toString();
    }

    // metodo che verifica l'esistenza di un team (tramite in nome) in un Hackathon. True se lo trova, false altrimenti
    public boolean esisteTeam(String nomeTeam) {
        return this.teamList.stream()
                .anyMatch(team -> team.getNome().equalsIgnoreCase(nomeTeam)); // ignora le lettere maiuscole
    }

    // metodo che cerca e restituisce un team, se viene trovato nella teamList, mediante nome del team
    public Team getTeamByNome(String nomeTeam) {
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

    // metodo per verificare se un Hackathon è terminato
    public boolean isTerminato() {
        LocalDate today = LocalDate.now();
        return dataFine != null && today.isAfter(dataFine);
    }

    public boolean iscrizioniTerminate() {
        LocalDate today = LocalDate.now();
        return today.isAfter(fineIscrizioni);
    }

    public boolean isCompleto() {
        return numMaxIscritti == numIscritti;
    }

    // getter per la lista di giudici che supervisionano l'Hackathon
    public List<Giudice> getGiudiceList() {
        return new ArrayList<>(giudiceList); // restituisce una copia per sicurezza
    }

    public List<Team> getTeamList() {
        return new ArrayList<>(teamList);
    }
}
