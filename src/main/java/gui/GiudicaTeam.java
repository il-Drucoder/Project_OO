package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GiudicaTeam {
    private static final String SDEFAULT = "Seleziona";
    private static final JFrame frame = new JFrame("Giudica team assegnati");
    private JPanel panel1;
    private JComboBox<String> comboBoxTitoloHackathon;
    private JComboBox<String> comboBoxNomeTeam;
    private JButton okButton;
    private JButton cancelButton;

    public GiudicaTeam(JFrame frameChiamante, String emailGiudice , Controller controller) {
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
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
        hackathonList.add(SDEFAULT);
        hackathonList.addAll(controller.getListaTitoliHackathonAssegnatiToGiudice(emailGiudice));
        comboBoxTitoloHackathon.setModel(new DefaultComboBoxModel<>(hackathonList.toArray(new String[0])));

        // secondo comboBox Team solo default
        comboBoxNomeTeam.addItem(SDEFAULT);

        comboBoxTitoloHackathon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedHackathon = (String) comboBoxTitoloHackathon.getSelectedItem();
                // evita di aggiornare se è selezionato SDEFAULT
                if (selectedHackathon != null && !selectedHackathon.equals(SDEFAULT)) {
                    // secondo comboBox Team con default
                    List<String> teamList = new ArrayList<>();
                    teamList.add(SDEFAULT);
                    teamList.addAll(controller.getListaNomiTeamGiudicabiliByGiudiceAndHackathon(emailGiudice, selectedHackathon));
                    comboBoxNomeTeam.setModel(new DefaultComboBoxModel<>(teamList.toArray(new String[0])));
                } else {
                    // reset comboBoxNomeTeam se non è selezionato un Hackathon valido
                    comboBoxNomeTeam.setModel(new DefaultComboBoxModel<>(new String[]{SDEFAULT}));
                }
            }
        });

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String hackathonSelezionato = Objects.toString(comboBoxTitoloHackathon.getSelectedItem(), SDEFAULT);
                String teamSelezionato = Objects.toString(comboBoxNomeTeam.getSelectedItem(), SDEFAULT);
                controller.metodoVisionaAndOrGiudicaTeam(frame, hackathonSelezionato, teamSelezionato, emailGiudice);
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
