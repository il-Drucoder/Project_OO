package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class BenvenutoOrganizzatore {
    private static JFrame frame;
    private JPanel panel1;
    private JLabel labelNomeOrganizzatore;
    private JComboBox<String> comboBoxSceltaAzione;
    private JButton okButton;

    public BenvenutoOrganizzatore(JFrame frameChiamante, String emailOrganizzatore, Controller controller) {
        new JFrame("Benvenuto Organizzatore");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        labelNomeOrganizzatore.setText("Benvenutə " + controller.getUtentePiattaformaByEmail(emailOrganizzatore).getNome() + " " + controller.getUtentePiattaformaByEmail(emailOrganizzatore).getCognome());

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sceltaAzione = Objects.toString(comboBoxSceltaAzione.getSelectedItem(), "Seleziona");
                switch (sceltaAzione) {
                    case "Crea Hackathon" :
                        new CreaHackathon(frame, emailOrganizzatore, controller);
                        frame.setVisible(false);
                        break;
                    case "Convoca giudice" :
                        if (controller.getListaTitoliHackathonCreatiByCreatore(emailOrganizzatore).isEmpty()) {
                            paginaDiWarning("Attenzione, creare prima almeno un Hackathon");
                        } else {
                            new ConvocaGiudice(frame, emailOrganizzatore, controller);
                            frame.setVisible(false);
                        }
                        break;
                    case "Cerca Hackathon creati" :
                        if (controller.getListaTitoliHackathonCreatiByCreatore(emailOrganizzatore).isEmpty()) {
                            paginaDiWarning("Non esistono Hackathon creati da te");
                        } else {
                            new SelezionaHackathon(frame, emailOrganizzatore, controller.getListaTitoliHackathonCreatiByCreatore(emailOrganizzatore), controller);
                            frame.setVisible(false);
                        }
                        break;
                    case "Cerca Hackathon" :
                        new SelezionaHackathon(frame, emailOrganizzatore, controller.getListaTitoliHackathon(), controller);
                        frame.setVisible(false);
                        break;
                    case "Logout" :
                        frameChiamante.setVisible(true);
                        frame.dispose();
                        break;
                    default :
                        paginaDiWarning("Scelta non valida!");
                }
            }
        });
    }

    private void paginaDiWarning(String messaggio) {
        JOptionPane.showMessageDialog(frame,
                messaggio,
                "Warning page",
                JOptionPane.WARNING_MESSAGE);
        frame.setVisible(true);
    }
}
