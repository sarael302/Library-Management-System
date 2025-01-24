package model;

import java.io.*;
import java.util.*;
import exceptions.LivreNotFoundException;
import exceptions.CSVFileException;
import utils.FileHandler;

public class LivreModel {

    private ArrayList<Livre> liste = new ArrayList<>();
    private String csvFileName;


    public LivreModel(String csvFileName) throws CSVFileException {
        super();
        this.csvFileName = csvFileName;
        try {
            FileHandler.createFileIfNotExists(csvFileName, "Id;Titre;Auteur;AnneePublication;Genre;Disponible");
        } catch (IOException e) {
            throw new CSVFileException("Erreur lors de la création du fichier CSV : " + csvFileName);
        }
    }

    public void ajouterLivre(Livre e) {
        // Find the highest existing ID in the list, or start with 1 if the list is empty
        int newId = liste.isEmpty() ? 1 : liste.stream().mapToInt(Livre::getId).max().orElse(0) + 1;
        e.setId(newId);  // Set the new ID to the livre object
        liste.add(e);     // Add the book to the list
    }


    public void supprimerLivre(int id) throws LivreNotFoundException {
        Livre e = rechercherParID(id);
        if (e != null) {
            liste.remove(e);

            // Optionally, reassign IDs after deletion to make them contiguous
            for (int i = 0; i < liste.size(); i++) {
                liste.get(i).setId(i + 1); // Reassign IDs starting from 1
            }
        } else {
            throw new LivreNotFoundException(id);  // Throw exception if the book is not found
        }
    }


    public void modifierLivre(String titre, String auteur, int anneePublication, String genre, boolean disponible, int id) throws LivreNotFoundException {
        Livre e = rechercherParID(id);
        if (e != null) {
            e.setTitre(titre);
            e.setAuteur(auteur);
            e.setGenre(genre);
            e.setAnneePublication(anneePublication);
            e.setDisponible(disponible);
        } else {
            throw new LivreNotFoundException(id);  // Lancer exception si le livre n'est pas trouvé
        }
    }





    public Livre rechercherParID(int id) throws LivreNotFoundException {
        Optional<Livre> p = liste.stream().filter(e -> e.getId() == id).findFirst();
        return p.orElseThrow(() -> new LivreNotFoundException(id));  // Lancer exception si le livre n'est pas trouvé
    }



    public void lireCSV() throws CSVFileException {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFileName))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";"); // Split the line by semicolon
                if (data.length == 6) {
                    int id = Integer.parseInt(data[0]);
                    String titre = data[1];
                    String auteur = data[2];
                    int anneePublication = Integer.parseInt(data[3]);
                    String genre = data[4];
                    boolean disponible = Boolean.parseBoolean(data[5]);

                    Livre livre = new Livre(titre, auteur, anneePublication, genre, disponible);
                    livre.setId(id);
                    liste.add(livre);
                }
            }
        } catch (IOException e) {
            throw new CSVFileException("Erreur lors de la lecture du fichier CSV : " + csvFileName);
        } catch (NumberFormatException e) {
            throw new CSVFileException("Erreur lors de l'analyse du nombre dans le fichier CSV : " + csvFileName);
        }
    }



    public void sauvegraderCSV() throws CSVFileException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFileName))) {
            bw.write("Id;Titre;Auteur;AnneePublication;Genre;Disponible");
            bw.newLine();

            for (Livre livre : liste) {
                String ligne = livre.getId() + ";" +
                        livre.getTitre() + ";" +
                        livre.getAuteur() + ";" +
                        livre.getAnneePublication() + ";" +
                        livre.getGenre() + ";" +
                        livre.isDisponible();
                bw.write(ligne);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new CSVFileException("Erreur lors de la sauvegarde du fichier CSV : " + csvFileName);
        }
    }



    public List<Livre> getListe() {
        return liste;
    }

    // Méthode pour vérifier l'existence d'un livre par son ID
    public boolean livreExiste(int id) {
        return liste.stream().anyMatch(e -> e.getId() == id);
    }



}

