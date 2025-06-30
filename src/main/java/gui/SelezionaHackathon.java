package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SelezionaHackathon {
    public JFrame frame;
    private JPanel panel1;
    private JComboBox comboBoxTitoloHackathon;
    private JButton okButton;
    private JButton cancelButton;

    public SelezionaHackathon(JFrame frameChiamante, String emailUtente, List<String> listaTitoliHackathon, Controller controller) {
        frame = new JFrame("Ricerca Hackathon");
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

        // comboBox Hackathon con default
        List<String> hackathonList = new ArrayList<>();
        hackathonList.add("Seleziona");
        hackathonList.addAll(listaTitoliHackathon);
        comboBoxTitoloHackathon.setModel(new DefaultComboBoxModel<>(hackathonList.toArray(new String[0])));

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.metodoSelezionaHackathon(frame, comboBoxTitoloHackathon.getSelectedItem().toString(), emailUtente);
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
    }
}
