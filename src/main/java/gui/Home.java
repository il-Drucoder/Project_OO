package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Home {
    private JPanel mainPanel;
    private JPanel panel1;
    private JButton Login;
    private JButton Signup;
    public static JFrame frameHome;

    private static Controller controller;

    public static void main(String[] args) {
        try {
            frameHome = new JFrame("Home");
            frameHome.setContentPane(new Home().panel1);
            frameHome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frameHome.pack();

            controller = new Controller();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.printf("Errore durante inizializzazione del controller \n");
        }

    }

    public Home() {
        frameHome.setVisible(true);

        Login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login LoginGUI = new Login(frameHome, controller);
                frameHome.setVisible(false);
            }
        });

        Signup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Signup SignupGUI = new Signup(frameHome, controller);
                frameHome.setVisible(false);
            }
        });
    }
}
