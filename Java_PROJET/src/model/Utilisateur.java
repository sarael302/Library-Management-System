package model;

public class Utilisateur {
    private String nom;
    private String email;
    private String NumTele;
    private int id;
    private static int compteur;

    public Utilisateur() {
        super();
        compteur++;
        id = compteur;
    }

    public Utilisateur(String nom, String email, String numTele) {
        super();
        this.nom = nom;
        this.email = email;
        this.NumTele = numTele;
        compteur++;
        id = compteur;
    }

    public Utilisateur(String nom, String email,String numTele, int id) {
        super();
        this.nom = nom;
        this.email = email;
        this.NumTele = numTele;
        this.id = id;
        compteur = id;
    }



    // Getters et setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getNumTele() {
        return NumTele;
    }
    public void setNumTele(String numTele) {
        this.NumTele = numTele;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id + ";" + nom + ";" + email + ";" + NumTele ;
    }

    public static int getCompteur() {
        return compteur;
    }

    public static void setCompteur(int compteur) {
        Utilisateur.compteur = compteur;
    }
}
