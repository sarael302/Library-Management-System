package model;

import exceptions.UtilisateurNotFoundException;
import exceptions.CSVFileException;
import java.util.List;

public interface UtilisateurModelInterface {

    void ajouterUtilisateur(Utilisateur e);
    void supprimerUtilisateur(int id) throws UtilisateurNotFoundException;
    void modifierUtilisateur(String nom, String email,int numTele, int id) throws UtilisateurNotFoundException;
    void listerUtilisateurs();
    void sauvegraderCSV() throws CSVFileException;
    Utilisateur rechercherParID(int id) throws UtilisateurNotFoundException;
    void lireCSV() throws CSVFileException, NumberFormatException;
    List<Utilisateur> getListe();
   
}
