package model;

import exceptions.EmpruntNotFoundException;
import exceptions.CSVFileException;
import java.time.LocalDate;
import java.util.List;

public interface EmpruntModelInterface {

    void ajouterEmprunt(int utilisateurId, int livreId, int nombreJours);
    void supprimerEmprunt(int id) throws EmpruntNotFoundException;
    void prolongerEmprunt(int id, int nombreJours) throws EmpruntNotFoundException;
    void listerEmprunts();
    void sauvegraderCSV() throws CSVFileException;
    Emprunt rechercherParID(int id) throws EmpruntNotFoundException;
    void lireCSV() throws CSVFileException;
    boolean empruntExiste(int empruntId);
    List<Emprunt> getListe();
    LocalDate getDateEmprunt(int empruntId) throws EmpruntNotFoundException;
}