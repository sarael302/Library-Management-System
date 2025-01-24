package model;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import exceptions.RetourNotFoundException;
import exceptions.CSVFileException;
import exceptions.EmpruntNotFoundException;
import utils.FileHandler;

public class RetourModel {

    private ArrayList<Retour> liste = new ArrayList<>();
    private String csvFileName;



    public RetourModel(String csvFileName) throws CSVFileException {
        super();
        this.csvFileName = csvFileName;
        try {
            FileHandler.createFileIfNotExists(csvFileName, "Id;EmpruntId;DateRetour;Penalite");
        } catch (IOException e) {
            throw new CSVFileException("Erreur lors de la création du fichier CSV : " + csvFileName);
        }
    }

    public void ajouterRetour(Retour e) {
        // Generate a new ID for the Retour object
        int newId = liste.isEmpty() ? 1 : liste.stream().mapToInt(Retour::getId).max().orElse(0) + 1;

        e.setId(newId); // Set the generated ID to the retour object
        liste.add(e);   // Add the retour to the list
    }
    public void supprimerRetour(int id) throws RetourNotFoundException {
        Retour e = rechercherParID(id);
        if (e != null) {
            liste.remove(e);

            // Optionally, reassign IDs after deletion to make them contiguous
            for (int i = 0; i < liste.size(); i++) {
                liste.get(i).setId(i + 1); // Reassign IDs starting from 1
            }
        } else {
            throw new RetourNotFoundException(id);  // Throw exception if retour is not found
        }
    }




    public void sauvegraderCSV() throws CSVFileException {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(csvFileName));
            bw.write("Id;EmpruntId;DateRetour;Penalite");
            for (int i = 0; i < liste.size(); i++) {
                bw.newLine();
                bw.write(liste.get(i).toString());
            }
            bw.close();
        } catch (IOException e) {
            throw new CSVFileException("Erreur lors de la sauvegarde du fichier CSV : " + csvFileName);  // Lancer exception CSVFileException
        }
    }

    public Retour rechercherParID(int id) throws RetourNotFoundException {
        Optional<Retour> p = liste.stream().filter(e -> e.getId() == id).findFirst();
        return p.orElseThrow(() -> new RetourNotFoundException(id));  // Lancer exception si le retour n'est pas trouvé
    }

    public void lireCSV() throws CSVFileException, NumberFormatException {
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFileName));
            br.readLine(); // Ignorer l'en-tête
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split(";");
                int id = Integer.parseInt(words[0]);
                int empruntId = Integer.parseInt(words[1]);
                String dateRetour = words[2];
                double penalite = Double.parseDouble(words[3]);

                Retour e = new Retour();
                e.setId(id);
                e.setEmpruntId(empruntId);
                e.setDateRetour(dateRetour);
                e.setPenalite(penalite);
                liste.add(e);
            }
            br.close();
        } catch (IOException e) {
            throw new CSVFileException("Erreur lors de la lecture du fichier CSV : " + csvFileName);  // Lancer exception CSVFileException
        } catch (java.lang.NumberFormatException e) {
            throw new NumberFormatException("Erreur lors de l'analyse du nombre dans le fichier CSV : " + csvFileName);
        }
    }

    public List<Retour> getListe() {
        return liste;
    }

    public void calculerPenalite(Retour retour, EmpruntModel empruntModel) throws EmpruntNotFoundException {
        Emprunt emprunt = empruntModel.rechercherParID(retour.getEmpruntId());
        LocalDate dateRetour = LocalDate.parse(retour.getDateRetour(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate dateRetourPrevu = LocalDate.parse(emprunt.getDateRetourPrevu(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if (dateRetour.isAfter(dateRetourPrevu)) {
            long joursRetard = ChronoUnit.DAYS.between(dateRetourPrevu, dateRetour);
            double penalite;

            if (joursRetard == 1) {
                penalite = 10; // 10 DH pour le premier jour
            } else {
                penalite = 10 + (joursRetard - 1) * 5; // 10 DH pour le premier jour et 5 DH pour les jours suivants
            }

            retour.setPenalite(penalite);
        } else {
            retour.setPenalite(0); // Pas de pénalité si le retour est à temps
        }
    }
}
