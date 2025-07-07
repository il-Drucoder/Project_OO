package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class BenvenutoConcorrente {
    private static JFrame frame;
    private JPanel panel1;
    private JLabel labelNomeConcorrente;
    private JComboBox<String> comboBoxSceltaAzione;
    private JButton okButton;

    public BenvenutoConcorrente(JFrame frameChiamante, String emailConcorrente, Controller controller) {
        new JFrame("Benvenuto Concorrente");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        labelNomeConcorrente.setText("Benvenutə " + controller.getUtentePiattaformaByEmail(emailConcorrente).getNome() + " " + controller.getUtentePiattaformaByEmail(emailConcorrente).getCognome());

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sceltaAzione = Objects.toString(comboBoxSceltaAzione.getSelectedItem(), "Seleziona");
                switch (sceltaAzione) {
                    case "Crea team" :
                        new CreaTeam(frame, emailConcorrente, controller);
                        frame.setVisible(false);
                        break;
                    case "Partecipa a un team" :
                        new PartecipaTeam(frame, emailConcorrente, controller);
                        frame.setVisible(false);
                        break;
                    case "Accedi a un team" :
                        new AccediTeam(frame, emailConcorrente, controller);
                        frame.setVisible(false);
                        break;
                    case "Cerca Hackathon" :
                        new SelezionaHackathon(frame, emailConcorrente, controller.getListaTitoliHackathon(), controller);
                        frame.setVisible(false);
                        break;
                    case "Logout" :
                        frameChiamante.setVisible(true);
                        frame.dispose();
                        break;
                    default :
                        JOptionPane.showMessageDialog(frame,
                                "Scelta non valida!",
                                "Warning page",
                                JOptionPane.WARNING_MESSAGE);
                        frame.setVisible(true);
                }
            }
        });
    }
}
