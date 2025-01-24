package model;

import java.io.*;
import java.util.*;
import exceptions.UtilisateurNotFoundException;
import exceptions.CSVFileException;
import utils.FileHandler;

public class UtilisateurModel {

    private ArrayList<Utilisateur> liste = new ArrayList<>();
    private String csvFileName;
    private List<Utilisateur> utilisateurs;


    public UtilisateurModel() {
        utilisateurs = new ArrayList<>();
    }

    public UtilisateurModel(String csvFileName) throws CSVFileException {
        super();
        this.csvFileName = csvFileName;
        try {
            FileHandler.createFileIfNotExists(csvFileName, "Id;Nom;Email;NumTele");
        } catch (IOException e) {
            throw new CSVFileException("Erreur lors de la création du fichier CSV : " + csvFileName);
        }
    }

    public void ajouterUtilisateur(Utilisateur e) {
        // Find the highest existing ID in the list, or start with 1 if the list is empty
        int newId = liste.isEmpty() ? 1 : liste.stream().mapToInt(Utilisateur::getId).max().orElse(0) + 1;
        e.setId(newId);  // Set the new ID to the utilisateur object
        liste.add(e);     // Add the user to the list
    }


    public void supprimerUtilisateur(int id) throws UtilisateurNotFoundException {
        Utilisateur e = rechercherParId(id);
        if (e != null) {
            liste.remove(e);

            // Optionally, reassign IDs after deletion to make them contiguous
            for (int i = 0; i < liste.size(); i++) {
                liste.get(i).setId(i + 1); // Reassign IDs starting from 1
            }
        } else {
            throw new UtilisateurNotFoundException(id);  // Lancer exception si l'utilisateur n'est pas trouvé
        }
    }


    public void modifierUtilisateur(String nom, String email, String numTele, int id) throws UtilisateurNotFoundException {
        Utilisateur e = rechercherParId(id);
        if (e != null) {
            e.setNom(nom);
            e.setEmail(email);
            e.setNumTele(numTele);

        } else {
            throw new UtilisateurNotFoundException(id);  // Lancer exception si l'utilisateur n'est pas trouvé
        }
    }

    public void listerUtilisateurs() {
        System.out.println(liste);
    }


    public Utilisateur rechercherParId(int id) throws UtilisateurNotFoundException {
        Optional<Utilisateur> p = liste.stream().filter(e -> e.getId() == id).findFirst();
        return p.orElseThrow(() -> new UtilisateurNotFoundException(id));  // Lancer exception si l'utilisateur n'est pas trouvé
    }

    public void lireCSV() throws CSVFileException {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFileName))) {
            String header = br.readLine();
            // Read and ignore the header
            if (header == null || !header.equalsIgnoreCase("Id;Nom;Email;NumTele")) {
                throw new CSVFileException("Fichier CSV mal formé ou sans en-tête valide : " + csvFileName);
            }

            String line;
            while ((line = br.readLine()) != null) {
                try {
                    // Split the line and validate the number of fields
                    String[] words = line.split(";");
                    if (words.length != 4) {  // Expecting 4 fields: Id, Nom, Email, NumTele
                        System.err.println("Ligne invalide ignorée (mauvais format) : " + line);
                        continue;
                    }

                    // Parse fields
                    int id = Integer.parseInt(words[0].trim());
                    String nom = words[1].trim();
                    String email = words[2].trim();
                    String numTele = words[3].trim(); // Parsing the phone number

                    // Create and add Utilisateur object
                    Utilisateur e = new Utilisateur();
                    e.setId(id);
                    e.setNom(nom);
                    e.setEmail(email);
                    e.setNumTele(numTele);  // Assuming NumTele is an integer, if not, use String
                    liste.add(e);
                } catch (NumberFormatException e) {
                    System.err.println("Erreur de format numérique dans la ligne ignorée : " + line);
                }
            }
        } catch (IOException e) {
            throw new CSVFileException("Erreur lors de la lecture du fichier CSV : " + csvFileName);
        }
    }


    public void sauvegraderCSV() throws CSVFileException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFileName))) {
            // Write the header
            writer.write("Id;Nom;Email;NumTele");
            writer.newLine();

            // Write the data
            for (Utilisateur utilisateur : liste) {
                writer.write(utilisateur.getId() + ";" + utilisateur.getNom() + ";" + utilisateur.getEmail() + ";" + utilisateur.getNumTele());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new CSVFileException("Erreur lors de la sauvegarde du fichier CSV : " + e.getMessage());
        }
    }

    public List<Utilisateur> getListe() {
        return liste;
    }

    // Méthode pour vérifier l'existence d'un utilisateur par son ID
    public boolean utilisateurExiste(int id) {
        return liste.stream().anyMatch(e -> e.getId() == id);
    }
}