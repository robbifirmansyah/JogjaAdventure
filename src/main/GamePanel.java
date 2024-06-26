package main;

import entity.Player;
import main.tile.TileManager;
import entity.Enemy;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GamePanel extends JPanel implements Runnable, GameControl {
    private boolean gameFinished = false;
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
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;

    private AudioPlayer backgroundMusic;

    private TileManager tileManager = new TileManager(this);

    public AudioPlayer getBackgroundMusic() {
        return backgroundMusic;
    }

    public TileManager getTileManager() {
        return tileManager;
    }

    private Timer gameTimer;
    private long startTime;
    private long elapsedTime;
    private JLabel timerLabel;
    private boolean gameOver = false;
    private GameOverPanel gameOverPanel;
    private FinishedPanel finishedPanel;

    public GamePanel(AudioPlayer backgroundMusic) {
        this.backgroundMusic = backgroundMusic;
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        // Initialize Timer
        gameTimer = new Timer();
        timerLabel = new JLabel("Time: 0");
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setFont(new Font("OneSize", Font.PLAIN, 20));
        this.add(timerLabel);
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

    }

    @Override
    public void startGame() {
        startTime = System.currentTimeMillis();
        startGameThread();
        startTimer();
    }

    private void startTimer() {
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                elapsedTime = System.currentTimeMillis() - startTime;
                timerLabel.setText("Time: " + (elapsedTime / 1000) + " seconds");
            }
        }, 0, 1000); // Update every second
    }

    private void stopTimer() {
        gameTimer.cancel();
    }

    // Call this method when the game is completed
    public void completeGame() {
        stopTimer();
        saveHighScore(elapsedTime / 1000); // Save elapsed time in seconds
    }

    private void saveHighScore(long timeInSeconds) {
        String playerName = JOptionPane.showInputDialog(this, "Congratulations! Enter your name:");
        if (playerName != null && !playerName.isEmpty()) {
            HighScoreManager highScoreManager = new HighScoreManager();
            highScoreManager.addHighScore(new HighScoreEntry(playerName, timeInSeconds));
            highScoreManager.saveHighScores();
        }
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

    public void setupGame() {
        player.setDefaultValues();
        enemies.add(new Enemy(this, 2143, 2276)); // Using tile codes for enemy patrol route
        gameState = playState;
    }

    public void update() {
        if (gameOver) {
            return;
        }
        player.update();
        checkPlayerPosition();
        for (Enemy enemy : enemies) {
            enemy.update(); // Update each enemy
        }
    }

    private void checkPlayerPosition() {
        int playerTileNum = (player.worldY / tileSize) * maxWorldCol + (player.worldX / tileSize);
        if (playerTileNum == 2901 || playerTileNum == 2902 || playerTileNum == 2903 || playerTileNum == 2904 || playerTileNum == 2905) {
            showFinishedPanel();
        }
    }

    private void showFinishedPanel() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame == null) {
            System.err.println("topFrame is null. Ensure GamePanel is added to a JFrame before calling showFinishedPanel.");
            return;
        }

        finishedPanel = new FinishedPanel();
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(screenWidth, screenHeight));

        // Add GamePanel to layered pane
        this.setBounds(0, 0, screenWidth, screenHeight);
        layeredPane.add(this, JLayeredPane.DEFAULT_LAYER);

        // Add FinishedPanel to layered pane (above GamePanel)
        finishedPanel.setBounds(0, 0, screenWidth, screenHeight);
        layeredPane.add(finishedPanel, JLayeredPane.PALETTE_LAYER); // Ensure it's in the foreground

        topFrame.setContentPane(layeredPane);
        topFrame.validate();
    }



    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw the map
        tileManager.draw(g2);

        // Draw the player
        player.draw(g2);
        for (Enemy enemy : enemies) {
            enemy.draw(g2); // Draw each enemy
        }
        g2.dispose();
    }

    @Override
    public void togglePause() {
        paused = !paused;
        if (paused) {
            backgroundMusic.pause();
            showPausePanel();
        } else {
            backgroundMusic.resume();
            removePausePanel();
        }
    }

    private void showPausePanel() {
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
    }

    private void removePausePanel() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        topFrame.setContentPane(this);
        topFrame.validate();
        this.requestFocus();
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

    // Method to restart the game
    public void restartGame() {
        gameOver = false;
        player.setDefaultValues();
        enemies.clear();
        setupGame();
        startTime = System.currentTimeMillis();
        elapsedTime = 0;
        startTimer();
    }

    // Method to check if the game is over
    public void checkGameOver() {
        gameOver = true;
        stopTimer();
        showGameOverPanel();
    }

    private void showGameOverPanel() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame == null) {
            System.err.println("topFrame is null. Ensure GamePanel is added to a JFrame before calling checkGameOver.");
            return;
        }
        gameOverPanel = new GameOverPanel(this);
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(screenWidth, screenHeight));
        gameOverPanel.setBounds(0, 0, screenWidth, screenHeight);
        layeredPane.add(this, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(gameOverPanel, JLayeredPane.PALETTE_LAYER);
        topFrame.setContentPane(layeredPane);
        topFrame.validate();
    }
}
