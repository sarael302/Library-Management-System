package test;

import model.Utilisateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilisateurTest {

    private Utilisateur utilisateur;

    @BeforeEach
    void setUp() {
        // Initialisation d'un utilisateur avant chaque test
        utilisateur = new Utilisateur("Jean Dupont", "jean.dupont@email.com", "0123456789");
    }

    @Test
    void testUtilisateurConstructorWithId() {
        Utilisateur utilisateur1 = new Utilisateur("Alice", "alice@email.com", "0987654321");
        assertNotNull(utilisateur1.getId(), "L'ID de l'utilisateur ne devrait pas être nul");
    }

    @Test
    void testUtilisateurConstructorWithoutId() {
        Utilisateur utilisateur2 = new Utilisateur("Bob", "bob@email.com", "0112233445");
        assertNotNull(utilisateur2.getId(), "L'ID de l'utilisateur devrait être généré automatiquement");
    }

    @Test
    void testGetNom() {
        assertEquals("Jean Dupont", utilisateur.getNom(), "Le nom de l'utilisateur doit être 'Jean Dupont'");
    }

    @Test
    void testSetNom() {
        utilisateur.setNom("Pierre Martin");
        assertEquals("Pierre Martin", utilisateur.getNom(), "Le nom de l'utilisateur doit être modifié");
    }

    @Test
    void testGetEmail() {
        assertEquals("jean.dupont@email.com", utilisateur.getEmail(), "L'email de l'utilisateur doit être 'jean.dupont@email.com'");
    }

    @Test
    void testSetEmail() {
        utilisateur.setEmail("pierre.martin@email.com");
        assertEquals("pierre.martin@email.com", utilisateur.getEmail(), "L'email de l'utilisateur doit être modifié");
    }

    @Test
    void testGetNumTele() {
        assertEquals("0123456789", utilisateur.getNumTele(), "Le numéro de téléphone doit être '0123456789'");
    }

    @Test
    void testSetNumTele() {
        utilisateur.setNumTele("0998776655");
        assertEquals("0998776655", utilisateur.getNumTele(), "Le numéro de téléphone doit être modifié");
    }

    @Test
    void testToString() {
        String expected = "1;Jean Dupont;jean.dupont@email.com;0123456789";
        assertEquals(expected, utilisateur.toString(), "La méthode toString() doit renvoyer la chaîne correcte");
    }
}
