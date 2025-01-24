package view;

import javax.swing.*;
import java.awt.*;

public class TestImage {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Image Test");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);

        // Load the image
        JLabel imageLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(TestImage.class.getResource("/image/image.png"));
            imageLabel.setIcon(icon);
        } catch (NullPointerException e) {
            System.err.println("Image not found! Check the path or file name.");
        }

        panel.add(imageLabel);
        frame.add(panel);
        frame.setVisible(true);
    }
}
