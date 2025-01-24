package controller;

import model.Retour;
import model.RetourModel;
import model.EmpruntModel;
import model.LivreModel;
import view.GestionView;
import exceptions.RetourNotFoundException;
import exceptions.CSVFileException;
import exceptions.EmpruntNotFoundException;
import exceptions.LivreNotFoundException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import javax.swing.table.DefaultTableModel;
import model.Livre;

public class ControleurRetour {

    private RetourModel retourModel;
    private EmpruntModel empruntModel;
    private LivreModel livreModel;
    private GestionView vue;

    public ControleurRetour(RetourModel retourModel, EmpruntModel empruntModel, LivreModel livreModel, GestionView vuePrincipale) throws CSVFileException {
        this.retourModel = retourModel;
        this.empruntModel = empruntModel;
        this.livreModel = livreModel;
        this.vue = vuePrincipale;
        addListeners();
        retourModel.lireCSV();
        refreshTable();
    }

    private void addListeners() {
        // Action pour le bouton Ajouter
        vue.getAjouterRetourBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = createRetourInputPanel(null);
                int result = JOptionPane.showConfirmDialog(vue, panel, "Ajouter un Retour",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        Retour retour = extractRetourFromPanel(panel);

                        // Vérifier l'existence de l'emprunt
                        if (!empruntModel.empruntExiste(retour.getEmpruntId())) {
                            JOptionPane.showMessageDialog(vue, "Erreur : L'emprunt n'existe pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Vérifier que la date de retour n'est pas inférieure à la date d'emprunt
                        LocalDate dateEmprunt = empruntModel.getDateEmprunt(retour.getEmpruntId());
                        LocalDate dateRetour = LocalDate.parse(retour.getDateRetour(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        if (dateRetour.isBefore(dateEmprunt)) {
                            JOptionPane.showMessageDialog(vue, "Erreur : La date de retour ne peut pas être antérieure à la date d'emprunt.", "Erreur", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Calculer la pénalité
                        retourModel.calculerPenalite(retour, empruntModel);

                        retourModel.ajouterRetour(retour);
                        retourModel.sauvegraderCSV(); // Sauvegarder après l'ajout

                        // Mettre à jour la disponibilité du livre
                        int livreId = empruntModel.rechercherParID(retour.getEmpruntId()).getLivreId();
                        livreModel.modifierLivre(
                                livreModel.rechercherParID(livreId).getTitre(),
                                livreModel.rechercherParID(livreId).getAuteur(),
                                livreModel.rechercherParID(livreId).getAnneePublication(),
                                livreModel.rechercherParID(livreId).getGenre(),
                                true, // Set disponible to true
                                livreId
                        );
                        livreModel.sauvegraderCSV();

                        JOptionPane.showMessageDialog(vue, "Retour ajouté avec succès ! Pénalité : " + retour.getPenalite() + " DH");
                        refreshTable();
                        refreshLivreTable(); // Add this method to refresh the book table
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(vue, "Erreur : l'ID doit être un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    } catch (CSVFileException ex) {
                        JOptionPane.showMessageDialog(vue, "Erreur lors de la sauvegarde du fichier CSV : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    } catch (IllegalArgumentException | EmpruntNotFoundException | LivreNotFoundException ex) {
                        JOptionPane.showMessageDialog(vue, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Action pour le bouton Supprimer
        vue.getSupprimerRetourBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(JOptionPane.showInputDialog("Entrez l'ID du retour à supprimer :"));
                    retourModel.supprimerRetour(id);

                    retourModel.sauvegraderCSV(); // Sauvegarder après suppression
                    JOptionPane.showMessageDialog(vue, "Retour supprimé avec succès !");
                    refreshTable();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(vue, "Erreur : l'ID doit être un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                } catch (RetourNotFoundException ex) {
                    JOptionPane.showMessageDialog(vue, "Retour non trouvé !", "Erreur", JOptionPane.ERROR_MESSAGE);
                } catch (CSVFileException ex) {
                    JOptionPane.showMessageDialog(vue, "Erreur lors de la sauvegarde du fichier CSV : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action pour la recherche des retours
        vue.getSearchRetoursField().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchQuery = vue.getSearchRetoursField().getText();
                filterRetours(searchQuery);
            }
        });
    }

    private void refreshTable() {
        // Rafraîchissement des données de la table
        vue.getModelRetours().setRowCount(0); // Effacer les anciennes données
        for (Retour retour : retourModel.getListe()) {
            vue.getModelRetours().addRow(new Object[]{retour.getId(), retour.getEmpruntId(), retour.getDateRetour(), retour.getPenalite()});
        }
    }

    private void filterRetours(String query) {
        vue.getModelRetours().setRowCount(0); // Effacer les anciennes données
        for (Retour retour : retourModel.getListe()) {
            if (String.valueOf(retour.getEmpruntId()).contains(query)) {
                vue.getModelRetours().addRow(new Object[]{retour.getId(), retour.getEmpruntId(), retour.getDateRetour(), retour.getPenalite()});
            }
        }
    }

    private JPanel createRetourInputPanel(Retour retour) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField empruntIdField = new JTextField(retour != null ? String.valueOf(retour.getEmpruntId()) : "");
        JTextField dateRetourField = new JTextField(retour != null ? retour.getDateRetour() : "");

        panel.add(new JLabel("ID Emprunt :"));
        panel.add(empruntIdField);
        panel.add(new JLabel("Date de retour (YYYY-MM-DD) :"));
        panel.add(dateRetourField);

        panel.putClientProperty("empruntIdField", empruntIdField);
        panel.putClientProperty("dateRetourField", dateRetourField);

        return panel;
    }

    private Retour extractRetourFromPanel(JPanel panel) throws NumberFormatException, IllegalArgumentException {
        JTextField empruntIdField = (JTextField) panel.getClientProperty("empruntIdField");
        JTextField dateRetourField = (JTextField) panel.getClientProperty("dateRetourField");

        String empruntIdText = empruntIdField.getText().trim();
        String dateRetour = dateRetourField.getText().trim();

        if (empruntIdText.isEmpty() || dateRetour.isEmpty()) {
            throw new IllegalArgumentException("Tous les champs doivent être remplis.");
        }

        if (!isValidDate(dateRetour)) {
            throw new IllegalArgumentException("La date doit être au format YYYY-MM-DD.");
        }

        int empruntId = Integer.parseInt(empruntIdText);

        return new Retour(empruntId, dateRetour);
    }

    private boolean isValidDate(String date) {
        String dateRegex = "^\\d{4}-\\d{2}-\\d{2}$";
        return Pattern.matches(dateRegex, date);
    }

    private void refreshLivreTable() {
        DefaultTableModel modelLivres = (DefaultTableModel) vue.getTableLivres().getModel();
        modelLivres.setRowCount(0); // Clear old data
        for (Livre livre : livreModel.getListe()) {
            modelLivres.addRow(new Object[]{
                    livre.getId(),
                    livre.getTitre(),
                    livre.getAuteur(),
                    livre.getAnneePublication(),
                    livre.getGenre(),
                    livre.isDisponible() ? "Oui" : "Non"
            });
        }
    }
}

