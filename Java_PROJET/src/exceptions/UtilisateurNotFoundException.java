package exceptions;

public class UtilisateurNotFoundException extends Exception {
    public UtilisateurNotFoundException(int email) {
        super("Utilisateur avec l'email " + email + " non trouvé.");
    }
}
