package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PartecipaTeam {
    public JFrame frame;
    private JPanel panel1;
    private JComboBox comboBoxHackathon;
    private JComboBox comboBoxTeam;
    private JPasswordField fieldPasswordTeam;
    private JButton vButton;
    private JButton xButton;

    public PartecipaTeam(JFrame frameChiamante, String emailConcorrente, Controller controller) {
        frame = new JFrame("Partecipa Team");
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
        comboBoxHackathon.setModel(new DefaultComboBoxModel<>(hackathonList.toArray(new String[0])));

        comboBoxHackathon.addActionListener(e -> {
            String selectedHackathon = (String) comboBoxHackathon.getSelectedItem();
            // Evita di aggiornare se è selezionato "Seleziona"
            if (selectedHackathon != null && !selectedHackathon.equals("Seleziona")) {
                // Secondo comboBox Team con default
                List<String> teamList = new ArrayList<>();
                teamList.add("Seleziona");
                teamList.addAll(controller.getListaNomiTeamLiberiByHackathon(selectedHackathon));
                comboBoxTeam.setModel(new DefaultComboBoxModel<>(teamList.toArray(new String[0])));
            } else {
                // Reset comboBox Team se non è selezionato un Hackathon valido
                comboBoxTeam.setModel(new DefaultComboBoxModel<>(new String[] {"Seleziona"}));
            }
        });

        vButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.metodoPartecipaTeam(frame, emailConcorrente, comboBoxHackathon.getSelectedItem().toString(), comboBoxTeam.getSelectedItem().toString(), fieldPasswordTeam.getPassword());
                // sovrascrizione della password (per renderla inaccessibile in modo non autorizzato)
                Arrays.fill(fieldPasswordTeam.getPassword(), '\0');
            }
        });

        xButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comboBoxHackathon.setSelectedIndex(0);
                fieldPasswordTeam.setText("");
            }
        });
    }
}
