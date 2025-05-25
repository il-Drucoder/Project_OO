package gui;

import controller.Controller;
import model.Concorrente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BenvenutoConcorrente {
    private JPanel panel1;
    public JFrame frame;
    private JLabel label1;
    private JLabel nome;
    private JComboBox comboBox1;
    private JButton OKButton;

    public BenvenutoConcorrente(JFrame frameChiamante, String emailConcorrente, Controller controller) {
        frame= new JFrame("Benvenuto Concorrente");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        Concorrente concorrente = controller.getConcorrenteByEmail(emailConcorrente);
        nome.setText("Benvenutə " + concorrente.getNome() + " " + concorrente.getCognome());

        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(comboBox1.getSelectedItem().toString().equals("Crea team")){
                    CreaTeam CreaTeamGUI = new CreaTeam(frame,emailConcorrente,controller);
                    frame.setVisible(false);
                } else if(comboBox1.getSelectedItem().toString().equals("Partecipa a un team")){
                    PartecipaTeam PartecipaTeamGUI = new PartecipaTeam(frame,controller);
                    frame.setVisible(false);
                } else if(comboBox1.getSelectedItem().toString().equals("Accedi a un team")){
                    AccediTeam AccediTeamGUI = new AccediTeam(frame,controller);
                    frame.setVisible(false);
                } else if(comboBox1.getSelectedItem().toString().equals("Visualizza la classifica di un Hackathon")){
                    VisualizzazioneClassifica VisualizzazioneClassificaGUI = new VisualizzazioneClassifica(frame,emailConcorrente,controller);
                    frame.setVisible(false);
                }
            }
        });
    }
}
