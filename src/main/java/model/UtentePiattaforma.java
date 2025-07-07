package model;

public class UtentePiattaforma {
    // attributi
    protected String nome;
    protected String cognome;
    protected String email;
    protected String pw;
    protected String ruolo;

    // metodi
    // Costruttore
    public UtentePiattaforma(String nome, String cognome, String email, String pw, String ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.pw = pw;
        this.ruolo = ruolo;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // metodi per il DAO
    public String getPw() { return pw; }
    public String getRuolo() { return ruolo; }

    // metodo per verificare la password senza esportarla
    public boolean verificaPassword(String passwordInserita) {
        return this.pw.equals(passwordInserita);
    }
}
