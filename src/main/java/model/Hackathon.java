package model;

import java.time.LocalDate;
import java.util.*;

public class Hackathon {
    // attributi
    private String titolo;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private int numMaxIscritti;
    private int dimMaxTeam;
    private LocalDate inizioIscrizioni;
    private LocalDate fineIscrizioni;
    private String descrizioneProblema;
    private List<String> classifica;
    private String indirizzoSede;

    // attributi derivati (contatori)
    private int numIscritti;
    private int numVotiAssegnati;

    // rappresentazione relazioni
    private final Organizzatore creatore;  // riferimento all'organizzatore
    private final List<Giudice> giudiceList = new ArrayList<>();
    private final List<Team> teamList = new ArrayList<>();

    // metodi
    // Costruttore (solo l'organizzatore può creare un Hackathon)
    public Hackathon(Organizzatore creatore, String titolo, int numMaxIscritti, int dimMaxTeam, LocalDate inizioIscrizioni, LocalDate dataInizio, LocalDate dataFine, String indirizzoSede) {
        if (creatore == null) throw new IllegalArgumentException("Creatore mancante!");
        this.creatore = creatore;
        this.titolo = titolo;
        this.numMaxIscritti = numMaxIscritti;
        this.dimMaxTeam = dimMaxTeam;
        this.inizioIscrizioni = inizioIscrizioni;
        this.fineIscrizioni = dataInizio.minusDays(2); // le iscrizioni chiudono 2 giorni prima l'inizio della gara
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.descrizioneProblema = null;
        this.classifica = null;
        this.indirizzoSede = indirizzoSede;
        this.numIscritti = 0;
        this.numVotiAssegnati = 0;
    }

    // Costruttore per il DAO
    public Hackathon(Organizzatore creatore, String titolo, int numMaxIscritti, int dimMaxTeam, LocalDate inizioIscrizioni, LocalDate fineIscrizioni, LocalDate dataInizio, LocalDate dataFine, String descrizioneProblema, String classifica, String indirizzoSede, int numIscritti, int numVotiAssegnati ) {
        this.creatore = creatore;
        this.titolo = titolo;
        this.numMaxIscritti = numMaxIscritti;
        this.dimMaxTeam = dimMaxTeam;
        this.inizioIscrizioni = inizioIscrizioni;
        this.fineIscrizioni = fineIscrizioni;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.descrizioneProblema = descrizioneProblema;
        this.classifica = convertStringToList(classifica);
        this.indirizzoSede = indirizzoSede;
        this.numIscritti = numIscritti;
        this.numVotiAssegnati = numVotiAssegnati;
    }

    public String getTitolo() { return titolo; }
    // setter con controllo del creatore
    public void setTitolo(Organizzatore creatore, String titolo) {
        if (!getCreatore().equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.titolo = titolo;
    }

    public LocalDate getDataInizio() { return dataInizio; }
    // setter con controllo del creatore
    public void setDataInizio(Organizzatore creatore, LocalDate dataInizio) {
        if (!getCreatore().equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataFine() { return dataFine; }
    // setter con controllo del creatore
    public void setDataFine(Organizzatore creatore, LocalDate dataFine) {
        if (!getCreatore().equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.dataFine = dataFine;
    }

    public int getNumMaxIscritti() { return numMaxIscritti; }
    // setter con controllo del creatore
    public void setNumMaxIscritti(Organizzatore creatore, int numMaxIscritti) {
        if (!getCreatore().equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.numMaxIscritti = numMaxIscritti;
    }

    public int getNumIscritti() { return numIscritti; }
    // metodo che aggiunge un iscritto (invocato nel metodo creaTeam e partecipaTeam in concorrente)
    public void addIscritto(Team team) {
        if (getTeamList().contains(team)) {
            this.numIscritti++;
        }
    }

    public int getNumVotiAssegnati() { return numVotiAssegnati; }
    // metodo che aggiunge un voto (invocato nel metodo assegnaVoto in giudice), con controllo del team
    public void addVoto(Team team) {
        if (getTeamList().contains(team)) {
            this.numVotiAssegnati++;
        }
    }

    public int getDimMaxTeam() { return dimMaxTeam; }
    // setter con controllo del creatore
    public void setDimMaxTeam(Organizzatore creatore, int dimMaxTeam) {
        if (getCreatore().equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.dimMaxTeam = dimMaxTeam;
    }

    public LocalDate getInizioIscrizioni() { return inizioIscrizioni; }
    // setter con controllo del creatore
    public void setInizioIscrizioni(Organizzatore creatore, LocalDate inizioIscrizioni) {
        if (getCreatore().equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.inizioIscrizioni = inizioIscrizioni;
    }

    public LocalDate getFineIscrizioni() { return fineIscrizioni; }
    // setter con controllo del creatore
    public void setFineIscrizioni(Organizzatore creatore, LocalDate fineIscrizioni) {
        if (!getCreatore().equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.fineIscrizioni = fineIscrizioni;
    }

    public String getDescrizioneProblema() { return descrizioneProblema; }
    // setter con controllo del creatore
    public void setDescrizioneProblema(Giudice giudice, String descrizioneProblema) {
        for (Hackathon hackathonG : giudice.getHackathonAssegnati()) {
            if (hackathonG.getTitolo().equals(this.titolo)) {
                this.descrizioneProblema = descrizioneProblema;
                return;
            }
        }
        throw new SecurityException("Accesso negato!");
    }

    // getter con controllo dell'utente
    public List<String> getClassifica(UtentePiattaforma utente) {
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
    private void setClassifica(List<String> classifica) { this.classifica = classifica; }

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
        if (!getCreatore().equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.indirizzoSede = indirizzoSede;
    }

    // metodo per il DAO
    public String getIndirizzoSede() {
        return indirizzoSede;
    }

    // metodo utilizzato per il calcolo della classifica di un hackathon
    private List<String> calcolaClassifica() throws IllegalStateException {
        // verifica che l'Hackathon sia concluso
        if (!isTerminato()) {
            throw new IllegalStateException("L'Hackathon è ancora in corso. Impossibile calcolare la classifica!");
        }
        // verifica che la classifica sia calcolabile, ovvero che tutti i giudici abbiano assegnato i propri voti
        if (!isClassificaCalcolabile()) {
            throw new IllegalStateException("L'Hackathon è terminato. In attesa delle valutazioni dei giudici. Impossibile calcolare la classifica!");
        }

        // prende i team della lista, li ordina per punteggio (decrescente), li converte in formato stringa
        teamList.sort(Comparator.comparing(Team::getPunteggio).reversed());
        List<String> classificaFormattata = new ArrayList<>();
        int posizione = 1;
        for (int i = 0; i < teamList.size(); i++) {
            // verifica dei pari merito
            if (i > 0) { // verifica a partire dal secondo classificato
                if (teamList.get(i).getPunteggio() != teamList.get(i - 1).getPunteggio()) { // non c'è il pari merito, cambio la posizione in classifica
                    posizione = i + 1;
                }
            }
            classificaFormattata.add("\n" + posizione + "\t|" + teamList.get(i).getPunteggio() + "\t|" + teamList.get(i).getNome());
        }
        return classificaFormattata;
    }

    // metodo che verifica l'esistenza di un team (tramite in nome) in un Hackathon. True se lo trova, false altrimenti
    public boolean esisteTeam(String nomeTeam) {
        return getTeamList().stream().anyMatch(team -> team.getNome().equalsIgnoreCase(nomeTeam)); // ignora le lettere maiuscole
    }

    // metodo che cerca e restituisce un team, se viene trovato nella teamList, mediante nome del team
    public Team getTeamByNome(String nomeTeam) {
        for (Team team : getTeamList()) {
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
        this.teamList.add(team);
        // aggiunge il team ai teamGiudicati dei giudici appartenenti all'Hackathon
        for (Giudice giudice : getGiudiceList()) {
            giudice.aggiungiTeam(team);
        }
    }

    // metodo per aggiungere un giudice alla lista giudiceList
    public void aggiungiGiudice(Giudice giudice, Organizzatore organizzatore) {
        // verifica che il giudice venga aggiunto all'Hackathon solo tramite il creatore
        if(!getCreatore().equals(organizzatore)) {
            throw new SecurityException("Impossibile aggiungere giudice!");
        }
        this.giudiceList.add(giudice);
    }

    // metodo per verificare se un Hackathon è terminato
    public boolean isTerminato() {
        return LocalDate.now().isAfter(getDataFine());
    }

    public boolean iscrizioniTerminate() {
        return LocalDate.now().isAfter(getFineIscrizioni());
    }

    // un Hackathon è al completo quando il numero di iscritti raggiunge il numero massimo di iscritti
    public boolean isCompleto() {
        return getNumMaxIscritti() == getNumIscritti();
    }

    // una classifica è calcolabile quando l'Hackathon è terminato e sono stati assegnati tutti i voti (V = T * G), dove V è il numero di voti, T è il numero di team e G è il numero di giudici (ogni giudice dà un voto ad ogni team)
    public boolean isClassificaCalcolabile() {
        return isTerminato() && (getNumVotiAssegnati() == getTeamList().size() * getGiudiceList().size());
    }

    // getter per il creatore dell'Hackathon
    public Organizzatore getCreatore() {
        return creatore;
    }

    // getter per la lista di giudici che supervisionano l'Hackathon
    public List<Giudice> getGiudiceList() {
        return new ArrayList<>(giudiceList); // restituisce una copia per sicurezza
    }

    // getter per la lista di team che partecipano all'Hackathon
    public List<Team> getTeamList() {
        return new ArrayList<>(teamList);
    }

    // metodo che determina lo stato in cui si trova la gara
    public String statoGara() {
        if (isClassificaCalcolabile()) {
            return "Valutata";
        }
        if (isTerminato()) {
            return "Terminata";
        }
        if (LocalDate.now().isAfter(getDataInizio().minusDays(1))) {
            return "In corso";
        }
        if (iscrizioniTerminate()) {
            return "Iscrizioni terminate. In attesa dell'inizio della gara";
        }
        if (isCompleto()) {
            return "Numero massimo concorrenti raggiunto. In attesa dell'inizio della gara";
        }
        if (LocalDate.now().isAfter(getInizioIscrizioni().minusDays(1))) {
            return "Iscrizioni aperte";
        }
        return "Iscrizioni non ancora aperte";
    }

    // metodo per verificare che la gara sia in un determinato stato
    public boolean verificaStatoGara(String statoRichiesto) {
        String statoGara = statoGara();

        // se lo stato della gara è quello richiesto, allora non ci sono problemi
        return statoRichiesto.equals(statoGara);
    }

    // metodo per verificare che la gara sia in un determinato stato (Overload del metodo precedente)
    public void verificaStatoGara(String statoRichiesto, String azioneDaFare) {
        String statoGara = statoGara();

        // se lo stato della gara è quello richiesto, allora non ci sono problemi
        if (statoRichiesto.equals(statoGara)) {
            return;
        }
        // se lo stato della gara NON è quello richiesto, lancia eccezione in base al problema riscontrato
        switch (statoGara) {
            case ("Valutata") :
                throw new IllegalStateException("Impossibile " + azioneDaFare + ": Hackathon concluso e valutato");
            case ("Conclusa") :
                throw new IllegalStateException("Impossibile " + azioneDaFare + ": Hackathon terminato");
            case ("In corso") :
                throw new IllegalStateException("Impossibile " + azioneDaFare + ": Hackathon in corso");
            case ("Iscrizioni terminate. In attesa dell'inizio della gara") :
                throw new IllegalStateException("Impossibile " + azioneDaFare + ": periodo di iscrizioni terminato");
            case ("Numero massimo concorrenti raggiunto. In attesa dell'inizio della gara") :
                throw new IllegalStateException("Impossibile " + azioneDaFare + ": Hackathon al completo");
            case ("Iscrizioni aperte") :
                throw new IllegalStateException("Impossibile " + azioneDaFare + ": periodo di iscrizioni aperto");
            case ("Iscrizioni non ancora aperte") :
                throw new IllegalStateException("Impossibile " + azioneDaFare + ": iscrizioni non ancora aperte");
        }
    }

    // metodo usato dal Costruttore per il DAO
    public static List<String> convertStringToList(String input) {
        if (input == null || input.length() < 2) {
            return new ArrayList<>();
        }

        // Rimuove le parentesi quadre iniziali e finali
        String trimmed = input.substring(1, input.length() - 1).trim();

        // Se la stringa è vuota dopo aver rimosso le parentesi
        if (trimmed.isEmpty()) {
            return new ArrayList<>();
        }
        String[] elements = trimmed.split(",");
        List<String> result = new ArrayList<>();
        for (String s : elements) {
            result.add(s.trim());
        }

        return result;
    }
}
