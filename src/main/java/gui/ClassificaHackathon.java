package gui;

import controller.Controller;

import javax.swing.*;

public class ClassificaHackathon {
    public JFrame frame;
    private JPanel panel1;
    private JLabel labelTitoloClassifica;
    private JLabel labelClassifica;

    public ClassificaHackathon(JFrame frameChiamante, String titoloHackathon, String emailUtente, Controller controller) {
        frame = new JFrame("Classifica Hackathon");
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

        if (controller.existUtentePiattaforma(emailUtente)) {
            labelTitoloClassifica.setText("Classifica Hackathon '" + titoloHackathon + "'");
            labelClassifica.setText(controller.getHackathonByTitolo(titoloHackathon).getClassifica(controller.getUtentePiattaformaByEmail(emailUtente)));
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Utente non registrato",
                    "Warning page",
                    JOptionPane.WARNING_MESSAGE);
            frame.setVisible(false);
        }
    }
}
