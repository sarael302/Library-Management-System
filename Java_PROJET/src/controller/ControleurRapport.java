package controller;

import exceptions.EmpruntNotFoundException;
import exceptions.LivreNotFoundException;
import exceptions.UtilisateurNotFoundException;
import model.*;
import view.GestionView;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ControleurRapport {

    private final LivreModel livreModel;
    private final UtilisateurModel utilisateurModel;
    private final EmpruntModel empruntModel;
    private final RetourModel retourModel;
    private final GestionView vue;

    public ControleurRapport(LivreModel livreModel, UtilisateurModel utilisateurModel, EmpruntModel empruntModel, RetourModel retourModel, GestionView vue) {
        this.livreModel = livreModel;
        this.utilisateurModel = utilisateurModel;
        this.empruntModel = empruntModel;
        this.retourModel = retourModel;
        this.vue = vue;
        addListeners();
    }

    private void addListeners() {
        vue.getGenererRapportBtn().addActionListener(e -> genererRapportComplet());
        vue.getGenererRapportLivresBtn().addActionListener(e -> afficherRapport(genererRapportLivres()));
        vue.getGenererRapportUtilisateursBtn().addActionListener(e -> afficherRapport(genererRapportUtilisateurs()));
        vue.getGenererRapportEmpruntsBtn().addActionListener(e -> afficherRapport(genererRapportEmprunts()));
        vue.getGenererRapportRetoursBtn().addActionListener(e -> afficherRapport(genererRapportRetours()));
    }

    private void afficherRapport(String rapport) {
        vue.getRapportArea().setText(rapport);
    }

    private void genererRapportComplet() {
        StringBuilder rapport = new StringBuilder("Rapport complet de la bibliothèque\n\n");
        rapport.append(genererStatistiquesGenerales())
                .append("\n")
                .append(genererRapportLivres())
                .append("\n")
                .append(genererRapportUtilisateurs())
                .append("\n")
                .append(genererRapportEmprunts())
                .append("\n")
                .append(genererRapportRetours())
                .append("\n")
                .append(genererGraphiquePopularite())
                .append("\n")
                .append(genererStatistiquesAvancees());

        vue.getRapportArea().setText(rapport.toString());
    }

    private String genererStatistiquesGenerales() {
        StringBuilder stats = new StringBuilder("Statistiques générales :\n");
        stats.append("Nombre total de livres : ").append(livreModel.getListe().size()).append("\n");
        stats.append("Nombre de livres disponibles : ").append(livreModel.getListe().stream().filter(Livre::isDisponible).count()).append("\n");
        stats.append("Nombre total d'utilisateurs : ").append(utilisateurModel.getListe().size()).append("\n");
        stats.append("Nombre total d'emprunts : ").append(empruntModel.getListe().size()).append("\n");
        stats.append("Nombre total de retours : ").append(retourModel.getListe().size()).append("\n");
        return stats.toString();
    }

    private String genererRapportLivres() {
        StringBuilder rapport = new StringBuilder("Rapport des livres :\n\n");

        // Livres les plus empruntés
        Map<Livre, Long> livresEmpruntes = empruntModel.getListe().stream()
                .collect(Collectors.groupingBy(emprunt -> {
                    try {
                        return livreModel.rechercherParID(emprunt.getLivreId());
                    } catch (LivreNotFoundException e) {
                        return new Livre("Inconnu", "Inconnu", 0, "Inconnu", true);
                    }
                }, Collectors.counting()));

        rapport.append("Top 5 des livres les plus empruntés :\n");
        livresEmpruntes.entrySet().stream()
                .sorted(Map.Entry.<Livre, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> rapport.append("- ").append(entry.getKey().getTitre())
                        .append(" (").append(entry.getValue()).append(" emprunts)\n"));

        // Genres les plus populaires
        Map<String, Long> genresPopulaires = livreModel.getListe().stream()
                .collect(Collectors.groupingBy(Livre::getGenre, Collectors.counting()));

        rapport.append("\nTop 3 des genres les plus populaires :\n");
        genresPopulaires.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .forEach(entry -> rapport.append("- ").append(entry.getKey())
                        .append(" (").append(entry.getValue()).append(" livres)\n"));

        // Livres jamais empruntés
        List<Livre> livresJamaisEmpruntes = livreModel.getListe().stream()
                .filter(livre -> !livresEmpruntes.containsKey(livre))
                .collect(Collectors.toList());

        rapport.append("\nLivres jamais empruntés :\n");
        livresJamaisEmpruntes.forEach(livre -> rapport.append("- ").append(livre.getTitre()).append("\n"));

        return rapport.toString();
    }

    private String genererRapportUtilisateurs() {
        StringBuilder rapport = new StringBuilder("Rapport des utilisateurs :\n\n");

        // Utilisateurs les plus actifs
        Map<Utilisateur, Long> utilisateursActifs = empruntModel.getListe().stream()
                .collect(Collectors.groupingBy(emprunt -> {
                    try {
                        return utilisateurModel.rechercherParId(emprunt.getUtilisateurId());
                    } catch (UtilisateurNotFoundException e) {
                        return new Utilisateur("Inconnu", "", "");
                    }
                }, Collectors.counting()));

        rapport.append("Top 5 des utilisateurs les plus actifs :\n");
        utilisateursActifs.entrySet().stream()
                .sorted(Map.Entry.<Utilisateur, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> rapport.append("- ").append(entry.getKey().getNom())
                        .append(" (").append(entry.getValue()).append(" emprunts)\n"));

        // Utilisateurs avec des emprunts en retard
        List<Utilisateur> utilisateursEnRetard = empruntModel.getListe().stream()
                .filter(emprunt -> LocalDate.parse(emprunt.getDateRetourPrevu()).isBefore(LocalDate.now()))
                .map(emprunt -> {
                    try {
                        return utilisateurModel.rechercherParId(emprunt.getUtilisateurId());
                    } catch (UtilisateurNotFoundException e) {
                        return new Utilisateur("Inconnu", "", "");
                    }
                })
                .distinct()
                .collect(Collectors.toList());


        return rapport.toString();
    }

    private String genererRapportEmprunts() {
        StringBuilder rapport = new StringBuilder("Rapport des emprunts :\n\n");

        long empruntsEnCours = empruntModel.getListe().stream()
                .filter(emprunt -> !retourModel.getListe().stream()
                        .anyMatch(retour -> retour.getEmpruntId() == emprunt.getId()))
                .count();

        rapport.append("Nombre d'emprunts en cours : ").append(empruntsEnCours).append("\n");

        // Durée moyenne des emprunts
        OptionalDouble dureeMoyenne = retourModel.getListe().stream()
                .mapToLong(retour -> {
                    try {
                        Emprunt emprunt = empruntModel.rechercherParID(retour.getEmpruntId());
                        return LocalDate.parse(retour.getDateRetour()).toEpochDay() - LocalDate.parse(emprunt.getDateEmprunt()).toEpochDay();
                    } catch (EmpruntNotFoundException e) {
                        return 0;
                    }
                })
                .average();

        rapport.append("Durée moyenne des emprunts : ")
                .append(dureeMoyenne.isPresent() ? String.format("%.2f jours", dureeMoyenne.getAsDouble()) : "N/A")
                .append("\n");

        // Emprunts par mois
        Map<String, Long> empruntParMois = empruntModel.getListe().stream()
                .collect(Collectors.groupingBy(
                        emprunt -> LocalDate.parse(emprunt.getDateEmprunt()).getMonth().toString(),
                        Collectors.counting()
                ));

        rapport.append("\nNombre d'emprunts par mois :\n");
        empruntParMois.forEach((mois, nombre) -> rapport.append("- ").append(mois).append(": ").append(nombre).append("\n"));

        return rapport.toString();
    }

    private String genererRapportRetours() {
        StringBuilder rapport = new StringBuilder("Rapport des retours :\n\n");

        // Calcul des pénalités totales
        double penalitesTotales = retourModel.getListe().stream()
                .mapToDouble(Retour::getPenalite)
                .sum();

        rapport.append("Total des pénalités : ").append(String.format("%.2f DH", penalitesTotales)).append("\n");

        // Nombre de retours en retard
        long retoursEnRetard = retourModel.getListe().stream()
                .filter(retour -> retour.getPenalite() > 0)
                .count();

        rapport.append("Nombre de retours en retard : ").append(retoursEnRetard).append("\n");

        // Retours par mois
        Map<String, Long> retourParMois = retourModel.getListe().stream()
                .collect(Collectors.groupingBy(
                        retour -> LocalDate.parse(retour.getDateRetour()).getMonth().toString(),
                        Collectors.counting()
                ));

        rapport.append("\nNombre de retours par mois :\n");
        retourParMois.forEach((mois, nombre) -> rapport.append("- ").append(mois).append(": ").append(nombre).append("\n"));

        return rapport.toString();
    }

    private String genererGraphiquePopularite() {
        StringBuilder graph = new StringBuilder("Graphique de popularité des genres:\n\n");
        Map<String, Long> genresPopulaires = livreModel.getListe().stream()
                .collect(Collectors.groupingBy(Livre::getGenre, Collectors.counting()));

        int maxCount = genresPopulaires.values().stream().mapToInt(Long::intValue).max().orElse(0);
        int scale = Math.max(1, maxCount / 20);

        genresPopulaires.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> {
                    graph.append(String.format("%-15s |", entry.getKey()));
                    int bars = (int) (entry.getValue() / scale);
                    graph.append("█".repeat(bars)).append(" ").append(entry.getValue()).append("\n");
                });

        return graph.toString();
    }

    private String genererStatistiquesAvancees() {
        StringBuilder stats = new StringBuilder("Statistiques avancées:\n\n");

        // Taux de rotation des livres
        double tauxRotation = (double) empruntModel.getListe().size() / livreModel.getListe().size();
        stats.append(String.format("Taux de rotation des livres: %.2f\n", tauxRotation));

        // Durée moyenne des emprunts
        OptionalDouble dureeMoyenne = retourModel.getListe().stream()
                .mapToLong(retour -> {
                    try {
                        Emprunt emprunt = empruntModel.rechercherParID(retour.getEmpruntId());
                        return LocalDate.parse(retour.getDateRetour()).toEpochDay() - LocalDate.parse(emprunt.getDateEmprunt()).toEpochDay();
                    } catch (EmpruntNotFoundException e) {
                        return 0;
                    }
                })
                .average();

        stats.append(String.format("Durée moyenne des emprunts: %.2f jours\n", dureeMoyenne.orElse(0)));

        // Utilisateurs les plus actifs ce mois-ci
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        Map<Utilisateur, Long> utilisateursActifsMois = empruntModel.getListe().stream()
                .filter(emprunt -> LocalDate.parse(emprunt.getDateEmprunt()).isAfter(startOfMonth))
                .collect(Collectors.groupingBy(emprunt -> {
                    try {
                        return utilisateurModel.rechercherParId(emprunt.getUtilisateurId());
                    } catch (UtilisateurNotFoundException e) {
                        return new Utilisateur("Inconnu", "", "");
                    }
                }, Collectors.counting()));

        stats.append("Top 3 des utilisateurs les plus actifs ce mois-ci:\n");
        utilisateursActifsMois.entrySet().stream()
                .sorted(Map.Entry.<Utilisateur, Long>comparingByValue().reversed())
                .limit(3)
                .forEach(entry -> stats.append(String.format("- %s: %d emprunts\n", entry.getKey().getNom(), entry.getValue())));

        return stats.toString();
    }
}