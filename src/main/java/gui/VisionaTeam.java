package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class VisionaTeam {
    public JFrame frame;
    private JPanel panel1;
    private JComboBox comboBoxTitoloHackathon;
    private JComboBox comboBoxNomeTeam;
    private JButton okButton;
    private JButton cancelButton;

    public VisionaTeam(JFrame frameChiamante, String emailGiudice , Controller controller) {
        frame = new JFrame("Visiona team giudicati");
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

        // primo comboBox Hackathon con default
        List<String> hackathonList = new ArrayList<>();
        hackathonList.add("Seleziona");
        hackathonList.addAll(controller.getListaTitoliHackathonAssegnatiToGiudice(emailGiudice));
        comboBoxTitoloHackathon.setModel(new DefaultComboBoxModel<>(hackathonList.toArray(new String[0])));

        // secondo comboBox Team solo default
        comboBoxNomeTeam.addItem("Seleziona");

        comboBoxTitoloHackathon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedHackathon = (String) comboBoxTitoloHackathon.getSelectedItem();
                // evita di aggiornare se è selezionato "Seleziona"
                if (selectedHackathon != null && !selectedHackathon.equals("Seleziona")) {
                    // secondo comboBox Team con default
                    List<String> teamList = new ArrayList<>();
                    teamList.add("Seleziona");
                    teamList.addAll(controller.getListaNomiTeamGiudicatiByGiudiceAndHackathon(emailGiudice, selectedHackathon));
                    comboBoxNomeTeam.setModel(new DefaultComboBoxModel<>(teamList.toArray(new String[0])));
                } else {
                    // reset comboBoxNomeTeam se non è selezionato un Hackathon valido
                    comboBoxNomeTeam.setModel(new DefaultComboBoxModel<>(new String[]{"Seleziona"}));
                }
            }
        });

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.metodoVisionaAndOrGiudicaTeam(frame, comboBoxTitoloHackathon.getSelectedItem().toString(), comboBoxNomeTeam.getSelectedItem().toString(), emailGiudice);
                azzeraCampi();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            azzeraCampi();
          }
        });
    }

    private void azzeraCampi() {
        comboBoxTitoloHackathon.setSelectedIndex(0);
        comboBoxNomeTeam.setSelectedIndex(0);
    }
}
