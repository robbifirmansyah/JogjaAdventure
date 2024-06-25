package main;

import entity.Player;
import main.tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable, GameControl {
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;  // Sesuaikan jumlah kolom layar
    final int maxScreenRow = 12;  // Sesuaikan jumlah baris layar
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // World settings
    public final int maxWorldCol = 48;  // Misalnya ukuran peta 50x50 tiles
    public final int maxWorldRow = 108;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    int FPS = 60;
    boolean paused = false;

    KeyHandler keyHandler = new KeyHandler(this);
    Thread gameThread;

    public Player player = new Player(this, keyHandler);
    List<Enemy> enemies = new ArrayList<>();

    private AudioPlayer backgroundMusic;

    private TileManager tileManager = new TileManager(this);

    public AudioPlayer getBackgroundMusic() {
        return backgroundMusic;
    }

    public TileManager getTileManager() {
        return tileManager;
    }

    public GamePanel(AudioPlayer backgroundMusic) {
        this.backgroundMusic = backgroundMusic;
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    @Override
    public void startGame() {
        startGameThread();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime();
        while (gameThread != null) {
            if (!paused) {
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
        }
    }

    public void update() {
        player.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw the map
        tileManager.draw(g2);

        // Draw the player
        player.draw(g2);

        g2.dispose();
    }

    @Override
    public void togglePause() {
        paused = !paused;
        if (paused) {
            backgroundMusic.pause();
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame == null) {
                System.err.println("topFrame is null. Ensure GamePanel is added to a JFrame before calling togglePause.");
                return;
            }
            JLayeredPane layeredPane = new JLayeredPane();
            layeredPane.setPreferredSize(new Dimension(screenWidth, screenHeight));
            PausePanel pausePanel = new PausePanel(this);
            pausePanel.setBounds(0, 0, screenWidth, screenHeight);
            layeredPane.add(this, JLayeredPane.DEFAULT_LAYER);
            layeredPane.add(pausePanel, JLayeredPane.PALETTE_LAYER);
            topFrame.setContentPane(layeredPane);
            topFrame.validate();
        } else {
            backgroundMusic.resume();
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.setContentPane(this);
            topFrame.validate();
            this.requestFocus();
        }
    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public void resumeGame() {
        if (paused) {
            togglePause();
        }
    }

    @Override
    public void pauseGame() {
        if (!paused) {
            togglePause();
        }
    }

    @Override
    public void quitGame() {
        System.exit(0);
    }

    // Method to toggle movement restriction
    public void toggleMovementRestriction(boolean enable) {
        player.setRestrictMovement(enable);
    }

}