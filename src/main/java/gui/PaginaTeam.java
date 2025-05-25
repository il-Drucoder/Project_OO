package gui;

import controller.Controller;
import model.Team;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaginaTeam {
    private JPanel panel1;
    private JLabel nomeTeam;
    private JLabel punteggioTeam;
    private JLabel capogruppoTeam;
    private JButton visualizzaDocumentiButton;
    private JButton aggiungiDocumentoButton;
    private JLabel hackathonApparteneza;
    public JFrame frame;

    PaginaTeam(JFrame frameChiamante, Team team, Controller controller) {
        frame = new JFrame("Team");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        nomeTeam.setText("Nome: "+ team.getNome());
        punteggioTeam.setText("Punteggio: "+ team.getPunteggio());
        capogruppoTeam.setText("Capogruppo: " + team.getCreatore().getNome() + " " + team.getCreatore().getCognome());
        hackathonApparteneza.setText("Hackathon Appartenza: " + team.getHackathon().getTitolo());

        visualizzaDocumentiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VisualizzaDocumenti VisualizzaDocumentiGUI = new VisualizzaDocumenti(frame,team,controller);
                frame.setVisible(false);
            }
        });

        aggiungiDocumentoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AggiungiDocumento AggiungiDocumentoGUI = new AggiungiDocumento(frame,team,controller);
                frame.setVisible(false);
            }
        });
    }
}
