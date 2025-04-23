package model;

public class UtentePiattaforma {
    // attributi
    protected String nome;
    protected String cognome;
    protected String email;

    // metodi
    // Costruttore che imposta automaticamente isRegistrato a true e imposta automaticamente l'id
    public UtentePiattaforma(String nome, String cognome, String email) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public void visualizzaClassifica(Hackathon hackathon) {
        // verifica che l'Hackathon esista
        if (hackathon == null) {
            System.out.println("Nessun Hackathon selezionato!");
            return;
        }
        System.out.println("=== Classifica di " + hackathon.getTitolo() + " ===");
        System.out.println(hackathon.getClassifica(this));
    }
}
