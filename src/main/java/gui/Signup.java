package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class Signup {
    public JFrame frame;
    private JPanel panel1;
    private JLabel label1;
    private JTextField fieldNome;
    private JTextField fieldCognome;
    private JTextField fieldEmail;
    private JPasswordField fieldPassword;
    private JButton vButton;
    private JButton xButton;

    public Signup(JFrame frameChiamante, Controller controller) {
        frame= new JFrame("Signup");
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

        vButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.metodoSignup(frame, fieldNome.getText(), fieldCognome.getText(), fieldEmail.getText(), fieldPassword.getPassword());
                // sovrascrizione della password (per renderla inaccessibile in modo non autorizzato)
                Arrays.fill(fieldPassword.getPassword(), '\0');
            }
        });

        xButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fieldNome.setText("");
                fieldCognome.setText("");
                fieldEmail.setText("");
                fieldPassword.setText("");
            }
        });
    }
}