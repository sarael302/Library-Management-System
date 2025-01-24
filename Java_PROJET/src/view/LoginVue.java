package view;

import controller.ControleurAuth;
import exceptions.AuthException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.util.Objects;

public class LoginVue extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;


    private final ControleurAuth controleurAuth;
    private final GestionView vuePrincipale;

    private static final Color PRIMARY_COLOR = new Color(0, 0, 0);
    private static final Color SECONDARY_COLOR = new Color(0, 0, 0);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);

    public LoginVue(ControleurAuth controleurAuth, GestionView vuePrincipale) {
        this.controleurAuth = controleurAuth;
        this.vuePrincipale = vuePrincipale;

        setTitle("Connexion à votre compte");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(createImagePanel(), gbc);

        gbc.gridx = 1;
        mainPanel.add(createFormPanel(), gbc);

        add(mainPanel);

        setVisible(true);
    }

    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR, 0, getHeight(), SECONDARY_COLOR);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.dispose();
            }
        };

        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/image/image.png")));
            Image img = icon.getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(img));
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            imagePanel.add(imageLabel, BorderLayout.CENTER);
        } catch (NullPointerException e) {
            System.err.println("Image not found! Check the path: /image/image.png");
        }

        return imagePanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JLabel titleLabel = new JLabel("Connexion");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(titleLabel, gbc);

        gbc.insets = new Insets(10, 0, 10, 0);

        usernameField = createStyledTextField("Adresse e-mail");
        formPanel.add(usernameField, gbc);

        passwordField = createStyledPasswordField("Mot de passe");
        formPanel.add(passwordField, gbc);




        loginButton = createStyledButton("Se connecter");
        gbc.insets = new Insets(30, 0, 10, 0);
        formPanel.add(loginButton, gbc);


        addActionListeners();

        return formPanel;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField textField = new JTextField(15);
        textField.setFont(MAIN_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, SECONDARY_COLOR),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        textField.setForeground(SECONDARY_COLOR);
        textField.setText(placeholder);
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(SECONDARY_COLOR);
                    textField.setText(placeholder);
                }
            }
        });
        return textField;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(MAIN_FONT);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, SECONDARY_COLOR),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        passwordField.setForeground(SECONDARY_COLOR);
        passwordField.setEchoChar((char) 0);
        passwordField.setText(placeholder);
        passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (String.valueOf(passwordField.getPassword()).equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setEchoChar('•');
                    passwordField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setEchoChar((char) 0);
                    passwordField.setForeground(SECONDARY_COLOR);
                    passwordField.setText(placeholder);
                }
            }
        });
        return passwordField;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(SECONDARY_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
        return button;
    }

    private void addActionListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.equals("Adresse e-mail") || password.equals("Mot de passe")) {
                    showError("Veuillez entrer votre adresse e-mail et votre mot de passe.");
                    return;
                }

                try {
                    controleurAuth.authentifier(username, password);
                    JOptionPane.showMessageDialog(LoginVue.this, "Connexion réussie", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    vuePrincipale.setVisible(true);
                } catch (AuthException ex) {
                    showError(ex.getMessage());
                }
            }
        });


    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}