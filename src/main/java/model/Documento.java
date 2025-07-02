package model;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Documento {
    // attributi
    private LocalDate dataAggiornamento;
    private File file;

    // rappresentazione relazioni
    private final List<String> Commenti;
    private final Team team;

    // metodi
    // Costruttore
    public Documento(File file,  Team team) {
        this.dataAggiornamento = LocalDate.now(); // la data di aggiornamento viene inserita in automatico appena viene creato il documento
        this.file = file;
        this.Commenti = new ArrayList<>();
        this.team = team;
    }

    public LocalDate getDataAggiornamento() { return dataAggiornamento; }
    // setter privato utilizzabile solo tramite il metodo setFile
    private void setDataAggiornamento(LocalDate dataAggiornamento) {
        this.dataAggiornamento = dataAggiornamento;
    }

    public File getFile() { return file; }
    // setFile fa automaticamente un setDataAggiornamento
    public void setFile(File file) {
        this.file = file;
        this.setDataAggiornamento(LocalDate.now()); // aggiorna alla data corrente
    }

    public List<String> getCommenti() { return Commenti; }
    // setter con controllo su giudice
    public void setCommenti(Giudice giudice, String commento) {
        team.getHackathon().verificaStatoGara("In corso", "aggiungere un commento");
        if (!giudice.isAssegnato(this.team)) {
            throw new SecurityException("Accesso negato!");
        }
        String commentoFormattato = "\n---\n" + commento + "\n---\n";
        Commenti.add(commento);
    }
}
