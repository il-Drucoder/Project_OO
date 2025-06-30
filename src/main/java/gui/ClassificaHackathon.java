package gui;

import javax.swing.*;
import java.util.List;

public class ClassificaHackathon {
    public JFrame frame;
    private JPanel panel1;
    private JLabel labelTitoloClassifica;
    private JTextArea textAreaClassifica;

    public ClassificaHackathon(JFrame frameChiamante, List<String> classifica, String titoloHackathon) {
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

        labelTitoloClassifica.setText("Classifica Hackathon '" + titoloHackathon + "'");
        textAreaClassifica.setEditable(false);
        textAreaClassifica.setText("posizione \t| punteggio \t| nome team " + classifica);
    }
}
