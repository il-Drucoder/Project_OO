package dao;

import model.Documento;
import model.Giudice;

import java.util.List;

public interface DocumentoDAO {
    void aggiungiDocumento(Documento documento);
    List<Documento> getTuttiDocumenti();
    void addCommento(Giudice giudice, Documento documento, String commento);
    List<String> getTuttiCommentiByDocumento(Documento documento);
}