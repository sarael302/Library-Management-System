package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class VueRetour extends JPanel {
    private JButton ajouterRetourBtn;
    private JButton supprimerRetourBtn;
    private JTextField searchRetoursField;
    private DefaultTableModel modelRetours;
    private JTable tableRetours;
    private Image backgroundImage;

    public VueRetour() {
        setLayout(new BorderLayout());
        backgroundImage = new ImageIcon(getClass().getResource("/image/image.png")).getImage();

        initializeComponents();
        createPanels();
        styleComponents();
    }

    private void initializeComponents() {
        ajouterRetourBtn = createStyledButton("Ajouter Retour", new Color(76, 175, 80));
        supprimerRetourBtn = createStyledButton("Supprimer Retour", new Color(244, 67, 54));

        searchRetoursField = new JTextField(20);
        searchRetoursField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        searchRetoursField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        modelRetours = new DefaultTableModel(new Object[]{"ID", "Emprunt ID", "Date de Retour", "Pénalité"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableRetours = new JTable(modelRetours);
    }

    private void createPanels() {
        JPanel topPanel = createTopPanel();
        JPanel centerPanel = createCenterPanel();
        JPanel bottomPanel = createBottomPanel();

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        topPanel.setOpaque(false);

        JLabel searchLabel = new JLabel("Rechercher :");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchLabel.setForeground(Color.WHITE);

        topPanel.add(searchLabel);
        topPanel.add(searchRetoursField);

        return topPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        styleTable();
        JScrollPane scrollPane = new JScrollPane(tableRetours);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        centerPanel.add(scrollPane, BorderLayout.CENTER);

        return centerPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        bottomPanel.setOpaque(false);

        bottomPanel.add(ajouterRetourBtn);
        bottomPanel.add(supprimerRetourBtn);

        return bottomPanel;
    }

    private void styleComponents() {
        setBackground(new Color(240, 240, 240));
    }

    private void styleTable() {
        tableRetours.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableRetours.setRowHeight(30);
        tableRetours.setShowVerticalLines(true);
        tableRetours.setIntercellSpacing(new Dimension(0, 10));
        tableRetours.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = tableRetours.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(0, 0, 0));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
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
        button.setPreferredSize(new Dimension(180, 40));

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

    public JButton getAjouterRetourBtn() {
        return ajouterRetourBtn;
    }

    public JButton getSupprimerRetourBtn() {
        return supprimerRetourBtn;
    }

    public JTextField getSearchRetoursField() {
        return searchRetoursField;
    }

    public DefaultTableModel getModelRetours() {
        return modelRetours;
    }


}