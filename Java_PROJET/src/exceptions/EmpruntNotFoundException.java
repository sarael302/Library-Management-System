package exceptions;

public class EmpruntNotFoundException extends Exception {
    public EmpruntNotFoundException(int id) {
        super("Emprunt avec l'ID " + id + " non trouvé.");
    }
}
