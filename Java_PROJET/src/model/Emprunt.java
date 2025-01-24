package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Emprunt {
    private int utilisateurId;
    private int livreId;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevu;
    private int id;
    private static int compteur;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Emprunt() {
        super();
        this.dateEmprunt = LocalDate.now();
        compteur++;
        id = compteur;
    }

    public Emprunt(int utilisateurId, int livreId, int nombreJours) {
        this();
        this.utilisateurId = utilisateurId;
        this.livreId = livreId;
        this.dateRetourPrevu = this.dateEmprunt.plusDays(nombreJours);
    }

    public Emprunt(int utilisateurId, int livreId, LocalDate dateEmprunt, LocalDate dateRetourPrevu, int id) {
        super();
        this.utilisateurId = utilisateurId;
        this.livreId = livreId;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevu = dateRetourPrevu;
        this.id = id;
        compteur = Math.max(compteur, id);
    }
    public Emprunt(int utilisateurId, int livreId, String dateEmprunt, String dateRetourPrevu) {
        this();
        this.utilisateurId = utilisateurId;
        this.livreId = livreId;
        this.dateEmprunt = LocalDate.parse(dateEmprunt, DATE_FORMATTER);
        this.dateRetourPrevu = LocalDate.parse(dateRetourPrevu, DATE_FORMATTER);
    }

    // Getters et setters
    public int getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public int getLivreId() {
        return livreId;
    }

    public void setLivreId(int livreId) {
        this.livreId = livreId;
    }

    public String getDateEmprunt() {
        return dateEmprunt.format(DATE_FORMATTER);
    }

    public void setDateEmprunt(String dateEmprunt) {
        this.dateEmprunt = LocalDate.parse(dateEmprunt, DATE_FORMATTER);
    }

    public String getDateRetourPrevu() {
        return dateRetourPrevu.format(DATE_FORMATTER);
    }

    public void setDateRetourPrevu(String dateRetourPrevu) {
        this.dateRetourPrevu = LocalDate.parse(dateRetourPrevu, DATE_FORMATTER);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id + ";" + utilisateurId + ";" + livreId + ";" + getDateEmprunt() + ";" + getDateRetourPrevu();
    }

    public static int getCompteur() {
        return compteur;
    }

    public static void setCompteur(int compteur) {
        Emprunt.compteur = compteur;
    }

    public void prolongerEmprunt(int nombreJours) {
        this.dateRetourPrevu = this.dateRetourPrevu.plusDays(nombreJours);
    }
}