package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class VueAccueil extends JPanel {
    private JButton gestionLivresBtn;
    private JButton gestionUtilisateursBtn;
    private JButton gestionEmpruntsBtn;
    private JButton gestionRetoursBtn;
    private JButton rapportsBtn;
    private Image backgroundImage;

    public VueAccueil() {
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 240, 245));

        // Load background image
        backgroundImage = new ImageIcon(getClass().getResource("/image/image.png")).getImage();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = createStyledLabel("Bienvenue dans votre Système de Gestion de Bibliothèque", 32, Font.BOLD);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        JLabel subtitleLabel = createStyledLabel("Sélectionnez une option pour commencer", 20, Font.ITALIC);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 15, 30, 15);
        add(subtitleLabel, gbc);

        gestionLivresBtn = createStyledButton("Gestion des Livres", new Color(0, 121, 107));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(gestionLivresBtn, gbc);

        gestionUtilisateursBtn = createStyledButton("Gestion des Utilisateurs", new Color(1, 87, 155));
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(gestionUtilisateursBtn, gbc);

        gestionEmpruntsBtn = createStyledButton("Gestion des Emprunts", new Color(194, 24, 91));
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(gestionEmpruntsBtn, gbc);

        gestionRetoursBtn = createStyledButton("Gestion des Retours", new Color(230, 74, 25));
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(gestionRetoursBtn, gbc);

        rapportsBtn = createStyledButton("Rapports", new Color(62, 39, 35));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(rapportsBtn, gbc);
    }

    private JLabel createStyledLabel(String text, int fontSize, int fontStyle) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", fontStyle, fontSize));
        label.setForeground(new Color(33, 33, 33));
        return label;
    }

    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();

                GradientPaint gradient = new GradientPaint(0, 0, baseColor, 0, height, baseColor.darker());
                g2.setPaint(gradient);
                g2.fill(new RoundRectangle2D.Float(0, 0, width, height, 15, 15));

                g2.setColor(new Color(255, 255, 255, 100));
                g2.drawRoundRect(0, 0, width - 1, height - 1, 15, 15);

                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setFont(button.getFont().deriveFont(Font.BOLD, 17));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setFont(button.getFont().deriveFont(Font.BOLD, 16));
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

        // Draw the background image
        g2d.drawImage(backgroundImage, 0, 0, w, h, this);

        // Apply a semi-transparent overlay
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.fillRect(0, 0, w, h);

        g2d.dispose();
    }

    public void addGestionLivresListener(ActionListener listener) {
        gestionLivresBtn.addActionListener(listener);
    }

    public void addGestionUtilisateursListener(ActionListener listener) {
        gestionUtilisateursBtn.addActionListener(listener);
    }

    public void addGestionEmpruntsListener(ActionListener listener) {
        gestionEmpruntsBtn.addActionListener(listener);
    }

    public void addGestionRetoursListener(ActionListener listener) {
        gestionRetoursBtn.addActionListener(listener);
    }

    public void addRapportsListener(ActionListener listener) {
        rapportsBtn.addActionListener(listener);
    }
}