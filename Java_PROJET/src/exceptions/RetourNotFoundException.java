package exceptions;

public class RetourNotFoundException extends Exception {
    public RetourNotFoundException(int id) {
        super("Retour avec l'ID " + id + " non trouvé.");
    }
}
