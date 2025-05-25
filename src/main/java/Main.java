import java.util.Date;
import model.*;

public class Main {
    public static void main(String[] args) {
        // ------------------------------
        // 1. INIZIALIZZAZIONE UTENTI
        // ------------------------------
        Organizzatore organizzatore1 = new Organizzatore("Luca", "Bianchi", "luca.bianchi@organizzatore.com");
        Organizzatore organizzatore2 = new Organizzatore("Marco", "Verdi", "francesco.verdi@organizzatore.com");

        Giudice giudice1 = new Giudice("Anna", "Rossi", "anna.rossi@giudice.com");

        Concorrente concorrente1 = new Concorrente("Maria", "Gialli", "maria.gialli@concorrente.com");
        Concorrente concorrente2 = new Concorrente("Fabrizio", "Neri", "fabrizio.neri@concorrente.com");
        Concorrente concorrente3 = new Concorrente("Federico", "Neri", "federico.neri@concorrente.com");

        // ------------------------------
        // 2. CREAZIONE HACKATHON (MEDIANTE SOLO ORGANIZZATORE)
        // ------------------------------
        System.out.println("\n=== CREAZIONE HACKATHON ===");
        Hackathon hackathon1 = organizzatore1.creaHackathon(
                "Offline First 2025",
                1000, // numero massimo iscritti
                5, // numero massimo concorrenti di un team
                new Date(), // data inizio iscrizioni (oggi)
                new Date(System.currentTimeMillis() + 86400000), // +1 giorno per le iscrizioni
                new Date(System.currentTimeMillis() + 86400000 * 6), // +6 giorni per la data fine
                "Progettare strumenti digitali che funzionino perfettamente senza internet: utili, giochi, o servizi che si sincronizzano solo quando serve.", // descrizione problema
                "Via Mezzocannone 10, Napoli" // inidirizzo sede svolgimento Hackathon
        );
        System.out.println("Hackathon creato: " + hackathon1.getTitolo());

        // ------------------------------
        // 3. CONVOCAZIONE GIUDICI (MEDIANTE SOLO ORGANIZZATORE)
        // ------------------------------
        System.out.println("\n=== CONVOCAZIONE GIUDICI ===");
        organizzatore1.convocaGiudice(giudice1, hackathon1);

        // ------------------------------
        // 4. REGISTRAZIONE TEAM (MEDIANTE SOLO CONCORRENTI)
        // ------------------------------
        System.out.println("\n=== REGISTRAZIONE TEAM ===");

        Team team1 = concorrente3.creaTeam("Forza Napoli!", "Maradona", hackathon1); // aggiunto all'Hackathon1
        concorrente2.partecipaTeam("Forza Napoli!", "Maradona", hackathon1); // aggiunta di un nuovo membro al team

        Team team2 = concorrente1.creaTeam("Terminator", "Schwarzenegger", hackathon1); // aggiunto all'Hackathon1

        // ------------------------------
        // 5. ASSEGNAZIONE VOTI (ASSEGNABILI SOLO DAI GIUDICI)
        // ------------------------------
        System.out.println("\n=== VALUTAZIONE ===");
        giudice1.assegnaVoto(team1, 9); // voto corretto
        System.out.println("Voto assegnato: 9");
        giudice1.assegnaVoto(team2, 10); // voto corretto
        System.out.println("Voto assegnato: 10");

        // ------------------------------
        // 6. VISUALIZZAZIONE CLASSIFICA (SOLO UTENTI REGISTRATI)
        // ------------------------------
        System.out.println("\n=== CLASSIFICA ===");

        // richiesta di visualizzazione classifica (lancia un'eccezione perché non è attualmente possibile calcolare la classifica, l'Hackathon1 è ancora in coro)
        System.out.println(hackathon1.getClassifica(organizzatore1));
    }
}