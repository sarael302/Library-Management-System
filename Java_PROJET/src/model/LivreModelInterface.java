package model;

import exceptions.LivreNotFoundException;
import exceptions.CSVFileException;
import java.util.List;

public interface LivreModelInterface {

    void ajouterLivre(Livre e);
    void supprimerLivre(int id) throws LivreNotFoundException;
    void modifierLivre(String titre, String auteur, int anneePublication, String genre, int id) throws LivreNotFoundException;
    void listerLivres();
    void trierLivre();
    void sauvegraderCSV() throws CSVFileException;
    Livre rechercherParID(int id) throws LivreNotFoundException;
    void lireCSV() throws CSVFileException, NumberFormatException;
    List<Livre> getListe();
    boolean livreExiste(int id);
   
}
