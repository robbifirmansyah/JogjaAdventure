package main;

import entity.Player;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable, GameControl {
    // SCREEN SETTINGS
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768
    final int screenHeight = tileSize * maxScreenRow; // 576

    int FPS = 60;
    int drawCount = 0;
    boolean paused = false; // Add a paused state

    KeyHandler keyHandler = new KeyHandler(this);
    Thread gameThread;
    Player player = new Player(this, keyHandler);

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime();
        long fpsCounter = 0;
        long startTime = System.nanoTime();
        while (gameThread != null) {
            if (!paused) { // Only update and repaint if not paused
                update();
                repaint();
            }
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            drawCount++;
            fpsCounter++;

            // If a second has passed, print the FPS and reset the counter
            if (System.nanoTime() - startTime >= 1000000000) {
                System.out.println("FPS: " + fpsCounter);
                fpsCounter = 0;
                startTime = System.nanoTime();
            }
        }
    }

    public void update() {
        player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // draw the game screen
        Graphics2D g2 = (Graphics2D) g;
        player.draw(g2);
        g2.dispose();
    }

    @Override
    public void startGame() {
        paused = false;
        if (gameThread == null) {
            startGameThread();
        }
        this.requestFocusInWindow(); // Ensure the game panel has focus
    }

    @Override
    public void pauseGame() {
        paused = true;
        // Show pause menu dialog
        SwingUtilities.invokeLater(() -> {
            int result = JOptionPane.showOptionDialog(this, "Game Paused", "Pause",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, new String[]{"Resume", "Quit"}, "Resume");
            if (result == 0) {
                paused = false;
                this.requestFocusInWindow(); // Ensure the game panel has focus
            } else if (result == 1) {
                quitGame();
            }
        });
    }

    @Override
    public void quitGame() {
        paused = true;
        gameThread = null;
        System.exit(0);
    }
}
