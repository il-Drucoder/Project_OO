package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BenvenutoConcorrente {
    public JFrame frame;
    private JPanel panel1;
    private JLabel label1;
    private JLabel nome;
    private JComboBox fieldSceltaAzione;
    private JButton OKButton;

    public BenvenutoConcorrente(JFrame frameChiamante, String emailConcorrente, Controller controller) {
        frame= new JFrame("Benvenuto Concorrente");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        nome.setText("Benvenutə " + controller.getUtentePiattaformaByEmail(emailConcorrente).getNome() + " " + controller.getUtentePiattaformaByEmail(emailConcorrente).getCognome());

        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sceltaAzione = fieldSceltaAzione.getSelectedItem().toString();
                if(sceltaAzione.equals("Crea team")){
                    CreaTeam CreaTeamGUI = new CreaTeam(frame, emailConcorrente, controller);
                    frame.setVisible(false);
                } else if(sceltaAzione.equals("Partecipa a un team")){
                    PartecipaTeam PartecipaTeamGUI = new PartecipaTeam(frame, emailConcorrente, controller);
                    frame.setVisible(false);
                } else if(sceltaAzione.equals("Accedi a un team")){
                    AccediTeam AccediTeamGUI = new AccediTeam(frame, emailConcorrente, controller);
                    frame.setVisible(false);
                } else if(sceltaAzione.equals("Visualizza la classifica di un Hackathon")){
                    VisualizzaClassifica VisualizzazioneClassificaGUI = new VisualizzaClassifica(frame, emailConcorrente, controller);
                    frame.setVisible(false);
                }
            }
        });
    }
}
