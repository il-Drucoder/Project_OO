package model;

public class Voto {
    // attributi
    private int valore;

    // metodi
    // Costruttore
    public Voto(int valore) {
        this.valore = valore;
    }

    public int getValore() { return valore; }
    public void setValore(int valore) {
        if (valore < 0 || valore > 10) throw new IllegalArgumentException("Voto non valido");
        this.valore = valore;
    }
}
