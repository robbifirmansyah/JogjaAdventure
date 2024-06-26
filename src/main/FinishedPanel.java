package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FinishedPanel extends JPanel {

    private BufferedImage finishedImage;
    private BufferedImage tryAgainImage;
    private BufferedImage exitImage;

    public FinishedPanel() {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        try {
            finishedImage = ImageIO.read(getClass().getResource("/res/finished/finished.png"));
            tryAgainImage = ImageIO.read(getClass().getResource("/res/finished/tryagainButton.png"));
            exitImage = ImageIO.read(getClass().getResource("/res/finished/exitButton.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Creating the layered pane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(finishedImage.getWidth(), finishedImage.getHeight()));

        // Adding finished image label at the lowest layer
        JLabel finishedLabel = new JLabel(new ImageIcon(finishedImage));
        finishedLabel.setBounds(150, 100, finishedImage.getWidth(), finishedImage.getHeight());
        layeredPane.add(finishedLabel, JLayeredPane.DEFAULT_LAYER);

        // Adding try again button label
        JLabel tryAgainLabel = new JLabel(new ImageIcon(tryAgainImage));
        tryAgainLabel.setBounds(225, 260, tryAgainImage.getWidth(), tryAgainImage.getHeight());
        tryAgainLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                restartGame();
            }
        });
        layeredPane.add(tryAgainLabel, JLayeredPane.PALETTE_LAYER);

        // Adding exit button label
        JLabel exitLabel = new JLabel(new ImageIcon(exitImage));
        exitLabel.setBounds(100, 100, exitImage.getWidth(), exitImage.getHeight());
        exitLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
        layeredPane.add(exitLabel, JLayeredPane.PALETTE_LAYER);

        // Adding the layered pane to the panel
        this.add(layeredPane, BorderLayout.CENTER);
    }

    private void restartGame() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame == null) {
            System.err.println("Top frame is null. Cannot restart game.");
            return;
        }
        AudioPlayer backgroundMusic = new AudioPlayer(); // Create or obtain an AudioPlayer object
        GamePanel gamePanel = new GamePanel(backgroundMusic); // Pass the AudioPlayer object
        topFrame.setContentPane(gamePanel);
        topFrame.validate();
        gamePanel.startGame();
    }
}
