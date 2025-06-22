package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaginaTeam {
    public JFrame frame;
    private JPanel panel1;
    private JLabel labelNomeTeam;
    private JLabel labelPunteggioTeam;
    private JLabel labelCapogruppoTeam;
    private JLabel labelMembri;
    private JLabel labelHackathonAppartenenza;
    private JLabel labelStatoGara;
    private JButton buttonAggiungiDocumento;
    private JButton buttonVisualizzaDocumenti;

    public PaginaTeam(JFrame frameChiamante, String nomeTeam, String titoloHackathon, Controller controller) {
        frame = new JFrame("Team");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                frameChiamante.setVisible(true);
                frame.dispose();
            }
        });

        labelNomeTeam.setText("Nome: "+ controller.getTeamByNome(nomeTeam).getNome());
        labelPunteggioTeam.setText("Punteggio: "+ controller.getTeamByNome(nomeTeam).getPunteggio());
        labelCapogruppoTeam.setText("Capogruppo: " + controller.getTeamByNome(nomeTeam).getCreatore().getNome() + " " + controller.getTeamByNome(nomeTeam).getCreatore().getCognome());
        labelMembri.setText("Membri: " + controller.getListaMembriByTeam(controller.getTeamByNome(nomeTeam)));
        labelHackathonAppartenenza.setText("Hackathon appartenenza: " + controller.getTeamByNome(nomeTeam).getHackathon().getTitolo());
        boolean statoGara = controller.getHackathonByTitolo(titoloHackathon).isTerminato();
        labelStatoGara.setText("Stato gara: " + (statoGara ? "terminata" : "in corso"));

        buttonVisualizzaDocumenti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VisualizzaDocumenti VisualizzaDocumentiGUI = new VisualizzaDocumenti(frame, nomeTeam, titoloHackathon, controller);
                frame.setVisible(false);
            }
        });

        buttonAggiungiDocumento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AggiungiDocumento AggiungiDocumentoGUI = new AggiungiDocumento(frame, nomeTeam, titoloHackathon, controller);
                frame.setVisible(false);
            }
        });
    }
}
