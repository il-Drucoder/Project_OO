package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class Login {
    private static JFrame frame;
    private JPanel panel1;
    private JTextField fieldEmailUtente;
    private JPasswordField fieldPassword;
    private JButton okButton;
    private JButton cancelButton;

    public Login(JFrame frameChiamante, Controller controller) {
        new JFrame("Login");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                frameChiamante.setVisible(true);
                frame.dispose();
            }
        });

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.metodoLogin(frame, fieldEmailUtente.getText(), fieldPassword.getPassword());
                azzeraCampi();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                azzeraCampi();
            }
        });
    }

    private void azzeraCampi() {
        fieldEmailUtente.setText("");
        fieldPassword.setText("");
        // sovrascrizione della password (per renderla inaccessibile in modo non autorizzato)
        Arrays.fill(fieldPassword.getPassword(), '\0');
    }
}