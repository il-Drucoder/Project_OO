package model;

public class Voto {
    private int valore;

    public int getValore() { return valore; }
    public void setValore(int valore) {
        if (valore < 0 || valore > 10) throw new IllegalArgumentException("Voto non valido");
        this.valore = valore;
    }
}
