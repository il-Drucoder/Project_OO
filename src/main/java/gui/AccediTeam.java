package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AccediTeam {
    public JFrame frame;
    private JPanel panel1;
    private JComboBox comboBoxTitoloHackathon;
    private JComboBox comboBoxNomeTeam;
    private JButton vButton;
    private JButton xButton;

    public AccediTeam(JFrame frameChiamante, String emailConcorrente , Controller controller) {
        frame = new JFrame("Accedi Team");
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
        hackathonList.addAll(controller.getListaTitoliHackathon());
        comboBoxTitoloHackathon.setModel(new DefaultComboBoxModel<>(hackathonList.toArray(new String[0])));

        comboBoxTitoloHackathon.addActionListener(e -> {
            String selectedHackathon = (String) comboBoxTitoloHackathon.getSelectedItem();
            // Evita di aggiornare se è selezionato "Seleziona"
            if (selectedHackathon != null && !selectedHackathon.equals("Seleziona")) {
                // Secondo comboBox Team con default
                List<String> teamList = new ArrayList<>();
                teamList.add("Seleziona");
                String nomeTeam = controller.getNomeTeamByHackathonAndConcorrente(selectedHackathon, emailConcorrente);
                if (nomeTeam != null) {
                    teamList.add(nomeTeam);
                }
                comboBoxNomeTeam.setModel(new DefaultComboBoxModel<>(teamList.toArray(new String[0])));
            } else {
                // Reset comboBoxNomeTeam se non è selezionato un Hackathon valido
                comboBoxNomeTeam.setModel(new DefaultComboBoxModel<>(new String[] {"Seleziona"}));
            }
        });

        vButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titoloHackathon = comboBoxTitoloHackathon.getSelectedItem().toString();
                String nomeTeam = comboBoxNomeTeam.getSelectedItem().toString();
                controller.metodoAccediTeam(frame, titoloHackathon, nomeTeam, emailConcorrente);
            }
        });

        xButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comboBoxTitoloHackathon.setSelectedIndex(0);
            }
        });
    }
}
