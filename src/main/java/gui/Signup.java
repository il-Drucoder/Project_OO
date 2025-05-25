package gui;

import controller.Controller;
import model.Concorrente;
import model.Giudice;
import model.Organizzatore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Signup {
    public JFrame frame;
    private JLabel label1;
    private JPanel panel1;
    private JTextField textField1;
    private JButton xButton;
    private JButton vButton;
    private JPasswordField passwordField1;
    private JTextField textField2;
    private JTextField textField3;

    public Signup(JFrame frameChiamante, Controller controller) {
        frame= new JFrame("Signup");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        vButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!(textField1.getText().isEmpty() || textField2.getText().isEmpty() || textField3.getText().isEmpty() || passwordField1.getPassword().length == 0)) {
                    if (textField3.getText().toLowerCase().contains("@c")) { //concorrente.com
                        Concorrente concorrente = new Concorrente(textField1.getText(),textField2.getText(),textField3.getText());
                        controller.addConcorrente(concorrente);
                        BenvenutoConcorrente BenvenutoConcorrenteGUI = new BenvenutoConcorrente(frame,textField3.getText(), controller);
                        frame.setVisible(false);
                    } else if (textField3.getText().toLowerCase().contains("@giudice.com")) {
                        //                    Giudice giudice = new Giudice(textField1.getText(),textField2.getText(),textField3.getText());
                        //                    controller.addGiudice(giudice);
                        //                    BenvenutoGiudice BenvenutoGiudiceGUI = new BenvenutoGiudice(frame, textField1);
                        //                    frame.setVisible(false);
                    } else if (textField3.getText().toLowerCase().contains("@organizzatore.com")) {
                        //                    Organizzatore organizzatore = new Organizzatore(textField1.getText(),textField2.getText(),textField3.getText());
                        //                    controller.addOrganizzatore(organizzatore);
                        //                    BenvenutoOrganizzatore BenvenutoOrganizzatoreGUI = new BenvenutoOrganizzatore(frame, textField1);
                        //                    frame.setVisible(false);
                    } else {
                        PaginaFallimento PaginaFallimentoGUI = new PaginaFallimento(frame, "Operazione di Signup", controller);
                        frame.setVisible(false);
                    }
                }
            }
        });

        xButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Signup SignupGUI = new Signup(frame,controller);
                frame.setVisible(false);
            }
        });
    }
}