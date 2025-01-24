package view;

import javax.swing.*;
import java.awt.*;

public class VueRapport extends JPanel {
    private JButton genererRapportBtn;
    private JButton genererRapportLivresBtn;
    private JButton genererRapportUtilisateursBtn;
    private JButton genererRapportEmpruntsBtn;
    private JButton genererRapportRetoursBtn;
    private JTextArea rapportArea;

    public VueRapport() {
        setLayout(new BorderLayout());
        setBackground(new Color(250, 250, 250));

        // Initialize buttons
        genererRapportBtn = createStyledButton("Rapport Complet");
        genererRapportLivresBtn = createStyledButton("Rapport Livres");
        genererRapportUtilisateursBtn = createStyledButton("Rapport Utilisateurs");
        genererRapportEmpruntsBtn = createStyledButton("Rapport Emprunts");
        genererRapportRetoursBtn = createStyledButton("Rapport Retours");

        // Initialize the text area to display the report
        rapportArea = new JTextArea();
        styleTextArea(rapportArea);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(250, 250, 250));
        buttonPanel.add(genererRapportBtn);
        buttonPanel.add(genererRapportLivresBtn);
        buttonPanel.add(genererRapportUtilisateursBtn);
        buttonPanel.add(genererRapportEmpruntsBtn);
        buttonPanel.add(genererRapportRetoursBtn);

        // Create report panel
        JPanel rapportPanel = new JPanel(new BorderLayout());
        rapportPanel.setBackground(new Color(250, 250, 250));
        rapportPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(rapportArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        rapportPanel.add(scrollPane, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.NORTH);
        add(rapportPanel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 0, 0));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(32, 30, 30).darker()),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(32, 30, 30).darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 0, 0));
            }
        });

        return button;
    }

    private void styleTextArea(JTextArea textArea) {
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        textArea.setBackground(Color.WHITE);
    }

    // Getters for the buttons and text area
    public JButton getGenererRapportBtn() { return genererRapportBtn; }
    public JButton getGenererRapportLivresBtn() { return genererRapportLivresBtn; }
    public JButton getGenererRapportUtilisateursBtn() { return genererRapportUtilisateursBtn; }
    public JButton getGenererRapportEmpruntsBtn() { return genererRapportEmpruntsBtn; }
    public JButton getGenererRapportRetoursBtn() { return genererRapportRetoursBtn; }
    public JTextArea getRapportArea() { return rapportArea; }
}