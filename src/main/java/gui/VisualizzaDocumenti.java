package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class VisualizzaDocumenti {
    private static JFrame frame;
    private JPanel panel1;
    private JComboBox<String> comboBoxDocumento;
    private JButton okButton;
    private JButton cancelButton;

    public VisualizzaDocumenti(JFrame frameChiamante, String nomeTeam, String titoloHackathon, String emailUtente, Controller controller) {
        new JFrame("Visualizza Documenti");
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

        List<String> docList = new ArrayList<>();
        docList.add("Seleziona");
        docList.addAll(controller.getListaDocumentiByTeam(nomeTeam, titoloHackathon));
        comboBoxDocumento.setModel(new DefaultComboBoxModel<>(docList.toArray(new String[0])));

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedFile = (String) comboBoxDocumento.getSelectedItem();
                controller.metodoVisualizzaDocumenti(frame, selectedFile, nomeTeam, titoloHackathon, emailUtente);
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
        comboBoxDocumento.setSelectedIndex(0);
    }
}
