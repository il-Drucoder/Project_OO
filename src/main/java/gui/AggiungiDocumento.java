package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class AggiungiDocumento extends JFrame {
    public JFrame frame;
    private JPanel panel1;
    private JTextField filePathField;
    private JButton browseButton;
    private JButton vButton;
    private JButton xButton;
    private File selectedFile;

    public AggiungiDocumento(JFrame frameChiamante, String nomeTeam, String titoloHackathon, Controller controller) {
        frame = new JFrame("Aggiunta documento");
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

        // campo testo per il path del file
        filePathField.setEditable(false);
        // pulsante "Sfoglia"
        browseButton.addActionListener(e -> chooseFile());
        // pulsante "Carica"
        vButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.metodoAggiungiDocumento(frame, selectedFile, nomeTeam, titoloHackathon);
            }
        });

        xButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filePathField.setText("");
            }
        });
    }

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }
}
