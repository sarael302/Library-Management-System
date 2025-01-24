package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class VueLivre extends JPanel {
    private JButton ajouterLivreBtn;
    private JButton modifierLivreBtn;
    private JButton supprimerLivreBtn;
    private JButton sauvegarderBtn;
    private JTextField searchLivresField;
    private DefaultTableModel modelLivres;
    private JTable tableLivres;
    private Image backgroundImage;

    public VueLivre() {
        // configure le gestionnaire de disposition
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
        ajouterLivreBtn = createStyledButton("Ajouter Livre", new Color(76, 175, 80));
        modifierLivreBtn = createStyledButton("Modifier Livre", new Color(3, 169, 244));
        supprimerLivreBtn = createStyledButton("Supprimer Livre", new Color(244, 67, 54));
        sauvegarderBtn = createStyledButton("Sauvegarder", new Color(255, 152, 0));
    }

    private void initializeSearchField() {
        //wide enough to show about 20 characters
        searchLivresField = new JTextField(20);
        // Combines two borders into one inner and outer
        searchLivresField.setBorder(BorderFactory.createCompoundBorder(
                //outer border / padding
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                //inner border
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        searchLivresField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void initializeTable() {
        //the argument (0) means the table starts empty table.
        modelLivres = new DefaultTableModel(new Object[]{"ID", "Titre", "Auteur", "Année de Publication", "Genre", "Disponible"}, 0);
        tableLivres = new JTable(modelLivres);
        tableLivres.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableLivres.setRowHeight(30);
        tableLivres.setShowVerticalLines(true);
        tableLivres.setIntercellSpacing(new Dimension(0, 10));

        JTableHeader header = tableLivres.getTableHeader();
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
        topPanel.add(searchLivresField);

        return topPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(tableLivres);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        centerPanel.add(scrollPane, BorderLayout.CENTER);

        return centerPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        bottomPanel.setOpaque(false);

        bottomPanel.add(ajouterLivreBtn);
        bottomPanel.add(modifierLivreBtn);
        bottomPanel.add(supprimerLivreBtn);
        bottomPanel.add(sauvegarderBtn);

        return bottomPanel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                //to reduce pixelation
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();

                g2.setColor(color);
                g2.fill(new RoundRectangle2D.Float(0, 0, width, height, 10, 10));

                g2.setColor(new Color(255, 255, 255, 100));
                g2.drawRoundRect(0, 0, width - 1, height - 1, 10, 10);

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

    }

    // Getter methods remain unchanged
    public JButton getAjouterLivreBtn() { return ajouterLivreBtn; }
    public JButton getModifierLivreBtn() { return modifierLivreBtn; }
    public JButton getSupprimerLivreBtn() { return supprimerLivreBtn; }
    public JButton getSauvegarderBtn() { return sauvegarderBtn; }
    public JTable getTableLivres() { return tableLivres; }
    public JTextField getSearchLivresField() { return searchLivresField; }
    public DefaultTableModel getModelLivres() { return modelLivres; }
}