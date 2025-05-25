package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaginaSuccesso {
    private JPanel panel1;
    private JFrame frame;
    private JButton tornaAllaHomePageButton;
    private JLabel oggettoAzioneLabel;

    public PaginaSuccesso(JFrame frameChiamante, String oggettoAzione, Controller controller) {
        frame = new JFrame("Pagina successo");
        frame.setContentPane(panel1);
        oggettoAzioneLabel.setText(oggettoAzione + " con successo!!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        tornaAllaHomePageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Home HomeGUI = new Home();
                frame.setVisible(false);
            }
        });
    }
}
