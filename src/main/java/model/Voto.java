package model;

public class Voto {
    // attributi
    private int valore;

    // rappresentazione relazioni
    private Team team; // team che riceve il voto
    private Giudice giudice; // giudice che assegna il voto

    // metodi
    // Costruttore
    public Voto(Giudice giudice, Team team, int valore) {
        this.giudice = giudice;
        this.team = team;
        this.valore = valore;
    }

    public int getValore() { return valore; }
    public void setValore(Giudice assegnatore, int valore) {
        // verifica che l'assegnatore sia effettivamente il giudice che ha assegnato in precedenza il voto
        if(assegnatore != giudice) {
            throw new SecurityException("Voto non modificabile da questo giudice!");
        }
        // verifica che il nuovo voto abbia un valore valido
        if (valore < 0 || valore > 10) {
            throw new IllegalArgumentException("Voto non valido");
        }
        this.valore = valore;
    }
}
