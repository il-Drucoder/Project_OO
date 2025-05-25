package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaginaFallimento {
    private JPanel panel1;
    private JFrame frame;
    private JButton tornaAllaHomePageButton;
    private JLabel oggettoLabel;

    public PaginaFallimento(JFrame frameChiamante, String oggetto, Controller controller) {
        frame = new JFrame("Pagina Fallimento");
        frame.setContentPane(panel1);
        oggettoLabel.setText(oggetto + " non riuscita!!");
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
