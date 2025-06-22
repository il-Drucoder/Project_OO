package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreaTeam {
    public JFrame frame;
    private JPanel panel1;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JComboBox comboBoxTitoloHackathon;
    private JTextField fieldNomeTeam;
    private JButton vButton;
    private JButton xButton;
    private JPasswordField fieldPasswordTeam;

    public CreaTeam(JFrame frameChiamante, String emailConcorrente, Controller controller) {
        frame = new JFrame("Crea Team");
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

        // Primo comboBox Hackathon con default
        List<String> hackathonList = new ArrayList<>();
        hackathonList.add("Seleziona");
        hackathonList.addAll(controller.getListaTitoliHackathonInCorso());
        comboBoxTitoloHackathon.setModel(new DefaultComboBoxModel<>(hackathonList.toArray(new String[0])));

        vButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.metodoCreaTeam(frame, fieldNomeTeam.getText(), fieldPasswordTeam.getPassword(), comboBoxTitoloHackathon.getSelectedItem().toString(), emailConcorrente);
                // sovrascrizione della password (per renderla inaccessibile in modo non autorizzato)
                Arrays.fill(fieldPasswordTeam.getPassword(), '\0');
            }
        });

        xButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comboBoxTitoloHackathon.setSelectedIndex(0);
                fieldNomeTeam.setText("");
                fieldPasswordTeam.setText("");
            }
        });
    }
}