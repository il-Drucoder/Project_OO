package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BenvenutoConcorrente {
    public JFrame frame;
    private JPanel panel1;
    private JLabel labelNomeConcorrente;
    private JComboBox comboBoxSceltaAzione;
    private JButton okButton;

    public BenvenutoConcorrente(JFrame frameChiamante, String emailConcorrente, Controller controller) {
        frame= new JFrame("Benvenuto Concorrente");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        labelNomeConcorrente.setText("Benvenutə " + controller.getUtentePiattaformaByEmail(emailConcorrente).getNome() + " " + controller.getUtentePiattaformaByEmail(emailConcorrente).getCognome());

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sceltaAzione = comboBoxSceltaAzione.getSelectedItem().toString();
                if (sceltaAzione.equals("Crea team")) {
                    CreaTeam CreaTeamGUI = new CreaTeam(frame, emailConcorrente, controller);
                    frame.setVisible(false);
                } else if (sceltaAzione.equals("Partecipa a un team")) {
                    PartecipaTeam PartecipaTeamGUI = new PartecipaTeam(frame, emailConcorrente, controller);
                    frame.setVisible(false);
                } else if (sceltaAzione.equals("Accedi a un team")) {
                    AccediTeam AccediTeamGUI = new AccediTeam(frame, emailConcorrente, controller);
                    frame.setVisible(false);
                } else if (sceltaAzione.equals("Cerca Hackathon")) {
                    SelezionaHackathon SelezionaHackathonGUI = new SelezionaHackathon(frame, emailConcorrente, controller.getListaTitoliHackathon(), controller);
                    frame.setVisible(false);
                } else if (sceltaAzione.equals("Logout")) {
                    frameChiamante.setVisible(true);
                    frame.dispose();
                }
            }
        });
    }
}
