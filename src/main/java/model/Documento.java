package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Documento {
    // attributi
    private int idDocumento; // id DB
    private LocalDate dataAggiornamento;
    private String nomeFile;
    private final List<String> commenti;

    // rappresentazione relazioni
    private final Team team;

    // metodi
    // Costruttore
    public Documento(String nomeFile,  Team team) {
        this.dataAggiornamento = LocalDate.now(); // la data di aggiornamento viene inserita in automatico appena viene creato il documento
        this.nomeFile = nomeFile;
        this.commenti = new ArrayList<>();
        this.team = team;
    }

    // Costruttore per il DAO
    public Documento(int idDocumento, String nomeFile,Team team, LocalDate dataAggiornamento) {
        this.idDocumento = idDocumento;
        this.nomeFile = nomeFile;
        this.commenti = new ArrayList<>();
        this.team = team;
        this.dataAggiornamento = dataAggiornamento;
    }

    public int getIdDocumento() { return idDocumento; }

    public void setIdDocumento(int idDocumento) { this.idDocumento = idDocumento; }

    public LocalDate getDataAggiornamento() { return dataAggiornamento; }
    // setter privato utilizzabile solo tramite il metodo setFile
    private void setDataAggiornamento(LocalDate dataAggiornamento) {
        this.dataAggiornamento = dataAggiornamento;
    }

    public String getNomeFile() { return nomeFile; }
    // setFile fa automaticamente un setDataAggiornamento
    public void setNomeFile(String nomeFile) {
        this.nomeFile = nomeFile;
        this.setDataAggiornamento(LocalDate.now()); // aggiorna alla data corrente
    }

    // aggiunto per il DAO
    public Team getTeam() { return team; }

    public List<String> getCommenti() { return commenti; }
    // metodo utilizzato dal dumpDatiDocumenti
    public void addTuttiCommenti(List<String> tuttiCommenti) {
        commenti.addAll(tuttiCommenti);
    }
    // setter con controllo su giudice
    public void setCommenti(Giudice giudice, String commento) {
        team.getHackathon().verificaStatoGara("In corso", "aggiungere un commento");
        if (!giudice.isAssegnato(this.team)) {
            throw new SecurityException("Accesso negato!");
        }
        String commentoFormattato = "\n---\n" + commento + "\n---\n";
        commenti.add(commentoFormattato);
    }
}