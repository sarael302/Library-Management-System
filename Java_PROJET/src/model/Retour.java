package model;

public class Retour {
    private int empruntId;
    private String dateRetour;
    private int id;
    private double penalite;
    private static int compteur;

    public Retour() {
        super();
        compteur++;
        id = compteur;
    }

    public Retour(int empruntId, String dateRetour) {
        super();
        this.empruntId = empruntId;
        this.dateRetour = dateRetour;
        compteur++;
        id = compteur;
    }

    // Getters et setters
    public int getEmpruntId() {
        return empruntId;
    }

    public void setEmpruntId(int empruntId) {
        this.empruntId = empruntId;
    }

    public String getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(String dateRetour) {
        this.dateRetour = dateRetour;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPenalite() {
        return penalite;
    }

    public void setPenalite(double penalite) {
        this.penalite = penalite;
    }

    @Override
    public String toString() {
        return id + ";" + empruntId + ";" + dateRetour + ";" + penalite;
    }

    public static int getCompteur() {
        return compteur;
    }

    public static void setCompteur(int compteur) {
        Retour.compteur = compteur;
    }
}
