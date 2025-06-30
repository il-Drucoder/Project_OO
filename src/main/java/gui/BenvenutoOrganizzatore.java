package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BenvenutoOrganizzatore {
    public JFrame frame;
    private JPanel panel1;
    private JLabel labelNomeOrganizzatore;
    private JComboBox comboBoxSceltaAzione;
    private JButton okButton;

    public BenvenutoOrganizzatore(JFrame frameChiamante, String emailOrganizzatore, Controller controller) {
        frame= new JFrame("Benvenuto Organizzatore");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        labelNomeOrganizzatore.setText("Benvenutə " + controller.getUtentePiattaformaByEmail(emailOrganizzatore).getNome() + " " + controller.getUtentePiattaformaByEmail(emailOrganizzatore).getCognome());

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sceltaAzione = comboBoxSceltaAzione.getSelectedItem().toString();
                if (sceltaAzione.equals("Crea Hackathon")) {
                    CreaHackathon CreaHackathonGUI = new CreaHackathon(frame, emailOrganizzatore, controller);
                    frame.setVisible(false);
                } else if (sceltaAzione.equals("Convoca giudice")) {
                    if (controller.getListaTitoliHackathonCreatiByCreatore(emailOrganizzatore).isEmpty()) {
                        JOptionPane.showMessageDialog(frame,
                                "Attenzione, creare prima almeno un Hackathon",
                                "Warning page",
                                JOptionPane.WARNING_MESSAGE);
                        frame.setVisible(true);
                    } else {
                        ConvocaGiudice ConvocaGiudiceGUI = new ConvocaGiudice(frame, emailOrganizzatore, controller);
                        frame.setVisible(false);
                    }
                } else if (sceltaAzione.equals("Cerca Hackathon creati")) {
                    if (controller.getListaTitoliHackathonCreatiByCreatore(emailOrganizzatore).isEmpty()) {
                        JOptionPane.showMessageDialog(frame,
                                "Non esistono Hackathon creati da te",
                                "Warning page",
                                JOptionPane.WARNING_MESSAGE);
                        frame.setVisible(true);
                    } else {
                        SelezionaHackathon SelezionaHackathonGUI = new SelezionaHackathon(frame, emailOrganizzatore, controller.getListaTitoliHackathonCreatiByCreatore(emailOrganizzatore), controller);
                        frame.setVisible(false);
                    }
                } else if (sceltaAzione.equals("Cerca Hackathon")) {
                    SelezionaHackathon SelezionaHackathonGUI = new SelezionaHackathon(frame, emailOrganizzatore, controller.getListaTitoliHackathon(), controller);
                    frame.setVisible(false);
                } else if (sceltaAzione.equals("Logout")) {
                    frameChiamante.setVisible(true);
                    frame.dispose();
                }
            }
        });
    }
}
