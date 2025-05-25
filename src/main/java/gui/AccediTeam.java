package gui;

import controller.Controller;
import model.Team;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AccediTeam {
    private JPanel panel1;
    public JFrame frame;
    private JPasswordField passwordField1;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JButton vButton;
    private JButton xButton;

    public AccediTeam(JFrame frameChiamante, Controller controller) {
        frame = new JFrame("Accedi Team");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // Primo comboBox Hackathon con default
        List<String> hackathonList = new ArrayList<>();
        hackathonList.add("Seleziona");
        hackathonList.addAll(controller.getNomeHackathon());
        comboBox1.setModel(new DefaultComboBoxModel<>(hackathonList.toArray(new String[0])));

        comboBox1.addActionListener(e -> {
            String selectedHackathon = (String) comboBox1.getSelectedItem();
            // Evita di aggiornare se è selezionato "Seleziona"
            if (selectedHackathon != null && !selectedHackathon.equals("Seleziona")) {
                // Secondo comboBox Team con default
                List<String> teamList = new ArrayList<>();
                teamList.add("Seleziona");
                teamList.addAll(controller.getListaNomiTeamByHackathon(selectedHackathon));
                comboBox2.setModel(new DefaultComboBoxModel<>(teamList.toArray(new String[0])));
            } else {
                // Reset comboBox2 se non è selezionato un hackathon valido
                comboBox2.setModel(new DefaultComboBoxModel<>(new String[] {"Seleziona"}));
            }
        });

        vButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Team team = controller.getTeamByNameAndHackathon(comboBox1.getSelectedItem().toString(), comboBox2.getSelectedItem().toString());
                if(!(comboBox1.getSelectedItem().toString().equals("Seleziona") || comboBox1.getSelectedItem().toString().equals("Seleziona") || passwordField1.getPassword().length == 0)) {
                    PaginaTeam PaginaTeamGUI = new PaginaTeam(frame,team,controller);
                    frame.setVisible(false);
                } else {
                    PaginaFallimento PaginaFallimentoGUI = new PaginaFallimento(frame,"Accesso al Team",controller);
                    frame.setVisible(false);
                }
            }
        });

        xButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AccediTeam AccediTeamGUI = new AccediTeam(frame,controller);
                frame.setVisible(false);
            }
        });
    }
}
