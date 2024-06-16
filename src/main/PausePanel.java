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

public class PausePanel extends JPanel {
    GamePanel gamePanel;
    private JLabel volumeButton;
    private int volumeLevel = 1;

    private BufferedImage pausedImage;
    private BufferedImage resumeImage;
    private BufferedImage exitImage;
    private BufferedImage volumeMute;
    private BufferedImage volume1;
    private BufferedImage volume2;
    private BufferedImage volume3;
    private AudioPlayer backgroundMusic;


    public PausePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.setLayout(null);
        this.setOpaque(false);

        // Load the images
        try {
            pausedImage = ImageIO.read(getClass().getResource("/res/pause/game_paused.png"));
            resumeImage = ImageIO.read(getClass().getResource("/res/pause/resume_button.png"));
            exitImage = ImageIO.read(getClass().getResource("/res/pause/exit_button.png"));
            volumeMute = ImageIO.read(getClass().getResource("/res/pause/volume_mute.png"));
            volume1 = ImageIO.read(getClass().getResource("/res/pause/volume_1.png"));
            volume2 = ImageIO.read(getClass().getResource("/res/pause/volume_2.png"));
            volume3 = ImageIO.read(getClass().getResource("/res/pause/volume_3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create and add buttons
        JButton resumeButton = createButton(resumeImage, 175, 260, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.togglePause();
            }
        });
        this.add(resumeButton);

        JButton exitButton = createButton(exitImage, 390, 260, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        this.add(exitButton);

        // Volume button
        volumeButton = new JLabel(new ImageIcon(volume1));
        volumeButton.setBounds(285, 345, 200, 80);
        volumeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                volumeLevel = (volumeLevel + 1) % 4;
                switch (volumeLevel) {
                    case 0:
                        volumeButton.setIcon(new ImageIcon(volumeMute));
                        gamePanel.getBackgroundMusic().setVolume(-80.0f); // Mute
                        break;
                    case 1:
                        volumeButton.setIcon(new ImageIcon(volume1));
                        gamePanel.getBackgroundMusic().setVolume(-20.0f); // Low volume
                        break;
                    case 2:
                        volumeButton.setIcon(new ImageIcon(volume2));
                        gamePanel.getBackgroundMusic().setVolume(-10.0f); // Medium volume
                        break;
                    case 3:
                        volumeButton.setIcon(new ImageIcon(volume3));
                        gamePanel.getBackgroundMusic().setVolume(0.0f); // Full volume
                        break;
                }
            }
        });
        this.add(volumeButton);
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

        if (pausedImage != null) {
            int x = (getWidth() - pausedImage.getWidth()) / 2;
            int y = 100;
            g.drawImage(pausedImage, x, y, null);
        }
    }
}
