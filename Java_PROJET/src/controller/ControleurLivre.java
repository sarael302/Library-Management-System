package controller;

import model.Livre;
import model.LivreModel;
import view.GestionView;
import exceptions.LivreNotFoundException;
import exceptions.CSVFileException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControleurLivre {

    private LivreModel livreModel;
    private GestionView vue;

    public ControleurLivre(LivreModel livreModel, GestionView vue) throws CSVFileException {
        this.livreModel = livreModel;
        this.vue = vue;
        addListeners();
        livreModel.lireCSV();
        refreshTable();
    }
    private void refreshTable() {
        // Rafraîchissement des données de la table
        System.out.println("Rafraîchissement de la table avec " + livreModel.getListe().size() + " éléments.");

        vue.getModelLivres().setRowCount(0); // Effacer les anciennes données

        for (Livre livre : livreModel.getListe()) {
            vue.getModelLivres().addRow(new Object[]{
                    livre.getId(),
                    livre.getTitre(),
                    livre.getAuteur(),
                    livre.getAnneePublication(),
                    livre.getGenre(),
                    livre.isDisponible() ? "Oui" : "Non"
            });
        }

    }

    private void addListeners() {
        // Action pour le bouton Ajouter
        vue.getAjouterLivreBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = createLivreInputPanel(null);
                int result = JOptionPane.showConfirmDialog(vue.getFrame(), panel, "Ajouter un Livre",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        Livre livre = extractLivreFromPanel(panel);
                        livreModel.ajouterLivre(livre);

                        JOptionPane.showMessageDialog(vue.getFrame(), "Livre ajouté avec succès !");
                        refreshTable();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(vue.getFrame(), "Erreur : l'année doit être un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }  catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(vue.getFrame(), ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Action pour le bouton Modifier
        vue.getModifierLivreBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(JOptionPane.showInputDialog("Entrez l'ID du livre à modifier :"));
                    Livre livre = livreModel.rechercherParID(id);

                    JPanel panel = createLivreInputPanel(livre);
                    int result = JOptionPane.showConfirmDialog(vue.getFrame(), panel, "Modifier un Livre",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        Livre updatedLivre = extractLivreFromPanel(panel);
                        livreModel.modifierLivre(updatedLivre.getTitre(), updatedLivre.getAuteur(),
                                updatedLivre.getAnneePublication(), updatedLivre.getGenre(),
                                updatedLivre.isDisponible(), // Pass the new attribute
                                id);

                        JOptionPane.showMessageDialog(vue.getFrame(), "Livre modifié avec succès !");
                        refreshTable();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(vue.getFrame(), "Erreur : l'ID ou l'année doit être un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                } catch (LivreNotFoundException ex) {
                    JOptionPane.showMessageDialog(vue.getFrame(), "Livre non trouvé !", "Erreur", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(vue.getFrame(), ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        // Action pour le bouton Supprimer
        vue.getSupprimerLivreBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(JOptionPane.showInputDialog("Entrez l'ID du livre à supprimer :"));
                    livreModel.supprimerLivre(id);

                    JOptionPane.showMessageDialog(vue.getFrame(), "Livre supprimé avec succès !");
                    refreshTable();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(vue.getFrame(), "Erreur : l'ID doit être un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                } catch (LivreNotFoundException ex) {
                    JOptionPane.showMessageDialog(vue.getFrame(), "Livre non trouvé !", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action pour la recherche des livres
        vue.getSearchLivresField().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchQuery = vue.getSearchLivresField().getText();
                filterLivres(searchQuery);
            }
        });
        vue.getSauvegarderBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    livreModel.sauvegraderCSV(); // Sauvegarde des données en mémoire dans le CSV
                    JOptionPane.showMessageDialog(vue.getFrame(), "Données sauvegardées avec succès !");
                } catch (CSVFileException ex) {
                    JOptionPane.showMessageDialog(vue.getFrame(), "Erreur lors de la sauvegarde du fichier CSV : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }



    private void filterLivres(String query) {
        vue.getModelLivres().setRowCount(0); // Effacer les anciennes données
        for (Livre livre : livreModel.getListe()) {
            if (livre.getTitre().toLowerCase().contains(query.toLowerCase())) {
                vue.getModelLivres().addRow(new Object[]{livre.getId(), livre.getTitre(), livre.getAuteur(), livre.getAnneePublication(), livre.getGenre()});
            }
        }
    }

    private JPanel createLivreInputPanel(Livre livre) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField titreField = new JTextField(livre != null ? livre.getTitre() : "");
        JTextField auteurField = new JTextField(livre != null ? livre.getAuteur() : "");
        JTextField genreField = new JTextField(livre != null ? livre.getGenre() : "");
        JTextField anneeField = new JTextField(livre != null ? String.valueOf(livre.getAnneePublication()) : "");
        JCheckBox disponibleCheckBox = new JCheckBox("Disponible", livre != null && livre.isDisponible()); // New field


        panel.add(new JLabel("Titre :"));
        panel.add(titreField);
        panel.add(new JLabel("Auteur :"));
        panel.add(auteurField);
        panel.add(new JLabel("Genre :"));
        panel.add(genreField);
        panel.add(new JLabel("Année de publication :"));
        panel.add(anneeField);
        panel.add(disponibleCheckBox);

        panel.putClientProperty("titreField", titreField);
        panel.putClientProperty("auteurField", auteurField);
        panel.putClientProperty("genreField", genreField);
        panel.putClientProperty("anneeField", anneeField);
        panel.putClientProperty("disponibleCheckBox", disponibleCheckBox);


        return panel;
    }

    private Livre extractLivreFromPanel(JPanel panel) throws NumberFormatException, IllegalArgumentException {
        //
        JTextField titreField = (JTextField) panel.getClientProperty("titreField");
        JTextField auteurField = (JTextField) panel.getClientProperty("auteurField");
        JTextField genreField = (JTextField) panel.getClientProperty("genreField");
        JTextField anneeField = (JTextField) panel.getClientProperty("anneeField");
        JCheckBox disponibleCheckBox = (JCheckBox) panel.getClientProperty("disponibleCheckBox"); // New line


        String titre = titreField.getText().trim();
        String auteur = auteurField.getText().trim();
        String genre = genreField.getText().trim();
        String anneePublicationText = anneeField.getText().trim();
        boolean disponible = disponibleCheckBox.isSelected(); // New line



        if (titre.isEmpty() || auteur.isEmpty() || genre.isEmpty() || anneePublicationText.isEmpty()) {
            throw new IllegalArgumentException("Tous les champs doivent être remplis.");
        }

        int anneePublication = Integer.parseInt(anneePublicationText);

        return new Livre(titre, auteur, anneePublication, genre, disponible);
    }
}
