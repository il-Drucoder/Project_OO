package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// import for JXDatePicker
import org.jdesktop.swingx.JXDatePicker;

// import for dateToLocalDate
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class CreaHackathon {
    private static final JFrame frame = new JFrame("Crea Hackathon");
    private JPanel panel1;
    private JTextField fieldTitoloHackathon;
    private JXDatePicker datePickerInizioIscrizioni;
    private JXDatePicker datePickerDataInizio;
    private JXDatePicker datePickerDataFine;
    private JSpinner spinnerNumMaxIscritti;
    private JSpinner spinnerDimMaxTeam;
    private JTextField fieldIndirizzoSede;
    private JButton okButton;
    private JButton cancelButton;

    public CreaHackathon(JFrame frameChiamante, String emailOrganizzatore, Controller controller) {
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

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // conversione date da Date a LocalDate
                LocalDate inizioIscrizioni = dateToLocalDate(datePickerInizioIscrizioni.getDate());
                LocalDate dataInizio = dateToLocalDate(datePickerDataInizio.getDate());
                LocalDate dataFine = dateToLocalDate(datePickerDataFine.getDate());
                controller.metodoCreaHackathon(frame, fieldTitoloHackathon.getText(), dataInizio, dataFine, (int) spinnerNumMaxIscritti.getValue(), (int) spinnerDimMaxTeam.getValue(), inizioIscrizioni, fieldIndirizzoSede.getText(), emailOrganizzatore);
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
        fieldTitoloHackathon.setText("");
        datePickerInizioIscrizioni.setDate(null);
        datePickerDataInizio.setDate(null);
        datePickerDataFine.setDate(null);
        spinnerNumMaxIscritti.setValue(0);
        spinnerDimMaxTeam.setValue(0);
        fieldIndirizzoSede.setText("");
    }

    // conversione da Date a LocalDate
    public LocalDate dateToLocalDate(Date selectedDate) {
        if (selectedDate == null) {
            return null;
        }
        return selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
