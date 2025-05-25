package gui;

import controller.Controller;
import model.Hackathon;
import model.UtentePiattaforma;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class VisualizzazioneClassifica {
    private JPanel panel1;
    private JComboBox comboBox1;
    private JButton vButton;
    private JButton xButton;
    public JFrame frame;

    public VisualizzazioneClassifica(JFrame frameChiamante, String emailUtente, Controller controller) {
        frame = new JFrame("Classifica Hackathon");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // Primo comboBox Hackathon con default
        List<String> hackathonList = new ArrayList<>();
        hackathonList.add("Seleziona");
        hackathonList.addAll(controller.getNomeHackathon());
        comboBox1.setModel(new DefaultComboBoxModel<>(hackathonList.toArray(new String[0])));

        vButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!(comboBox1.getSelectedItem().toString().equals("Seleziona"))) {
                    ClassificaHackathon ClassificaHackathonGUI = new ClassificaHackathon(frame,comboBox1.getSelectedItem().toString(),emailUtente,controller);
                    frame.setVisible(false);
                }
            }
        });

        xButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VisualizzazioneClassifica VisualizzazioneClassificaGUI = new VisualizzazioneClassifica(frame,emailUtente,controller);
                frame.setVisible(false);
            }
        });
    }

}
