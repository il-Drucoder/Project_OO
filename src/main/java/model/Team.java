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
        this.hackathon = hackathon; // Imposta l'hackathon di riferimento
        this.creatore = creatore;
    }

    public String getNome() { return nome; }
    // setter con controllo del creatore
    public void setNome(Concorrente creatore, String nome) {
        if (!this.creatore.equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.nome = nome;
    }

    // setter con controllo del creatore
    public String getPw(Concorrente creatore) {
        if (!this.creatore.equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        return pw;
    }
    // setter con controllo del creatore
    public void setPw(Concorrente creatore, String pw) {
        if (!this.creatore.equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.pw = pw;
    }

    public Concorrente getCreatore() { return creatore; }

    // getter per la lista di voti assegnati al team
    public List<Voto> getVoti() { return voti; }
    // aggiungi un voto al team
    public void aggiungiVoto(int valore) {
        // verifica che il voto sia compreso tra 0 e 10
        if (valore < 0 || valore > 10) {
            throw new IllegalArgumentException("Voto non valido!");
        }
        Voto voto = new Voto(valore);
        voti.add(voto);
    }

    // calcola la media dei voti
    public double getPunteggio() {
        if (voti.isEmpty()) return 0.0;

        int somma = 0;
        for (Voto voto : voti) {
            somma += voto.getValore();
        }
        return (double) somma / voti.size();
    }

    // override del metodo toString della classe Object, per rappresentare l'oggetto Team come stringa contenente nome e punteggio
    @Override
    public String toString() {
        return String.format("Team: %s (Punteggio: %.1f)", this.nome, this.getPunteggio());
    }

    // getter per la lista di membri partecipanti al team
    public List<Concorrente> getMembri() { return membri; }

    // metodo per aggiungere un nuovo concorrente al team
    public void aggiungiMembro(Concorrente concorrente) {
        // verifica che il team non sia al completo
        if (membri.size() == hackathon.getDimMaxTeam()) {
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
}
