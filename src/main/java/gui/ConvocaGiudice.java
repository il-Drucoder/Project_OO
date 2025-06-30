package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ConvocaGiudice {
    public JFrame frame;
    private JPanel panel1;
    private JComboBox comboBoxGiudice;
    private JComboBox comboBoxHackathon;
    private JButton okButton;
    private JButton cancelButton;

    public ConvocaGiudice(JFrame frameChiamante, String emailOrganizzatore, Controller controller) {
        frame = new JFrame("Convoca giudice");
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
        hackathonList.addAll(controller.getListaTitoliHackathonAnnunciatiByCreatore(emailOrganizzatore));
        comboBoxHackathon.setModel(new DefaultComboBoxModel<>(hackathonList.toArray(new String[0])));

        // secondo comboBox Giudice con default
        // utilizzo di liste parallele per non perdere l'associazione nome-email
        List<String> giudiceNominativoList = new ArrayList<>();
        List<String> giudiceEmailList = new ArrayList<>();
        giudiceNominativoList.add("Seleziona");
        giudiceEmailList.add("");
        giudiceNominativoList.addAll(controller.getListaNominativiGiudici());
        giudiceEmailList.addAll(controller.getListaEmailGiudici());
        comboBoxGiudice.setModel(new DefaultComboBoxModel<>(giudiceNominativoList.toArray(new String[0])));

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ricavo l'indice del nominativo selezionato, per accedere alla rispettiva email
                int selectedIndex = comboBoxGiudice.getSelectedIndex();
                controller.metodoConvocaGiudice(frame, comboBoxHackathon.getSelectedItem().toString(), giudiceEmailList.get(selectedIndex).toString(), emailOrganizzatore);
                azzeraCampi();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                azzeraCampi();;
            }
        });
    }

    private void azzeraCampi() {
        comboBoxHackathon.setSelectedIndex(0);
        comboBoxGiudice.setSelectedIndex(0);
    }
}
