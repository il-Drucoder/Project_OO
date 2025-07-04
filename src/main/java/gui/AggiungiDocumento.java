package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class AggiungiDocumento extends JFrame {
    public JFrame frame;
    private JPanel panel1;
    private JTextField filePercorsoFile;
    private JButton browseButton;
    private JButton okButton;
    private JButton cancelButton;
    private File fileSelezionato;

    public AggiungiDocumento(JFrame frameChiamante, String nomeTeam, String titoloHackathon, Controller controller) {
        frame = new JFrame("Aggiungi documento");
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
        filePercorsoFile.setEditable(false);
        // pulsante "Sfoglia documenti"
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFile();
            }
        });

        // pulsante "Carica documento"
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.metodoAggiungiDocumento(frame, fileSelezionato, nomeTeam, titoloHackathon);
                filePercorsoFile.setText("");
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
        filePercorsoFile.setText("");
    }

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            fileSelezionato = fileChooser.getSelectedFile();
            filePercorsoFile.setText(fileSelezionato.getAbsolutePath());
        }
    }
}
