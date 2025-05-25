package gui;

import controller.Controller;
import model.UtentePiattaforma;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {
    public JFrame frame;
    private JPanel panel1;
    private JLabel label1;
    private JTextField textField1;
    private JButton vButton;
    private JButton xButton;
    private JPasswordField passwordField1;

    public Login(JFrame frameChiamante, Controller controller) {
        frame= new JFrame("Login");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        vButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!(textField1.getText().isEmpty() || passwordField1.getPassword().length == 0)) {
                    UtentePiattaforma utentePiattaforma = controller.getUtentePiattaformaByEmail(textField1.getText());
                    if(utentePiattaforma == null) {
                        PaginaFallimento PaginaFallimentoGUI = new PaginaFallimento(frame,"Utente non registrato. Operazione di Login",controller);
                        frame.setVisible(false);
                    } else {
                        String emailUtentePiattaforma = utentePiattaforma.getEmail();

                        if(textField1.getText().toLowerCase().contains("@concorrente.com")) {
                            BenvenutoConcorrente BenvenutoConcorrenteGUI = new BenvenutoConcorrente(frame,emailUtentePiattaforma,controller);
                            frame.setVisible(false);
                        } else if(textField1.getText().toLowerCase().contains("@giudice.com")) {
//                    BenvenutoGiudice BenvenutoGiudiceGUI = new BenvenutoGiudice(frame,nomeUtentePiattaforma,controller);
//                    frame.setVisible(false);
                        } else if(textField1.getText().toLowerCase().contains("@organizzatore.com")) {
//                    BenvenutoOrganizzatore BenvenutoOrganizzatoreGUI = new BenvenutoOrganizzatore(frame,nomeUtentePiattaforma,controller);
//                    frame.setVisible(false);
                        } else {
                            PaginaFallimento PaginaFallimentoGUI = new PaginaFallimento(frame,"Operazione di Login",controller);
                            frame.setVisible(false);
                        }
                    }
                }
            }
        });

        xButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login LoginGUI = new Login(frame,controller);
                frame.setVisible(false);
            }
        });
    }
}