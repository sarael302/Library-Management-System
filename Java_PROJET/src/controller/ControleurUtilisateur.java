package controller;

import model.Utilisateur;
import model.UtilisateurModel;
import view.GestionView;
import exceptions.UtilisateurNotFoundException;
import exceptions.CSVFileException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class ControleurUtilisateur {

    private UtilisateurModel utilisateurModel;
    private GestionView vue;


    public ControleurUtilisateur(UtilisateurModel utilisateurModel, GestionView vue) throws CSVFileException {
        this.utilisateurModel = utilisateurModel;
        this.vue = vue;
        addListeners();
        utilisateurModel.lireCSV();  // Read data from the CSV when the controller is initialized
        refreshTable();  // Refresh the table with the data from CSV
    }


    private void addListeners() {
        // Action pour le bouton Ajouter
        vue.getAjouterUtilisateurBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = createUtilisateurInputPanel(null);
                int result = JOptionPane.showConfirmDialog(vue.getFrame(), panel, "Ajouter un Utilisateur",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        Utilisateur utilisateur = extractUtilisateurFromPanel(panel);
                        utilisateurModel.ajouterUtilisateur(utilisateur);

                        // Remove the direct CSV save here
                        JOptionPane.showMessageDialog(vue.getFrame(), "Utilisateur ajouté avec succès !");
                        refreshTable();
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(vue.getFrame(), ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Action pour le bouton Modifier
        vue.getModifierUtilisateurBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(JOptionPane.showInputDialog("Entrez l'ID de l'utilisateur à modifier :"));
                    Utilisateur utilisateur = utilisateurModel.rechercherParId(id);

                    JPanel panel = createUtilisateurInputPanel(utilisateur);
                    int result = JOptionPane.showConfirmDialog(vue.getFrame(), panel, "Modifier un Utilisateur",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        Utilisateur updatedUtilisateur = extractUtilisateurFromPanel(panel);
                        utilisateurModel.modifierUtilisateur(updatedUtilisateur.getNom(), updatedUtilisateur.getEmail(), updatedUtilisateur.getNumTele(), id);

                        utilisateurModel.sauvegraderCSV(); // Sauvegarder après modification
                        JOptionPane.showMessageDialog(vue.getFrame(), "Utilisateur modifié avec succès !");
                        refreshTable();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(vue.getFrame(), "Erreur : l'ID doit être un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                } catch (UtilisateurNotFoundException ex) {
                    JOptionPane.showMessageDialog(vue.getFrame(), "Utilisateur non trouvé !", "Erreur", JOptionPane.ERROR_MESSAGE);
                } catch (CSVFileException ex) {
                    JOptionPane.showMessageDialog(vue.getFrame(), "Erreur lors de la sauvegarde du fichier CSV : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(vue.getFrame(), ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        // Action for the Save button
        vue.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    utilisateurModel.sauvegraderCSV();  // Call the save method from the model
                    JOptionPane.showMessageDialog(vue.getFrame(), "Données sauvegardées avec succès !");
                } catch (CSVFileException ex) {
                    JOptionPane.showMessageDialog(vue.getFrame(), "Erreur lors de la sauvegarde du fichier CSV : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        // Action pour le bouton Supprimer
        vue.getSupprimerUtilisateurBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(JOptionPane.showInputDialog("Entrez l'ID de l'utilisateur à supprimer :"));
                    utilisateurModel.supprimerUtilisateur(id);

                    utilisateurModel.sauvegraderCSV(); // Sauvegarder après suppression
                    JOptionPane.showMessageDialog(vue.getFrame(), "Utilisateur supprimé avec succès !");
                    refreshTable();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(vue.getFrame(), "Erreur : l'ID doit être un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                } catch (UtilisateurNotFoundException ex) {
                    JOptionPane.showMessageDialog(vue.getFrame(), "Utilisateur non trouvé !", "Erreur", JOptionPane.ERROR_MESSAGE);
                } catch (CSVFileException ex) {
                    JOptionPane.showMessageDialog(vue.getFrame(), "Erreur lors de la sauvegarde du fichier CSV : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action pour la recherche des utilisateurs
        vue.getSearchUtilisateursField().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchQuery = vue.getSearchUtilisateursField().getText();
                filterUtilisateurs(searchQuery);
            }
        });

    }


    private void refreshTable() {
        // Rafraîchissement des données de la table
        vue.getModelUtilisateurs().setRowCount(0); // Effacer les anciennes données
        for (Utilisateur utilisateur : utilisateurModel.getListe()) {
            vue.getModelUtilisateurs().addRow(new Object[]{utilisateur.getId(), utilisateur.getNom(), utilisateur.getEmail(),utilisateur.getNumTele() });
        }
    }

    private void filterUtilisateurs(String query) {
        vue.getModelUtilisateurs().setRowCount(0); // Effacer les anciennes données
        for (Utilisateur utilisateur : utilisateurModel.getListe()) {
            if (utilisateur.getNom().toLowerCase().contains(query.toLowerCase()) ||
                    utilisateur.getEmail().toLowerCase().contains(query.toLowerCase())) {
                vue.getModelUtilisateurs().addRow(new Object[]{utilisateur.getId(), utilisateur.getNom(), utilisateur.getEmail()});
            }
        }
    }

    private JPanel createUtilisateurInputPanel(Utilisateur utilisateur) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField nomField = new JTextField(utilisateur != null ? utilisateur.getNom() : "");
        JTextField emailField = new JTextField(utilisateur != null ? utilisateur.getEmail() : "");
        JTextField numField = new JTextField(String.valueOf(utilisateur != null ? utilisateur.getNumTele() : ""));

        panel.add(new JLabel("Nom complet:"));
        panel.add(nomField);
        panel.add(new JLabel("Email :"));
        panel.add(emailField);
        panel.add(new JLabel("Numéro de téléphone :"));
        panel.add(numField);


        panel.putClientProperty("nomField", nomField);
        panel.putClientProperty("emailField", emailField);
        panel.putClientProperty("numField", numField);


        return panel;
    }

    private Utilisateur extractUtilisateurFromPanel(JPanel panel) throws IllegalArgumentException {
        JTextField nomField = (JTextField) panel.getClientProperty("nomField");
        JTextField emailField = (JTextField) panel.getClientProperty("emailField");
        JTextField numField = (JTextField) panel.getClientProperty("numField");

        String nom = nomField.getText().trim();
        String email = emailField.getText().trim();
        String numTele = numField.getText().trim();

        if (nom.isEmpty() || email.isEmpty() || numTele.isEmpty()) {
            throw new IllegalArgumentException("Tous les champs doivent être remplis.");
        }

        // Validate phone number format
        if (!numTele.matches("\\d+")) {  // Check if numTele contains only digits
            throw new IllegalArgumentException("Numéro de téléphone invalide.");
        }

        // Return the Utilisateur object
        return new Utilisateur(nom, email, numTele);  // Pass numTele as String
    }


}
