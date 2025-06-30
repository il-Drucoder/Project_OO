package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BenvenutoGiudice {
    public JFrame frame;
    private JPanel panel1;
    private JLabel labelNomeGiudice;
    private JComboBox comboBoxSceltaAzione;
    private JButton okButton;

    public BenvenutoGiudice(JFrame frameChiamante, String emailGiudice, Controller controller) {
        frame= new JFrame("Benvenuto Giudice");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        labelNomeGiudice.setText("Benvenutə " + controller.getUtentePiattaformaByEmail(emailGiudice).getNome() + " " + controller.getUtentePiattaformaByEmail(emailGiudice).getCognome());

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sceltaAzione = comboBoxSceltaAzione.getSelectedItem().toString();
                if (sceltaAzione.equals("Giudica team assegnati")) {
                    if (controller.getListaNomiTeamGiudicabiliByGiudice(emailGiudice).isEmpty()) {
                        JOptionPane.showMessageDialog(frame,
                                "Non ci sono team assegnati da giudicare",
                                "Warning page",
                                JOptionPane.WARNING_MESSAGE);
                        frame.setVisible(true);
                    } else {
                        GiudicaTeam GiudicaTeamGUI = new GiudicaTeam(frame, emailGiudice, controller);
                        frame.setVisible(false);
                    }

                } else if (sceltaAzione.equals("Visiona team giudicati")) {
                    if (controller.getListaNomiTeamGiudicatiByGiudice(emailGiudice).isEmpty()) {
                        JOptionPane.showMessageDialog(frame,
                                "Non ci sono team assegnati giudicati",
                                "Warning page",
                                JOptionPane.WARNING_MESSAGE);
                        frame.setVisible(true);
                    } else {
                        VisionaTeam VisionaTeamGUI = new VisionaTeam(frame, emailGiudice, controller);
                        frame.setVisible(false);
                    }
                } else if (sceltaAzione.equals("Cerca Hackathon assegnati")) {
                    if (controller.getListaTitoliHackathonAssegnatiToGiudice(emailGiudice).isEmpty()) {
                        JOptionPane.showMessageDialog(frame,
                                "Non ci sono Hackathon assegnati",
                                "Warning page",
                                JOptionPane.WARNING_MESSAGE);
                        frame.setVisible(true);
                    } else {
                        SelezionaHackathon SelezionaHackathonGUI = new SelezionaHackathon(frame, emailGiudice, controller.getListaTitoliHackathonAssegnatiToGiudice(emailGiudice), controller);
                        frame.setVisible(false);
                    }
                } else if (sceltaAzione.equals("Cerca Hackathon")) {
                    SelezionaHackathon SelezionaHackathonGUI = new SelezionaHackathon(frame, emailGiudice, controller.getListaTitoliHackathon(), controller);
                    frame.setVisible(false);
                } else if (sceltaAzione.equals("Logout")) {
                    frameChiamante.setVisible(true);
                    frame.dispose();
                }
            }
        });
    }
}
