package exceptions;

public class LivreNotFoundException extends Exception {
    public LivreNotFoundException(int id) {
        super("Livre avec l'ID " + id + " non trouvé.");
    }
}
