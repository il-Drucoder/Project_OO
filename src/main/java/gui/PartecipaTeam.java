package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PartecipaTeam {
    private JPanel panel1;
    private JPasswordField passwordField1;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JButton vButton;
    private JButton xButton;
    public JFrame frame;

    public PartecipaTeam(JFrame frameChiamante, Controller controller) {
        frame = new JFrame("Partecipa Team");
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
                if(!(comboBox1.getSelectedItem().toString().equals("Seleziona") || comboBox2.getSelectedItem().toString().equals("Seleziona") || passwordField1.getPassword().length == 0)) {
                    PaginaSuccesso PaginaSuccessoGUI = new PaginaSuccesso(frame,"Partecipazione al Team " + comboBox2.getSelectedItem().toString() + " avvenuta",controller);
                    frame.setVisible(false);
                } else {
                    PaginaFallimento PaginaFallimentoGUI = new PaginaFallimento(frame,"Partecipazione al Team",controller);
                    frame.setVisible(false);
                }
            }
        });

        xButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PartecipaTeam PartecipaTeamGUI = new PartecipaTeam(frame,controller);
                frame.setVisible(false);
            }
        });
    }
}
