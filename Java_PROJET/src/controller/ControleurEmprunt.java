package controller;

import model.Emprunt;
import model.EmpruntModel;
import model.LivreModel;
import model.UtilisateurModel;
import model.Livre;
import model.Utilisateur;
import view.GestionView;
import exceptions.EmpruntNotFoundException;
import exceptions.CSVFileException;
import exceptions.LivreNotFoundException;
import exceptions.UtilisateurNotFoundException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class ControleurEmprunt {

    private EmpruntModel empruntModel;
    private LivreModel livreModel;
    private UtilisateurModel utilisateurModel;
    private GestionView vue;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ControleurEmprunt(EmpruntModel empruntModel, LivreModel livreModel, UtilisateurModel utilisateurModel, GestionView vue) throws CSVFileException {
        this.empruntModel = empruntModel;
        this.livreModel = livreModel;
        this.utilisateurModel = utilisateurModel;
        this.vue = vue;
        addListeners();
        empruntModel.lireCSV();
        refreshAllTables();
    }

    private void addListeners() {
        vue.getAjouterEmpruntBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                JComboBox<String> utilisateurComboBox = new JComboBox<>();
                JTextField livreIdField = new JTextField(10);
                JTextField nombreJoursField = new JTextField(10);

                for (Utilisateur utilisateur : utilisateurModel.getListe()) {
                    utilisateurComboBox.addItem(utilisateur.getId() + " - " + utilisateur.getNom());
                }

                panel.add(new JLabel("Utilisateur :"));
                panel.add(utilisateurComboBox);
                panel.add(new JLabel("ID du Livre :"));
                panel.add(livreIdField);
                panel.add(new JLabel("Nombre de jours de l'emprunt :"));
                panel.add(nombreJoursField);

                int result = JOptionPane.showConfirmDialog(vue.getFrame(), panel, "Ajouter un Emprunt",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        String utilisateurSelection = (String) utilisateurComboBox.getSelectedItem();
                        String livreIdText = livreIdField.getText().trim();
                        String nombreJoursText = nombreJoursField.getText().trim();

                        if (utilisateurSelection == null || livreIdText.isEmpty() || nombreJoursText.isEmpty()) {
                            throw new IllegalArgumentException("Tous les champs doivent être remplis.");
                        }

                        int utilisateurId = Integer.parseInt(utilisateurSelection.split(" - ")[0]);
                        int livreId = Integer.parseInt(livreIdText);
                        int nombreJours = Integer.parseInt(nombreJoursText);

                        if (!livreModel.livreExiste(livreId)) {
                            throw new IllegalArgumentException("Le livre avec l'ID " + livreId + " n'existe pas.");
                        }

                        Livre livre = livreModel.rechercherParID(livreId);
                        if (!livre.isDisponible()) {
                            throw new IllegalArgumentException("Le livre avec l'ID " + livreId + " n'est pas disponible.");
                        }

                        // Add new emprunt
                        empruntModel.ajouterEmprunt(utilisateurId, livreId, nombreJours);
                        empruntModel.sauvegraderCSV();

                        // Update livre availability
                        livre.setDisponible(false);
                        livreModel.modifierLivre(livre.getTitre(), livre.getAuteur(), livre.getAnneePublication(), livre.getGenre(), false, livreId);
                        livreModel.sauvegraderCSV();

                        JOptionPane.showMessageDialog(vue.getFrame(), "Emprunt ajouté avec succès et disponibilité du livre mise à jour !");
                        refreshAllTables();

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(vue.getFrame(), "Erreur : Les IDs doivent être des nombres valides.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    } catch (CSVFileException ex) {
                        JOptionPane.showMessageDialog(vue.getFrame(), "Erreur lors de la sauvegarde du fichier CSV : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    } catch (IllegalArgumentException | LivreNotFoundException ex) {
                        JOptionPane.showMessageDialog(vue.getFrame(), "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        vue.getProlongerEmpruntBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(JOptionPane.showInputDialog("Entrez l'ID de l'emprunt à prolonger :"));
                    Emprunt emprunt = empruntModel.rechercherParID(id);

                    JPanel panel = createProlongementInputPanel(emprunt);
                    int result = JOptionPane.showConfirmDialog(vue.getFrame(), panel, "Prolonger un Emprunt",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        int nombreJours = extractNombreJoursFromPanel(panel);
                        empruntModel.prolongerEmprunt(id, nombreJours);
                        empruntModel.sauvegraderCSV();
                        JOptionPane.showMessageDialog(vue.getFrame(), "Emprunt prolongé avec succès !");
                        refreshAllTables();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(vue.getFrame(), "Erreur : L'ID doit être un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                } catch (EmpruntNotFoundException ex) {
                    JOptionPane.showMessageDialog(vue.getFrame(), "Erreur : L'emprunt n'existe pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
                } catch (CSVFileException ex) {
                    JOptionPane.showMessageDialog(vue.getFrame(), "Erreur lors de la sauvegarde du fichier CSV : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(vue.getFrame(), ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        vue.getSupprimerEmpruntBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(JOptionPane.showInputDialog("Entrez l'ID de l'emprunt à supprimer :"));
                    Emprunt emprunt = empruntModel.rechercherParID(id);
                    int livreId = emprunt.getLivreId();

                    empruntModel.supprimerEmprunt(id);
                    empruntModel.sauvegraderCSV();

                    // Update livre availability
                    Livre livre = livreModel.rechercherParID(livreId);
                    livre.setDisponible(true);
                    livreModel.modifierLivre(livre.getTitre(), livre.getAuteur(), livre.getAnneePublication(), livre.getGenre(), true, livreId);
                    livreModel.sauvegraderCSV();

                    JOptionPane.showMessageDialog(vue.getFrame(), "Emprunt supprimé avec succès !");
                    refreshAllTables();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(vue.getFrame(), "Erreur : L'ID doit être un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                } catch (EmpruntNotFoundException | LivreNotFoundException ex) {
                    JOptionPane.showMessageDialog(vue.getFrame(), "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                } catch (CSVFileException ex) {
                    JOptionPane.showMessageDialog(vue.getFrame(), "Erreur lors de la sauvegarde du fichier CSV : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        vue.getSearchEmpruntsField().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchQuery = vue.getSearchEmpruntsField().getText();
                filterEmprunts(searchQuery);
            }
        });
    }

    private void refreshAllTables() {
        refreshEmpruntTable();
        refreshLivreTable();
    }

    private void refreshEmpruntTable() {
        vue.getModelEmprunts().setRowCount(0); // Clear old data
        for (Emprunt emprunt : empruntModel.getListe()) {
            try {
                Utilisateur utilisateur = utilisateurModel.rechercherParId(emprunt.getUtilisateurId());
                Livre livre = livreModel.rechercherParID(emprunt.getLivreId());

                vue.getModelEmprunts().addRow(new Object[]{
                        emprunt.getId(),
                        utilisateur.getNom(),
                        livre.getTitre(),
                        emprunt.getDateEmprunt(),
                        emprunt.getDateRetourPrevu()
                });
            } catch (UtilisateurNotFoundException | LivreNotFoundException ex) {
                vue.getModelEmprunts().addRow(new Object[]{
                        emprunt.getId(),
                        "Utilisateur inconnu",
                        "Livre inconnu",
                        emprunt.getDateEmprunt(),
                        emprunt.getDateRetourPrevu()
                });
            }
        }
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

    private void filterEmprunts(String query) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(vue.getModelEmprunts());
        vue.getTableEmprunts().setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(query)));
    }





    private JPanel createProlongementInputPanel(Emprunt emprunt) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField nombreJoursField = new JTextField("7"); // Default to 7 days

        panel.add(new JLabel("Nombre de jours à prolonger :"));
        panel.add(nombreJoursField);

        panel.putClientProperty("nombreJoursField", nombreJoursField);

        return panel;
    }


    private int extractNombreJoursFromPanel(JPanel panel) throws IllegalArgumentException {
        JTextField nombreJoursField = (JTextField) panel.getClientProperty("nombreJoursField");
        String nombreJoursText = nombreJoursField.getText().trim();

        if (nombreJoursText.isEmpty()) {
            throw new IllegalArgumentException("Le nombre de jours doit être rempli.");
        }

        try {
            int nombreJours = Integer.parseInt(nombreJoursText);
            if (nombreJours <= 0) {
                throw new IllegalArgumentException("Le nombre de jours doit être positif.");
            }
            return nombreJours;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le nombre de jours doit être un nombre entier valide.");
        }
    }


}

