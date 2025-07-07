package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Home {
    private JPanel panel1;
    private JButton loginButton;
    private JButton signupButton;
    private static final JFrame frameHome = new JFrame("Home");

    private static Controller controller;

    public static void main(String[] args) {
        try {
            frameHome.setContentPane(new Home().panel1);
            frameHome.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frameHome.pack();

            controller = new Controller();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Home() {
        frameHome.setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login(frameHome, controller);
                frameHome.setVisible(false);
            }
        });

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Signup(frameHome, controller);
                frameHome.setVisible(false);
            }
        });
    }
}
