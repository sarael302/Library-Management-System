package model;

import exceptions.RetourNotFoundException;
import exceptions.CSVFileException;
import exceptions.EmpruntNotFoundException;
import java.util.List;

public interface RetourModelInterface {

    void ajouterRetour(Retour e);
    void supprimerRetour(int id) throws RetourNotFoundException;
    void listerRetours();
    void sauvegraderCSV() throws CSVFileException;
    Retour rechercherParID(int id) throws RetourNotFoundException;
    void lireCSV() throws CSVFileException, NumberFormatException;
    List<Retour> getListe();
    void calculerPenalite(Retour retour, EmpruntModel empruntModel) throws EmpruntNotFoundException;
}
