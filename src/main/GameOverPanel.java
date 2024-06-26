package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GameOverPanel extends JPanel {
    private GamePanel gamePanel;
    private BufferedImage gameOverImage;
    private BufferedImage tryAgainImage;
    private BufferedImage exitImage;

    public GameOverPanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.setLayout(null);
        this.setOpaque(false);

        // Load the images
        try {
            gameOverImage = ImageIO.read(getClass().getResource("/res/gameover/gameover.png"));
            tryAgainImage = ImageIO.read(getClass().getResource("/res/gameover/tryagainButton.png"));
            exitImage = ImageIO.read(getClass().getResource("/res/gameover/exitButton.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create and add buttons
        JButton tryAgainButton = createButton(tryAgainImage, 175, 260, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.restartGame();
                gamePanel.remove(GameOverPanel.this);
                gamePanel.requestFocusInWindow();
            }
        });
        this.add(tryAgainButton);

        JButton exitButton = createButton(exitImage, 390, 260, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        this.add(exitButton);
    }

    private JButton createButton(BufferedImage image, int x, int y, ActionListener actionListener) {
        JButton button = new JButton();
        button.setBounds(x, y, 200, 80);
        button.setIcon(new ImageIcon(image));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.addActionListener(actionListener);
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0, 0, 0, 150)); // Background with opacity
        g.fillRect(0, 0, getWidth(), getHeight());

        if (gameOverImage != null) {
            int x = (getWidth() - gameOverImage.getWidth()) / 2;
            int y = 100;
            g.drawImage(gameOverImage, x, y, null);
        }
    }
}
