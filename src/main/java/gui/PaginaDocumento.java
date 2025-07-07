package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PaginaDocumento {
    private static final JFrame frame = new JFrame("Documento");
    private JPanel panel1;
    private JLabel labelNomeDocumento;
    private JButton openDocButton;
    private JLabel labelDataCaricamento;
    private JTextArea textAreaListaCommentiAlTeam;
    private JPanel panelGiudice;
    private JTextArea textAreaCommentoGiudice;
    private JRadioButton addSignRadioButton;
    private JButton sendCommentButton;

    public PaginaDocumento(JFrame frameChiamante, String selectedFile, String nomeTeam, String titoloHackathon, String emailUtente, Controller controller) {
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                frameChiamante.setVisible(true);
                frame.dispose();
            }
        });

        labelNomeDocumento.setText("Nome: " + selectedFile);

        openDocButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.apriDocumento(frame, selectedFile);
                azzeraCampi();
            }
        });

        labelDataCaricamento.setText("Data caricamento (anno-mese-giorno): " + controller.getDataDocumento(selectedFile, nomeTeam, titoloHackathon));

        textAreaListaCommentiAlTeam.setEditable(false);
        List<String> commenti = controller.getCommentiToTeamDocumento(selectedFile, nomeTeam, titoloHackathon);
        if (!commenti.isEmpty()) {
            textAreaListaCommentiAlTeam.setText("Commenti ricevuti:\n" + commenti);
        } else {
            textAreaListaCommentiAlTeam.setText("Nessun commento ricevuto");
        }

        // verifica la possibilità di aggiungerne commenti (solo in fase di gara in corso e solo per i giudici assegnati)
        if (!(controller.getHackathonByTitolo(titoloHackathon).verificaStatoGara("In corso") && controller.isTeamGiudicabileByGiudice(nomeTeam, titoloHackathon, emailUtente))) {
            panelGiudice.setVisible(false);
        }

        sendCommentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.metodoAggiungiCommento(frame, emailUtente, textAreaCommentoGiudice.getText(), addSignRadioButton.isSelected(), selectedFile, nomeTeam, titoloHackathon);
                azzeraCampi();
                // ritorniamo alla pagina di seleziona documento (pagina precedente a questa)
                frameChiamante.setVisible(true);
                frame.dispose();
            }
        });
    }

    private void azzeraCampi() {
        textAreaCommentoGiudice.setText("");
        addSignRadioButton.setSelected(false);
    }
}
