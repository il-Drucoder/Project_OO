package gui;

import controller.Controller;
import model.Team;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class AggiungiDocumento extends JFrame {
    private JPanel panel1;
    private JTextField filePathField;
    private JButton browseButton;
    private JButton vButton;
    private JButton xButton;
    private JButton uploadButton;
    private File selectedFile;
    public JFrame frame;

    public AggiungiDocumento(JFrame frameChiamante, Team team, Controller controller) {
        frame = new JFrame("Aggiunta documento");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // Campo testo per il path del file
        filePathField.setEditable(false);

        // Pulsante "Sfoglia"
        browseButton.addActionListener(e -> chooseFile());

        // Pulsante "Carica"
        vButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedFile != null) {
                    controller.aggiungiDocumentoInTeam(controller.setFileInDocumenti(selectedFile),team);
                    PaginaSuccesso PaginaSuccessoGUI = new PaginaSuccesso(frame,"File " + selectedFile.getName() + " aggiunto ",controller);
                    frame.setVisible(false);
                } else {
                    PaginaFallimento PaginaFallimentoGUI = new PaginaFallimento(frame,"Operazione inserimento file",controller);
                }
            }
        });

        xButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AggiungiDocumento AggiungiDocumentoGUI = new AggiungiDocumento(frame,team,controller);
                frame.setVisible(false);
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
