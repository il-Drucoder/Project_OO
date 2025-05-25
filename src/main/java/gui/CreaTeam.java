package gui;

import controller.Controller;
import model.Team;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CreaTeam {
    private JPanel panel1;
    public JFrame frame;
    private JLabel label1;
    private JLabel nome;
    private JComboBox comboBox1;
    private JTextField textField1;
    private JButton vButton;
    private JButton xButton;
    private JPasswordField passwordField1;
    private JButton OKButton;

    public CreaTeam(JFrame frameChiamante, String concorrente, Controller controller) {
        frame = new JFrame("Crea Team");
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
                if(!(textField1.getText().isEmpty() || passwordField1.getPassword().length == 0 || comboBox1.getSelectedItem().toString().equals("Seleziona"))) {
                    Team team = new Team(textField1.getText(), passwordField1.getPassword().toString(), controller.getHackathonByName(comboBox1.getSelectedItem().toString()), controller.getConcorrenteByEmail(concorrente));
                    controller.addTeam(team);
                    PaginaSuccesso PaginaSuccessoGUI = new PaginaSuccesso(frame,"Creazione Team avvenuta",controller);
                    frame.setVisible(false);
                } else {
                    PaginaFallimento PaginaFallimentoGUI = new PaginaFallimento(frame,"Creazione Team",controller);
                    frame.setVisible(false);
                }
            }
        });

        xButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreaTeam CreaTeamGUI = new CreaTeam(frame,concorrente,controller);
                frame.setVisible(false);
            }
        });
    }
}