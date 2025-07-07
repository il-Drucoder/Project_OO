package model;

import java.util.ArrayList;
import java.util.List;

public class Team {
    // attributi
    private String nome;
    private String pw;

    // rappresentazione relazioni
    private final Hackathon hackathon;
    private final Concorrente creatore;
    private final List<Concorrente> membri = new ArrayList<>(); // lista concorrenti
    private final List<Voto> voti = new ArrayList<>(); // lista di voti ricevuti
    private final List<Documento> documenti = new ArrayList<>(); // lista documenti pubblicati dal team

    // metodi
    // Costruttore
    public Team(String nome, String pw, Hackathon hackathon, Concorrente creatore) {
        this.nome = nome;
        this.pw = pw;
        this.hackathon = hackathon; // imposta l'Hackathon di riferimento
        this.creatore = creatore;
    }

    public String getNome() { return nome; }
    // setter con controllo del creatore
    public void setNome(Concorrente creatore, String nome) {
        if (getCreatore().equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.nome = nome;
    }

    // setter con controllo del creatore
    public String getPw(Concorrente creatore) {
        if (!getCreatore().equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        return pw;
    }
    // setter con controllo del creatore
    public void setPw(Concorrente creatore, String pw) {
        if (!getCreatore().equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.pw = pw;
    }

    // metodo per il DAO
    public String getPw() {
        return pw;
    }

    public Hackathon getHackathon() { return hackathon; }

    public Concorrente getCreatore() { return creatore; }

    // getter per la lista di voti assegnati al team
    public List<Voto> getVoti() { return voti; }
    // metodo utilizzato dal dumpDatiVoto
    public void addVoti(Voto voto) {
        voti.add(voto);
    }

    // aggiungi un voto al team
    public void aggiungiVoto(Giudice giudice, Voto voto) {
        if(!giudice.isAssegnato(this)) {
            throw new SecurityException("Accesso negato!");
        }
        voti.add(voto);
    }

    // calcola la media dei voti
    public double getPunteggio() {
        if (getVoti().isEmpty()) {
            return 0.0;
        }
        int somma = 0;
        for (Voto voto : getVoti()) {
            somma += voto.getValore();
        }
        return (double) somma / getVoti().size();
    }

    // getter per la lista di membri partecipanti al team
    public List<Concorrente> getMembri() { return membri; }
    // metodo utilizzato dal dumpDatiPartecipazioneAiTeam
    public void addMembri(Concorrente concorrente) {
        membri.add(concorrente);
    }

    // metodo per aggiungere un nuovo concorrente al team
    public void aggiungiMembro(Concorrente concorrente) {
        // verifica che il team non sia al completo
        if (isCompleto()) {
            throw new IllegalStateException("Team al completo!");
        }
        membri.add(concorrente);
    }

    // metodo per verificare la password senza esportarla
    public boolean verificaPassword(String passwordInserita) {
        return this.pw.equals(passwordInserita);
    }

    // getter per la lista di documenti creati dal team
    public List<Documento> getDocumenti() { return documenti; }
    // metodo utilizzato dal dumpDatiDocumento
    public void addDocumenti(Documento documento) {
        documenti.add(documento);
    }

    // metodo per aggiungere un documento alla lista documenti di un team
    public void aggiungiDocumento(Documento documento) {
        for (Documento d : getDocumenti()) {
            if (documento.getNomeFile().equals(d.getNomeFile())) {
                throw new IllegalArgumentException("Il documento selezionato è già stato caricato");
            }
        }
        this.documenti.add(documento);
    }

    // metodo per verificare se il team è al completo
    public boolean isCompleto() {
        return (getMembri().size() == getHackathon().getDimMaxTeam());
    }

    // metodo per verificare se un concorrente fa parte del team
    public  boolean isPartecipante(Concorrente concorrente) {
        return getMembri().contains(concorrente);
    }
}
