package gui;

import  controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaginaHackathon {
    public JFrame frame;
    private JPanel panel1;
    private JLabel labelNomeHackathon;
    private JLabel labelStatoGara;
    private JLabel labelInizioIscrizioni;
    private JLabel labelFineIscrizioni;
    private JLabel labelDataInizio;
    private JLabel labelDataFine;
    private JLabel labelNumIscritti;
    private JLabel labelNumMaxIscritti;
    private JLabel labelDimMaxTeam;
    private JLabel labelIndirizzoSede;
    private JLabel labelDescrizioneProblema;
    private JTextArea textAreaDescrizione;
    private JPanel panelUtente;
    private JButton joinWithATeamButton;
    private JButton joinWithNewTeamButton;
    private JPanel panelGiudice;
    private JTextArea textAreaInserimentoDescrizione;
    private JButton confirmButton;
    private JButton judgeTeamButton;
    private JButton addJudgeButton;
    private JButton viewRankButton;

    public PaginaHackathon(JFrame frameChiamante, String emailUtente, String titoloHackathon, Controller controller) {
        frame = new JFrame("Hackathon");
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

        labelNomeHackathon.setText("Titolo: " + titoloHackathon);
        labelStatoGara.setText("Stato gara: " + controller.getHackathonByTitolo(titoloHackathon).statoGara());
        labelInizioIscrizioni.setText("Inizio iscrizioni: " + controller.getHackathonByTitolo(titoloHackathon).getInizioIscrizioni());
        labelFineIscrizioni.setText("Fine iscrizioni: " + controller.getHackathonByTitolo(titoloHackathon).getFineIscrizioni());
        labelDataInizio.setText("Inizio gara: " + controller.getHackathonByTitolo(titoloHackathon).getDataInizio());
        labelDataFine.setText("Fine gara: " + controller.getHackathonByTitolo(titoloHackathon).getDataFine());
        labelNumIscritti.setText("Numero di iscritti: " + controller.getHackathonByTitolo(titoloHackathon).getNumIscritti());
        labelNumMaxIscritti.setText("Numero massimo di partecipanti: " + controller.getHackathonByTitolo(titoloHackathon).getNumMaxIscritti());
        labelDimMaxTeam.setText("Numero massimo di partecipanti per singolo team: " + controller.getHackathonByTitolo(titoloHackathon).getDimMaxTeam());
        labelIndirizzoSede.setText("Indirizzo sede gara: " + controller.getHackathonByTitolo(titoloHackathon).getIndirizzoSede(controller.getUtentePiattaformaByEmail(emailUtente)));
        labelDescrizioneProblema.setText("Descrizione problema: ");
        textAreaDescrizione.setText(controller.getHackathonByTitolo(titoloHackathon).getDescrizioneProblema());
        textAreaDescrizione.setEditable(false);

        // verifica possibilità di visualizzare la descrizione del problema (visibile in qualsiasi momento ma solo dai giudici e organizzatore)
        // oppure (non visibile nel periodo che va da "iscrizioni terminate" a prima dell'inizio della gara, ossia il periodo in cui viene inserita dai giudici)
        if (controller.isGiudice(emailUtente) || controller.isOrganizzatore(emailUtente)) {
            if (textAreaDescrizione.getText().isEmpty()) {
                textAreaDescrizione.setText("*ASSENTE*\nInserire la descrizione prima dell'inizio gara");
            }
        } else if (controller.getHackathonByTitolo(titoloHackathon).verificaStatoGara("Iscrizioni terminate. In attesa dell'inizio della gara")) {
            textAreaDescrizione.setText("Non ancora disponibile");
        }

        joinWithATeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PartecipaTeam PartecipaTeamGUI = new PartecipaTeam(frame, emailUtente, controller);
                frame.setVisible(false);
            }
        });

        joinWithNewTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreaTeam CreaTeamGUI = new CreaTeam(frame, emailUtente, controller);
                frame.setVisible(false);
            }
        });

        // verifica possibilità di iscrizione all'Hackathon (durante il periodo di iscrizioni e solo per i concorrenti)
        if (!(controller.getHackathonByTitolo(titoloHackathon).statoGara().equals("Iscrizioni aperte") && emailUtente.toLowerCase().contains("@concorrente.com"))) {
            panelUtente.setVisible(false);
        }

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.metodoSetDescrizioneProblemaHackathon(frame, textAreaInserimentoDescrizione.getText(), titoloHackathon, emailUtente);
                textAreaInserimentoDescrizione.setText("");
                // ritorniamo alla pagina di seleziona documento (pagina precedente a questa)
                frameChiamante.setVisible(true);
                frame.dispose();
            }
        });

        judgeTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GiudicaTeam GiudicaTeamGUI = new GiudicaTeam(frame, emailUtente, controller);
                frame.setVisible(false);
            }
        });

        // verifica possibilità di fare azioni solo per i giudici assegnati (inserire descrizione problema Hackathon, giudicare team)
        if (controller.isGiudice(emailUtente)) {
            if (controller.getListaTitoliHackathonAssegnatiToGiudice(emailUtente).contains(titoloHackathon)) {
                // inserire/reinserire la descrizione del problema (dopo la fine delle iscrizioni, prima dell'inizio della gara)
                if (!controller.getHackathonByTitolo(titoloHackathon).verificaStatoGara("Iscrizioni terminate. In attesa dell'inizio della gara")) {
                    panelGiudice.setVisible(false);
                }
                // giudicare team (aggiungere commenti ai documenti, durante la fase di gara oppure assegnare i voti ai team, subito dopo la fine della gara)
                if (!(controller.getHackathonByTitolo(titoloHackathon).verificaStatoGara("In corso") || controller.getHackathonByTitolo(titoloHackathon).verificaStatoGara("Terminata"))) {
                    judgeTeamButton.setVisible(false);
                }
            } else {
                panelGiudice.setVisible(false);
                judgeTeamButton.setVisible(false);
            }
        } else {
            panelGiudice.setVisible(false);
            judgeTeamButton.setVisible(false);
        }

        addJudgeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConvocaGiudice ConvocaGiudiceGUI = new ConvocaGiudice(frame, emailUtente, controller);
                frame.setVisible(false);
            }
        });

        // verifica possibilità di convocare giudice (prima dell'inizio delle iscrizioni e solo per l'organizzatore)
        if (!(controller.getHackathonByTitolo(titoloHackathon).statoGara().equals("Iscrizioni non ancora aperte") && emailUtente.toLowerCase().contains("@organizzatore.com"))) {
            addJudgeButton.setVisible(false);
        }

        viewRankButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.metodoVisualizzaClassifica(frame, titoloHackathon, emailUtente);
            }
        });

        // verifica possibilità di visualizzazione classifica (solo in fase di gara conclusa e valutata)
        if (!controller.getHackathonByTitolo(titoloHackathon).verificaStatoGara("Valutata")) {
            viewRankButton.setVisible(false);
        }
    }
}
