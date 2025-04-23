import java.util.Date;
import model.*;

public class Main {
    public static void main(String[] args) {
        // ------------------------------
        // 1. INIZIALIZZAZIONE UTENTI
        // ------------------------------
        Organizzatore organizzatore1 = new Organizzatore("Luca", "Bianchi", "luca.bianchi@hackathon.org");
        Organizzatore organizzatore2 = new Organizzatore("Mucca", "Bianchi", "muuca.bianchi@hackathon.org");

        Giudice giudice1 = new Giudice("Anna", "Rossi", "anna.rossi@giudice.com");

        Concorrente concorrente1 = new Concorrente("Marco", "Verdi", "marco.verdi@studente.it");
        Concorrente concorrente2 = new Concorrente("Fabrizio", "Neri", "fabrizio.neri@studente.it");

        // ------------------------------
        // 2. CREAZIONE HACKATHON (SOLO ORGANIZZATORE)
        // ------------------------------
        System.out.println("\n=== CREAZIONE HACKATHON ===");
        Hackathon hackathon1 = organizzatore1.creaHackathon(
                "AI Innovation 2023",
                100, // numero massimo iscritti
                3, // numero massimo concorrenti di un team
                new Date(), // data inizio iscrizioni (oggi)
                new Date(System.currentTimeMillis() + 86400000), // +1 giorno per le iscrizioni
                new Date(System.currentTimeMillis() + 86400000 * 6), // +6 giorni per la data fine
                "Sviluppa soluzioni AI per il clima", // descrizione problema
                "Via Pippo Franco"
        );
        System.out.println("Hackathon creato: " + hackathon1.getTitolo());

        // ------------------------------
        // 3. CONVOCAZIONE GIUDICI (SOLO ORGANIZZATORE)
        // ------------------------------
        System.out.println("\n=== CONVOCAZIONE GIUDICI ===");
        organizzatore1.convocaGiudice(giudice1, hackathon1);

        // DA VERIFICARE
        //giudice1.aggiungiHackathonAssegnato(hackathon1, organizzatore1); // metodo da monello

        // ------------------------------
        // 4. REGISTRAZIONE TEAM (SOLO CONCORRENTI)
        // ------------------------------
        System.out.println("\n=== REGISTRAZIONE TEAM ===");
        Team team1 = concorrente1.creaTeam("AI Masters", "password123", hackathon1); // Aggiunto all'hackathon
        System.out.println("Team creato: " + team1.getNome());
        concorrente2.partecipaTeam("AI Masters", "password123", hackathon1); // Aggiunta di un nuovo membro al team

        // ------------------------------
        // 5. ASSEGNAZIONE VOTI (SOLO GIUDICI)
        // ------------------------------
        System.out.println("\n=== VALUTAZIONE ===");
        giudice1.assegnaVoto(team1, 9); // Voto corretto
        System.out.println("Voto assegnato: 9");

        // ------------------------------
        // 6. VISUALIZZAZIONE CLASSIFICA (SOLO UTENTI REGISTRATI)
        // ------------------------------
        System.out.println("\n=== CLASSIFICA ===");

        // creazione utente registrato
        UtentePiattaforma utenteRegistrato = new Concorrente("Paolo", "Neri", "paolo@email.com");

        // richiesta di visualizzazione classifica (lancia un'eccezione perché non è attualmente possibile calcolare la classifica, l'Hackathon è ancora in coro)
        System.out.println(hackathon1.getClassifica(utenteRegistrato));

    }
}