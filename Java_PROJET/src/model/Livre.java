package model;

/**
 * Représente un livre dans le système de gestion de bibliothèque.
 * Chaque livre a un titre, un auteur, une année de publication, un genre, un statut de disponibilité et un identifiant unique.
 * @author Sara Amine Hadil Saad
 */
public class Livre {
    private String titre;
    private String auteur;
    private int anneePublication;
    private String genre;
    private boolean disponible;
    private int id;
    private static int compteur;

    /**
     * Constructeur par défaut pour créer un livre avec un ID généré automatiquement.
     */
    public Livre() {
        super();
        compteur++;
        id = compteur;
    }

    /**
     * Constructeur pour créer un livre avec des attributs spécifiés et un ID généré automatiquement.
     *
     * @param titre             Le titre du livre.
     * @param auteur            L'auteur du livre.
     * @param anneePublication  L'année de publication du livre.
     * @param genre             Le genre du livre.
     * @param disponible        Le statut de disponibilité du livre.
     */
    public Livre(String titre, String auteur, int anneePublication, String genre, boolean disponible) {
        super();
        this.titre = titre;
        this.auteur = auteur;
        this.anneePublication = anneePublication;
        this.genre = genre;
        this.disponible = disponible;
        compteur++;
        id = compteur;
    }

    /**
     * Constructeur pour créer un livre avec des attributs spécifiés et un ID personnalisé.
     *
     * @param titre             Le titre du livre.
     * @param auteur            L'auteur du livre.
     * @param anneePublication  L'année de publication du livre.
     * @param genre             Le genre du livre.
     * @param disponible        Le statut de disponibilité du livre.
     * @param id                L'ID unique du livre.
     */
    public Livre(String titre, String auteur, int anneePublication, String genre, boolean disponible, int id) {
        super();
        this.titre = titre;
        this.auteur = auteur;
        this.anneePublication = anneePublication;
        this.genre = genre;
        this.disponible = disponible;
        this.id = id;
        compteur = id;
    }

    // Getters et setters

    /**
     * Récupère le titre du livre.-++-
     *
     * @return Le titre du livre.
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Modifie le titre du livre.
     *
     * @param titre Le nouveau titre du livre.
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * Récupère l'auteur du livre.
     *
     * @return L'auteur du livre.
     */
    public String getAuteur() {
        return auteur;
    }

    /**
     * Modifie l'auteur du livre.
     *
     * @param auteur Le nouvel auteur du livre.
     */
    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    /**
     * Récupère l'année de publication du livre.
     *
     * @return L'année de publication du livre.
     */
    public int getAnneePublication() {
        return anneePublication;
    }

    /**
     * Modifie l'année de publication du livre.
     *
     * @param anneePublication La nouvelle année de publication du livre.
     */
    public void setAnneePublication(int anneePublication) {
        this.anneePublication = anneePublication;
    }

    /**
     * Récupère le genre du livre.
     *
     * @return Le genre du livre.
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Modifie le genre du livre.
     *
     * @param genre Le nouveau genre du livre.
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Récupère l'ID du livre.
     *
     * @return L'ID du livre.
     */
    public int getId() {
        return id;
    }

    /**
     * Modifie l'ID du livre.
     *
     * @param id Le nouvel ID du livre.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Vérifie si le livre est disponible.
     *
     * @return true si le livre est disponible, false sinon.
     */
    public boolean isDisponible() {
        return disponible;
    }

    /**
     * Modifie le statut de disponibilité du livre.
     *
     * @param disponible true si le livre devient disponible, false sinon.
     */
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    /**
     * Retourne une chaîne de caractères représentant l'objet Livre.
     *
     * @return Une chaîne de caractères représentant l'objet Livre.
     */
    @Override
    public String toString() {
        return id + ";" + titre + ";" + auteur + ";" + anneePublication + ";" + genre;
    }

    /**
     * Récupère le compteur qui génère les IDs uniques des livres.
     *
     * @return Le compteur des IDs de livres.
     */
    public static int getCompteur() {
        return compteur;
    }

    /**
     * Modifie le compteur des IDs de livres.
     *
     * @param compteur Le nouveau compteur des IDs de livres.
     */
    public static void setCompteur(int compteur) {
        Livre.compteur = compteur;
    }
}
