package model;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import exceptions.EmpruntNotFoundException;
import exceptions.CSVFileException;
import utils.FileHandler;

public class EmpruntModel {

    private ArrayList<Emprunt> liste = new ArrayList<>();
    private String csvFileName;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public EmpruntModel(String csvFileName) throws CSVFileException {
        super();
        this.csvFileName = csvFileName;
        try {
            FileHandler.createFileIfNotExists(csvFileName, "Id;UtilisateurId;LivreId;DateEmprunt;DateRetourPrevu");
        } catch (IOException e) {
            throw new CSVFileException("Erreur lors de la création du fichier CSV : " + csvFileName);
        }
    }

    public void ajouterEmprunt(int utilisateurId, int livreId, int nombreJours) {
        // Generate a new ID for the Emprunt
        int newId = liste.isEmpty() ? 1 : liste.stream().mapToInt(Emprunt::getId).max().orElse(0) + 1;
        Emprunt e = new Emprunt(utilisateurId, livreId, nombreJours);
        e.setId(newId); // Assign the new ID to the emprunt
        liste.add(e);    // Add the emprunt to the list
    }
    public void supprimerEmprunt(int id) throws EmpruntNotFoundException {
        Emprunt e = rechercherParID(id);
        if (e != null) {
            liste.remove(e);

            // Optionally, reassign IDs after deletion to make them contiguous
            for (int i = 0; i < liste.size(); i++) {
                liste.get(i).setId(i + 1); // Reassign IDs starting from 1
            }
        } else {
            throw new EmpruntNotFoundException(id);  // Throw exception if the emprunt is not found
        }
    }


    public void prolongerEmprunt(int id, int nombreJours) throws EmpruntNotFoundException {
        Emprunt e = rechercherParID(id);
        if (e != null) {
            e.prolongerEmprunt(nombreJours);
        } else {
            throw new EmpruntNotFoundException(id);
        }
    }


    public void sauvegraderCSV() throws CSVFileException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFileName))) {
            bw.write("Id;UtilisateurId;LivreId;DateEmprunt;DateRetourPrevu");
            for (Emprunt e : liste) {
                bw.newLine();
                bw.write(e.toString());
            }
        } catch (IOException e) {
            throw new CSVFileException("Erreur lors de la sauvegarde du fichier CSV : " + csvFileName);
        }
    }

    public Emprunt rechercherParID(int id) throws EmpruntNotFoundException {
        return liste.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EmpruntNotFoundException(id));
    }

    public void lireCSV() throws CSVFileException {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFileName))) {
            String line;
            br.readLine(); // Ignorer l'en-tête
            while ((line = br.readLine()) != null) {
                String[] words = line.split(";");
                if (words.length == 5) {
                    int id = Integer.parseInt(words[0]);
                    int utilisateurId = Integer.parseInt(words[1]);
                    int livreId = Integer.parseInt(words[2]);
                    LocalDate dateEmprunt = LocalDate.parse(words[3], DATE_FORMATTER);
                    LocalDate dateRetourPrevu = LocalDate.parse(words[4], DATE_FORMATTER);

                    Emprunt e = new Emprunt(utilisateurId, livreId, dateEmprunt, dateRetourPrevu, id);
                    liste.add(e);
                }
            }
        } catch (IOException e) {
            throw new CSVFileException("Erreur lors de la lecture du fichier CSV : " + csvFileName);
        } catch (NumberFormatException e) {
            throw new CSVFileException("Erreur lors de l'analyse du nombre dans le fichier CSV : " + csvFileName);
        }
    }

    public boolean empruntExiste(int empruntId) {
        return liste.stream().anyMatch(emprunt -> emprunt.getId() == empruntId);
    }

    public List<Emprunt> getListe() {
        return new ArrayList<>(liste);
    }

    public LocalDate getDateEmprunt(int empruntId) throws EmpruntNotFoundException {
        Emprunt emprunt = rechercherParID(empruntId);
        return LocalDate.parse(emprunt.getDateEmprunt(), DATE_FORMATTER);
    }
}