package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class VueUtilisateur extends JPanel {
    private JButton ajouterUtilisateurBtn;
    private JButton modifierUtilisateurBtn;
    private JButton supprimerUtilisateurBtn;
    private JButton sauvegarderBtn;
    private JTextField searchUtilisateursField;
    private DefaultTableModel modelUtilisateurs;
    private JTable tableUtilisateurs;
    private Image backgroundImage;

    public VueUtilisateur() {
        setLayout(new BorderLayout());
        backgroundImage = new ImageIcon(getClass().getResource("/image/image.png")).getImage();

        // Initialize components
        initializeButtons();
        initializeSearchField();
        initializeTable();

        // Create panels
        JPanel topPanel = createTopPanel();
        JPanel centerPanel = createCenterPanel();
        JPanel bottomPanel = createBottomPanel();

        // Add panels to main layout
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void initializeButtons() {
        ajouterUtilisateurBtn = createStyledButton("Ajouter Utilisateur", new Color(76, 175, 80));
        modifierUtilisateurBtn = createStyledButton("Modifier Utilisateur", new Color(3, 169, 244));
        supprimerUtilisateurBtn = createStyledButton("Supprimer Utilisateur", new Color(244, 67, 54));
        sauvegarderBtn = createStyledButton("Sauvegarder", new Color(255, 152, 0));
    }

    private void initializeSearchField() {
        searchUtilisateursField = new JTextField(20);
        searchUtilisateursField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        searchUtilisateursField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void initializeTable() {
        modelUtilisateurs = new DefaultTableModel(new Object[]{"ID", "Nom complet", "Email", "Numéro de téléphone"}, 0);
        tableUtilisateurs = new JTable(modelUtilisateurs);
        tableUtilisateurs.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableUtilisateurs.setRowHeight(30);
        tableUtilisateurs.setShowVerticalLines(true);
        tableUtilisateurs.setIntercellSpacing(new Dimension(0, 10));

        JTableHeader header = tableUtilisateurs.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(0, 0, 0));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        topPanel.setOpaque(false);

        JLabel searchLabel = new JLabel("Rechercher :");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchLabel.setForeground(Color.WHITE);

        topPanel.add(searchLabel);
        topPanel.add(searchUtilisateursField);

        return topPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(tableUtilisateurs);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        centerPanel.add(scrollPane, BorderLayout.CENTER);

        return centerPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        bottomPanel.setOpaque(false);

        bottomPanel.add(ajouterUtilisateurBtn);
        bottomPanel.add(modifierUtilisateurBtn);
        bottomPanel.add(supprimerUtilisateurBtn);
        bottomPanel.add(sauvegarderBtn);

        return bottomPanel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();

                g2.setColor(color);
                g2.fill(new RoundRectangle2D.Float(0, 0, width, height, 10, 10));

                g2.setColor(new Color(255, 255, 255, 100));
                g2.drawRoundRect(0, 0, width - 1, height - 1, 10, 10);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 40));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setFont(button.getFont().deriveFont(Font.BOLD, 15));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setFont(button.getFont().deriveFont(Font.BOLD, 14));
            }
        });

        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();

        g2d.drawImage(backgroundImage, 0, 0, w, h, this);

        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, w, h);

        g2d.dispose();
    }

    // Getter methods
    public JButton getAjouterUtilisateurBtn() { return ajouterUtilisateurBtn; }
    public JButton getModifierUtilisateurBtn() { return modifierUtilisateurBtn; }
    public JButton getSupprimerUtilisateurBtn() { return supprimerUtilisateurBtn; }
    public JButton getSauvegarderBtn() { return sauvegarderBtn; }
    public JTextField getSearchUtilisateursField() { return searchUtilisateursField; }
    public DefaultTableModel getModelUtilisateurs() { return modelUtilisateurs; }
    public JTable getTableUtilisateurs() { return tableUtilisateurs; }

}