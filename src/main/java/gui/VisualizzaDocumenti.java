package gui;

import controller.Controller;
import model.Team;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VisualizzaDocumenti {
    private JPanel panel1;
    public JFrame frame;
    private JComboBox comboBox1;
    private JButton vButton;
    private JButton xButton;

    public VisualizzaDocumenti(JFrame frameChiamante, Team team, Controller controller) {
        frame = new JFrame("Visualizza Documenti");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        List<String> docList = new ArrayList<>();
        docList.add("Seleziona");
        docList.addAll(controller.getListaDocumentiPerTeam(team));
        comboBox1.setModel(new DefaultComboBoxModel<>(docList.toArray(new String[0])));

        xButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VisualizzaDocumenti VisualizzaDocumentiGUI = new VisualizzaDocumenti(frame,team,controller);
                frame.setVisible(false);
            }
        });

        vButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedFile = (String) comboBox1.getSelectedItem();

                if (selectedFile != null && !selectedFile.equals("Seleziona")) {
                    // Percorso del file (modifica questo path base secondo la tua logica)
                    File file = new File(selectedFile);

                    if (file.exists()) {
                        try {
                            // Apri il file in Notepad
                            Runtime.getRuntime().exec(new String[] { "xdg-open", file.getAbsolutePath() });
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(frame, "Errore nell'aprire il file:\n" + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Il file selezionato non esiste:\n" + file.getAbsolutePath(), "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Seleziona un documento valido", "Attenzione", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

    }
}
