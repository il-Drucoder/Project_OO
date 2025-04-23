package model;

import java.io.File;
import java.util.Date;

public class Documento {
    // attributi
    private Date dataAggiornamento;
    private File file;

    // metodi
    // Costruttore
    public Documento(File file) {
        this.dataAggiornamento = new Date(); // la data di aggiornamento viene inserita in automatico appena viene creato il documento
        this.file = file;
    }

    public Date getDataAggiornamento() { return dataAggiornamento; }
    // setter privato utilizzabile solo tramite il metodo setFile
    private void setDataAggiornamento(Date dataAggiornamento) {
        this.dataAggiornamento = dataAggiornamento;
    }

    public File getFile() { return file; }
    // setFile fa automaticamente un setDataAggiornamento
    public void setFile(File file) {
        this.file = file;
        this.setDataAggiornamento(new Date()); // aggiorna alla data corrente
    }
}
