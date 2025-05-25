package gui;

import controller.Controller;
import model.Hackathon;
import model.UtentePiattaforma;

import javax.swing.*;

public class ClassificaHackathon {
    private JPanel panel1;
    private JLabel classifica;
    private JLabel nomeHackathon;
    public JFrame frame;

    public ClassificaHackathon(JFrame frameChiamante, String titoloHackathon, String emailUtente, Controller controller) {
        frame = new JFrame("Classifica Hackathon");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        UtentePiattaforma utentePiattaforma = controller.getUtentePiattaformaByEmail(emailUtente);

        if(utentePiattaforma == null) {
            PaginaFallimento PaginaFallimentoGUI = new PaginaFallimento(frame,"Utente non registrato. Operazione richiesta",controller);
            frame.setVisible(false);
        } else {
            Hackathon hackathon = controller.getHackathonByName(titoloHackathon);
            nomeHackathon.setText("Classifica Hackathon '" + hackathon.getTitolo() + "'");
            classifica.setText(hackathon.getClassifica(utentePiattaforma));
        }
    }
}
