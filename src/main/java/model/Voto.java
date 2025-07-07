package model;

public class Voto {
    // attributi
    private int valore;

    // rappresentazione relazioni
    private final Team team; // team che riceve il voto
    private final Giudice giudice; // giudice che assegna il voto

    // metodi
    // Costruttore
    public Voto(Giudice giudice, Team team, int valore) {
        this.giudice = giudice;
        this.team = team;
        setValore(valore); // utilizza il set per effettuare la verifica del valore
    }

    public int getValore() { return valore; }
    public void setValore(int valore) {
        // verifica che il nuovo voto abbia un valore valido
        if (valore < 0 || valore > 10) {
            throw new IllegalArgumentException("Voto non valido");
        }
        this.valore = valore;
    }

    public Team getTeam() { return team; }
    public Giudice getGiudice() { return giudice; }

    // override del metodo toString della classe Object, per rappresentare l'oggetto Voto come stringa (numero a lettere)
    @Override
    public String toString() {
        return (switch (getValore()) {
                    case 0 -> "zero";
                    case 1 -> "uno";
                    case 2 -> "due";
                    case 3 -> "tre";
                    case 4 -> "quattro";
                    case 5 -> "cinque";
                    case 6 -> "sei";
                    case 7 -> "sette";
                    case 8 -> "otto";
                    case 9 -> "nove";
                    case 10 -> "dieci";
                    default -> "non valido";
                }
        );
    }
}
